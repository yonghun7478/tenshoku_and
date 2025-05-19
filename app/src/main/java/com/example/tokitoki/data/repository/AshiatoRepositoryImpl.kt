package com.example.tokitoki.data.repository

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
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE //<y_bin_46>-MM-dd

    // 전체 더미 데이터를 AshiatoData 객체에 담아 관리
    private val fullDummyAshiatoData: AshiatoData = generateFullDummyAshiatoData(30)

    /**
     * 아시아토 목록 페이지를 가져오는 함수 구현 (AshiatoData 기반 더미 데이터 사용)
     */
    override suspend fun getAshiatoPage(cursor: String?, limit: Int?): Result<AshiatoPage> {
        // 네트워크 지연 시간 시뮬레이션
        delay(Random.nextLong(300, 1000))

        try {
            val pageSize = limit ?: 7 // 기본 페이지 크기 (예: 7일치 데이터)
            val allDailyLogs = fullDummyAshiatoData.dailyAshiatoList // 전체 데이터 리스트 접근

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

    /**
     * 테스트용 전체 더미 데이터를 생성하는 함수 (AshiatoData 형태 반환)
     * @param days 생성할 과거 일수
     * @return AshiatoData 전체 더미 데이터
     */
    private fun generateFullDummyAshiatoData(days: Int): AshiatoData {
        val regions = listOf("서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주")
        val today = LocalDate.now()
        val dailyList = mutableListOf<DailyAshiato>()

        for (i in 0 until days) {
            val date = today.minusDays(i.toLong())
            val dateString = date.format(dateFormatter)
            val viewerCount = Random.nextInt(1, 8) // 하루 방문자 수 (1~7명 랜덤)
            val viewers = mutableListOf<AshiatoViewer>()

            for (j in 0 until viewerCount) {
                val hour = Random.nextInt(0, 24)
                val minute = Random.nextInt(0, 60)
                viewers.add(
                    AshiatoViewer(
                        userId = "${(1..150).random()}",
                        thumbnailUrl = "https://dimg.donga.com/wps/NEWS/IMAGE/2024/10/23/130275989.1.jpg", // Placeholder 이미지
                        age = Random.nextInt(20, 40),
                        region = regions.random(),
                        viewedTime = "%02d:%02d".format(hour, minute)
                    )
                )
            }
            // 시간 내림차순 정렬 (최신순)
            viewers.sortByDescending { it.viewedTime }
            dailyList.add(DailyAshiato(date = dateString, viewers = viewers))
        }
        // 날짜 내림차순 정렬 (최신순) - 이미 생성 순서가 최신순이지만 명시적으로 정렬
        dailyList.sortByDescending { it.date }
        // 생성된 리스트를 AshiatoData로 감싸서 반환
        return AshiatoData(dailyAshiatoList = dailyList)
    }
}