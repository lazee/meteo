#!/usr/bin/env bash
# http://www.mojohaus.org/jaxb2-maven-plugin/Documentation/v2.2/example_xjc_basic.html
if [ -z "$1" ]
then
    echo "usage: ./scripts/download_schemas.sh FILENAME"
    echo "  FILENAME is where the Maven plugin configuration will be written to"
    exit 1
fi
xml=""
xml+="<plugin>\n"
xml+="\t<groupId>org.codehaus.mojo</groupId>\n"
xml+="\t<artifactId>jaxb2-maven-plugin</artifactId>\n"
xml+="\t<version>2.3.1</version>\n"
xml+="\t<executions>\n"
for a in `cat scripts/met_service_xsd_schemas.txt`
do
   SERVICENAME=$(echo ${a} | rev | cut -d/ -f3 | rev)
   VERSION=$(echo ${a} | rev | cut -d/ -f2 | rev | tr . _)
   xml+="\t\t<execution>\n"
   xml+="\t\t\t<id>xjc-${SERVICENAME}</id>\n"
   xml+="\t\t\t<goals>\n"
   xml+="\t\t\t\t<goal>xjc</goal>\n"
   xml+="\t\t\t</goals>\n"
   xml+="\t\t\t<configuration>\n"
   xml+="\t\t\t\t<clearOutputDir>false</clearOutputDir>\n"
   xml+="\t\t\t\t<quiet>true</quiet>\n"
   xml+="\t\t\t\t<readOnly>true</readOnly>\n"
   xml+="\t\t\t\t<sources>\n"
   xml+="\t\t\t\t\t<source>src/main/xsd/${SERVICENAME}/${SERVICENAME}_v${VERSION}.xsd</source>\n"
   xml+="\t\t\t\t</sources>\n"
   xml+="\t\t\t\t<packageName>no.api.meteo.jaxb.${SERVICENAME}.v${VERSION}</packageName>\n"
   xml+="\t\t\t</configuration>\n"
   xml+="\t\t</execution>\n"
   ./scripts/download_schema.sh $a "xsd"
done

xml+="\t\t<!--\n"
for a in `cat scripts/met_service_dtd_schemas.txt`
do
   SERVICENAME=$(echo ${a} | rev | cut -d/ -f3 | rev)
   VERSION=$(echo ${a} | rev | cut -d/ -f2 | rev | tr . _)
   xml+="\t\t<execution>\n"
   xml+="\t\t\t<id>xjc-${SERVICENAME}</id>\n"
   xml+="\t\t\t<goals>\n"
   xml+="\t\t\t\t<goal>xjc</goal>\n"
   xml+="\t\t\t</goals>\n"
   xml+="\t\t\t<configuration>\n"
   xml+="\t\t\t\t<clearOutputDir>false</clearOutputDir>\n"
   xml+="\t\t\t\t<quiet>true</quiet>\n"
   xml+="\t\t\t\t<readOnly>true</readOnly>\n"
   xml+="\t\t\t\t<sources>\n"
   xml+="\t\t\t\t\t<source>src/main/dtd/${SERVICENAME}/${SERVICENAME}_v${VERSION}.dtd</source>\n"
   xml+="\t\t\t\t</sources>\n"
   xml+="\t\t\t\t<packageName>no.api.meteo.jaxb.${SERVICENAME}.v${VERSION}</packageName>\n"
   xml+="\t\t\t\t<sourceType>dtd</sourceType>\n"
   xml+="\t\t\t</configuration>\n"
   xml+="\t\t</execution>\n"
   ./scripts/download_schema.sh $a "dtd"
done
xml+="\t\t-->\n"
xml+="\t</executions>\n"
xml+="</plugin>\n"
echo "Maven Plugin Configuration written to $1"
echo -e $xml > $1