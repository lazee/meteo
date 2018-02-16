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

package no.api.meteo.service.textforecast.available;

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.service.textforecast.available.Available;
import no.api.meteo.service.AbstractMeteoDataParser;
import no.api.meteo.service.MeteoDataParser;
import no.api.meteo.service.textforecast.available.builder.AvailableBuilder;
import no.api.meteo.service.textforecast.available.builder.ParameterBuilder;
import no.api.meteo.service.textforecast.available.builder.QueryBuilder;
import no.api.meteo.util.EntityBuilder;
import no.api.meteo.util.MeteoXppUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.URI;
import java.util.Stack;

import static no.api.meteo.util.MeteoConstants.TAG_AVAILABLE;
import static no.api.meteo.util.MeteoConstants.TAG_LABEL;
import static no.api.meteo.util.MeteoConstants.TAG_NAME;
import static no.api.meteo.util.MeteoConstants.TAG_PARAMETER;
import static no.api.meteo.util.MeteoConstants.TAG_QUERY;
import static no.api.meteo.util.MeteoConstants.TAG_URI;
import static no.api.meteo.util.MeteoConstants.TAG_VALUE;
import static no.api.meteo.util.MeteoXppUtils.readText;

@Slf4j
public class AvailableTextforecastsParser extends AbstractMeteoDataParser<Available, QueryBuilder>
        implements MeteoDataParser<Available> {

    private ParameterBuilder parameterBuilder;

    @Override
    public Available parse(String data) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(data), new AvailableBuilder());
    }

    @Override
    public Available parse(InputStream inputStream) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(inputStream), new AvailableBuilder());
    }

    @Override
    public void handleStartTags(XmlPullParser xpp, Stack<QueryBuilder> stack) throws MeteoException {
        switch (xpp.getName()) {
            case TAG_AVAILABLE:
                break;
            case TAG_QUERY: {
                stack.push(new QueryBuilder());
                break;
            }
            case TAG_PARAMETER:
                parameterBuilder = new ParameterBuilder();
                break;
            case TAG_NAME: {
                parameterBuilder.setName(readText(xpp));
                break;
            }
            case TAG_VALUE: {
                parameterBuilder.setValue(readText(xpp));
                break;
            }
            case TAG_LABEL: {
                parameterBuilder.setLabel(readText(xpp));
                break;
            }
            case TAG_URI: {
                String uri = null;
                try {
                    uri = readText(xpp);
                    stack.peek().setUri(URI.create(uri));
                } catch (IllegalArgumentException e) {
                    log.error("Could not parse as uri: " + uri, e);
                }
                break;
            }
            default:
                log.trace("Unhandled start tag: " + xpp.getName());
                break;
        }
    }

    @Override
    public void handleEndTags(EntityBuilder<Available> builder, XmlPullParser xpp, Stack<QueryBuilder> stack) {
        switch (xpp.getName()) {
            case TAG_QUERY:
                getAvailableBuilder().getQueries().add(stack.pop().build());
                break;
            case TAG_PARAMETER:
                stack.peek().getParameters().add(parameterBuilder.build());
                break;
            default:
                log.trace("Unhandled end tag: " + xpp.getName());
                break;
        }
    }

    private AvailableBuilder getAvailableBuilder() {
        return (AvailableBuilder) getEntityBuilder();
    }

}
