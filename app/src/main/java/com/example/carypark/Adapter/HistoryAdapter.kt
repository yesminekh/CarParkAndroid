package com.example.carypark.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.carypark.R
import com.example.carypark.models.Service.RetrofitApi
import com.example.carypark.models.Qrj
import com.example.carypark.models.Reservation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var usern=""
var carn=""
var parka=""
var parkp=""
var chekin1=""
var chekout1=""
var cin1=""
var phone1=""

class HistoryAdapter (val reservationList: MutableList<Reservation>) :  RecyclerView.Adapter<HistoryAdapter.viewHolder>()
{
    lateinit var mSharedPref: SharedPreferences
     var mContext: Context?=null
    private var qrImage : Bitmap? = null

    class viewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val prix = itemView.findViewById<TextView>(R.id.price)
        val park = itemView.findViewById<TextView>(R.id.park)
        val nom = itemView.findViewById<TextView>(R.id.matricule)
        val checkin = itemView.findViewById<TextView>(R.id.check_in_time)
        val checkout = itemView.findViewById<TextView>(R.id.check_out_time)
        val qr = itemView.findViewById<FloatingActionButton>(R.id.qrcode1)
        val history = itemView.findViewById<CardView>(R.id.history)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.historymodel, parent, false)


        this.mContext=parent.getContext()


        return viewHolder (rootView)
    }




    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        mSharedPref = mContext?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        val email: String = mSharedPref.getString("email", "").toString()

        usern=reservationList[position].user?.fullName!!.toString()
        carn=reservationList[position].user?.car!!.toString()
        parkp=reservationList[position].parking?.prix!!.toString()
        parka=reservationList[position].parking?.adresse!!.toString()
        chekin1=reservationList[position].dateEntre!!.toString()
        chekout1=reservationList[position].dateSortie!!.toString()
        cin1=reservationList[position].user?.cin!!.toString()
        phone1=reservationList[position].user?.phone!!.toString()

        val dateEntre = reservationList[position].dateEntre
        val dateSortie = reservationList[position].dateSortie
        val user = reservationList[position].user
        val prix = reservationList[position].parking
        val park = reservationList[position].parking


        holder.nom.text = user!!.car.toString()
        holder.checkin.text = dateEntre!!
        holder.checkout.text = dateSortie!!
        holder.prix.text = prix?.prix.toString()+" "+"Dt/H"
        holder.park.text = park?.adresse.toString()

        holder.qr.setOnClickListener {
            val dialog = BottomSheetDialog(mContext!!)
            dialog.setContentView(R.layout.qrcodedialogue)
           val image=dialog.findViewById<ImageView>(R.id.generate)
            /*  val encoder1=QRGEncoder(holder.nom.toString(),null,QRGContents.Type.TEXT,1000)
             val encoder2=QRGEncoder(holder.checkin.toString(),null,QRGContents.Type.TEXT,1000)
             val encoder3=QRGEncoder(holder.checkout.toString(),null,QRGContents.Type.TEXT,1000)
             val encoder4=QRGEncoder(holder.prix.toString(),null,QRGContents.Type.TEXT,1000)
             val encoder6=QRGEncoder(holder.park.toString(),null,QRGContents.Type.TEXT,1000)
             val information=" name : " + encoder1.bitmap+ "\n chekinTime:"+encoder2.bitmap+"\n checkoutTime : "+encoder3.bitmap + "\n price: "+encoder4.bitmap+ "\n park: "+encoder6 .bitmap
             val encoder5= QRGEncoder(information, null, QRGContents.Type.TEXT,1000)
             image!!.setImageBitmap(encoder5.bitmap )*/
            fun generateQj() {
                val data = Qrj("User Car", reservationList[position].user?.car,
                   "DateEntre", reservationList[position].dateEntre ,
                   "Parking", reservationList[position].parking?.prix,
                    "DateSortie",reservationList[position].dateSortie,
                   "Adresse", reservationList[position].parking?.adresse)

                var gson = Gson()
                var jsonString = gson.toJson(data)
                println(jsonString)

                val writer = QRCodeWriter()
                try {
                    val bitMatrix = writer.encode(jsonString, BarcodeFormat.QR_CODE, 512,512)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bmp = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565)
                    for (x in 0 until width){
                        for (y in 0 until height) {
                            bmp.setPixel(x,y, if(bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                        }
                    }
                    image?.setImageBitmap(bmp)
                } catch (e: WriterException){
                    println("oooo")
                }

            }



            generateQj()
            dialog.show()
        }
        holder.history.setOnClickListener{


            basicAlert(reservationList[position]._id!!)

        }

    }

    private fun basicAlert(id:String) {
        val builder = AlertDialog.Builder(mContext)

        with(builder)
        {
            val positiveButtonClick = { dialog: DialogInterface, which: Int ->


              val apiInterface = RetrofitApi.create()
                apiInterface.delete(id!!).enqueue(object: Callback<Reservation> {
                    override fun onResponse(call: Call<Reservation>, response: Response<Reservation>) {
                        Log.i("C bon tfaskh", response.body().toString())
                        if(response.isSuccessful){
                            println(response.body().toString())
                            Toast.makeText(context, "reservation canceled ", Toast.LENGTH_LONG).show()
                        }
                        else {
                            println("desole")    }
                    }

                    override fun onFailure(call: Call<Reservation>, t: Throwable) {
                        println("failed")

                    }
                }


                )


            }
            val negativeButtonClick = { dialog: DialogInterface, which: Int ->
                showdialoguedetails()

            }


            setNeutralButton("Delete", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton("Details", DialogInterface.OnClickListener(function = negativeButtonClick) )
            //setNeutralButton("Maybe", neutralButtonClick)
            show()
        }

    }

    private fun showdialoguedetails(){


        val dialog = BottomSheetDialog(mContext!!)
        dialog.setContentView(R.layout.reservationdetails)
        val park = dialog.findViewById<TextView>(R.id.park1)
        val price = dialog.findViewById<TextView>(R.id.price1)
        val username = dialog.findViewById<TextView>(R.id.username)
        val car = dialog.findViewById<TextView>(R.id.matricule1)
        val cin = dialog.findViewById<TextView>(R.id.cin1)
        val phone = dialog.findViewById<TextView>(R.id.phone1)
        val checkin = dialog.findViewById<TextView>(R.id.check_in_time1)
        val chekout = dialog.findViewById<TextView>(R.id.check_out_time1)

        park!!.text= parka
        price!!.text= parkp
        username!!.text= usern
        car!!.text= carn
        checkin!!.text= chekin1
        chekout!!.text= chekout1
        cin!!.text= cin1
        phone!!.text= phone1

        dialog.show()

    }


    override fun getItemCount(): Int {
    return reservationList.size
    }


}
