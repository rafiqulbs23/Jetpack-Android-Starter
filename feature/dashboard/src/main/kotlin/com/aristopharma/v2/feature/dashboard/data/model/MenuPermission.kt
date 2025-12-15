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

package com.aristopharma.v2.feature.dashboard.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Data model for menu permissions stored in the local database.
 *
 * @param id The unique identifier for the menu permission.
 * @param title The title/name of the menu item.
 * @param sequence The display sequence/order of the menu item.
 */
@Entity(tableName = "appMenuPermission")
@Serializable
data class MenuPermission(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    val title: String = "",
    val sequence: Int? = null,
)

