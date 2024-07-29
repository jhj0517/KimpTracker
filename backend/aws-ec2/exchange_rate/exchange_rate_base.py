from abc import ABC, abstractmethod
from typing import Optional, List


class ExchangeRateBase(ABC):
    def __init__(self,
                 base_endpoint: str,
                 rpm: Optional[float] = None):
        self.base_endpoint = base_endpoint
        self.rpm = rpm
        self.api_interval = None
        if self.rpm is not None:
            self.api_interval = 1 / (self.rpm / 60)

    @abstractmethod
    def get_exchange_rate(self):
        """
        Get exchange rate data of the KRW-USD(T)

        :return: Exchange rate data
        """
        raise NotImplementedError
