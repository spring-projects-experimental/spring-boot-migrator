<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/integration https://www.springframework.org/schema/integration/spring-integration.xsd
        http://www.springframework.org/schema/integration/http https://www.springframework.org/schema/integration/http/spring-integration-http.xsd"
       xmlns:int="http://www.springframework.org/schema/integration"
       xmlns:int-http="http://www.springframework.org/schema/integration/http">
    <int-http:inbound-gateway request-channel="receiveChannel"
                              path="${path}"
                              supported-methods="GET"/>
    <int:channel id="receiveChannel"/>
    <int:chain input-channel="receiveChannel">
        <int:service-activator expression="'${payload}'"/>
    </int:chain>
</beans>