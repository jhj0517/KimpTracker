from abc import ABC, abstractmethod, abstractproperty
from typing import Optional, List
import aiohttp
import asyncio
from aiolimiter import AsyncLimiter


class CryptoCurrencyMarketBase(ABC):
    def __init__(self,
                 base_endpoint: str,
                 rpm: int):
        self.base_endpoint = base_endpoint
        self.rpm = rpm
        self.session = None
        self.limiter = AsyncLimiter(self.rpm, 60)

    async def get_session(self):
        if self.session is None:
            self.session = aiohttp.ClientSession()
        return self.session

    async def close(self):
        if self.session:
            await self.session.close()
            self.session = None

    @abstractmethod
    async def get_currencies(self) -> List:
        """
        Get all currency codes from the platform

        :return: List of currency codes
        """
        raise NotImplementedError

    @abstractmethod
    async def get_current_price(self,
                          currency: Optional[str]):
        """
        Get the latest price of the currency from the platform

        :param currency: Currency code
        :return: Price info data of the currency code from the platform
        """
        raise NotImplementedError
