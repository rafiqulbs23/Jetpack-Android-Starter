# Dashboard Feature

## Overview

The Dashboard is the main hub of the application after login. It provides navigation to all features, displays summary information, and manages menu permissions based on user roles.

## Architecture

This feature follows Clean Architecture principles with three main layers:

### Data Layer
- **Models**: `DashboardMenuItem`, `DashboardSummary`, `MenuPermission`, `DashboardMenuType`
- **Data Sources**: 
  - `DashboardRemoteDataSource` - Remote API operations
  - `DashboardLocalDataSource` - Local database and preferences
- **Repository**: `DashboardRepositoryImpl` - Orchestrates data flow

### Domain Layer
- **Repository Interface**: `DashboardRepository` - Contract for data operations
- **Domain Models**: `DashboardState`, `DashboardEvent` - UI state and events

### Presentation Layer
- **ViewModel**: `DashboardViewModel` - Manages UI state and business logic
- **Screen**: `DashboardScreen` - Material3 Compose UI
- **Components**: 
  - `DashboardMenuItem` - Menu item card
  - `DashboardSummaryCard` - Summary information card

## Features

- **Dynamic Menu**: Menu items loaded from database based on user permissions
- **Summary Cards**: 
  - Employee ID and name
  - Attendance status (Checked In/Checked Out/Idle)
  - Last sync time and date
  - Post order count (resets daily)
- **Red Dot Indicators**: Shows pending order approvals for managers
- **Sync Functionality**: Manual sync button
- **Material3 Design**: Modern UI with Material3 components

## Menu Items

The dashboard displays menu items based on user permissions:

### Order Management
- **Draft Order** - View and manage draft orders
- **Post Order** - Create and submit new orders
- **Post Special Order** - Create special orders
- **Order History (User)** - View user's order history
- **Order History (Manager)** - View and approve orders (with red dot for pending approvals)

### Attendance
- **Start Your Day** - Check-in/check-out
- **Manager Live Location** - View team members' live locations
- **Attendance Report** - View attendance reports

### Leave Management
- **Leave Management** - Manager leave approval
- **Leave** - Employee leave requests

### Reports
- **Sales Summary Report** - Overall sales summary
- **Product Sales Report** - Product-wise sales
- **Chemist Sales Report** - Chemist-wise sales

## Usage

```kotlin
DashboardScreen(
    onMenuItemClick = { menuType ->
        // Navigate to appropriate screen
    },
    onNotificationClick = {
        // Navigate to notifications
    },
    onLogout = {
        // Handle logout
    },
    onShowSnackbar = { message, action, error ->
        // Show snackbar
    },
)
```

## Dependencies

- `core:ui` - UI utilities and components
- `core:android` - Android utilities
- `core:network` - Network utilities
- `core:preferences` - Preferences management
- `core:room` - Room database

## Future Enhancements

- Add more summary metrics (sales targets, achievements)
- Implement dashboard widgets
- Add quick actions shortcuts
- Enhance bar chart with real data
- Add filter/search for menu items
- Implement dashboard customization
- Add analytics and usage tracking

