package com.cmps312.seniorproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.cmps312.seniorproject.ui.viewmodel.PillViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(R.layout.fragment_login) {

    private val pillViewModel: PillViewModel by activityViewModels()

    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient


    companion object {

        private const val RC_SIGN_IN = 1001

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pillViewModel.loggedInFlag = false

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = FirebaseAuth.getInstance()

        loginBTN.setOnClickListener {
            signIn()
        }

        loginGoogleBTN.setOnClickListener {
            signInWithGoogle()
        }

        registerTV.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }


    }

    private fun signIn(): Boolean {
        val email = loginEmailET?.text.toString().trim()
        val password = loginPasswordEt?.text.toString().trim()
        var flag = false

        if (password.isNotEmpty() && email.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        pillViewModel.currentUser.email = email
                        pillViewModel.currentUser.uid = firebaseUser.uid
                        pillViewModel.addFirebaseUser(pillViewModel.currentUser)

                        /*Toast.makeText(
                            context,
                            "Signed In Successfully user ${firebaseUser.uid}",
                            Toast.LENGTH_SHORT
                        ).show()*/

                        findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                        flag = true
                    }

                }.addOnFailureListener {
                    Toast.makeText(context, "Failed To Sign in: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            Toast.makeText(context, "Filling All Fields is Required ***", Toast.LENGTH_SHORT).show()
        }
        return flag
    }

    private fun signInWithGoogle() {
        // Configure Google Sign In
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    Toast.makeText(
                        context,
                        "Google sign in Success",
                        Toast.LENGTH_SHORT
                    ).show()
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                    Toast.makeText(
                        context,
                        "Google sign in failed ${task.exception}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            } else {
                Log.w("SignInActivity", exception.toString())
                Toast.makeText(
                    context,
                    "login is not successful ${task.exception}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")
                    if (user != null) {
                        pillViewModel.currentUser.email = user.email.toString()
                        pillViewModel.currentUser.uid = user.uid
                        pillViewModel.addFirebaseUser(pillViewModel.currentUser)

                        /*Toast.makeText(
                            context,
                            "signInWithCredential:success user UID: ${user.uid}",
                            Toast.LENGTH_SHORT
                        ).show()*/

                    }
                    //If everything works successfully, here we can take the user info
                    findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignInActivity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        context,
                        "signInWithCredential:failure ${task.exception}",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }
      }


}

