import requests
from pymongo import MongoClient
import os
import asyncio

"""
This python file is designed to update coin price data in the mongodb cloud and runs on EC2 24hours.
If you want to clone this project, you will need to set up your own MongoDB cloud and AWS EC2 instance.
"""


class Binance:
    def __init__(self) -> None:
        self.base_endpoint = 'https://api.binance.com'
        self.rpm = 2400  # Request Per Minute # https://dev.binance.vision/t/binance-api-status-error-503/14553/3
        self.api_interval = 1/(self.rpm/60)

    def get_current_price(self, ticker):
        url = self.base_endpoint + "/api/v3/ticker/price"
        param = {
            "symbol": ticker
        }
        try:
            return requests.get(url, params=param).json()
        except Exception as e:
            print(f"failed to fetch price : {ticker}, Error: {e}")
            return {}
    
    def get_tickers(self):
        url = self.base_endpoint + "/api/v3/exchangeInfo"
        tickers = []
        try:
            tickers = requests.get(url).json()['symbols']
        except Exception as e:
            print(f"failed to fetch ticker, Error: {e}")
            return {}

        return {s['symbol']: s for s in tickers if 'BUSD' in s['symbol'] and not 'USDT' in s['symbol'] and not 'USDC' in s['symbol']
        and not 'USDS' in s['symbol'] and not 'BTTBUSD' in s['symbol']}
        

class Upbit:
    def __init__(self) -> None:
        self.base_endpoint = 'https://api.upbit.com'
        self.rpm = 1800  # https://docs.upbit.com/docs/user-request-guide
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
        tickers = []
        try:
            tickers = requests.get(url, headers=headers).json()
        except Exception as e:
            print(f"failed to fetch ticker, Error : {e}")
            return {}

        for f in tickers:
            if(f['market']=='KRW-BTT'):
                f['market'] = 'KRW-BTTC'  # BTTC problem, see here : https://newsroompost.com/business/what-is-bttc-and-what-happened-to-btt-how-to-swap-bttold-read-here/5057305.html
        return {s['market']: s for s in tickers if 'KRW' in s['market']}
    

class ExchangeRate:
    def __init__(self) -> None:
        self.base_endpoint = 'https://quotation-api-cdn.dunamu.com/v1/forex/recent'
        self.rpd = 2  # Request Per Day
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
    def __init__(self) -> None:
        self.password = os.getenv('MONGO_DB_PASSWORD')
        self.connection_url = f'mongodb+srv://your_id_for_mongodb_cloud:{self.password}@cluster0.gulri.mongodb.net/MyDatabase?retryWrites=true&w=majority'
        self.client = MongoClient(self.connection_url)
        self.db = self.client['Coins']
        self.upbit = self.db['Upbit']
        self.binance = self.db['Binance']
        self.exchange_rate = self.db['ExchangeRate']

    async def renew_upbit_prices(self, upbit: Upbit):
        upbit_coins = upbit.get_tickers()
        for c in upbit_coins:
            price = upbit.get_current_price(c)
            self.upbit.update_many({"coin": f"{c}"}, {'$set': price}, upsert=True)
            await asyncio.sleep(upbit.api_interval)

    async def renew_binance_prices(self, binance: Binance):
        binance_coins = binance.get_tickers()
        for c in binance_coins:
            price = binance.get_current_price(c)
            self.binance.update_many({"coin": f"{c}"}, {'$set': price}, upsert=True)
            await asyncio.sleep(binance.api_interval)

    async def renew_exchange_rate(self, exchange_rate: ExchangeRate):
        data = exchange_rate.get_exchange_rate()[0]
        self.exchange_rate.update_many({"data": data['currencyCode']}, {'$set': data}, upsert=True)
        await asyncio.sleep(exchange_rate.api_interval)


if __name__ == "__main__":
    binance = Binance()
    upbit = Upbit()
    exchange_rate = ExchangeRate()
    mongodb = MongoDB()

    async def update_upbit():
        while True:
            await mongodb.renew_upbit_prices(upbit=upbit)

    async def update_binance():
        while True:
            await mongodb.renew_binance_prices(binance=binance)

    async def update_exchange_rate():
        while True:
            await mongodb.renew_exchange_rate(exchange_rate=exchange_rate)

    async def main():
        await asyncio.gather(update_upbit(), update_binance(), update_exchange_rate())

    asyncio.run(main())



