package com.flockinger.poppynotes.notesService.api;

import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;


public abstract class BaseControllerTest {
  
  protected MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  private final static ObjectMapper mapper = new ObjectMapper();

  @SuppressWarnings("unchecked")
  protected <T extends Object> String json(T entity) throws IOException {
    return mapper.writeValueAsString(entity);
  }
}
