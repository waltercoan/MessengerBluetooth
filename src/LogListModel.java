import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;


public class LogListModel extends AbstractTableModel {
	
	private ArrayList<String> logList;
	
	
	public LogListModel(ArrayList<String> logList) {
		this.logList = logList;
	}
	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return logList.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return logList.get(arg0);
	}
	@Override
	public String getColumnName(int arg0) {
		return "Log Execução";
	}
}
