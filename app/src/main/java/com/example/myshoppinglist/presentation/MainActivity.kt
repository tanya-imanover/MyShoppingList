package com.example.myshoppinglist.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter : ShopListAdapter
    private lateinit var addShopItemButton: FloatingActionButton
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
        setupAddShopItemButton()
    }

    private fun setupAddShopItemButton() {
        addShopItemButton = findViewById(R.id.floatingActionButton)
        addShopItemButton.setOnClickListener {
                startActivity(ShopItemActivity.newIntentAddItem(this))
        }
    }


    private fun observeViewModel(){
        viewModel.shopList.observe(this){
            shopListAdapter.submitList(it)
        }
    }
    private fun setupRecyclerView() {
        val recyclerViewShopList = findViewById<RecyclerView>(R.id.shopListRecyclerView)
        with(recyclerViewShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            recycledViewPool
                .setMaxRecycledViews(
                    ShopListAdapter.VIEW_TYPE_ENABLED,
                    ShopListAdapter.MAX_POOL_SIZE
                )
            recycledViewPool
                .setMaxRecycledViews(
                    ShopListAdapter.VIEW_TYPE_DISABLED,
                    ShopListAdapter.MAX_POOL_SIZE
                )
            setupLongClickListener()
            setupClickListener()
            setupSwipeListener(recyclerViewShopList)
        }
    }

    private fun setupSwipeListener(recyclerViewShopList: RecyclerView) {
        val helper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val item = shopListAdapter.currentList[position]
                    viewModel.removeShopItem(item)
                }

            })
        helper.attachToRecyclerView(recyclerViewShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            startActivity(ShopItemActivity.newIntentEditItem(this, it.id))
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeShopItemState(it)
        }
    }
}