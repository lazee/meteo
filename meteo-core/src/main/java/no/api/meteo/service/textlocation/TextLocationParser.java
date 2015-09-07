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

package no.api.meteo.service.textlocation;

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.service.textlocation.TextLocationWeather;
import no.api.meteo.service.AbstractMetaMeteoDataParser;
import no.api.meteo.service.MeteoDataParser;
import no.api.meteo.service.textlocation.builder.TextLocationBuilder;
import no.api.meteo.service.textlocation.builder.TextLocationTimeBuilder;
import no.api.meteo.service.textlocation.builder.TextLocationWeatherBuilder;
import no.api.meteo.util.EntityBuilder;
import no.api.meteo.util.MeteoXppUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.Stack;

import static no.api.meteo.util.MeteoConstants.*;
import static no.api.meteo.util.MeteoXppUtils.getOffsetDateTime;
import static no.api.meteo.util.MeteoXppUtils.getString;
import static no.api.meteo.util.MeteoXppUtils.readText;

@Slf4j
public class TextLocationParser
        extends AbstractMetaMeteoDataParser<TextLocationWeather, TextLocationTimeBuilder>
        implements MeteoDataParser<TextLocationWeather> {

    private static final String ATTR_ISSUER = "issuer";
    private static final String TAG_FORECAST = "forecast";

    @Override
    public TextLocationWeather parse(String data) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(data), new TextLocationWeatherBuilder());
    }

    @Override
    public TextLocationWeather parse(InputStream inputStream) throws MeteoException {
        return doParse(MeteoXppUtils.createPullParser(inputStream), new TextLocationWeatherBuilder());
    }

    @Override
    public void handleStartTags(XmlPullParser xpp, Stack<TextLocationTimeBuilder> stack) throws MeteoException {
        switch (xpp.getName()) {
            case TAG_META:
                handleMetaTag(xpp);
                break;
            case TAG_PRODUCT_DESCRIPTION:
                getTextLocationWeatherBuilder().setProductDescription(getString(xpp, ATTR_PRODNAME));
                break;
            case TAG_TIME: {
                handleTimeTag(xpp, stack);
                break;
            }
            case TAG_LOCATION: {
                handleLocationTag(xpp, stack);
                break;
            }
            case TAG_HEADER: {
                stack.peek().getTextLocationBuilder().setHeader(readText(xpp));
                break;
            }
            case TAG_FORECAST: {
                stack.peek().getTextLocationBuilder().setForecast(readText(xpp));
            }
            default:
                log.trace(MSG_UNHANDLED_START_TAG, xpp.getName());
                break;
        }
    }

    @Override
    public void handleEndTags(EntityBuilder<TextLocationWeather> builder, XmlPullParser xpp,
                              Stack<TextLocationTimeBuilder> stack) {
        if (TAG_TIME.equals(xpp.getName())) {
            getTextLocationWeatherBuilder().getTimes().add(stack.pop().build());
        } else {
            log.trace(MSG_UNHANDLED_END_TAG, xpp.getName());
        }
    }

    private void handleTimeTag(XmlPullParser xpp, Stack<TextLocationTimeBuilder> stack) throws MeteoException {
        TextLocationTimeBuilder timeBuilder = new TextLocationTimeBuilder();
        timeBuilder.setFrom(getOffsetDateTime(xpp, ATTR_FROM));
        timeBuilder.setTo(getOffsetDateTime(xpp, ATTR_TO));
        stack.push(timeBuilder);
    }

    private void handleLocationTag(XmlPullParser xpp, Stack<TextLocationTimeBuilder> stack) {
        TextLocationBuilder builder = stack.peek().getTextLocationBuilder();
        builder.setName(getString(xpp, ATTR_NAME));
        builder.setId(getString(xpp, ATTR_ID));
        builder.setIssuer(getString(xpp, ATTR_ISSUER));
        builder.setType(getString(xpp, ATTR_TYPE));
    }


    private TextLocationWeatherBuilder getTextLocationWeatherBuilder() {
        return (TextLocationWeatherBuilder) getEntityBuilder();
    }


}
