import json
import os
from pymongo import MongoClient
from bson import json_util
from typing import Union, List, Dict

mongodb_uri = os.environ['mongodb_uri']
client = MongoClient(mongodb_uri)


def lambda_handler(event, context):
    try:
        path = event['path']
        params = event['queryStringParameters']

        db = client['Coins']
        upbit_collection = db['Upbit']
        binance_collection = db['Binance']
        exc_collection = db['ExchangeRate']
        kp_collection = db['KimchiPremium']

        if path.startswith("/upbit"):

            if params is None:
                result = list(upbit_collection.find({}, {'_id': 0}))

                return format_response(
                    200,
                    result
                )

            elif 'symbol' in params:
                symbol = params['symbol']

                result = list(upbit_collection.find({
                    "market": {"$eq": symbol},
                }, {'_id': 0}))

                if not result:
                    return format_response(
                        400,
                        {"message": "No symbol has found"}
                    )

                return format_response(
                    200,
                    result
                )

        elif path.startswith("/binance"):

            if params is None:
                result = list(binance_collection.find({}, {'_id': 0}))

                return format_response(
                    200,
                    result
                )

            elif 'symbol' in params:
                symbol = params['symbol']

                result = list(binance_collection.find({
                    "symbol": {"$eq": symbol},
                }, {'_id': 0}))

                if not result:
                    return format_response(
                        400,
                        {"message": "No symbol has found"}
                    )

                return format_response(
                    200,
                    result
                )

        elif path.startswith("/exchange-rate"):

            result = list(exc_collection.find({}, {'_id': 0}))

            return format_response(
                200,
                result
            )

        elif path.startswith("/kimchi-premium"):

            if params is None:
                result = list(kp_collection.find({}, {'_id': 0}))

                return format_response(
                    200,
                    result
                )

            else:
                query = {}

                if 'base_symbol' in params:
                    query['base_symbol'] = {"$eq": params['base_symbol']}
                elif 'upbit_symbol' in params:
                    query['upbit_data.symbol'] = {"$eq": params['upbit_symbol']}
                elif 'binance_symbol' in params:
                    query['binance_data.symbol'] = {"$eq": params['binance_symbol']}
                else:
                    return format_response(
                        400,
                        {"message": "Invalid parameters. Use 'base_symbol', 'upbit_symbol', or 'binance_symbol'."}
                    )

                result = list(kp_collection.find(query, {'_id': 0}))

                if not result:
                    return format_response(
                        400,
                        {"message": "No symbol has found"}
                    )

                return format_response(
                    200,
                    result
                )

        else:
            return format_response(
                404,
                {"message": "Resource not found"}
            )

    except Exception as e:
        print(f"An error occurred: {str(e)}")
        return format_response(
            500,
            {"message": "Internal server error"}
        )


def format_response(status_code: int, response_body: Union[List, Dict]) -> Dict:
    return {
        'statusCode': status_code,
        'headers': {
            'Content-Type': 'application/json',
        },
        'body': json.dumps(response_body, default=json_util.default)
    }