package no.api.meteo.yr;

import no.api.meteo.MeteoRuntimeException;
import no.api.meteo.test.MeteoTestUtils;
import org.junit.Assert;
import org.junit.Test;

import static no.api.meteo.yr.TestConstants.YR_TEST_DATA_1_JSON;
import static no.api.meteo.yr.TestConstants.YR_TEST_DATA_1_XML;

public class YRContentTest {

    /**
     * Simply check that the test file can be loaded and that is is unmodified
     *
     * @throws Exception If test file couldn't be loaded
     */
    @Test
    public void test_to_xml() throws Exception {
        String testContent = MeteoTestUtils.getTextResource(YR_TEST_DATA_1_XML);
        Assert.assertNotNull(testContent);
        YRContent yrContent = new YRContent(testContent);
        Assert.assertNotNull(yrContent.toXML());
        Assert.assertEquals(16464, yrContent.toXML().length());
    }

    @Test(expected = MeteoRuntimeException.class)
    public void test_invalid_xml_to_json() throws Exception {
        YRContent yrContent = new YRContent("<<>/>");
        yrContent.toJSON();
    }

    @Test
    public void test_to_json() throws Exception {
        String xmlTestContent = MeteoTestUtils.getTextResource(YR_TEST_DATA_1_XML);
        YRContent yrContent = new YRContent(xmlTestContent);
        Assert.assertEquals(MeteoTestUtils.getTextResource(YR_TEST_DATA_1_JSON), yrContent.toJSON());
    }


}
