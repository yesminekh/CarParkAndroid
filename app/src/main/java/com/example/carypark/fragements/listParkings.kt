package com.example.carypark.fragements

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carypark.Adapter.ParkingsAdapter
import com.example.carypark.R
import com.example.carypark.models.Service.RetrofitApi
import com.example.carypark.models.Parking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class listParkings : Fragment() {
    lateinit var mSharedPreferences: SharedPreferences
    lateinit var recylcerAdapter: ParkingsAdapter
    lateinit var carRecyclerView: RecyclerView
    lateinit var cardparking :LinearLayout

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mSharedPreferences=requireActivity().getSharedPreferences("UserPref", AppCompatActivity.MODE_PRIVATE)
        val id=mSharedPreferences.getString("ID","0")

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_list_parkings, container, false)
        val apiInterface = RetrofitApi.create()
        apiInterface.GetMyParkings(id!!).enqueue(object: Callback<List<Parking>> {
            override fun onResponse(call: Call<List<Parking>>, response: Response<List<Parking>>) {
                Log.i("yessss ", response.body().toString())
                if(response.isSuccessful){
                    carRecyclerView = rootView.findViewById(R.id.parkingslist)

                    recylcerAdapter = ParkingsAdapter(response.body()!! as MutableList<Parking>)
                    carRecyclerView.adapter = recylcerAdapter

                    carRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,
                        false)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())                }
            }
            override fun onFailure(call: Call<List<Parking>>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
        return rootView
    }


}