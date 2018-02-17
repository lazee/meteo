package no.api.meteo.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public abstract class AbstractTest {

    public <T extends Object> T unmarshal(String fileName, Class<T> type) throws JAXBException {
        JAXBContext context = createContext(type);
        Unmarshaller um = context.createUnmarshaller();
        return (T) um.unmarshal(this.getClass().getResourceAsStream("/" + fileName));
    }

    public JAXBContext createContext(Class type) throws JAXBException {
        return JAXBContext.newInstance(type);
    }

}
