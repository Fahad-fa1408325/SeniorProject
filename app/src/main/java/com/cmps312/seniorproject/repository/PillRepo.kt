package com.cmps312.seniorproject.repository

import android.util.Log
import com.cmps312.seniorproject.LoginFragment
import com.cmps312.seniorproject.model.entity.FirebaseUser
import com.cmps312.seniorproject.model.entity.GuestUser
import com.cmps312.seniorproject.model.entity.Pill
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlinx.coroutines.tasks.await

object PillRepo {

    val TAG = "PillListRepo"
    val db by lazy { FirebaseFirestore.getInstance() }
    val mainDeviceDocumentRef by lazy { db.collection("maindevice") }
    val mainUserDocumentRef by lazy { db.collection("maindevice/guest_users") }
    val guestUserDocumentRef by lazy { db.collection("maindevice/main_users") }
    val pillsDocumentRef by lazy { db.collection("Pills") }

    //all the magic of caching
    init {
        db.firestoreSettings = firestoreSettings { isPersistenceEnabled = true }
    }

    suspend fun addFirebaseUser(user: FirebaseUser) {
        val newUser = hashMapOf(
            "email" to "${user.email}",
            "uid" to "${user.uid}"
        )
        var documentName = user.email
        db.collection("users").document(documentName).set(newUser)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun addGuestUser(user: GuestUser) = guestUserDocumentRef.add(user)
        .addOnSuccessListener { Log.d(TAG, "Successfully added new GuestUser") }
        .addOnFailureListener { Log.d(TAG, "Was not able to add the new GuestUser") }

    suspend fun getGuestUsers(uid: String): MutableList<GuestUser> {

        val querySnapshot = guestUserDocumentRef
            .whereEqualTo("uid", uid)
            .get().await()

        val users = mutableListOf<GuestUser>()

        querySnapshot.forEach {
            val user = it.toObject(GuestUser::class.java)
            user.id = it.id
            users.add(user)
        }

        return users

    }

    suspend fun getPillListByUid(uid: String): MutableList<Pill> {
        val querySnapshot = pillsDocumentRef
            .whereEqualTo("uid", uid)
            .get().await()

        val pills = mutableListOf<Pill>()
        querySnapshot.forEach {
            val pill = it.toObject(Pill::class.java)
            pill.pillId = it.id
            pills.add(pill)
        }
        return pills
    }

    suspend fun getAllPills(): MutableList<Pill> {

        val querySnapshot = pillsDocumentRef
            .get().await()

        val pills = mutableListOf<Pill>()

        querySnapshot.forEach {
            val pill = it.toObject(Pill::class.java)
            pill.pillId = it.id
            pills.add(pill)
        }

        return pills
    }

    suspend fun addPill(pill: Pill) = pillsDocumentRef.add(pill)
        .addOnSuccessListener { Log.d(TAG, "Successfully added new pill") }
        .addOnFailureListener { Log.d(TAG, "Was not able to add the new pill") }

    suspend fun getPill(id: String) =
        pillsDocumentRef.document(id).get().await().toObject(Pill::class.java)

    suspend fun deletePill(pill: Pill) = pill.pillId?.let { pillsDocumentRef.document(it).delete() }
    suspend fun updatePill(pill: Pill) =
        pill.pillId?.let { pillsDocumentRef.document(it).set(pill) }

}