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

package no.api.meteo.service.locationforecast;

import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.Location;
import no.api.meteo.entity.core.Meta;
import no.api.meteo.entity.core.service.locationforecast.Cloudiness;
import no.api.meteo.entity.core.service.locationforecast.Fog;
import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.HighClouds;
import no.api.meteo.entity.core.service.locationforecast.Humidity;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.LowClouds;
import no.api.meteo.entity.core.service.locationforecast.MediumClouds;
import no.api.meteo.entity.core.service.locationforecast.Model;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
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
import no.api.meteo.util.MeteoNetUtils;
import no.api.meteo.util.MeteoXppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

import static no.api.meteo.util.MeteoConstants.ATTR_ALTITUDE;
import static no.api.meteo.util.MeteoConstants.ATTR_BEAUFORT;
import static no.api.meteo.util.MeteoConstants.ATTR_CREATED;
import static no.api.meteo.util.MeteoConstants.ATTR_DEG;
import static no.api.meteo.util.MeteoConstants.ATTR_FROM;
import static no.api.meteo.util.MeteoConstants.ATTR_ID;
import static no.api.meteo.util.MeteoConstants.ATTR_LATITUDE;
import static no.api.meteo.util.MeteoConstants.ATTR_LICENSEURL;
import static no.api.meteo.util.MeteoConstants.ATTR_LONGITUDE;
import static no.api.meteo.util.MeteoConstants.ATTR_MAXVALUE;
import static no.api.meteo.util.MeteoConstants.ATTR_MINVALUE;
import static no.api.meteo.util.MeteoConstants.ATTR_MPS;
import static no.api.meteo.util.MeteoConstants.ATTR_NAME;
import static no.api.meteo.util.MeteoConstants.ATTR_NUMBER;
import static no.api.meteo.util.MeteoConstants.ATTR_PERCENT;
import static no.api.meteo.util.MeteoConstants.ATTR_PROBABILITY;
import static no.api.meteo.util.MeteoConstants.ATTR_TO;
import static no.api.meteo.util.MeteoConstants.ATTR_UNIT;
import static no.api.meteo.util.MeteoConstants.ATTR_VALUE;
import static no.api.meteo.util.MeteoConstants.TAG_CLOUDINESS;
import static no.api.meteo.util.MeteoConstants.TAG_FOG;
import static no.api.meteo.util.MeteoConstants.TAG_HIGH_CLOUDS;
import static no.api.meteo.util.MeteoConstants.TAG_HUMIDITY;
import static no.api.meteo.util.MeteoConstants.TAG_LOCATION;
import static no.api.meteo.util.MeteoConstants.TAG_LOW_CLOUDS;
import static no.api.meteo.util.MeteoConstants.TAG_MEDIUM_CLOUDS;
import static no.api.meteo.util.MeteoConstants.TAG_META;
import static no.api.meteo.util.MeteoConstants.TAG_MODEL;
import static no.api.meteo.util.MeteoConstants.TAG_PRECIPITATION;
import static no.api.meteo.util.MeteoConstants.TAG_PRESSURE;
import static no.api.meteo.util.MeteoConstants.TAG_SYMBOL;
import static no.api.meteo.util.MeteoConstants.TAG_SYMBOL_PROBABILITY;
import static no.api.meteo.util.MeteoConstants.TAG_TEMPERATURE;
import static no.api.meteo.util.MeteoConstants.TAG_TEMPERATURE_PROBABILITY;
import static no.api.meteo.util.MeteoConstants.TAG_TIME;
import static no.api.meteo.util.MeteoConstants.TAG_WEATHERDATA;
import static no.api.meteo.util.MeteoConstants.TAG_WIND_DIRECTION;
import static no.api.meteo.util.MeteoConstants.TAG_WIND_PROBABILITY;
import static no.api.meteo.util.MeteoConstants.TAG_WIND_SPEED;
import static no.api.meteo.util.MeteoXppUtils.getString;
import static no.api.meteo.util.MeteoXppUtils.getDate;
import static no.api.meteo.util.MeteoXppUtils.getDouble;
import static no.api.meteo.util.MeteoXppUtils.getInteger;

public class LocationforcastLTSParser implements MeteoDataParser<LocationForecast> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

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
            LocationForecast locationForecast = new LocationForecast();
            locationForecast.setForecasts(new ArrayList<Forecast>());
            int eventType = xpp.getEventType();
            Stack<Forecast> stack = new Stack<>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    handleStartTags(locationForecast, xpp, stack);
                } else if (eventType == XmlPullParser.END_TAG) {
                    handleEndTags(locationForecast, xpp, stack);
                } else {
                    log.trace("Skipping event type : " + eventType);
                }
                eventType = xpp.next();
            }
            return locationForecast;
        } catch (IOException e) {
            throw new MeteoDataParserException("An IO problem occurred", e);
        } catch (XmlPullParserException e) {
            throw new MeteoDataParserException("A parsing problem occurred", e);
        }
    }

    private void handleStartTags(LocationForecast locationForecast, XmlPullParser xpp, Stack<Forecast> stack) { // NOSONAR The complexity is quit alright :)
        String n = xpp.getName();
        switch (n) {
            case TAG_WEATHERDATA:
                handleWeatherDataTag(locationForecast, xpp);
                break;
            case TAG_TIME:
                handleTimeDataTag(stack, xpp);
                break;
            case TAG_LOCATION:
                if (locationForecast.getLocation() == null) {
                    handleLocationDataTag(locationForecast, xpp);
                } else {
                    log.trace("Skipping locations since it is already added.");
                }
                break;
            case TAG_PRECIPITATION:
                switchStackedObjectToPeriodForecastIfPeriodForecast(stack);
                ((PeriodForecast) stack.peek()).setPrecipitation(
                        new Precipitation(
                                null,
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_MINVALUE),
                                getDouble(xpp, ATTR_VALUE),
                                getDouble(xpp, ATTR_PROBABILITY),
                                getDouble(xpp, ATTR_MAXVALUE)));
                break;
            case TAG_SYMBOL:
                switchStackedObjectToPeriodForecastIfPeriodForecast(stack);
                ((PeriodForecast) stack.peek()).setSymbol(
                        new Symbol(
                                getString(xpp, ATTR_ID),
                                getInteger(xpp, ATTR_NUMBER)));
                break;
            case TAG_FOG:
                ((PointForecast) stack.peek()).setFog(
                        new Fog(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_PRESSURE:
                ((PointForecast) stack.peek()).setPressure(
                        new Pressure(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_HIGH_CLOUDS:
                ((PointForecast) stack.peek()).setHighClouds(
                        new HighClouds(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_MEDIUM_CLOUDS:
                ((PointForecast) stack.peek()).setMediumClouds(
                        new MediumClouds(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_LOW_CLOUDS:
                ((PointForecast) stack.peek()).setLowClouds(
                        new LowClouds(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_WIND_DIRECTION:
                ((PointForecast) stack.peek()).setWindDirection(
                        new WindDirection(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_NAME),
                                getDouble(xpp, ATTR_DEG)));
                break;
            case TAG_WIND_SPEED:
                ((PointForecast) stack.peek()).setWindSpeed(
                        new WindSpeed(
                                getString(xpp, ATTR_ID),
                                getInteger(xpp, ATTR_BEAUFORT),
                                getDouble(xpp, ATTR_MPS),
                                getString(xpp, ATTR_NAME)));
                break;
            case TAG_CLOUDINESS:
                ((PointForecast) stack.peek()).setCloudiness(
                        new Cloudiness(
                                getString(xpp, ATTR_ID),
                                getDouble(xpp, ATTR_PERCENT)));
                break;
            case TAG_HUMIDITY:
                ((PointForecast) stack.peek()).setHumidity(
                        new Humidity(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_TEMPERATURE:
                ((PointForecast) stack.peek()).setTemperature(
                        new Temperature(
                                getString(xpp, ATTR_ID),
                                getString(xpp, ATTR_UNIT),
                                getDouble(xpp, ATTR_VALUE)));
                break;
            case TAG_WIND_PROBABILITY:
                ((PointForecast) stack.peek()).setWindProbability(
                        new WindProbability(
                                getString(xpp, ATTR_UNIT),
                                getInteger(xpp, ATTR_VALUE)));
                break;
            case TAG_SYMBOL_PROBABILITY:
                switchStackedObjectToPeriodForecastIfPeriodForecast(stack);
                ((PeriodForecast) stack.peek()).setSymbolProbability(
                        new SymbolProbability(
                                getString(xpp, ATTR_UNIT),
                                getInteger(xpp, ATTR_VALUE)));
                break;
            case TAG_TEMPERATURE_PROBABILITY:
                ((PointForecast) stack.peek()).setTemperatureProbability(
                        new TemperatureProbability(
                                getString(xpp, ATTR_UNIT),
                                getInteger(xpp, ATTR_VALUE)));
                break;
            case TAG_META:
                Meta meta = new Meta();
                try {
                    meta.setLicenseUrl(MeteoNetUtils.createUrl(getString(xpp, ATTR_LICENSEURL)));
                } catch (MeteoException e) {
                    log.debug("License url not found in feed");
                }
                locationForecast.setMeta(meta);
                break;
            case TAG_MODEL:
                Model model = new Model();
                model.setName(getString(xpp, "name"));
                try {
                    model.setFrom(getDate(xpp, "from"));
                    model.setTo(getDate(xpp, "to"));
                    model.setRunEnded(getDate(xpp, "runended"));
                    model.setNextRun(getDate(xpp, "nextrun"));
                    model.setTermin(getDate(xpp, "termin"));
                } catch (MeteoException e) {
                    log.warn("Could not convert model dates found in returned xml", e);
                }

                locationForecast.getMeta().getModels().add(model);
                break;
            default:
                log.trace("Unhandled start tag: " + xpp.getName());
                break;
        }
    }

    private boolean isStackedObjectPointForecast(Stack<Forecast> stack) {
        if (stack.peek() instanceof PointForecast) {
            return true;
        }
        return false;
    }

    private void switchStackedObjectToPeriodForecastIfPeriodForecast(Stack<Forecast> stack) {
        if (isStackedObjectPointForecast(stack)) {
            PointForecast pointForecast = (PointForecast) stack.pop();
            PeriodForecast periodForecast = new PeriodForecast();
            periodForecast.setFromTime(pointForecast.getFromTime());
            periodForecast.setToTime(pointForecast.getToTime());
            stack.push(periodForecast);
        }
    }

    private void handleEndTags(LocationForecast locationForecast, XmlPullParser xpp, Stack<Forecast> stack) {
        if (TAG_TIME.equals(xpp.getName())) {
            locationForecast.getForecasts().add(stack.pop());
        } else {
            log.trace("Unhandled end tag: " + xpp.getName());
        }
    }

    private void handleTimeDataTag(Stack<Forecast> stack, XmlPullParser xpp) {
        PointForecast forecast = new PointForecast();
        try {
            forecast.setToTime(getDate(xpp, ATTR_TO));
            forecast.setFromTime(getDate(xpp, ATTR_FROM));
        } catch (MeteoException e) {
            log.warn("Could not convert time dates from xml", e);
        }

        stack.push(forecast);
    }

    private void handleWeatherDataTag(LocationForecast locationForecast, XmlPullParser xpp) {
        try {
            locationForecast.setCreated(getDate(xpp, ATTR_CREATED));
        } catch (MeteoException e) {
            log.warn("Could not convert created data from weatherData tag", e);
        }
    }

    private void handleLocationDataTag(LocationForecast locationForecast, XmlPullParser xpp) {
        locationForecast.setLocation(
                new Location(getDouble(xpp, ATTR_LONGITUDE),
                        getDouble(xpp, ATTR_LATITUDE),
                        getDouble(xpp, ATTR_ALTITUDE)));
    }
}
