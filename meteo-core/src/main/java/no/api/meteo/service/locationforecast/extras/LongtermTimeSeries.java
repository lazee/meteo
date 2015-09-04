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

package no.api.meteo.service.locationforecast.extras;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class LongtermTimeSeries {

    @Getter
    private final List<LongtermTimeSerie> series;

    public LongtermTimeSeries() {
        series = new ArrayList<>();

        LongtermTimeSerie closeSerie = new LongtermTimeSerie().add(-2, 4).add(4, 10).add(10, 16).add(16, 22);
        LongtermTimeSerie distantSerie = new LongtermTimeSerie().add(0, 6).add(6, 12).add(12, 18).add(18, 24);

        for (int i = 0; i < 9; i++) {
            if (i < 2) {
                series.add(closeSerie);
            } else {
                series.add(distantSerie);
            }
        }
    }

    public class LongtermTimeSerie {

        private final List<FromTo> fromTos;

        private LongtermTimeSerie() {
            this.fromTos = new ArrayList<>();
        }

        public LongtermTimeSerie add(int from, int to) {
            fromTos.add(new FromTo(from, to));
            return this;
        }

        public List<FromTo> getFromTos() {
            return fromTos;
        }
    }

    public class FromTo {

        private final int from;

        private final int to;

        public FromTo(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }
    }

}
