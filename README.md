# Movies

![launcherIcon](https://github.com/eveey/Movies/blob/master/app/src/main/assets/ic_launcher_web_hi_res.png)

Movies are my thing.

Sample Android app in [Kotlin](https://kotlinlang.org/) for viewing movies that are now playing in theaters from [The Movie DB](https://www.themoviedb.org/).

## Building the app
* Gradle build tools
* Requires [Java 8](https://java.com/en/download/faq/java8.xml)

`Important note: to use the services you need to add your movie DB API KEY to project local.properties file like this:
 apiKey="<your_api_key>"`

## Architecture
* MVVM (Model-View-ViewModel)
* [Android Arhitecture Components](https://developer.android.com/topic/libraries/architecture/)
* [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData)
* [RxKotlin](https://github.com/ReactiveX/RxKotlin)

## Dependency management
* [Google/Dagger](https://github.com/google/dagger) - Dependency injection

## Network
* [Square/Retrofit](https://github.com/square/retrofit) - HTTP RESTful connections
* [OkHttp 3](https://square.github.io/okhttp/3.x/okhttp/)
* [Square/Moshi](https://github.com/square/moshi) - JSON adapter

## Unit tests
* [JUnit4](https://junit.org/junit4/)
* [Mockito-Kotlin](https://github.com/nhaarman/mockito-kotlin)

## Other
### Image loading
* [Glide](https://github.com/bumptech/glide) - 
An image loading and caching library for Android focused on smooth scrolling
### Logging
* [Timber](https://github.com/JakeWharton/timber)
### Code quality
* [Ktlint](https://ktlint.github.io/) - An anti-bikeshedding Kotlin linter with built-in formatter
* [Detekt](https://github.com/arturbosch/detekt) - Static code analysis for Kotlin

## Roadmap:
* [Espresso](https://developer.android.com/training/testing/espresso/) - UI testing framework
* [ProGuard](https://www.guardsquare.com/en/products/proguard) - Code obfuscation
* [CircleCI](https://circleci.com/) - Continuous integration
* [Firebase](https://firebase.google.com/) - Analytics

## Wunderlist:
* [Android Jetpack/Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) - Android navigation framework

## Disclaimer:
```PERSONAL PROJECT - NOT INTENDED FOR COMMERCIAL USE```

