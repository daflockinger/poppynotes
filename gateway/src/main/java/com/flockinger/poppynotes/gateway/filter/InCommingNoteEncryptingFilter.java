package com.flockinger.poppynotes.gateway.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flockinger.poppynotes.gateway.model.ModifiableHttpServletRequest;
import com.flockinger.poppynotes.gateway.service.NoteEncryptionService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class InCommingNoteEncryptingFilter extends ZuulFilter {
	
	@Autowired
	private NoteEncryptionService encryptionService;
	
	private static final String NOTES_PATH = "/notes";
	private static Logger logger = Logger.getLogger(InCommingNoteEncryptingFilter.class.getName());
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		ModifiableHttpServletRequest modifiableRequest = new ModifiableHttpServletRequest(request);
		
		try {
			InputStream encryptedNoteStream = encryptionService.encryptNote(request.getInputStream(), "");
			modifiableRequest.setInputStream(new SimpleServletInputStream(encryptedNoteStream));
		} catch (IOException e) {
			logger.warn("Cannot fetch ServletRequest InputStream!",e);
		}
		
		ctx.setSendZuulResponse(false);
		ctx.setRequest(modifiableRequest);
		return null;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		return isNotesPath(request.getRequestURL()) && isPostOrPut(request);
	}
	
	boolean isNotesPath(StringBuffer url) {
		return StringUtils.containsIgnoreCase(url.toString(), NOTES_PATH);
	}
	
	boolean isPostOrPut(HttpServletRequest request){
		return StringUtils.equalsIgnoreCase(request.getMethod(),"post") || StringUtils.equalsIgnoreCase(request.getMethod(),"put");
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		return "pre";
	}
}
