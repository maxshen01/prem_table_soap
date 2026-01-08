package com.prem_table_soap.backend.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

//to summarise, the http path doesn't matter as much compared to rest apis. as long as /ws is included, whatever comes
//afterwards is all handled the same. config apis care about the content of the xml request. also wsdl's are xml files
//for documentation, and are not needed, but highly recommended.

@EnableWs  //tells spring to endable web services
@Configuration  //tells spring this is config class
public class WebServiceConfig {

    //this bean is used to configure the dispatcher servlet, telling it to use the endpoint beans
    //to decide which endpoint should be hit based on the incoming request.
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {

        //main spring object used to handle different routes
        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();

        //give it spring application context, i.e. show it all endpoints
        messageDispatcherServlet.setApplicationContext(context);

        //make wsdl dynamic
        messageDispatcherServlet.setTransformWsdlLocations(true);

        //bean says run messageDispatcherServlet when the endpoint is localhost:8080/ws, and let it
        //figure out which endpoint it needs to hit.
        return new ServletRegistrationBean<>(messageDispatcherServlet, "/ws/*");
    }

    //create bean called courses to define the wsdl of the api
    @Bean(name = "teams")
    public DefaultWsdl11Definition teamsWsdl(XsdSchema teamsSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("CoursePort");
        definition.setTargetNamespace("http://prem_table_soap.com/backend/xsdObjects/team");
        definition.setLocationUri("/ws");
        definition.setSchema(teamsSchema);
        return definition;
    }

    //create a simpleXSDSchema using the xsd found in course-details.xsd
    @Bean
    public XsdSchema teamsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("teams.xsd"));
    }

    @Bean(name = "results")
    public DefaultWsdl11Definition resultsWsdl(XsdSchema resultsSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("CoursePort");
        definition.setTargetNamespace("http://prem_table_soap.com/backend/xsdObjects/results");
        definition.setLocationUri("/ws");
        definition.setSchema(resultsSchema);
        return definition;
    }

    //create a simpleXSDSchema using the xsd found in course-details.xsd
    @Bean
    public XsdSchema resultsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("results.xsd"));
    }
}
