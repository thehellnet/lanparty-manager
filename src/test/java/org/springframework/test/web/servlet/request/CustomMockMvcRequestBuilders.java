package org.springframework.test.web.servlet.request;

import org.springframework.http.HttpMethod;

public class CustomMockMvcRequestBuilders extends MockMvcRequestBuilders {

    public static CustomAddressMockHttpServletRequestBuilder postCustomRemoteAddress(String urlTemplate, String remoteAddr, Object... uriVars) {
        CustomAddressMockHttpServletRequestBuilder mockHttpServletRequestBuilder = new CustomAddressMockHttpServletRequestBuilder(HttpMethod.POST, urlTemplate, uriVars);
        mockHttpServletRequestBuilder.setRemoteAddr(remoteAddr);
        return mockHttpServletRequestBuilder;
    }
}
