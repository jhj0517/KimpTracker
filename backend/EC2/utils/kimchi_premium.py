def calculate_kimchi_premium(
    binance_price: float,
    upbit_price: float,
    exchange_rate: float
):
    binance_price_krw = binance_price * exchange_rate

    kimchi_premium = ((upbit_price - binance_price_krw) / binance_price_krw) * 100

    return kimchi_premium
