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

package no.api.meteo.locationgroups.config;

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import no.api.meteo.entity.extras.locationgroup.ExtendedLocation;
import no.api.meteo.entity.extras.locationgroup.LocationGroup;
import no.api.meteo.locationgroups.config.builder.LocationGroupBuilder;
import no.api.meteo.service.MeteoDataParser;
import no.api.meteo.service.MeteoDataParserException;
import no.api.meteo.util.MeteoXppUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import static no.api.meteo.util.MeteoXppUtils.getDouble;
import static no.api.meteo.util.MeteoXppUtils.getInteger;
import static no.api.meteo.util.MeteoXppUtils.getString;

@Slf4j
public class ConfigParser implements MeteoDataParser<Map<String, LocationGroup>> {

    @Override
    public Map<String, LocationGroup> parse(String data) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(data));
    }

    @Override
    public Map<String, LocationGroup> parse(InputStream inputStream) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(inputStream));
    }

    public Map<String, LocationGroup> doParse(XmlPullParser xpp) throws MeteoException {
        try {
            Map<String, LocationGroup> groups = new TreeMap<>();

            int eventType = xpp.getEventType();
            Stack<LocationGroupBuilder> stack = new Stack<>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    handleStartTags(xpp, stack);
                } else if (eventType == XmlPullParser.END_TAG) {
                    handleEndTags(groups, xpp, stack);
                } else {
                    log.trace("Skipping event type : " + eventType);
                }
                eventType = xpp.next();
            }
            return groups;
        } catch (XmlPullParserException e) {
            throw new MeteoDataParserException("A parsing problem occurred", e);
        } catch (IOException e) {
            throw new MeteoDataParserException("An IO problem occurred", e);
        }
    }

    private void handleStartTags(XmlPullParser xpp, Stack<LocationGroupBuilder> stack) {
        String n = xpp.getName();
        if ("group".equals(n)) {
            LocationGroupBuilder locationGroupBuilder = new LocationGroupBuilder();
            locationGroupBuilder.setId(getString(xpp, "id"));
            stack.push(locationGroupBuilder);
        } else if ("location".equals(n)) {
            LocationGroupBuilder locationGroupBuilder = stack.peek();
            locationGroupBuilder.getLocations().add(new ExtendedLocation(
                    getDouble(xpp, "longitude"),
                    getDouble(xpp, "latitude"),
                    getInteger(xpp, "moh"),
                    getString(xpp, "name")));
        }
    }

    private void handleEndTags(Map<String, LocationGroup> groups, XmlPullParser xpp, Stack<LocationGroupBuilder> stack) {
        if ("group".equals(xpp.getName())) {
            LocationGroupBuilder locationGroupBuilder = stack.pop();
            groups.put(locationGroupBuilder.getId(), locationGroupBuilder.build());
        } else {
            log.trace("Unhandled end tag: " + xpp.getName());
        }
    }
}
