package com.example.kotlinmessagner

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.example.kotlinmessagner.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar()?.hide(); //hide the title bar
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonLogin.setOnClickListener{
            login()
        }



        binding.backToRegitserLogin.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }


    fun login() {

        val email = binding.loginEmailaddress.text.toString()
        val password = binding.loginTextpassword.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this," Please Enter Email and Password", Toast.LENGTH_LONG).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                } else {
                    Toast.makeText(this, "Succesfully Login", Toast.LENGTH_LONG).show()
                        val intent = Intent(this,LatestMessengerActivity::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

                    startActivity(intent)

                }
            }
            .addOnFailureListener() {
                Toast.makeText(this, "Incorrect  Login Details", Toast.LENGTH_LONG).show()
            }

    }
}