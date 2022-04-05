package com.example.carypark.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.carypark.R
import com.example.carypark.models.Parking
import com.example.carypark.models.Reservation

class ParkingsAdapter (val parkingList: MutableList<Parking>) :RecyclerView.Adapter<ParkingsAdapter.viewHolder>()
{
    fun deleteItem(i :Int){
        parkingList.removeAt(i)
        notifyDataSetChanged()
    }
    fun addItem(i : Int , parking : Parking){
        parkingList.add(i,parking)
        notifyDataSetChanged()

    }

    class viewHolder (itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val address = itemView.findViewById<TextView>(R.id.adresse)
        val nbrplace = itemView.findViewById<TextView>(R.id.nbrplace)
        val price = itemView.findViewById<TextView>(R.id.price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingsAdapter.viewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.modellistparkings, parent, false)
        return viewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ParkingsAdapter.viewHolder, position: Int) {
        val adr=parkingList[position].adresse
        val place=parkingList[position].nbrPlace
        val prix=parkingList[position].prix
        holder.address.text=adr
        holder.nbrplace.text=place
        holder.price.text=prix+""+"DT/H"
    }

    override fun getItemCount(): Int {
        return parkingList.size
    }
}