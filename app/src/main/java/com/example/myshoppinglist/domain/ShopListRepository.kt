package com.example.myshoppinglist.domain

interface ShopListRepository {

    fun addShopItem(item: ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun getShopItem(id: Int) : ShopItem

    fun getShopList(): List<ShopItem>

    fun removeShopItem(shopItem: ShopItem)
}