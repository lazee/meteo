
# METEO

DEPRECATED: This client is not maintained anymore.

Meteo is an Open-Source Java based library/client for handling weather data from http://api.met.no.

It mainly consists of two parts: A Java client that parses data from the MET api into java objects and helper tools to help you
pick the right kind of data for different scenarios.

Please note that Meteo is in no way affiliated with the Norwegian Meteorological Institute
except for the fact that it use their API to provide you with weather data.

Meteo is developed and maintained by Amedia Utvikling AS. It is in use within all the Amedia sites (more than 70).
It mainly supports the services that Amedia needs at any given time. But we will consider adding other services as
well upon request (or send a pull request).

## The MET terms of use

Make sure to read the [Conditions for use of the service](http://api.met.no/conditions_service.html) from
the Norwegian Meteorological Institute.

Also make sure to read this http://api.met.no/license_data.html.

## Reverse proxying

As MET states in their terms of use, you must respect their cache headers. Meaning that you should only fetch one
resource one time within the TTL set by MET in each response.

The easiest way to achieve this is by installing a [Reverse Proxy](https://en.wikipedia.org/wiki/Reverse_proxy)
like [Squid](http://wiki.squid-cache.org/SquidFaq/ReverseProxy). An alternative solution is to implement your own
[MeteoClient](https://github.com/amedia/meteo/blob/master/meteo-core/src/main/java/no/api/meteo/client/MeteoClient.java)
that respects these requirements.

The [default implementation](https://github.com/amedia/meteo/blob/master/meteo-client/src/main/java/no/api/meteo/client/DefaultMeteoClient.java)
of MeteoClient does not have any caching what so ever, but it does support configuring a proxy.

## The core library and client

The core library in Meteo is all about offering access to the raw MET data through a Java API.
As stated earlier we have only implemented support for the services that Amedia needs or that is requested/implemented
by other users (Please send pull requests).

### Requirements

Meteo requires Java 8.x or later.

### Installation

You will find Meteo in the central maven repository: http://search.maven.org/#search|ga|1|a%3A%22meteo%22

For Maven you would add this to your pom:

~~~ java
<dependency>
    <groupId>no.api.meteo</groupId>
    <artifactId>meteo-core</artifactId>
    <version>3.0.0</version>
</dependency>
~~~

If you want to use the default implementation of a MeteoClient, then you would also have to add this:

~~~ xml
<dependency>
    <groupId>no.api.meteo</groupId>
    <artifactId>meteo-client</artifactId>
    <version>3.0.0</version>
</dependency>
~~~

### Usage

In the [meteo-examples](https://github.com/amedia/meteo/tree/master/meteo-examples/src/main/java/no/api/meteo/examples)
 module you will find several useful examples that should get you up and running

#### Creating a default client

If you feel fine with the default MeteoClient shipped with Meteo, then create a client like this:

~~~ java
MeteoClient meteoClient = new DefaultMeteoClient();
~~~

This client allows you to configure a proxy and connection timeout if needed:

~~~ java
meteoClient.setProxy("myproxy.host", 9000);
meteoClient.setTimeout(1000);
~~~

You can also create you own client easily by implementing the MeteoClient interface in your own class.

### Services

The Meteo core library is organized in a set of services where each service represents on of the services in the MET API.
Each service will need an Meteo client to be able to fetch data.

Current services:

* Location forecast
* Sunrise
* Text forecast (Experimental)
* Text locations (Experimental)

#### Location forecast service

This is the service for fetching raw data from http://api.met.no/weatherapi/locationforecastlts/1.2/documentation.

~~~ java
LocationforecastLTSService service = new LocationforecastLTSService(meteoClient);
try {
    MeteoData<LocationForecast> data = service.fetchContent(longitude, latitude, altitude);
} catch (MeteoException e) {
    // Handle exception.
}
~~~

#### Sunrise service

This is the service for fetching raw data from http://api.met.no/weatherapi/sunrise/1.0/documentation.

~~~ java
SunriseService service = new SunriseService(meteoClient);
try {
    MeteoData<Sunrise> data = service.fetchContent(longitude, latitude, date);
} catch (MeteoException e) {
    // Handle exception.
}
~~~

## General developer notes

Meteo uses checked exceptions at the moment. This will change in the 3.1.x versions.
