package no.api.meteo.yr;

import org.junit.Assert;
import org.junit.Test;

public class YrLocateTest {

    @Test
    public void test_locale() {
        Assert.assertEquals("sted", YRLocale.NO.getPlace());
        Assert.assertEquals("stad", YRLocale.NN.getPlace());
        Assert.assertEquals("place", YRLocale.EN.getPlace());
    }
}
