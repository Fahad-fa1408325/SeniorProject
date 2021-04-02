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

    //private val pillViewModel: PillViewModel by activityViewModels()

    lateinit var pillViewModel: PillViewModel

//    private var nfcAdapter: NfcAdapter? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // Check for available NFC Adapter
//        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
//        if (nfcAdapter == null) {
//            Toast.makeText(context, "NFC is not available", Toast.LENGTH_LONG).show()
//            return
//        }
//        // Register callback
//        nfcAdapter?.setNdefPushMessageCallback(this, activity)
//    }

    private var mNfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //todo check if this is working
        //setContentView(R.layout.AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA)
        pillViewModel = ViewModelProvider(this).get(PillViewModel::class.java)
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

    //"${nfcSSIDET.text}+${nfcPasswordET.text}+${pillViewModel.currentUser.email}+${pillViewModel.currentUser.uid}"
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        //TODO editText to get values from to NFC instead of AAAAAAAAAAAAA
        //super.onNewIntent(intent)
        val messageWrittenSuccessfully =
            createNFCMessage(
                "${nfcSSIDET.text}+${nfcPasswordET.text}+${pillViewModel.currentUser.email}+${pillViewModel.currentUser.uid}",
                intent
            )
        textView2.text = ifElse(
            messageWrittenSuccessfully,
            "Successful Written to Tag",
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


//    override fun createNdefMessage(event: NfcEvent): NdefMessage {
//        val text = "Beam me up, Android!\n\n" +
//                "Beam Time: " + System.currentTimeMillis()
//        return NdefMessage(
//            arrayOf(
//                createMime("application/vnd.com.example.android.beam", text.toByteArray())
//            )
//            /**
//             * The Android Application Record (AAR) is commented out. When a device
//             * receives a push with an AAR in it, the application specified in the AAR
//             * is guaranteed to run. The AAR overrides the tag dispatch system.
//             * You can add it back in to guarantee that this
//             * activity starts when receiving a beamed message. For now, this code
//             * uses the tag dispatch system.
//             */
//            //,NdefRecord.createApplicationRecord("com.example.android.beam")
//        )
//    }

//    override fun onResume() {
//        super.onResume()
//        // Check to see that the Activity started due to an Android Beam
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED == activity?.intent?.action) {
//            processIntent(requireActivity().intent)
//        }
//    }

//    fun onNewIntent(intent: Intent) {
//        // onResume gets called after this to handle the intent
//        activity?.intent = intent
//    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
//    private fun processIntent(intent: Intent) {
//        // only one message sent during the beam
//        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMsgs ->
//            (rawMsgs[0] as NdefMessage).apply {
//                // record 0 contains the MIME type, record 1 is the AAR, if present
//                textView2.text = String(records[0].payload)
//            }
//        }
//    }
}