package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MT940Min {
	private @Id @GeneratedValue Long reference;
	private String account_Number; //IBAN
	private String description;
	private double start_Balance;
	private String mutation; //to be split and calculated
	private double end_Balance;
	
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
}
