<%@ page contentType="text/html" session="false" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:import url="WEB-INF/jsp/header.jsp"/>
<div class="jumbotron">
    <div class="container">
        <h1>Meteo Examples</h1>

        <p>This application is a collection of examples on how to use Meteo to fetch weather data from api.met.no.
        Click on the Examples dropdown in the top menu to start testing...</p>

        <p>Also keep in mind that all use of the MET data is restricted by the <a href="http://met.no">Norwegian Meteorological Institute</a>.
            Make sure to read their <a href="http://api.met.no/">conditions</a> before use.
        </p>
    </div>
</div>

<c:import url="WEB-INF/jsp/footer.jsp"/>
