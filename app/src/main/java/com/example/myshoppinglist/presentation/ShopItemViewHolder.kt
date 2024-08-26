package com.example.myshoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R

class ShopItemViewHolder(view: View): RecyclerView.ViewHolder(view){
    val tvName = view.findViewById<TextView>(R.id.textViewShopItemName)
    val tvCount = view.findViewById<TextView>(R.id.textViewShopItemQuantity)

}