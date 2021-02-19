package com.cmps312.seniorproject

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_register2.*


class RegisterFragment : Fragment(R.layout.fragment_register2) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerBTN.setOnClickListener {
            if (signUp()) {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
            }
        }

        registerLoginTV.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
        }

    }

    private fun signUp(): Boolean {
        val email = registerEmailET?.text.toString().toLowerCase().trim()
        val password = registerPasswordET?.text.toString().trim()
        val confirmPassword = confirmPasswordET?.text.toString().trim()
        var flag = false

        if (
            email.isNotEmpty() &&
            password.isNotEmpty() &&
            confirmPassword.isNotEmpty()
        ) {
            if (email.contains("@") && email.takeLast(4).equals(".com")) {
                if (password.length >= 6) {
                    if (password == confirmPassword) {
                        //Here we will register
                        //Toast.makeText(context, email+password, Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        context,
                                        "User Added Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    flag = true
                                }

                            }.addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Failed To create User: ${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        return flag
                    } else {
                        Toast.makeText(context, "Passwords are not the same", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Password should be at least 6 characters",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(context, "Email Entered is Wrong", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(context, "Filling All Fields is Required ***", Toast.LENGTH_SHORT).show()
        }
        return flag
    }

    private fun checkPassword(password: String): Boolean {
        var lengthFlag = password.length >= 6
        var lowerCaseFlag: Boolean = false
        var upperCaseFlag: Boolean = false
        var numberFlag: Boolean = false

        for (n in password.indices) {
            if (password[n].isDigit()) numberFlag = true
            if (password[n].isUpperCase()) upperCaseFlag = true
            if (password[n].isLowerCase()) lowerCaseFlag = true
        }

        if (lengthFlag && lowerCaseFlag && upperCaseFlag && numberFlag) {
            return true
        } else {
            Toast.makeText(context, "Password Entered is Wrong", Toast.LENGTH_SHORT).show()
        }
        return false
    }
}