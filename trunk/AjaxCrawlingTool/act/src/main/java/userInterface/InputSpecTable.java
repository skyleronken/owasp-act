package userInterface;

import java.util.Vector;

@SuppressWarnings("serial")
public class InputSpecTable extends StandardTableModel {

	//private Vector<String> columnNames;
    private Vector<String[]> items;
	
	public InputSpecTable(Vector<String[]> items, Vector<String> columnNames) {
		super(items, columnNames);
		this.columnNames = columnNames;
        this.items = items;
	}
	
	public void addRow(String name, String value){
    	items.add(new String[]{name,value});
    	this.fireTableDataChanged();
    }
    
    public void removeRow(int index){
    	items.removeElementAt(index);
    	this.fireTableDataChanged();
    }

}
