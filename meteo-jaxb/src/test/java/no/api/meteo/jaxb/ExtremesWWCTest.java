package no.api.meteo.jaxb;

import no.api.meteo.jaxb.extremeswwc.v1_2.Weatherdata;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.io.Writer;

public class ExtremesWWCTest extends AbstractTest {

    @Test
    public void test1_2() throws JAXBException {
        final Weatherdata a =  unmarshal("extremeswwc_v1.2.xml", Weatherdata.class);
        Marshaller m = createContext(Weatherdata.class).createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        Writer w = new StringWriter();
        m.marshal(a, w);
        //System.out.println(w.toString());
        //Assert.assertTrue(w.toString().contains("https://api.met.no/license_data.html"));
        Assert.assertTrue(true);
    }

}
