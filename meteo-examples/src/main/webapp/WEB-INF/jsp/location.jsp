<!DOCTYPE html>
<html>
<%@ page contentType="text/html" session="false" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>LocationLTS demo</title>
    <link rel="stylesheet" type="text/css" href="/style.css"/>
</head>
<body>
<h1>
    <table>
        <tr>
            <td><img src="<c:url value="/images/weather/1.png"/>"/></td>
            <td>LocationLTS test servlet</td>
        </tr>
    </table>
</h1>

<div id="searchform">
    <form action="" method="get">
        <input type="hidden" name="issearch" value="true"/>
        Longitude : <input name="longitude" type="dec" pattern="^\d*\.\d*$" size="20" value="${longitude}"/>
        Latitude : <input name="latitude" type="dec" pattern="^\d*\.\d*$" size="20" value="${latitude}"/>
        Altitude : <input name="altitude" type="dec" pattern="^\d*(\.\d*)?$" size="20" value="${altitude}"
                          title="Must be a number dude!" on/>
        <input type="submit" value="search"/>

        <p>
            Predefined searches:
        </p>

        <p>
            <a href="location.html?issearch=true&longitude=10.7460923576733&latitude=59.912726542422&altitude=14&p=1">Oslo</a>
            <br/>
            <a href="location.html?issearch=true&longitude=10.8039&latitude=59.8106&altitude=105&p=2">Kolbotn</a>
            <br/>
            <a href="location.html?issearch=true&longitude=13.41053&latitude=52.52437&altitude=74&p=3">Berlin</a>
            <br/>
            <a href="location.html?issearch=true&longitude=10.6915510490055&latitude=60.7957437316181&altitude=129&p=4">Gj&oslash;vik</a>
        </p>
    </form>
    <c:if test="${search}">
        <c:choose>
            <c:when test="${param.p == 1}">
                <p><img src="http://www.yr.no/sted/Norge/Oslo/Oslo/Oslo/meteogram.png"/></p>
            </c:when>
            <c:when test="${param.p == 2}">
                <p><img src="http://www.yr.no/sted/Norge/Akershus/Oppeg%C3%A5rd/Kolbotn/meteogram.png"/></p>
            </c:when>
            <c:when test="${param.p == 3}">
                <p><img src="http://www.yr.no/sted/Tyskland/Berlin/Berlin/meteogram.png"/></p>
            </c:when>
            <c:when test="${param.p == 4}">
                <p><img src="http://www.yr.no/sted/Norge/Oppland/Gj%C3%B8vik/Gj%C3%B8vik/meteogram.png"/></p>
            </c:when>
        </c:choose>
        <h3>24 hour presentation</h3>
        <p>
        <c:choose>
            <c:when test="${last24 != null}">
                <table>
                    <tr>
                        <c:forEach var="pair" items="${last24}">
                            <td class="hourpres">
                                <c:set var="periodInfo">From:<fmt:formatDate value="${pair.periodForecast.fromTime}"
                                                                             timeZone="Z"
                                                                             pattern="dd-MM-yy HH:mm 'Z'"/><br/>To:<fmt:formatDate
                                        value="${pair.periodForecast.toTime}" timeZone="Z"
                                        pattern="dd-MM-yy HH:mm 'Z'"/></c:set>
                                <span onmouseover="tooltip.show('<c:out value="${periodInfo}" escapeXml="true"/>', 200);"
                                      onmouseout="tooltip.hide();">
                                    <img src="<c:url value="/images/weather/${pair.periodForecast.symbol.number}.png"/>"/><br/>
                                </span>
                                <span class="hourdate"><fmt:formatDate value="${pair.pointForecast.fromTime}"
                                                                       pattern="dd-MM-yy"/></span></br>
                                <span class="hourdate"><fmt:formatDate value="${pair.pointForecast.fromTime}"
                                                                       pattern="HH:mm"/></span></br>
                                <span class="hourtemp"><fmt:formatNumber value="${pair.pointForecast.temperature.value}"
                                                                         pattern="0"/>&#8451;</span><br/>
                                <span class="hourpre"><fmt:formatNumber
                                        value="${pair.periodForecast.precipitation.minValue}" pattern="0.0"/> - <fmt:formatNumber
                                        value="${pair.periodForecast.precipitation.maxValue}" pattern="0.0"/>mm</span><br/>
                                <c:set var="symbolName">${fn:toLowerCase(pair.pointForecast.windDirection.name)}<fmt:formatNumber pattern="00"
                                                      value="${pair.pointForecast.windSpeed.beaufort}"/></c:set>
                                <img src="<c:url value="/images/wind/${symbolName}.png"/>"/>
                                <span class="hourdate">${pair.pointForecast.windDirection.name} (${pair.pointForecast.windDirection.deg})</span></br>
                            </td>
                        </c:forEach>
                    </tr>
                </table>
            </c:when>
            <c:otherwise>
                Could not fetch 24 hour forecast.
            </c:otherwise>
        </c:choose>
        </p>

        <h3>3 days</h3>

        <table>
            <tr>
                <td class="hourpres">
                    <c:set var="periodInfo">From:<fmt:formatDate value="${today.periodForecast.fromTime}"
                                                                 timeZone="Z"
                                                                 pattern="dd-MM-yy HH:mm 'Z'"/><br/>To:<fmt:formatDate
                            value="${today.periodForecast.toTime}" timeZone="Z"
                            pattern="dd-MM-yy HH:mm 'Z'"/></c:set>
                    <span onmouseover="tooltip.show('<c:out value="${periodInfo}" escapeXml="true"/>', 200);"
                          onmouseout="tooltip.hide();">
                        <img src="<c:url value="/images/weather/${today.periodForecast.symbol.number}.png"/>"/><br/>
                    </span>
                    <span class="hourdate"><fmt:formatDate value="${today.pointForecast.fromTime}"
                                                           pattern="dd-MM-yy"/></span></br>
                    <span class="hourdate"><fmt:formatDate value="${today.pointForecast.fromTime}"
                                                           pattern="HH:mm"/></span></br>
                    <span class="hourtemp"><fmt:formatNumber
                            value="${today.pointForecast.temperature.value}"
                            pattern="0"/>&#8451;</span><br/>
                    <span class="hourpre"><fmt:formatNumber
                            value="${today.periodForecast.precipitation.minValue}" pattern="0.0"/> - <fmt:formatNumber
                            value="${today.periodForecast.precipitation.maxValue}"
                            pattern="0.0"/>mm</span><br/>
                    <c:set var="symbolName">${fn:toLowerCase(today.pointForecast.windDirection.name)}<fmt:formatNumber
                            pattern="00" value="${today.pointForecast.windSpeed.beaufort}"/></c:set>
                    <img src="<c:url value="/images/wind/${symbolName}.png"/>"/>
                    <span class="hourdate">${today.pointForecast.windDirection.name} (${today.pointForecast.windDirection.deg})</span></br>
                </td>
                <td class="hourpres">
                    <c:set var="periodInfo">From:<fmt:formatDate value="${tomorrow.periodForecast.fromTime}"
                                                                 timeZone="Z"
                                                                 pattern="dd-MM-yy HH:mm 'Z'"/><br/>To:<fmt:formatDate
                            value="${tomorrow.periodForecast.toTime}" timeZone="Z"
                            pattern="dd-MM-yy HH:mm 'Z'"/></c:set>
                    <span onmouseover="tooltip.show('<c:out value="${periodInfo}" escapeXml="true"/>', 200);"
                          onmouseout="tooltip.hide();">
                       <img src="<c:url value="/images/weather/${tomorrow.periodForecast.symbol.number}.png"/>"/><br/>
                    </span>
                    <span class="hourdate"><fmt:formatDate value="${tomorrow.pointForecast.fromTime}"
                                                           pattern="dd-MM-yy"/></span></br>
                    <span class="hourdate"><fmt:formatDate value="${tomorrow.pointForecast.fromTime}"
                                                           pattern="HH:mm"/></span></br>
                    <span class="hourtemp"><fmt:formatNumber
                            value="${tomorrow.pointForecast.temperature.value}"
                            pattern="0"/>&#8451;</span><br/>
                    <span class="hourpre"><fmt:formatNumber
                            value="${tomorrow.periodForecast.precipitation.minValue}"
                            pattern="0.0"/> - <fmt:formatNumber
                            value="${tomorrow.periodForecast.precipitation.maxValue}"
                            pattern="0.0"/>mm</span><br/>
                    <c:set var="symbolName">${fn:toLowerCase(tomorrow.pointForecast.windDirection.name)}<fmt:formatNumber pattern="00" value="${tomorrow.pointForecast.windSpeed.beaufort}"/></c:set>
                    <img src="<c:url value="/images/wind/${symbolName}.png"/>"/>
                    <span class="hourdate">${tomorrow.pointForecast.windDirection.name} (${tomorrow.pointForecast.windDirection.deg})</span></br>
                </td>
                <td class="hourpres">
                    <c:set var="periodInfo">From:<fmt:formatDate value="${thedayaftertomorrow.periodForecast.fromTime}"
                                                                 timeZone="Z"
                                                                 pattern="dd-MM-yy HH:mm 'Z'"/><br/>To:<fmt:formatDate
                            value="${thedayaftertomorrow.periodForecast.toTime}" timeZone="Z"
                            pattern="dd-MM-yy HH:mm 'Z'"/></c:set>
                                <span onmouseover="tooltip.show('<c:out value="${periodInfo}" escapeXml="true"/>', 200);"
                                      onmouseout="tooltip.hide();">
                                    <img src="<c:url value="/images/weather/${thedayaftertomorrow.periodForecast.symbol.number}.png"/>"/><br/>
                                </span>
                                <span class="hourdate"><fmt:formatDate
                                        value="${thedayaftertomorrow.pointForecast.fromTime}"
                                        pattern="dd-MM-yy"/></span></br>
                                <span class="hourdate"><fmt:formatDate
                                        value="${thedayaftertomorrow.pointForecast.fromTime}"
                                        pattern="HH:mm"/></span></br>
                                <span class="hourtemp"><fmt:formatNumber
                                        value="${thedayaftertomorrow.pointForecast.temperature.value}"
                                        pattern="0"/>&#8451;</span><br/>
                                <span class="hourpre"><fmt:formatNumber
                                        value="${thedayaftertomorrow.periodForecast.precipitation.minValue}" pattern="0.0"/> - <fmt:formatNumber
                            value="${thedayaftertomorrow.periodForecast.precipitation.maxValue}"
                            pattern="0.0"/>mm</span><br/>
                    <c:set var="symbolName">${fn:toLowerCase(thedayaftertomorrow.pointForecast.windDirection.name)}<fmt:formatNumber pattern="00" value="${thedayaftertomorrow.pointForecast.windSpeed.beaufort}"/></c:set>
                    <img src="<c:url value="/images/wind/${symbolName}.png"/>"/>
                    <span class="hourdate">${thedayaftertomorrow.pointForecast.windDirection.name} (${thedayaftertomorrow.pointForecast.windDirection.deg})</span></br>
                </td>
            </tr>
        </table>


        <h3>Created</h3>

        <p>${data.created}</p>

        <h3>License</h3>

        <p>${data.meta.licenseUrl}</p>

        <h3>Models</h3>
        <table border="1">
            <tr>
                <th>name</th>
                <th>to</th>
                <th>from</th>
                <th>runEnded</th>
                <th>nextRun</th>
                <th>termin</th>
            </tr>
            <c:forEach var="model" items="${data.meta.models}">
                <tr>
                    <td>${model.name}</td>
                    <td>${model.to}</td>
                    <td>${model.from}</td>
                    <td>${model.runEnded}</td>
                    <td>${model.nextRun}</td>
                    <td>${model.termin}</td>
                </tr>
            </c:forEach>
        </table>
        <h3>Location</h3>
        <table>
            <tr>
                <th>Longitude</th>
                <th>Latitude</th>
                <th>Altitude</th>
            </tr>
            <tr>
                <td>${data.location.longitude}</td>
                <td>${data.location.latitude}</td>
                <td>${data.location.altitude}</td>
            </tr>
        </table>

    </c:if>


    <pre>
<c:out escapeXml="true" value="${raw}"/>
    </pre>
</div>
<script type="text/javascript" src="<c:url value="/script.js" />"></script>
</body>
</html>