# STREAMY

This is an application that is used to list out movies

## Architecture and considerations

### Architecture: MVVM (Model-View-ViewModel)
That application makes use of the MVVM architecture which promotes separation of concerns and maintainability. The architecture consists of three main components:

- **Model:** Represents the data and business logic of the application.
- **View:** Displays the user interface and observes ViewModel changes.
- **ViewModel:** Manages the presentation logic, interacts with the Model, and exposes observable data to the View.

### Dependency Injection: Hilt
Hilt is used for dependency injection, ensuring a modular and testable codebase. It simplifies the process of providing dependencies to various parts of the application, such as the ViewModels and repositories.

## Third-Party Dependencies

Below is an exhaustive list of third-party dependencies used in this project along with their respective purposes:

- **Hilt:** Dependency injection library that simplifies the management of dependencies in the application.
- **Retrofit:** A type-safe HTTP client for making network requests.
- **OkHttp:** An HTTP client for efficient network operations.
- **Gson:** A library for serializing and deserializing JSON data.
- **Coroutine:** Provides asynchronous programming using coroutines for managing background tasks.
- **LiveData:** A data holder class that provides observable data to UI components.
- **JUnit Jupiter:** A testing framework for writing and running unit tests in Java and Kotlin.
- **MockK:** A mocking library for Kotlin that helps with creating mock objects for testing.
- **NetworkResponseAdapter:** This library provides a Kotlin Coroutines-based Retrofit call adapter for wrapping your API responses in a NetworkResponse sealed type.
- **Timber:** A highly extensible and customizable logging library for Android.

## Building the Project

1. Clone the repository: ` `
2. Open the project in Android Studio.
3. Build and run the project on an emulator or physical device.


