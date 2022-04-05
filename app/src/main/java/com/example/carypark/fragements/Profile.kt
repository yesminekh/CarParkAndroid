package com.example.carypark.fragements

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.carypark.R
import com.github.dhaval2404.imagepicker.ImagePicker


class Profile : Fragment() {
    private var selectedImageUri: Uri? = null
lateinit var image:ImageView
lateinit var emailedit:TextView
lateinit var fullnameedit :TextView
lateinit var fullnameedit1 :TextView
lateinit var cinedit :TextView
lateinit var emailedit1 :TextView
lateinit var editprofilePic:ImageView
lateinit var phoneedit :TextView
lateinit var phoneedit1 :TextView
lateinit var addressedit :TextView
lateinit var matricul :TextView
lateinit var matricul1 :TextView



    lateinit var mSharedPref: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mSharedPref = this.requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        val rootview=inflater.inflate(R.layout.fragment_profile, container, false)

        image=rootview.findViewById(R.id.userimage)
        emailedit=rootview.findViewById(R.id.editemail)
        emailedit1=rootview.findViewById(R.id.editemail1)
        editprofilePic=rootview.findViewById(R.id.editprofilepic)
        fullnameedit=rootview.findViewById(R.id.editname)
        fullnameedit1=rootview.findViewById(R.id.editname1)
        phoneedit=rootview.findViewById(R.id.editphone)
        phoneedit1=rootview.findViewById(R.id.phoneedit)
        cinedit=rootview.findViewById(R.id.cinedit)
        addressedit=rootview.findViewById(R.id.editadresse)
        matricul=rootview.findViewById(R.id.editcar)
        matricul1=rootview.findViewById(R.id.matriculeedit)


        val picStr: String = mSharedPref.getString("photo","yesmine").toString()
        val email: String = mSharedPref.getString("LOGIN","yesmine").toString()
        val fullname: String = mSharedPref.getString("FULLNAME","yesmine.khayati").toString()
        val car: String = mSharedPref.getString("CAR","117tu9887").toString()
        val addresse: String = mSharedPref.getString("ADDRESS","nabeul").toString()
        val phone: String = mSharedPref.getString("PHONE","29385320").toString()
        val cin: String = mSharedPref.getString("CIN","09877635").toString()

        val ppp = "https://carypark-backend.herokuapp.com/ api/user/download/"+picStr
        Glide.with(this).load(Uri.parse(ppp)).into(image)
        emailedit.text=email;
        emailedit1.text=email;
        fullnameedit.text=fullname
        fullnameedit1.text=fullname
        matricul.text=car
        matricul1.text=car
        phoneedit.text=phone
        phoneedit1.text=phone
        cinedit.text=cin
        addressedit.text=addresse
        editprofilePic.setOnClickListener(
            View.OnClickListener {
                ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }



        )

        return rootview
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            selectedImageUri = data?.data
            image?.setImageURI(selectedImageUri)

        }
    }


}