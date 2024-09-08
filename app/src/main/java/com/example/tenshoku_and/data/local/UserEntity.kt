package com.example.tenshoku_and.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address_street: String,
    val address_suite: String,
    val address_city: String,
    val address_zipcode: String,
    val address_geo_lat: String,
    val address_geo_lng: String,
    val phone: String,
    val website: String,
    val company_name: String,
    val company_catchPhrase: String,
    val company_bs: String
)