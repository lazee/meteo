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

package no.api.meteo.service;

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import no.api.meteo.util.EntityBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Stack;

@Slf4j
public abstract class AbstractMeteoDataParser<E, F> {

    private EntityBuilder<E> entityBuilder;

    public E doParse(XmlPullParser xpp, EntityBuilder<E> entityBuilder) throws MeteoException {
        this.entityBuilder = entityBuilder;
        try {
            Stack<F> stack = new Stack<>();
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    handleStartTags(xpp, stack);
                } else if (eventType == XmlPullParser.END_TAG) {
                    handleEndTags(entityBuilder, xpp, stack);
                }
                eventType = xpp.next();
            }
            return entityBuilder.build();
        } catch (XmlPullParserException e) {
            throw new MeteoDataParserException("A parsing problem occurred", e);
        } catch (IOException e) {
            throw new MeteoDataParserException("An IO problem occurred", e);
        }
    }

    public EntityBuilder<E> getEntityBuilder() {
        return entityBuilder;
    }

    public abstract void handleStartTags(XmlPullParser xpp, Stack<F> stack);

    public abstract void handleEndTags(EntityBuilder<E> builder, XmlPullParser xpp, Stack<F> stack);
}
