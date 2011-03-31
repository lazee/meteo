package no.api.meteo.examples;

import no.api.meteo.MeteoData;
import no.api.meteo.entity.LocationForecast;
import org.junit.Assert;
import org.junit.Test;

public class LocationExampleTest {

    @Test
    public void testRunExample() throws Exception {
        LocationExample locationExample = new LocationExample();
        MeteoData<LocationForecast> meteoData = locationExample.runExample();
        Assert.assertNotNull(meteoData);
        Assert.assertNotNull(meteoData.getRawResult());
        locationExample.shutDown();
    }
}
