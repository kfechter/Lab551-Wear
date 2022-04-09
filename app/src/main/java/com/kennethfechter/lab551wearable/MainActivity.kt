package com.kennethfechter.lab551wearable

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.kennethfechter.lab551wearable.databinding.ActivityMainBinding
import com.kennethfechter.lab551wearable.services.DialogAndToastUtils
import com.kennethfechter.lab551wearable.services.HostCardService

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled) {
                DialogAndToastUtils(this).showNFCDisabled()
            }
            else {
                // Start Service
                val startIntent = Intent(this, HostCardService::class.java);
                ContextCompat.startForegroundService(this, startIntent);
            }
        }
    }
}