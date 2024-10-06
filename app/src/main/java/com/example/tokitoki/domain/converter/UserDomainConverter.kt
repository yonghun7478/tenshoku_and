package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.backup.UserEntity
import com.example.tokitoki.data.model.AddressResponse
import com.example.tokitoki.data.model.CompanyResponse
import com.example.tokitoki.data.model.GeoResponse
import com.example.tokitoki.data.model.UserResponse
import com.example.tokitoki.domain.model.Address
import com.example.tokitoki.domain.model.Company
import com.example.tokitoki.domain.model.Geo
import com.example.tokitoki.domain.model.User

object UserDomainConverter {

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

    fun domainToEntity(user: User): UserEntity {
        return UserEntity(
            id = user.id,
            name = user.name,
            username = user.username,
            email = user.email,
            address_street = user.address.street,
            address_suite = user.address.suite,
            address_city = user.address.city,
            address_zipcode = user.address.zipcode,
            address_geo_lat = user.address.geo.lat,
            address_geo_lng = user.address.geo.lng,
            phone = user.phone,
            website = user.website,
            company_name = user.company.name,
            company_catchPhrase = user.company.catchPhrase,
            company_bs = user.company.bs
        )
    }

    fun entityToDomain(userEntity: UserEntity): User {
        return User(
            id = userEntity.id,
            name = userEntity.name,
            username = userEntity.username,
            email = userEntity.email,
            address = Address(
                street = userEntity.address_street,
                suite = userEntity.address_suite,
                city = userEntity.address_city,
                zipcode = userEntity.address_zipcode,
                geo = Geo(
                    lat = userEntity.address_geo_lat,
                    lng = userEntity.address_geo_lng
                )
            ),
            phone = userEntity.phone,
            website = userEntity.website,
            company = Company(
                name = userEntity.company_name,
                catchPhrase = userEntity.company_catchPhrase,
                bs = userEntity.company_bs
            )
        )
    }
}
