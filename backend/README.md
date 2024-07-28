## KimpTracker Backend
각 거래소 (업비트, 바이낸스) 의 암호 화폐 가격 조회와 두 거래소 간의 김치프리미엄 지수를 확인하기 위한 KimpTracker의 백엔드입니다. 

## Architecture
![structure](https://github.com/user-attachments/assets/8a9f0f2b-a23c-4444-bd4b-7638835f4d87)


## Overview
### 프록시 서버
  1. AWS API Gateway + AWS Lambda 로 MongoDB 클라우드 조회를 위한 REST API 서버를 구축하였습니다.
  2. AWS Route53 에서 CNAME DNS Record 를 추가하여 API Gateway 와 매핑해 REST API 의 오리진으로 https://kimpapi.apibottle.com 을 사용하였습니다.
### 데이터 업데이트
1. AWS EC2 인스턴스에서 다음의 오픈 API 들이 허용하는 한도 내에서 최대한 짧은 인터벌로 데이터를 업데이트 하였습니다.
   1. [업비트 오픈 API](https://docs.upbit.com/reference/%EC%A0%84%EC%B2%B4-%EA%B3%84%EC%A2%8C-%EC%A1%B0%ED%9A%8C)
   2. [바이낸스 오픈 API](https://binance-docs.github.io/apidocs/spot/en/#introduction)
   3. [환율 오픈 API](https://fxratesapi.com/)

> [기술 블로그 포스트: AWS Lambda + AWS API Gateway 로 REST API 구성하기](https://medium.com/aws-tip/building-a-rest-api-with-aws-lambda-and-api-gateway-4582b3783fe5)


## Endpoints & API Documentation
4 종류의 엔드포인트가 있습니다.
| Endpoint | METHOD | Description |
|----------|--------|-------------|
| `/binance` | GET | 바이낸스 코인의 현재가를 조회합니다. USDT 거래쌍만 조회 가능합니다. |
| `/exchange-rate` | GET | USD 를 베이스로 여러 환율 데이터를 조회합니다. |
| `/upbit` | GET | 업비트 코인의 현재가를 조회합니다. KRW 거래쌍만 조회 가능합니다. |
| `/kimchi-premium` | GET | 업비트, 바이낸스 두 거래소에서 공통으로 존재하는 코인 간의 김치프리미엄 데이터를 조회합니다. |

### **API 문서**
각 엔드포인트의 쿼리 파라미터 및 예시 응답을 다음의 API 문서 링크에서 확인할 수 있습니다.
- https://jhj0517.github.io/kimptracker-swagger-ui/

[Swagger UI](https://github.com/swagger-api/swagger-ui)로 API 를 문서화 하였습니다. 깃허브 액션을 통해 [Github Pages](https://docs.github.com/en/pages/getting-started-with-github-pages/about-github-pages)로 무료 호스팅 중입니다.<br>

깃허브 액션으로 호스팅 중인 레포지토리: [jhj0517/kimptracker-swagger-ui](https://github.com/jhj0517/kimptracker-swagger-ui)

