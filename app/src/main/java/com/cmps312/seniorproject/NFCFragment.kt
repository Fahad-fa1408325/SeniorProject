package com.cmps312.seniorproject

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cmps312.seniorproject.ui.viewmodel.PillViewModel
import kotlinx.android.synthetic.main.fragment_n_f_c.*
import java.io.IOException

class NFCFragment : AppCompatActivity(R.layout.fragment_n_f_c) {

    private var mNfcAdapter: NfcAdapter? = null

    lateinit var email: String
    lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = intent.getStringExtra("email").toString()
        uid = intent.getStringExtra("uid").toString()
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

    }

    override fun onResume() {
        super.onResume()
        mNfcAdapter?.let {
            enableNFCInForeground(it, this, javaClass)
        }
    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.let {
            disableNFCInForeground(it, this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val messageWrittenSuccessfully =
            createNFCMessage(
                "${nfcSSIDET.text}%%${nfcPasswordET.text}%%$email%%$uid",
                intent
            )
        textView2.text = ifElse(
            messageWrittenSuccessfully,
            "Successfully Written to Tag",
            "Something Went wrong Try Again"
        )
    }

    fun <T> ifElse(condition: Boolean, primaryResult: T, secondaryResult: T) =
        if (condition) primaryResult else secondaryResult

    fun createNFCMessage(payload: String, intent: Intent?): Boolean {

        val pathPrefix = ""
        val nfcRecord = NdefRecord(
            NdefRecord.TNF_EXTERNAL_TYPE,
            pathPrefix.toByteArray(),
            ByteArray(0),
            payload.toByteArray()
        )
        val nfcMessage = NdefMessage(arrayOf(nfcRecord))
        intent?.let {
            val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            return writeMessageToTag(nfcMessage, tag)
        }
        return false
    }

    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {

        try {
            val nDefTag = Ndef.get(tag)

            nDefTag?.let {
                it.connect()
                if (it.maxSize < nfcMessage.toByteArray().size) {
                    //Message to large to write to NFC tag
                    return false
                }
                if (it.isWritable) {
                    it.writeNdefMessage(nfcMessage)
                    it.close()
                    //Message is written to tag
                    return true
                } else {
                    //NFC tag is read-only
                    return false
                }
            }

            val nDefFormatableTag = NdefFormatable.get(tag)

            nDefFormatableTag?.let {
                try {
                    it.connect()
                    it.format(nfcMessage)
                    it.close()
                    //The data is written to the tag
                    return true
                } catch (e: IOException) {
                    //Failed to format tag
                    return false
                }
            }
            //NDEF is not supported
            return false

        } catch (e: Exception) {
            //Write operation has failed
        }
        return false
    }


    fun <T> enableNFCInForeground(nfcAdapter: NfcAdapter, activity: Activity, classType: Class<T>) {
        val pendingIntent = PendingIntent.getActivity(
            activity, 0,
            Intent(activity, classType).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val nfcIntentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val filters = arrayOf(nfcIntentFilter)

        val TechLists =
            arrayOf(arrayOf(Ndef::class.java.name), arrayOf(NdefFormatable::class.java.name))

        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, filters, TechLists)
    }

    fun disableNFCInForeground(nfcAdapter: NfcAdapter, activity: Activity) {
        nfcAdapter.disableForegroundDispatch(activity)
    }

}