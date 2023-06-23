import json
import os
from pymongo import MongoClient
from bson import json_util

mongodb_uri = os.environ['mongodb_uri']
client= MongoClient(mongodb_uri)

def lambda_handler(event, context):
    db = client['Coins']
    col_Upbit = db['Upbit']
    col_Binance = db['Binance']
    col_ExcRate = db['ExchangeRate']
    upbit_coins = col_Upbit.find()
    binance_coins = col_Binance.find()
    exc = col_ExcRate.find()
     
    return {
        'statusCode': 200,
        'body': json.dumps({
            "result":{
                "upbit":parse_json(upbit_coins),
                "binance":parse_json(binance_coins),
                "exc":parse_json(exc) 
            }
        })
    }
    
def parse_json(data):
    return json.loads(json_util.dumps(data))    
    
