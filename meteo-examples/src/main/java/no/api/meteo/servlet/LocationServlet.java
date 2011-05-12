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

package no.api.meteo.servlet;

import no.api.meteo.MeteoException;
import no.api.meteo.client.DefaultMeteoClient;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.services.LocationForecastHelper;
import no.api.meteo.services.entity.MeteoExtrasForecast;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * This is a demo servlet that we use to test weather data.
 *
 * It should not be looked as a best practice implementation of the weather API. 
 */
public class LocationServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LocationServlet.class);

    private static final MeteoClient METEO_CLIENT = new DefaultMeteoClient();

    public static final int HOURS_24 = 24;

    public static final int TWELVE_O_CLOCK = 12;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("issearch") != null && req.getParameter("issearch").equals("true")) {
            req.setAttribute("search", true);
            req.setAttribute("longitude", req.getParameter("longitude"));
            req.setAttribute("latitude", req.getParameter("latitude"));
            req.setAttribute("altitude", req.getParameter("altitude"));

            double longitude = Double.valueOf(req.getParameter("longitude"));
            double latitude = Double.valueOf(req.getParameter("latitude"));
            int altitude = Integer.valueOf(req.getParameter("altitude"));

            try {
                LocationForecastHelper
                        helper = LocationForecastHelper.createInstance(METEO_CLIENT, longitude, latitude, altitude);
                helper.fetch();
                
                req.setAttribute("data", helper.getMeteoData().getResult());
                req.setAttribute("raw", helper.getMeteoData().getRawResult());
                List<MeteoExtrasForecast> last24 = helper.getPointForecastsByHour(HOURS_24);
                req.setAttribute("last24", last24);
                DateTime firstDate = new DateTime();
                firstDate = firstDate.withHourOfDay(TWELVE_O_CLOCK).withMinuteOfHour(0).withSecondOfMinute(0);

                MeteoExtrasForecast todayForecast = helper.getNearestForecast(firstDate.toDate());
                req.setAttribute("today", todayForecast);

                MeteoExtrasForecast tomorrowForecast = helper.getNearestForecast(firstDate.plusDays(1).toDate());
                req.setAttribute("tomorrow", tomorrowForecast);

                MeteoExtrasForecast afterForecast = helper.getNearestForecast(firstDate.plusDays(2).toDate());
                req.setAttribute("thedayaftertomorrow", afterForecast);
                
            } catch (MeteoException e) {
                log.error("Caught exception", e);
            }

        } else {
            req.setAttribute("search", false);
        }

        getServletContext().getRequestDispatcher("/WEB-INF/jsp/location.jsp").forward(req, resp);
    }


}
