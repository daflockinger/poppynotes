package com.flockinger.poppynotes.notesService.api;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.flockinger.poppynotes.notesService.config.GeneralConfig;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;

@ContextConfiguration(classes={RateLimitInterceptor.class})
@TestPropertySource(properties={"notes.rate-limit.time-range-minutes=1",
    "notes.rate-limit.call-limit=10"})
@RunWith(SpringRunner.class)
@Import(GeneralConfig.class)
public class RateLimitInterceptorTest {
  
  @Autowired
  private RateLimitInterceptor limitInterceptor;
  
  @Autowired
  private RequestRateLimiter rateLimiter;
  
  private final static String testIP = "1.2.3.4";
  
  @Before
  public void setup() {
    rateLimiter.resetLimit("modify-IP:" + testIP);
  }
  
  @Test
  public void testRateLimit_withLotsOfGetCalls_shouldLetPassThrough() throws Exception {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getRemoteAddr()).thenReturn(testIP);
    when(mockRequest.getMethod()).thenReturn("GET");
    
    for(int i=0; i<100; i++) {
      assertEquals("verify GET call is not limited", true, 
          limitInterceptor.preHandle(mockRequest, null, null));
    }
  }
  
  @Test
  public void testRateLimit_withLotsOfHeadCalls_shouldLetPassThrough() throws Exception {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getRemoteAddr()).thenReturn(testIP);
    when(mockRequest.getMethod()).thenReturn("HEAD");
    
    for(int i=0; i<100; i++) {
      assertEquals("verify HEAD call is not limited", true, 
          limitInterceptor.preHandle(mockRequest, null, null));
    }
  }
  
  @Test
  public void testRateLimit_withNotTooMuchPostCalls_shouldLetPassThrough() throws Exception {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getRemoteAddr()).thenReturn(testIP);
    when(mockRequest.getMethod()).thenReturn("POST");
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    
    for(int i=0; i<10; i++) {
      assertEquals("verify POST call limit not reached yet", true, 
          limitInterceptor.preHandle(mockRequest, mockResponse, null));
    }
  }
  
  @Test
  public void testRateLimit_withTooMuchPostCalls_shouldLimit() throws Exception {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getRemoteAddr()).thenReturn(testIP);
    when(mockRequest.getMethod()).thenReturn("POST");
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    
    for(int i=0; i<10; i++) {
      assertEquals("verify POST call limit not reached yet", true, 
          limitInterceptor.preHandle(mockRequest, mockResponse, null));
    }
    assertEquals("verify POST call limit reached", false, 
        limitInterceptor.preHandle(mockRequest, mockResponse, null));
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(mockResponse).setStatus(statusCaptor.capture());
    assertEquals("verify correct too-many-requests status is set", 
        HttpStatus.TOO_MANY_REQUESTS.value(), statusCaptor.getValue().intValue());
  }
  
  @Test
  public void testRateLimit_withTooMuchPutCalls_shouldLimit() throws Exception {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getRemoteAddr()).thenReturn(testIP);
    when(mockRequest.getMethod()).thenReturn("PUT");
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    
    for(int i=0; i<10; i++) {
      assertEquals("verify PUT call limit not reached yet", true, 
          limitInterceptor.preHandle(mockRequest, mockResponse, null));
    }
    assertEquals("verify PUT call limit reached", false, 
        limitInterceptor.preHandle(mockRequest, mockResponse, null));
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(mockResponse).setStatus(statusCaptor.capture());
    assertEquals("verify correct too-many-requests status is set", 
        HttpStatus.TOO_MANY_REQUESTS.value(), statusCaptor.getValue().intValue());
  }
  
  @Test
  public void testRateLimit_withTooMuchDeleteCalls_shouldLimit() throws Exception {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getRemoteAddr()).thenReturn(testIP);
    when(mockRequest.getMethod()).thenReturn("DELETE");
    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    
    for(int i=0; i<10; i++) {
      assertEquals("verify DELETE call limit not reached yet", true, 
          limitInterceptor.preHandle(mockRequest, mockResponse, null));
    }
    assertEquals("verify DELETE call limit reached", false, 
        limitInterceptor.preHandle(mockRequest, mockResponse, null));
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(mockResponse).setStatus(statusCaptor.capture());
    assertEquals("verify correct too-many-requests status is set", 
        HttpStatus.TOO_MANY_REQUESTS.value(), statusCaptor.getValue().intValue());
  }
}
