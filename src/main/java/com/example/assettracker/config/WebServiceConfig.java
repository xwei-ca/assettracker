package com.example.assettracker.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurer;
import org.springframework.ws.soap.server.endpoint.SoapFaultAnnotationExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig implements WsConfigurer {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "assets")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema assetSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("AssetsPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://example.com/assettracker/soap");
        wsdl11Definition.setSchema(assetSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema assetSchema() {
        return new SimpleXsdSchema(new ClassPathResource("META-INF/schemas/assetWs.xsd"));
    }

    @Bean
    public SoapFaultAnnotationExceptionResolver soapFaultExceptionResolver() {
        return new SoapFaultAnnotationExceptionResolver();
    }    
}