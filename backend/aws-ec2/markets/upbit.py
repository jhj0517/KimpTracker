from typing import List
import requests

from markets.crypto_currency_market_base import CryptoCurrencyMarketBase

# Upbit API Documentation : https://docs.upbit.com/docs/user-request-guide


class Upbit(CryptoCurrencyMarketBase):
    def __init__(self,
                 base_endpoint: str = 'https://api.upbit.com',
                 rpm: int = 1800):
        super().__init__(
            base_endpoint=base_endpoint,
            rpm=rpm
        )

    def get_currencies(self) -> List:
        """Only returns KRW buy-able coins"""
        url = self.base_endpoint + "/v1/market/all?isDetails=false"
        headers = {"Accept": "application/json"}
        try:
            tickers = requests.get(url, headers=headers).json()
        except Exception as e:
            print(f"failed to fetch ticker, Error : {e}")
            return []
        return [t for t in tickers if t['market'].startswith("KRW-")]

    def get_current_price(self,
                          ticker: str
                          ):
        """Returns price data for the ticker(symbol)"""
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