package com.sOm2y.XML.people;

public class Person {

	private String uPIField;

	public Person(String uPIField) {
		super();

		this.uPIField = uPIField;

	}

	public String getuPIField() {
		return uPIField;
	}

	public void setuPIField(String uPIField) {
		this.uPIField = uPIField;
	}

	public Person() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return uPIField + "\n";
	}

}
