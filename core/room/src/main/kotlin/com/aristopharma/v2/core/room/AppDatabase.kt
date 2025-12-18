/*
 * Copyright 2025 Md. Rafiqul Islam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aristopharma.v2.core.room

/**
 * NOTE: AppDatabase has been removed to avoid circular dependencies.
 * 
 * Each feature module now has its own database:
 * - feature:dashboard has DashboardDatabase
 * - feature:attendance can have AttendanceDatabase
 * 
 * This follows the principle of feature isolation and prevents
 * circular dependencies between core and feature modules.
 * 
 * If you need a shared database, create it in the app module instead.
 */
