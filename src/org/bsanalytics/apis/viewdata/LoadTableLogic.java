package org.bsanalytics.apis.viewdata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bsanalytics.client.loaddata.ReadCSVFileRowCount;
import org.bsanalytics.general.server.SQLLiteDBAccess;
import org.bsanalytics.general.server.ServerSideGsonConversion;

public class LoadTableLogic {
	
	//static LoadTableFromDataBase lTFD;
    static int column_count=0;
    //static ReadCSVFileRowCount getting_total_rows_from_csv;
    private static Integer totalRows;
    static ServerSideGsonConversion gson_conv = new ServerSideGsonConversion();
    static SQLLiteDBAccess sqlLiteDB = new SQLLiteDBAccess();
    static Statement stmt_sqlite = sqlLiteDB.getLocalSQLLiteDBConnection();
	
	
	public static String initializeDBConnection(String table_name){
	
    	
    	//executing the database statement
		LoadTableFromDataBase.loadDataFromTable(table_name);
    	
    	//getting column count
    	column_count = LoadTableFromDataBase.getColumnCount();
    	
    	//reading total number of rows from CSV file
    	//getting_total_rows_from_csv = new ReadCSVFileRowCount();    	
    	//setTotalRows(getting_total_rows_from_csv.GetLineCount(""));
    	setTotalRows(getToalRowsFromSQLLiteDB(table_name));    	
    	
    	 /*String column_names = getcolumnNames(table_name);*/
         return Integer.toString(getTotalRows());
	}
	
	public static String getcolumnNamesonFirstCall(String table_name){
		 return getcolumnNames(table_name);
		 
	}

	public static String getInitialTable(String fetch_size) {
		System.out.println("fetch_size : " + Integer.parseInt(fetch_size));
		LoadTableFromDataBase.getTCustomRowsList(column_count, Integer.parseInt(fetch_size));
		List<List<Object>> list_chunck = new ArrayList<>();
      	list_chunck = LoadTableFromDataBase.getListTwo();
      	gson_conv.setListForConversion(list_chunck);      	
		return gson_conv.getConvertedString();
	}

	public static Integer getTotalRows() {
		return totalRows;
	}

	public static void setTotalRows(Integer totalRows) {
		LoadTableLogic.totalRows = totalRows;
	}
	
	
	public static int getToalRowsFromSQLLiteDB(String table_name){
		
		
		int total_rows=0;
		String sql = "select * from " + table_name;
		try {
			ResultSet res = stmt_sqlite.executeQuery(sql);
			
			while(res.next()){
				total_rows = res.getInt("total_rows");
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//sqlLiteDB.closeSQLLiteConnection();
		
		return total_rows;
		
	}
	
	
public static String getcolumnNames(String table_name){
		
		
		String column_names = null;
		String sql = "select * from " + table_name+"_columns";
		try {
			ResultSet res = stmt_sqlite.executeQuery(sql);
			
			while(res.next()){
				column_names = res.getString("columnnames");
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//sqlLiteDB.closeSQLLiteConnection();
		
		return column_names;
		
	}

}
