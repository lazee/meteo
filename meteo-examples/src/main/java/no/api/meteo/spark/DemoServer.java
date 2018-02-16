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
package no.api.meteo.spark;

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import no.api.meteo.client.DefaultMeteoClient;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoData;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecast;
import no.api.meteo.service.locationforecast.LocationforecastLTSService;
import no.api.meteo.service.locationforecast.extras.LocationForecastHelper;
import spark.ModelAndView;
import spark.Request;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

@Slf4j
public class DemoServer {

    private static final MeteoClient METEO_CLIENT = new DefaultMeteoClient("MyExampleApp");

    public static void main(String[] args) {
        staticFileLocation("/public");
        port(9090);

        get("/", (req, rest) -> {
            Map<String, Object> attributes = new HashMap<>();
            return new ModelAndView(attributes, "index");
        }, new FreeMarkerTemplateEngine());

        get("/location", (req, rest) -> {
            Map<String, Object> attributes = new HashMap<>();
            if (req.queryParams("issearch") != null && req.queryParams("issearch").equals("true")) {
                prepareLocationResult(req, attributes);
            } else {
                attributes.put("search", false);
            }
            return new ModelAndView(attributes, "location");
        }, new FreeMarkerTemplateEngine());

    }

    private static void prepareLocationResult(Request req, Map<String, Object> attributes) {
        attributes.put("search", true);
        String p = req.queryParams("p");
        attributes.put("p", p == null ? "0" : p);
        attributes.put("longitude", req.queryParams("longitude"));
        attributes.put("latitude", req.queryParams("latitude"));
        attributes.put("altitude", req.queryParams("altitude"));

        try {
            MeteoData<LocationForecast> meteoData = fetchLocationForecast(req);
            attributes.put("data", meteoData.getResult());
            attributes.put("raw", meteoData.getResponse().getData());

            LocationForecastHelper helper = new LocationForecastHelper(meteoData.getResult());
            attributes.put("last24", helper.findHourlyPointForecastsFromNow(24));

            ZonedDateTime firstDate = (ZonedDateTime.now(ZoneId.of("Z")).withHour(12).withMinute(0).withSecond(0));
            Optional<MeteoExtrasForecast> todayForecast = helper.findNearestForecast(firstDate);
            if (todayForecast.isPresent()) {
                attributes.put("today", todayForecast.get());
            }

            Optional<MeteoExtrasForecast> tomorrowForecast = helper.findNearestForecast(firstDate.plusDays(1));
            if (tomorrowForecast.isPresent()) {
                attributes.put("tomorrow", tomorrowForecast.get());
            }

            Optional<MeteoExtrasForecast> afterForecast = helper.findNearestForecast(firstDate.plusDays(2));
            if (afterForecast.isPresent()) {
                attributes.put("thedayaftertomorrow", afterForecast.get());
            }
        } catch (MeteoException e) {
            log.error("Caught exception", e);
        }
    }

    private static MeteoData<LocationForecast> fetchLocationForecast(Request req) throws MeteoException {
        LocationforecastLTSService service = new LocationforecastLTSService(METEO_CLIENT);
        return service.fetchContent(Double.valueOf(req.queryParams("longitude")),
                                     Double.valueOf(req.queryParams("latitude")),
                                     Integer.valueOf(req.queryParams("altitude")));

    }
}
