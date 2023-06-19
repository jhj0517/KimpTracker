# KimpTracker
This is an practice Android project that provides cryptocurrency prices and Kimchi-Premium.

## Demo Video
https://github.com/jhj0517/KimpTracker/assets/97279763/d4a89977-5862-4f22-8631-461352f082e6

## Feature
- Provide cryptocurrency prices from different platforms and get KimChi-Premium based on the prices, in the form of `RecycleView`.
- Place widget on home screen so users can see prices/Kimch-Premium directly on home screen without opening app

## Technologies

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
The app is available on the PlayStore. If you want to see how it works, you can download it here
