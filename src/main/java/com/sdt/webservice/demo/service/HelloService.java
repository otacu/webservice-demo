package com.sdt.webservice.demo.service;

import javax.jws.WebMethod;

@javax.jws.WebService
public interface HelloService {

    @WebMethod
    String sayHello(String user);
}
