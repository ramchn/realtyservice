package com.tamkosoft.primerealty.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class BufferedDataSource implements DataSource { 

	private byte[] _data; 
	private String _name; 
	private String _contentType;

	public BufferedDataSource(byte[] data, String name, String contentType) { 
		_data = data; 
		_name = name;
		_contentType = contentType;
	} 

	public String getContentType() { return _contentType;} 
	public InputStream getInputStream() throws IOException { return new ByteArrayInputStream(_data);} 
	public String getName() { return _name;} 

	/** 
	* Returns an OutputStream from the DataSource 
	* @returns OutputStream Array of bytes converted into an OutputStream 
	*/ 
	public OutputStream getOutputStream() throws IOException { 
		OutputStream out = new ByteArrayOutputStream(); 
		out.write(_data); 
		return out;
	}
}