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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

/**
 * Interface for handling OTP received events.
 */
interface OtpReceivedListener {
    /**
     * Called when OTP is received from SMS.
     *
     * @param otp The OTP code extracted from SMS.
     */
    fun onOtpReceived(otp: String)

    /**
     * Called when OTP retrieval times out.
     */
    fun onOtpTimeout()
}

/**
 * Broadcast receiver for SMS Retriever API.
 * Replaces the legacy SmsListener class.
 */
class SmsRetrieverReceiver(
    private val listener: OtpReceivedListener?,
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != SmsRetriever.SMS_RETRIEVED_ACTION) {
            return
        }

        val extras = intent.extras ?: run {
            Log.w(TAG, "SMS Retriever intent has no extras")
            return
        }

        val status = extras.get(SmsRetriever.EXTRA_STATUS) as? Status ?: run {
            Log.w(TAG, "SMS Retriever intent has no status")
            return
        }

        when (status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as? String
                message?.let { smsMessage ->
                    val pattern = Pattern.compile("\\d{6}")
                    val matcher = pattern.matcher(smsMessage)
                    if (matcher.find()) {
                        val otp = matcher.group(0)
                        listener?.onOtpReceived(otp)
                    } else {
                        Log.w(TAG, "SMS message does not contain 6-digit OTP pattern: $smsMessage")
                    }
                } ?: run {
                    Log.w(TAG, "SMS Retriever message is null")
                }
            }
            CommonStatusCodes.TIMEOUT -> {
                Log.d(TAG, "SMS Retriever timeout")
                listener?.onOtpTimeout()
            }
            else -> {
                Log.w(TAG, "SMS Retriever status: ${status.statusCode}")
            }
        }
    }

    companion object {
        private const val TAG = "SmsRetrieverReceiver"
    }
}

/**
 * Helper class for managing SMS Retriever functionality.
 */
class SmsRetrieverHelper(private val context: Context) {

    private var receiver: SmsRetrieverReceiver? = null

    /**
     * Starts SMS retriever and registers broadcast receiver.
     *
     * @param listener The listener for OTP events.
     * @return IntentFilter for the receiver (to be used with LocalBroadcastManager or Activity.registerReceiver).
     */
    fun startSmsRetriever(listener: OtpReceivedListener): IntentFilter {
        receiver = SmsRetrieverReceiver(listener)

        val client = SmsRetriever.getClient(context)
        val task = client.startSmsRetriever()

        task.addOnSuccessListener {
            Log.d(TAG, "SMS Retriever started successfully")
        }

        task.addOnFailureListener { exception ->
            Log.e(TAG, "Failed to start SMS Retriever", exception)
        }

        return IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
    }

    /**
     * Gets the broadcast receiver instance.
     *
     * @return The broadcast receiver or null if not started.
     */
    fun getReceiver(): BroadcastReceiver? = receiver

    /**
     * Cleans up the receiver.
     */
    fun cleanup() {
        receiver = null
    }

    companion object {
        private const val TAG = "SmsRetrieverHelper"
    }
}

