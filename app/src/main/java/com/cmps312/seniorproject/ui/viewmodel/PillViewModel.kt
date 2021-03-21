package com.cmps312.seniorproject.ui.viewmodel

import android.app.Application
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

    var currentUser: FirebaseUser = FirebaseUser()
    var selectedpill = Pill()
    var loggedInFlag = false

    init {
        registerPillsListener()
    }

    fun getPills(uid: String) {
        _pills.value = listOf<Pill>() //clear the list
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _pills.value = PillRepo.getPillListByUid(uid)
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
        PillRepo.pillsDocumentRef.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener

            val pills = mutableListOf<Pill>()
            snapshot?.forEach {
                val pill = it.toObject(Pill::class.java)
                pill.pillId = it.id
                if (pill.uid == currentUser?.uid)
                    pills.add(pill)
            }
            _pills.value = pills
        }

    }

}