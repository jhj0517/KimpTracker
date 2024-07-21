from abc import ABC, abstractmethod, abstractproperty
from typing import Optional, List


class CryptoCurrencyMarketBase(ABC):
    def __init__(self,
                 base_endpoint: str,
                 rpm: Optional[int] = None):
        self.base_endpoint = base_endpoint
        self.rpm = rpm
        self.api_interval = None
        if self.rpm is not None:
            self.api_interval = 1 / (self.rpm / 60)

    @abstractmethod
    def get_currencies(self) -> List:
        """
        Get all currency codes from the platform

        :return: List of currency codes
        """
        pass

    @abstractmethod
    def get_current_price(self,
                          currency: str):
        """
        Get the latest price of the currency from the platform

        :param currency: Currency code
        :return: Price info data of the currency code from the platform
        """
        pass
