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

package com.aristopharma.v2.feature.auth.data.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays

private const val TAG = "SignatureHelper"
private const val HASH_TYPE = "SHA-256"
private const val HASHED_BYTES = 9
private const val BASE64_CHAR = 11

/**
 * Helper class for generating app signature hash for SMS Retriever API.
 * Replaces the legacy HashstringGen class.
 */
class SignatureHelper(context: Context?) : ContextWrapper(context) {

    /**
     * Get all the app signatures for the current package.
     *
     * @return List of signature hashes.
     */
    val appSignature: List<String>
        get() {
            val appCodes = mutableListOf<String>()
            try {
                val myPackageName = packageName
                val myPackageManager = packageManager
                val signatures = myPackageManager.getPackageInfo(
                    myPackageName,
                    PackageManager.GET_SIGNATURES,
                ).signatures

                // For each signature create a compatible hash
                if (signatures != null) {
                    for (signature in signatures) {
                        val hash = hash(myPackageName, signature.toCharsString())
                        if (hash != null) {
                            appCodes.add(hash)
                        }
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.d(TAG, "Package not found", e)
            }
            return appCodes
        }

    companion object {
        /**
         * Creates a hash from package name and signature.
         *
         * @param pkgName The package name.
         * @param signature The signature string.
         * @return The hash string or null if hashing fails.
         */
        private fun hash(pkgName: String, signature: String): String? {
            val appInfo = "$pkgName $signature"
            return try {
                val messageDigest = MessageDigest.getInstance(HASH_TYPE)
                messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
                var myHashSignature = messageDigest.digest()
                // Truncated into HASHED_BYTES
                myHashSignature = Arrays.copyOfRange(myHashSignature, 0, HASHED_BYTES)
                // Encode into Base64
                var base64Hash = Base64.encodeToString(
                    myHashSignature,
                    Base64.NO_PADDING or Base64.NO_WRAP,
                )
                base64Hash = base64Hash.substring(0, BASE64_CHAR)
                Log.d(TAG, String.format("pkg: %s -- hash: %s", pkgName, base64Hash))
                base64Hash
            } catch (error: NoSuchAlgorithmException) {
                Log.e(TAG, "Algorithm not Found", error)
                null
            }
        }
    }
}

