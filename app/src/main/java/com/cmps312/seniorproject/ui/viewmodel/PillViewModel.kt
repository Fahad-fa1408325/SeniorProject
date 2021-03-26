package com.cmps312.seniorproject.ui.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cmps312.seniorproject.model.entity.FirebaseUser
import com.cmps312.seniorproject.model.entity.GuestUser
import com.cmps312.seniorproject.model.entity.Pill
import com.cmps312.seniorproject.repository.PillRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PillViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "PillViewModel"

    private var _pills = MutableLiveData<List<Pill>>()
    var pills: LiveData<List<Pill>> = _pills

    private var _tempPills = MutableLiveData<List<Pill>>()
    var tempPills: LiveData<List<Pill>> = _tempPills

    private var _guestUsers = MutableLiveData<List<GuestUser>>()
    var guestUsers: LiveData<List<GuestUser>> = _guestUsers

    private var _tempGuestUsers = MutableLiveData<List<GuestUser>>()
    var tempGuestUsers: LiveData<List<GuestUser>> = _tempGuestUsers


    var currentUser: FirebaseUser = FirebaseUser()
    var selectedpill = Pill()
    var loggedInFlag = false

    init {
        registerPillsListener()
        registerGuestUsersListener()
    }

    fun getPills(uid: String, email: String) {
        _pills.value = listOf<Pill>() //clear the list
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _pills.value = PillRepo.getPillListByUid(uid, email)
            }
        }
    }

    fun getGuestUsers(uid: String) {
        _guestUsers.value = listOf<GuestUser>() //clear the list
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _guestUsers.value = PillRepo.getGuestUsers(uid)
            }
        }
    }

    fun addGuestUser(user: GuestUser) {
        viewModelScope.launch(Dispatchers.IO) {
            PillRepo.addGuestUser(user).await()
        }
    }

    fun getAllPills() {
        _tempPills.value = listOf<Pill>()  //clear the list
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _tempPills.value = PillRepo.getAllPills()
            }
        }
    }

    fun getAllGuestUsers() {
        _tempGuestUsers.value = listOf<GuestUser>()  //clear the list
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _tempGuestUsers.value = PillRepo.getAllGuestUsers()
            }
        }
    }

    fun addPill(pill: Pill) {
        viewModelScope.launch(Dispatchers.IO) {
            PillRepo.addPill(pill).await()
        }
    }

    fun deletePill(pill: Pill) {
        viewModelScope.launch(Dispatchers.IO) {
            PillRepo.deletePill(pill)
        }
    }

    fun deleteGuestUser(user: GuestUser) {
        viewModelScope.launch(Dispatchers.IO) {
            PillRepo.deleteGuestUser(user)
        }
    }

    fun updatePill(pill: Pill) {
        viewModelScope.launch(Dispatchers.IO) {
            PillRepo.updatePill(pill)
        }
    }

    fun addFirebaseUser(user: FirebaseUser) {
        viewModelScope.launch(Dispatchers.IO) {
            PillRepo.addFirebaseUser(user)
        }
    }

    private fun registerPillsListener() {

        getAllGuestUsers()

        PillRepo.pillsDocumentRef.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener

            val pills = mutableListOf<Pill>()
            var users = tempGuestUsers.value
            var tempUsers = mutableListOf<GuestUser>()


            users?.forEach {
                if (it.email == currentUser?.email) {
                    tempUsers.add(it)
                }
            }

            snapshot?.forEach {
                val pill = it.toObject(Pill::class.java)
                pill.pillId = it.id
                if (pill.uid == currentUser?.uid) {
                    pill.mainUserFlag = true
                    pill.mainUserEmail = ""
                    pills.add(pill)
                }
            }

            tempUsers.forEach { user ->
                snapshot?.forEach {
                    val pill = it.toObject(Pill::class.java)
                    pill.pillId = it.id
                    if (pill.uid == user.uid) {
                        pill.mainUserFlag = false
                        pill.mainUserEmail = user.mainUserEmail
                        pills.add(pill)
                    }
                }
            }

            pills.forEach {
                Log.d("flags", "${it.name} and ${it.mainUserFlag}")
            }

            pills.forEach {
                if (it.uid != currentUser?.uid) {
                    it.mainUserFlag = false
                }
            }

            _pills.value = pills
        }

    }

    private fun registerGuestUsersListener() {

        PillRepo.guestUserDocumentRef.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener

            val users = mutableListOf<GuestUser>()
            snapshot?.forEach {
                val user = it.toObject(GuestUser::class.java)
                user.id = it.id
                if (user.uid == currentUser?.uid)
                    users.add(user)
            }
            _guestUsers.value = users
        }

    }


}