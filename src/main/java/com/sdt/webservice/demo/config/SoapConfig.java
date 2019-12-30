package com.sdt.webservice.demo.config;

import com.sdt.webservice.demo.service.HelloService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class SoapConfig {

    @Autowired
    @Qualifier(Bus.DEFAULT_BUS_ID)
    private SpringBus bus;

    @Autowired
    private HelloService helloService;

    /**
     * 改变项目中服务名的前缀名
     * 没有这个bean：helloService访问地址为 http://localhost:8084/services/helloService?wsdl
     * 有这个bean：helloService访问地址为 http://localhost:8084/sdtapi/helloService?wsdl
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean sdtDispatcherServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/sdtapi/*");
    }

    // 发布多个接口 添加多个@Bean endpoint.publish 这里不能一样
    @Bean
    public Endpoint helloEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, helloService);
        endpoint.publish("/helloService");
        return endpoint;
    }
}
