package com.example.demo.processing;

public class ProcessReport {
	public boolean status;
	public String log;
	
	public ProcessReport(boolean status, String log) {
		this.status = status;
		this.log = log;
	}

	public static ProcessReport DefaultReport() { return new ProcessReport(false, "not processed"); };
	public static ProcessReport DefaultSuccessReport() { return new ProcessReport(true, ""); };
	
	public static final ProcessReport defaultReport = DefaultReport();
	public static final ProcessReport defaultSuccessReport = DefaultSuccessReport();
	
}
