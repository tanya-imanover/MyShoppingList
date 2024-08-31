package com.example.myshoppinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter : ShopListAdapter
    private lateinit var addShopItemButton: FloatingActionButton
    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        shopItemContainer = findViewById(R.id.shop_item_container)
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

    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()


    }

    private fun isOnePaneMode() = shopItemContainer == null

    private fun setupAddShopItemButton() {
        addShopItemButton = findViewById(R.id.floatingActionButton)
        addShopItemButton.setOnClickListener {
            if(isOnePaneMode()) {
                startActivity(ShopItemActivity.newIntentAddItem(this))
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    private fun setupItemClickListener() {
        shopListAdapter.onShopItemClickListener = {
            if(isOnePaneMode()) {
                startActivity(ShopItemActivity.newIntentEditItem(this, it.id))
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
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
            setupItemClickListener()
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



    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeShopItemState(it)
        }
    }

    override fun onEditingFinish() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()

    }
}