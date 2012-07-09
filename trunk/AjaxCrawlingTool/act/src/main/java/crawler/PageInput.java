package crawler;

import java.io.Serializable;

public class PageInput implements Serializable {


	private static final long serialVersionUID = 6597455322628442862L;
	private String fieldName = "";
	private String value = "";
	
	//Constructors
	
	public PageInput(){
		
	}
	
	public PageInput(String field, String value){
		this();
		this.fieldName = field;
		this.value = value;
	}

	// Field Name Accessors
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	// Field Value Accessors

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String[] toStringArray(){
		return new String[]{fieldName, value};
	}
	
}
