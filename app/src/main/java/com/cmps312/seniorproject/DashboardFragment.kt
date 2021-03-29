package com.cmps312.seniorproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.cmps312.seniorproject.model.alarm.AlarmReceiver
import com.cmps312.seniorproject.ui.viewmodel.PillViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*


class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private val pillViewModel: PillViewModel by activityViewModels()

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val auth = FirebaseAuth.getInstance()

        removePreviousAlarms()
        getPreviousGuestUsers()
        getPreviousAlarms()
        //getMainUserPills()


        logOutCardView.setOnClickListener {
            removePreviousAlarms()
            Toast.makeText(context, "Logged out Successfully", Toast.LENGTH_SHORT).show()
            auth.signOut()
            activity?.onBackPressed()
        }

        nfcCardView.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_NFCFragment)
        }

        scheduleCardView.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_scheduleFragment)
        }

        addUserCardView.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addGuestUserFragment)
        }

        statisticsCardView.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_statFragment)
        }

        dispenseOnDemandCardView.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_dispenseOnDemandFragment)
        }

    }

    private fun removePreviousAlarms() {

        if (!pillViewModel.loggedInFlag) {

            pillViewModel.getAllPills()
            pillViewModel.tempPills.observe(viewLifecycleOwner) { pill ->
                pill.forEach {
                    var time = it.time.split(":")
                    var hours = time[0].toInt()
                    var minutes = time[1].toInt()

                    alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                        intent.putExtra("message", it.name)
                        PendingIntent.getBroadcast(
                            context,
                            it.requestKey,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }

                    val cal: Calendar = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, hours)
                        set(Calendar.MINUTE, minutes)
                        set(Calendar.SECOND, 0)
                    }

                    alarmMgr?.set(
                        AlarmManager.RTC_WAKEUP, cal.timeInMillis,
                        alarmIntent
                    )

                    alarmMgr?.cancel(alarmIntent)

                }
            }
        }
    }

    private fun getPreviousAlarms() {

        if (!pillViewModel.loggedInFlag) {

            pillViewModel.getMainUser(pillViewModel.currentUser.uid)
            pillViewModel.getPills(pillViewModel.currentUser.uid, pillViewModel.currentUser.email)


            pillViewModel.pills.observe(viewLifecycleOwner) { pill ->

                pill.forEach {

                    var time = it.time.split(":")
                    var hours = time[0].toInt()
                    var minutes = time[1].toInt()


                    alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                        intent.putExtra("message", it.name)
                        PendingIntent.getBroadcast(
                            context,
                            it.requestKey,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }

                    val cal: Calendar = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, hours)
                        set(Calendar.MINUTE, minutes)
                        set(Calendar.SECOND, 0)
                    }



                    alarmMgr?.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        3600000 * it.repeadtly.toString().toLong(),
                        alarmIntent
                    )

                }
            }
            pillViewModel.loggedInFlag = true
        }
    }

    private fun getPreviousGuestUsers() {

        if (!pillViewModel.loggedInFlag) {
            pillViewModel.getGuestUsers(pillViewModel.currentUser.uid)
        }

    }

    /*private fun getMainUserPills() {
        if (!pillViewModel.loggedInFlag) {

            pillViewModel.getAllGuestUsers()

            pillViewModel.tempGuestUsers.observe(viewLifecycleOwner) { users ->

                users.forEach {
                    if (it.email == pillViewModel.currentUser.email) {
                        pillViewModel.mainUserUID = it.uid
                        pillViewModel.getMainUserPills(it.uid)
                    }
                }

            }
            pillViewModel.mainUserPills.observe(viewLifecycleOwner) { pill ->
                pill.forEach {
                    it.mainUserFlag = false

                }
                pillViewModel.loggedInFlag = true
            }
        }

    }*/

}