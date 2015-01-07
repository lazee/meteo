<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html" session="false" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Meteo - LocationLTS</title>

    <link href="/components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="/components/jumbotron.css" rel="stylesheet">
    <link rel="stylesheet" href="/components/highlight/styles/tomorrow.css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <style type="text/css">
        .dataTable {
            border: 1px solid black;
        }
        .dataTable th {
            background-color: #eee;
        }
        .dataTable td, .dataTable th {
            padding: 4px;
            border: 1px solid black;
            vertical-align: top;
        }

    </style>
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Meteo</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="<c:url value="/"/>">Home</a></li>
            </ul>
        </div>
        <!--/.navbar-collapse -->
    </div>
</nav>

<div class="jumbotron">
    <div class="container">
        <p>
        <form class="navbar-form navbar" action="" method="get">
            <div class="form-group">
                <input type="hidden" name="issearch" value="true"/>
                <input placeholder="Longitude" class="form-control" name="longitude" type="dec" pattern="^\d*\.\d*$"
                       size="20" value="${longitude}"/>
                <input placeholder="Latitude" class="form-control" name="latitude" type="dec" pattern="^\d*\.\d*$"
                       size="20" value="${latitude}"/>
                <input placeholder="Altitude" class="form-control" name="altitude" type="dec" pattern="^\d*(\.\d*)?$"
                       size="20" value="${altitude}" title="Must be a number!" on/>
                <input class="btn btn-success" type="submit" value="search"/>
            </div>
        </form>
        </p>
        <p>
            <a href="location.html?issearch=true&longitude=10.7460923576733&latitude=59.912726542422&altitude=14&p=1">Oslo</a>
            |
            <a href="location.html?issearch=true&longitude=10.8039&latitude=59.8106&altitude=105&p=2">Kolbotn</a> |
            <a href="location.html?issearch=true&longitude=13.41053&latitude=52.52437&altitude=74&p=3">Berlin</a> |
            <a href="location.html?issearch=true&longitude=10.6915510490055&latitude=60.7957437316181&altitude=129&p=4">Gj&oslash;vik</a>
        </p>
    </div>
</div>

<div class="container">
    <!-- Example row of columns -->
    <div class="row">
        <div class="col-md-12">


            <c:if test="${search}">

                <ul id="myTab" class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active"><a href="#view" id="view-tab" role="tab" data-toggle="tab" aria-controls="view" aria-expanded="true">View</a></li>
                    <li role="presentation"><a href="#data" role="tab" id="data-tab" data-toggle="tab" aria-controls="data">XML</a></li>
                </ul>
                <div id="myTabContent" class="tab-content">
                    <div role="tabpanel" class="tab-pane fade in active" id="view" aria-labelledBy="view-tab">
                        <h3>Forecast for the next days</h3>
                        <h4>Today</h4>
                        <table class="dataTable">
                            <thead>
                            <tr>
                                <th>Time</th>
                                <th>Prediction</th>
                                <th>Temperature</th>
                                <th>Precipitation</th>
                                <th>Wind</th>
                            </tr>
                            </thead>



                            <tr>
                                <td>22</td>
                                <td>22</td>
                                <td>22</td>
                                <td>22</td>
                                <td>22</td>
                            </tr>
                        </table>

                        <h4>Tomorrow</h4>


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
                        <table class="dataTable">
                            <tr>
                                <c:forEach var="pair" items="${last24}">
                                    <td class="hourpres">
                                        <c:set var="periodInfo">
                                            <span class="hourdate">Date: <fmt:formatDate value="${pair.pointForecast.fromTime}" pattern="dd-MM-yy"/></span><br/>
                                            From PeriodForecast:<br/>
                                            {<fmt:formatDate value="${pair.periodForecast.fromTime}" timeZone="Z" pattern="dd-MM-yy HH:mm 'Z'"/>
                                            <br/>
                                            <fmt:formatDate value="${pair.periodForecast.toTime}" timeZone="Z" pattern="dd-MM-yy HH:mm 'Z'"/>}
                                        </c:set>
                                        <img  data-toggle="tooltip" title="" data-original-title="<c:out value="${periodInfo}" escapeXml="true"/>" src="<c:url value="/images/weather/${pair.periodForecast.symbol.number}.png"/>"/><br/>

                                        <span class="hourdate"><fmt:formatDate value="${pair.pointForecast.fromTime}" pattern="HH:mm"/></span><br/>
                                        <span class="hourtemp"><fmt:formatNumber value="${pair.pointForecast.temperature.value}" pattern="0"/>&#8451;</span><br/>
                                <span class="hourpre"><fmt:formatNumber
                                        value="${pair.periodForecast.precipitation.minValue}" pattern="0.0"/> - <fmt:formatNumber
                                        value="${pair.periodForecast.precipitation.maxValue}" pattern="0.0"/>mm</span><br/>
                                        <c:set var="symbolName">${fn:toLowerCase(pair.pointForecast.windDirection.name)}<fmt:formatNumber pattern="00"
                                                                                                                                          value="${pair.pointForecast.windSpeed.beaufort}"/></c:set>
                                        <img src="<c:url value="/images/wind/${symbolName}.png"/>"/>
                                        <span class="hourdate">${pair.pointForecast.windDirection.name} (${pair.pointForecast.windDirection.deg})</span><br/>
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

                        <table class="dataTable">
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
                                                           pattern="dd-MM-yy"/></span><br/>
                    <span class="hourdate"><fmt:formatDate value="${today.pointForecast.fromTime}"
                                                           pattern="HH:mm"/></span><br/>
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
                                    <span class="hourdate">${today.pointForecast.windDirection.name} (${today.pointForecast.windDirection.deg})</span><br/>
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
                                                           pattern="dd-MM-yy"/></span><br/>
                    <span class="hourdate"><fmt:formatDate value="${tomorrow.pointForecast.fromTime}"
                                                           pattern="HH:mm"/></span><br/>
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
                                    <span class="hourdate">${tomorrow.pointForecast.windDirection.name} (${tomorrow.pointForecast.windDirection.deg})</span><br/>
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
                                        pattern="dd-MM-yy"/></span><br/>
                                <span class="hourdate"><fmt:formatDate
                                        value="${thedayaftertomorrow.pointForecast.fromTime}"
                                        pattern="HH:mm"/></span><br/>
                                <span class="hourtemp"><fmt:formatNumber
                                        value="${thedayaftertomorrow.pointForecast.temperature.value}"
                                        pattern="0"/>&#8451;</span><br/>
                                <span class="hourpre"><fmt:formatNumber
                                        value="${thedayaftertomorrow.periodForecast.precipitation.minValue}" pattern="0.0"/> - <fmt:formatNumber
                                        value="${thedayaftertomorrow.periodForecast.precipitation.maxValue}"
                                        pattern="0.0"/>mm</span><br/>
                                    <c:set var="symbolName">${fn:toLowerCase(thedayaftertomorrow.pointForecast.windDirection.name)}<fmt:formatNumber pattern="00" value="${thedayaftertomorrow.pointForecast.windSpeed.beaufort}"/></c:set>
                                    <img src="<c:url value="/images/wind/${symbolName}.png"/>"/>
                                    <span class="hourdate">${thedayaftertomorrow.pointForecast.windDirection.name} (${thedayaftertomorrow.pointForecast.windDirection.deg})</span><br/>
                                </td>
                            </tr>
                        </table>


                        <h3>Created</h3>

                        <p>${data.created}</p>

                        <h3>License</h3>

                        <p><c:out value="${data.meta.licenseUrl}" default="No license found in source"/> </p>

                        <h3>Models</h3>
                        <table class="dataTable">
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
                        <table class="dataTable">
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


                    </div>
                    <div role="tabpanel" class="tab-pane fade" id="data" aria-labelledBy="data-tab">
                        <h3>Raw data from MET</h3>
                        <pre><code class="xml"><c:out escapeXml="true" value="${raw}"/></code></pre>
                    </div>
                </div>

            </c:if>

        </div>

    </div>

    <hr>

    <footer>
        <p><a href="http://github.com/amedia/meteo">The Meteo project</a></p>
    </footer>
</div>
<!-- /container -->


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="/components/jquery/dist/jquery.min.js"></script>
<script src="/components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="/components/ie10-viewport-bug-workaround.js"></script>
<script type="application/javascript">
    $(function () {
        $('[data-toggle="tooltip"]').tooltip({html: true})
    })
</script>
<script src="/components/highlight/highlight.pack.js"></script>
<script>hljs.initHighlightingOnLoad();</script>

</body>
</html>