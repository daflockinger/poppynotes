package com.flockinger.poppynotes.notesService.api;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.flockinger.poppynotes.notesService.dao.UserWhitelistRepository;

@Component
public class WhitelistUserInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private UserWhitelistRepository whitelistDao;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    boolean isUserAllowed = false;
    Optional<String> userHeader = Optional.ofNullable(request.getHeader(NotesApi.USER_HEADER));
    if (userHeader.isPresent()) {
      isUserAllowed = whitelistDao.existsByAllowedUserHash(userHeader.get());
    }
    if(!isUserAllowed) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
    return isUserAllowed;
  }
}
