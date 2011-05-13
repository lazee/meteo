package no.api.meteo.locationgroups.config;

import no.api.meteo.entity.extras.locationgroup.LocationGroup;
import no.api.meteo.test.MeteoTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.Map;

public class ConfigLoaderTest {

    @Test
    public void testLoadString() throws Exception {
        String s = MeteoTestUtils.getTextResource("/META-INF/meteo/config/meteo-config.xml");
        Map<String,LocationGroup> config = ConfigLoader.load(s);
        Assert.assertNotNull(config);
        Assert.assertEquals(77, config.size());
        LocationGroup locationGroup = config.get("an");
        Assert.assertNotNull(locationGroup);
        Assert.assertEquals(9, locationGroup.getLocations().size());
        Assert.assertEquals("Fauske", locationGroup.getLocations().get(1).getName());
    }

    @Test
    public void testLoadInputstream() throws Exception {
        InputStream is = MeteoTestUtils.getResource("/META-INF/meteo/config/meteo-config.xml");
        Map<String,LocationGroup> config = ConfigLoader.load(is);
        Assert.assertNotNull(config);
        Assert.assertEquals(77, config.size());
        LocationGroup locationGroup = config.get("an");
        Assert.assertNotNull(locationGroup);
        Assert.assertEquals(9, locationGroup.getLocations().size());
        Assert.assertEquals("Fauske", locationGroup.getLocations().get(1).getName());
    }
}
