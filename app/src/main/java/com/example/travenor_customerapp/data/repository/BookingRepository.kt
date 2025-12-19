package com.example.travenor_customerapp.data.repository

import com.example.travenor_customerapp.data.model.BookingRequest
import com.example.travenor_ownerapp.core.utils.Status
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    fun createRequest(cityId: String,customerId: String) = db.collection("request")
        .add(mapOf("cityId" to cityId,
            "customerId" to customerId,
            "status" to Status.PENDING,
            "createdAt" to FieldValue.serverTimestamp()
        )
        )

    fun listenToRequest(requestId: String): Flow<BookingRequest?>{

        return callbackFlow {

            val reg = db.collection("request")
                .document(requestId)
                .addSnapshotListener { doc,e->
                    if (e!=null){
                        close(e)
                        return@addSnapshotListener
                    }

                    if (doc==null){
                        trySend(null)
                        return@addSnapshotListener
                    }

                    trySend(
                        BookingRequest(
                            id = doc.id,
                            customerId = doc.getString("customerId"),
                            cityId = doc.getString("cityId"),
                            status = doc.getString("status") ?: Status.PENDING
                        )
                    )
                }

            awaitClose { reg.remove() }

        }

    }


}