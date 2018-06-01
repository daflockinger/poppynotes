package com.flockinger.poppynotes.notesService.api;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.flockinger.poppynotes.notesService.dao.UserWhitelistRepository;


@ContextConfiguration(classes = {WhitelistUserInterceptor.class, UserWhitelistRepository.class})
@RunWith(SpringRunner.class)
public class WhitelistUserInterceptorTest {

  @MockBean
  private UserWhitelistRepository whitelistDao;
  @Autowired
  private WhitelistUserInterceptor interceptor;
  
  @Test
  public void testPreHandle_withExistingUser_shouldReturnTrue() throws Exception {
    when(whitelistDao.existsByAllowedUserHash(matches("user1"))).thenReturn(true);
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getHeader(anyString())).thenReturn("user1");
    
    boolean result = interceptor.preHandle(mockRequest, mock(HttpServletResponse.class), null);
    assertEquals("verify that correct request responds true", true, result);
  }
  
  
  @Test
  public void testPreHandle_withNotExistingUser_shouldReturnFalseSet401() throws Exception {
    when(whitelistDao.existsByAllowedUserHash(matches("nonexistante"))).thenReturn(false);
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getHeader(anyString())).thenReturn("nonexistante");
    
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    boolean result = interceptor.preHandle(mockRequest, mockResponse, null);
    assertEquals("verify that request responds false", false, result);
    
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(mockResponse).setStatus(statusCaptor.capture());
    assertEquals("verify correct status is set", 401, statusCaptor.getValue().intValue());
  }
  
  @Test
  public void testPreHandle_withMissingUserIdHeader_shouldReturnFalseSet401() throws Exception {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getHeader(anyString())).thenReturn(null);
    
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    boolean result = interceptor.preHandle(mockRequest, mockResponse, null);
    assertEquals("verify that request responds false", false, result);
    
    verify(whitelistDao, times(0)).existsByAllowedUserHash(anyString());
    
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(mockResponse).setStatus(statusCaptor.capture());
    assertEquals("verify correct status is set", 401, statusCaptor.getValue().intValue());
  }
}
