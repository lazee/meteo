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
import no.api.meteo.entity.core.Model;
import no.api.meteo.util.MetaEntityBuilder;
import no.api.meteo.util.MeteoNetUtils;
import org.xmlpull.v1.XmlPullParser;

import java.time.ZonedDateTime;

import static no.api.meteo.util.MeteoConstants.ATTR_CREATED;
import static no.api.meteo.util.MeteoConstants.ATTR_FROM;
import static no.api.meteo.util.MeteoConstants.ATTR_LICENSEURL;
import static no.api.meteo.util.MeteoConstants.ATTR_NAME;
import static no.api.meteo.util.MeteoConstants.ATTR_NEXTRUN;
import static no.api.meteo.util.MeteoConstants.ATTR_RUNENDED;
import static no.api.meteo.util.MeteoConstants.ATTR_TERMIN;
import static no.api.meteo.util.MeteoConstants.ATTR_TO;
import static no.api.meteo.util.MeteoXppUtils.getString;
import static no.api.meteo.util.MeteoXppUtils.getZonedDateTime;

@Slf4j
public abstract class AbstractMetaMeteoDataParser<E, F> extends AbstractMeteoDataParser<E, F> {

    public E doParse(XmlPullParser xpp, MetaEntityBuilder<E> entityBuilder) throws MeteoException {
        return super.doParse(xpp, entityBuilder);
    }

    public MetaEntityBuilder<E> getEntityBuilder() {
        return (MetaEntityBuilder<E>) super.getEntityBuilder();
    }

    public void handleMetaTag(XmlPullParser xpp) {
        try {
            String uriStr = getString(xpp, ATTR_LICENSEURL);
            if (uriStr != null) {
                getEntityBuilder().getMetaBuilder().setLicenseUri(MeteoNetUtils.createUri(uriStr));
            }
            ZonedDateTime zoneDateTime = getZonedDateTime(xpp, ATTR_CREATED);
            if (zoneDateTime != null) {
                getEntityBuilder().setCreated(zoneDateTime);
            }
        } catch (MeteoException e) {
            log.warn("Meta information not found in xml data");
        }
    }

    public void handleModelTag(XmlPullParser xpp) {
        try {
            Model model = new Model(getZonedDateTime(xpp, ATTR_TO),
                                    getZonedDateTime(xpp, ATTR_FROM),
                                    getZonedDateTime(xpp, ATTR_RUNENDED),
                                    getZonedDateTime(xpp, ATTR_NEXTRUN),
                                    getZonedDateTime(xpp, ATTR_TERMIN),
                                    getString(xpp, ATTR_NAME));
            getEntityBuilder().getMetaBuilder().getModels().add(model);
        } catch (MeteoException e) {
            log.warn("Could not convert model dates found in returned xml", e);
        }
    }

}
