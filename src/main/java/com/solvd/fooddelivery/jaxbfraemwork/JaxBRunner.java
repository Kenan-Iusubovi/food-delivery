package com.solvd.fooddelivery.jaxbfraemwork;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class JaxBRunner<T> {


    public T parseWithJaxB(Class<T> tClass, String filePath) {

        try {
            JAXBContext context = JAXBContext.newInstance(tClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return (T) unmarshaller.unmarshal(new File(filePath));

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
