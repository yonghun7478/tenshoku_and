package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.backup.LocalUserDao
import com.example.tokitoki.data.local.backup.LocalUserEntity
import com.example.tokitoki.data.local.backup.UserDao
import com.example.tokitoki.data.local.backup.UserPreferences
import com.example.tokitoki.data.remote.UserApiService
import com.example.tokitoki.domain.converter.UserDomainConverter
import com.example.tokitoki.domain.model.User
import com.example.tokitoki.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.tokitoki.domain.util.Resource


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val localUserDao: LocalUserDao,
    private val userPreferences: UserPreferences,
    private val userApiService: UserApiService,
    private val dispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun getUsersFromApi(): Flow<Resource<List<User>>> = flow {
        try {
            emit(Resource.Loading)

            val userResponse = userApiService.getUsers()

            if (userResponse.isSuccessful) {
                val users = userResponse.body()!!.map { UserDomainConverter.responseToDomain(it) }
                emit(Resource.Success(users))
            } else {
                emit(Resource.Error(message = "API request failed with code ${userResponse.code()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error(throwable = e)) // 에러 상태 방출
        }
    }.flowOn(dispatcher)

    override suspend fun getUsersFromDb(): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading)

        try {
            userDao.getUsersFlow().collect { userEntities -> // collect를 사용하여 Flow 수집
                val users = userEntities.map { UserDomainConverter.entityToDomain(it) }
                emit(Resource.Success(users))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = "Failed to load users from database", throwable = e))
        }
    }.flowOn(dispatcher)

    override suspend fun saveUsersToDb(users: List<User>) = withContext(dispatcher) {
        userDao.insertUsers(users.map { UserDomainConverter.domainToEntity(it) })
    }

    override suspend fun saveUserToDb(users: User) = withContext(dispatcher) {
        userDao.insertUser(UserDomainConverter.domainToEntity(users))
        localUserDao.insertUser(
            LocalUserEntity(
                id = 1,
                name = "",
                username = "",
                email = "",
                address_street = "",
                address_suite = "",
                address_city = "",
                address_zipcode = "",
                address_geo_lat = "",
                address_geo_lng = "",
                phone = "",
                website = "",
                company_name = "",
                company_bs = "",
                company_catchPhrase = ""
            )
        )
    }

    override suspend fun deleteUserFromDb(id: Int) {
        userDao.deleteUserById(id)
    }

    override suspend fun updateUserFromDb(id: Int, name: String) {
        userDao.updateUser(UserDomainConverter.domainToEntity(User(id = id, name = name)))
    }

    override suspend fun saveUserNameFromPreferences(name: String) {
        userPreferences.saveUserNameFrompreferences(name)
    }

    override suspend fun getUserNameFromPreferences(): String? {
        return userPreferences.getUserNameFrompreferences()
    }
}