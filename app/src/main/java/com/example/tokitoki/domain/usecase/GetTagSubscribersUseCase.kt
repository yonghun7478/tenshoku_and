package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.common.ResultWrapper.*
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.repository.TagRepository
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

interface GetTagSubscribersUseCase {
    suspend operator fun invoke(
        tagId: String,
        cursor: String?,
        limit: Int
    ): ResultWrapper<UserList>
}

class GetTagSubscribersUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository
) : GetTagSubscribersUseCase {
    override suspend operator fun invoke(
        tagId: String,
        cursor: String?,
        limit: Int
    ): ResultWrapper<UserList> {
        // 1. TagRepository에서 페이징된 구독자 ID 목록과 커서 정보를 가져옵니다.
        val idListResult = tagRepository.getTagSubscribers(tagId, cursor, limit)

        return when (idListResult) {
            is ResultWrapper.Success -> {
                val idList = idListResult.data
                if (idList.ids.isEmpty()) {
                    // 구독자가 없으면 빈 목록을 반환합니다.
                    return Success(UserList(emptyList(), null, true))
                }

                // 2. UserRepository에서 ID 목록에 해당하는 사용자 정보를 가져옵니다.
                when (val userListResult = userRepository.getUsersByIds(idList.ids)) {
                    is Success -> {
                        // 3. 사용자 정보와 커서 정보를 조합하여 최종 UserList를 만듭니다.
                        Success(
                            UserList(
                                users = userListResult.data,
                                nextCursor = idList.nextCursor,
                                isLastPage = idList.isLastPage
                            )
                        )
                    }
                    is Error -> {
                        // 사용자 정보 조회 실패 시 에러를 반환합니다.
                        userListResult
                    }

                    Loading -> TODO()
                }
            }
            is Error -> {
                // ID 목록 조회 실패 시 에러를 그대로 반환합니다.
                Error(idListResult.errorType)
            }

            Loading -> TODO()
        }
    }
} 