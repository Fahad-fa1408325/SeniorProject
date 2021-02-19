package com.cmps312.seniorproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var googleSignInClient : GoogleSignInClient
    lateinit var googleSignInOption : GoogleSignInOptions
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.fragment)
    }

    private fun google(){
        // Configure Google Sign In

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        auth = FirebaseAuth.getInstance()
    }



}


/*Firebase.auth.addAuthStateListener {
    if (it.currentUser?.uid == null) {
        showSignIn()
    } else {
        val displayName = it.currentUser!!.displayName ?: "Unknown"
        Toast.makeText(this, "Welcome Mr $displayName", Toast.LENGTH_SHORT).show()
    }
}
}*/

/* fun showSignIn() {
     val providers = listOf(
         AuthUI.IdpConfig.EmailBuilder().build(),
         AuthUI.IdpConfig.GoogleBuilder().build()
     )

     val intent = AuthUI.getInstance()
         .createSignInIntentBuilder()
         .setAvailableProviders(providers)
         .setLogo(R.mipmap.ic_launcher)
         .setIsSmartLockEnabled(false)
         .build()
     startActivityForResult(intent, SIGN_IN_CODE)
 }

 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     super.onActivityResult(requestCode, resultCode, data)
     val response = IdpResponse.fromResultIntent(data)
     if (requestCode == SIGN_IN_CODE) {
         if (resultCode == Activity.RESULT_OK) {

         } else {
             Toast.makeText(this, response?.error?.message, Toast.LENGTH_SHORT).show()
             showSignIn()
         }
     }
 }

}*/