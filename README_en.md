# KimpTracker
Get cryptocurrency prices of [Upbit](https://upbit.com/home) & [Binance](https://binance.com) and [Kimchi Premium](https://www.cnbc.com/2024/04/03/south-koreas-kimchi-premium-in-the-spotlight-after-btcs-record-highs.html#:~:text=The%20%22kimchi%20premium%22%20refers%20to,to%20make%20a%20quick%20buck.) between them.

https://github.com/jhj0517/KimpTracker/assets/97279763/5d1bdc06-6c9e-4ebf-b0e9-50c6786dbd44

# Architecture
![Flowcharts - Page 1](https://github.com/jhj0517/KimpTracker/assets/97279763/4c21fa15-ecb3-45a3-ae6e-e8b532c8326e)

## Overview

| Technology | Usage |
| ---------- | ----- |
| Retrofit | Handles network tasks |
| Room | Manages local database operations |
| Coroutine | Manages background tasks and makes the app more responsive |
| Hilt | Manages dependency injection |
| SharedFlow | Handles data sharing among multiple collectors |
| Widget | Widget that displays prices right on the home screen |
| MVVM (Model-View-ViewModel) | Separates the user interface logic from business logic |
| AWS Lambda | Handles serverless back-end processes |
| MongoDB Cloud | Used for cloud-based database management |

## What I learned

- Navigation between fragments without registering icons in the NavBottomBar, achieved through the use of `LiveData`. This expanded my understanding of Android navigation components.
- The correct use of `SharedFlow` to periodically update data via the REST API within the application. This allowed me to efficiently handle data sharing among multiple collectors in a reactive programming paradigm, improving the app's responsiveness and performance.
- What I need to do to make a widget in Android. I need to create `AppWidgetProvider` class and metadata `xml` for it.
- Using MongoDB Cloud for backend cloud-based database management. This gave me hands-on experience of setting up and managing a cloud-based MongoDB database.

## PlayStore
[![download on playstore](https://github.com/jhj0517/AIBridge/assets/97279763/6457404a-a4d9-4303-b614-f4a8e58c5b79)](https://play.google.com/store/apps/details?id=com.librarydevloperjo.cointracker&hl=ko&gl=US)
