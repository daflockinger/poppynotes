package com.flockinger.poppynotes.gateway.filter;

import java.io.InputStream;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.flockinger.poppynotes.gateway.model.AuthUserResponse;
import com.flockinger.poppynotes.gateway.service.NoteEncryptionService;
import com.flockinger.poppynotes.gateway.service.UserClientService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class OutGoingNoteDecryptingFilter extends ZuulFilter {

	@Autowired
	private NoteEncryptionService encryptionService;

	@Autowired
	private UserClientService userService;

	private static final String NOTES_PATH = "/notes";

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		Principal principal = ctx.getRequest().getUserPrincipal();
		AuthUserResponse userDetails = userService.getUserInfoFromAuthEmail(principal.getName());

		InputStream decryptedNoteStream = encryptionService.decryptNote(ctx.getResponseDataStream(),
				userDetails.getCryptKey(),principal.getName());
		ctx.setResponseDataStream(decryptedNoteStream);
		ctx.setSendZuulResponse(true);
		return null;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		return isNotesPath(request.getRequestURL()) && isGet(request);
	}

	boolean isNotesPath(StringBuffer url) {
		return StringUtils.containsIgnoreCase(url.toString(), NOTES_PATH);
	}

	boolean isGet(HttpServletRequest request) {
		return StringUtils.equalsIgnoreCase(request.getMethod(), HttpMethod.GET.name());
	}

	@Override
	public int filterOrder() {
		return 11;
	}

	@Override
	public String filterType() {
		return "post";
	}
}
