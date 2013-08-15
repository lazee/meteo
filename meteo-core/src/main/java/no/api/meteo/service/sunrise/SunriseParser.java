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

package no.api.meteo.service.sunrise;

import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.Location;
import no.api.meteo.entity.core.Meta;
import no.api.meteo.entity.core.service.sunrise.Moon;
import no.api.meteo.entity.core.service.sunrise.Noon;
import no.api.meteo.entity.core.service.sunrise.PhaseType;
import no.api.meteo.entity.core.service.sunrise.Sun;
import no.api.meteo.entity.core.service.sunrise.Sunrise;
import no.api.meteo.entity.core.service.sunrise.SunriseDate;
import no.api.meteo.service.MeteoDataParser;
import no.api.meteo.service.MeteoDataParserException;
import no.api.meteo.util.MeteoConstants;
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
import static no.api.meteo.util.MeteoConstants.ATTR_DATE;
import static no.api.meteo.util.MeteoConstants.ATTR_LATITUDE;
import static no.api.meteo.util.MeteoConstants.ATTR_LICENSEURL;
import static no.api.meteo.util.MeteoConstants.ATTR_LONGITUDE;
import static no.api.meteo.util.MeteoConstants.TAG_LOCATION;
import static no.api.meteo.util.MeteoConstants.TAG_META;
import static no.api.meteo.util.MeteoConstants.TAG_TIME;
import static no.api.meteo.util.MeteoXppUtils.getString;
import static no.api.meteo.util.MeteoXppUtils.getBoolean;
import static no.api.meteo.util.MeteoXppUtils.getDate;
import static no.api.meteo.util.MeteoXppUtils.getDouble;
import static no.api.meteo.util.MeteoXppUtils.getSimpleDate;

public class SunriseParser implements MeteoDataParser<Sunrise> {

    private static final String COULD_NOT_CONVERT_DATE_FROM_XML = "Could not convert date from xml";

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Sunrise parse(String data) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(data));
    }

    @Override
    public Sunrise parse(InputStream inputStream) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(inputStream));
    }

    public Sunrise doParse(XmlPullParser xpp) throws MeteoException {
        try {
            Sunrise sunrise = new Sunrise();
            sunrise.setDates(new ArrayList<SunriseDate>());
            int eventType = xpp.getEventType();
            Stack<SunriseDate> stack = new Stack<>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    handleStartTags(sunrise, xpp, stack);
                } else if (eventType == XmlPullParser.END_TAG) {
                    handleEndTags(sunrise, xpp, stack);
                } else {
                    log.trace("Skipping event type : " + eventType);
                }
                eventType = xpp.next();
            }
            return sunrise;
        } catch (XmlPullParserException e) {
            throw new MeteoDataParserException("A parsing problem occurred", e);
        } catch (IOException e) {
            throw new MeteoDataParserException("An IO problem occurred", e);
        }
    }

    private void handleStartTags(Sunrise sunrise, XmlPullParser xpp, Stack<SunriseDate> stack) { // NOSONAR The complexity is quit alright :)
        String n = xpp.getName();
        switch (n) {
            case TAG_META:
                Meta meta = new Meta();
                try {
                    meta.setLicenseUrl(MeteoNetUtils.createUrl(getString(xpp, ATTR_LICENSEURL)));
                } catch (MeteoException e) {
                    log.warn("License url not found in feed");
                }
                sunrise.setMeta(meta);
                break;
            case TAG_TIME: {
                SunriseDate sunriseDate = new SunriseDate();
                try {
                    sunriseDate.setDate(getSimpleDate(xpp, ATTR_DATE));
                } catch (MeteoException e) {
                    log.warn(COULD_NOT_CONVERT_DATE_FROM_XML, e);
                }
                stack.push(sunriseDate);
                break;
            }
            case TAG_LOCATION:
                if (sunrise.getLocation() == null) {
                    sunrise.setLocation(new Location(getDouble(xpp, ATTR_LONGITUDE),
                            getDouble(xpp, ATTR_LATITUDE), null));
                } else {
                    log.trace("Skipping locations since it is already added.");
                }
                break;
            case MeteoConstants.TAG_SUN: {
                SunriseDate sunriseDate = stack.peek();
                Sun sun = new Sun();
                sun.setDaylength(getDouble(xpp, MeteoConstants.ATTR_DAYLENGTH));
                try {
                    sun.setRise(getDate(xpp, MeteoConstants.ATTR_RISE));
                    sun.setSet(getDate(xpp, MeteoConstants.ATTR_SET));
                } catch (MeteoException e) {
                    log.warn(COULD_NOT_CONVERT_DATE_FROM_XML, e);
                }
                sun.setNeverRise(getBoolean(xpp, MeteoConstants.ATTR_NEVER_RISE));
                sun.setNeverSet(getBoolean(xpp, MeteoConstants.ATTR_NEVER_SET));
                sunriseDate.setSun(sun);
                break;
            }
            case MeteoConstants.TAG_NOON: {
                SunriseDate sunriseDate = stack.peek();
                Noon noon = new Noon();
                noon.setAltitude(getDouble(xpp, ATTR_ALTITUDE));
                noon.setDirection(getDouble(xpp, MeteoConstants.ATTR_DIRECTION));
                try {
                    noon.setTime(getDate(xpp, MeteoConstants.TIME));
                } catch (MeteoException e) {
                    log.warn(COULD_NOT_CONVERT_DATE_FROM_XML, e);
                }
                sunriseDate.getSun().getNoon().add(noon);
                break;
            }
            case "error":
                // FIXME Not implemented yet
                log.trace("Got error tag. This is not implemented yet");
                break;
            case "twilight":
                // FIXME Not implemented yet
                log.trace("Got twilight tag. This is not implemented yet");
                break;
            case "moon": {
                SunriseDate sunriseDate = stack.peek();
                Moon moon = new Moon();
                try {
                    moon.setRise(getDate(xpp, MeteoConstants.ATTR_RISE));
                    moon.setSet(getDate(xpp, MeteoConstants.ATTR_SET));
                } catch (MeteoException e) {
                    log.warn(COULD_NOT_CONVERT_DATE_FROM_XML, e);
                }
                moon.setNeverRise(getBoolean(xpp, MeteoConstants.ATTR_NEVER_RISE));
                moon.setNeverSet(getBoolean(xpp, MeteoConstants.ATTR_NEVER_SET));
                moon.setPhase(PhaseType.findByValue(getString(xpp, MeteoConstants.ATTR_PHASE)));
                sunriseDate.setMoon(moon);
                break;
            }
            default:
                log.trace("Unhandled start tag: " + xpp.getName());
                break;
        }
    }

    private void handleEndTags(Sunrise sunrise, XmlPullParser xpp, Stack<SunriseDate> stack) {
        if (TAG_TIME.equals(xpp.getName())) {
            sunrise.getDates().add(stack.pop());
        } else {
            log.trace("Unhandled end tag: " + xpp.getName());
        }
    }

}
