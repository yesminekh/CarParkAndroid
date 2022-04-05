package com.example.carypark.fragements

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carypark.Adapter.IncommingsRidesAdapter
import com.example.carypark.R
import com.example.carypark.models.Service.RetrofitApi
import com.example.carypark.models.Reservation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IncommingRides : Fragment() {
    lateinit var mSharedPreferences: SharedPreferences

    lateinit var recylcerridesAdapter: IncommingsRidesAdapter
    lateinit var carRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mSharedPreferences=requireActivity().getSharedPreferences("UserPref", AppCompatActivity.MODE_PRIVATE)
        val id=mSharedPreferences.getString("ID","0")


        // Inflate the layout for this fragment
        val rootView= inflater.inflate(R.layout.fragment_incomming_rides, container, false)
        val apiInterface = RetrofitApi.create()
        apiInterface.incoming(id!!).enqueue(object: Callback<List<Reservation>> {
            override fun onResponse(call: Call<List<Reservation>>, response: Response<List<Reservation>>) {
                Log.i("yessss", response.body().toString())
                if(response.isSuccessful){
                    carRecyclerView = rootView.findViewById(R.id.incomingsrides)

                    recylcerridesAdapter = IncommingsRidesAdapter(response.body()!! as MutableList<Reservation>)
                    carRecyclerView.adapter = recylcerridesAdapter
                    carRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,
                        false)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Toast.makeText(context, R.string.description, Toast.LENGTH_LONG).show()

                    Log.i("nooooo", response.body().toString())                }
            }
            override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })


        return rootView
    }


}