#!/usr/bin/env bash
# http://www.mojohaus.org/jaxb2-maven-plugin/Documentation/v2.2/example_xjc_basic.html
rm -fR src/main/xsd/*
for a in `cat scripts/met_service_xsd_schemas.txt`
do
   SERVICENAME=$(echo ${a} | rev | cut -d/ -f3 | rev)
   VERSION=$(echo ${a} | rev | cut -d/ -f2 | rev | tr . _)
   EXTRA=""
   if [ $SERVICENAME == "errornotifications" ]
   then
        EXTRA="-b src/main/xjb/errornotifications.xjb"
   fi
   ./scripts/download_schema.sh $a src/main/xsd/${SERVICENAME}/v$VERSION "${SERVICENAME}.xsd"
   xjc ${EXTRA} -extension "src/main/xsd/${SERVICENAME}/v${VERSION}/${SERVICENAME}.xsd" -d "src/main/java" -readOnly -p "no.api.meteo.jaxb.$SERVICENAME.v$VERSION"
done

# Handle manually converted DTDs
xjc -extension "src/main/dtd/extremeforecast/v1_0/extremeforecast.xsd" -d "src/main/java" -readOnly -p "no.api.meteo.jaxb.extremeforecast.v1_0"
xjc -extension "src/main/dtd/textforecast/v1_6/textforecast.xsd" -d "src/main/java" -readOnly -p "no.api.meteo.jaxb.textforecast.v1_6"
