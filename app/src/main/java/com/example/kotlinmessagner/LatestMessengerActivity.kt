package com.example.kotlinmessagner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kotlinmessagner.SelectUser.Companion.User_Key
import com.example.kotlinmessagner.databinding.LatestMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class LatestMessengerActivity : AppCompatActivity() {
    private lateinit var binding: LatestMessageBinding
    val adapter = GroupAdapter<GroupieViewHolder>()


    companion object{
        var currentuser : User?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Dashboard"
        binding = LatestMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerviewForLatestMessage.adapter= adapter
        binding.recyclerviewForLatestMessage.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        // set itemclick listener for your adapter   so that when you click on it it took to you to that chat
        adapter.setOnItemClickListener { item, view ->
val intent = Intent(this,ChatlogActivity::class.java)
// which item you are clicking onto
            // as is used to casting the element
            val row = item as LatestMessageobjectclass
        intent.putExtra(SelectUser.User_Key,row.chatuser)
 startActivity(intent)
        }

        fetchcurrentuser()
        verifyuserisloggedin()
//       setupdummyrows()
        ListenforlatestMessage()



    }
    private fun fetchcurrentuser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentuser = snapshot.getValue(User::class.java)
println("Currentmessage : ${currentuser?.username}")
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
    private fun verifyuserisloggedin(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this,MainActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
    // Kotlin HashMap is a collection which contains pairs of object.
    // Kotlin Hash Table based implementation of the MutableMap interface.
    // It stores the data in the form of key and value pair.
    // Map keys are unique and the map holds only one value for each key
    val latestmessagelistenerMap = HashMap<String,ChatMessage>()

    private  fun refreshrecylerviewmessages(){
        adapter.clear()
        latestmessagelistenerMap.values.forEach{
            adapter.add(LatestMessageobjectclass(it))
        }

    }


    private fun ListenforlatestMessage(){
        var fromId = FirebaseAuth.getInstance().uid
        var ref = FirebaseDatabase.getInstance().getReference("latest-message/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // we convert a snapchot here into chaat message and then pass that to hashmap
val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                // key is belong to user that messages us

                latestmessagelistenerMap[snapshot.key!!]=chatMessage
         refreshrecylerviewmessages()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // when new user messages us we are notified as a chnage
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestmessagelistenerMap[snapshot.key!!]=chatMessage
                refreshrecylerviewmessages()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }




  private fun setupdummyrows(){

//      adapter.add(LatestMessageobjectclass())
//      adapter.add(LatestMessageobjectclass())
//      adapter.add(LatestMessageobjectclass())
//      adapter.add(LatestMessageobjectclass())

      
  }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item?.itemId){

            R.id.menu_new_message ->{
                val intent = Intent(this,SelectUser::class.java)
                startActivity(intent)

            }
            R.id.menu_sign_out ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this,LoginActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


}