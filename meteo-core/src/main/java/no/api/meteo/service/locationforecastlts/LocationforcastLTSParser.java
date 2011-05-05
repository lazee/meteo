/*
 * Copyright (c) 2011 A-pressen Digitale Medier
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

package no.api.meteo.service.locationforecastlts;

import no.api.meteo.util.MeteoConstants;
import no.api.meteo.parser.MeteoDataParser;
import no.api.meteo.parser.MeteoDataParserException;
import no.api.meteo.MeteoException;
import no.api.meteo.util.MeteoNetUtils;
import no.api.meteo.service.locationforecastlts.entity.Cloudiness;
import no.api.meteo.service.locationforecastlts.entity.Fog;
import no.api.meteo.service.locationforecastlts.entity.HighClouds;
import no.api.meteo.service.locationforecastlts.entity.Humidity;
import no.api.meteo.service.locationforecastlts.entity.Location;
import no.api.meteo.service.locationforecastlts.entity.LocationForecast;
import no.api.meteo.service.locationforecastlts.entity.LowClouds;
import no.api.meteo.service.locationforecastlts.entity.MediumClouds;
import no.api.meteo.service.locationforecastlts.entity.PointForecast;
import no.api.meteo.service.locationforecastlts.entity.Precipitation;
import no.api.meteo.service.locationforecastlts.entity.Pressure;
import no.api.meteo.service.locationforecastlts.entity.Symbol;
import no.api.meteo.service.locationforecastlts.entity.SymbolProbability;
import no.api.meteo.service.locationforecastlts.entity.Temperature;
import no.api.meteo.service.locationforecastlts.entity.TemperatureProbability;
import no.api.meteo.service.locationforecastlts.entity.WindDirection;
import no.api.meteo.service.locationforecastlts.entity.WindProbability;
import no.api.meteo.service.locationforecastlts.entity.WindSpeed;
import no.api.meteo.xpp.XppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import static no.api.meteo.util.MeteoConstants.ATTR_ALTITUDE;
import static no.api.meteo.util.MeteoConstants.ATTR_BEAUFORT;
import static no.api.meteo.util.MeteoConstants.ATTR_CREATED;
import static no.api.meteo.util.MeteoConstants.ATTR_DEG;
import static no.api.meteo.util.MeteoConstants.ATTR_FROM;
import static no.api.meteo.util.MeteoConstants.ATTR_ID;
import static no.api.meteo.util.MeteoConstants.ATTR_LATITUDE;
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
import static no.api.meteo.util.MeteoParserUtils.getAttributeValue;
import static no.api.meteo.util.MeteoParserUtils.getDateAttributeValue;
import static no.api.meteo.util.MeteoParserUtils.getDoubleAttributeValue;
import static no.api.meteo.util.MeteoParserUtils.getIntegerAttributeValue;

/**
 *
 */
public class LocationforcastLTSParser implements MeteoDataParser<LocationForecast> {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public LocationForecast parse(String data) throws MeteoDataParserException {
        try {
            LocationForecast locationForecast = new LocationForecast();
            locationForecast.setForecasts(new ArrayList<PointForecast>());
            XmlPullParser xpp = XppUtils.createNewPullParser(data);
            int eventType = xpp.getEventType();
            Stack<PointForecast> stack = new Stack<PointForecast>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    handleStartTags(locationForecast, xpp, stack);
                } else if (eventType == XmlPullParser.END_TAG) {
                    handleEndTags(locationForecast, xpp, stack);
                } else {
                    // Skipping everything else in the document.
                }
                eventType = xpp.next();
            }
            return locationForecast;
        } catch (IOException e) {
            throw new MeteoDataParserException("An IO problem occured", e);
        } catch (XmlPullParserException e) {
            throw new MeteoDataParserException("A parsing problem occurred", e);
        }
    }

    private void handleStartTags(LocationForecast locationForecast, XmlPullParser xpp, Stack<PointForecast> stack) {
        String n = xpp.getName();
        if (TAG_WEATHERDATA.equals(n)) {
            handleWeatherDataTag(locationForecast, xpp);
        } else if (TAG_TIME.equals(n)) {
            handleTimeDataTag(stack, xpp);
        } else if (TAG_LOCATION.equals(n)) {
            if (locationForecast.getLocation() == null) {
                handleLocationDataTag(locationForecast, xpp);
            } else {
                // Skipping locations since it is already added.
            }
        } else if (TAG_PRECIPITATION.equals(n)) {
            stack.peek().setPrecipitation(
                    new Precipitation(
                            null,
                            getAttributeValue(xpp, ATTR_UNIT),
                            getDoubleAttributeValue(xpp, ATTR_MINVALUE),
                            getDoubleAttributeValue(xpp, ATTR_VALUE),
                            getDoubleAttributeValue(xpp, ATTR_PROBABILITY),
                            getDoubleAttributeValue(xpp, ATTR_MAXVALUE)));
        } else if (TAG_SYMBOL.equals(n)) {
            stack.peek().setSymbol(
                    new Symbol(
                            getAttributeValue(xpp, ATTR_ID),
                            getIntegerAttributeValue(xpp, ATTR_NUMBER)));
        } else if (TAG_FOG.equals(n)) {
            stack.peek().setFog(
                    new Fog(
                            getAttributeValue(xpp, ATTR_ID),
                            getDoubleAttributeValue(xpp, ATTR_PERCENT)));
        } else if (TAG_PRESSURE.equals(n)) {
            stack.peek().setPressure(
                    new Pressure(
                            getAttributeValue(xpp, ATTR_ID),
                            getAttributeValue(xpp, ATTR_UNIT),
                            getDoubleAttributeValue(xpp, ATTR_VALUE)));
        } else if (TAG_HIGH_CLOUDS.equals(n)) {
            stack.peek().setHighClouds(
                    new HighClouds(
                            getAttributeValue(xpp, ATTR_ID),
                            getDoubleAttributeValue(xpp, ATTR_PERCENT)));
        } else if (TAG_MEDIUM_CLOUDS.equals(n)) {
            stack.peek().setMediumClouds(
                    new MediumClouds(
                            getAttributeValue(xpp, ATTR_ID),
                            getDoubleAttributeValue(xpp, ATTR_PERCENT)));
        } else if (TAG_LOW_CLOUDS.equals(n)) {
            stack.peek().setLowClouds(
                    new LowClouds(
                            getAttributeValue(xpp, ATTR_ID),
                            getDoubleAttributeValue(xpp, ATTR_PERCENT)));
        } else if (TAG_WIND_DIRECTION.equals(n)) {
            stack.peek().setWindDirection(
                    new WindDirection(
                            getAttributeValue(xpp, ATTR_ID),
                            getAttributeValue(xpp, ATTR_NAME),
                            getDoubleAttributeValue(xpp, ATTR_DEG)));
        } else if (TAG_WIND_SPEED.equals(n)) {
            stack.peek().setWindSpeed(
                    new WindSpeed(
                            getAttributeValue(xpp, ATTR_ID),
                            getIntegerAttributeValue(xpp, ATTR_BEAUFORT),
                            getDoubleAttributeValue(xpp, ATTR_MPS),
                            getAttributeValue(xpp, ATTR_NAME)));
        } else if (TAG_CLOUDINESS.equals(n)) {
            stack.peek().setCloudiness(
                    new Cloudiness(
                            getAttributeValue(xpp, ATTR_ID),
                            getDoubleAttributeValue(xpp, ATTR_PERCENT)));
        } else if (TAG_HUMIDITY.equals(n)) {
            stack.peek().setHumidity(
                    new Humidity(
                            getAttributeValue(xpp, ATTR_ID),
                            getAttributeValue(xpp, ATTR_UNIT),
                            getDoubleAttributeValue(xpp, ATTR_VALUE)));
        } else if (TAG_TEMPERATURE.equals(n)) {
            stack.peek().setTemperature(
                    new Temperature(
                            getAttributeValue(xpp, ATTR_ID),
                            getAttributeValue(xpp, ATTR_UNIT),
                            getDoubleAttributeValue(xpp, ATTR_VALUE)));
        } else if (TAG_WIND_PROBABILITY.equals(n)) {
            stack.peek().setWindProbability(
                    new WindProbability(
                            getAttributeValue(xpp, ATTR_UNIT),
                            getIntegerAttributeValue(xpp, ATTR_VALUE)));
        } else if (TAG_SYMBOL_PROBABILITY.equals(n)) {
            stack.peek().setSymbolProbability(
                    new SymbolProbability(
                            getAttributeValue(xpp, ATTR_UNIT),
                            getIntegerAttributeValue(xpp, ATTR_VALUE)));
        } else if (TAG_TEMPERATURE_PROBABILITY.equals(n)) {
            stack.peek().setTemperatureProbability(
                    new TemperatureProbability(
                            getAttributeValue(xpp, ATTR_UNIT),
                            getIntegerAttributeValue(xpp, ATTR_VALUE)));
        } else if (MeteoConstants.TAG_META.equals(n)) {
            try {
                locationForecast.setLicenseUrl(
                        MeteoNetUtils.createUrl(getAttributeValue(xpp, MeteoConstants.ATTR_LICENSEURL)));
            } catch (MeteoException e) {
                log.warn("License url not found in feed");
            }
        } else {
            log.trace("Unhandled start tag: " + xpp.getName());
        }
    }

    private void handleEndTags(LocationForecast locationForecast, XmlPullParser xpp, Stack<PointForecast> stack) {
        if (TAG_TIME.equals(xpp.getName())) {
            locationForecast.getForecasts().add(stack.pop());
        } else {
            log.trace("Unhandled end tag: " + xpp.getName());
        }
    }

    private void handleTimeDataTag(Stack<PointForecast> stack, XmlPullParser xpp) {
        PointForecast forecast = new PointForecast();
        forecast.setToTime(getDateAttributeValue(xpp, ATTR_TO));
        forecast.setFromTime(getDateAttributeValue(xpp, ATTR_FROM));
        stack.push(forecast);
    }

    private void handleWeatherDataTag(LocationForecast locationForecast, XmlPullParser xpp) {
        locationForecast.setCreated(getDateAttributeValue(xpp, ATTR_CREATED));
    }

    private void handleLocationDataTag(LocationForecast locationForecast, XmlPullParser xpp) {
        locationForecast.setLocation(
                new Location(getDoubleAttributeValue(xpp, ATTR_LONGITUDE),
                        getDoubleAttributeValue(xpp, ATTR_LATITUDE),
                        getIntegerAttributeValue(xpp, ATTR_ALTITUDE)));
    }
}
