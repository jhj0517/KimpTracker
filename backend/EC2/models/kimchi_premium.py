from pydantic import BaseModel


class ExchangeRate(BaseModel):
    usd_krw: float
    timestamp: int


class Market(BaseModel):
    symbol: str
    price: float
    timestamp: int


class KimchiPremium(BaseModel):
    base_symbol: str
    kimchi_premium: float
    korean_name: str
    english_name: str
    timestamp: int
    upbit_data: Market
    binance_data: Market
    exchange_rate: ExchangeRate
