package com.example.tenshoku_and.data.repository

import com.example.tenshoku_and.data.local.UserDao
import com.example.tenshoku_and.data.remote.UserApiService
import com.example.tenshoku_and.domain.converter.UserConverter
import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.tenshoku_and.domain.util.Result


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userApiService: UserApiService,
    private val dispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun getUsersFromApi(): Flow<Result<List<User>>> = flow {
        try {
            val userResponse = userApiService.getUsers()

            if (userResponse.isSuccessful) {
                val users = userResponse.body()!!.map { UserConverter.responseToDomain(it) }
                emit(Result.Success(users))
            } else {
                emit(Result.Error(message = "API request failed with code ${userResponse.code()}"))
            }

        } catch (e: Exception) {
            emit(Result.Error(throwable = e)) // 에러 상태 방출
        }
    }.flowOn(dispatcher)

    override suspend fun saveUsersToDb(users: List<User>) = withContext(dispatcher) {
        userDao.insertUsers(users.map { UserConverter.domainToEntity(it) })
    }
}