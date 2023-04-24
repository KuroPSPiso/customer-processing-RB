package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@Entity
public class MT940Min {
	@JacksonXmlProperty(localName = "reference")
	private @Id Long reference;
	@JacksonXmlProperty(localName = "accountNumber")
	private String account_Number; //IBAN
	@JacksonXmlProperty(localName = "description")
	private String description;
	@JacksonXmlProperty(localName = "startBalance")
	private double start_Balance;
	@JacksonXmlProperty(localName = "mutation")
	private String mutation; //to be split and calculated
	@JacksonXmlProperty(localName = "endBalance")
	private double end_Balance;
	@JsonIgnore
	@Transient
	@javax.persistence.Transient
	private boolean transaction_Status = true;
	@JsonIgnore
	@Transient
	@javax.persistence.Transient
	private String transaction_StatusMsg = "";
	
	public MT940Min() {}

	public MT940Min(Long reference, String account_Number, String description, double start_Balance, String mutation,
			double end_Balance) {
		super();
		this.reference = reference;
		this.account_Number = account_Number;
		this.description = description;
		this.start_Balance = start_Balance;
		this.mutation = mutation;
		this.end_Balance = end_Balance;
		this.transaction_Status = false;
	}
	
	public MT940Min(Long reference, String account_Number, String description, double start_Balance, String mutation,
			double end_Balance, boolean transaction_Status, String transaction_StatusMsg) {
		super();
		this.reference = reference;
		this.account_Number = account_Number;
		this.description = description;
		this.start_Balance = start_Balance;
		this.mutation = mutation;
		this.end_Balance = end_Balance;
	}

	public Long getReference() {
		return reference;
	}

	public String getAccount_Number() {
		return account_Number;
	}

	public String getDescription() {
		return description;
	}

	public double getStart_Balance() {
		return start_Balance;
	}

	public String getMutation() {
		return mutation;
	}

	public double getEnd_Balance() {
		return end_Balance;
	}

	public boolean isTransaction_Status() {
		return transaction_Status;
	}

	public void setTransaction_Status(boolean transaction_Status) {
		this.transaction_Status = transaction_Status;
	}

	public String getTransaction_StatusMsg() {
		return transaction_StatusMsg;
	}

	public void setTransaction_StatusMsg(String transaction_StatusMsg) {
		this.transaction_StatusMsg = transaction_StatusMsg;
	}
	
	public void appendTransaction_StatusMsg(String transaction_StatusMsg) {
		this.transaction_StatusMsg += transaction_StatusMsg;
	}
}
