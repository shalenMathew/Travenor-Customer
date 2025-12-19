package com.example.travenor_customerapp.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(val auth: FirebaseAuth) {

  suspend  fun signInOrCreate(email: String, password: String){

      try {
          auth.signInWithEmailAndPassword(email, password).await()
      } catch (e: FirebaseAuthException) {

          Log.d("test",e.errorCode)
          when (e.errorCode) {
              "ERROR_INVALID_CREDENTIAL" -> {
                  auth.createUserWithEmailAndPassword(email, password).await()
              }
              else -> throw e
          }
      }

  }

    fun signOut() {
        auth.signOut()
    }


}