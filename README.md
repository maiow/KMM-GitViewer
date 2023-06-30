# KMM-GitViewer

Android client for GitHub personal account, with shared module for cross-platform code /Kotlin Multiplatform Mobile.
<br />It's my previous app https://github.com/maiow/GitTest migration to KMM. Goal was to move data layer (storage and logic of working with the network) to the shared module. 
<br />What's been changed:
+ SharedPrefs -> Multiplatform-Settings with platform realizations
+ Retrofit -> Ktor
+ Hilt -> Koin
+ added Napier for logging.

Splash Screen (Splash Screen API used). In-app checks of authorization token, and handling errors came from GitHub server as per tech reqs:
![studio64_bqE7zGHDep](https://github.com/maiow/KMM-GitViewer/assets/113892176/3b6ce3d1-bfe0-4bff-bf82-b454121a2e07)

Repositories list screen, click on item, repository info screen, logout:
![studio64_nBypvauZXQ](https://github.com/maiow/KMM-GitViewer/assets/113892176/800db70e-e51b-43fc-a05c-c9fe249ddc6e)

Handling No Readme case error from GitHub server:
![studio64_BmDzd99xiT](https://github.com/maiow/KMM-GitViewer/assets/113892176/6fd8d671-ed9c-4d91-929e-060421781b9d)

Lost Network connection case and retry:
![studio64_U3JkHSiaut](https://github.com/maiow/KMM-GitViewer/assets/113892176/a6765b86-c8ed-471a-9dd5-fc09c0ebf0e6)
