package com.example.carypark.fragements

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carypark.Adapter.HistoryAdapter
import com.example.carypark.R
import com.example.carypark.models.Service.RetrofitApi
import com.example.carypark.models.Reservation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class History : Fragment() {
    lateinit var mSharedPreferences: SharedPreferences
    private  lateinit var delete :String

    lateinit var recylcerChampionAdapter: HistoryAdapter
    lateinit var carRecyclerView: RecyclerView
     var reservationList: ArrayList<Reservation>? = ArrayList<Reservation>()
    var park="Park"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mSharedPreferences=requireActivity().getSharedPreferences("UserPref", AppCompatActivity.MODE_PRIVATE)
        val id=mSharedPreferences.getString("ID","0")


        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_history, container, false)

        val apiInterface = RetrofitApi.create()
        apiInterface.GetAllReservation(id!!).enqueue(object: Callback<List<Reservation>> {
            override fun onResponse(call: Call<List<Reservation>>, response: Response<List<Reservation>>) {
                Log.i("yessss", response.body().toString())

                if(response.isSuccessful){
                    carRecyclerView = rootView.findViewById(R.id.historyrecycleview)

                    recylcerChampionAdapter = HistoryAdapter(response.body()!! as MutableList<Reservation>)
                    carRecyclerView.adapter = recylcerChampionAdapter

                    carRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,
                        false)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())                }
            }
            override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })


        return rootView
    }
    /*override fun onSwiped(viewHolder:RecyclerView.ViewHolder,direction:Int){
        var position=viewHolder.adapterPosition
        when(direction){
            ItemTouchHelper.LEFT->{
                delete = reservationList?.get(position).toString()
                reservationList?.removeAt(position)
                recylcerChampionAdapter.notifyItemRangeRemoved(position)


            }
        }
    }
    private  var simpleCallback=object :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))
    {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
        }

    }*/
}