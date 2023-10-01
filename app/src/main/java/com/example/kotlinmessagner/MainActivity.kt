package com.example.kotlinmessagner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.example.kotlinmessagner.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    var selectedPhotoUri  : Uri ?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title

        getSupportActionBar()?.hide(); //hide the title bar
         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
performregister()
        }
        FirebaseApp.initializeApp(this)

        binding.alreadyhaveaccountRegister.setOnClickListener {
            Log.d("MainActivity","Already Have Account..!")
            // launching login Activity
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.selectPhoto.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type= "image/*"
            startActivityForResult(intent,0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            binding.selectphotoImageviewRegister.setImageBitmap(bitmap)

            binding.selectPhoto.alpha = 0f

        }
    }

    fun performregister(){
        val email= binding.emailEdittextRegister.text.toString()
        val password = binding.passwordEdittextRegister.text.toString()
        val username = binding.usernameEdittextRegister.text.toString()
        val image = binding.selectPhoto.text.toString()
        Log.d("MainActivity","your Email is $email")
        Log.d("MainActivity","your Password is $password")

        if(selectedPhotoUri==null)
        {
            Toast.makeText(this, "Please select a profile image", Toast.LENGTH_SHORT).show()
            return
        }
       else if(username.isEmpty()){
            Toast.makeText(this," Please Enter  Username", Toast.LENGTH_LONG).show()
         return
        }
        else if(email.isEmpty()){
            Toast.makeText(this," Please Enter Email", Toast.LENGTH_LONG).show()
            return
        }

        else {
            if (password.isEmpty()) {
                Toast.makeText(this, " Please Enter Correct Password", Toast.LENGTH_LONG).show()
            }

        }

        //Returns an instance of this class corresponding to the default FirebaseApp instance.
        // Firebase Authentication provides backend services for easy use the
        // SDKs and in-built UI libraries to authenticate the user in the application.
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful){
                    return@addOnCompleteListener

                }
                else{
                    Toast.makeText(this,"Registration Succesfull", Toast.LENGTH_LONG).show()
                    Log.d("Main","Successful User Created : ${it.result.user?.uid}" )

                    uploadimagetoFirebase()
                }
            }

            .addOnFailureListener {
                Toast.makeText(this,"Enter Correct Email Address and password atleast 8 chracters", Toast.LENGTH_SHORT).show()
            }
    }

 @SuppressLint("SuspiciousIndentation")
 private   fun uploadimagetoFirebase(){

     val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
     ref.putFile(selectedPhotoUri!!)
         .addOnSuccessListener {
             Toast.makeText(this, "Picture Uploaded Successfully", Toast.LENGTH_LONG).show()
             ref.downloadUrl.addOnSuccessListener {

//                 Toast.makeText(this, "Picture Upload Succesfully Uri : ${it}", Toast.LENGTH_LONG).show()

                 Log.e("MainActivity","File location:  $it")

                 SaveUserToFirebaseDatabase(it.toString())
             }
         }
         .addOnFailureListener { e ->
             Toast.makeText(this, "Picture Upload Failed: ${e.message}", Toast.LENGTH_LONG).show()
             Log.e("MainActivity", "Error uploading image: ${e.message}", e)
         }

 }


    private  fun SaveUserToFirebaseDatabase(profileImageUrl: String){

        val uid = FirebaseAuth.getInstance().uid ?: ""

  val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid,binding.usernameEdittextRegister.text.toString(),profileImageUrl)

        ref.setValue(user)

            .addOnSuccessListener {
                Toast.makeText(this, "Save Data Succesfully in Database", Toast.LENGTH_LONG).show()
             val intent = Intent(this, LatestMessengerActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, " Data in not saved in  Database", Toast.LENGTH_LONG).show()
            }
    }

}
