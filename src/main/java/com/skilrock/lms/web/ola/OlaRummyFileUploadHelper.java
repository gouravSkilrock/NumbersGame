package com.skilrock.lms.web.ola;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.skilrock.lms.beans.OlaExPinBean;
import com.skilrock.lms.beans.OlaExPinSummaryBean;
import com.skilrock.lms.common.db.DBConnect;
/**
 * This class provide method to Read Excel File 
 * @author Neeraj Jain
 *
 */
public class OlaRummyFileUploadHelper {
/**
 * This method readExcel file specific for Rummy Withdrawal Request File
 * Insert data into st_ola_rummy_withdrawal_rep table
 * @param file excel File
 * @return true/false
 * 
 */
	public String readExcel(File file,String approveDate,String requestDate) {

		InputStream inputStream = null;
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		String query =null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		POIFSFileSystem fileSystem = null;

		try {
			con.setAutoCommit(false);
					
			java.sql.Date appDate = new java.sql.Date(sdf.parse(approveDate).getTime());
			java.sql.Date reqDate = new java.sql.Date(sdf.parse(requestDate).getTime());
			
			System.out.println("--Reading Starts--");
			String lastReqDateQuery ="select request_date from st_ola_rummy_withdrawal_rep order by request_date desc limit 1";
			ps1=con.prepareStatement(lastReqDateQuery);
			ResultSet rs =ps1.executeQuery();
			if(rs.next()){
				if(reqDate.compareTo(rs.getDate("request_date"))==0){
					System.out.println("File already has been uploaded for this date");
					return "File already has been uploaded for this date";
				}
				
			}
			inputStream = new FileInputStream(file);
			fileSystem = new POIFSFileSystem(inputStream);

			HSSFWorkbook workBook = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = workBook.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			boolean rowA = true;
			boolean cellA = true;
			ArrayList strArr = new ArrayList();

			while (rows.hasNext() && rowA) {
				HSSFRow row = (HSSFRow) rows.next();
				StringBuilder sb = new StringBuilder();
				// display row number in the console.
				System.out.println("Row No.: " + row.getRowNum());

				// once get a row its time to iterate through cells.
				int firstCell = row.getFirstCellNum();
				int lastCell = row.getLastCellNum();
				Iterator<Cell> cells = row.cellIterator();

				while ((firstCell<=lastCell) && cellA) {
					HSSFCell cell = row.getCell((short)firstCell);
					if(cell==null){
						firstCell++;
						continue;
					}
					System.out.println("Cell No.: " + cell.getCellNum());

					/*
					 * Now we will get the cell type and display the values
					 * accordingly.
					 */
					HSSFDataFormat hs;

					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC: {

						// cell type numeric.
						long i = (long) cell.getNumericCellValue();
						sb.append(String.valueOf(i).trim() + "N@@@xt");
						System.out.println("Numeric value: "
								+ String.valueOf(i));

						break;
					}

					case HSSFCell.CELL_TYPE_STRING: {

						// cell type string.
						if (cell.getStringCellValue().equalsIgnoreCase(
								"Not Approved")) {
							rowA = false;
							cellA = false;
						}
						sb.append(cell.getStringCellValue().trim() + "N@@@xt");
						System.out.println("String value: "
								+ cell.getStringCellValue());
						
						break;
					}

					default: {
						sb.append(cell.getStringCellValue() + "N@@@xt");
						// types other than String and Numeric.
						System.out.println("Type not supported."
								+ cell.getCellType());

						break;
					}
					}
						firstCell++;
				}
				strArr.add(sb.toString());

			}

			// Here Read Approved Withdrawal Data
			for (int i = 0; i < strArr.size(); i++) {
				String[] arr = strArr.get(i).toString().split("N@@@xt");
				
				if (arr.length == 11) {
					if(arr[9].trim().equalsIgnoreCase("IFSC CODE")||arr[0].trim().equalsIgnoreCase("User id")||arr[5].trim().equalsIgnoreCase("Bank Name")){
						continue;//Ignore Header Row Of ExcelFile
					}else if(arr[0].trim().equalsIgnoreCase("")&&arr[1].trim().equalsIgnoreCase("")&&arr[10].trim().equalsIgnoreCase("")){
						continue;
					}
					
					
							
						query = "insert into st_ola_rummy_withdrawal_rep(account_id,user_id,isBind,request_date,plr_email,plr_phone,plr_banking_name," +
							"bank_name,bank_account_nbr,bank_branch_name,bank_branch_city,ifs_code,amount,transfer_mode,approval_date,rms_process_status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
						ps = con.prepareStatement(query);
						ps.setString(1,arr[0]);
						ps.setString(2,arr[1]);
						String isBind = checkPlrBinding(con,arr[1]);
						if(isBind!=null){
						ps.setString(3,isBind);
						}
						else{
							System.out.println("Some Error in Bind Player Checking");
							return "Some Error In File Upload";
						}
						ps.setDate(4,reqDate);
						ps.setString(5,arr[2]);
						ps.setString(6,arr[3]);
						ps.setString(7,arr[4]);
						ps.setString(8,arr[5]);
						ps.setString(9,arr[6].replace("“","").replace("”","").replaceAll("\"","").trim());
						ps.setString(10,arr[7]);
						ps.setString(11,arr[8]);
						ps.setString(12,arr[9]);
						ps.setDouble(13,Double.parseDouble(arr[10]));
						ps.setString(14,"ONLINE" );
						ps.setDate(15,appDate);
						ps.setString(16,"PENDING" );
						ps.executeUpdate();
				}
				
			}
		con.commit();
		System.out.println("--Reading Ends--");
		return "true";	
		}catch (FileNotFoundException e) {
			System.out.println("File not found in the specified path.");
			e.printStackTrace();
			return "Some Error In File Upload";
		}catch (Exception e) {
			e.printStackTrace();
			return "Some Error In File Upload";
		}finally{
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
				}
			}
		}
	}
	
/**
 * This Method check whether player is OLA Bind or Not 
 * @param con Connection from readExcel Method
 * @param plr_id Player's User Name at www.khelplayrummy.com
 * @return YES(if PlayerBind to OLA)/NO/NULL
 */	
public String checkPlrBinding(Connection con,String plr_id){
	try{
		String query = " select player_id from	(select player_id,wallet_id from st_ola_affiliate_plr_mapping where player_id=?)pm "
						+ "inner join	(select wallet_id from st_ola_wallet_master where wallet_name=?)wm on pm.wallet_id=wm.wallet_id";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, plr_id);
		ps.setString(2,"Rummy");// Rummy Wallet 
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			return "YES";
		}
		else{
			return "NO";
		}
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}

public ArrayList<Object>  readTxtFile(File file){
	HashMap<String, OlaExPinSummaryBean> pinSummaryBeanMap = new HashMap<String,OlaExPinSummaryBean>();
	ArrayList<OlaExPinBean> olaExPinBeanList = new ArrayList<OlaExPinBean>();
	ArrayList<Object> list = new ArrayList<Object>();
	try {
		BufferedReader br= new BufferedReader(new FileReader(file));
		String line =null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		HashSet<String> hs = new HashSet<String>();
		
	while((line=br.readLine())!=null){
		OlaExPinSummaryBean pinSummaryBean = new OlaExPinSummaryBean();
		OlaExPinBean     olaExPinBean = new OlaExPinBean();
			if(line.trim().equalsIgnoreCase("")){
				continue;
			}
			String dataColums[] =line.split("\t");
			for(int i=0;i<dataColums.length;i++){
					System.out.println("fileData"+dataColums[i]);
			}
			if(dataColums[0].equalsIgnoreCase("Serial Number")){
				continue;
			}else{
				String temp = dataColums[3]+"N@xt"+dataColums[4];
				if(hs.add(temp)){
					pinSummaryBean.setAmount(Integer.parseInt(dataColums[3]));
					pinSummaryBean.setExpiryDate(dataColums[4]);
					pinSummaryBean.setTotalPin(1);
					pinSummaryBeanMap.put(temp, pinSummaryBean);
				}else{
					pinSummaryBean=pinSummaryBeanMap.get(temp);
					pinSummaryBean.setTotalPin(pinSummaryBean.getTotalPin()+1);
					pinSummaryBeanMap.put(temp, pinSummaryBean);
				}
				olaExPinBean.setSerialNumber(dataColums[0]);
				olaExPinBean.setPinNumber(dataColums[1]);
				olaExPinBean.setAmount(Integer.parseInt(dataColums[3]));
				olaExPinBean.setExpiryDate(new java.sql.Date(sdf.parse(dataColums[4]).getTime()));
				olaExPinBeanList.add(olaExPinBean);
			}
			
		
			
			
		}
	System.out.println("Iterate Summary Bean");
	ArrayList<String> arrList = new ArrayList<String>(hs);
	for(int i=0;i<arrList.size();i++){
		OlaExPinSummaryBean pinSummaryBean = new OlaExPinSummaryBean();
		pinSummaryBean=pinSummaryBeanMap.get(arrList.get(i));
		System.out.println("Amount"+pinSummaryBean.getAmount()+"Amount"+pinSummaryBean.getExpiryDate()+"Amount"+pinSummaryBean.getTotalPin());
	}
		
	}catch(Exception e){
		e.printStackTrace();
		
	}
	list.add(pinSummaryBeanMap);
	list.add(olaExPinBeanList);
	return list;
}
static final Comparator<OlaExPinBean> ORDER = 
    new Comparator<OlaExPinBean>() {
public int compare(OlaExPinBean e1, OlaExPinBean e2) {
return ((Integer)e2.getAmount()).compareTo((Integer)e1.getAmount());
}
};	
public boolean expirePins(ArrayList<OlaExPinBean> olaExPinBeanList,String desKey,String propKey){
	Connection con = DBConnect.getConnection();
	StringBuffer inSb = new StringBuffer();
	HashMap<Integer,ArrayList<OlaExPinBean>> olaExPinBeanMap =new HashMap<Integer,ArrayList<OlaExPinBean>>();
	Collections.sort(olaExPinBeanList,ORDER);
	StringBuffer srsb = new StringBuffer();
	StringBuffer pinsb = new StringBuffer();
	StringBuffer exsb = new StringBuffer();
	srsb.append(" serial_number in( " );
	pinsb.append(" pin_number in( " );
	exsb.append(" expiry_date in( " );
	CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
	String query="";
	for(int i=1;i<olaExPinBeanList.size();i++){
		OlaExPinBean bean =olaExPinBeanList.get(i);
		srsb.append(olaExPinBeanList.get(i-1).getSerialNumber()+",");
		pinsb.append("'"+helper.encryptPin(olaExPinBeanList.get(i-1).getPinNumber(),desKey,propKey)+"',");
		exsb.append("'"+olaExPinBeanList.get(i-1).getExpiryDate()+"',");
		if(bean.getAmount()!=olaExPinBeanList.get(i-1).getAmount()){
			query="select * from st_ola_cashcard_rm_"+olaExPinBeanList.get(i-1).getSerialNumber().substring(0,1)+"_"+olaExPinBeanList.get(i-1).getAmount()+" " +
					"where "+srsb.deleteCharAt(srsb.length()-1)+") and "+pinsb.deleteCharAt(pinsb.length()-1)+") and "+exsb.deleteCharAt(exsb.length()-1)+")";
			srsb.replace(0, srsb.length()," serial_number in(");
			pinsb.replace(0, pinsb.length()," pin_number in(");
			exsb.replace(0, exsb.length()," expiry_date in( ");
			System.out.println("query"+query);
		}
		if(i==olaExPinBeanList.size()-1){
			query="select * from st_ola_cashcard_rm_"+olaExPinBeanList.get(i-1).getSerialNumber().substring(0,1)+"_"+olaExPinBeanList.get(i-1).getAmount()+" " +
			"where "+srsb.deleteCharAt(srsb.length()-1)+") and "+pinsb.deleteCharAt(pinsb.length()-1)+") and "+exsb.deleteCharAt(exsb.length()-1)+")";
			System.out.println("end"+query);
		}
		System.out.println("Serail Number:"+bean.getSerialNumber()+"Pin Number"+bean.getPinNumber()+"Amount"+bean.getAmount()+"Date"+bean.getExpiryDate());
	}
	
	  try{
	    	
	    	String query1 = "";//"insert into st_ola_cashcard_rm_"+bean.getSerialNumber().substring(0)+"_"+bean.getAmount()+" values(?,?,?,?,?,?,?,?,?,?,?)";
	    	System.out.println("Query"+query);
	      	PreparedStatement ps = con.prepareStatement(query);
	      	
	      	
	      	
	    }catch(Exception e){
	    	
	    }	
	return true;
	
}
	
}
