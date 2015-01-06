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

package no.api.meteo.service.sunrise;

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.Location;
import no.api.meteo.entity.core.Meta;
import no.api.meteo.entity.core.service.sunrise.Noon;
import no.api.meteo.entity.core.service.sunrise.PhaseType;
import no.api.meteo.entity.core.service.sunrise.Sunrise;
import no.api.meteo.service.MeteoDataParser;
import no.api.meteo.service.MeteoDataParserException;
import no.api.meteo.service.sunrise.builder.MoonBuilder;
import no.api.meteo.service.sunrise.builder.SunBuilder;
import no.api.meteo.service.sunrise.builder.SunriseBuilder;
import no.api.meteo.service.sunrise.builder.SunriseDateBuilder;
import no.api.meteo.util.MeteoConstants;
import no.api.meteo.util.MeteoNetUtils;
import no.api.meteo.util.MeteoXppUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import static no.api.meteo.util.MeteoConstants.ATTR_ALTITUDE;
import static no.api.meteo.util.MeteoConstants.ATTR_CREATED;
import static no.api.meteo.util.MeteoConstants.ATTR_DATE;
import static no.api.meteo.util.MeteoConstants.ATTR_LATITUDE;
import static no.api.meteo.util.MeteoConstants.ATTR_LICENSEURL;
import static no.api.meteo.util.MeteoConstants.ATTR_LONGITUDE;
import static no.api.meteo.util.MeteoConstants.TAG_LOCATION;
import static no.api.meteo.util.MeteoConstants.TAG_META;
import static no.api.meteo.util.MeteoConstants.TAG_TIME;
import static no.api.meteo.util.MeteoXppUtils.getBoolean;
import static no.api.meteo.util.MeteoXppUtils.getDate;
import static no.api.meteo.util.MeteoXppUtils.getDouble;
import static no.api.meteo.util.MeteoXppUtils.getSimpleDate;
import static no.api.meteo.util.MeteoXppUtils.getString;

@Slf4j
public final class SunriseParser implements MeteoDataParser<Sunrise> {

    private static final String COULD_NOT_CONVERT_DATE_FROM_XML = "Could not convert date from xml";

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
            SunriseBuilder sunriseBuilder = new SunriseBuilder();
            int eventType = xpp.getEventType();
            Stack<SunriseDateBuilder> stack = new Stack<>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    handleStartTags(sunriseBuilder, xpp, stack);
                } else if (eventType == XmlPullParser.END_TAG) {
                    handleEndTags(sunriseBuilder, xpp, stack);
                } else {
                    log.trace("Skipping event type : " + eventType);
                }
                eventType = xpp.next();
            }
            return sunriseBuilder.build();
        } catch (XmlPullParserException e) {
            throw new MeteoDataParserException("A parsing problem occurred", e);
        } catch (IOException e) {
            throw new MeteoDataParserException("An IO problem occurred", e);
        }
    }

    private void handleStartTags(SunriseBuilder sunriseBuilder, XmlPullParser xpp,
                                 Stack<SunriseDateBuilder> stack) { // NOSONAR The complexity is quit alright :)
        String n = xpp.getName();
        switch (n) {
            case TAG_META:

                try {
                    Meta meta = new Meta(MeteoNetUtils.createUrl(getString(xpp, ATTR_LICENSEURL)), null);
                    sunriseBuilder.setMeta(meta);
                    sunriseBuilder.setCreated(getSimpleDate(xpp, ATTR_CREATED));
                } catch (MeteoException e) {
                    log.warn("License url not found in feed");
                }
                break;
            case TAG_TIME: {
                try {
                    SunriseDateBuilder sunriseDateBuilder = new SunriseDateBuilder();
                    sunriseDateBuilder.setDate(getSimpleDate(xpp, ATTR_DATE));
                    stack.push(sunriseDateBuilder);
                } catch (MeteoException e) {
                    log.warn(COULD_NOT_CONVERT_DATE_FROM_XML, e);
                }
                break;
            }
            case TAG_LOCATION:
                if (sunriseBuilder.getLocation() == null) {
                    sunriseBuilder.setLocation(new Location(getDouble(xpp, ATTR_LONGITUDE),
                                                            getDouble(xpp, ATTR_LATITUDE), null));
                } else {
                    log.trace("Skipping locations since it is already added.");
                }
                break;
            case MeteoConstants.TAG_SUN: {
                SunriseDateBuilder sunriseDateBuilder = stack.peek();
                try {
                    SunBuilder sunBuilder = new SunBuilder();
                    sunBuilder.setRise(getDate(xpp, MeteoConstants.ATTR_RISE));
                    sunBuilder.setSet(getDate(xpp, MeteoConstants.ATTR_SET));
                    sunBuilder.setNeverRise(getBoolean(xpp, MeteoConstants.ATTR_NEVER_RISE));
                    sunBuilder.setNeverSet(getBoolean(xpp, MeteoConstants.ATTR_NEVER_SET));
                    sunBuilder.setDaylength(getDouble(xpp, MeteoConstants.ATTR_DAYLENGTH));
                    sunriseDateBuilder.setSunBuilder(sunBuilder);
                } catch (MeteoException e) {
                    log.warn(COULD_NOT_CONVERT_DATE_FROM_XML, e);
                }
                break;
            }
            case MeteoConstants.TAG_NOON: {
                SunriseDateBuilder sunriseDateBuilder = stack.peek();

                try {
                    Noon noon = new Noon(getDate(xpp, MeteoConstants.TIME), getDouble(xpp, ATTR_ALTITUDE),
                                         getDouble(xpp, MeteoConstants.ATTR_DIRECTION));
                    sunriseDateBuilder.getSunBuilder().getNoon().add(noon);
                } catch (MeteoException e) {
                    log.warn(COULD_NOT_CONVERT_DATE_FROM_XML, e);
                }
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
                SunriseDateBuilder sunriseDate = stack.peek();

                try {
                    MoonBuilder moonBuilder = new MoonBuilder();
                    moonBuilder.setRise(getDate(xpp, MeteoConstants.ATTR_RISE));
                    moonBuilder.setSet(getDate(xpp, MeteoConstants.ATTR_SET));
                    moonBuilder.setNeverRise(getBoolean(xpp, MeteoConstants.ATTR_NEVER_RISE));
                    moonBuilder.setNeverSet(getBoolean(xpp, MeteoConstants.ATTR_NEVER_SET));
                    moonBuilder.setPhase(PhaseType.findByValue(getString(xpp, MeteoConstants.ATTR_PHASE)));
                    sunriseDate.setMoonBuilder(moonBuilder);
                } catch (MeteoException e) {
                    log.warn(COULD_NOT_CONVERT_DATE_FROM_XML, e);
                }
                break;
            }
            default:
                log.trace("Unhandled start tag: " + xpp.getName());
                break;
        }
    }

    private void handleEndTags(SunriseBuilder sunriseBuilder, XmlPullParser xpp, Stack<SunriseDateBuilder> stack) {
        if (TAG_TIME.equals(xpp.getName())) {
            sunriseBuilder.getDates().add(stack.pop().build());
        } else {
            log.trace("Unhandled end tag: " + xpp.getName());
        }
    }

}
