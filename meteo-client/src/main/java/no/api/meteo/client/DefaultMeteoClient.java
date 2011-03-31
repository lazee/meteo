package no.api.meteo.client;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DefaultMeteoClient implements MeteoClient {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private DefaultHttpClient httpClient;

    public DefaultMeteoClient() {
        httpClient = new DefaultHttpClient();
    }

    @Override
    public void setProxy(String hostname, int port) {
        HttpHost proxy = new HttpHost(hostname, port);
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    @Override
    public void shutdown() {
        httpClient.getConnectionManager().shutdown();
    }

    @Override
    public MeteoResponse fetchContent(URL url) throws MeteoClientException {
        try {
            log.debug("Going to fetch: " + url.toString());
            HttpGet httpget = new HttpGet(url.toURI());

            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 1024000) {
                    return new MeteoResponse(EntityUtils.toString(entity), createMeteoResponseHeaders(response));
                } else {
                    throw new MeteoClientException("The returned content exceeds the data limit of 1024000 bytes.");
                }
            } else {
                throw new MeteoClientException("No content returned from request: " + url.toString());
            }
        } catch (URISyntaxException e) {
            throw new MeteoClientException("Caught exception while fetching content", e);
        } catch (ClientProtocolException e) {
            throw new MeteoClientException("Caught exception while fetching content", e);
        } catch (IOException e) {
            throw new MeteoClientException("Caught exception while fetching content", e);
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
        }
    }

    private List<MeteoResponseHeader> createMeteoResponseHeaders(HttpResponse response) {
        List<MeteoResponseHeader> headers = new ArrayList<MeteoResponseHeader>();
        for (Header header : response.getAllHeaders()) {
            log.debug("Adding header : " + header.toString());

            headers.add(new MeteoResponseHeader(header.getName(), header.getValue()));
        }
        return headers;
    }


}
