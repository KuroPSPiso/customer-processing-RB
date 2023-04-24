package com.example.demo.processing;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import com.example.demo.models.MT940Min;
import com.example.demo.processing.XML.Records;
import com.example.demo.repositories.MT940MinRepository;
import com.example.demo.services.MT940MinService;
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
	
	public static ProcessReport processCSV(byte[] contents, final MT940MinService repo)
	{
		ProcessReport report = ProcessReport.DefaultReport();
		
		//format csv
		String sContents = new String(contents, 0, contents.length);
		List<List<String>> records = new ArrayList<>();
		try (Scanner scanner = new Scanner(sContents)) {
		    while (scanner.hasNextLine()) {
		        records.add(getRecordFromLine(scanner.nextLine()));
		    }
		}
		
		report = new ProcessReport(false, String.format("%s\n", sContents.length()));
		
		return report;
	}
	
	public static ProcessReport processXML(byte[] contents, final MT940MinService service)
	{
		ProcessReport report = ProcessReport.DefaultReport();
		XmlMapper mapper = new XmlMapper();
		try {
			Records records = mapper.readValue(contents, Records.class);
			if(records == null) throw new Exception("Unable to load records\n");
			if(records.getTransactions() == null) throw new Exception("Records attempted to load but was unable to parse\n");
			
			MT940Min[] transactions = new MT940Min[records.getTransactions().size()];
			records.getTransactions().toArray(transactions);
			
			transactions = checkTransactions(report, transactions);
			
			transactions = service.addFileTransactions(transactions);
			//build final report
			report = rebuildProcessReport(report, transactions);
		} catch (StreamReadException e) {
			e.printStackTrace();
		} catch (DatabindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			report = new ProcessReport(false, e.getMessage());
		}
		
		return report;
	}
	
	private static ProcessReport rebuildProcessReport(ProcessReport report, MT940Min[] transactions) {
		int succeeded = 0;
		
		ProcessReport newReport = ProcessReport.DefaultSuccessReport();
		
		for(MT940Min transaction : transactions) {
			if(!transaction.isTransaction_Status()) {
				newReport.status = false;
				newReport.log += transaction.getTransaction_StatusMsg();
			} else {
				succeeded += 1;
			}
		}
		
		newReport.log += String.format("\n%s records were added.\n", succeeded);
		
		return newReport;
	}

	private static MT940Min[] checkTransactions(ProcessReport report, MT940Min[] transactions) {
		HashMap<Long, Integer> transactionReferenceFirstOccuranceList = new HashMap<Long, Integer>();
		HashMap<Long, Integer> transactionReferenceCheckList = new HashMap<Long, Integer>();
		
		for(int iTransaction = 0; iTransaction < transactions.length; iTransaction++)
		{
			MT940Min transaction = transactions[iTransaction];
			transaction.setTransaction_Status(true);
			
			//check if the transaction reference already exists
			ProcessReport referenceCheck = ProcessReport.DefaultSuccessReport();
			if(!transactionReferenceCheckList.containsKey(transaction.getReference())) {
				transactionReferenceCheckList.put(transaction.getReference(), 1);
				transactionReferenceFirstOccuranceList.put(transaction.getReference(), iTransaction);
			} else {
				//invalidate values
				int reoccurance = 0;
				transactionReferenceCheckList.put(transaction.getReference(), reoccurance = transactionReferenceCheckList.get(transaction.getReference()) + 1);
				transactions[transactionReferenceFirstOccuranceList.get(transaction.getReference())].setTransaction_Status(false);
				transactions[transactionReferenceFirstOccuranceList.get(transaction.getReference())].setTransaction_StatusMsg("reference number is not unique");
				
				referenceCheck = new ProcessReport(false, String.format("%s: has been detected %s times.\n", transaction.getReference(), reoccurance));
				transaction.setTransaction_StatusMsg("reference number is not unique\n");
			}
			//TODO: IBAN check
			
			ProcessReport balanceCheck = balanceCheck(transaction);
			
			if(!referenceCheck.status) {
				report.log += referenceCheck.log;
				transaction.setTransaction_Status(false);
				transaction.appendTransaction_StatusMsg(referenceCheck.log);
			}
			if(!balanceCheck.status) {
				report.log += balanceCheck.log;
				transaction.setTransaction_Status(false);
				transaction.appendTransaction_StatusMsg(balanceCheck.log);
			}
			
			transactions[iTransaction] = transaction;
		}
		
		return transactions;
	}
	
	private static ProcessReport balanceCheck(MT940Min transaction) {

		ProcessReport report = ProcessReport.DefaultSuccessReport();
		if(transaction.getMutation().length() < 2) return new ProcessReport(false, String.format("%s: missing data on mutation\n", transaction.getReference()));
		double mutationValue = Double.valueOf(transaction.getMutation().substring(1, transaction.getMutation().length()));
		double calculatedValue = transaction.getStart_Balance();
		
		switch(transaction.getMutation().charAt(0)) {
		case '-':
			calculatedValue -= mutationValue;
			break;
		case '+':
			calculatedValue += mutationValue;
			break;
		default: return new ProcessReport(false, String.format("%s: missing data on mutation\n", transaction.getReference()));
		}
		
		calculatedValue = round(calculatedValue, 2);
		
		if(calculatedValue != transaction.getEnd_Balance())
		{
			return new ProcessReport(false, String.format("%s: mutated value (%s) does not match expected end balance (%s)\n", transaction.getReference(), calculatedValue, transaction.getEnd_Balance()));
		}
		
		return report;
	}
	
	//TODO: should all be replaced with string to int and offsetting decimal pointers.
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
