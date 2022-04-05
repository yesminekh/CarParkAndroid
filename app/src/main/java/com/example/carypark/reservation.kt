package com.example.carypark
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.carypark.models.Service.RetrofitApi
import com.example.carypark.fragements.ADRESSE
import com.example.carypark.fragements.ID_Parking
import com.example.carypark.fragements.PRIX
import com.example.carypark.models.Reservation
import com.example.carypark.models.ReservationParking
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class reservation : AppCompatActivity(),DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute=0


    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var SavedHour = 0
    var SavedHour1 = 0

    var SavedMinute=0

    lateinit var button:ImageButton
    lateinit var date:TextView
    lateinit var checkin:TextView
    lateinit var checkout:TextView
    lateinit var picker:NumberPicker
    lateinit var dure:TextView
    lateinit var price:TextView
    lateinit var park:TextView
    lateinit var next:Button
    lateinit var mSharedPref: SharedPreferences
    var duree=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)
        button=findViewById(R.id.pickdatebutton)
        date=findViewById(R.id.saveddate)
        checkin=findViewById(R.id.savedcheckin)
        checkout=findViewById(R.id.savedcheckout)
        picker=findViewById(R.id.picker)
        price=findViewById(R.id.price)
        park=findViewById(R.id.parkingadresse)
        dure=findViewById(R.id.dure)
        next=findViewById(R.id.Continue)

            mSharedPref = this.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val idPrking = intent.getStringExtra(ID_Parking).toString()
        val prix = intent.getStringExtra(PRIX).toString()
        val adresse = intent.getStringExtra(ADRESSE).toString()
        park.text=adresse


        val id: String = mSharedPref.getString("ID","null").toString()
            pickDate()

        picker.minValue=1
        picker.maxValue=23
        picker.wrapSelectorWheel=true
        picker.setOnValueChangedListener{picker,oldValue,newValue->dure.text=" Duration $newValue H"
            val pricee=(prix.toInt()*newValue)
            price.text=pricee.toString()

            duree=newValue

        }
            next.setOnClickListener{

               var reservation = ReservationParking()
                reservation.parking=idPrking
                reservation.dateEntre=checkin.text.toString()
                reservation.dateSortie= checkout.text.toString()
                reservation.user=id

                 print(reservation.toString())
           val apiuser = RetrofitApi.create().addReservation(reservation)

            apiuser.enqueue(object: Callback<Reservation> {
                override fun onResponse(call: Call<Reservation>, response: Response<Reservation>) {
                    if(response.isSuccessful ) {
                        println("###########################################")
                        println(response.body())
                        Toast.makeText(applicationContext," Reservation request was send ",Toast.LENGTH_SHORT).show()

                    }
                    else
                    {
                        Toast.makeText(applicationContext, " erreur, please try later! ", Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(call: Call<Reservation>, t: Throwable) {
                    Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
                }
            })






        }
    }



    private fun showDialogpayement() {
        val dialog1 = BottomSheetDialog(this)
        dialog1.setContentView(R.layout.payement)
        val ttprica=findViewById<TextView>(R.id.price12)
        val locale=findViewById<Button>(R.id.pay_with_google_button)
        val cardd=findViewById<Button>(R.id.pay_with_card_button)
        ttprica?.text=price.text.toString()
        locale?.setOnClickListener {
            Toast.makeText(this, "locale payement ", Toast.LENGTH_SHORT).show()
        }
        cardd?.setOnClickListener {

        }


        dialog1.show()
    }

    private  fun getDateTimeCalendar(){
        val cal:Calendar= Calendar.getInstance()
        day=cal.get(Calendar.DAY_OF_MONTH)
        month=cal.get(Calendar.MONTH)
        year=cal.get(Calendar.YEAR)
        hour=cal.get(Calendar.HOUR)
        minute=cal.get(Calendar.MINUTE)
    }
    private  fun pickDate(){
        button.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(this,this,year,month,day).show()
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
    savedDay=dayOfMonth
    savedMonth=month
    savedYear=year
        getDateTimeCalendar()
        TimePickerDialog(this,this,hour,minute,true).show()

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
       SavedHour=hourOfDay
       SavedMinute=minute
        date.text="$savedDay/$savedMonth/$savedYear"
        checkin.text= date.text as String +"T$SavedHour:$SavedMinute"
        SavedHour1=SavedHour+duree
        checkout.text=date.text as String +"T$SavedHour1:$SavedMinute"


    }




}