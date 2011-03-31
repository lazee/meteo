package no.api.meteo.yr;

import org.junit.Assert;
import org.junit.Test;


public class YrUtilTest {

    @Test
    public void test_is_legal_place_name() throws Exception {
        Assert.assertFalse(YrUtil.isLegalPlaceName(null));
        Assert.assertFalse(YrUtil.isLegalPlaceName("/Bergen"));
        Assert.assertFalse(YrUtil.isLegalPlaceName("/Bergen/"));
        Assert.assertFalse(YrUtil.isLegalPlaceName("Bergen/"));
        Assert.assertTrue(YrUtil.isLegalPlaceName("Norge/Hordaland/Bergen"));
    }
}
