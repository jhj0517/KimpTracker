# KimpTracker
This is an practice Android project that provides cryptocurrency prices and Kimchi-Premium.

## Demo Video
https://github.com/jhj0517/KimpTracker/assets/97279763/5d1bdc06-6c9e-4ebf-b0e9-50c6786dbd44

## Feature
- Provide cryptocurrency prices from different platforms and get KimChi-Premium based on the prices, in the form of `RecycleView`.

  **This project has its own backend to store cryptocurrency price data, so if you want to clone this project, you need to set up your own MongoDB cloud and AWS. Check the `backend` folder to see what I've done in the backend!**
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
The app is available on the PlayStore. If you want to see how it works, you can check it [here](https://play.google.com/store/apps/details?id=com.librarydevloperjo.cointracker&hl=ko&gl=US).
