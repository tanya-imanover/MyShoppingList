package com.example.myshoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.domain.ShopItem
import com.example.myshoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository{

    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    init{
        for(i in 0..10 ){
            val item = ShopItem("Name $i", 1, true)
           addShopItem(item)
        }
    }

    override fun addShopItem(item: ShopItem) {
        if(item.id == ShopItem.UNDEFINED_ID){
            item.id = autoIncrementId++
        }
        shopList.add(item)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        shopList.remove(getShopItem(shopItem.id))
        addShopItem(shopItem)
    }

    override fun getShopItem(id: Int): ShopItem {
        return shopList.find {
            it.id == id
        } ?: throw RuntimeException("Element with id $id not found")
     }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    override fun removeShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    private fun updateList(){
        shopListLD.value = shopList.toList()
    }

}