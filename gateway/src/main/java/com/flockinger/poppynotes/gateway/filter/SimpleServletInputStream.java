package com.flockinger.poppynotes.gateway.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

import org.apache.log4j.Logger;


public class SimpleServletInputStream extends ServletInputStream{

	private InputStream inputStream;
	private static Logger logger = Logger.getLogger(SimpleServletInputStream.class.getName());
	
	public SimpleServletInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}

	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

	@Override
	public boolean isFinished() {
		boolean finished = true;
		try {
			finished = inputStream.available() == 0;
		} catch (IOException e) {
			logger.error("Cannot access sertlet-request input-stream!",e);
		}
		return finished;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(ReadListener listener) {
		throw new RuntimeException("Not implemented");
	}
}
