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

package no.api.meteo.service.textforecast;

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.service.textforecast.Weather;
import no.api.meteo.service.AbstractMetaMeteoDataParser;
import no.api.meteo.service.MeteoDataParser;
import no.api.meteo.service.textforecast.builder.AreaBuilder;
import no.api.meteo.service.textforecast.builder.LocationBuilder;
import no.api.meteo.service.textforecast.builder.TimeBuilder;
import no.api.meteo.service.textforecast.builder.WeatherBuilder;
import no.api.meteo.util.EntityBuilder;
import no.api.meteo.util.MeteoXppUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.Stack;

import static no.api.meteo.util.MeteoConstants.TAG_META;
import static no.api.meteo.util.MeteoXppUtils.getInteger;
import static no.api.meteo.util.MeteoXppUtils.getString;
import static no.api.meteo.util.MeteoXppUtils.getZoneDateTimeNorway;
import static no.api.meteo.util.MeteoXppUtils.readText;

@Slf4j
public class TextforecastParser extends AbstractMetaMeteoDataParser<Weather, TimeBuilder>
        implements MeteoDataParser<Weather> {

    @Override
    public Weather parse(String data) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(data), new WeatherBuilder());
    }

    @Override
    public Weather parse(InputStream inputStream) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(inputStream), new WeatherBuilder());
    }


    @Override
    public void handleStartTags(XmlPullParser xpp, Stack<TimeBuilder> stack) {
        switch (xpp.getName()) {
            case TAG_META:
                handleMetaTag(getWeatherBuilder(), xpp);
                break;
            case "productdescription":
                getWeatherBuilder().setProductDescription(getString(xpp, "prodname"));
                break;
            case "title": {
                getWeatherBuilder().setTitle(readText(xpp).trim());
                break;
            }
            case "time": {
                handleTimeTag(xpp, stack);
                break;
            }
            case "forecasttype": {
                stack.peek().getForecastTypeBuilder().setName(getString(xpp, "name"));
                break;
            }
            case "area": {
                handleAreaTag(xpp, stack);
                break;
            }
            case "location": {
                handleLocationTag(xpp, stack);
                break;
            }
            case "header": {
                stack.peek().getForecastTypeBuilder().getAreaBuilder().getLocationBuilder()
                        .setHeader(readText(xpp).trim());
                break;
            }
            case "in": {
                stack.peek().getForecastTypeBuilder().getAreaBuilder().getLocationBuilder().setIn(readText(xpp).trim());
            }
            default:
                log.trace("Unhandled start tag: " + xpp.getName());
                break;
        }

    }

    @Override
    public void handleEndTags(EntityBuilder<Weather> builder, XmlPullParser xpp, Stack<TimeBuilder> stack) {
        if ("area".equals(xpp.getName())) {
            stack.peek().getForecastTypeBuilder().pushAreaBuilder();
        } else if ("time".equals(xpp.getName())) {
            getWeatherBuilder().getTimes().add(stack.pop().build());
        } else {
            log.trace("Unhandled end tag: " + xpp.getName());
        }
    }

    private void handleLocationTag(XmlPullParser xpp, Stack<TimeBuilder> stack) {
        LocationBuilder locationBuilder =
                stack.peek().getForecastTypeBuilder().getAreaBuilder().getLocationBuilder();
        locationBuilder.setName(getString(xpp, "name"));
        locationBuilder.setType(getString(xpp, "type"));
        locationBuilder.setId(getString(xpp, "id"));
    }

    private void handleAreaTag(XmlPullParser xpp, Stack<TimeBuilder> stack) {
        AreaBuilder areaBuilder = stack.peek().getForecastTypeBuilder().getAreaBuilder();
        areaBuilder.setType(getString(xpp, "type"));
        areaBuilder.setName(getString(xpp, "name"));
        areaBuilder.setId(getInteger(xpp, "id"));
    }

    private void handleTimeTag(XmlPullParser xpp, Stack<TimeBuilder> stack) {
        TimeBuilder timeBuilder = new TimeBuilder();
        timeBuilder.setForecastOrigin(getString(xpp, "forecast_origin"));

        try {
            timeBuilder.setFrom(getZoneDateTimeNorway(xpp, "from"));
        } catch (MeteoException e) {
            log.error("Could not parse from date", e);
        }

        try {
            timeBuilder.setTo(getZoneDateTimeNorway(xpp, "to"));
        } catch (MeteoException e) {
            log.error("Could not parse to date", e);
        }

        stack.push(timeBuilder);
    }

    private WeatherBuilder getWeatherBuilder() {
        return (WeatherBuilder) getEntityBuilder();
    }


}
