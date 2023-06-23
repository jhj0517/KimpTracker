import requests
import time
from pymongo import MongoClient
import schedule

"""
This python file is designed to refresh coin price data in the mongodb cloud, and is running on EC2 24hours.
If you want to clone this project, you will need to set up your own MongoDB Cloud and AWS EC2 instance.
"""


class Binance:
    def __init__(self) -> None:
        self.base_endpoint = 'https://api.binance.com'
        self.api_interval = 60

    def getCurrentPrice(self, ticker):
        url = self.base_endpoint + "/api/v3/ticker/price"
        param = {
            "symbol":ticker
        }
        return requests.get(url,params=param).json()
    
    def getTickers(self):
        url = self.base_endpoint + "/api/v3/exchangeInfo"
        full_data = requests.get(url).json()['symbols']
        return {s['symbol']: s for s in full_data if 'BUSD' in s['symbol'] and not 'USDT' in s['symbol'] and not 'USDC' in s['symbol'] 
        and not 'USDS' in s['symbol'] and not 'BTTBUSD' in s['symbol']}
        

class Upbit:
    def __init__(self) -> None:
        self.base_endpoint = 'https://api.upbit.com'
        self.api_interval = 60

    def getCurrentPrice(self, ticker):
        url = self.base_endpoint + "/v1/ticker"
        header = {"Accept": "application/json"}
        param = {
            "markets":ticker
        }
        return requests.get(url,params=param ,headers=header).json()[0]
    
    def getTickers(self):
        url = self.base_endpoint + "v1/market/all?isDetails=false"
        headers = {"Accept": "application/json"}
        fulldata = requests.get(url,headers=headers).json()
        for f in fulldata: 
            if(f['market']=='KRW-BTT'):
                f['market'] = 'KRW-BTTC' # BTTC problem , see here : https://newsroompost.com/business/what-is-bttc-and-what-happened-to-btt-how-to-swap-bttold-read-here/5057305.html
        return {s['market']: s for s in fulldata if 'KRW' in s['market']}
    

class ExchangeRate:
    def __init__(self) -> None:
        self.base_endpoint = 'https://quotation-api-cdn.dunamu.com/v1/forex/recent'
        self.api_interval = 60*60*12

    def getExchangeRate():
        url = 'https://quotation-api-cdn.dunamu.com/v1/forex/recent'
        param = {
            "codes" : "FRX.KRWUSD",
        }
        return requests.get(url=url,params=param).json()    


class MongoDB:
    def __init__(self) -> None:
        self.password = 'your_password_for_mongodb_cloud'
        self.connection_url = f'mongodb+srv://your_id_for_mongodb_cloud:{self.password}@cluster0.gulri.mongodb.net/MyDatabase?retryWrites=true&w=majority'
        self.client = MongoClient(self.connection_url)
        self.db = self.client['Coins']
        self.upbit = self.db['Upbit']
        self.binance = self.db['Binance']
        self.exchange_rate = self.db['ExchangeRate']
        self.api_interval = 0.01

    def renewUpbitPrices(self, upbit:Upbit):
        upbit_coins = upbit.getTickers()
        for c in upbit_coins:
            price = upbit.getCurrentPrice(c)
            self.upbit.update_many({"coin":f"{c}"},{'$set':price},upsert=True)
            time.sleep(self.api_interval)

    def renewBinancePrices(self, binance:Binance):
        binance_coins = binance.getTickers()
        for c in binance_coins:
            price = binance.getCurrentPrice(c)
            self.binance.update_many({"coin":f"{c}"},{'$set':price},upsert=True)
            time.sleep(self.api_interval)

    def renewExchangeRate(self, exchange_rate:ExchangeRate):
        data = exchange_rate.getExchangeRate()[0]
        self.exchange_rate.update_many({"data":data['currencyCode']},{'$set':data},upsert=True)

if __name__ == "__main__":
    binance = Binance()
    upbit = Upbit()
    exchange_rate = ExchangeRate()
    mongodb = MongoDB()

    def job_mongodb_renewUpbitPrices():
        mongodb.renewUpbitPrices(upbit=upbit)

    def job_mongodb_renewBinancePrices():
        mongodb.renewBinancePrices(binance=binance)

    def job_mongodb_renewExchangeRate():
        mongodb.renewExchangeRate(exchange_rate=exchange_rate)
    
    schedule.every(upbit.api_interval).minutes.do(job_mongodb_renewUpbitPrices)
    schedule.every(binance.api_interval).minutes.do(job_mongodb_renewBinancePrices)
    schedule.every(exchange_rate.api_interval).hours.do(job_mongodb_renewExchangeRate)

    while True:
        schedule.run_pending()
        time.sleep(1)


