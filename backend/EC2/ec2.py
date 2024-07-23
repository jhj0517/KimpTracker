import requests
from motor.motor_asyncio import AsyncIOMotorClient  # Async client for MongoDB
import os
import asyncio
import time
from typing import Callable
import logging
from dotenv import load_dotenv

from markets import Binance, Upbit
from exchange_rate import FXRateAPI
from utils.kimchi_premium import calculate_kimchi_premium


# https://www.datensen.com/

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("mongo_db_currency_renewal")


class DBManager:
    def __init__(self,
                 connection_url: str):
        self.connection_url = connection_url
        self.client = AsyncIOMotorClient(self.connection_url)
        self.upbit_api = Upbit()
        self.binance_api = Binance()
        self.exchange_rate_api = FXRateAPI()
        self.db = self.client['Coins']
        self.write_rpm = 6000  # Write RPM (Request Per Minute) for free version: https://www.mongodb.com/docs/atlas/reference/free-shared-limitations/
        self.write_interval = 1/(self.write_rpm/60)

    async def renew_upbit_prices(self):
        api = self.upbit_api
        collection = self.db['Upbit']

        upbit_coins = api.get_currencies()
        symbols = []
        for currency in upbit_coins:
            symbol = currency["market"]
            symbols.append(symbol)
            price = api.get_current_price(symbol)

            data = {**currency, **price}

            await collection.update_one(
                {"market": {"$eq": symbol}},
                {"$set": data},
                upsert=True
            )
            await asyncio.sleep(api.api_interval)

        # Remove delisted symbols
        await collection.delete_many(
            {"market": {"$nin": symbols}}
        )

    async def renew_binance_prices(self):
        api = self.binance_api
        collection = self.db['Binance']

        all_price_data = api.get_current_price()
        await asyncio.sleep(api.api_interval)

        symbols = []
        for data in all_price_data:
            symbol = data["symbol"]
            symbols.append(symbol)

            await collection.update_one(
                {"symbol": {"$eq": symbol}},
                {"$set": data},
                upsert=True
            )
            await asyncio.sleep(api.api_interval)

        # Remove delisted symbols
        await collection.delete_many(
            {"market": {"$nin": symbols}}
        )

    async def renew_exchange_rate(self):
        api = self.exchange_rate_api
        collection = self.db['ExchangeRate']

        data = api.get_exchange_rate()
        await collection.update_one(
            {"base": {"$eq": "USD"}},
            {"$set": data},
            upsert=True
        )
        await asyncio.sleep(api.api_interval)

    async def renew_kimchi_premium(self):
        kp_collection = self.db['KimchiPremium']
        er_collection = self.db['ExchangeRate']
        binance_collection = self.db['Binance']
        upbit_collection = self.db["Upbit"]

        exchange_rate_data = await er_collection.find()
        timestamp = int(time.time() * 1000)

        upbit_coins = await upbit_collection.find()
        binance_coins = await binance_collection.find()

        binance_map = {coin['symbol'].replace('USDT', ''): coin for coin in binance_coins if coin['symbol'].endswith('USDT')}

        for upbit_data in upbit_coins:
            upbit_symbol = upbit_data["market"]

            if upbit_symbol.startswith("KRW-"):
                base_symbol = upbit_symbol.replace("KRW-", "")
                binance_data = binance_map[base_symbol]

                upbit_price = upbit_data["trade_price"]
                binance_price = binance_data["price"]
                exchange_rate = exchange_rate_data["rates"]["KRW"]
                kimchi_premium = calculate_kimchi_premium(binance_price, upbit_price, exchange_rate)

                data = {
                    "base_symbol": base_symbol,
                    "kimchi_premium": kimchi_premium,
                    "upbit_timestamp": upbit_data["timestamp"],
                    "binance_timestamp": binance_data["timestamp"],
                    "timestamp": timestamp,
                    "upbit_data": {
                        "symbol": upbit_data["market"],
                        "price": upbit_data["trade_price"],
                        "timestamp": upbit_data["timestamp"]
                    },
                    "binance_data": {
                        "symbol": binance_data["symbol"],
                        "price": binance_data["price"],
                        "timestamp": binance_data["timestamp"]
                    },
                    "exchange_rate": {
                        "usd_krw": exchange_rate,
                        "timestamp": exchange_rate_data["timestamp"]
                    }
                }

                await kp_collection.update_one(
                    {"base_symbol": {"$eq": base_symbol}},
                    {"$set": data},
                    upsert=True
                )
                await asyncio.sleep(api.api_interval)

async def update_schedule(
    func: Callable,
    name: str = ""
):
    while True:
        try:
            start_time = time.time()
            await func()
            elapsed_time = time.time() - start_time
            logger.info(f"{name} updated in: {elapsed_time:.2f} seconds")
        except Exception as e:
            logger.error(f"Error updating {name}: {str(e)}")
        await asyncio.sleep(1)


if __name__ == "__main__":
    load_dotenv()
    mongodb_cloud_url = os.getenv("MONGO_DB_URL")
    db_manager = DBManager(connection_url=mongodb_cloud_url)

    tasks = [
        update_schedule(
            func=db_manager.renew_upbit_prices,
            name="Ubpit"
        ),
        update_schedule(
            func=db_manager.renew_binance_prices,
            name="Binance"
        ),
        update_schedule(
            func=db_manager.renew_exchange_rate,
            name="Exchange Rate"
        )
    ]

    async def main():
        await asyncio.gather(*tasks)

    asyncio.run(main())

