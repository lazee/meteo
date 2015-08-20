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

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import no.api.freemarker.java8.Java8ObjectWrapper;
import spark.ModelAndView;
import spark.TemplateEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.time.ZoneId;
import java.util.TimeZone;

@Slf4j
public class FreeMarkerTemplateEngine extends TemplateEngine {

    private Configuration configuration;

    public FreeMarkerTemplateEngine() {
        this.configuration = createFreeMarkerConfiguration();
    }

    @Override
    public String render(ModelAndView modelAndView) {
        try {
            StringWriter stringWriter = new StringWriter();
            Template template = configuration.getTemplate(modelAndView.getViewName() + ".ftl");
            template.process(modelAndView.getModel(), stringWriter);
            stringWriter.flush();
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Configuration createFreeMarkerConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Oslo")));
        configuration.setClassForTemplateLoading(FreeMarkerTemplateEngine.class, "/");
        configuration.setObjectWrapper(new Java8ObjectWrapper(Configuration.VERSION_2_3_23));
        return configuration;
    }
}
