package com.example.carypark.fragements

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.example.carypark.R
import com.example.carypark.models.Service.RetrofitApi
import com.example.carypark.databinding.FragmentMapreservationBinding
import com.example.carypark.models.Parking
import com.example.carypark.reservation
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var PRIX="PRIX"
var ID_Parking="IDPARKING3" +
        "3"
var ID="id"
var ADRESSE="ADRESSE"
var NBRPLACE="NBR"
lateinit var parkingAll:Parking

class mapreservation : Fragment() ,MapboxMap.OnMarkerClickListener {
    lateinit var mSharedPreferences: SharedPreferences
    @MapboxExperimental
    private lateinit var viewAnnotationManager1: ViewAnnotationManager
    private lateinit var pointAnnotationManager: PointAnnotationManager

    var mapView: MapView? = null
    var mContext: Context?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        val binding = FragmentMapreservationBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_mapreservation, container, false)
        mapView = rootView.findViewById(R.id.mapView1)

        val viewAnnotationManager1 = binding.mapView1.viewAnnotationManager
        val activity = activity as Context
        this.mContext=container!!.getContext()


        mapView?.getMapboxMap()?.loadStyleUri(
            Style.MAPBOX_STREETS,
            object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {

                }
            })

        val apiInterface = RetrofitApi.create()
        apiInterface.GetAllParkings().enqueue(object: Callback<List<Parking>> {
            override fun onResponse(call: Call<List<Parking>>, response: Response<List<Parking>>) {
                Log.i("yessss", response.body().toString())
                if(response.isSuccessful){
                    println(response.body().toString())
                    for(parking in response.body()!!){

                        val long=parking.longitude!!.toDouble();
                        val lat= parking.latitude!!.toDouble()
                        val id=parking._id!!.toString()
                        addAnnotationToMap(parking)


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

        return rootView }

    /*private fun showDialogue2() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.activity_reservation)
        dialog.show()
    }*/


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





    fun basicAlert(parking: Parking){

        val builder = AlertDialog.Builder(context)

        with(builder)
        {

            val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                val mainIntent = Intent(mContext!!, reservation::class.java).apply {
                    putExtra(PRIX, parking.prix)
                    putExtra(ADRESSE, parking.adresse)
                    putExtra(ID_Parking,parking._id)

                }


                startActivity(mainIntent)
            }
            val negativeButtonClick = { dialog: DialogInterface, which: Int ->
                Toast.makeText(context,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }


            setTitle(  parking.nbrPlace+" available places " )
            setMessage("make reservation ")
            setPositiveButton("Oui", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton(android.R.string.no, negativeButtonClick)
            //setNeutralButton("Maybe", neutralButtonClick)
            show()
        }


    }


    private companion object {
        const val SELECTED_ADD_COEF_PX = 25
        const val STARTUP_TEXT = "Click on a map to add a view annotation."
    }

    private fun addAnnotationToMap(parking:Parking) {
// Create an instance of the Annotation API and get the PointAnnotationManager.
        bitmapFromDrawableRes( mContext!!,
            R.drawable.ic_baseline_local_parking_24
        )?.let {


            val annotationApi = mapView?.annotations

            val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
            pointAnnotationManager?.addClickListener(object : OnPointAnnotationClickListener {
                override fun onAnnotationClick(annotation: PointAnnotation): Boolean {
                        basicAlert(parking)

                    return true

                }
            })

// Set options for the resulting symbol layer.
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()

// Define a geographic coordinate.
                .withPoint(com.mapbox.geojson.Point.fromLngLat(parking.longitude!!, parking.latitude!!))
// Specify the bitmap you assigned to the point annotation
// The bitmap will be added to map style automatically.
                .withIconImage(it)
            //.withTextField("  ")
            //.withTextColor("@Color/dgreen_2")
            //.withDraggable(true)


// Add the resulting pointAnnotation to the map.
            pointAnnotationManager?.create(pointAnnotationOptions)
        }

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        println("marker"+marker)
       // addAnnotationToMap(marker.position.latitude,marker.position.longitude)
        basicAlert(parkingAll)
        println("off ya")
        return true

    }


}
