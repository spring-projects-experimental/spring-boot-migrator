<#if packageName?has_content>
    package ${packageName};

</#if>
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

@EnableWs
@Configuration
public class ${className} extends WsConfigurerAdapter {

@Bean
public ServletRegistrationBean
<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, <#list wsdls as wsdl>"/${wsdl.contextPath}/*"<#sep>, </#sep></#list>);
    }

    <#list wsdls as wsdl>
        @Bean(name = "${wsdl.beanName}")
        SimpleWsdl11Definition ${wsdl.methodName}() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("${wsdl.file}"));
        return wsdl11Definition;
        }

    </#list>
    }
