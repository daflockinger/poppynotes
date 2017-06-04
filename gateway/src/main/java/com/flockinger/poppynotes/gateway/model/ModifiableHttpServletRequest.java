package com.flockinger.poppynotes.gateway.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import com.netflix.zuul.http.HttpServletRequestWrapper;

public class ModifiableHttpServletRequest/* extends HttpServletRequestWrapper*/ implements HttpServletRequest {

	private final HttpServletRequest simpleRequest;
	private ServletInputStream bodyStream = null;
	
	public ModifiableHttpServletRequest(HttpServletRequest simpleRequest) {
		this.simpleRequest = simpleRequest;
	}
	
	@Override
	public Object getAttribute(String name) {
		return simpleRequest.getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return simpleRequest.getAttributeNames();
	}

	@Override
	public String getCharacterEncoding() {
		return simpleRequest.getCharacterEncoding();
	}

	@Override
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		simpleRequest.setCharacterEncoding(env);
	}

	@Override
	public int getContentLength() {
		return simpleRequest.getContentLength();
	}

	@Override
	public long getContentLengthLong() {
		return simpleRequest.getContentLengthLong();
	}

	@Override
	public String getContentType() {
		return simpleRequest.getContentType();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return (bodyStream!=null) ? bodyStream : simpleRequest.getInputStream();
	}
	
	public void setInputStream(ServletInputStream bodyStream) {
		this.bodyStream = bodyStream;
	}

	@Override
	public String getParameter(String name) {
		return simpleRequest.getParameter(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return simpleRequest.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return simpleRequest.getParameterValues(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return simpleRequest.getParameterMap();
	}

	@Override
	public String getProtocol() {
		return simpleRequest.getProtocol();
	}

	@Override
	public String getScheme() {
		return simpleRequest.getScheme();
	}

	@Override
	public String getServerName() {
		return simpleRequest.getServerName();
	}

	@Override
	public int getServerPort() {
		return simpleRequest.getServerPort();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public String getRemoteAddr() {
		return simpleRequest.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return simpleRequest.getRemoteHost();
	}

	@Override
	public void setAttribute(String name, Object o) {
		simpleRequest.setAttribute(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		simpleRequest.removeAttribute(name);
	}

	@Override
	public Locale getLocale() {
		return simpleRequest.getLocale();
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return simpleRequest.getLocales();
	}

	@Override
	public boolean isSecure() {
		return simpleRequest.isSecure();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return simpleRequest.getRequestDispatcher(path);
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getRealPath(String path) {
		return simpleRequest.getRealPath(path);
	}

	@Override
	public int getRemotePort() {
		return simpleRequest.getRemotePort();
	}

	@Override
	public String getLocalName() {
		return simpleRequest.getLocalName();
	}

	@Override
	public String getLocalAddr() {
		return simpleRequest.getLocalAddr();
	}

	@Override
	public int getLocalPort() {
		return simpleRequest.getLocalPort();
	}

	@Override
	public ServletContext getServletContext() {
		return simpleRequest.getServletContext();
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return simpleRequest.startAsync();
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException {
		return simpleRequest.startAsync(servletRequest, servletResponse);
	}

	@Override
	public boolean isAsyncStarted() {
		return simpleRequest.isAsyncStarted();
	}

	@Override
	public boolean isAsyncSupported() {
		return simpleRequest.isAsyncSupported();
	}

	@Override
	public AsyncContext getAsyncContext() {
		return simpleRequest.getAsyncContext();
	}

	@Override
	public DispatcherType getDispatcherType() {
		return simpleRequest.getDispatcherType();
	}

	@Override
	public String getAuthType() {
		return simpleRequest.getAuthType();
	}

	@Override
	public Cookie[] getCookies() {
		return simpleRequest.getCookies();
	}

	@Override
	public long getDateHeader(String name) {
		return simpleRequest.getDateHeader(name);
	}

	@Override
	public String getHeader(String name) {
		return simpleRequest.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return simpleRequest.getHeaders(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return simpleRequest.getHeaderNames();
	}

	@Override
	public int getIntHeader(String name) {
		return simpleRequest.getIntHeader(name);
	}

	@Override
	public String getMethod() {
		return simpleRequest.getMethod();
	}

	@Override
	public String getPathInfo() {
		return simpleRequest.getPathInfo();
	}

	@Override
	public String getPathTranslated() {
		return simpleRequest.getPathTranslated();
	}

	@Override
	public String getContextPath() {
		return simpleRequest.getContextPath();
	}

	@Override
	public String getQueryString() {
		return simpleRequest.getQueryString();
	}

	@Override
	public String getRemoteUser() {
		return simpleRequest.getRemoteUser();
	}

	@Override
	public boolean isUserInRole(String role) {
		return simpleRequest.isUserInRole( role);
	}

	@Override
	public Principal getUserPrincipal() {
		return simpleRequest.getUserPrincipal();
	}

	@Override
	public String getRequestedSessionId() {
		return simpleRequest.getRequestedSessionId();
	}

	@Override
	public String getRequestURI() {
		return simpleRequest.getRequestURI();
	}

	@Override
	public StringBuffer getRequestURL() {
		return simpleRequest.getRequestURL();
	}

	@Override
	public String getServletPath() {
		return simpleRequest.getServletPath();
	}

	@Override
	public HttpSession getSession(boolean create) {
		return simpleRequest.getSession(create);
	}

	@Override
	public HttpSession getSession() {
		return simpleRequest.getSession();
	}

	@Override
	public String changeSessionId() {
		return simpleRequest.changeSessionId();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return simpleRequest.isRequestedSessionIdValid();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return simpleRequest.isRequestedSessionIdFromCookie();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return simpleRequest.isRequestedSessionIdFromURL();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return simpleRequest.isRequestedSessionIdFromUrl();
	}

	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		return simpleRequest.authenticate(response);
	}

	@Override
	public void login(String username, String password) throws ServletException {
		simpleRequest.login( username,  password);
	}

	@Override
	public void logout() throws ServletException {
		simpleRequest.logout();
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return simpleRequest.getParts();
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		return simpleRequest.getPart(name);
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass)
			throws IOException, ServletException {
		return simpleRequest.upgrade(httpUpgradeHandlerClass);
	}

}
