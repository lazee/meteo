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

import static no.api.meteo.util.MeteoConstants.*;
import static no.api.meteo.util.MeteoXppUtils.getInteger;
import static no.api.meteo.util.MeteoXppUtils.getString;
import static no.api.meteo.util.MeteoXppUtils.getZonedDateTimeNorway;
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
    public void handleStartTags(XmlPullParser xpp, Stack<TimeBuilder> stack) throws MeteoException {
        switch (xpp.getName()) {
            case TAG_META:
                handleMetaTag(xpp);
                break;
            case TAG_PRODUCT_DESCRIPTION:
                getWeatherBuilder().setProductDescription(getString(xpp, ATTR_PRODNAME));
                break;
            case TAG_TITLE: {
                getWeatherBuilder().setTitle(readText(xpp));
                break;
            }
            case TAG_TIME: {
                handleTimeTag(xpp, stack);
                break;
            }
            case TAG_FORECAST_TYPE: {
                stack.peek().getForecastTypeBuilder().setName(getString(xpp, ATTR_NAME));
                break;
            }
            case TAG_AREA: {
                handleAreaTag(xpp, stack);
                break;
            }
            case TAG_LOCATION: {
                handleLocationTag(xpp, stack);
                break;
            }
            case TAG_HEADER: {
                stack.peek().getForecastTypeBuilder().getAreaBuilder().getLocationBuilder()
                        .setHeader(readText(xpp));
                break;
            }
            case TAG_IN: {
                stack.peek().getForecastTypeBuilder().getAreaBuilder().getLocationBuilder().setIn(readText(xpp));
            }
            default:
                log.trace(MSG_UNHANDLED_START_TAG, xpp.getName());
                break;
        }

    }

    @Override
    public void handleEndTags(EntityBuilder<Weather> builder, XmlPullParser xpp, Stack<TimeBuilder> stack) {
        switch (xpp.getName()) {
            case TAG_AREA:
                stack.peek().getForecastTypeBuilder().pushAreaBuilder();
                break;
            case TAG_TIME:
                getWeatherBuilder().getTimes().add(stack.pop().build());
                break;
            default:
                log.trace(MSG_UNHANDLED_END_TAG, xpp.getName());
                break;
        }
    }

    private void handleLocationTag(XmlPullParser xpp, Stack<TimeBuilder> stack) {
        LocationBuilder locationBuilder =
                stack.peek().getForecastTypeBuilder().getAreaBuilder().getLocationBuilder();
        locationBuilder.setName(getString(xpp, ATTR_NAME));
        locationBuilder.setType(getString(xpp, ATTR_TYPE));
        locationBuilder.setId(getString(xpp, ATTR_ID));
    }

    private void handleAreaTag(XmlPullParser xpp, Stack<TimeBuilder> stack) throws MeteoException {
        AreaBuilder areaBuilder = stack.peek().getForecastTypeBuilder().getAreaBuilder();
        areaBuilder.setType(getString(xpp, ATTR_TYPE));
        areaBuilder.setName(getString(xpp, ATTR_NAME));
        areaBuilder.setId(getInteger(xpp, ATTR_ID));
    }

    private void handleTimeTag(XmlPullParser xpp, Stack<TimeBuilder> stack) {
        TimeBuilder timeBuilder = new TimeBuilder();
        timeBuilder.setForecastOrigin(getString(xpp, ATTR_FORECAST_ORIGIN));
        timeBuilder.setFrom(getZonedDateTimeNorway(xpp, ATTR_FROM));
        timeBuilder.setTo(getZonedDateTimeNorway(xpp, ATTR_FROM));
        stack.push(timeBuilder);
    }

    private WeatherBuilder getWeatherBuilder() {
        return (WeatherBuilder) getEntityBuilder();
    }


}
