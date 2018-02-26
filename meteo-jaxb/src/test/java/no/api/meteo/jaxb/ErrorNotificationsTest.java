package no.api.meteo.jaxb;

import no.api.meteo.jaxb.errornotifications.v1_0.Errornotifications;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;

public class ErrorNotificationsTest extends AbstractTest {

    @Test
    public void test1_0() throws JAXBException {
        final Errornotifications a =  unmarshal("errornotifications_v1.0.xml", Errornotifications.class);
        Assert.assertTrue(a.getErrornotification().get(0).getDescription().getValue()
                                  .contains("Radar Hægebostad er dessverre ute av drift på grunn av strømbrudd"));

    }
}
