package com.homework.library_management.entities.common;

import com.homework.library_management.entities.log.AppLog;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class LogStorage {
    HashMap<String, ArrayList<AppLog>> logs;
    HashMap<String, String> groupIds;

    public LogStorage() {
        this.logs = new HashMap<>();
        this.groupIds = new HashMap<>();
    }

    public void clearGroup(String groupId) {
        logs.remove(groupId);
    }

    public ArrayList<AppLog> getGroupById(String groupId) {
        return logs.get(groupId);
    }

    public void init(String groupId) {
        logs.put(groupId, new ArrayList<AppLog>());
    }

    public void put(AppLog appLog) {
        logs.get(appLog.getGroupId()).add(appLog);
    }
}
