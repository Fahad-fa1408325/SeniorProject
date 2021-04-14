package com.cmps312.seniorproject.repository

import android.util.Log
import com.cmps312.seniorproject.model.entity.FirebaseUser
import com.cmps312.seniorproject.model.entity.GuestUser
import com.cmps312.seniorproject.model.entity.MainUser
import com.cmps312.seniorproject.model.entity.Pill
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlinx.coroutines.tasks.await

object PillRepo {

    val TAG = "PillListRepo"
    val db by lazy { FirebaseFirestore.getInstance() }
    val mainUserDocumentRef by lazy { db.collection("main_users") }
    val guestUserDocumentRef by lazy { db.collection("guest_users") }
    val pillsDocumentRef by lazy { db.collection("pills") }

    //caching
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

    suspend fun getMainUser(uid: String): MainUser {
        var mainUser = MainUser()

        val querySnapshot = mainUserDocumentRef
            .whereEqualTo("uid", uid)
            .get().await()

        querySnapshot.forEach {
            mainUser = it.toObject(MainUser::class.java)
        }
        return mainUser
    }

    suspend fun getPillListByUid(uid: String, email: String): MutableList<Pill> {
        val querySnapshot = pillsDocumentRef
            .whereEqualTo("uid", uid)
            .get().await()

        val pills = mutableListOf<Pill>()
        var mainUser = getMainUser(uid)
        var connectedFlag = true

        querySnapshot.forEach {
            val pill = it.toObject(Pill::class.java)
            pill.mainUserFlag = true
            pill.mainUserEmail = ""
            pill.pillId = it.id
            pills.add(pill)
        }

        var users = getAllGuestUsers()
        var tempUsers = mutableListOf<GuestUser>()

        users.forEach {
            if (it.email == email) {
                tempUsers.add(it)
            }
        }

        tempUsers.forEach { user ->
            var newUid = user.uid
            var querySnapshot2 = pillsDocumentRef
                .whereEqualTo("uid", newUid)
                .get().await()
            querySnapshot2.forEach {
                val pill = it.toObject(Pill::class.java)
                pill.mainUserFlag = false
                pill.mainUserEmail = user.mainUserEmail
                pill.pillId = it.id
                pills.add(pill)
            }
        }

        if (!mainUser.uid.isNullOrEmpty()) {
            connectedFlag = false
            val querySnapshot3 = pillsDocumentRef
                .whereEqualTo("mainUserEmail", "Main Device")
                .get().await()

            querySnapshot3.forEach {
                val pill = it.toObject(Pill::class.java)
                pill.mainUserFlag = false
                pill.editFromMain = true
                pill.pillId = it.id
                pills.add(pill)
            }
        }

        if (connectedFlag) {
            tempUsers.forEach {
                mainUser = getMainUser(it.uid)
                if (it.mainUserEmail == mainUser.email) {

                    val querySnapshot3 = pillsDocumentRef
                        .whereEqualTo("mainUserEmail", "Main Device")
                        .get().await()

                    querySnapshot3.forEach {
                        val pill = it.toObject(Pill::class.java)
                        pill.mainUserFlag = false
                        pill.editFromMain = false
                        pill.readFromMain = true
                        pill.pillId = it.id
                        pills.add(pill)
                    }
                }
            }
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

    suspend fun getAllGuestUsers(): MutableList<GuestUser> {

        val querySnapshot = guestUserDocumentRef
            .get().await()

        val users = mutableListOf<GuestUser>()

        querySnapshot.forEach {
            val user = it.toObject(GuestUser::class.java)
            user.id = it.id
            users.add(user)
        }

        return users
    }

    suspend fun getAllMainUsers(): MutableList<MainUser> {

        val querySnapshot = mainUserDocumentRef
            .get().await()

        val users = mutableListOf<MainUser>()

        querySnapshot.forEach {
            val user = it.toObject(MainUser::class.java)
            user.id = it.id
            users.add(user)
        }

        return users
    }


    suspend fun addPill(pill: Pill) = pillsDocumentRef.add(pill)
        .addOnSuccessListener { Log.d(TAG, "Successfully added new pill") }
        .addOnFailureListener { Log.d(TAG, "Was not able to add the new pill") }

    suspend fun deletePill(pill: Pill) = pill.pillId?.let { pillsDocumentRef.document(it).delete() }

    suspend fun deleteGuestUser(user: GuestUser) =
        user.id?.let { guestUserDocumentRef.document(it).delete() }

    suspend fun updatePill(pill: Pill) =
        pill.pillId?.let { pillsDocumentRef.document(it).set(pill) }

}