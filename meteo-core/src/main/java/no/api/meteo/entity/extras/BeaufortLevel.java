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

package no.api.meteo.entity.extras;

/**
 * Enum representation of the Beaufort scale.
 *
 * Each enum type represents one level/step/class/unit in this scale.
 *
 * <p>Resource: http://en.wikipedia.org/wiki/Beaufort_scale</p>
 *
 * <p>As for now this implementation has english and norwegian (bokmaal) translations of the name on each level</p>
 */
public enum BeaufortLevel {

    /**
     * <p>Id: 0</p>
     *
     * <p>Sea: Flat</p>
     *
     * <p>Land: Calm. Smoke rises vertically.</p>
     */
    CALM(0, "Calm", "Stille",
            new BeaufortRange(0.0, 0.2),
            new BeaufortRange(0, 1),
            new BeaufortRange(0, 1)),

    /**
     * <p>Id: 1</p>
     *
     * <p>Sea: Ripples without crests.</p>
     *
     * <p>Land: Smoke drift indicates wind direction and wind vanes cease moving.</p>
     */
    LIGHT_AIR(1, "Light air", "Flau vind",
            new BeaufortRange(0.3, 1.5),
            new BeaufortRange(1, 5),
            new BeaufortRange(1, 3)),

    /**
     * <p>Id: 2</p>
     *
     * <p>Sea: Small wavelets. Crests of glassy appearance, not breaking.</p>
     *
     * <p>Land: Wind felt on exposed skin. Leaves rustle and wind vanes begin to move.</p>
     */
    LIGHT_BREEZE(2, "Light breeze", "Svak vind",
            new BeaufortRange(1.6, 3.3),
            new BeaufortRange(6, 11),
            new BeaufortRange(4, 6)),

    /**
     * <p>Id: 3</p>
     *
     * <p>Sea: Large wavelets. Crests begin to break; scattered whitecaps</p>
     *
     * <p>Land: Leaves and small twigs constantly moving, light flags extended</p>
     */
    GENTLE_BREEZE(3, "Gentle breeze", "Lett bris",
            new BeaufortRange(3.4, 5.4),
            new BeaufortRange(12, 19),
            new BeaufortRange(7, 10)),


    /**
     * <p>Id: 4</p>
     *
     * <p>Sea: Small waves with breaking crests. Fairly frequent whitecaps.</p>
     *
     * <p>Land: Dust and loose paper raised. Small branches begin to move.</p>
     */
    MODERATE_BREEZE(4, "Moderate breeze", "Laber bris",
            new BeaufortRange(5.5, 7.9),
            new BeaufortRange(20, 28),
            new BeaufortRange(11, 16)),


    /**
     * <p>Id: 5</p>
     *
     * <p>Sea: Moderate waves of some length. Many whitecaps. Small amounts of spray.</p>
     *
     * <p>Land: Branches of a moderate size move. Small trees in leaf begin to sway.</p>
     */
    FRESH_BREEZE(5, "Fresh breeze", "Frisk bris",
            new BeaufortRange(8.0, 10.7),
            new BeaufortRange(29, 38),
            new BeaufortRange(17, 21)),

    /**
     * <p>Id: 6</p>
     *
     * <p>Sea: Long waves begin to form. White foam crests are very frequent. Some airborne spray is present.</p>
     *
     * <p>Land: Large branches in motion. Whistling heard in overhead wires. Umbrella use becomes difficult. Empty
     * plastic garbage cans tip over.</p>
     */
    STRONG_BREEZE(6, "Strong breeze", "Liten kuling",
            new BeaufortRange(10.8, 13.8),
            new BeaufortRange(39, 49),
            new BeaufortRange(22, 27)),

    /**
     * <p>Id: 7</p>
     *
     * <p>Sea: Sea heaps up. Some foam from breaking waves is blown into streaks along wind direction. Moderate amounts
     * of airborne spray.</p>
     *
     * <p>Land: Whole trees in motion. Effort needed to walk against the wind.</p>
     */
    NEAR_GALE(7, "High wind, Moderate gale, Near gale", "Stiv kuling",
            new BeaufortRange(13.9, 17.1),
            new BeaufortRange(50, 61),
            new BeaufortRange(28, 33)),

    /**
     * <p>Id: 8</p>
     *
     * <p>Sea: Moderately high waves with breaking crests forming spindrift. Well-marked streaks of foam are blown along
     * wind direction. Considerable airborne spray.</p>
     *
     * <p>Land: Some twigs broken from trees. Cars veer on road. Progress on foot is seriously impeded.</p>
     */
    FRESH_GALE(8, "Gale, Fresh gale", "Sterk kuling",
            new BeaufortRange(17.2, 20.7),
            new BeaufortRange(62, 74),
            new BeaufortRange(34, 40)),

    /**
     * <p>Id: 9</p>
     *
     * <p>Sea: High waves whose crests sometimes roll over. Dense foam is blown along wind direction. Large amounts of
     * airborne spray may begin to reduce visibility.</p>
     *
     * <p>Land: Some branches break off trees, and some small trees blow over. Construction/temporary signs and
     * barricades blow over.</p>
     */
    STRONG_GALE(9, "Strong gale", "Liten storm",
            new BeaufortRange(20.8, 24.4),
            new BeaufortRange(75, 88),
            new BeaufortRange(41, 47)),

    /**
     * <p>Id: 10</p>
     *
     * <p>Sea: Very high waves with overhanging crests. Large patches of foam from wave crests give the sea a white
     * appearance. Considerable tumbling of waves with heavy impact. Large amounts of airborne spray reduce
     * visibility.</p>
     *
     * <p>Land: Trees are broken off or uprooted, saplings bent and deformed. Poorly attached asphalt shingles and
     * shingles in poor condition peel off roofs.</p>
     */
    STORM(10, "Storm, Whole gale", "Full storm",
            new BeaufortRange(24.5, 28.4),
            new BeaufortRange(89, 102),
            new BeaufortRange(48, 55)),

    /**
     * <p>Id: 11</p>
     *
     * <p>Sea: Exceptionally high waves. Very large patches of foam, driven before the wind, cover much of the sea
     * surface. Very large amounts of airborne spray severely reduce visibility.</p>
     *
     * <p>Land: Widespread damage to vegetation. Many roofing surfaces are damaged; asphalt tiles that have curled p
     * and/or fractured due to age may break away completely.</p>
     */
    VIOLENT_STORM(11, "Violent storm", "Sterk storm",
            new BeaufortRange(28.5, 32.6),
            new BeaufortRange(103, 117),
            new BeaufortRange(56, 63)),

    /**
     * <p>Id: 12</p>
     *
     * <p>Sea: Huge waves. Sea is completely white with foam and spray. Air is filled with driving spray, greatly
     * reducing visibility.</p>
     *
     * <p>Land: Very widespread damage to vegetation. Some windows may break; mobile homes and poorly constructed sheds
     * and barns are damaged. Debris may be hurled about.</p>
     */
    HURRICANE_FORCE(12, "Hurrican-force", "Orkan",
            new BeaufortRange(32.7, 36.9),
            new BeaufortRange(118, Double.MAX_VALUE),
            new BeaufortRange(64, Double.MAX_VALUE));


    private final int id;

    private final String nameEN;

    private final String nameNO;

    private final BeaufortRange ms;

    private final BeaufortRange kmh;

    private final BeaufortRange kn;

    BeaufortLevel(int id, String nameEN, String nameNO, BeaufortRange ms, BeaufortRange kmh, BeaufortRange kn) {
        this.nameEN = nameEN;
        this.nameNO = nameNO;
        this.id = id;
        this.ms = ms;
        this.kmh = kmh;
        this.kn = kn;
    }

    /**
     * Get the unit range in kilometers per hour.
     *
     * @return Range object containing the start and end values for the unit.
     */
    public BeaufortRange getKmhRange() {
        return kmh;
    }

    /**
     * Get the unit range in knots.
     *
     * @return Range object containing the start and end values for the unit.
     */
    public BeaufortRange getKnRange() {
        return kn;
    }

    /**
     * Get the unit range in meters per second.
     *
     * @return Range object containing the start and end values for the unit.
     */
    public BeaufortRange getMsRange() {
        return ms;
    }

    /**
     * Get he name of the unit.
     *
     * @return The name of the unit.
     */
    public String getNameEN() {
        return nameEN;
    }

    /**
     * Get the norwegian (bokmaal) translation of the unit name.
     *
     * @return The name of the unit.
     */
    public String getNameNO() {
        return nameNO;
    }

    /**
     * Get the beaufort id.
     *
     * @return Number representing the id on the Beaufort scale.
     */
    public int getId() {
        return id;
    }

    /**
     * Find matching unit for a given number.
     *
     * @param value Number representing wind speed in meters per second.
     * @return A matching unit.
     */
    public static BeaufortLevel findUnitByMetersPerSecond(double value) {
        for (BeaufortLevel beaufortLevel : BeaufortLevel.values()) {
            if (value >= beaufortLevel.getMsRange().getStart() && value <= beaufortLevel.getMsRange().getStop()) {
                return beaufortLevel;
            }
        }
        return getLevelOutsideRange(value, BeaufortLevel.CALM.getMsRange().getStart());
    }

    /**
     * Find matching unit for a given number.
     *
     * @param value Number representing wind speed in knots.
     * @return A matching unit.
     */
    public static BeaufortLevel findUnitByKnots(double value) {
        for (BeaufortLevel beaufortLevel : BeaufortLevel.values()) {
            if (value >= beaufortLevel.getKnRange().getStart() && value <= beaufortLevel.getKnRange().getStop()) {
                return beaufortLevel;
            }
        }
        return getLevelOutsideRange(value, BeaufortLevel.CALM.getKnRange().getStart());
    }

    /**
     * Find matching unit for a given number.
     *
     * @param value Number representing wind speed in kilometers per hour.
     * @return A matching unit. If a given value is negative, then {@link BeaufortLevel#CALM} is returned. If the value
     *         exceeds the Beaufort scale, then {@link BeaufortLevel#HURRICANE_FORCE} is returned.
     */
    public static BeaufortLevel findUnitByKilometersPerHour(double value) {
        for (BeaufortLevel beaufortLevel : BeaufortLevel.values()) {
            if (value >= beaufortLevel.getKmhRange().getStart() && value <= beaufortLevel.getKmhRange().getStop()) {
                return beaufortLevel;
            }
        }
        return getLevelOutsideRange(value, BeaufortLevel.CALM.getKmhRange().getStart());
    }

    /**
     * Find matching unit for a given number.
     *
     * @param value Number representing the Beaufort id.
     * @return A matching unit. If a given value is negative, then {@link BeaufortLevel#CALM} is returned. If the value
     *         exceeds the Beaufort scale, then {@link BeaufortLevel#HURRICANE_FORCE} is returned.
     */
    public static BeaufortLevel findUnitById(int value) {
        for (BeaufortLevel beaufortLevel : BeaufortLevel.values()) {
            if (value == beaufortLevel.getId()) {
                return beaufortLevel;
            }
        }
        if (value < 0) {
            return BeaufortLevel.CALM;
        } else {
            return BeaufortLevel.HURRICANE_FORCE;
        }
    }

    private static BeaufortLevel getLevelOutsideRange(double value, double startValue) {
        if (value < startValue) {
            return BeaufortLevel.CALM;
        } else {
            return BeaufortLevel.HURRICANE_FORCE;
        }
    }
}
