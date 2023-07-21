package demo.demo.utility;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Time {
    public String getTime() {
        // 현재 날짜와 시간으로 LocalDateTime 객체 생성
        LocalDateTime currentDateTime = LocalDateTime.now();
        // DateTimeFormatter를 사용하여 형식 지정 (년도/월/일/시/분/초)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // LocalDateTime 객체를 원하는 형식으로 포맷팅하여 추출
        String timeWithSeconds = currentDateTime.format(formatter);
        System.out.println(timeWithSeconds);

        return timeWithSeconds;
    }
}
