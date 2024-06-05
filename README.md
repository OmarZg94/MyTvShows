
# My TV Shows - Mobile Coding Sample

## Project Overview

The My Tv Shows mobile application is designed to showcase the capabilities of a clean, simple mobile app using the TVMaze API to display a television schedule of currently airing programs in the US. The application is built with the MVVM (Model-View-ViewModel) architecture to ensure a robust, flexible, and reusable codebase that can be easily expanded for future projects and customer requirements.

## Features

- **Television Schedule**: Displays a list of TV shows currently airing in the US.
- **Show Details**: Provides detailed metadata about a selected show, including its summary, genres, and airing time.
- **Date Picker**: Allows users to view the TV schedule for a specific date.
- **Search Functionality**: Enables users to search for shows or channels by name.
- **Segmented Schedule**: Differentiates between shows currently airing ("Now") and those scheduled to air next ("Next").

## Architecture

The application follows the MVVM architecture pattern, which separates the UI (View) from the business logic (ViewModel) and data (Model). This structure promotes a clean and maintainable codebase, making it easier to extend and test.

### Model

- **Episode**: Represents the TV show episodes fetched from the TVMaze API.
- **UiState**: Encapsulates the different states of the UI, such as Loading, Success, and Error.

### ViewModel

- **ShowViewModel**: Manages the app's data logic and prepares the data for the UI. It interacts with the TVMaze API to fetch the TV schedule and handles user actions like selecting a date or searching for a show.

### View

- **ShowListScreen**: Displays the list of TV shows currently airing.
- **ShowDetailContent**: Shows detailed information about a selected TV show.
- **SearchBar**: Provides a text input field for searching TV shows or channels.
- **DatePickerDialog**: Allows users to select a date to view the TV schedule.
- **LoadingIndicator**: Displays a loading spinner while data is being fetched.
- **SectionHeader**: Shows headers for different sections of the TV schedule.

## Libraries and Tools

- **Jetpack Compose**: Used for building the UI in a declarative manner.
- **Hilt**: Dependency injection to manage and provide dependencies throughout the application.
- **Retrofit**: HTTP client for making API requests to the TVMaze API.
- **OkHttp**: Network interceptor for logging and network call optimization.
- **Coil**: Image loading library for loading TV show images.

## Installation and Setup

1. **Clone the repository**:
    \`\`\`sh
    git clone <repository-url>
    \`\`\`

2. **Open the project in Android Studio**:
    - Open Android Studio.
    - Click on "Open an existing project".
    - Navigate to the cloned repository and select it.

3. **Build and Run**:
    - Connect an Android device or start an emulator.
    - Click on the "Run" button in Android Studio.

## Usage

- **View Current Schedule**: The app opens to the current TV schedule for the day.
- **Select Date**: Click on the calendar icon in the top bar to open a date picker and select a different date.
- **Search**: Use the search bar to filter TV shows or channels by name.
- **View Show Details**: Click on a show to view its detailed information in a bottom sheet dialog.

## Design Decisions

### MVVM Architecture

The MVVM architecture was chosen for its clear separation of concerns, which enhances code maintainability and testability. This pattern allows the ViewModel to handle business logic and data preparation, while the View focuses on rendering the UI, making it easier to manage complex applications.

### Jetpack Compose

Jetpack Compose was used for its modern, declarative approach to building UIs, which simplifies the process of creating and managing UI components. This choice aligns with contemporary Android development practices and ensures the app's UI is both flexible and efficient.

### Dependency Injection with Hilt

Hilt was implemented to manage dependencies, providing a clear and scalable way to handle objects' lifecycles and dependencies. This approach improves code reusability and testability.

### Network Layer with Retrofit and OkHttp

Retrofit and OkHttp were chosen for their efficiency and ease of use in handling network requests and responses. Retrofit simplifies the process of defining and consuming APIs, while OkHttp provides robust HTTP client capabilities, including logging and network interception.

### Image Loading with Coil

Coil was selected for its seamless integration with Jetpack Compose and efficient image loading capabilities. It ensures that images are loaded quickly and displayed smoothly within the app.

## Future Improvements

- **Pagination**: Implement pagination to handle large datasets efficiently.
- **Offline Support**: Add offline support to cache data and allow the app to function without an active internet connection.
- **Unit Tests**: Increase test coverage, particularly for ViewModel and Repository layers, to ensure robustness.
- **Additional Features**: Expand the app's functionality based on user feedback and new customer requirements.

## Conclusion

This project demonstrates a clean and efficient implementation of a TV schedule app using modern Android development practices. The codebase is designed to be correct, robust, flexible, reusable, and efficient, providing a strong foundation for future enhancements and customer projects.
