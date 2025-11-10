package com.solvd.fooddelivery.parsers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.solvd.fooddelivery.entity.FoodDelivery;
import com.solvd.fooddelivery.parsers.saxparser.handler.CustomSaxHandler;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.UUID;

public class ParsersRunner {

    private final static String PATH_TO_INCORRECT_XSD = "src/main/resources/food-delivery-INCORRECT.xsd";
    private final static String PATH_TO_CORRECT_XSD = "src/main/resources/food-delivery-CORRECT.xsd";
    private final static String PATH_TO_XML = "src/main/resources/food-delivery.xml";
    private final static String PATH_TO_JSON = "src/main/resources/food-delivery.json";

    private final static Logger log = LogManager.getLogger(ParsersRunner.class);

    public static void main(String[] args) {

        log.info("Checking with incorrect xsd");
        saxCheckXmlDueXsd(PATH_TO_XML, PATH_TO_INCORRECT_XSD);
        log.info("Checking with correct xsd");
        saxCheckXmlDueXsd(PATH_TO_XML, PATH_TO_CORRECT_XSD);

        saxParseXml(PATH_TO_XML);

        jaxBParseXml(PATH_TO_XML);

        jacksonParseJson(PATH_TO_JSON);
    }

    public static void saxParseXml(String pathToXml) {
        try {

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser parser = saxParserFactory.newSAXParser();

            CustomSaxHandler handler = new CustomSaxHandler();
            parser.parse(new File(pathToXml), handler);

            FoodDelivery foodDelivery = handler.getResult();

            log.info(foodDelivery);

        } catch (Exception e) {
            log.error(e.getCause() + e.getMessage());
        }
    }

    public static void saxCheckXmlDueXsd(String xmlPath, String xsdPath) {
        try {

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(xsdPath));

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            saxParserFactory.setSchema(schema);

            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new File(xmlPath), new DefaultHandler() {

                @Override
                public void error(SAXParseException e) throws SAXException {
                    throw e;
                }

                @Override
                public void warning(SAXParseException e) throws SAXException {
                   log.warn("Warning: " + e.getMessage());
                }
            });

            log.info("XML file is valid due to XSD");
        } catch (SAXException e) {
            log.error("XML is NOT valid: " + e.getMessage());
        } catch (IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void jaxBParseXml(String xmlPath) {

        try {

            JAXBContext context = JAXBContext.newInstance(FoodDelivery.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FoodDelivery foodDelivery = (FoodDelivery) unmarshaller.unmarshal(new File(xmlPath));

           log.info(foodDelivery);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static void jacksonParseJson(String jsonPath) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
            mapper.registerModule(new JavaTimeModule());
            FoodDelivery foodDelivery = mapper.readValue(new File(jsonPath), FoodDelivery.class);
            log.info(foodDelivery);

            String json = new String(Files.readAllBytes(Path.of(jsonPath)));

            String ownerName = JsonPath.read(json, "$.foodSpotOwners[0].name");
            System.out.println("1.Owner Name: " + ownerName);

            String foodSpotAddress = JsonPath.read(json,
                    "$.foodSpotOwners[0].foodSpots[0].address");
            System.out.println("2.Food Spot address: " + foodSpotAddress);

            String menuId = JsonPath.read(json, "$.foodSpotOwners[0].foodSpots[0].menus[0].id");
            System.out.println("3.Menu id: " + menuId);

            LocalTime foodSpotOpeningTime = LocalTime.parse(JsonPath.read(json,
                    "$.foodSpotOwners[0].foodSpots[0].openingTime"));
            System.out.println("4.Food Spot opening time: " + foodSpotOpeningTime);

            UUID orderNumber = UUID.fromString(JsonPath.read(json, "$.foodSpotOwners[0]." +
                    "foodSpots[0].orders[0].orderNumber"));
            System.out.println("5.Order number: " + orderNumber);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

