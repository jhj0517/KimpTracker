from typing import List

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

    async def get_currencies(self) -> List:
        """Only returns KRW buy-able coins"""
        url = self.base_endpoint + "/v1/market/all?isDetails=false"
        headers = {"Accept": "application/json"}

        async with self.limiter:
            session = await self.get_session()
            try:
                response = await session.get(url, headers=headers)
                tickers = await response.json()
                return [t for t in tickers if t['market'].startswith("KRW-")]
            except Exception as e:
                print(f"Failed to fetch ticker. Error: {e}")
                return []

    async def get_current_price(self, ticker: str):
        """Returns price data for the ticker(symbol)"""
        url = self.base_endpoint + "/v1/ticker"
        headers = {"Accept": "application/json"}
        params = {"markets": ticker}

        async with self.limiter:
            session = await self.get_session()
            try:
                response = await session.get(url, params=params, headers=headers)
                return await response.json()
            except Exception as e:
                print(f"Failed to fetch price for {ticker}. Error: {e}")
                return {}