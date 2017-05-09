package com.skilrock.cs.beans;

public class CamlotAvailBean {
	private CamlotSOAPHeaderBean header ;
	private boolean verbose;
	private boolean available;
	private CamlotFaultBean fault;
	
	public CamlotSOAPHeaderBean getHeader() {
		return header;
	}
	public void setHeader(CamlotSOAPHeaderBean header) {
		this.header = header;
	}
	public boolean isVerbose() {
		return verbose;
	}
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public CamlotFaultBean getFault() {
		return fault;
	}
	public void setFault(CamlotFaultBean fault) {
		this.fault = fault;
	}
}
