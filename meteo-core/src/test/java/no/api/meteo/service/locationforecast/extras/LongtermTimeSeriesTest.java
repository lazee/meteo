package no.api.meteo.service.locationforecast.extras;

import org.junit.Test;

import static org.junit.Assert.*;

public class LongtermTimeSeriesTest {

    @Test
    public void testDifference() {
        final int i = LongtermTimeSeries.timeDifference();
        assertTrue(i > 0 && i < 3);
    }

}