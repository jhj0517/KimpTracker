# KimpTracker
[업비트](https://upbit.com/home), [바이낸스](https://binance.com), [FXRateAPI (환율)](https://fxratesapi.com/) 로 부터 계산한 김치 프리미엄 지수를 확인할 수 있는 앱입니다.

https://github.com/jhj0517/KimpTracker/assets/97279763/5d1bdc06-6c9e-4ebf-b0e9-50c6786dbd44

## Overview
![Flowcharts - Page 1](https://github.com/jhj0517/KimpTracker/assets/97279763/4c21fa15-ecb3-45a3-ae6e-e8b532c8326e)
> [!NOTE]
> 백엔드에 대한 구성은 [backend](https://github.com/jhj0517/KimpTracker/tree/master/backend) 폴더에서 확인할 수 있습니다.

## Key Feature

- 바이낸스, 업비트 암호화폐 최근 거래가 확인
- FXRateAPI 기준 최신 환율 확인 ( 1시간 단위 갱신 )
- 바이낸스 & 업비트 거래소에서 공통으로 존재하는 코인 간의 김치 프리미엄 확인
- 홈 화면 위젯을 통한 빠른 코인 가격 및 김치 프리미엄 확인
   > 📝 [기술 블로그 포스트: ForegroundService 로 끊임없이 업데이트 되는 홈화면 위젯 구현하기](https://medium.com/@developerjo0517/endlessly-updating-a-widget-at-short-intervals-on-android-ca29573d5243)

## Techstack

- `Network - Retrofit`
- `Local Database - Room`
- `Asynchronous - Coroutine, SharedFlow`
- `DI - Hilt`
- `UI - WidgetProvider`
- `Service - ForegroundService`
- `Architecture - MVVM (Model-View-ViewModel)`
- `Backend - AWS Lambda, AWS API Gateway, MongoDB Cloud`

## Folder Structure
```
KimpTracker
├── 📁 app                 // 안드로이드 앱 모듈
    ├── 📁 adapters        // 리사이클러 뷰 및 데이터 바인딩 어댑터 들
    ├── 📁 coinwidget      // 홈 화면 위젯 
    ├── 📁 data            // 데이터 레이어 
    ├── 📁 di              // DI 모듈
    ├── 📁 network         // 레트로핏 관련 스크립트
    ├── 📁 util            // Constant 및 유틸 클래스
    ├── 📁 viewmodels      // 뷰모델 레이어
    ├── 📁 views           // 뷰 레이어
├── 📁 backend             // 백엔드 
    ├── 📁 aws-ec2         // AWS EC2 업비트, 바이낸스 코인 가격 및 환율 데이터 갱신 스크립트
    ├── 📁 aws-lambda      // AWS 람다 함수 내 REST API 엔드포인트 로직
```
## PlayStore
[![download on playstore](https://github.com/jhj0517/AIBridge/assets/97279763/6457404a-a4d9-4303-b614-f4a8e58c5b79)](https://play.google.com/store/apps/details?id=com.librarydevloperjo.cointracker&hl=ko&gl=US)
