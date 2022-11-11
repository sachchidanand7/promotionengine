package com.snp.common.config.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigEntry {
    HashMap<String, String> entryMap;
    HashMap<String, String>  entryAttributeMap;

    public ConfigEntry() {
        entryMap = new HashMap<>();
        entryAttributeMap = new HashMap<>();
    }

    public void addEntry(String entry, String value) {
        entryMap.put(entry, value);
    }

    public void addEntryAttribute(String entryAttribute, String value) {
        entryAttributeMap.put(entryAttribute, value);
    }

    public String getEntry(String key) {
        return entryMap.get(key);
    }

    public String getEntryAttribute(String key) {
        return entryAttributeMap.get(key);
    }


}
