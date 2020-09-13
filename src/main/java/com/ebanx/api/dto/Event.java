package com.ebanx.api.dto;

import java.util.HashMap;

public class Event {

	private String type;
	private int amount;
	private int destination;

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}

	private int origin;
	private static HashMap<Integer, Integer> ledger = new HashMap<Integer, Integer>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public HashMap<String, Account> eventProcess(String OpType, int amount, int destination, int origin) {
		int originBalance = 0;
		int desitnationBalance = 0;
		int destinationNewBalance = 0;
		int originNewBalance = 0;
		HashMap<String, Account> result = null;
		type = OpType;
		switch (type) {
		case "deposit":

			if (!checkOrigin(destination)) {
				// Perform deposit
				ledger.put(destination, amount);
				result = new HashMap<String, Account>();
				result.put("destination", new Account(String.valueOf(destination), amount));
			} else {
				ledger.put(destination, ledger.get(destination) + amount);
				result = new HashMap<String, Account>();
				result.put("destination",new Account(String.valueOf(destination), ledger.get(destination)));
			}
			break;
		case "transfer":
			// Get balance from origin
			if (!checkOrigin(origin))
				break;
			originBalance = (int) ledger.get(origin);
			if(checkOrigin(destination))
			{
			desitnationBalance = (int) ledger.get(destination);
			destinationNewBalance = desitnationBalance + amount;
			}
			else
			{
				destinationNewBalance = amount;
			}
			originNewBalance = originBalance - amount;
			// Perform transfer
			ledger.put(origin, originNewBalance);
			ledger.put(destination, destinationNewBalance);
			result = new HashMap<String, Account>();
			result.put("origin", new Account(String.valueOf(origin), originNewBalance));
			result.put("destination", new Account(String.valueOf(destination), destinationNewBalance));
			break;
		case "withdraw":
			if (!checkOrigin(origin))
				break;
			// Perform Withdrawal
			ledger.put(origin, ledger.get(origin) - amount);
			result = new HashMap<String, Account>();
			result.put("origin",
					new Account(String.valueOf(origin), ledger.get(origin)));
			break;
		}

		return result;
	}

	public Account balance(int accountID) {
		Account result = null;
		int balance = 0;
		if (!ledger.containsKey(accountID))
			return null;
		else {
			balance = (int) ledger.get(accountID);
			result = new Account(String.valueOf(accountID), balance);

		}
		return result;
	}

	public void reset() {
		type = "";
		amount = destination = origin = 0;
		ledger = new HashMap<Integer, Integer>();
	}

	private boolean checkOrigin(int originID) {
		return ledger.containsKey(originID);
	}
}
