import requests
from motor.motor_asyncio import AsyncIOMotorClient  # Async client for MongoDB
import os
import asyncio
import time

"""
This python file is designed to update coin price data in the mongodb cloud and runs on EC2 24hours.
If you want to clone this project, you will need to set up your own MongoDB cloud and AWS EC2 instance.
"""


class Binance:
    def __init__(self):
        self.base_endpoint = 'https://api.binance.com'
        self.rpm = 2400  # RPM (Request Per Minute): https://dev.binance.vision/t/binance-api-status-error-503/14553/3
        self.api_interval = 1/(self.rpm/60)

    def get_current_price(self, ticker):
        url = self.base_endpoint + "/api/v3/ticker/price"
        param = {
            "symbol": ticker
        }
        try:
            latest_prices = requests.get(url, params=param).json()
        except Exception as e:
            print(f"failed to fetch price : {ticker}, Error: {e}")
            return {}

        return [item for item in latest_prices if item['symbol'].endswith("USDT")]
    
    def get_tickers(self):
        url = self.base_endpoint + "/api/v3/exchangeInfo"
        try:
            info = requests.get(url).json()['symbols']
        except Exception as e:
            print(f"failed to fetch ticker, Error: {e}")
            return []

        return [i['symbol'] for i in info if i['symbol'].endswith("USDT")]
        

class Upbit:
    def __init__(self):
        self.base_endpoint = 'https://api.upbit.com'
        self.rpm = 1800  # RPM (Request Per Minute): https://docs.upbit.com/docs/user-request-guide
        self.api_interval = 1/(self.rpm/60)

    def get_current_price(self, ticker):
        url = self.base_endpoint + "/v1/ticker"
        header = {"Accept": "application/json"}
        param = {
            "markets": ticker
        }
        try:
            return requests.get(url, params=param, headers=header).json()[0]
        except Exception as e:
            print(f"failed to fetch price : {ticker}, Error: {e}")
            return {}
    
    def get_tickers(self):
        url = self.base_endpoint + "/v1/market/all?isDetails=false"
        headers = {"Accept": "application/json"}
        try:
            tickers = requests.get(url, headers=headers).json()
        except Exception as e:
            print(f"failed to fetch ticker, Error : {e}")
            return []
        return [t['market'] for t in tickers if t['market'].startswith("KRW-")]


class ExchangeRate:
    def __init__(self):
        self.base_endpoint = 'https://quotation-api-cdn.dunamu.com/v1/forex/recent'
        self.rpd = 2  # RPD (Request Per Day): Request Per Day
        self.api_interval = 1/(self.rpd/(24*60*60))

    def get_exchange_rate(self):
        param = {
            "codes": "FRX.KRWUSD",
        }
        try:
            return requests.get(url=self.base_endpoint, params=param).json()
        except Exception as e:
            print(f"failed to fetch ticker, Error : {e}")
            return {}


class MongoDB:
    def __init__(self):
        self.password = os.getenv("MONGO_DB_PASSWORD")
        self.connection_url = f'mongodb+srv://cointracker:{self.password}@cluster0.gulri.mongodb.net/MyDatabase?retryWrites=true&w=majority'
        self.client = AsyncIOMotorClient(self.connection_url)
        self.db = self.client['Coins']
        self.upbit_col = self.db['Upbit']
        self.binance_col = self.db['Binance']
        self.exchange_rate_col = self.db['ExchangeRate']
        self.rpm = 6000  # RPM (Request Per Minute): https://www.mongodb.com/docs/atlas/reference/free-shared-limitations/
        self.api_interval = 1/(self.rpm/60)

    async def renew_upbit_prices(self, upbit: Upbit):
        upbit_coins = upbit.get_tickers()
        for c in upbit_coins:
            price = upbit.get_current_price(c)
            await self.upbit_col.update_many({"coin": f"{c}"}, {'$set': price}, upsert=True)
            await asyncio.sleep(upbit.api_interval)

        collection = await self.upbit_col.find().to_list(length=None)
        collection = [d["coin"] for d in collection]
        delisted = set(collection) - set(upbit_coins)
        if delisted:
            print(f"상장폐지 코인 : {delisted}")
            for de in delisted:
                await self.upbit_col.delete_many({"coin": f"{de}"})

    async def renew_binance_prices(self, binance: Binance):
        prices = binance.get_current_price(None)
        await asyncio.sleep(binance.api_interval)
        for p in prices:
            p["coin"] = p["symbol"]

        for p in prices:
            await self.binance_col.update_many({"coin": f"{p['coin']}"}, {'$set': p}, upsert=True)
            await asyncio.sleep(self.api_interval)

        collection = await self.binance_col.find().to_list(length=None)
        collection = [d["coin"] for d in collection]
        binance_coins = [p['symbol'] for p in prices]
        delisted = set(collection) - set(binance_coins)
        if delisted:
            print(f"상장폐지 코인 : {delisted}")
            for de in delisted:
                await self.upbit_col.delete_many({"coin": f"{de}"})

    async def renew_exchange_rate(self, exchange_rate: ExchangeRate):
        data = exchange_rate.get_exchange_rate()[0]
        await self.exchange_rate_col.update_many({"data": data['currencyCode']}, {'$set': data}, upsert=True)
        await asyncio.sleep(exchange_rate.api_interval)


if __name__ == "__main__":
    binance = Binance()
    upbit = Upbit()
    exchange_rate = ExchangeRate()
    mongodb = MongoDB()

    async def update_upbit():
        while True:
            start_time = time.time()
            await mongodb.renew_upbit_prices(upbit=upbit)
            elapsed_time = time.time() - start_time
            print(f"Upbit updated in: {elapsed_time:.2f} seconds")

    async def update_binance():
        while True:
            start_time = time.time()
            await mongodb.renew_binance_prices(binance=binance)
            elapsed_time = time.time() - start_time
            print(f"Binance updated in: {elapsed_time:.2f} seconds")

    async def update_exchange_rate():
        while True:
            await mongodb.renew_exchange_rate(exchange_rate=exchange_rate)

    async def main():
        await asyncio.gather(
            update_upbit(),
            update_binance(),
            update_exchange_rate()
        )

    asyncio.run(main())

