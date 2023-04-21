package com.example.demo.processing;

public class ProcessReport {
	public boolean status;
	public String log;
	
	public ProcessReport(boolean status, String log) {
		this.status = status;
		this.log = log;
	}

	public static final ProcessReport defaultReport = new ProcessReport(false, "not processed");
	public static final ProcessReport defaultSuccessReport = new ProcessReport(true, "data has succesfully been processed");
	
}
