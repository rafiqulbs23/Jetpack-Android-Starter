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

package com.aristopharma.v2.feature.auth.presentation.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Helper class to generate app signature hash for SMS Retriever API.
 * 
 * The SMS Retriever API requires the SMS message to contain an 11-character hash
 * that identifies your app. This hash is generated from your app's package name
 * and signing certificate.
 */
class AppSignatureHelperNew(private val context: Context) {

    companion object {
        private const val TAG = "AppSignatureHelper"
        private const val HASH_TYPE = "SHA-256"
        private const val NUM_HASHED_BYTES = 9
        private const val NUM_BASE64_CHAR = 11
    }

    /**
     * Get the app signature hash.
     * 
     * @return The 11-character hash string, or empty string if unable to generate.
     */
    fun getAppSignature(): String {
        val appSignatures = getAppSignatures()
        return if (appSignatures.isNotEmpty()) {
            appSignatures[0]
        } else {
            ""
        }
    }

    /**
     * Get all app signature hashes.
     * 
     * @return List of signature hashes.
     */
    private fun getAppSignatures(): List<String> {
        val appSignatureHashList = ArrayList<String>()

        try {
            // Get all package signatures for the current package
            val packageName = context.packageName
            val packageManager = context.packageManager
            val signatures = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                val signingInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                ).signingInfo
                
                if (signingInfo?.hasMultipleSigners() ?: false) {
                    signingInfo.apkContentsSigners
                } else {
                    signingInfo?.signingCertificateHistory
                }
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                ).signatures
            }

            // For each signature, create a compatible hash
            if (signatures != null) {
                for (signature in signatures) {
                    val hash = hash(packageName, signature.toByteArray())
                    if (hash != null) {
                        appSignatureHashList.add(String.format("%s", hash))
                        Log.d(TAG, "App Signature Hash: $hash")
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Package not found", e)
        }

        return appSignatureHashList
    }

    /**
     * Generate hash from package name and signature.
     */
    private fun hash(packageName: String, signature: ByteArray): String? {
        val appInfo = "$packageName ${String(signature, Charsets.ISO_8859_1)}"
        try {
            val messageDigest = MessageDigest.getInstance(HASH_TYPE)
            messageDigest.update(appInfo.toByteArray(Charsets.UTF_8))
            var hashSignature = messageDigest.digest()

            // Truncate hash to NUM_HASHED_BYTES bytes
            hashSignature = hashSignature.copyOfRange(0, NUM_HASHED_BYTES)
            
            // Encode in Base64
            var base64Hash = Base64.encodeToString(
                hashSignature,
                Base64.NO_PADDING or Base64.NO_WRAP
            )
            
            // Remove trailing characters to get exactly NUM_BASE64_CHAR characters
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)

            Log.d(TAG, "Package: $packageName -- Hash: $base64Hash")
            return base64Hash
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "No Such Algorithm Exception", e)
        }

        return null
    }

    /**
     * Log all app signatures for debugging purposes.
     */
    fun logAppSignatures() {
        val appSignatures = getAppSignatures()
        Log.d(TAG, "App Signatures:")
        for (signature in appSignatures) {
            Log.d(TAG, "  - $signature")
        }
    }
}
