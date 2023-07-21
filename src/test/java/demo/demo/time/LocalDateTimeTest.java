package demo.demo.time;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTest {

    @Test
    void logDateTime() {
        // 현재 날짜와 시간으로 LocalDateTime 객체 생성
        LocalDateTime currentDateTime = java.time.LocalDateTime.now();
        // DateTimeFormatter를 사용하여 형식 지정 (년도/월/일/시/분/초)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // LocalDateTime 객체를 원하는 형식으로 포맷팅하여 추출
        String timeWithSeconds = currentDateTime.format(formatter);

        System.out.println(timeWithSeconds);
    }
}
