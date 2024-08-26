package com.example.myshoppinglist.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R
import com.example.myshoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter : ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        observeViewModel()
        }


    private fun observeViewModel(){
        viewModel.shopList.observe(this){
            shopListAdapter.shopList = it
        }
    }
    private fun setupRecyclerView() {
        val recyclerViewShopList = findViewById<RecyclerView>(R.id.shopListRecyclerView)
        with(recyclerViewShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            recyclerViewShopList.recycledViewPool
                .setMaxRecycledViews(
                    ShopListAdapter.VIEW_TYPE_ENABLED,
                    ShopListAdapter.MAX_POOL_SIZE
                )
            recyclerViewShopList.recycledViewPool
                .setMaxRecycledViews(
                    ShopListAdapter.VIEW_TYPE_DISABLED,
                    ShopListAdapter.MAX_POOL_SIZE
                )
            shopListAdapter.onShopItemLongClickListener =
                object : ShopListAdapter.OnShopItemLongClickListener {
                    override fun onShopItemLongClick(shopItem: ShopItem) {
                        viewModel.changeShopItemState(shopItem)
                    }
                }
        }
    }
}