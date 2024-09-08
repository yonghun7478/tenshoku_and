package com.example.tokitoki.data.model

data class UserResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: AddressResponse,
    val phone: String,
    val website: String,
    val company: CompanyResponse
)

data class CompanyResponse(
    val name: String,
    val catchPhrase: String,
    val bs: String
)

data class AddressResponse(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: GeoResponse
)

data class GeoResponse(
    val lat: String,
    val lng: String
)