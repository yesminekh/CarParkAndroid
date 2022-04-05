package com.example.carypark.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.*
import com.example.carypark.MainActivity
import com.example.carypark.MainActivityowner
import com.example.carypark.R
import com.example.carypark.models.Service.RetrofitApi
import com.example.carypark.models.User
import com.example.carypark.models.UserResetPassword
import com.google.android.material.textfield.TextInputLayout
import retrofit2.*
const val IS_REMEMBRED = "IS_REMEMBRED"
const val PREF_NAME = "LOGIN_PREF_LOL"
const val LOGIN = "LOGIN"
const val PASSWORD = "PASSWORD"
val ROLE="ROLE"
class Login : AppCompatActivity() {
    lateinit var mSharedPref: SharedPreferences

    private var email:EditText?=null
    private var password:EditText?=null
    private var fpassword:TextView?=null
    private var singup:TextView?=null
    private var social:TextView?=null


    lateinit var rbme: CheckBox

    private var txtlayoutemail:TextInputLayout?=null
    private var txtlayoutpassword:TextInputLayout?=null
    private var btnlogin:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title="welcome"
        email=findViewById(R.id.txtemail)
        password=findViewById(R.id.txtpassword)
        fpassword=findViewById<TextView>(R.id.txtfpswd)
        singup=findViewById(R.id.singup)
        txtlayoutemail=findViewById(R.id.txtLayoutemail)
        txtlayoutpassword=findViewById(R.id.txtLayoutpassword)
        btnlogin=findViewById(R.id.btnlogin)
        mSharedPref = getSharedPreferences("UserPref", MODE_PRIVATE)
        rbme=findViewById(R.id.cbrmbr)

        if (mSharedPref.getBoolean(IS_REMEMBRED, false)){
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        btnlogin!!.setOnClickListener { var user = UserResetPassword()
            user.email = email?.text.toString()
            user.password = password?.text.toString()
            val apiuser = RetrofitApi.create().userLogin(user)
            if(validate()){
            apiuser.enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful ){
                        println("assslema !!!!")
                        print(response.body()?.photo.toString())

                        mSharedPref.edit().apply {
                            putString("ID",response.body()?._id.toString())
                            putString("LOGIN", response.body()?.email.toString())
                            putString("PASSWORD", response.body()?.password.toString())
                            putString("photo", response.body()?.photo.toString())
                            putString("CAR", response.body()?.car.toString())
                            putString("FULLNAME", response.body()?.fullName.toString())
                            putString("ADDRESS", response.body()?.address.toString())
                            putString("PHONE", response.body()?.phone.toString())
                            putString("CIN", response.body()?.cin.toString())
                            putString("ROLE", response.body()?.role.toString())
                           println(response.body())

                            if(rbme.isChecked()){
                                putBoolean("IS_REMEMBRED", true)

                            }
                            println("###########################################")
                            println(response.body())
                            println("###########################################")
                            putString("tokenUser", response.body()?.token.toString())
                        }.apply()
                        if(response.body()?.role.equals("NormalUser"))
                        {
                            println(response.body()?.role)

                            val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        }
                        else {
                            val intent = Intent(applicationContext, MainActivityowner::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        }

                    }

                    else {

                        Toast.makeText(applicationContext, "Failed to Login , email or password incorrect ", Toast.LENGTH_LONG).show()
                    } }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    println("assslema !!!!2")

                    Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
                }
            }) } }



        fpassword!!.setOnClickListener{
            val mainIntent = Intent(this, ForgetPassword ::class.java)
            startActivity(mainIntent)
            fpassword!!.movementMethod = LinkMovementMethod.getInstance();
        }
        singup!!.setOnClickListener{
            val mainIntent = Intent(this, Singup::class.java)
            startActivity(mainIntent) }
    }



    private fun validate(): Boolean {
        if(email?.text!!.isEmpty())
        {
            txtlayoutemail!!.error="Please enter Your Email"
            return false

        }
        if(password?.text!!.isEmpty())
        {
            txtlayoutpassword!!.error="Please enter Your Password"
            return false

        }
      return true
    }


}

