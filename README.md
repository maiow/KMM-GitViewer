# KMM-GitViewer

Android client for GitHub personal account, with shared module for cross-platform code /Kotlin Multiplatform Mobile.
<br />It's my previous app https://github.com/maiow/GitTest migration to KMM. Goal was to move data layer (storage and logic of working with the network) to the shared module. 
<br />What's been changed:
+ SharedPrefs -> Multiplatform-Settings with platform realizations
+ Retrofit -> Ktor
+ Hilt -> Koin
+ added Napier for logging.
