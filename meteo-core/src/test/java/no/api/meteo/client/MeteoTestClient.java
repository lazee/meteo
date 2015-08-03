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

package no.api.meteo.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public final class MeteoTestClient implements MeteoClient {

    private final String content;

    public MeteoTestClient(String content) {
        this.content = content;
    }

    @Override
    public MeteoResponse fetchContent(URI uri) throws MeteoClientException {
        if (uri == null) {
            throw new MeteoClientException("url is null");
        } else if ("http://www.fo.no".equals(uri.toString())) {
            throw new MeteoClientException("url is empty", new IllegalArgumentException("foo"));
        }
        List<MeteoResponseHeader> list = new ArrayList<>();
        list.add(new MeteoResponseHeader("foo", "bar"));
        return new MeteoResponse(content, list, 200, "");
    }

    @Override
    public void setProxy(String hostName, int port) {
        // Not needed in this client
    }

    @Override
    public void setTimeout(int timeout) {
        // Not needed in this client
    }

    @Override
    public void shutdown() {
        // Not needed in this client
    }
}
