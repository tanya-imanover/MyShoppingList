package com.example.myshoppinglist.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GetShopListUseCase (private val shopListRepository: ShopListRepository) {
    fun getShopList(): LiveData<List<ShopItem>>{
        return shopListRepository.getShopList()
    }
}