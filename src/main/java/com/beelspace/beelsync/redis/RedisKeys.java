package com.beelspace.beelsync.redis;

public enum RedisKeys {
    PROFILE("profile"),
    LAST_TIME_UPDATE("lastTimeUpdate");

    private String key;
    RedisKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
