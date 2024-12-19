package com.poten.dive_in.common.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static String formatDateTimeToKorean(LocalDateTime localDateTime) {
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");

        LocalDateTime koreaDateTime = localDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(koreaZoneId)
                .toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd HH:mm");
        return koreaDateTime.format(formatter);
    }
}
