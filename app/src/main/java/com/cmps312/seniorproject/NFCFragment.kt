package com.cmps312.seniorproject
/*
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.*
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_n_f_c.*
import java.io.IOException
import java.io.UnsupportedEncodingException
import kotlin.experimental.and

class NFCFragment : Fragment(R.layout.fragment_n_f_c) {
    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    lateinit var writingTagFilters: Array<IntentFilter>
    var writeMode = false
    var myTag: Tag? = null
    var edit_message = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nfcConnectBTN.setOnClickListener {
            if (nfcPasswordET.text.isNotEmpty() && nfcSSIDET.text.isNullOrEmpty()) {
                edit_message = nfcPasswordET.text.toString()
            }
            try {
                if (myTag == null) {
                    Toast.makeText(
                        context,
                        "com.example.nfctutorial.MainActivity.Companion.Error_Detected",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    write("PlainText|$edit_message", myTag!!)
                    Toast.makeText(
                        context,
                        "com.example.nfctutorial.MainActivity.Companion.Write_Success",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "com.example.nfctutorial.MainActivity.Companion.Write_Error",
                    Toast.LENGTH_LONG
                ).show()
                e.printStackTrace()
            } catch (e: FormatException) {
                Toast.makeText(
                    context,
                    "com.example.nfctutorial.MainActivity.Companion.Write_Error",
                    Toast.LENGTH_LONG
                ).show()
                e.printStackTrace()
            }
        }
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        if (nfcAdapter == null) {
            Toast.makeText(context, "This device does not support NFC", Toast.LENGTH_SHORT).show()
        }
        activity?.let { readFromIntent(it.intent) }
        pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT)
        writingTagFilters = arrayOf(tagDetected)
    }

    private fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            var msgs: Array<NdefMessage?>? = null
            if (rawMsgs != null) {
                msgs = arrayOfNulls(rawMsgs.size)
                for (i in rawMsgs.indices) {
                    msgs[i] = rawMsgs[i] as NdefMessage
                }
            }
            buildTagViews(msgs)
        }
    }

    private fun buildTagViews(msgs: Array<NdefMessage?>?) {
        if (msgs == null || msgs.isEmpty()) return
        var text = ""
        //        String tagId = new String(msgs[0].getRecords()[0].getType());
        val payload = msgs[0]!!.records[0].payload
        val textEncoding =
            if (payload[0] and 128.toByte() == 0.toByte()) Charsets.UTF_8 else Charsets.UTF_16 // Get the Text Encoding
        val languageCodeLength: Byte = payload[0] and 51 // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        try {
            // Get the Text
            text = String(
                payload,
                languageCodeLength + 1,
                payload.size - languageCodeLength - 1,
                textEncoding
            )
        } catch (e: UnsupportedEncodingException) {
            Log.e("UnsupportedEncoding", e.toString())
        }
        textView2.text = "NFC Content: $text"
    }

    @Throws(IOException::class, FormatException::class)
    private fun write(text: String, tag: Tag) {
        val records = arrayOf(createRecord(text))
        val message = NdefMessage(records)
        // Get an instance of Ndef for the tag.
        val ndef = Ndef.get(tag)
        // Enable I/O
        ndef.connect()
        // Write the message
        ndef.writeNdefMessage(message)
        // Close the connection
        ndef.close()
    }

    @Throws(UnsupportedEncodingException::class)
    private fun createRecord(text: String): NdefRecord {
        val lang = "en"
        val textBytes = text.toByteArray()
        val langBytes = lang.toByteArray(charset("US-ASCII"))
        val langLength = langBytes.size
        val textLength = textBytes.size
        val payload = ByteArray(1 + langLength + textLength)

        // set status byte (see NDEF spec for actual bits)
        payload[0] = langLength.toByte()

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength)
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), payload)
    }

    fun onNewIntent(intent: Intent) {
        activity?.intent = intent
        readFromIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        }
    }

    override fun onPause() {
        super.onPause()
        WriteModeOff()
    }

    override fun onResume() {
        super.onResume()
        WriteModeOn()
    }

    /******************************************************************************
     * Enable Write********************************
     */
    private fun WriteModeOn() {
        writeMode = true
        nfcAdapter!!.enableForegroundDispatch(
            activity,
            pendingIntent,
            writingTagFilters,
            null
        )
    }

    /******************************************************************************
     * Disable Write*******************************
     */
    private fun WriteModeOff() {
        writeMode = false
        nfcAdapter!!.disableForegroundDispatch(activity)
    }

    companion object {
        const val Error_Detected = "No NFC Tag Detected"
        const val Write_Success = "Text Written Successfully!"
        const val Write_Error = "Error during Writing, Try Again!"
    }
}*/


/*
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NfcF
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_n_f_c.*

class NFCFragment : Fragment(R.layout.fragment_n_f_c) {

    private var intentFiltersArray: Array<IntentFilter>? = null
    private val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))
    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(context)
    }
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //nfc process start
        pendingIntent = PendingIntent.getActivity(
            context, 0, Intent(context, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            ndef.addDataType("text/plain")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("fail", e)
        }
        intentFiltersArray = arrayOf(ndef)
        if (nfcAdapter == null) {
            val builder = context?.let { AlertDialog.Builder(it, R.style.MyAlertDialogStyle) }
            builder?.setMessage("This device doesn't support NFC.")
            builder?.setPositiveButton("Cancel", null)
            val myDialog = builder?.create()
            myDialog?.setCanceledOnTouchOutside(false)
            myDialog?.show()
            // txttext.setText("THIS DEVICE DOESN'T SUPPORT NFC. PLEASE TRY WITH ANOTHER DEVICE!")
        } else if (!nfcAdapter!!.isEnabled) {
            val builder = context?.let { AlertDialog.Builder(it, R.style.MyAlertDialogStyle) }
            builder?.setTitle("NFC Disabled")
            builder?.setMessage("Plesae Enable NFC")
            // txttext.setText("NFC IS NOT ENABLED. PLEASE ENABLE NFC IN SETTINGS->NFC")
            builder?.setPositiveButton("Settings") { _, _ -> startActivity(Intent(Settings.ACTION_NFC_SETTINGS)) }
            builder?.setNegativeButton("Cancel", null)
            val myDialog = builder?.create()
            myDialog?.setCanceledOnTouchOutside(false)
            myDialog?.show()
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(
            activity,
            pendingIntent,
            intentFiltersArray,
            techListsArray
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onNewIntent(intent: Intent) {
        try {
            if (!nfcSSIDET.text.toString().equals("") && !nfcPasswordET.text.toString()
                    .equals("")
            ) {


                val ssid = nfcSSIDET.text.toString()
                val password = nfcPasswordET.text.toString()

                if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                    || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                ) {

                    val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                    val ndef = Ndef.get(tag) ?: return

                    if (ndef.isWritable) {

                        var message = NdefMessage(
                            arrayOf(
                                NdefRecord.createTextRecord("en", ssid),
                                NdefRecord.createTextRecord("en", password)
//                        NdefRecord.createTextRecord("en", userid)

                            )
                        )


                        ndef.connect()
                        ndef.writeNdefMessage(message)
                        ndef.close()


                        Toast.makeText(context, "Successfully Wroted!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(context, "Write on text box!", Toast.LENGTH_SHORT).show()
            }
        } catch (Ex: Exception) {
            Toast.makeText(context, Ex.message, Toast.LENGTH_SHORT).show()
        }


    }

    override fun onPause() {
        if (activity?.isFinishing == true) {
            nfcAdapter?.disableForegroundDispatch(activity)
        }
        super.onPause()
    }


}*/

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord.createMime
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_n_f_c.*

class NFCFragment : Fragment(R.layout.fragment_n_f_c), NfcAdapter.CreateNdefMessageCallback {

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check for available NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        if (nfcAdapter == null) {
            Toast.makeText(context, "NFC is not available", Toast.LENGTH_LONG).show()
            return
        }
        // Register callback
        nfcAdapter?.setNdefPushMessageCallback(this, activity)
    }

    override fun createNdefMessage(event: NfcEvent): NdefMessage {
        val text = "Beam me up, Android!\n\n" +
                "Beam Time: " + System.currentTimeMillis()
        return NdefMessage(
            arrayOf(
                createMime("application/vnd.com.example.android.beam", text.toByteArray())
            )
            /**
             * The Android Application Record (AAR) is commented out. When a device
             * receives a push with an AAR in it, the application specified in the AAR
             * is guaranteed to run. The AAR overrides the tag dispatch system.
             * You can add it back in to guarantee that this
             * activity starts when receiving a beamed message. For now, this code
             * uses the tag dispatch system.
             */
            //,NdefRecord.createApplicationRecord("com.example.android.beam")
        )
    }

    override fun onResume() {
        super.onResume()
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == activity?.intent?.action) {
            processIntent(requireActivity().intent)
        }
    }

    fun onNewIntent(intent: Intent) {
        // onResume gets called after this to handle the intent
        activity?.intent = intent
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    private fun processIntent(intent: Intent) {
        // only one message sent during the beam
        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMsgs ->
            (rawMsgs[0] as NdefMessage).apply {
                // record 0 contains the MIME type, record 1 is the AAR, if present
                textView2.text = String(records[0].payload)
            }
        }
    }
}