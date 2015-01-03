/*
 * Copyright (c) 2011-2013 Amedia AS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.api.meteo.services;

import no.api.meteo.entity.core.service.locationforecast.WindDirection;
import no.api.meteo.entity.core.service.locationforecast.WindSpeed;
import no.api.meteo.entity.extras.BeaufortLevel;
import no.api.meteo.service.locationforecast.builder.PointForecastBuilder;
import org.junit.Assert;
import org.junit.Test;

public class WindSymbolHelperTest {

    @Test
    public void testCreateWindSymbolName() throws Exception {
        WindSpeed windSpeed = new WindSpeed("ff", 2, 0.0, "Light breeze");
        WindDirection windDirection = new WindDirection("dd", "SW", 0.0);

        PointForecastBuilder pointForecastBuilder = new PointForecastBuilder();
        Assert.assertNull(WindSymbolHelper.createWindSymbolName(null));
        Assert.assertNull(WindSymbolHelper.createWindSymbolName(pointForecastBuilder.build()));

        pointForecastBuilder.setWindDirection(windDirection);
        Assert.assertNull(WindSymbolHelper.createWindSymbolName(pointForecastBuilder.build()));

        pointForecastBuilder.setWindSpeed(windSpeed);
        Assert.assertNotNull(WindSymbolHelper.createWindSymbolName(pointForecastBuilder.build()));
        Assert.assertEquals("sw02", WindSymbolHelper.createWindSymbolName(pointForecastBuilder.build()));
    }

    @Test
    public void testFindBeaufortLevel() throws Exception {
        WindSpeed windSpeed = new WindSpeed("ff", 2, 0.0, "Light breeze");
        WindDirection windDirection = new WindDirection("dd", "SW", 0.0);
        Assert.assertNull(WindSymbolHelper.findBeaufortLevel(null));
        PointForecastBuilder pointForecastBuilder = new PointForecastBuilder();
        Assert.assertNull(WindSymbolHelper.findBeaufortLevel(pointForecastBuilder.build()));

        pointForecastBuilder.setWindDirection(windDirection);
        Assert.assertNull(WindSymbolHelper.findBeaufortLevel(pointForecastBuilder.build()));

        pointForecastBuilder.setWindSpeed(windSpeed);
        Assert.assertNotNull(WindSymbolHelper.findBeaufortLevel(pointForecastBuilder.build()));
        Assert.assertEquals(BeaufortLevel.LIGHT_BREEZE, WindSymbolHelper.findBeaufortLevel(pointForecastBuilder.build()));
    }
}
