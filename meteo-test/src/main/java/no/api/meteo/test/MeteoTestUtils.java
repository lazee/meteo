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

package no.api.meteo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class MeteoTestUtils {

    private MeteoTestUtils() {
        throw new UnsupportedOperationException();
    }

    public static InputStream getResource(String uri) {
        return MeteoTestUtils.class.getResourceAsStream(uri);
    }

    public static String getTextResource(String uri) throws MeteoTestException {
        StringBuilder stringBuilder = new StringBuilder();
        String str;
        try (InputStream inputStream = getResource(uri);
             InputStreamReader reader = new InputStreamReader(inputStream, "UTF8");
             BufferedReader in = new BufferedReader(reader)) {

            str = in.readLine();
            while (str != null) {
                stringBuilder.append(str).append("\n");
                str = in.readLine();
            }
        } catch (IOException e) {
            throw new MeteoTestException(e);
        }
        return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
    }

}
