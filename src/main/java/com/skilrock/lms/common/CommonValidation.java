package com.skilrock.lms.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.opensymphony.xwork2.validator.ValidationException;
/**
 * Validations
 * @author Administrator
 *
 */
public class CommonValidation {
	/**
	 * checks if a string is empty
	 * 
	 * @param value
	 * @return
	 */
	private static final Log logger = LogFactory.getLog(CommonValidation.class);
	
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String AMOUNT_PATTERN="^[0-9]*[.]{0,1}[0-9]*$";
	private static final Pattern mobilePattern = Pattern.compile("\\d{10}");
	
	
	
	
	public static boolean isEmpty(String value) {
		return (value == null || value.equals(""));

	}
	
	/**
	 * checks if a Integer is empty
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Integer value) {	
		return (value == null || value<=0);
	}
	
	/**
	 * checks if a date is empty
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Date value) {
		return (value == null);
	}

	/**
	 * checks if a date is empty
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Double value) {
		return (value<=0.0);
	}

	/**
	 * checks if a date is empty
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Long value) {
		return (value == null);
	}
	
	public static boolean isEmpty(Character value) {
		return (value == null);
	}

	/**
	 * checks if the value is a integer
	 * 
	 * @param value
	 * @return
	 * @throws ValidationException
	 */
	public static boolean isInteger(String checkStr) {

		try {
			Integer.parseInt(checkStr);
			return true; // Did not throw, must be a number
		} catch (NumberFormatException err) {
			return false; // Threw, So is not a number
		}
	}

	
	/**
	 * checks if the value is a Long
	 * 
	 * @param value
	 * @return
	 * @throws ValidationException
	 */
	public static boolean isLong(String checkStr) {

		try {
			Long.parseLong(checkStr);
			return true; // Did not throw, must be a Long
		} catch (NumberFormatException err) {
			return false; // Threw, So is not a number
		}
	}
	
	/**
	 * checks if the value is a integer
	 * 
	 * @param value
	 * @return
	 * @throws ValidationException
	 */
	public static boolean isDouble(Double checkStr) {

		return (!Double.isNaN(checkStr));		
	}
 
	public static boolean isDouble(String checkStr, boolean isSpace) {

		return Pattern.matches("[0-9]+(\\.[0-9])?$", checkStr);
	}
	/**
	 * validates a email
	 * 
	 * @param email
	 * @return boolean
	 */
	public static boolean isValidEmail(final String email) {
		return Pattern.matches(EMAIL_PATTERN, email);
	}	
	
	
	
	

	/**
	 * validates a date
	 * 
	 * @param date
	 * @return
	 */
	public static boolean validateDate(Date date) {
		try {

			String format = "EEE MMM dd HH:mm:ss Z yyyy";
			String strDate = date.toString();
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			sdf.parse(strDate);			
		} catch (ParseException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}

		return true;
	}

	
	public static boolean validateDate2(String date) {
		try {

			String format = "MM/dd/yyyy";
			
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			sdf.parse(date);			
		} catch (ParseException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}

		return true;
	}
	
	
	/**
	 * selected date is before current date
	 * @param arg
	 */
	public static boolean beforeCurrentDate(Date date){

		if(checkCurrentDate(date))
		{
			return false;
		}
		else{
			if(date.before(new Date())){
				return true;
			}else{
				return false;
			}

		}
	}

	public static boolean checkCurrentDate(Date date){

		SimpleDateFormat sdf = new SimpleDateFormat ( "MM/dd/yyyy" ) ;
		Date currentDate = new Date();
		currentDate = convertString2Date(sdf.format(currentDate));

//		Date userDate = convertString2Date(sdf.format(date));

		if(date.compareTo(currentDate)==0){
			return true;
		}else {
			return false;
		}

	}

	/**
	 * selected date is after current date
	 * @param arg
	 */
	public static boolean afterCurrentDate(Date date){		

		if(checkCurrentDate(date)){
			return false;
		}
		else{
			if(date.after(new Date())){
				return true;
			}else{
				return false;
			}

		}

	}

	/**
	 * compare dates 
	 * @param arg
	 */
	public static boolean compareDates(Date fromdate, Date todate){

		if(fromdate.compareTo(todate)<0){
			return true;
		}
		else if(fromdate.compareTo(todate)>0){
			return false;
		}
		else{
			return true;
		}

	}

	/**
	 * Calculates the number of days between two calendar days com a manner
	 * which is independent of the Calendar type used.
	 *
	 * @param d1    The first date.
	 * @param d2    The second date.
	 *
	 * @return      The number of days between the two dates.  Zero is
	 *              returned if the dates are the same, one if the dates are
	 *              adjacent, etc.  The order of the dates
	 *              does not matter, the value returned is always >= 0.
	 *              If Calendar types of d1 and d2
	 *              are different, the result may not be accurate.
	 */
	static int getDaysBetween (Date date) {
		GregorianCalendar d1 = new GregorianCalendar();
		d1.setTime(date);

		GregorianCalendar d2 = new GregorianCalendar();
		d2.setTime(new Date());

		if (d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
			GregorianCalendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(GregorianCalendar.DAY_OF_YEAR) -
		d1.get(GregorianCalendar.DAY_OF_YEAR);
		int y2 = d2.get(GregorianCalendar.YEAR);
		if (d1.get(GregorianCalendar.YEAR) != y2) {
			d1 = (GregorianCalendar) d1.clone();
			do {
				days += d1.getActualMaximum(GregorianCalendar.DAY_OF_YEAR);
				d1.add(GregorianCalendar.YEAR, 1);
			} while (d1.get(GregorianCalendar.YEAR) != y2);
		}
		return days;
	} // getDaysBetween()



	/**
	 * check if age is above 16 years
	 * @param arg
	 */
	public static boolean validateAge(Date date){

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		int userday = cal.get(Calendar.DAY_OF_MONTH);
		int usermonth = cal.get(Calendar.MONTH)+1;
		int useryear = cal.get(Calendar.YEAR);

		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.setTime(new Date());
		int currentday = cal1.get(Calendar.DAY_OF_MONTH);
		int currentmonth = cal1.get(Calendar.MONTH)+1;
		int currentyear = cal1.get(Calendar.YEAR);

		if(currentyear-useryear<16){			
			return false;
		}
		if(currentyear-useryear>16){
			return true;
		}
		if(currentyear-useryear==16){

			if(currentmonth>usermonth){
				return true;
			}
			if(currentmonth<usermonth){
				return false;
			}
			if(currentmonth==usermonth){

				if(currentday>userday){
					return true;
				}
				if(currentday<userday){
					return false;
				}
				if(currentday==userday){
					return false;
				}	
			}
		}	
		return true;

	}

	/**
	 *  checks whether the string passed is 
	 *  alphabetic with spaces
	 * @param value
	 * @return
	 */
	public static boolean isAlphabetic(String value, boolean spaces){		

		if(spaces){
			return Pattern.matches("[a-zA-Z ]+", value);
		}
		else{
			return Pattern.matches("[a-zA-Z]+", value);
		}
	}
	
	public static boolean isAlphabeticWithMinus(String value, boolean spaces){		

		if(spaces){
			return Pattern.matches("[-a-zA-Z ]+", value);
		}
		else{
			return Pattern.matches("[-a-zA-Z]+", value);
		}
	}

	/**added by Kapil
	 *  checks that the string passed must be
	 *  alpahanumeric
	 * @param value
	 * @return
	 */
	public static boolean testPassword(String value,String strMatch){
		
		return Pattern.matches(strMatch, value);
		

	}
	
	
	/**
	 *  checks whether the string passed is 
	 *  alpahanumeric with spaces
	 * @param value
	 * @return
	 */
	public static boolean isAlphanumeric(String value,boolean spaces){
		if(spaces){
			return Pattern.matches("[0-9A-Za-z._ ]+", value);
		}
		else{
			return Pattern.matches("[0-9A-Za-z._]+", value);
		}

	}
	/**
	 *  checks whether the string passed is 
	 *  alpahanumeric with spaces
	 * @param value
	 * @return
	 */
	public static boolean isContainsInteger(String value,boolean spaces){
		logger.info("isContainsInteger");
		if(spaces){
			return Pattern.matches("[a-zA-Z]*[0-9]+[a-zA-Z0-9]*", value);
		}
		else{
			return Pattern.matches("[a-zA-Z]*[0-9]+[a-zA-Z0-9]*", value);
		}

	}
	
	
	
	/**
	 * 
	 * @param value
	 * @param spaces
	 * @return
	 */
	
	public static boolean isAlphanumericNoChar(String value,boolean spaces){		

		if(spaces){
			return Pattern.matches("[0-9A-Za-z ]*", value);
		}
		else{
			return Pattern.matches("[0-9A-Za-z]*", value);
		}
	}

	/**
	 *  checks whether the string passed is 
	 *  numeric with spaces
	 * @param value
	 * @return
	 */
	public static boolean isNumeric(String value,boolean spaces){		

		if(spaces){
			return Pattern.matches("[0-9. ]+", value);
		}
		else{
			return Pattern.matches("[0-9.]+", value);
		}

	}
	
	public static boolean isNumericWithoutDot(String value,boolean spaces){		

		if(spaces){
			return Pattern.matches("[0-9 ]+", value);
		}
		else{
			return Pattern.matches("[0-9]+", value);
		}

	}
	
	public static boolean isValidAmount(final double amount){
		try{
		final String amt=String.valueOf(amount);
		return Pattern.matches(AMOUNT_PATTERN, amt);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @param amount
	 * @return
	 */
	public static boolean isValidAmount(final String amount){
		
		return Pattern.matches(AMOUNT_PATTERN, amount);
		
	}
	
/**
 * 
 * @param value
 * @param spaces
 * @return
 */

	public static boolean isValidCity(String value, boolean spaces ){

		if(spaces){
			return Pattern.matches("[0-9A-Za-z\\ ]+", value);
		}
		else{
			return Pattern.matches("[0-9A-Za-z\\ ]+", value);
		}
	}
	
	
	
	
	public static boolean isValidAddress(String value){

		return Pattern.matches("[ 0-9A-Za-z]+[ 0-9A-Za-z.,-@_()/]*", value);
	}
	
	
/**
 * 
 * @param datestring
 * @return
 */
	public static Date convertString2Date(String datestring){

		SimpleDateFormat sdf = new SimpleDateFormat ( "MM/dd/yyyy" ) ;
		Date date=null;		
		try	{							
			date = sdf.parse(datestring);				

		}
		catch (java.text.ParseException pe)	{
			pe.printStackTrace();
		}	
		return 	date;
	}
/**
 * 
 * @param date
 * @return
 */
	public static String convertDate2String(Date date){

		String datestring = null;
		SimpleDateFormat sdf = new SimpleDateFormat ( "MM/dd/yyyy" ) ;
		if(date!=null){
			datestring = sdf.format(date);
		}
		return datestring;
	}
	
	public static String getTodayDate(){
		
		Date date = Calendar.getInstance().getTime();		
		 DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		 String today = formatter.format(date);
		 
		 return today;
		 
	}
	

	/**
	 * @param flag
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
//	public static boolean  isRequestIpAllowed(String requestIP)
//			throws FileNotFoundException, IOException {
//		boolean flag = false;
//		//*********  Player's GeoIP Location ***************************
//		CountryLookupTest lookup = new CountryLookupTest();
//		String playerLocation= lookup.getPlayerLocation(requestIP);
//		logger.info("Player Location is :--- -- --- "  +playerLocation);	
//		//Chnage here to get Country list whish status is Active
//		 ArrayList<String>  arrCountryListactive = getCountrylistActive();
////		 flag=arrCountryListactive.contains(playerLocation.toUpperCase());
//		 for (String country : arrCountryListactive) {
//			if(country.equalsIgnoreCase(playerLocation)){				
//				flag = true;
//				break;
//			}else {
//				flag = false;
//			}
//		}
//		return flag;
//	}
	/**
	 * GeoIP
	 * Country list if ACtive
	 * @return the countrylist
	 */
//	public static ArrayList<String> getCountrylistActive() {
//		logger.info("inside getCountrylistActive:---------------");
//		ServiceRequest sreq = new ServiceRequest();
//		sreq.setServiceName(ServiceName.GETCOUNTRYLIST_ACTIVE);
//		sreq.setServiceData(null);
//		sreq.setServiceMethod(ServiceName.GETCOUNTRYLIST_ACTIVE_METHOD);				
//		ServiceController controller = new ServiceController();					
//		ServiceResponse sresponse = controller.doService(sreq);		
//		return (ArrayList)sresponse.getResponseData();
//	}
	
	public static boolean containsSpecChars(String s){
		char specChars[] = {'\'','"',';','<','>','(',')','=','#','%','&','$'};
		for (char c :specChars){
			if(s.indexOf(c)!=-1){
				return true;
			}
		}
		return false;
	}

	public static boolean isValidPhoneNumber(String  phonNbr){
	     Matcher matcher = mobilePattern.matcher(phonNbr);
		if(phonNbr==null||!matcher.matches()){
			logger.info("Invalid PhoneNumber"+phonNbr);
			 return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param arg
	 */
	public static void main(String arg[]) {
		
		
		//System.out.println(isValidEmail("sumit.singla@jj.com"));
		//System.out.println(isValidAmount(0.13));
		System.out.println(!isEmpty(0.0));
		System.out.println(isEmpty(-1));
		/*String eml = "miltoncse00@yahoo.com";
		logger.info("email check "+ eml + " " + validateEmail(eml));
		
		eml = "2miltoncse00@yahoo.com";
		logger.info("email check "+ eml + " " + validateEmail(eml));
		
		eml = "milton cse00@yahoo.com";
		logger.info("email check "+ eml + " " + validateEmail(eml));
		
		eml = "milton_cse@yahoo.com";
		logger.info("email check "+ eml + " " + validateEmail(eml));

		eml = "milton_cse_00@yahoo.com";
		logger.info("email check "+ eml + " " + validateEmail(eml));
		
		eml = "milton_cse_@yahoo.com";
		logger.info("email check "+ eml + " " + validateEmail(eml));
		
		eml = "milton.case_00_00@yahoo.com";
		logger.info("email check "+ eml + " " + validateEmail(eml));

		eml = "milton.cas.e_00_00@yahoo.com";
		logger.info("email check "+ eml + " " + validateEmail(eml));
		
		eml = "milton.cas.e_00_00@yahoo.co.in";
		logger.info("email check "+ eml + " " + validateEmail(eml));	
		
		eml = "milton.cas.e_00_00.@yahoo.co.in";
		logger.info("email check "+ eml + " " + validateEmail(eml));	*/
		
	/*	String strmatch="^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,20}$";
		String value = "111111d";
		System.out.println(Pattern.matches(strmatch, value));
		String date = "01/01/19";
		System.out.println("hahahaaaa");
		System.out.println(validateDate2(date));*/
	}

}
