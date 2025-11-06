package com.solvd;

import com.solvd.fooddelivery.entity.FoodDelivery;
import com.solvd.fooddelivery.handler.CustomSaxHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
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

    private final static String PATH_TO_INCORRECT_XSD = "src/main/resources/food-delivery-INCORRECT.xsd";
    private final static String PATH_TO_CORRECT_XSD = "src/main/resources/food-delivery-CORRECT.xsd";
    private final static String PATH_TO_XML = "src/main/resources/food-delivery.xml";

    public static void main(String[] args) {

       // System.out.println("Checking with incorrect xsd");
       // checkXmlDueXsd(PATH_TO_XML,PATH_TO_INCORRECT_XSD);
       // System.out.println("Checking with correct xsd");
       // checkXmlDueXsd(PATH_TO_XML,PATH_TO_CORRECT_XSD);

        parseXml(PATH_TO_XML);
    }

    public static void parseXml(String pathToXml){
        try {

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser parser = saxParserFactory.newSAXParser();

            CustomSaxHandler handler = new CustomSaxHandler();
            parser.parse(new File(pathToXml), handler);

            FoodDelivery foodDelivery = handler.getResult();

            System.out.println(foodDelivery);

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkXmlDueXsd(String xmlPath, String xsdPath){
        try {

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(xsdPath));

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            saxParserFactory.setSchema(schema);

            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new File(xmlPath), new DefaultHandler(){

            @Override
            public void error(SAXParseException e) throws SAXException {
                throw e;
            }

                @Override
            public void warning(SAXParseException e) throws SAXException {
                System.out.println("Warning: " + e.getMessage());
            }
        });

            System.out.println("XML file is valid due to XSD");
        } catch (SAXException e) {
            System.out.println("XML is NOT valid: " + e.getMessage());
        } catch (IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}

