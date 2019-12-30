package com.sdt.webservice.demo.service.impl;

import com.sdt.webservice.demo.service.HelloService;
import org.apache.cxf.interceptor.InInterceptors;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Date;


@InInterceptors(interceptors={"com.sdt.webservice.demo.interceptor.AuthInterceptor"})// 添加拦截器
@WebService
@SOAPBinding(style= SOAPBinding.Style.RPC)
@Component
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String user) {
        return user+" sayHello at "+new Date();
    }
}
