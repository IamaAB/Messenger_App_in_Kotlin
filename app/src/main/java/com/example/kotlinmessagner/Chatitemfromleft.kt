package com.example.kotlinmessagner

import android.widget.ImageView
import android.widget.TextView
import com.example.kotlinmessagner.databinding.ChatBoxRowLeftBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


// chat from left means = other user
class Chatitemfromleft(val text: String,val user: User): Item<GroupieViewHolder>() {
    private lateinit var binding: ChatBoxRowLeftBinding

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val textfromleft = viewHolder.itemView.findViewById<TextView>(R.id.textchatrowfromleft)
        textfromleft.text = text

        val image = viewHolder.itemView.findViewById<ImageView>(R.id.imagefromleft)

        // load our user imaage into sthe image

        // picasso our image loading image getting a libraray
        val uri = user.profileImageUrl
        Picasso.get().load(uri).into(image)
    }

    override fun getLayout(): Int {
        return  R.layout.chat_box_row_left
    }

}
// chat from right means = we are sending message

class ChatfromRight(val text: String,val user: User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val textfromleft = viewHolder.itemView.findViewById<TextView>(R.id.textchatrowtoright)
        textfromleft.text = text
        val image = viewHolder.itemView.findViewById<ImageView>(R.id.imagetoright)

        // load our user imaage into sthe image

        // picasso our image loading image getting a libraray
        val uri = user.profileImageUrl
        Picasso.get().load(uri).into(image)
    }

    override fun getLayout(): Int {
        return  R.layout.chat_box_row_right
    }

}