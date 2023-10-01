package com.example.kotlinmessagner

import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class Useritem(var user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // will be called in our listfor each user object
        val userNameTextView = viewHolder.itemView.findViewById<TextView>(R.id.username)
        userNameTextView.text = user.username
        val imageview = viewHolder.itemView.findViewById<ImageView>(R.id.dp_imageview)
        Picasso.get().load(user.profileImageUrl).into(imageview)

    }

    override fun getLayout(): Int {
        return R.layout.user_row_selectuser
    }


}

