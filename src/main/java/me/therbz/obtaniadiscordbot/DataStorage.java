package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class DataStorage {
    private HashMap<User, Long> suggestCooldowns;
    private HashMap<User, Long> bugReportCooldowns;

    public DataStorage() {
        this.suggestCooldowns = new HashMap<User, Long>();
        this.bugReportCooldowns = new HashMap<User, Long>();
    }

    public Long getUserSuggestCooldown(User user) {
        return this.suggestCooldowns.get(user);
    }

    public Long getUserBugReportCooldown(User user) {
        return this.bugReportCooldowns.get(user);
    }

    public Long setUserSuggestCooldown(User user, Long time) {
        return this.suggestCooldowns.put(user, time);
    }

    public Long setUserBugReportCooldown(User user, Long time) {
        return this.bugReportCooldowns.put(user, time);
    }
}
