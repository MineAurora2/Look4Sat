package com.rtbishop.lookingsat.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.amsacode.predict4java.TLE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rtbishop.lookingsat.MainViewModel
import com.rtbishop.lookingsat.R
import com.rtbishop.lookingsat.repo.SatPass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SkyFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var recViewCurrent: RecyclerView
    private lateinit var recViewFuture: RecyclerView
    private lateinit var recAdapterCurrent: RecyclerView.Adapter<*>
    private lateinit var recAdapterFuture: RecyclerView.Adapter<*>
    private lateinit var timeToAos: TextView
    private lateinit var btnPassPrefs: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton

    private var aosTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sky, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(activity as MainActivity).get(MainViewModel::class.java)
        timeToAos = (activity as MainActivity).findViewById(R.id.toolbar_time_to_aos)
        btnPassPrefs = (activity as MainActivity).findViewById(R.id.toolbar_btn_refresh)
        progressBar = view.findViewById(R.id.sky_progressbar)
        recViewFuture = view.findViewById(R.id.sky_recycler_future)
        fab = view.findViewById(R.id.sky_fab)

        recViewFuture.layoutManager = LinearLayoutManager(activity)

        fab.setOnClickListener { showSelectSatDialog() }

        resetTimer()
    }

    private fun showSelectSatDialog() {
        val tleSelectedMap = viewModel.tleSelectedMap
        val tleMainListSize = viewModel.tleMainList.size

        val tleNameArray = arrayOfNulls<String>(tleMainListSize)
        val tleCheckedArray = BooleanArray(tleMainListSize)

        if (tleSelectedMap.isEmpty()) {
            for ((position, tle) in viewModel.tleMainList.withIndex()) {
                tleNameArray[position] = tle.name
                tleCheckedArray[position] = false
            }
        } else {
            for ((position, tle) in viewModel.tleMainList.withIndex()) {
                tleNameArray[position] = tle.name
                tleCheckedArray[position] = viewModel.tleSelectedMap.getOrDefault(tle, false)
            }
        }

        val selectionMap = mutableMapOf<TLE, Boolean>()
        val builder = AlertDialog.Builder(activity as MainActivity)
        builder.setTitle("Select satellites to track")
            .setMultiChoiceItems(tleNameArray, tleCheckedArray) { _, which, isChecked ->
                selectionMap[viewModel.tleMainList[which]] = isChecked
            }
            .setPositiveButton("Ok") { _, _ ->
                for ((tle, value) in selectionMap) {
                    tleSelectedMap[tle] = value
                    calculatePasses()
                }
            }
            .setNeutralButton("Clear All") { _, _ ->
                tleSelectedMap.clear()
                calculatePasses()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun calculatePasses() {
        var satPassList: List<SatPass>
        lifecycleScope.launch(Dispatchers.Main) {
            recViewFuture.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            progressBar.isIndeterminate = true
            satPassList = viewModel.getPassesForSelectedSatellites()
            recAdapterFuture = SatPassAdapter(satPassList)
            recViewFuture.adapter = recAdapterFuture
            progressBar.isIndeterminate = false
            progressBar.visibility = View.INVISIBLE
            recViewFuture.visibility = View.VISIBLE
            if (satPassList.isEmpty()) {
                resetTimer()
            } else {
                setTimer(satPassList[0].pass.startTime.time)
            }
        }
    }

    private fun setTimer(passTime: Long) {
        if (aosTimer == null) {
            val totalMillis = passTime.minus(System.currentTimeMillis())
            aosTimer = object : CountDownTimer(totalMillis, 1000) {
                override fun onFinish() {
                    Toast.makeText(activity, "Time is up!", Toast.LENGTH_SHORT).show()
                    this.cancel()
                    resetTimer()
                }

                override fun onTick(millisUntilFinished: Long) {
                    timeToAos.text = String.format(
                        "AOS -%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                    )
                }
            }
            (aosTimer as CountDownTimer).start()
        } else {
            resetTimer()
        }
    }

    private fun resetTimer() {
        if (aosTimer != null) {
            (aosTimer as CountDownTimer).cancel()
            aosTimer = null
        }
        timeToAos.text = String.format("AOS -%02d:%02d:%02d", 0, 0, 0)
    }
}