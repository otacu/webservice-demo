package com.sdt.webservice.demo.interceptor;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;

public class AuthInterceptor extends AbstractSoapInterceptor {

    private static final String BASIC_PREFIX = "Basic ";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";

    /**
     * 指定加入拦截器到某个阶段
     */
    public AuthInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {

        // 获取HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);

        String auth = request.getHeader("Authorization");

        if (auth == null) {
            SOAPException exception = new SOAPException("Authorization 授权信息为空！");
            throw new Fault(exception);
        }
        if (!auth.startsWith(BASIC_PREFIX)) {
            SOAPException exception = new SOAPException("Authorization 非baisc验证");
            throw new Fault(exception);
        }
        // 合法的baisc格式为username:password  例如：admin:123456
        String plaintext = new String(Base64.decodeBase64(auth.substring(BASIC_PREFIX.length())));
        if (StringUtils.isEmpty(plaintext) || !plaintext.contains(":")) {
            SOAPException exception = new SOAPException("Authorization 非baisc验证");
            throw new Fault(exception);
        }

        String[] usernameAndPass = plaintext.split(":");
        String username = usernameAndPass[0];
        String password = usernameAndPass[1];
        if (!USERNAME.equals(username) || !PASSWORD.equals(password)) {
            SOAPException exception = new SOAPException("用户名或密码不正确！");
            throw new Fault(exception);
        }

    }
}
