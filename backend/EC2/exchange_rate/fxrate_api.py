import requests

from exchange_rate.exchange_rate_base import ExchangeRateBase

# FXRateAPI documentation : https://fxratesapi.com/docs


class FXRateAPI(ExchangeRateBase):
    def __init__(self,
                 base_endpoint: str = "https://api.fxratesapi.com/latest",
                 rpd: int = 24):
        super().__init__(
            base_endpoint=base_endpoint,
            rpd=rpd
        )

    def get_exchange_rate(self):
        param = {
            "base": "USD",
            "currencies": None,  # Currency code like "KRW". Get all currencies by default.
            "resolution": "1h",
            "places": str(6)
        }
        try:
            result = requests.get(url=self.base_endpoint, params=param).json()
            result["platform"] = "fxratesapi"
            return result
        except Exception as e:
            print(f"failed to fetch ticker, Error : {e}")
            return {}
