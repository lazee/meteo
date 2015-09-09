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

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Default Meteo Client implementation.
 *
 * This implementation is using the PoolingHttpClientConnectionManager for managing connections against the MET API.
 * It should be thread safe, but don't take the word for it until you have tested it properly in your own environment.
 */
@Slf4j
public class DefaultMeteoClient implements MeteoClient {

    private final PoolingHttpClientConnectionManager connManager;

    private RequestConfig defaultRequestConfig;

    private int timeout = 2000;

    /**
     * Constructor for this Meteo Client implementation with max number of total connections set to 200
     */
    public DefaultMeteoClient() {
        this.connManager = new PoolingHttpClientConnectionManager();
        init(200);
    }

    /**
     * Constructor for this Meteo Client implementation asking for the maximum number of simultaneous connections
     * allowed.
     *
     * @param maxTotalConnections
     *         The number of simultaneous connections allowed.
     */
    public DefaultMeteoClient(int maxTotalConnections) {
        this.connManager = new PoolingHttpClientConnectionManager();
        init(maxTotalConnections);
    }

    @Override
    public MeteoResponse fetchContent(URI uri) throws MeteoClientException {
        log.debug("Going to fetch content from : %", uri.toString());
        CloseableHttpClient client = createClient();
        CloseableHttpResponse response = null;

        try {
            response = client.execute(prepareHttpGet(uri));
            validateResponse(response);
            return handleEntity(uri, response);
        } catch (IOException e) {
            throw new MeteoClientException("Got IOException while fetching content for: " + uri.toString(), e);
        } finally {
            closeResponse(response);
            closeClient(client);
        }
    }

    @Override
    public void setProxy(String hostname, int port) {
        defaultRequestConfig = RequestConfig
                .copy(defaultRequestConfig)
                .setProxy(new HttpHost(hostname, port))
                .build();
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void shutdown() {
        connManager.shutdown();
    }

    private void init(int maxTotalConnections) {
        connManager.setMaxTotal(maxTotalConnections);
        connManager.setDefaultMaxPerRoute(maxTotalConnections);
        defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setExpectContinueEnabled(false)
                .build();
    }


    private HttpGet prepareHttpGet(URI uri) {
        HttpGet get = new HttpGet(uri);
        get.setConfig(createRequestConfig());
        return get;
    }

    private void validateResponse(CloseableHttpResponse response) throws MeteoClientException {
        if (response.getStatusLine().getStatusCode() != 200 &&
                response.getStatusLine().getStatusCode() != 203) {
            throw new MeteoClientException(
                    "The request failed with error code " + response.getStatusLine().getStatusCode() + " : " +
                            response.getStatusLine().getReasonPhrase());
        }
    }

    private MeteoResponse handleEntity(URI uri, CloseableHttpResponse response)
            throws IOException, MeteoClientException {
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            return new MeteoResponse(EntityUtils.toString(entity),
                                     createMeteoResponseHeaders(response),
                                     response.getStatusLine().getStatusCode(),
                                     response.getStatusLine().getReasonPhrase());
        } else {
            throw new MeteoClientException("No content returned from response for: " + uri.toString());
        }
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.copy(defaultRequestConfig)
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();
    }

    private CloseableHttpClient createClient() {
        return HttpClients
                .custom()
                .setConnectionManager(connManager)
                .setConnectionManagerShared(true)
                .build();
    }

    private void closeClient(CloseableHttpClient client) {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            log.warn("Could not close http client. This might cause trouble further down.", e);
        }
    }

    private void closeResponse(CloseableHttpResponse response) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            log.warn("Could not close http response. This might cause trouble further down.", e);
        }
    }

    private List<MeteoResponseHeader> createMeteoResponseHeaders(HttpResponse response) {
        List<MeteoResponseHeader> headers = new ArrayList<>();
        for (Header header : response.getAllHeaders()) {
            log.debug("Adding response header : " + header.toString());
            headers.add(new MeteoResponseHeader(header.getName(), header.getValue()));
        }
        return headers;
    }

}
