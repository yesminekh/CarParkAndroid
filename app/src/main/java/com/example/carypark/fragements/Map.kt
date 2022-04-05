package com.example.carypark.fragements

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.carypark.R
import com.example.carypark.models.Service.RetrofitApi
import com.example.carypark.databinding.FragmentMapBinding
import com.example.carypark.models.Parking
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var latitude0=0.0
var longitude0 =0.0
lateinit var mSharedPref: SharedPreferences

class Map : Fragment() , OnMapClickListener {
    lateinit var mSharedPreferences: SharedPreferences


    fun basicAlert(lat: Double,long : Double){

        val builder = AlertDialog.Builder(context)

        with(builder)
        {

            val positiveButtonClick = { dialog: DialogInterface, which: Int ->


                showDialogue2()

                latitude0= lat
                longitude0 = long
                println("########"+ latitude0+"######"+ longitude0)


            }
            val negativeButtonClick = { dialog: DialogInterface, which: Int ->
                Toast.makeText(context,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }


            setTitle( "wanna add a new parking sport here" )
            setMessage("lat="+lat+"\nlon="+long)
            setPositiveButton("Oui", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton(android.R.string.no, negativeButtonClick)
            //setNeutralButton("Maybe", neutralButtonClick)
            show()
        }


    }

    @MapboxExperimental
    private lateinit var viewAnnotationManager1: ViewAnnotationManager
    var mapView: MapView? =null
    var mContext: Context?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        mSharedPreferences=requireActivity().getSharedPreferences("UserPref", AppCompatActivity.MODE_PRIVATE)
        val id=mSharedPreferences.getString("ID","0")
        val binding = FragmentMapBinding.inflate(layoutInflater)
        this.mContext=container!!.getContext()


        // Inflate the layout for this fragment
        val rootView=inflater.inflate(R.layout.fragment_map, container, false)
        viewAnnotationManager1 = binding.mapView.viewAnnotationManager
        mapView=rootView.findViewById(R.id.mapView)
        mapView?.getMapboxMap()?.apply {
            loadStyleUri(Style.MAPBOX_STREETS){
                addOnMapClickListener(
                    this@Map
                )

            }
        }
        val apiInterface = RetrofitApi.create()
        apiInterface.GetMyParkings(id!!).enqueue(object: Callback<List<Parking>> {
            override fun onResponse(call: Call<List<Parking>>, response: Response<List<Parking>>) {
                Log.i("yessss", response.body().toString())
                if(response.isSuccessful){
                    println(response.body().toString())
                    for(parking in response.body()!!){
                        val long=parking.longitude!!.toDouble();
                        val lat= parking.latitude!!.toDouble()
                        addAnnotationToMap(lat,long)
                    }

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
    private fun showDialogue2() {
        val dialog = BottomSheetDialog(mContext!!)
        dialog.setContentView(R.layout.activity_ajoutparking)
        val adresse=dialog.findViewById<TextView>(R.id.adresse)
        val price=dialog.findViewById<TextView>(R.id.price)
        val places=dialog.findViewById<TextView>(R.id.places)
        val latitude=dialog.findViewById<TextView>(R.id.latitude)
        val longitude=dialog.findViewById<TextView>(R.id.longitude)
        val add=dialog.findViewById<Button>(R.id.add)
        latitude!!.text=latitude0.toString()
        longitude!!.text= longitude0.toString()

        mSharedPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        val id: String = mSharedPref.getString("ID","null").toString()

        add?.setOnClickListener{ var parking = Parking()
            parking.adresse=adresse!!.text.toString()
            parking.prix=price!!.text.toString()
            parking.nbrPlace= places!!.text.toString()
            parking.latitude= latitude0
            parking.longitude= longitude0
            println("parkingg  "+parking.latitude)
            println(parking.latitude.toString()+parking.longitude.toString())
            parking.user=id
            val apiuser = RetrofitApi.create().addpark(parking)

            apiuser.enqueue(object: Callback<Parking> {
                override fun onResponse(call: Call<Parking>, response: Response<Parking>) {
                    if(response.isSuccessful ) {
                        println("###########################################")
                        println(response.body())

                    }
                    else
                    {
                        Toast.makeText(context, "parking invailable ", Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(call: Call<Parking>, t: Throwable) {
                    Toast.makeText(context, "erreur server", Toast.LENGTH_LONG).show()
                }
            })


        }


        dialog.show()    }


    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }






    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }



    @SuppressLint("SetTextI18n")
    private fun addViewAnnotation(point: com.mapbox.geojson.Point) {
        val viewAnnotation = viewAnnotationManager1.addViewAnnotation(
            resId = R.layout.viewannotations,
            options = viewAnnotationOptions {
                geometry(point)
                allowOverlap(true)
            }

        )
        addAnnotationToMap(point.latitude(),point.longitude())
    }


    private companion object {
        const val SELECTED_ADD_COEF_PX = 25
        const val STARTUP_TEXT = "Click on a map to add a view annotation."
    }
    private fun addAnnotationToMap(lat: Double,long : Double) {
// Create an instance of the Annotation API and get the PointAnnotationManager.
        bitmapFromDrawableRes(
            mContext!!,
            R.drawable.ic_baseline_local_parking_24
        )?.let {

            val annotationApi = mapView?.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
// Set options for the resulting symbol layer.
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
// Define a geographic coordinate.

                .withPoint(com.mapbox.geojson.Point.fromLngLat(long,lat ))
// Specify the bitmap you assigned to the point annotation
// The bitmap will be added to map style automatically.
                .withIconImage(it)
            //.withTextField(args.name)
            //.withTextColor("@Color/dgreen_2")
            //.withDraggable(true)

// Add the resulting pointAnnotation to the map.
            pointAnnotationManager?.create(pointAnnotationOptions)
            //basicAlert(lat,long)

        }
    }

    override fun onMapClick(point: com.mapbox.geojson.Point): Boolean {
        addViewAnnotation(point)
        basicAlert(point.latitude(), point.longitude())

        return true
    }


}