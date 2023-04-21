package com.example.demo.processing.XML;

import com.example.demo.MT940Min;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.*;

@JacksonXmlRootElement
public class Records {

	@JacksonXmlProperty(localName = "record")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<MT940Min> transactions;

	public List<MT940Min> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<MT940Min> transactions) {
		if (this.transactions == null){
			this.transactions = new ArrayList<MT940Min>(transactions.size());
        }
		this.transactions.addAll(transactions);
	}
	
	
}
