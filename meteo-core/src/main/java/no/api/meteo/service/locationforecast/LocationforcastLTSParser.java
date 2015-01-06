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
import no.api.meteo.entity.core.service.locationforecast.Model;
import no.api.meteo.entity.core.service.locationforecast.Precipitation;
import no.api.meteo.entity.core.service.locationforecast.Pressure;
import no.api.meteo.entity.core.service.locationforecast.Symbol;
import no.api.meteo.entity.core.service.locationforecast.SymbolProbability;
import no.api.meteo.entity.core.service.locationforecast.Temperature;
import no.api.meteo.entity.core.service.locationforecast.TemperatureProbability;
import no.api.meteo.entity.core.service.locationforecast.WindDirection;
import no.api.meteo.entity.core.service.locationforecast.WindProbability;
import no.api.meteo.entity.core.service.locationforecast.WindSpeed;
import no.api.meteo.service.MeteoDataParser;
import no.api.meteo.service.MeteoDataParserException;
import no.api.meteo.service.locationforecast.builder.LocationForecastBuilder;
import no.api.meteo.service.locationforecast.builder.PeriodForecastBuilder;
import no.api.meteo.service.locationforecast.builder.PointForecastBuilder;
import no.api.meteo.util.EntityBuilder;
import no.api.meteo.util.MeteoNetUtils;
import no.api.meteo.util.MeteoXppUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import static no.api.meteo.util.MeteoConstants.*;
import static no.api.meteo.util.MeteoXppUtils.getDate;
import static no.api.meteo.util.MeteoXppUtils.getDouble;
import static no.api.meteo.util.MeteoXppUtils.getInteger;
import static no.api.meteo.util.MeteoXppUtils.getString;

@Slf4j
public final class LocationforcastLTSParser implements MeteoDataParser<LocationForecast> {

    @Override
    public LocationForecast parse(String data) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(data));
    }

    @Override
    public LocationForecast parse(InputStream inputStream) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(inputStream));
    }

    public LocationForecast doParse(XmlPullParser xpp) throws MeteoException {
        try {
            LocationForecastBuilder locationForecastBuilder = new LocationForecastBuilder();
            int eventType = xpp.getEventType();
            Stack<EntityBuilder> stack = new Stack<>();
            while (isMoreToParse(eventType)) {
                if (eventType == XmlPullParser.START_TAG) {
                    handleStartTags(locationForecastBuilder, xpp, stack);
                } else if (eventType == XmlPullParser.END_TAG) {
                    handleEndTags(locationForecastBuilder, xpp, stack);
                } else {
                    log.trace("Skipping event type : " + eventType);
                }
                eventType = xpp.next();
            }
            return locationForecastBuilder.build();
        } catch (IOException e) {
            throw new MeteoDataParserException("An IO problem occurred", e);
        } catch (XmlPullParserException e) {
            throw new MeteoDataParserException("A parsing problem occurred", e);
        }
    }

    private boolean isMoreToParse(int eventType) {
        return eventType != XmlPullParser.END_DOCUMENT;
    }

    private void handleStartTags(LocationForecastBuilder locationForecastBuilder, XmlPullParser xpp,
                                 Stack<EntityBuilder> stack) { // NOSONAR The complexity is quit alright :)
        String n = xpp.getName();
        switch (n) {
            case TAG_WEATHERDATA:
                handleWeatherDataTag(locationForecastBuilder, xpp);
                break;
            case TAG_TIME:
                handleTimeDataTag(stack, xpp);
                break;
            case TAG_LOCATION:

                if (locationForecastBuilder.getLocation() == null) {
                    handleLocationDataTag(locationForecastBuilder, xpp);
                } else {
                    log.trace("Skipping locations since it is already added.");
                }
                break;
            case TAG_PRECIPITATION:
                switchStackedObjectToPeriodForecastIfPeriodForecast(stack);
                ((PeriodForecastBuilder) stack.peek()).setPrecipitation(
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
                ((PeriodForecastBuilder) stack.peek()).setSymbol(
                        new Symbol(getString(xpp, ATTR_ID),
                                   getInteger(xpp, ATTR_NUMBER)));
                break;
            case TAG_FOG:
                ((PointForecastBuilder) stack.peek()).setFog(
                        new Fog(getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_PRESSURE:
                ((PointForecastBuilder) stack.peek()).setPressure(
                        new Pressure(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_HIGH_CLOUDS:
                ((PointForecastBuilder) stack.peek()).setHighClouds(
                        new HighClouds(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_MEDIUM_CLOUDS:
                ((PointForecastBuilder) stack.peek()).setMediumClouds(
                        new MediumClouds(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_LOW_CLOUDS:
                ((PointForecastBuilder) stack.peek()).setLowClouds(
                        new LowClouds(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_WIND_DIRECTION:
                ((PointForecastBuilder) stack.peek()).setWindDirection(
                        new WindDirection(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_NAME),
                                getDouble(xpp, ATTR_DEG)));
                break;
            case TAG_WIND_SPEED:
                ((PointForecastBuilder) stack.peek()).setWindSpeed(
                        new WindSpeed(
                                getString(xpp, ATTR_ID),
                                getInteger(xpp, ATTR_BEAUFORT),
                                getDouble(xpp, ATTR_MPS),
                                getString(xpp, ATTR_NAME)));
                break;
            case TAG_CLOUDINESS:
                ((PointForecastBuilder) stack.peek()).setCloudiness(
                        new Cloudiness(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_HUMIDITY:
                ((PointForecastBuilder) stack.peek()).setHumidity(
                        new Humidity(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_TEMPERATURE:
                ((PointForecastBuilder) stack.peek()).setTemperature(
                        new Temperature(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_WIND_PROBABILITY:
                ((PointForecastBuilder) stack.peek()).setWindProbability(
                        new WindProbability(
                                getString(xpp, ATTR_UNIT),
                                getInteger(xpp, ATTR_VALUE)));
                break;
            case TAG_SYMBOL_PROBABILITY:
                switchStackedObjectToPeriodForecastIfPeriodForecast(stack);
                ((PeriodForecastBuilder) stack.peek()).setSymbolProbability(
                        new SymbolProbability(
                                getString(xpp, ATTR_UNIT),
                                getInteger(xpp, ATTR_VALUE)));
                break;
            case TAG_TEMPERATURE_PROBABILITY:
                ((PointForecastBuilder) stack.peek()).setTemperatureProbability(
                        new TemperatureProbability(
                                getString(xpp, ATTR_UNIT),
                                getInteger(xpp, ATTR_VALUE)));
                break;
            case TAG_META:

                try {
                    locationForecastBuilder.getMetaBuilder().setLicenseUrl(
                            MeteoNetUtils.createUrl(getString(xpp, ATTR_LICENSEURL)));
                } catch (MeteoException e) {
                    log.debug("License url not found in feed");
                }
                break;
            case TAG_MODEL:

                try {
                    Model model = new Model(getDate(xpp, "to"),
                                            getDate(xpp, "from"),
                                            getDate(xpp, "runended"),
                                            getDate(xpp, "nextrun"),
                                            getDate(xpp, "termin"),
                                            getString(xpp, "name"));
                    locationForecastBuilder.getMetaBuilder().getModels().add(model);
                } catch (MeteoException e) {
                    log.warn("Could not convert model dates found in returned xml", e);
                }


                break;
            default:
                log.trace("Unhandled start tag: " + xpp.getName());
                break;
        }
    }

    private boolean isStackedObjectPointForecast(Stack<EntityBuilder> stack) {
        if (stack.peek() instanceof PointForecastBuilder) {
            return true;
        }
        return false;
    }

    private void switchStackedObjectToPeriodForecastIfPeriodForecast(Stack<EntityBuilder> stack) {
        if (isStackedObjectPointForecast(stack)) {
            PointForecastBuilder pointForecast = (PointForecastBuilder) stack.pop();
            PeriodForecastBuilder periodForecastBuilder = new PeriodForecastBuilder();
            periodForecastBuilder.setFromTime(pointForecast.getFromTime());
            periodForecastBuilder.setToTime(pointForecast.getToTime());
            stack.push(periodForecastBuilder);
        }
    }

    private void handleEndTags(LocationForecastBuilder locationForecastBuilder, XmlPullParser xpp,
                               Stack<EntityBuilder> stack) {
        if (TAG_TIME.equals(xpp.getName())) {
            locationForecastBuilder.getForecasts().add((Forecast) stack.pop().build());
        } else {
            log.trace("Unhandled end tag: " + xpp.getName());
        }
    }

    private void handleTimeDataTag(Stack<EntityBuilder> stack, XmlPullParser xpp) {
        try {
            PointForecastBuilder pointForecastBuilder = new PointForecastBuilder();
            pointForecastBuilder.setToTime(getDate(xpp, ATTR_TO));
            pointForecastBuilder.setFromTime(getDate(xpp, ATTR_FROM));
            stack.push(pointForecastBuilder);
        } catch (MeteoException e) {
            log.warn("Could not convert time dates from xml", e);
        }

    }

    private void handleWeatherDataTag(LocationForecastBuilder locationForecastBuilder, XmlPullParser xpp) {
        try {
            locationForecastBuilder.setCreated(getDate(xpp, ATTR_CREATED));
        } catch (MeteoException e) {
            log.warn("Could not convert created data from weatherData tag", e);
        }
    }

    private void handleLocationDataTag(LocationForecastBuilder locationForecastBuilder, XmlPullParser xpp) {
        locationForecastBuilder.setLocation(
                new Location(getDouble(xpp, ATTR_LONGITUDE),
                             getDouble(xpp, ATTR_LATITUDE),
                             getDouble(xpp, ATTR_ALTITUDE)));
    }
}
