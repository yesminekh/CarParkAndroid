package com.example.carypark.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carypark.R
import com.example.carypark.models.Parking
import com.example.carypark.models.Reservation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class IncommingsRidesAdapter(val incommingrideList: MutableList<Reservation>) :  RecyclerView.Adapter<IncommingsRidesAdapter.viewHolder>() {
    var mContext: Context?=null

    class viewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val matricule = itemView.findViewById<TextView>(R.id.reservationmatricule)
        val parking = itemView.findViewById<TextView>(R.id.parking)
        val checkin = itemView.findViewById<TextView>(R.id.checkintime)
        val checkout = itemView.findViewById<TextView>(R.id.checkOutTime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.modelincomingsrides, parent, false)
        this.mContext=parent.getContext()

        return viewHolder(rootView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val dateEntre = incommingrideList[position].dateEntre
        val dateSortie = incommingrideList[position].dateSortie
        val user = incommingrideList[position].user
        val parking = incommingrideList[position].parking
        holder.checkin.text=dateEntre!!
        holder.checkout.text=dateSortie!!
        holder.parking.text=parking!!.adresse
        holder.matricule.text=user!!.car

    }

    override fun getItemCount(): Int {
        return incommingrideList.size
    }


}