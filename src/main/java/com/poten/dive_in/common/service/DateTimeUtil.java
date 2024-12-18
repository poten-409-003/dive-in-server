package com.poten.dive_in.common.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    // 한국 시간으로 변환하고 문자열로 반환하는 메서드
    public static String formatDateTimeToKorean(LocalDateTime localDateTime) {
        // 한국 시간대 설정
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");

        // 한국 시간으로 변환
        LocalDateTime koreaDateTime = localDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(koreaZoneId)
                .toLocalDateTime();

        // 원하는 형식으로 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd HH:mm");
        return koreaDateTime.format(formatter);
    }
}
