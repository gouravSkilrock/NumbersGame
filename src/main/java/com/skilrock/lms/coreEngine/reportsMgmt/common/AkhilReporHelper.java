package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.AgtLedAccDetailsBean;
import com.skilrock.lms.beans.RetLedAccDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.web.reportsMgmt.common.GraphReportAction;
import com.skilrock.lms.web.reportsMgmt.common.LedgerAction;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;

public class AkhilReporHelper {

	
	
	
	public static void main(String p []){
		
		
		DBConnect dbConnect=null;
		Connection connection=null;
		JasperReport jasperReport;/* JasperReport is the object that holds our compiled jrxml file */
		JasperPrint jasperPrint;/* JasperPrint is the object contains report after result filling process */
		byte bytes[] = null;
	  
		dbConnect = new DBConnect();
		connection = dbConnect.getConnection();
		try{
			HashMap jasperParameter = new HashMap();
		
		Statement stmt = connection.createStatement();
		// jrxml compiling process
	//	jasperReport =
		JasperCompileManager.compileReportToFile("D://akhil/raw/VATAgent.jrxml","D://akhil/compiled/VATAgent.jasper");
		
		//jasperPrint = JasperFillManager.fillReport("C://akhil/ak.jsaper",jasperParameter, connection);
		
		//JasperExportManager.exportReportToPdfFile(jasperPrint, "D://akhil/ak.pdf");
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally {
			try {

				System.out.println(" closing connection  ");
				if (connection!=null){
					connection.close();
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	
	
	}

}
