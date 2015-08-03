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

import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Value
public class MeteoResponse {

    private final String data;

    private final List<MeteoResponseHeader> responseHeaders;

    private final int status;

    private final String statusPhrase;

    public List<MeteoResponseHeader> getResponseHeaders() {
        return responseHeaders == null
                ? new ArrayList<>()
                : Collections.unmodifiableList(responseHeaders);
    }

    /**
     * Tells whether the MET API version used in the request is deprecated or not.
     *
     * An API version is by design deprecated if the response status code is 203 instead of the default 200.
     * In these cases you need to go to http://api.met.no to check when and how you can start using the new
     * version of the API. Also check if there is a newer version of Meteo available that fixes your problem.
     *
     * From the MET documentation on http://api.met.no:
     *
     * Since a product can change its API, there is a version number as part of every product URL. If you try to
     * use a product version which is deprecated, you will get the data you expect, but with the HTTP status
     * code 203 Non-Authoritative Information. It is important for client software to accept 203-responses, but
     * you should implement appropriate checks or alarms on the return codes.
     *
     * @return <code>true</code> if the MET API version is deprecated, else <code>false</code>
     */
    public boolean isDeprecated() {
        return status == 203;
    }
}
