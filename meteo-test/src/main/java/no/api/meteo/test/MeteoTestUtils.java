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

package no.api.meteo.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MeteoTestUtils {

    private static Logger log = LoggerFactory.getLogger(MeteoTestUtils.class);

    private MeteoTestUtils() {
        // Intentional
    }

    public static InputStream getResource(String uri) {
        return MeteoTestUtils.class.getResourceAsStream(uri);
    }

    public static String getTextResource(String uri) throws MeteoTestException {
        InputStream inputStream = getResource(uri);
        if (inputStream == null) {
            throw new MeteoTestException("Could not open stream to " + uri);
        }
        StringBuilder stringBuilder = new StringBuilder();
        String str;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
            while ((str = in.readLine()) != null) {
                stringBuilder.append(str + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            throw new MeteoTestException(e);
        } catch (IOException e) {
            throw new MeteoTestException(e);
        }
        return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
    }

}
