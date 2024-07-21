from abc import ABC


class CryptoCurrencyMarketBase(ABC):
    def __init__(self):
        self.base_endpoint = None
        self.rpm = None
        self.api_interval = None

    @property
    def rpm(self):
        return self.rpm

    @rpm.setter
    def rpm(self, value):
        self.rpm = value
        if value is not None:
            self.api_interval = 1 / (value / 60)