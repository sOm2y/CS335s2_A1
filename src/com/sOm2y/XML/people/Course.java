package com.sOm2y.XML.people;

public class Course {
	private String codeField;
	private String semesterField;
	private String titleField;

	public Course() {
		// TODO Auto-generated constructor stub
	}

	public String getCodeField() {
		return codeField;
	}

	public void setCodeField(String codeField) {
		this.codeField = codeField;
	}

	public String getSemesterField() {
		return semesterField;
	}

	public void setSemesterField(String semesterField) {
		this.semesterField = semesterField;
	}

	public String getTitleField() {
		return titleField;
	}

	public void setTitleField(String titleField) {
		this.titleField = titleField;
	}

	public Course(String codeField, String semesterField, String titleField) {
		super();
		this.codeField = codeField;
		this.semesterField = semesterField;
		this.titleField = titleField;
	}

	@Override
	public String toString() {
		return ""+codeField +"\n"+semesterField+"\n"+titleField+"\n";
	}

}
