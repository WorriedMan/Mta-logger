package me.oegodf.mta.main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

public class Translation {
    private static Translation sInstance = new Translation();
    private HashMap<String, String> mStrings;
    public static Translation get() {
        return sInstance;
    }

    private Translation() {

    }

    boolean load(String language) {
        try
        {
            mStrings = new HashMap<>();
            URL languageRes = getClass().getResource("/languages/"+language+".xml");
            File fXmlFile = new File(languageRes.toURI());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList stringList = doc.getElementsByTagName("string");
            for (int temp = 0; temp < stringList.getLength(); temp++) {
                Node stringNode = stringList.item(temp);
                String id = ((Element) stringNode).getAttribute("id");
                String text = stringNode.getTextContent();
                mStrings.put(id,text);
            }
            return true;
        }
        catch (Exception cex)
        {
            return false;
        }
    }

    public String string(String id) {
        Optional<String> stringTranslation = Optional.ofNullable(mStrings.get(id));
        return stringTranslation.orElse(id);
    }
}
