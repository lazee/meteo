package no.api.meteo.jaxb;

import no.api.meteo.jaxb.locationforecast.v1_9.Weatherdata;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.io.Writer;

public class LocationForecastTest extends AbstractTest{

    @Test
    public void test1_0() throws JAXBException {
        final Weatherdata a =  unmarshal("locationforecast_v1.9.xml", Weatherdata.class);

        Assert.assertEquals("70", a.getProduct().get(0).getTime().get(0).getLocation().get(0).getAltitude());

        Marshaller m = createContext(Weatherdata.class).createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        Writer w = new StringWriter();
        m.marshal(a, w);
        System.out.println(w.toString());
        Assert.assertTrue(w.toString().contains("runended=\"2018-02-17T14:25:42Z\""));
    }
}
