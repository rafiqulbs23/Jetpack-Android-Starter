# Notification Feature

## Overview

The Notification feature handles displaying notifications and notices from the backend. It provides a Material3 Compose UI for viewing all notifications in a list format.

## Architecture

This feature follows clean architecture principles with three layers:

### Data Layer
- **Models**: `NoticeModel` - Data transfer objects for notifications
- **API Service**: `NotificationApiService` - Retrofit interface for API calls
- **Remote Data Source**: `NotificationRemoteDataSource` - Interface and implementation for remote data access
- **Repository**: `NotificationRepositoryImpl` - Implementation of the domain repository

### Domain Layer
- **Repository Interface**: `NotificationRepository` - Contract for notification operations
- **Models**: `NotificationState`, `NotificationEvent` - Domain models for state management

### Presentation Layer
- **ViewModel**: `NotificationViewModel` - Manages UI state and business logic
- **Screen**: `NotificationScreen` - Material3 Compose UI for displaying notifications
- **Navigation**: `NotificationRoute` - Navigation route for the notification screen

## Features

- **Notification List**: Display all notifications in a scrollable list
- **Material3 Design**: Modern Material3 UI components
- **Error Handling**: Comprehensive error handling with user-friendly messages
- **Loading States**: Loading indicators during data fetching
- **Empty State**: User-friendly empty state when no notifications are available

## API Integration

- **Endpoint**: `GET /api/v1/broadcast/all-notification`
- **Query Parameter**: `Emp_Id` - Employee ID
- **Response**: `BaseResponse<List<NoticeModel>?>`

## Usage

### Navigation

Add the notification route to your navigation graph:

```kotlin
composable<NotificationDestination> {
    NotificationRoute(
        onShowSnackbar = onShowSnackbar,
    )
}
```

### ViewModel

The ViewModel automatically loads notifications when the screen is displayed. It uses the employee ID from user preferences.

## Dependencies

- `core:ui` - UI utilities and components
- `core:android` - Android utilities
- `core:preferences` - User preferences data source
- `core:network` - Network utilities
- `firebase:auth` - Firebase authentication (for user context)

## Migration from Legacy Code

This module replaces:
- `NotificationActivity` → `NotificationScreen`
- `NoticeBoardFragment` → Integrated into `NotificationScreen`
- `NoticeRecyclerViewAdapter` → Material3 Compose `LazyColumn` with items
- `NotificationRecyclerViewAdapter` → Not used (legacy code)

## Future Enhancements

- Notification categories and filtering
- Mark as read/unread functionality
- Notification details screen
- Push notification integration
- Notification preferences
- Search functionality
- Pagination for large notification lists

