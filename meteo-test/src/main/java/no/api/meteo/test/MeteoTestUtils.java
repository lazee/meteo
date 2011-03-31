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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MeteoTestUtils {

    private static Logger log = LoggerFactory.getLogger(MeteoTestUtils.class);
    
    private MeteoTestUtils() {
        // Intentional
    }

    public static void printResource(OutputStream os, String uri) throws MeteoTestException {
        InputStream is = getResource(uri);
        if (is == null) {
            throw new MeteoTestException("Could not find resource : " + uri);
        }

        if (os == null) {
            throw new MeteoTestException("OutputStream is null");
        }

        try {
            int bufferSize = 4096;
            byte[] buf = new byte[bufferSize];
            int bytesRead;
            try {
                while ((bytesRead = is.read(buf)) != -1) {
                    os.write(buf, 0, bytesRead);
                }
            } finally {
                is.close();
                os.close();
            }
        } catch (IOException e) {
            log.error("Got error. Wrapped exception: " + e.getMessage());
            throw new MeteoTestException(e);
        }
    }

    public static InputStream getResource(String uri) {
        return MeteoTestUtils.class.getResourceAsStream(uri);
    }

    public static String getTextResource(String uri) throws MeteoTestException {
        OutputStream output = new OutputStream() {
            private StringBuilder string = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b);
            }

            public String toString() {
                return this.string.toString();
            }
        };
        printResource(output, uri);
        return output.toString();
    }

}
