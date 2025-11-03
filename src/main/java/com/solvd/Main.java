package com.solvd;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {


        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File("src/main/resources/food-delivery.xsd"));

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            saxParserFactory.setSchema(schema);

            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new File("src/main/resources/food-delivery.xml"), new DefaultHandler());

            System.out.println("XML file is valid due to XSD");
        } catch (SAXException e) {
            System.out.println("XML is NOT valid: " + e.getMessage());
        } catch (IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}

