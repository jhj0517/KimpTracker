## KimpTracker Backend
각 거래소 (업비트, 바이낸스) 의 암호 화폐 가격 조회와 두 거래소 간의 김치프리미엄 지수를 확인하기 위한 KimpTracker의 백엔드입니다. 

## Architecture
![architecture](https://github.com/user-attachments/assets/f693ee73-ec27-4d27-ade1-8ffbbeae61aa)

## Overview
### 프록시 서버
  1. AWS API Gateway + AWS Lambda 로 MongoDB 클라우드 조회를 위한 REST API 서버를 구성하였습니다.
  2. AWS Route53 에서 CNAME DNS Record 를 추가하여 API Gateway 와 매핑해 REST API 의 오리진으로 https://kimpapi.apibottle.com 을 사용하였습니다.
### 데이터 업데이트
1. AWS EC2 인스턴스에서 각 오픈 API 들이 허용하는 RPM (Requests Per Minute) 내에서 최대한 짧은 인터벌로 데이터를 업데이트 하였습니다.
   1. [업비트 오픈 API](https://docs.upbit.com/reference/%EC%A0%84%EC%B2%B4-%EA%B3%84%EC%A2%8C-%EC%A1%B0%ED%9A%8C) : 1800 RPM  
   2. [바이낸스 오픈 API](https://binance-docs.github.io/apidocs/spot/en/#introduction) : 2400 RPM 
   3. [환율 오픈 API](https://fxratesapi.com/) : 0.5 RPM 

> [📝 기술 블로그 포스트: AWS Lambda + AWS API Gateway 로 REST API 구성하기](https://medium.com/aws-tip/building-a-rest-api-with-aws-lambda-and-api-gateway-4582b3783fe5)


## API Documentation
[Swagger UI](https://github.com/swagger-api/swagger-ui)로 API 를 문서화 하였습니다. 
- https://jhj0517.github.io/kimptracker-swagger-ui/

[jhj0517/kimptracker-swagger-ui](https://github.com/jhj0517/kimptracker-swagger-ui) 에서 깃허브 액션을 통해 [Github Pages](https://docs.github.com/en/pages/getting-started-with-github-pages/about-github-pages)로 무료 호스팅 중입니다.<br>
