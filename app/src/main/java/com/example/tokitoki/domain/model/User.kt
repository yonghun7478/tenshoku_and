package com.example.tokitoki.domain.model

data class User(
    val id: Int = 0,
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val address: Address = Address(),
    val phone: String = "",
    val website: String = "",
    val company: Company = Company()
)

data class Company(
    val name: String = "",
    val catchPhrase: String = "",
    val bs: String = ""
)

data class Address(
    val street: String = "",
    val suite: String = "",
    val city: String = "",
    val zipcode: String = "",
    val geo: Geo = Geo()
)

data class Geo(
    val lat: String = "",
    val lng: String = ""
)