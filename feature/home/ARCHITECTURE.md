# Home Feature - Clean Architecture

This feature follows Clean Architecture principles with clear separation of concerns across three layers.

## Architecture Layers

```
feature/home/
├── domain/           # Business logic layer (no Android dependencies)
│   ├── model/        # Domain models (pure Kotlin)
│   ├── repository/   # Repository interfaces (contracts)
│   └── usecase/      # Use cases (business logic)
├── data/             # Data layer (Android dependencies allowed)
│   ├── mapper/       # Data mappers (domain ↔ data models)
│   └── repository/   # Repository implementations
├── ui/               # Presentation layer
│   ├── home/         # Home screen UI and ViewModel
│   └── item/         # Item screen UI and ViewModel
└── di/               # Dependency injection modules
```

## Layer Responsibilities

### Domain Layer
- **Pure Kotlin** - No Android dependencies
- Contains **business logic** and **use cases**
- Defines **repository interfaces** (contracts)
- Contains **domain models** (business entities)

### Data Layer
- Implements **repository interfaces** from domain layer
- Handles **data sources** (local, remote)
- Contains **mappers** for converting between domain and data models
- Can have Android dependencies

### UI Layer
- Contains **ViewModels** that use **use cases**
- Contains **Composables** for UI
- Observes **domain models** through use cases

## Data Flow

```
UI (ViewModel) 
  → Use Case (Domain)
    → Repository Interface (Domain)
      → Repository Implementation (Data)
        → Data Sources (Local/Remote)
```

## Key Benefits

1. **Testability**: Domain layer can be tested without Android dependencies
2. **Maintainability**: Clear separation makes code easier to understand and modify
3. **Scalability**: Easy to add new features following the same pattern
4. **Reusability**: Domain logic can be reused across different platforms

## Usage Example

```kotlin
// ViewModel uses Use Cases
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getJetpacksUseCase: GetJetpacksUseCase,
    private val deleteJetpackUseCase: DeleteJetpackUseCase,
) : ViewModel() {
    // Use cases encapsulate business logic
    fun getJetpacks() {
        getJetpacksUseCase()
            .collect { jetpacks -> /* update UI */ }
    }
}
```


