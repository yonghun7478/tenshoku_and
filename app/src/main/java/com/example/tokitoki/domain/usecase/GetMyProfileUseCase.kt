package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.repository.MyProfileRepository

import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) {
    suspend operator fun invoke(): MyProfile {
        val myProfile = MyProfile(
            name = "yonghun",
            age = 33,
            thumbnailImageUri = "Uri.EMPTY",
            mySelfSentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus feugiat sapien quis turpis luctus, id convallis mauris malesuada. Ut tincidunt sapien risus, sit amet accumsan elit varius ut. Sed condimentum malesuada ultricies. In hac habitasse platea dictumst."
        )

        return myProfile
    }
}
