package com.cmps312.seniorproject.ui.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cmps312.seniorproject.model.alarm.AlarmReceiver
import com.cmps312.seniorproject.model.entity.FirebaseUser
import com.cmps312.seniorproject.model.entity.GuestUser
import com.cmps312.seniorproject.model.entity.MainUser
import com.cmps312.seniorproject.model.entity.Pill
import com.cmps312.seniorproject.repository.PillRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class PillViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "PillViewModel"

    private val context = getApplication<Application>().applicationContext

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    private var _pills = MutableLiveData<List<Pill>>()
    var pills: LiveData<List<Pill>> = _pills

    private var _tempPills = MutableLiveData<List<Pill>>()
    var tempPills: LiveData<List<Pill>> = _tempPills

    private var _guestUsers = MutableLiveData<List<GuestUser>>()
    var guestUsers: LiveData<List<GuestUser>> = _guestUsers

    private var _tempGuestUsers = MutableLiveData<List<GuestUser>>()
    var tempGuestUsers: LiveData<List<GuestUser>> = _tempGuestUsers

    private var _mainUsers = MutableLiveData<List<MainUser>>()
    var mainUsers: LiveData<List<MainUser>> = _mainUsers

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
                _tempGuestUsers.value = PillRepo.getAllGuestUsers()
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
        _guestUsers.value = listOf<GuestUser>()  //clear the list
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _guestUsers.value = PillRepo.getAllGuestUsers()
                _tempGuestUsers.value = PillRepo.getAllGuestUsers()
            }
        }
    }

    fun getAllMainUsers() {
        _mainUsers.value = listOf<MainUser>()  //clear the list
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _mainUsers.value = PillRepo.getAllMainUsers()
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

        Log.d("checkMainUser", "${mainUser.email} Hello")

        PillRepo.pillsDocumentRef.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener

            _pills.value = listOf<Pill>() //clear the list
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    _pills.value = updatingPillList()
                }
            }

        }
    }


    private fun registerGuestUsersListener() {

        PillRepo.guestUserDocumentRef.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener

            val users = mutableListOf<GuestUser>()
            val tempUsers = mutableListOf<GuestUser>()

            snapshot?.forEach {

                val user = it.toObject(GuestUser::class.java)
                user.id = it.id

                if (user.uid == currentUser?.uid) {
                    Log.d("guestListener", " guest can be deleted ${user.email}")
                    users.add(user)
                }

                tempUsers.add(user)

            }
            _tempGuestUsers.value = tempUsers
            _guestUsers.value = users

            _pills.value = listOf<Pill>() //clear the list
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    _pills.value = updatingPillList()
                }
            }

        }
    }

    private fun registerMainUsersListener() {

        PillRepo.mainUserDocumentRef.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener

            var users = mutableListOf<MainUser>()

            snapshot?.forEach {

                var tempMainUser = it.toObject(MainUser::class.java)

                if (tempMainUser.uid == currentUser?.uid) {
                    mainUser = tempMainUser
                }
                users.add(tempMainUser)
            }

            _mainUsers.value = users

            _pills.value = listOf<Pill>() //clear the list
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    _pills.value = updatingPillList()
                }
            }

        }

    }

    private suspend fun updatingPillList(): List<Pill> {

        val snapshot = PillRepo.pillsDocumentRef
            .get().await()

        val pills = mutableListOf<Pill>()

        Log.d("checkMainUser", "${mainUser.email}")

        var users = mutableListOf<GuestUser>()

        tempGuestUsers.value?.forEach {
            if (it.email == currentUser?.email) {
                users.add(it)
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

        Log.d("pillListener", "say hai from pill Listener")

        users?.forEach { user ->
            Log.d("pillListener", "${user.email}")
            snapshot?.forEach {
                val pill = it.toObject(Pill::class.java)
                pill.pillId = it.id
                if (pill.uid == user.uid) {
                    Log.d("pillListener", "new guest user ${user.email} pill ${pill.name}")
                    pill.mainUserFlag = false
                    pill.mainUserEmail = user.mainUserEmail
                    pills.add(pill)
                }
            }
        }

        mainUsers.value?.forEach { user ->
            if (user.email == currentUser?.email) {
                Log.d("pillListener", "adding edit main device with email ${user.email}")
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

        mainUsers.value?.forEach { main ->
            Log.d("pillListener", "adding edit main device with email ${main.email}")
            users?.forEach { user ->
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
        }

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)

        pills.forEach {
            if (it.percentage <= 25) {

                var refillMessage = "${it.name} need to be Refilled"

                alarmMgr =
                    context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                    intent.putExtra("message", refillMessage)
                    PendingIntent.getBroadcast(
                        context,
                        (1..100000).random(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                val cal: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, currentHour)
                    set(Calendar.MINUTE, currentMinute)
                    set(Calendar.SECOND, 0)
                }

                alarmMgr?.set(
                    AlarmManager.RTC_WAKEUP, cal.timeInMillis,
                    alarmIntent
                )

            }
        }


        return pills

    }


}