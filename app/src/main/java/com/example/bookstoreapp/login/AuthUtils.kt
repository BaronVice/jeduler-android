package com.example.bookstoreapp.login

import android.content.Context
import com.example.bookstoreapp.AppUtils.showToast
import com.google.firebase.auth.FirebaseAuth

object AuthUtils {
    private const val EMPTY_CREDENTIALS = "Credentials cannot be empty."
    private const val ERR_LEN = "Password must have at least 8 characters."
    private const val ERR_MAX_LEN = "Password must have no more then 20 characters."

    private const val ERR_WHITESPACE = "Whitespaces are not allowed in password."
    private const val ERR_DIGIT = "Password must contain at least one digit."
    private const val ERR_UPPER = "Password must have at least one uppercase letter."
    private const val ERR_SPECIAL = "Password must have at least one special character, such as: _%-=+#@"


    fun signUp(
        auth: FirebaseAuth,
        email: String,
        password: String,
        context: Context
    ): Boolean {
        if (areCredentialsEmpty(email, password, context)) return false
        if (!isPasswordStrong(password, context)) return false

        var isLoggedIn = true
        auth
            .createUserWithEmailAndPassword(
                email,
                password
            )
            .addOnSuccessListener{
                val user = auth.currentUser
//                    updateUI(user)
            }
            .addOnFailureListener {
                showToast(
                    context,
                    it.message.toString()
                )
                isLoggedIn = false
//                    updateUI(null)
            }

        return isLoggedIn
    }

    fun signIn(
        auth: FirebaseAuth,
        email: String,
        password: String,
        context: Context
    ): Boolean {
        if (areCredentialsEmpty(email, password, context)) return false

        var isLoggedIn = true
        auth
            .signInWithEmailAndPassword(
                email,
                password
            )
            .addOnSuccessListener{
                val user = auth.currentUser
//                    updateUI(user)
            }
            .addOnFailureListener {
                showToast(
                    context,
                    it.message.toString()
                )
                isLoggedIn = false
//                    updateUI(null)
            }

        return isLoggedIn
    }


    private fun areCredentialsEmpty(
        email: String,
        password: String,
        context: Context
    ): Boolean {
        if (email == "" || password == ""){
            showToast(
                context,
                EMPTY_CREDENTIALS
            )
            return true
        }

        return false
    }

    private fun isPasswordStrong(
        password: String,
        context: Context
    ): Boolean {
        if (password.length < 8) {
            showToast(context, ERR_LEN)
            return false
        }
        if (password.length > 20){
            showToast(context, ERR_MAX_LEN)
        }
        if (password.any { it.isWhitespace() }) {
            showToast(context, ERR_WHITESPACE)
            return false
        }
        if (password.none { it.isDigit() }) {
            showToast(context, ERR_DIGIT)
            return false
        }
        if (password.none { it.isUpperCase() }) {
            showToast(context, ERR_UPPER)
            return false
        }
        if (password.none { !it.isLetterOrDigit() }) {
            showToast(context, ERR_SPECIAL)
            return false
        }

        return true
    }

}