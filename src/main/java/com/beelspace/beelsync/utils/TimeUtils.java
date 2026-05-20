package com.beelspace.beelsync.utils;

import com.beelspace.beelsync.config.impl.MainConfig;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {
    public static boolean hasTimePassed(LocalDateTime time){
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        return time == null || Duration.between(time, currentTime).toSeconds() >= MainConfig.REDIS.timeSynchronization/20;
    }
}
