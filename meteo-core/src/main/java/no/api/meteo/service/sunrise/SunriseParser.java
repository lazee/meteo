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

package no.api.meteo.service.sunrise;

import no.api.meteo.MeteoException;
import no.api.meteo.service.MeteoDataParser;
import no.api.meteo.service.MeteoDataParserException;
import no.api.meteo.service.entity.Location;
import no.api.meteo.service.locationforecast.entity.Meta;
import no.api.meteo.service.sunrise.entity.Moon;
import no.api.meteo.service.sunrise.entity.Noon;
import no.api.meteo.service.sunrise.entity.PhaseType;
import no.api.meteo.service.sunrise.entity.Sun;
import no.api.meteo.service.sunrise.entity.Sunrise;
import no.api.meteo.service.sunrise.entity.SunriseDate;
import no.api.meteo.util.MeteoNetUtils;
import no.api.meteo.util.MeteoXppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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
import static no.api.meteo.util.MeteoXppUtils.getAttributeValue;
import static no.api.meteo.util.MeteoXppUtils.getBooleanAttributeValue;
import static no.api.meteo.util.MeteoXppUtils.getDateAttributeValue;
import static no.api.meteo.util.MeteoXppUtils.getDoubleAttributeValue;
import static no.api.meteo.util.MeteoXppUtils.getSimpleDateAttributeValue;

public class SunriseParser implements MeteoDataParser<Sunrise> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Sunrise parse(String data) throws MeteoException {
        try {
            Sunrise sunrise = new Sunrise();
            sunrise.setDates(new ArrayList<SunriseDate>());
            XmlPullParser xpp = MeteoXppUtils.createNewPullParser(data);
            int eventType = xpp.getEventType();
            Stack<SunriseDate> stack = new Stack<SunriseDate>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    handleStartTags(sunrise, xpp, stack);
                } else if (eventType == XmlPullParser.END_TAG) {
                    handleEndTags(sunrise, xpp, stack);
                } else {
                    // Skipping eveything else in the document
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

    private void handleStartTags(Sunrise sunrise, XmlPullParser xpp, Stack<SunriseDate> stack) {
        String n = xpp.getName();
        if (TAG_META.equals(n)) {
            Meta meta = new Meta();
            try {
                meta.setLicenseUrl(MeteoNetUtils.createUrl(getAttributeValue(xpp, ATTR_LICENSEURL)));
            } catch (MeteoException e) {
                log.warn("License url not found in feed");
            }
            sunrise.setMeta(meta);
        } else if (TAG_TIME.equals(n)) {
            SunriseDate sunriseDate = new SunriseDate();
            try {
                sunriseDate.setDate(getSimpleDateAttributeValue(xpp, ATTR_DATE));
            } catch (MeteoException e) {
                log.warn("Could not convert date from xml", e);
            }
            stack.push(sunriseDate);
        } else if (TAG_LOCATION.equals(n)) {
            if (sunrise.getLocation() == null) {
                sunrise.setLocation(new Location(getDoubleAttributeValue(xpp, ATTR_LONGITUDE),
                        getDoubleAttributeValue(xpp, ATTR_LATITUDE), null));
            } else {
                // Skipping locations since it is already added.
            }
        } else if ("sun".equals(n)) {
            SunriseDate sunriseDate = stack.peek();
            Sun sun = new Sun();
            sun.setDaylength(getDoubleAttributeValue(xpp, "daylength"));
            try {
                sun.setRise(getDateAttributeValue(xpp, "rise"));
                sun.setSet(getDateAttributeValue(xpp, "set"));
            } catch (MeteoException e) {
                log.warn("Could not convert date from xml", e);
            }
            sun.setNeverRise(getBooleanAttributeValue(xpp, "never_rise"));
            sun.setNeverSet(getBooleanAttributeValue(xpp, "never_set"));
            sunriseDate.setSun(sun);
        } else if ("noon".equals(n)) {
            SunriseDate sunriseDate = stack.peek();
            Noon noon = new Noon();
            noon.setAltitude(getDoubleAttributeValue(xpp, ATTR_ALTITUDE));
            noon.setDirection(getDoubleAttributeValue(xpp, "direction"));
            try {
                noon.setTime(getDateAttributeValue(xpp, "time"));
            } catch (MeteoException e) {
                log.warn("Could not convert date from xml", e);
            }
            sunriseDate.getSun().getNoon().add(noon);
        } else if ("error".equals(n)) {
            // FIXME Not implemented yet
        } else if ("twilight".equals(n)) {
            // FIXME Not implemented yet
        } else if ("moon".equals(n)) {
            SunriseDate sunriseDate = stack.peek();
            Moon moon = new Moon();
            try {
                moon.setRise(getDateAttributeValue(xpp, "rise"));
                moon.setSet(getDateAttributeValue(xpp, "set"));
            } catch (MeteoException e) {
                log.warn("Could not convert date from xml", e);
            }
            moon.setNeverRise(getBooleanAttributeValue(xpp, "never_rise"));
            moon.setNeverSet(getBooleanAttributeValue(xpp, "never_set"));
            moon.setPhase(PhaseType.findByValue(getAttributeValue(xpp, "phase")));
            sunriseDate.setMoon(moon);
        } else {
            log.trace("Unhandled start tag: " + xpp.getName());
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
