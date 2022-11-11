package com.snp.common.config;

import com.snp.common.config.bean.ConfigEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultConfiguration implements Configuration {
    private final String path;
    private HashMap<String, ArrayList<ConfigEntry>> configEntryMap;

    public DefaultConfiguration(String path) {
        this.path = path;
        configEntryMap = new HashMap<>();
    }

    @Override
    public void load() {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = null;
            try {
                doc = db.parse(new File(path));
            } catch (org.xml.sax.SAXException e) {
                e.printStackTrace();
                throw new RuntimeException("Found issue while parsing file");

            }

            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("promotionType");

            ArrayList<ConfigEntry> configEntries = new ArrayList();
            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    ConfigEntry configEntry = new ConfigEntry();

                    String name = element.getAttribute("name");
                    configEntry.addEntryAttribute("name", name);
                    String className = element.getAttribute("className");
                    configEntry.addEntryAttribute("className", className);

                    NodeList nodeList = element.getElementsByTagName("price");
                    if (nodeList != null && nodeList.getLength() > 0) {
                        String price = nodeList.item(0).getTextContent();
                        configEntry.addEntry("price", price);
                    }

                    NodeList strategyNodeList = element.getElementsByTagName("strategyPrice");
                    if (strategyNodeList != null && strategyNodeList.getLength() > 0) {
                        String strategyPrice = strategyNodeList.item(0).getTextContent();
                        configEntry.addEntry("strategyPrice", strategyPrice);
                    }
                    configEntries.add(configEntry);

                }
            }
            configEntryMap.put("promotionType", configEntries);

        } catch (ParserConfigurationException  | IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getInt(String node) {
        return 0;
    }

    @Override
    public int getLong(String node) {
        return 0;
    }

    @Override
    public boolean getBoolean(String node) {
        return false;
    }

    @Override
    public String getString(String node) {
        return null;
    }

    @Override
    public String getValueFromRepeatedNodes(String repeatedNode, String entryNodeName, int entryIndex) {
        if (configEntryMap.containsKey(repeatedNode)) {
            ArrayList<ConfigEntry> configEntries = configEntryMap.get(repeatedNode);
            if (configEntries != null) {
                ConfigEntry configEntry = configEntries.get(entryIndex);
                if(configEntry != null) {
                    return configEntry.getEntry(entryNodeName);
                }
            }
        }

        return null;
    }

    @Override
    public String getAttributeFromRepeatedNodes(String repeatedNode, String entryNodeName, int entryIndex) {

        if (configEntryMap.containsKey(repeatedNode)) {
            ArrayList<ConfigEntry> configEntries = configEntryMap.get(repeatedNode);
            if (configEntries != null) {
                ConfigEntry configEntry = configEntries.get(entryIndex);
                if(configEntry != null) {
                    return configEntry.getEntryAttribute(entryNodeName);
                }
            }
        }

        return null;
    }

    @Override
    public int getTotalEntriesOfRepeatedNodes(String repeatedNode) {
        if (configEntryMap.containsKey(repeatedNode)) {
            ArrayList<ConfigEntry> configEntries = configEntryMap.get(repeatedNode);
            if (configEntries != null) {
               return configEntries.size();
            }
        }

        return 0;
    }
}
