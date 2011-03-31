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

package no.api.meteo.yr;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import no.api.meteo.MeteoRuntimeException;

public class YRContent {

    private String xmlContent;

    private XMLSerializer xmlSerializer;

    public YRContent(String xmlContent) {
        this.xmlContent = xmlContent;
        xmlSerializer = new XMLSerializer();
    }

    public String toXML() {
        return xmlContent;
    }

    public String toJSON() {
        try {
            JSONObject json = (JSONObject) xmlSerializer.read(xmlContent);
            return json.toString(4, 0);
        } catch (JSONException e) {
            throw new MeteoRuntimeException("The XML data could not be converted into JSON by Meteo", e);
        }
    }
}
