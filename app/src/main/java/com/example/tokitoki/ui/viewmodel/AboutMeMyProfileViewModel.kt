package com.example.tokitoki.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.MyProfile
import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.domain.usecase.CalculateAgeUseCase
import com.example.tokitoki.domain.usecase.CheckEmailRegisteredUseCase
import com.example.tokitoki.domain.usecase.ClearMyTagUseCase
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.GetMySelfSentenceUseCase
import com.example.tokitoki.domain.usecase.GetMyTagUseCase
import com.example.tokitoki.domain.usecase.RegisterMyProfileUseCase
import com.example.tokitoki.domain.usecase.SaveTokensUseCase
import com.example.tokitoki.domain.usecase.SetMyProfileUseCase
import com.example.tokitoki.domain.usecase.SetMyTagUseCase
import com.example.tokitoki.ui.constants.AboutMeMyProfileAction
import com.example.tokitoki.ui.converter.MyProfileUiConverter
import com.example.tokitoki.ui.state.AboutMeMyProfileEvent
import com.example.tokitoki.ui.state.AboutMeMyProfileState
import com.example.tokitoki.utils.FileManager
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tokitoki.domain.usecase.tag.GetTagsUseCase
import com.example.tokitoki.ui.model.MyTagItem

@HiltViewModel
class AboutMeMyProfileViewModel
@Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getMyTagUseCase: GetMyTagUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val calculateAgeUseCase: CalculateAgeUseCase,
    private val getMySelfSentenceUseCase: GetMySelfSentenceUseCase,
    private val registerMyProfileUseCase: RegisterMyProfileUseCase,
    private val fileManager: FileManager,
    private val checkEmailRegisteredUseCase: CheckEmailRegisteredUseCase,
    private val saveTokensUseCase: SaveTokensUseCase,
    private val setMyProfileUseCase: SetMyProfileUseCase,
    private val setMyTagUseCase: SetMyTagUseCase,
    private val clearMyTagUseCase: ClearMyTagUseCase,


    ) : ViewModel() {
    private val _uiState = MutableStateFlow(AboutMeMyProfileState())
    val uiState: StateFlow<AboutMeMyProfileState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeMyProfileEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var buffer:MyProfile? = null
    var tagBuffer:ArrayList<MyTagItem>? = null

    suspend fun init(
        uri: Uri = Uri.EMPTY,
        birthday: String? = null,
        name: String? = null,
        selfSentenceId: Int? = null,
        tagIds: ArrayList<MyTagItem>? = null
    ) {
        Log.d("AboutMeMyProfileViewModel", "init uri: $uri $birthday $name $selfSentenceId $tagIds")

        var myProfile = getMyProfileUseCase() // Fetches the base profile
        myProfile = myProfile.copy(
            birthDay = birthday ?: myProfile.birthDay, // Uses 'birthday' parameter if not null, else uses 'myProfile.birthDay' (from getMyProfileUseCase)
            name = name ?: myProfile.name,             // Uses 'name' parameter if not null, else uses 'myProfile.name'
            mySelfSentenceId = selfSentenceId ?: myProfile.mySelfSentenceId, // Uses 'selfSentenceId' parameter if not null, else uses 'myProfile.mySelfSentenceId'
            thumbnailUrl = uri.toString()
        )

        buffer = myProfile
        tagBuffer = tagIds

        val age = calculateAgeUseCase(myProfile.birthDay).getOrNull() ?: ""

        val myTags = tagIds?.map { MyTag(it.tagId, it.categoryId) } ?: getMyTagUseCase()
        val allTagIds = myTags.map { it.tagId }

        val allTags = getTagsUseCase(allTagIds)

        val mySelfSentence = getMySelfSentenceUseCase(myProfile.mySelfSentenceId)

        val myProfileItem =
            MyProfileUiConverter.domainToUi(myProfile, age, allTags, mySelfSentence.sentence)

        _uiState.update { currentState ->
            currentState.copy(
                myProfileItem = myProfileItem,
                uri = Uri.parse(Uri.decode(myProfile.thumbnailUrl))
            )
        }
    }

    fun aboutMeMyProfileAction(action: AboutMeMyProfileAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeMyProfileEvent.ACTION(action))
        }
    }

    suspend fun getBirthDay(): String {
        val myProfile = getMyProfileUseCase()
        return myProfile.birthDay
    }

    suspend fun getMySelfSentenceId(): Int {
        val myProfile = getMyProfileUseCase()
        return myProfile.mySelfSentenceId
    }

    fun getMyTags(): String {
        val tagsJson = Gson().toJson(_uiState.value.myProfileItem.myTagItems)
        return tagsJson
    }

    suspend fun registerMyProfile(): Boolean {
        val thumbnailPath = fileManager.saveUriToInternalCache(uiState.value.uri).orEmpty()
        val myProfile = getMyProfileUseCase()
        val result = registerMyProfileUseCase(myProfile, thumbnailPath)

        if (result is ResultWrapper.Success) {
            setMyProfileUseCase(result.data)

            (checkEmailRegisteredUseCase("true") as ResultWrapper.Success).let { res ->
                saveTokensUseCase(res.data.accessToken, res.data.refreshToken)
            }
        }

        return result is ResultWrapper.Success
    }

    suspend fun saveMyProfile() {
        setMyProfileUseCase(buffer ?: MyProfile())
        tagBuffer?.let { tagItems ->
            clearMyTagUseCase()
            val tagIds = tagItems.map { it.tagId }
            val myTags = getTagsUseCase(tagIds) 
             val tags = myTags.map { MyTag(it.id.toInt(), it.tagType.ordinal) }
            setMyTagUseCase(tags)
        }
    }
}
