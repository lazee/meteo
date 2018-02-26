package no.api.meteo.jaxb;

import no.api.meteo.jaxb.sunrise.v1_1.Astrodata;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.io.Writer;

public class SunriseTest extends AbstractTest {

    @Test
    public void test1_0() throws JAXBException {
        final Astrodata a = unmarshal("sunrise_v1_0.xml", Astrodata.class);
        Assert.assertEquals("https://api.met.no/license_data.html", a.getMeta().getLicenseurl());
        Assert.assertEquals(1, a.getTime().size());
        Assert.assertEquals(7.168,
                            a.getTime().get(0).getLocation().get(0).getSun().getNoon().getAltitude().doubleValue(),
                            0.001);

        Marshaller m = createContext(Astrodata.class).createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        Writer w = new StringWriter();
        m.marshal(a, w);
        Assert.assertTrue(w.toString().contains("https://api.met.no/license_data.html"));
    }
}
