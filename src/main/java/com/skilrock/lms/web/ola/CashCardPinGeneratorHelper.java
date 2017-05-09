package com.skilrock.lms.web.ola;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import rng.RNGUtilities;

import com.skilrock.lms.beans.CashCardPinBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.ola.SendSMS;
import com.skilrock.lms.coreEngine.ola.common.EncpDecpUtil;
/**
 * This class provide methods to generated cashCard,serialNumbers,PinNumbers,pinEncryption,
 * pinDecryption,RecordInsertion at the time of Pin Generation,write generated pins to file.
 * @author Neeraj Jain
 *
 */

public class CashCardPinGeneratorHelper {
	Connection con = null;
	Connection con1 = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt1 = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	String query = null;
	HashSet<Long> hPin = new HashSet<Long>();//Pin Set
	List<Long> listSerial = new ArrayList<Long>();//Serial Number List
/**
 * this method generated pins for a given quantity 
 * 
 * @param distributorType For which pins has been generated
 * @param denoType   Cash Card Denomination 
 * @param pinQuantity Number of pins to be generated
 * @param partyType   where pins will be redeemed 
 * @param walletId    Pin Wallet
 * @param expiryDate  date till pin is valid
 * @param rootPath		deploy path to save pin files
 * @param pinBean     bean contain pin data
 * @param desKey      DES KEY 
 * @param propKey	  AES KEY		
 * @param userName	 BO user by whom pins has been generated	
 * @param ip		 IP Address from where pin has been generated
 * @return  CashCardPinBean
 * @throws LMSException
 */
	public CashCardPinBean cashCardPinGenerator(String distributorType,
			int denoType, int pinQuantity, String partyType, int walletId,
			String expiryDate, String rootPath, CashCardPinBean pinBean,
			String desKey, String propKey, String userName,String ip,double commRate) throws LMSException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String tableName = "st_ola_cashcard_rm_" + walletId + "_" + denoType; 
	try {
		    con = DBConnect.getConnection();
			con.setAutoCommit(false);
			//Insert Pin Generation Record
			int generatedKey = pinRecord(con,userName,pinQuantity,denoType,walletId,distributorType,ip,commRate,expiryDate);
			if(generatedKey==0){
				pinBean.setSuccess(false);
				System.out.println("Error In Pin Record Insertion ");
				return pinBean;
			}
			boolean tableExits = checkTable(tableName,con ); 
			if(!tableExits)
			{
				query = "create table "+ tableName+ "(serial_number bigint(16) unsigned NOT NULL,pin_number  varchar(50) NOT NULL,amount decimal(10,2) NOT NULL,expiry_date date NOT NULL,"                      
							+ " distributor varchar(10) NOT NULL,player_id varchar(50),lms_transaction_id bigint(20) NOT NULL,tp_transaction_id varchar(20) default NULL,verification_date datetime ,"                 
								+ " verification_status enum('PENDING','DONE') NOT NULL,generation_id int default 0, PRIMARY KEY  (serial_number),FOREIGN KEY (generation_id ) REFERENCES st_ola_pin_status(generation_id ))ENGINE=InnoDB";
				pstmt = con.prepareStatement(query);
				pstmt.executeUpdate();
				
			}
			//Get the lastGeneratedSerial count and Day Count
			String lastGeneratedSerialDayCount[] = getLastGeneratedPin(walletId ,con,"FIXED");
			if(lastGeneratedSerialDayCount[0]==null||lastGeneratedSerialDayCount==null){
				pinBean.setReturnType(null);
				System.out.print("Entry of this type does not exists in  st_ola_pin_generation table...");
				pinBean.setSuccess(false);
				return pinBean;
			}
			
			String lastGeneratedSerial=lastGeneratedSerialDayCount[0];
			String lastGeneratedDayCount=lastGeneratedSerialDayCount[1];
				
			listSerial = randomSerial("FIXED", listSerial, pinQuantity,walletId,lastGeneratedSerial,lastGeneratedDayCount);// generate serial Numbers for given quantity 
			hPin = randomPin(hPin, pinQuantity);// generate pins for given quantity
			List<Long> listPin = new ArrayList<Long>(hPin);//change pins hash set to list
			pinBean.setStartSerialNumber(listSerial.get(0));//Set Starting Serial  Number
			System.out.println("SR. Number " + listSerial.get(0));
			int endPinIndex = listSerial.size()-1;
			pinBean.setEndSerialNumber(listSerial.get(endPinIndex));//Set End Serial Number
			System.out.println("SR. Number " + listSerial.get(endPinIndex));
			// update st_ola_pin_generation
			pstmt3 =con.prepareStatement("update st_ola_pin_generation set last_generated_serial_nbr=? where wallet_id =? and pin_type = ? ");
			pstmt3.setString(1,listSerial.get(endPinIndex).toString().substring(7));
			pstmt3.setInt(2,walletId);
			pstmt3.setString(3,"FIXED");
			pstmt3.executeUpdate();
			Collections.shuffle(listPin);//shuffle the Pin Numbers
			pstmt1 = con.prepareStatement("insert into "
					+ tableName + "(serial_number,pin_number,amount,expiry_date,distributor,lms_transaction_id ,verification_status,generation_id) values(?,?,?,?,?,?,?,?)");
			
			File directory = new File(rootPath + "/PinFiles");
			boolean exists = directory.exists();
			if (!exists) {
				directory.mkdirs();
			}
			
		  File file = File.createTempFile(tableName,".txt",directory);
		  FileWriter writer = new FileWriter(file);
		  writer.write("Serial Number\tPin Number\tAmount\tExpiry Date");
		  writer.write("\r\n");
		  java.sql.Date expiryDate1 = new java.sql.Date(format.parse(expiryDate).getTime());
		  String serial_number1,pin_nbr1,record;
		  // insert generated pin in db and write them into file
		 for (int k = 0; k < pinQuantity; k++) {
				serial_number1 = listSerial.get(k).toString();
				pin_nbr1=listPin.get(k).toString();
				record =""+serial_number1+"\t"+pin_nbr1+"\t\t"+denoType+"\t"+expiryDate1 ;
				writer.write(record);
			    writer.write("\r\n");
			    pin_nbr1=encryptPin(pin_nbr1,desKey,propKey);
			    pstmt1.setLong(1, listSerial.get(k));
				pstmt1.setString(2,pin_nbr1);
				pstmt1.setInt(3, denoType);
				pstmt1.setDate(4, expiryDate1);
				pstmt1.setString(5, distributorType);
				pstmt1.setInt(6,0);//lms_transaction_id default 0 
				pstmt1.setString(7, "PENDING");// status default PENDING
				pstmt1.setInt(8,generatedKey);// Primary Key for record in  st_ola_pin_status
				pstmt1.addBatch();
				if (k>0 && (k % 10000 == 0))
					pstmt1.executeBatch();
			}
			pstmt1.executeBatch();
			con.commit();
			writer.flush();
			System.out.println("Temporary file created at : " + file.getPath());
			pinBean.setFilePath(file.getAbsolutePath());
			writer.close();	
			pinBean.setSuccess(true);
			// send msg to admin user 
			String msg ="Dear admin,This is to inform you that "+userName+" has generated "+pinQuantity+" pins of denomiation "+denoType+" using OLAMS to be distributed at "+distributorType+"  for redemption at "+partyType+" .";
			String plrPhoneNumber="9818505764";//send msg to abhishek sir 
			SendSMS smsSend = new SendSMS(msg, plrPhoneNumber);
			smsSend.setDaemon(true);
			smsSend.start();
			System.out.println(" SMS Sent");	
			return pinBean;

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error During Pin Generation::");
		}finally{
			
			try {
				if(con!=null){
					con.close();
					
				}
			}			
			catch(Exception e){
				e.printStackTrace();
				throw new LMSException(e);
			}
			
		}	
	}
/**
 * This method generate serial number of Format :WALLETID-FIXED(1)/FLEXI(2) BIT-DDD(auto Increment)-2DigitRandomNumber-5DigitSerialNumber previously it was YY-DDD-WALLETID-FIXED(1)/FLEXI(2) BIT-9DigitSerialNumber
 * for a given quantity
 * @param denoType
 * @param h1 serialNumber List
 * @param pinQuantity
 * @param walletId
 * @param lastGeneratedSerial // last count of serialNumber
 * @param lastGeneratedDayCount // Last count of day 
 * @return serialNumber List
 */
public List<Long> randomSerial(String denoType, List<Long> h1,
			int pinQuantity,int walletId,String lastGeneratedSerial,String lastGeneratedDayCount) {
	
		try {			
			String lastSerialStr ;
			StringBuffer strB = new StringBuffer();
			/*SimpleDateFormat sdf = new SimpleDateFormat("yy");
			Calendar cal = Calendar.getInstance();
			int ddd = cal.get(Calendar.DAY_OF_YEAR);
			String yy = sdf.format(cal.getTime());
			String ddd1 = new Integer(ddd).toString();
			int tempyy = Integer.parseInt(yy);			
			if (tempyy / 10 == 0) {
				yy = "0" + "" + tempyy;
			} else {
				yy = tempyy + "";
			}
			if (ddd / 100 == 0) {
				if (ddd / 10 == 0) {
					ddd1 = "00" + ddd;
				} else {
					ddd1 = "0" + ddd;
				}
			}

			else {
				ddd1 = ddd + "";
			}*/
			if(lastGeneratedSerial.equalsIgnoreCase("")){
				lastSerialStr="00000";
			}
			else{
			lastSerialStr = lastGeneratedSerial ;
			}
				
			if (denoType.equalsIgnoreCase("FIXED")) {
				strB = strB.append(walletId+ "1" + lastGeneratedDayCount+"00"+lastSerialStr); // For Fixed 1: append walletId,Fixed Bit,Day Count,5Digit Serial Number
			}
			else {
				strB = strB.append( walletId + "2" +lastGeneratedDayCount+"00"+lastSerialStr);//For Flexi: append walletId,Flexi Bit,Day Count,5Digit Serial Number
				}			
			long lastSerial = Long.parseLong(strB.toString());			
			while (h1.size() != pinQuantity) {
				lastSerial++;
				StringBuffer sb= new StringBuffer(lastSerial+"");
				int rnumber= RNGUtilities.generateRandomNumber(10,99);
				sb.replace(5,7,rnumber+"");// 2digit Random Number
				h1.add(Long.parseLong(sb.toString()));
				System.out.println("SR. Number " + sb.toString());
			}
		
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return h1;
	}
/**
 * This method generate pin number of Format :4DigitRandomNumber previously is was DDD-13DigitRandomNumber
 * for a given quantity
 * @param h1 hash set 
 * @param pinQuantity number of pins to be generated
 * @return Set of random Numbers
 * 
 */
	
public HashSet<Long> randomPin(HashSet<Long> h1, int pinQuantity) {
		//String randomPin = null;
		try {
			long rnumber;

			/*SimpleDateFormat sdf = new SimpleDateFormat("yy");
			Calendar cal = Calendar.getInstance();
			int ddd = cal.get(Calendar.DAY_OF_YEAR);
			String ddd1 = new Integer(ddd).toString();
			if (ddd / 100 == 0) {
				if (ddd / 10 == 0) {
					ddd1 = "00" + ddd;
				} else {
					ddd1 = "0" + ddd;
				}

			}else {
				ddd1 = ddd + "";
			}*/

		while(h1.size()!=pinQuantity) {

				StringBuffer strB = new StringBuffer();
				rnumber = RNGUtilities.generateRandomNumber(1000l,
						9999l);
				//strB.append(ddd1+rnumber);//append  year,Month, 13 digit random number 
				strB.append(rnumber);//append 4 digit random number 
				h1.add(Long.parseLong(strB.toString()));
			//	System.out.println("Pin Number " + strB);

			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return h1;
	}
/**
 * Write Pin Data to file
 * @param pinBean
 * @param rootPath
 * @return FileInputStream 
 * @throws LMSException
 */	
public FileInputStream cashCardPinsDownload(CashCardPinBean pinBean,String rootPath) throws LMSException{

	try{
		FileInputStream fstream = new FileInputStream(pinBean.getFilePath());
		 System.out.print("Writing Pins to File ... ");
        return fstream;
	   	
	}catch(Exception e){
		e.printStackTrace();
		throw new LMSException("Error In File Generation");
	}finally {
		
		try {
			if(con1!=null){
				con1.close();
				
			}
		}
		catch (Exception e){
			
			e.printStackTrace();
			throw new LMSException(e);
		}
		
	}
	
}

/**
 * Check weather table of given tableName exists or not 
 * @param tableName
 * @param con
 * @return true/false
 */
public boolean checkTable(String tableName, Connection con) {

		try {
			ResultSet rs = con.getMetaData().getTables(null, null, tableName,null);
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}
/**
 * 
 * @param walletId
 * @param con
 * @param pin_type Fixed/Flexi
 * @return Return Last Generated Pin Serial  Number and Day Count
 */
	
	
public String[] getLastGeneratedPin(int walletId,Connection con,String pin_type) {
		
		String tempQ = "select last_generated_serial_nbr,last_day_serial_nbr from st_ola_pin_generation where wallet_id='"+walletId+"' and pin_type='"+pin_type+"'" ;
		try {
			pstmt2 = con.prepareStatement(tempQ);
			ResultSet rs =pstmt2.executeQuery();
			String lastGeneratedSerialDayCount[]=new String[2];
			if(rs.next()){
				lastGeneratedSerialDayCount[0]=rs.getString("last_generated_serial_nbr");
				lastGeneratedSerialDayCount[1]=rs.getString("last_day_serial_nbr");
				return lastGeneratedSerialDayCount;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
/**
 * encrypt given pin number
 * @param pin_nbr
 * @param desKey DES Key
 * @param propKey AES Key
 * @return Encrypted Pin
 */
public String encryptPin(String pin_nbr,String desKey,String propKey){
	String enPin =null;
	try {
			byte[]propkey = Base64.decodeBase64(propKey.getBytes());
			byte[]deskey = Base64.decodeBase64(desKey.getBytes());
			byte[] decoAesKey = EncpDecpUtil.decodeDES(deskey,propkey);
			//	key(propkey,deskey,decoAesKey);  //call me to know keys 
			byte[]value=pin_nbr.getBytes();
			byte[] encodedPin = EncpDecpUtil.encodeAES(decoAesKey,value);
			enPin = new String(Base64.encodeBase64(encodedPin));
			//String dePin = decryptPin(enPin);
			System.out.println("dePin"+enPin);
	}
	catch(Exception e){
			e.printStackTrace();
			System.out.println("Error In Pin Encryption");
			
	}
	return enPin;
	

}
/**
 * Decrypt Given Pin
 * @param enPin encrypted Pin 
 * @param desKey DES Key
 * @param propKey AES Key
 * @return decrypt pin
 */

public String decryptPin(String enPin,String desKey,String propKey){
	
	byte[]propkey = Base64.decodeBase64(propKey.getBytes());
	byte[]deskey = Base64.decodeBase64(desKey.getBytes());
	byte[] decoAesKey = EncpDecpUtil.decodeDES(deskey,propkey);
	byte[]pin = Base64.decodeBase64(enPin.getBytes());
	byte[] decodedPin=EncpDecpUtil.decodeAES(decoAesKey,pin);

	String st1 = new String(decodedPin);
	System.out.println("decoded Pin"+st1);
		
	return st1;
	
	
}
// this method can be used to check the value of AES and DES Keys 
// to know the keys uncomment key function calling line in encryptPin Function
private void key(byte[] propkey, byte[] deskey, byte[] decoAesKey) {
	String nothing = new String(propkey);
	String aes= new String(deskey);
	String des = new String(decoAesKey);
	System.out.println("str1 :"+nothing+"str2 :"+aes+"str3 :"+des);
	
}
/**
 * this function insert pin generation record
 * @param con
 * @param userName Bo username
 * @param pinQuantity number of pins has been generated
 * @param denoType denomination amount
 * @param walletId
 * @param distributorType For which pins has been generated
 * @param ip  IP Address from where pin has been generated
 * @return generation_id 
 */
private int pinRecord(Connection con,String userName,int pinQuantity,int denoType,int walletId,String distributorType,String ip,double commRate,String expiryDate){
	try{
		String query =null;
		Calendar cal = Calendar.getInstance();
		Timestamp currentTime = new Timestamp(cal.getTime().getTime());
		query="insert into st_ola_pin_status(wallet_id,amount,sale_comm_rate,no_of_pin_generated,generated_by_user,generated_for,generated_from_ip,generation_time,expiry_date) values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1,walletId);
		ps.setInt(2, denoType);
		ps.setDouble(3,commRate);
		ps.setInt(4,pinQuantity);
		ps.setString(5,userName);
		ps.setString(6,distributorType );
		ps.setString(7,ip);
		ps.setTimestamp(8,currentTime);
		ps.setString(9, expiryDate);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		int generationId=0;
		if(rs.next()){
			generationId=rs.getInt(1); 
		}
		if(generationId!=0){
			System.out.println("Pin Record"+walletId+":"+denoType+":"+pinQuantity+":"+userName+":"+distributorType+":"+ip+":"+currentTime+" Inserted");
			return generationId;
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return 0;
}
}
