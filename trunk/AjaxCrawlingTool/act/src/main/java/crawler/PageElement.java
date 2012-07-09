package crawler;

import java.io.Serializable;

public class PageElement implements Serializable {


	private static final long serialVersionUID = -2082240380674193714L;
	private String tag = "";
	private String xPath = "";
	private String attributeName = "";
	private String attributeValue = "";
	private String text = "";
	private boolean doClick = true;

	//Constructors

	public PageElement(){
	}
	
	public PageElement(String tag){
		this();
		this.tag = tag;
	}

	// Tag Accessors
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	// XPath Accessors

	public String getXPath() {
		return xPath;
	}

	public void setXPath(String xPath) {
		this.xPath = xPath;
	}

	// Attribute Name Accessors
	
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	// Attribute Value Accessors
	
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	// Text Accessors
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	// Do Click Accessors
	
	public boolean doClick(){
		return this.doClick;
	}
	
	public void setDoClick(boolean b){
		this.doClick = b;
	}
	
	public Object[] toObjectArray(){
		return new Object[]{tag,attributeName,attributeValue,text,xPath,new Boolean(doClick)};
	}
	
}
