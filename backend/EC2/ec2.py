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
        for currency in upbit_coins:
            price = api.get_current_price(currency)
            await collection.update_many(
                {"coin": currency},
                {'$set': price},
                upsert=True
            )
            await asyncio.sleep(api.api_interval)

    async def renew_binance_prices(self):
        api = self.binance_api
        collection = self.db['Binance']

        prices = api.get_current_price(None)
        await asyncio.sleep(api.api_interval)
        for p in prices:
            p["coin"] = p["symbol"]

        for p in prices:
            await collection.update_many(
                {"coin": f"{p['coin']}"},
                {'$set': p},
                upsert=True
            )
            await asyncio.sleep(api.api_interval)

    async def renew_exchange_rate(self):
        api = self.exchange_rate_api
        collection = self.db['ExchangeRate']

        data = api.get_exchange_rate()
        await collection.update_one(
            {"base": {"$eq": "USD"}},
            {'$set': data},
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

