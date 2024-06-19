package com.example.tenshoku_and.domain.converter

import com.example.tenshoku_and.data.model.AddressResponse
import com.example.tenshoku_and.data.model.CompanyResponse
import com.example.tenshoku_and.data.model.GeoResponse
import com.example.tenshoku_and.data.model.UserResponse
import com.example.tenshoku_and.domain.model.Address
import com.example.tenshoku_and.domain.model.Company
import com.example.tenshoku_and.domain.model.Geo
import com.example.tenshoku_and.domain.model.User

object UserConverter {

    fun responseToDomain(userResponse: UserResponse): User {
        return User(
            id = userResponse.id,
            name = userResponse.name,
            username = userResponse.username,
            email = userResponse.email,
            address = addressResponseToDomain(userResponse.address),
            phone = userResponse.phone,
            website = userResponse.website,
            company = companyResponseToDomain(userResponse.company)
        )
    }

    private fun addressResponseToDomain(addressResponse: AddressResponse): Address {
        return Address(
            street = addressResponse.street,
            suite = addressResponse.suite,
            city = addressResponse.city,
            zipcode = addressResponse.zipcode,
            geo = geoResponseToDomain(addressResponse.geo)
        )
    }

    private fun geoResponseToDomain(geoResponse: GeoResponse): Geo {
        return Geo(
            lat = geoResponse.lat,
            lng = geoResponse.lng
        )
    }

    private fun companyResponseToDomain(companyResponse: CompanyResponse): Company {
        return Company(
            name = companyResponse.name,
            catchPhrase = companyResponse.catchPhrase,
            bs = companyResponse.bs
        )
    }
}
