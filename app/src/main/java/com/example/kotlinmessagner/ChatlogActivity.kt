package com.example.kotlinmessagner

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.IntentCompat.getParcelableExtra
import com.example.kotlinmessagner.databinding.ActivityChatlogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

// Activity where we are loading chats

class ChatlogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatlogBinding
    val adapter = GroupAdapter<GroupieViewHolder>()
var toUser : User?=null
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// passing the username here
//         val username = intent.getStringExtra(SelectUser.User_Key)
         toUser = intent.getParcelableExtra<User>(SelectUser.User_Key)

            supportActionBar?.title = toUser?.username


        binding = ActivityChatlogBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //   SetupDummyDataChat()
        binding.recyclerviewChatbox.adapter=adapter
    Fetchmessagesinchat()
        binding.sendChatButton.setOnClickListener {
Toast.makeText(this, "Message Send ",Toast.LENGTH_SHORT).show()
            performSendMessage()

        }

    }


//    private fun SetupDummyDataChat(){
//
//       val adapter = GroupAdapter<GroupieViewHolder>()
//adapter.add(Chatitemfromleft("hello "))
//adapter.add(ChatfromRight("hello  hi AB here"))
//adapter.add(Chatitemfromleft("How are you "))
//       adapter.add(ChatfromRight("hello  hi  here"))
//       adapter.add(Chatitemfromleft("hello "))
//
//       binding.recyclerviewChatbox.adapter= adapter
//
//    }
    private fun performSendMessage() {

        val chatlogmessage = binding.entermessageChat.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val username = intent.getParcelableExtra<User>(SelectUser.User_Key)
        val ToId = username?.uid
        if (fromId==null) return

//        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$ToId").push()
        val toreference = FirebaseDatabase.getInstance().getReference("/user-messages/$ToId/$fromId").push()

        val chatMessage = ChatMessage(reference.key!!,chatlogmessage, fromId, ToId!!,System.currentTimeMillis()/1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Toast.makeText(this,"Succesfully send", Toast.LENGTH_SHORT).show()
                binding.entermessageChat.text.clear()
                binding.recyclerviewChatbox.scrollToPosition(adapter.itemCount-1)
            }
    toreference.setValue(chatMessage)

    val latestmessage = FirebaseDatabase.getInstance().getReference("/latest-message/$fromId/$ToId")
    latestmessage.setValue(chatMessage)
    val latesttomessage = FirebaseDatabase.getInstance().getReference("/latest-message/$ToId/$fromId")
latesttomessage.setValue(chatMessage)
}

 private fun Fetchmessagesinchat(){

     val fromId = FirebaseAuth.getInstance().uid
     val ToId = toUser?.uid
     val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$ToId")
     // every data in this we fetch
     reference.addChildEventListener(object : ChildEventListener{
         override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
val chatMessage = snapshot.getValue(ChatMessage::class.java)

             if (chatMessage != null) {

                 if(chatMessage.fromId== FirebaseAuth.getInstance().uid)
                 {
                     val currentuser = LatestMessengerActivity.currentuser ?:return
                     adapter.add(Chatitemfromleft(chatMessage.text,currentuser))

                 }
                 else{

                  adapter.add(ChatfromRight(chatMessage.text,toUser!!))

                 }

             }
             binding.recyclerviewChatbox.scrollToPosition(adapter.itemCount-1)



         }

         override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
         }

         override fun onChildRemoved(snapshot: DataSnapshot) {

         }

         override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

         }

         override fun onCancelled(error: DatabaseError) {
         }


     })

}



}



