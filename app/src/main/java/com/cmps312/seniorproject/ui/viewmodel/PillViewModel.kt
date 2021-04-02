package com.cmps312.seniorproject.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cmps312.seniorproject.model.entity.FirebaseUser
import com.cmps312.seniorproject.model.entity.GuestUser
import com.cmps312.seniorproject.model.entity.MainUser
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

    private var _tempMainUsers = MutableLiveData<List<MainUser>>()
    var tempMainUsers: LiveData<List<MainUser>> = _tempMainUsers

    var currentUser: FirebaseUser = FirebaseUser()
    var mainUser: MainUser = MainUser()
    var selectedPill = Pill()
    var loggedInFlag = false

    init {
        getAllMainUsers()
        registerPillsListener()
        registerGuestUsersListener()
        registerMainUsersListener()
    }

    fun getPills(uid: String, email: String) {
        _pills.value = listOf<Pill>() //clear the list
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _pills.value = PillRepo.getPillListByUid(uid, email)
            }
        }
    }

    fun getMainUser(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                mainUser = PillRepo.getMainUser(uid)
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

    fun getAllMainUsers() {
        _tempGuestUsers.value = listOf<GuestUser>()  //clear the list
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _tempMainUsers.value = PillRepo.getAllMainUsers()
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

    fun registerPillsListener() {

        getAllGuestUsers()

        Log.d("checkMainUser", "${mainUser.email} Hello")

        var pillListener = PillRepo.pillsDocumentRef.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener

            val pills = mutableListOf<Pill>()
            var guestUsers = tempGuestUsers.value
            var mainUsers = tempMainUsers.value

            var tempUsers = mutableListOf<GuestUser>()
            var connectedFlag = true

            Log.d("checkMainUser", "${mainUser.email}")

            guestUsers?.forEach {
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

            mainUsers?.forEach { user ->
                if (user.email == currentUser?.email) {
                    snapshot?.forEach {
                        val pill = it.toObject(Pill::class.java)
                        if (pill.mainUserEmail == "Main Device") {
                            pill.mainUserFlag = false
                            pill.readFromMain = false
                            pill.editFromMain = true
                            pill.pillId = it.id
                            pills.add(pill)
                        }
                    }
                }
            }
            mainUsers?.forEach { main ->
                tempUsers.forEach { user ->
                    if (user.mainUserEmail == main.email) {
                        snapshot?.forEach {
                            val pill = it.toObject(Pill::class.java)
                            if (pill.mainUserEmail == "Main Device") {
                                pill.mainUserFlag = false
                                pill.readFromMain = true
                                pill.editFromMain = false
                                pill.pillId = it.id
                                pills.add(pill)
                            }
                        }

                    }
                }

                pills.forEach {
                    Log.d(
                        "currentPill",
                        "name :${it.name} + read :${it.readFromMain} + edit :${it.editFromMain} + main : ${it.mainUserFlag}"
                    )
                }

                Log.d("currentPill", "Finished")
                Log.d("currentPill", "main user :${mainUser.email}")
                Log.d("currentPill", "connected :$connectedFlag")

                _pills.value = pills
            }
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

    private fun registerMainUsersListener() {

        PillRepo.mainUserDocumentRef.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener

            /*
            var pill = Pill()
            pill.name = "Panadrex"
            pill.time = "13:16"
            pill.dosage = 1
            pill.repeadtly = 1
            pill.pillId = "TVMYawrF9j9MAKtWUfIP"
            pill.uid = "o45WOPsSsuhqSBTNWyxlIAXUx4M8"
            pill.mainUserFlag = true
            pill.mainUserEmail = ""
            pill.requestKey = 70416
            pill.readFromMain = false
            pill.editFromMain = false
            pill.percentage = 0.0
            pill.demanded = 0
            */

            snapshot?.forEach {

                var tempMainUser = it.toObject(MainUser::class.java)

                if (tempMainUser.uid == currentUser?.uid) {

                    mainUser = tempMainUser

                }

            }

        }

    }


}