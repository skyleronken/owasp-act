package userInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class StandardTableModel extends AbstractTableModel {

	protected Vector<String> columnNames;
    protected Vector<String[]> items;
    @SuppressWarnings("rawtypes")
	protected Comparator comparator = new Comparator<String[]>(){
            public int compare(String[] arg0, String[] arg1) {
                    return arg0[0].compareTo(arg1[0]);
            }
    };

    public StandardTableModel(Vector<String[]> items, Vector<String> columnNames){
            this.columnNames = columnNames;
            this.items = items;
    }


    public String getColumnName(int col){
            return columnNames.get(col);
    }

    public boolean isCellEditable(int row, int col){ 
            return false; 
    }

    public void setValueAt(String value, int row, int col) {
            (items.get(row))[col] = value;
            fireTableCellUpdated(row, col);
    }

    public int getColumnCount() { return columnNames.size(); }

    public int getRowCount() { return items.size(); }

    public Object getValueAt(int vectorIndex, int arrayIndex) {
            return (items.get(vectorIndex))[arrayIndex];
    }

    public Vector<String[]> getItems() {
            return items;
    }
    
    public void setItems(Vector<String[]> items) {
            this.items = items;
            this.fireTableDataChanged();
    }
    
    @SuppressWarnings("unchecked")
	public void sortItems()
    {
            Collections.sort(items, comparator);
    }

}
