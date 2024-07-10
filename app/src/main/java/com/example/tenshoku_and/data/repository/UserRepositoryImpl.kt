package com.example.tenshoku_and.data.repository

import com.example.tenshoku_and.data.local.UserDao
import com.example.tenshoku_and.data.remote.UserApiService
import com.example.tenshoku_and.domain.converter.UserDomainConverter
import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.tenshoku_and.domain.util.Resource


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
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
    }
}