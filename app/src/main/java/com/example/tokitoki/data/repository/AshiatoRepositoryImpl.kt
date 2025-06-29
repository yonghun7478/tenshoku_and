package com.example.tokitoki.data.repository

import com.example.tokitoki.data.dummy.DummyData
import com.example.tokitoki.data.model.AshiatoData
import com.example.tokitoki.data.model.AshiatoViewer
import com.example.tokitoki.data.model.DailyAshiato
import com.example.tokitoki.domain.model.AshiatoTimeline
import com.example.tokitoki.domain.model.DailyAshiatoLog
import com.example.tokitoki.domain.model.toDomain
import com.example.tokitoki.domain.repository.AshiatoPage
import com.example.tokitoki.domain.repository.AshiatoRepository
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.random.Random

/**
 * AshiatoRepository의 구현체 클래스. (더미 데이터 사용)
 */
class AshiatoRepositoryImpl @Inject constructor() : AshiatoRepository {

    // 날짜 포맷터
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE // yyyy-MM-dd

    private val CURRENT_USER_ID = "0" // 현재 사용자의 ID를 "0"으로 고정
    private val viewedPairs: MutableSet<Pair<String, String>> = mutableSetOf() // (조회한 사람 ID, 조회된 프로필 ID)

    // 현재 사용자의 발자국 로그를 저장할 리스트
    private val currentUserAshiatoLogs: MutableList<DailyAshiato> = mutableListOf()

    init {
        val users = DummyData.getUsers()
        val today = LocalDate.now()

        // 지난 30일 동안의 더미 발자국 데이터 생성 (현재 사용자의 프로필을 방문한 경우)
        for (i in 0 until 30) {
            val date = today.minusDays(i.toLong())
            val dateString = date.format(dateFormatter)
            val viewerCount = Random.nextInt(1, 8) // 하루 방문자 수 (1~7명 랜덤)

            val dailyViewers = mutableListOf<AshiatoViewer>()
            for (j in 0 until viewerCount) {
                val randomViewer = users.random()
                // 방문자가 현재 사용자가 아닌 경우에만 유효한 발자국으로 간주
                if (randomViewer.id != CURRENT_USER_ID) {
                    viewedPairs.add(Pair(randomViewer.id, CURRENT_USER_ID)) // (조회한 사람 ID, 조회된 프로필 ID)

                    val hour = Random.nextInt(0, 24)
                    val minute = Random.nextInt(0, 60)
                    dailyViewers.add(
                        AshiatoViewer(
                            userId = randomViewer.id,
                            thumbnailUrl = randomViewer.thumbnailUrl,
                            age = randomViewer.age,
                            region = randomViewer.location, // UserDetail의 location을 region으로 사용
                            viewedTime = "%02d:%02d".format(hour, minute)
                        )
                    )
                }
            }
            // 해당 날짜에 발자국이 있을 경우에만 로그 추가
            if (dailyViewers.isNotEmpty()) {
                dailyViewers.sortByDescending { it.viewedTime } // 시간 내림차순 정렬 (최신순)
                currentUserAshiatoLogs.add(DailyAshiato(date = dateString, viewers = dailyViewers))
            }
        }
        currentUserAshiatoLogs.sortByDescending { it.date } // 날짜 내림차순 정렬 (최신순)
    }

    /**
     * 아시아토 목록 페이지를 가져오는 함수 구현 (AshiatoData 기반 더미 데이터 사용)
     */
    override suspend fun getAshiatoPage(cursor: String?, limit: Int?): Result<AshiatoPage> {
        // 네트워크 지연 시간 시뮬레이션
        delay(Random.nextLong(300, 1000))

        try {
            val pageSize = limit ?: 7 // 기본 페이지 크기 (예: 7일치 데이터)
            val allDailyLogs = currentUserAshiatoLogs // 현재 사용자의 발자국 데이터 사용

            // 1. 커서를 기준으로 필터링
            val filteredDailyData: List<DailyAshiato> = if (cursor == null) {
                // 커서가 null이면 가장 최신 데이터부터 pageSize만큼 가져옴
                allDailyLogs.take(pageSize)
            } else {
                // 커서 날짜보다 이전 날짜들만 필터링하여 pageSize만큼 가져옴
                allDailyLogs.filter { it.date < cursor }.take(pageSize)
            }

            // 2. 다음 페이지 커서 계산 (반환된 데이터의 마지막 날짜)
            val nextCursor = filteredDailyData.lastOrNull()?.date

            // --- Data Layer 객체 생성 및 Domain 변환 수행 ---
            // 3. 현재 페이지 데이터를 Data Layer 구조(AshiatoData)로 래핑
            //    (실제 API 통신 시에는 응답 자체가 이 구조일 것)
            val dataLayerPageData = AshiatoData(dailyAshiatoList = filteredDailyData)

            // 4. Data Layer 객체를 Domain Layer 객체(AshiatoTimeline)로 변환 (Mapper 사용)
            val ashiatoTimeline: AshiatoTimeline = dataLayerPageData.toDomain()
            // 5. AshiatoPage가 List<DailyAshiatoLog>를 필요로 하므로 여기서 추출
            val domainLogs: List<DailyAshiatoLog> = ashiatoTimeline.dailyLogs
            // -------------------------------------------------------

            // 6. 최종 결과인 AshiatoPage 생성
            val resultPage = AshiatoPage(
                logs = domainLogs,
                nextCursor = nextCursor
            )

            // 7. 성공 결과 반환
            return Result.success(resultPage)

        } catch (e: Exception) {
            // 오류 발생 시 실패 결과 반환
            println("Error fetching Ashiato page: ${e.message}")
            return Result.failure(e)
        }
    }
}