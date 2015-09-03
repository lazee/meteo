/*
 * Copyright (c) 2011-2015 Amedia Utvikling AS.
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

package no.api.meteo.service.locationforecast.extras;

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
        Assert.assertFalse(WindSymbolHelper.createWindSymbolName(null).isPresent());
        Assert.assertFalse(WindSymbolHelper.createWindSymbolName(pointForecastBuilder.build()).isPresent());

        pointForecastBuilder.setWindDirection(windDirection);
        Assert.assertFalse(WindSymbolHelper.createWindSymbolName(pointForecastBuilder.build()).isPresent());

        pointForecastBuilder.setWindSpeed(windSpeed);
        Assert.assertTrue(WindSymbolHelper.createWindSymbolName(pointForecastBuilder.build()).isPresent());
        Assert.assertEquals("sw02", WindSymbolHelper.createWindSymbolName(pointForecastBuilder.build()).get());
    }

    @Test
    public void testFindBeaufortLevel() throws Exception {
        WindSpeed windSpeed = new WindSpeed("ff", 2, 0.0, "Light breeze");
        WindDirection windDirection = new WindDirection("dd", "SW", 0.0);
        Assert.assertFalse(WindSymbolHelper.findBeaufortLevel(null).isPresent());
        PointForecastBuilder pointForecastBuilder = new PointForecastBuilder();
        Assert.assertFalse(WindSymbolHelper.findBeaufortLevel(pointForecastBuilder.build()).isPresent());

        pointForecastBuilder.setWindDirection(windDirection);
        Assert.assertFalse(WindSymbolHelper.findBeaufortLevel(pointForecastBuilder.build()).isPresent());

        pointForecastBuilder.setWindSpeed(windSpeed);
        Assert.assertTrue(WindSymbolHelper.findBeaufortLevel(pointForecastBuilder.build()).isPresent());
        Assert.assertEquals(BeaufortLevel.LIGHT_BREEZE, WindSymbolHelper.findBeaufortLevel(pointForecastBuilder.build()).get());
    }
}
