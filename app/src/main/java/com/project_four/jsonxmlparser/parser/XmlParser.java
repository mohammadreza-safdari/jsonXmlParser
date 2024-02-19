package com.project_four.jsonxmlparser.parser;

import android.content.Context;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.widget.Toast;

import com.project_four.jsonxmlparser.MainActivity;
import com.project_four.jsonxmlparser.R;
import com.project_four.jsonxmlparser.model.Employee;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.NamespaceAware;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.XMLConstants;

public class XmlParser {
    private Context context;
    private List<Employee> employees = null;
    private Employee current_employee = null;
    private String current_tag = null;
    InputStream inputStream = null;
    public XmlParser(Context context){
        this.context = context;
    }
    public List<Employee> xmlPullParser(){
        employees = new ArrayList<Employee>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser pullParser = factory.newPullParser();
            inputStream = context.getResources().openRawResource(R.raw.employees);
            pullParser.setInput(inputStream, null);
            int eventType = pullParser.getEventType();
            while (eventType != pullParser.END_DOCUMENT){ //END_DOCUMENT : 1
                if (eventType == pullParser.START_TAG){
                    manageStartTag(pullParser);
                }
                else if (eventType == pullParser.END_TAG){
                    manageEndTag(pullParser);
                }
                eventType = pullParser.next();
            }
            inputStream.close();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private void manageEndTag(XmlPullParser pullParser) {
        String name = pullParser.getName();
        if (name.equalsIgnoreCase("employee") && current_employee != null){
            employees.add(current_employee);
        }
    }

    private void manageStartTag(XmlPullParser pullParser){
        String name = pullParser.getName();
        if ("employee".equals(name)){
            current_employee = new Employee();
            current_employee.setId(Integer.valueOf(pullParser.getAttributeValue(null, "id")));
        } else if (current_employee != null){
            try {
                switch (name){
                    case "name":
                        current_employee.setName(pullParser.nextText());
                        break;
                    case "position":
                        current_employee.setPosition(pullParser.nextText());
                        break;
                    case "department":
                        current_employee.setDepartment(pullParser.nextText());
                        break;
                    default:
                        break;
                }
            } catch (IOException  | XmlPullParserException e){
                e.printStackTrace();
            }
        }
    }

    public List<Employee> jdomXmlParser(){
        employees = new ArrayList<Employee>();
        try {
            SAXBuilder saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
            saxBuilder.setFeature("http://xml.org/sax/features/external-general-entities", false);
            Document document = saxBuilder.build(context.getAssets().open("employees.xml"));
            Element root_element = document.getRootElement();
            List<Element> elementsList = root_element.getChild("employees").getChildren("employee");
            for (Element element : elementsList){
                current_employee = new Employee();
                current_employee.setId(Integer.valueOf(element.getAttributeValue( "id")));
                current_employee.setName(element.getChildText("name"));
                current_employee.setPosition(element.getChildText("position"));
                current_employee.setDepartment(element.getChildText("department"));
                employees.add(current_employee);
            }
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        return employees;
    }
}
