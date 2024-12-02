package com.example.tokitoki.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.Period
import javax.inject.Inject

interface CalculateAgeUseCase {
    suspend operator fun invoke(birthDateString: String): Result<String>
}

class CalculateAgeUseCaseImpl @Inject constructor(
) : CalculateAgeUseCase {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun invoke(birthDateString: String): Result<String> {
        return try {
            // 날짜 형식 파싱
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            val birthDate = LocalDate.parse(birthDateString, formatter)

            // 현재 날짜와 비교하여 나이 계산
            val currentDate = LocalDate.now()
            val age = Period.between(birthDate, currentDate).years

            // 나이를 String으로 변환
            Result.success(age.toString())
        } catch (e: DateTimeParseException) {
            Result.failure(IllegalArgumentException("Invalid date format: $birthDateString"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
