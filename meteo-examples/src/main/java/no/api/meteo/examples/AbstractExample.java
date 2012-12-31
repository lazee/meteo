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

package no.api.meteo.examples;

import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.Precipitation;
import no.api.meteo.util.MeteoDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public abstract class AbstractExample {

    private static final Logger log = LoggerFactory.getLogger(AbstractExample.class);

    public void configureLog(String level) {
        // TODO Presently deactivated as we no longer use log4j
        //PropertyConfigurator.configure(createLogProperties(level));
    }

    private Properties createLogProperties(String level) {
        Properties props = new Properties();
        props.setProperty("log4j.rootLogger", level.toUpperCase() + ", A1");
        props.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
        props.setProperty("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
        props.setProperty("log4j.appender.A1.layout.ConversionPattern", "%m%n");
        //props.setProperty("log4j.appender.A1.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n");

        return props;
    }

    public void prettyLogPeriodForecast(PeriodForecast periodForecast) {
        if (periodForecast == null) {
            log.error("Period forecast -> null");
        } else {
            log.error("Period forecast -> from:" +
                    MeteoDateUtils.dateToString(periodForecast.getFromTime(), "yyyy-MM-dd HH:mm") + ", to:" +
                    MeteoDateUtils.dateToString(periodForecast.getToTime(), "yyyy-MM-dd HH:mm"));
            prettyLogPrecipitation(periodForecast.getPrecipitation());
        }
    }

    public void prettyLogPrecipitation(Precipitation precipitation) {
        if (precipitation == null) {
            log.error("  Precipitation -> null");
        } else {
            log.error("  Precipitation -> unit:" + precipitation.getUnit() +
                    ", maxValue:" + precipitation.getMaxValue() + ", minValue:" + precipitation.getMinValue() +
                    ", value:" + precipitation.getValue() + ", probability:" + precipitation.getProbability());
        }
    }

}
