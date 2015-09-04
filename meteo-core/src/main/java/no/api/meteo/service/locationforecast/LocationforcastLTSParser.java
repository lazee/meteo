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

package no.api.meteo.service.locationforecast;

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.Location;
import no.api.meteo.entity.core.service.locationforecast.Cloudiness;
import no.api.meteo.entity.core.service.locationforecast.Fog;
import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.HighClouds;
import no.api.meteo.entity.core.service.locationforecast.Humidity;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.LowClouds;
import no.api.meteo.entity.core.service.locationforecast.MediumClouds;
import no.api.meteo.entity.core.service.locationforecast.Precipitation;
import no.api.meteo.entity.core.service.locationforecast.Pressure;
import no.api.meteo.entity.core.service.locationforecast.Symbol;
import no.api.meteo.entity.core.service.locationforecast.SymbolProbability;
import no.api.meteo.entity.core.service.locationforecast.Temperature;
import no.api.meteo.entity.core.service.locationforecast.TemperatureProbability;
import no.api.meteo.entity.core.service.locationforecast.WindDirection;
import no.api.meteo.entity.core.service.locationforecast.WindProbability;
import no.api.meteo.entity.core.service.locationforecast.WindSpeed;
import no.api.meteo.service.AbstractMetaMeteoDataParser;
import no.api.meteo.service.MeteoDataParser;
import no.api.meteo.service.locationforecast.builder.LocationForecastBuilder;
import no.api.meteo.service.locationforecast.builder.PeriodForecastBuilder;
import no.api.meteo.service.locationforecast.builder.PointForecastBuilder;
import no.api.meteo.util.EntityBuilder;
import no.api.meteo.util.MeteoXppUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.Stack;

import static no.api.meteo.util.MeteoConstants.*;
import static no.api.meteo.util.MeteoXppUtils.getDouble;
import static no.api.meteo.util.MeteoXppUtils.getInteger;
import static no.api.meteo.util.MeteoXppUtils.getString;
import static no.api.meteo.util.MeteoXppUtils.getZonedDateTime;

@Slf4j
public final class LocationforcastLTSParser
        extends AbstractMetaMeteoDataParser<LocationForecast, EntityBuilder>
        implements MeteoDataParser<LocationForecast> {

    @Override
    public LocationForecast parse(String data) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(data), new LocationForecastBuilder());
    }

    @Override
    public LocationForecast parse(InputStream inputStream) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(inputStream), new LocationForecastBuilder());
    }

    @Override
    public void handleStartTags(XmlPullParser xpp, Stack<EntityBuilder> stack) throws MeteoException {
        switch (xpp.getName()) {
            case TAG_WEATHERDATA:
                handleWeatherDataTag((LocationForecastBuilder) getEntityBuilder(), xpp);
                break;
            case TAG_TIME:
                handleTimeDataTag(stack, xpp);
                break;
            case TAG_LOCATION:
                if (((LocationForecastBuilder) getEntityBuilder()).getLocation() == null) {
                    handleLocationDataTag((LocationForecastBuilder) getEntityBuilder(), xpp);
                } else {
                    log.trace("Skipping locations since it is already added.");
                }
                break;
            case TAG_PRECIPITATION:
                switchStackedObjectToPeriodForecastIfPeriodForecast(stack);
                periodPeek(stack).setPrecipitation(
                        new Precipitation(
                                null,
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE),
                                getDouble(xpp, ATTR_MINVALUE),
                                getDouble(xpp, ATTR_MAXVALUE),
                                getDouble(xpp, ATTR_PROBABILITY)));
                break;
            case TAG_SYMBOL:
                switchStackedObjectToPeriodForecastIfPeriodForecast(stack);
                periodPeek(stack).setSymbol(
                        new Symbol(getString(xpp, ATTR_ID),
                                   getInteger(xpp, ATTR_NUMBER)));
                break;
            case TAG_FOG:
                pointPeek(stack).setFog(
                        new Fog(getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_PRESSURE:
                pointPeek(stack).setPressure(
                        new Pressure(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_HIGH_CLOUDS:
                pointPeek(stack).setHighClouds(
                        new HighClouds(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_MEDIUM_CLOUDS:
                pointPeek(stack).setMediumClouds(
                        new MediumClouds(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_LOW_CLOUDS:
                pointPeek(stack).setLowClouds(
                        new LowClouds(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_WIND_DIRECTION:
                pointPeek(stack).setWindDirection(
                        new WindDirection(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_NAME),
                                getDouble(xpp, ATTR_DEG)));
                break;
            case TAG_WIND_GUST:
                pointPeek(stack).setWindGust(
                        new WindSpeed(
                                getString(xpp, ATTR_ID),
                                getInteger(xpp, ATTR_BEAUFORT),
                                getDouble(xpp, ATTR_MPS),
                                getString(xpp, ATTR_NAME)));
                break;
            case TAG_CLOUDINESS:
                pointPeek(stack).setCloudiness(
                        new Cloudiness(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_HUMIDITY:
                pointPeek(stack).setHumidity(
                        new Humidity(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_TEMPERATURE:
                pointPeek(stack).setTemperature(
                        new Temperature(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_DEWPOINT_TEMPERATURE:
                pointPeek(stack).setDewpointTemperature(
                        new Temperature(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_MIN_TEMPERATURE:
                periodPeek(stack).setMinTemperature(
                        new Temperature(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_MAX_TEMPERATURE:
                periodPeek(stack).setMaxTemperature(
                        new Temperature(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_WIND_PROBABILITY:
                pointPeek(stack).setWindProbability(
                        new WindProbability(
                                getString(xpp, ATTR_UNIT),
                                getInteger(xpp, ATTR_VALUE)));
                break;
            case TAG_SYMBOL_PROBABILITY:
                switchStackedObjectToPeriodForecastIfPeriodForecast(stack);
                periodPeek(stack).setSymbolProbability(
                        new SymbolProbability(
                                getString(xpp, ATTR_UNIT),
                                getInteger(xpp, ATTR_VALUE)));
                break;
            case TAG_TEMPERATURE_PROBABILITY:
                pointPeek(stack).setTemperatureProbability(
                        new TemperatureProbability(
                                getString(xpp, ATTR_UNIT),
                                getInteger(xpp, ATTR_VALUE)));
                break;
            case TAG_META:
                handleMetaTag(xpp);
                break;
            case TAG_MODEL:
                handleModelTag(xpp);
                break;
            default:
                log.trace(MSG_UNHANDLED_START_TAG, xpp.getName());
                break;
        }
    }

    @Override
    public void handleEndTags(EntityBuilder<LocationForecast> builder, XmlPullParser xpp, Stack<EntityBuilder> stack) {
        if (TAG_TIME.equals(xpp.getName())) {
            ((LocationForecastBuilder) getEntityBuilder()).getForecasts().add((Forecast) stack.pop().build());
        } else {
            log.trace(MSG_UNHANDLED_END_TAG, xpp.getName());
        }
    }

    private PeriodForecastBuilder periodPeek(Stack<EntityBuilder> stack) {
        return (PeriodForecastBuilder) stack.peek();
    }

    private PointForecastBuilder pointPeek(Stack<EntityBuilder> stack) {
        return (PointForecastBuilder) stack.peek();
    }

    private boolean isStackedObjectPointForecast(Stack<EntityBuilder> stack) {
        return stack.peek() instanceof PointForecastBuilder;
    }

    private void switchStackedObjectToPeriodForecastIfPeriodForecast(Stack<EntityBuilder> stack) {
        if (isStackedObjectPointForecast(stack)) {
            PointForecastBuilder pointForecast = (PointForecastBuilder) stack.pop();
            PeriodForecastBuilder periodForecastBuilder = new PeriodForecastBuilder();
            periodForecastBuilder.setFrom(pointForecast.getFrom());
            periodForecastBuilder.setTo(pointForecast.getTo());
            stack.push(periodForecastBuilder);
        }
    }

    private void handleTimeDataTag(Stack<EntityBuilder> stack, XmlPullParser xpp) {
        try {
            PointForecastBuilder pointForecastBuilder = new PointForecastBuilder();
            pointForecastBuilder.setTo(getZonedDateTime(xpp, ATTR_TO));
            pointForecastBuilder.setFrom(getZonedDateTime(xpp, ATTR_FROM));
            stack.push(pointForecastBuilder);
        } catch (MeteoException e) {
            log.warn("Could not convert time dates from xml", e);
        }

    }

    private void handleWeatherDataTag(LocationForecastBuilder locationForecastBuilder, XmlPullParser xpp) {
        try {
            locationForecastBuilder.setCreated(getZonedDateTime(xpp, ATTR_CREATED));
        } catch (MeteoException e) {
            log.warn("Could not convert created data from weatherData tag", e);
        }
    }

    private void handleLocationDataTag(LocationForecastBuilder locationForecastBuilder, XmlPullParser xpp)
            throws MeteoException {
        locationForecastBuilder.setLocation(
                new Location(getDouble(xpp, ATTR_LONGITUDE),
                             getDouble(xpp, ATTR_LATITUDE),
                             getInteger(xpp, ATTR_ALTITUDE), ""));
    }


}
