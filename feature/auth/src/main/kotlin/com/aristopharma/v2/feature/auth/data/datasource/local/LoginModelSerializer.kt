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

package com.aristopharma.v2.feature.auth.data.datasource.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.aristopharma.v2.feature.auth.data.model.LoginModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/**
 * Serializer implementation for serializing and deserializing [LoginModel] objects.
 */
object LoginModelSerializer : Serializer<LoginModel> {

    /**
     * The default value of [LoginModel] to be used when deserialization fails.
     */
    override val defaultValue: LoginModel = LoginModel()

    /**
     * Json instance configured for serialization with forward compatibility.
     */
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    /**
     * Reads a [LoginModel] object from the provided [InputStream].
     *
     * @param input The input stream to read data from.
     * @return The deserialized [LoginModel] object.
     * @throws CorruptionException if there's an issue with deserialization.
     */
    override suspend fun readFrom(input: InputStream): LoginModel {
        return try {
            val bytes = input.readBytes()
            if (bytes.isEmpty()) {
                defaultValue
            } else {
                json.decodeFromString(
                    deserializer = LoginModel.serializer(),
                    string = bytes.decodeToString(),
                )
            }
        } catch (e: SerializationException) {
            throw CorruptionException("Unable to read LoginModel", e)
        } catch (e: Exception) {
            throw CorruptionException("Unexpected error reading LoginModel", e)
        }
    }

    /**
     * Writes a [LoginModel] object to the provided [OutputStream].
     *
     * @param t The [LoginModel] object to be serialized.
     * @param output The output stream to write data to.
     */
    override suspend fun writeTo(t: LoginModel, output: OutputStream) {
        withContext(Dispatchers.IO) {
            try {
                val jsonString = json.encodeToString(
                    serializer = LoginModel.serializer(),
                    value = t,
                )
                output.write(jsonString.encodeToByteArray())
            } catch (e: Exception) {
                throw CorruptionException("Unable to write LoginModel", e)
            }
        }
    }
}
