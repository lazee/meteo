
Invalid Schema: https://api.met.no/weatherapi/tafmetar/1.0/schema

Add this to errornotifications config in pom after generating new config
~~~
<xjbSources>
    <xjbSource>src/main/xsd/errornotifications/binding.xjb</xjbSource>
</xjbSources>
~~~