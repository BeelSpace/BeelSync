package com.beelspace.beelsync.database.mysql;

public enum MySqlRequests {
    CREATE_PROFILE("INSERT INTO profiles (nick_player, inventory, potion_effects, health, health_scale, max_health, experience, food_level, saturation, exhaustion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),
    GET_PROFILE("SELECT inventory, potion_effects, health, health_scale, max_health, experience, food_level, saturation, exhaustion FROM profiles WHERE nick_player = ?"),
    GET_PROFILES("SELECT nick_player, inventory, potion_effects, health, health_scale, max_health, experience, food_level, saturation, exhaustion FROM profiles"),
    UPDATE_PROFILE("UPDATE profiles SET inventory = ?, potion_effects = ?, health = ?, health_scale = ?, max_health = ?, experience = ?, food_level = ?, saturation = ?, exhaustion = ? WHERE nick_player = ?");

    private final String request;
    MySqlRequests(String request) {
        this.request = request;
    }
    public String getRequest() {
        return request;
    }
}
