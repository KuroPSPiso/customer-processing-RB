package com.example.demo.processing;
import java.io.IOException;
import java.util.*;

import com.example.demo.MT940Min;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ProcessMT940Min {
	
	final static String COMMA_DELIMITER = ",";
	
	private static List<String> getRecordFromLine(String line) {
	    List<String> values = new ArrayList<String>();
	    try (Scanner rowScanner = new Scanner(line)) {
	        rowScanner.useDelimiter(COMMA_DELIMITER);
	        while (rowScanner.hasNext()) {
	            values.add(rowScanner.next());
	        }
	    }
	    return values;
	}
	
	public static ProcessReport processCSV(byte[] contents)
	{
		ProcessReport report = ProcessReport.defaultReport;
		
		//format csv
		String sContents = new String(contents, 0, contents.length);
		List<List<String>> records = new ArrayList<>();
		try (Scanner scanner = new Scanner(sContents)) {
		    while (scanner.hasNextLine()) {
		        records.add(getRecordFromLine(scanner.nextLine()));
		    }
		}
		
		report = new ProcessReport(false, String.format("%s", sContents.length()));
		
		return report;
	}
	
	public static ProcessReport processXML(byte[] contents)
	{
		ProcessReport report = ProcessReport.defaultReport;
		
		XmlMapper mapper = new XmlMapper();
		try {
			List records = mapper.readValue(contents, List.class);
			report = new ProcessReport(true, String.format("%s", records.size()));
		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return report;
	}
}
