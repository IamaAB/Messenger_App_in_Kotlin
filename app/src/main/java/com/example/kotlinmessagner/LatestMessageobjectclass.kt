package com.example.kotlinmessagner

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class LatestMessageobjectclass(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    var chatuser : User ?=null
    @SuppressLint("SuspiciousIndentation")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val text = viewHolder.itemView.findViewById<TextView>(R.id.latest_textview)
        text.text = chatMessage.text

        val chatPartnerId : String
            if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                chatPartnerId= chatMessage.toId
        } else {
                chatPartnerId=  chatMessage.fromId
        }

        Log.d("ChatPartnerId", "Chat Partner ID: $chatPartnerId")

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatuser = dataSnapshot.getValue(User::class.java)

                // Log user data and check for null
                Log.d("UserData", "User: $chatuser")

                if (chatuser != null) {
                    // Log username and set it in the TextView
                    Log.d("Username", "Username: ${chatuser?.username}")
                    viewHolder.itemView.findViewById<TextView>(R.id.username_latest).text =
                        chatuser?.username
                } else {
                    // Handle null user dataz
                    Log.e("UserData", "User data is null")
                }
                val targetImageView = viewHolder.itemView.findViewById<ImageView>(R.id.imageView)
                Picasso.get().load(chatuser?.profileImageUrl).into(targetImageView)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun getLayout(): Int {
        return R.layout.latest_message_list_row
    }
}
