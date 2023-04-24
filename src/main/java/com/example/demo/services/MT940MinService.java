package com.example.demo.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.MT940Min;
import com.example.demo.processing.ProcessReport;
import com.example.demo.repositories.MT940MinRepository;

@Service
public class MT940MinService {

	private final MT940MinRepository repo;
	
	MT940MinService(MT940MinRepository repo)
	{
		this.repo = repo;
	}

	public MT940Min[] addFileTransactions(MT940Min[] transactions)
	{
		for(int iTransaction = 0; iTransaction < transactions.length; iTransaction++)
		{
			MT940Min transaction = transactions[iTransaction];
			if(transaction.isTransaction_Status()) {
				try {
					transactions[iTransaction] = addTransaction(transaction);
				} catch (Exception e) {
					transactions[iTransaction].setTransaction_Status(false);
					transactions[iTransaction].setTransaction_StatusMsg(e.getMessage());
				}
			}
		}
		
		return transactions;
	}

	@Transactional (rollbackFor = Exception.class)
	public MT940Min addTransaction(MT940Min transaction) throws Exception
	{
		Optional<MT940Min> knownRecordWithId = this.repo.findById(transaction.getReference());
		if(knownRecordWithId.isPresent()) throw new Exception(String.format("%s: transaction with this id already exists in the database\n", transaction.getReference()));
		return this.repo.save(transaction);
	}
}
