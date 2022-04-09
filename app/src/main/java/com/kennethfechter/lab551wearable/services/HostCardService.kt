package com.kennethfechter.lab551wearable.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kennethfechter.lab551wearable.MainActivity

class HostCardService : HostApduService() {
    companion object {
        val TAG = "Host Card Emulator"
        val STATUS_SUCCESS = "9000"
        val STATUS_FAILED = "6F00"
        val CLA_NOT_SUPPORTED = "6E00"
        val INS_NOT_SUPPORTED = "6D00"
        val AID = "A0000001020304"
        val SELECT_INS = "A4"
        val DEFAULT_CLA = "00"
        val MIN_APDU_LENGTH = 12
    }

    override fun onDeactivated(p0: Int) {
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val TRACKING_CHANNEL = "nfc_service"
        val TRACKING_NOTIFICATION_ID = 1

        val notificationChannel = NotificationChannel(
            TRACKING_CHANNEL,
            "Foreground Location Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(notificationChannel)

        val input = intent?.getStringExtra("inputExtra")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(this, TRACKING_CHANNEL)
            .setContentTitle("Test")
            .setContentText(input)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(TRACKING_NOTIFICATION_ID, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun processCommandApdu(commandApdu: ByteArray?,
                                    extras: Bundle?): ByteArray {
        Log.d(TAG, "APDU process command")

        if (commandApdu == null) {
            return StringUtils.hexStringToByteArray(STATUS_FAILED)
        }

        val hexCommandApdu = StringUtils.toHex(commandApdu)


        if (hexCommandApdu.length < MIN_APDU_LENGTH) {
            return StringUtils.hexStringToByteArray(STATUS_FAILED)
        }

        if (hexCommandApdu.substring(0, 2) != DEFAULT_CLA) {
            return StringUtils.hexStringToByteArray(CLA_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(2, 4) != SELECT_INS) {
            return StringUtils.hexStringToByteArray(INS_NOT_SUPPORTED)
        }

        return if (hexCommandApdu.substring(10, 24) == AID) {
            // we wont return success 90 00, we respond with our uid
            // return ByteArrayHexUtil.hexStringToByteArray(STATUS_SUCCESS)

            val dataStore = PreferenceUtility(this)
            val uid = dataStore.getID()
            StringUtils.hexStringToByteArray(uid)


        } else {
            StringUtils.hexStringToByteArray(STATUS_FAILED)
        }
    }
}