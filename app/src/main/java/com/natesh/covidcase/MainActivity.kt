package com.natesh.covidcase

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.natesh.covidcase.pojo.CovidModel
import com.natesh.covidcase.pojo.Statewise
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val stateArray = ArrayList<String>()
    private var stateList = ArrayList<Statewise>()
    private lateinit var stateAdapter: ArrayAdapter<String>
    private lateinit var stateSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        stateSpinner = spinner_state
        stateSpinner.onItemSelectedListener = this

        jsonSpinnerData()

    }

    private fun jsonSpinnerData() {
        val apiResponse = ApiService.serviceRequest()
        apiResponse.getSpinnerList().enqueue(object :
            Callback<CovidModel> {
            override fun onFailure(call: Call<CovidModel>, t: Throwable) {
//                cancelDialog()
            }

            override fun onResponse(
                call: Call<CovidModel>,
                response: Response<CovidModel>
            ) {
//                cancelDialog()
                if (response.isSuccessful) {

                    stateList.addAll(response.body()!!.statewise)

                    for (state in response.body()!!.statewise) {
                        if (state.state.equals("Total"))
                        stateArray.add("All")
                        else
                        stateArray.add(state.state)
                    }

                    stateArray.sort()
                    setUpstateSpinner()
                    Log.e("Result", ""+response.body()!!.statewise.size)
                }
            }
        })
    }

    private fun setUpstateSpinner() {

        stateAdapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            stateArray
        )
        stateSpinner.adapter = stateAdapter
        stateSpinner.setSelection(0)

        stateSpinner.onItemSelectedListener = this


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var state = stateArray[position]

        if (state.equals("All"))
            state = "Total"

        for (stateData in stateList){
            if (state.equals(stateData.state, true)){
                last_date_txt.text = "As on "+stateData.lastupdatedtime
                confirmed_txt.text = stateData.confirmed
                active_txt.text = stateData.active
                recover_txt.text = stateData.recovered
                death_txt.text = stateData.deaths
            }
        }

    }
}