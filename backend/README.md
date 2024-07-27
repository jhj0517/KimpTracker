## KimpTracker Backend
KimpTracker의 백엔드입니다. 

## Structure
![structure](https://github.com/user-attachments/assets/8a9f0f2b-a23c-4444-bd4b-7638835f4d87)




## Overview
- **프록시 서버**
  1. AWS API Gateway + AWS Lambda 로 MongoDB 클라우드 조회를 위한 REST API 서버를 구축하였습니다.
  2. AWS Route53 에서 CNAME DNS Record 를 추가하여 REST API 의 오리진으로 https://kimpapi.apibottle.com 을 사용하였습니다.
- **데이터 갱신**
  1. [업비트 오픈 API](https://docs.upbit.com/reference/%EC%A0%84%EC%B2%B4-%EA%B3%84%EC%A2%8C-%EC%A1%B0%ED%9A%8C), [바이낸스 오픈 API](https://binance-docs.github.io/apidocs/spot/en/#introduction), [환율 오픈 API](https://fxratesapi.com/) 가 허용하는 한도 내에서 최대한 짧은 인터벌로 코인 가격 및 환율 데이터를 갱신하였습니다.

미디엄 포스트 링크

## Endpoints & API Documentation
4 종류의 엔드포인트가 있습니다.

+ API 문서 링크
