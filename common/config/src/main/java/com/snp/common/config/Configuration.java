package com.snp.common.config;

import com.snp.common.config.bean.ConfigEntry;

import java.util.List;

public interface Configuration {

    void load();
    int getInt(String node);
    int getLong(String node);
    boolean getBoolean(String node);
    String getString(String node);
    int getTotalEntriesOfRepeatedNodes(String repeatedNode);
    String getValueFromRepeatedNodes(String repeatedNode, String entryNodeName, int entryIndex);
    String getAttributeFromRepeatedNodes(String repeatedNode, String entrytNodeName, int entryIndex);
}
