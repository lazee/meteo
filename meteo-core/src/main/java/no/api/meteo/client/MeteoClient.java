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

package no.api.meteo.client;

import java.net.URL;

public interface MeteoClient {

    /**
     * @param url The url pointing to the location where the content should be fetched from.
     * @return Response object containing the fetched content.
     * @throws MeteoClientException If invalid url or content couldn't be fetched.
     */
    public abstract MeteoResponse fetchContent(URL url) throws MeteoClientException;

    /**
     * Set proxy settings for the client.
     * 
     * @param hostName The proxy hostname to be used.
     * @param port The proxy port to be used.
     */
    public abstract void setProxy(String hostName, int port);

    /**
     * When HttpClient instance is no longer needed, shut down the client to ensure immediate deallocation
     * of all system resources.
     */
    public abstract void shutdown();
}
