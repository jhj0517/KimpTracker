from typing import List, Optional
import requests
import time

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

    async def get_currencies(self) -> List:
        """Only returns USDT buy-able coins"""
        url = self.base_endpoint + "/api/v3/exchangeInfo"
        try:
            async with self.limiter:
                session = await self.get_session()
                response = await session.get(url)
                info = await response.json()['symbols']
                return [i['symbol'] for i in info if i['symbol'].endswith("USDT")]
        except Exception as e:
            print(f"failed to fetch ticker, Error: {e}")
            return []

    async def get_current_price(self,
                                currency: Optional[str] = None):
        """
        Get prices of the currencies from Binance. Only returns USDT buy-able currencies.

        :param currency: Currency code. Returns price of all currencies if it's None.
        :return: List of currency prices
        """
        url = self.base_endpoint + "/api/v3/ticker/price"
        param = {
            "symbol": currency
        }
        try:
            async with self.limiter:
                session = await self.get_session()
                response = await session.get(url, params=param)
                latest_prices = await response.json()

                usdt_prices = [item for item in latest_prices if item['symbol'].endswith("USDT")]

                timestamp = int(time.time() * 1000)
                for currency in usdt_prices:
                    currency["timestamp"] = timestamp

                return usdt_prices
        except Exception as e:
            print(f"failed to fetch price : {currency}, Error: {e}")
            return {}
