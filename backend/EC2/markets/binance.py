from typing import List
import requests

from markets.crypto_currency_market_base import CryptoCurrencyMarketBase

# Binance API documentation : https://dev.binance.vision/t/binance-api-status-error-503/14553/3


class Binance(CryptoCurrencyMarketBase):
    def __init__(self,
                 base_endpoint: str = 'https://api.binance.com',
                 rpm: int = 2400
                 ):
        super().__init__(
            base_endpoint=base_endpoint,
            rpm=rpm
        )

    def get_currencies(self) -> List:
        url = self.base_endpoint + "/api/v3/exchangeInfo"
        try:
            info = requests.get(url).json()['symbols']
        except Exception as e:
            print(f"failed to fetch ticker, Error: {e}")
            return []

        return [i['symbol'] for i in info if i['symbol'].endswith("USDT")]

    def get_current_price(self,
                          ticker: str):
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