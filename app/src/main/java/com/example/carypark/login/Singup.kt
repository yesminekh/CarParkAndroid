package com.example.carypark.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.carypark.R
import com.example.carypark.models.Service.RetrofitApi
import com.example.carypark.models.User
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.RequestBody.Companion.toRequestBody
import com.github.dhaval2404.imagepicker.ImagePicker

const val RESET01="RESET1"
const val RESET02="RESET2"
const val RESET03="RESET3"
const val RESET04="RESET4"

class Singup : AppCompatActivity() {
    private var ProfilePic: ImageView?=null
    lateinit var  mSharedPref: SharedPreferences
    private var selectedImageUri: Uri? = null
    private lateinit var fab: Button
    private var fullname: EditText?=null
    private var email: EditText?=null
    private var car: EditText?=null
    private var phone: EditText?=null
    private var password: EditText?=null
    private var adresse: EditText?=null
    private var singin:TextView?=null
    private var cin:TextView?=null
    lateinit var role:CheckBox

    private var layoutcin: TextInputLayout?=null
    private var layoutfullname: TextInputLayout?=null
    private var layoutmatricule: TextInputLayout?=null
    private var layoutemail: TextInputLayout?=null
    private var layoutphone: TextInputLayout?=null
    private var layoutpassword: TextInputLayout?=null
    private var layoutadresse: TextInputLayout?=null
    private var btnext:Button?=null

    private fun login(fullname_text: String, email_text: String, password_text: String,
                      phone_text: String, matricule_text: String, adresse_text: String,cin_text:String,
                      role_text:String)
    { val stream = contentResolver.openInputStream(selectedImageUri!!)
        if(stream!=null){
            val request =
                stream?.let { RequestBody.create("image/png".toMediaTypeOrNull(), it.readBytes()) } // read all bytes using kotlin extension
            val profilePicture = request?.let {
                MultipartBody.Part.createFormData(
                    "file",
                    "file.png",
                    it) }
            Log.d("MyActivity", "on finish upload file")

            val apiInterface = RetrofitApi.create()
            val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

            data["fullName"] = fullname_text.toRequestBody(MultipartBody.FORM)
            data["email"] = email_text.toRequestBody(MultipartBody.FORM)
            data["password"] = password_text.toRequestBody(MultipartBody.FORM)
            data["phone"] = phone_text.toRequestBody(MultipartBody.FORM)
            data["car"] = matricule_text.toRequestBody(MultipartBody.FORM)
            data["address"] = adresse_text.toRequestBody(MultipartBody.FORM)
            data["cin"] = cin_text.toRequestBody(MultipartBody.FORM)
            data["role"] = role_text.toRequestBody(MultipartBody.FORM)


            if (profilePicture != null) {
                println("++++++++++++++++++++++++++++++++++++"+profilePicture)
                apiInterface.userSingup(data,profilePicture).enqueue(object:
                    Callback<User> {
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>)
                    {
                        if(response.isSuccessful){
                            Log.i("onResponse goooood", response.body().toString())
                            }
                        else
                        {
                            Log.i("OnResponse not good", response.body().toString()) }
                    }
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        println("noooooooooooooooooo")
                    }
                })
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.singup)
        ProfilePic=findViewById(R.id.imagepick)
        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        fullname=findViewById(R.id.txtFullName)
        email=findViewById(R.id.txtEmail)
        car=findViewById(R.id.txtmatricule)
        phone=findViewById(R.id.txtPhone)
        password=findViewById(R.id.txtpassword)
        adresse=findViewById(R.id.txtadresse)
        layoutfullname=findViewById(R.id.txtLayoutFullName)
        layoutmatricule=findViewById(R.id.txtLayoutMatricule)
        layoutemail=findViewById(R.id.txtLayoutemail)
        layoutphone=findViewById(R.id.txtLayoutPhone)
        layoutpassword=findViewById(R.id.txtLayoutpassword )
        layoutcin=findViewById(R.id.txtLayoutCin)
        layoutadresse=findViewById(R.id.txtlayoutadresse)
        fab = findViewById(R.id.pickButton)
        cin=findViewById(R.id.txtCin)
        role=findViewById(R.id.role)
        btnext=findViewById(R.id.btnNext1)
        singin=findViewById(R.id.singin)


        //retour vers login
        singin!!.setOnClickListener{
            val mainIntent = Intent(this, Login::class.java)
            startActivity(mainIntent)
        }
        fab.setOnClickListener(
            View.OnClickListener {
                ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            })

        btnext!!.setOnClickListener {
            var rolee="null"
            if(validate()){
                if(role.isChecked()){
                     rolee="ParkOwner"
                }
                else{
                    rolee="NormalUser"
                }

            val email_text = email!!.text.toString().trim()
            val password_text = password!!.text.toString().trim()
            val fullname_text = fullname!!.text.toString().trim()
            val phone_text = phone!!.text.toString().trim()
            val matricule_text = car!!.text.toString().trim()
            val adresse_text = adresse!!.text.toString().trim()
            val cin_text = cin!!.text.toString().trim()
                role.text=rolee.toString()
            val role_text = role!!.text.toString().trim()


                println("########################################"+email!!.text.toString())
                println("########################################"+role!!.text.toString())

                login(fullname_text, email_text, password_text, phone_text, matricule_text, adresse_text,cin_text,role_text)

                val mainIntent = Intent(this, Login::class.java)
                startActivity(mainIntent)


            }
        }

        }








    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            selectedImageUri = data?.data
            ProfilePic
                ?.setImageURI(selectedImageUri)

        }
    }





    private fun validate(): Boolean {


        if(selectedImageUri == null){
            Toast.makeText(applicationContext, "image is required", Toast.LENGTH_LONG).show()
            return false
        }


            if(fullname?.text!!.isEmpty()){
            layoutfullname!!.error="Must not be empty"
            return false}
        if(email?.text!!.isEmpty())
        { layoutemail!!.error="Please enter Your Email"
            return false
        }
        if(car?.text!!.isEmpty()){
            layoutmatricule!!.error="Must not be empty"
            return false}

        if(phone?.text!!.isEmpty()){
            layoutphone!!.error="Must not be empty"
            return false}
        if(cin?.text!!.isEmpty()){
            layoutcin!!.error="Must not be empty"
            return false}
        if(password?.text!!.isEmpty()){
            layoutpassword!!.error="Must not be empty"
            return false}
        if(adresse?.text!!.isEmpty()){
            layoutpassword!!.error="Must not be empty"
            return false}

        return true
    }
}
