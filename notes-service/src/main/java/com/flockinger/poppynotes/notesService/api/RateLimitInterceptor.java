package com.flockinger.poppynotes.notesService.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;

@Component
public class RateLimitInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private RequestRateLimiter rateLimiter;

  private final static String MODIFY_IP_KEY_NAME = "modify-IP:";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (StringUtils.equalsAnyIgnoreCase(request.getMethod(), "GET", "HEAD")) {
      return true;
    }
    String clientIp = request.getRemoteAddr();
    boolean modifyCallAllowed = !rateLimiter.overLimitWhenIncremented(MODIFY_IP_KEY_NAME + clientIp);

    if(!modifyCallAllowed) {
      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    }
    return modifyCallAllowed;
  }
}
