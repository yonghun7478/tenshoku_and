package com.example.tokitoki.ui.state

data class AshiatoScreenState(
    val users: List<UserData> = emptyList()
)

data class UserData(
    val id: String,
    val profileImageUrl: String,
    val daysAgoVisited: Int,
    val age: Int,
    val location: String
)

object AshiatoData {
    val dummyUsers = listOf(
        UserData("1", "https://source.unsplash.com/random/300x300?face&1", 1, 25, "서울"),
        UserData("2", "https://source.unsplash.com/random/300x300?face&2", 2, 28, "부산"),
        UserData("3", "https://source.unsplash.com/random/300x300?face&3", 3, 22, "대구"),
        UserData("4", "https://source.unsplash.com/random/300x300?face&4", 1, 29, "인천"),
        UserData("5", "https://source.unsplash.com/random/300x300?face&5", 2, 24, "광주"),
        UserData("6", "https://source.unsplash.com/random/300x300?face&6", 3, 27, "대전"),
        UserData("7", "https://source.unsplash.com/random/300x300?face&7", 1, 26, "울산"),
        UserData("8", "https://source.unsplash.com/random/300x300?face&8", 2, 23, "세종"),
        UserData("9", "https://source.unsplash.com/random/300x300?face&9", 3, 30, "경기"),
        UserData("10", "https://source.unsplash.com/random/300x300?face&10", 1, 21, "강원"),
        UserData("11", "https://source.unsplash.com/random/300x300?face&11", 2, 28, "충북"),
        UserData("12", "https://source.unsplash.com/random/300x300?face&12", 3, 25, "충남"),
    )
}