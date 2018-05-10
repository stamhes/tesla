/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.tesla.gateway.netty.filter.request;

import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * 安全扫描限制
 */
public class SecurityScannerHttpRequestFilter extends HttpRequestFilter {

  private Map<String, String> headers;

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject) {
    this.headers = this.getAllHeaders(servletRequest);
    boolean acunetixAspect = this.contains("Acunetix-Aspect");
    boolean acunetixAspectPassword = this.contains("Acunetix-Aspect-Password");
    boolean acunetixAspectQueries = this.contains("Acunetix-Aspect-Queries");
    boolean xScanMemo = this.contains("X-Scan-Memo");
    boolean xRequestMemo = this.contains("X-Request-Memo");
    boolean xRequestManagerMemo = this.contains("X-RequestManager-Memo");
    boolean xWIPP = this.contains("X-WIPP");
    Pattern pattern1 = Pattern.compile("AppScan_fingerprint");
    Matcher matcher1 = pattern1.matcher(servletRequest.getRequestURI());
    String bsKey = "--%3E%27%22%3E%3CH1%3EXSS%40HERE%3C%2FH1%3E";
    boolean matcher2 = servletRequest.getRequestURI().contains(bsKey);
    Pattern pattern3 = Pattern.compile("netsparker=");
    Matcher matcher3 = pattern3.matcher(servletRequest.getRequestURI());
    if (acunetixAspect || acunetixAspectPassword || acunetixAspectQueries) {
      super.writeFilterLog(headers.toString(), this.getClass(), "Acunetix Web Vulnerability");
      return super.createResponse(HttpResponseStatus.FORBIDDEN, servletRequest.getNettyRequest());
    } else if (xScanMemo || xRequestMemo || xRequestManagerMemo || xWIPP) {
      super.writeFilterLog(headers.toString(), this.getClass(), "HP WebInspect");
      return super.createResponse(HttpResponseStatus.FORBIDDEN, servletRequest.getNettyRequest());
    } else if (matcher1.find()) {
      super.writeFilterLog(headers.toString(), this.getClass(), "Appscan");
      return super.createResponse(HttpResponseStatus.FORBIDDEN, servletRequest.getNettyRequest());
    } else if (matcher2) {
      super.writeFilterLog(headers.toString(), this.getClass(), "Bugscan");
      return super.createResponse(HttpResponseStatus.FORBIDDEN, servletRequest.getNettyRequest());
    } else if (matcher3.find()) {
      super.writeFilterLog(headers.toString(), this.getClass(), "Netsparker");
      return super.createResponse(HttpResponseStatus.FORBIDDEN, servletRequest.getNettyRequest());
    }
    return null;
  }

  private Boolean contains(String name) {
    return this.headers.containsKey(name) || this.headers.containsValue(name);
  }

  private Map<String, String> getAllHeaders(NettyHttpServletRequest request) {
    final Map<String, String> headers = Maps.newHashMap();
    Enumeration<String> e = request.getHeaderNames();
    while (e.hasMoreElements()) {
      String headerName = e.nextElement();
      Enumeration<String> headerValues = request.getHeaders(headerName);
      while (headerValues.hasMoreElements()) {
        headers.put(headerName, headerValues.nextElement());
      }
    }
    return headers;
  }

  @Override
  public RequestFilterTypeEnum filterType() {
    return RequestFilterTypeEnum.SecurityScannerHttpRequestFilter;
  }

}
