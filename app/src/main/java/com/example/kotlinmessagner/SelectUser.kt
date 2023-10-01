package com.example.kotlinmessagner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.kotlinmessagner.databinding.SelectuserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class SelectUser : AppCompatActivity() {
    private lateinit var binding: SelectuserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Select User"

        binding = SelectuserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fetchUsersData()
    }

companion object{
    val User_Key = "USER_KEY"
}

  fun fetchUsersData(){
      val ref = FirebaseDatabase.getInstance().getReference("/users")

      ref.addListenerForSingleValueEvent(object : ValueEventListener{
          override fun onDataChange(snapshot: DataSnapshot) {
              val adapter = GroupAdapter<GroupieViewHolder>()

              snapshot.children.forEach {

                  val user = it.getValue(User::class.java)
                  if (user != null) {
                      adapter.add(Useritem(user))
                  }}
              // item is rendering each row
              adapter.setOnItemClickListener { item, view ->
                  val useritem = item as Useritem
                  val intent = Intent(view.context,ChatlogActivity::class.java)
                  // paasing username here by getting it from useritem
//                  intent.putExtra(User_Key,useritem.user.username)
             intent.putExtra(User_Key,useritem.user)
                  startActivity(intent)
finish()
              }

              binding.recyclerViewNewmessage.adapter = adapter
          }

          override fun onCancelled(error: DatabaseError) {
          }

      })



  }
}
