package com.skilrock.lms.coreEngine.scratchService.userMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

/**
 * <p>
 * This class checks the userId and password and solves the authentication
 * purpose.
 * </p>
 */
public class SupplierRegHelper {

	Connection con = null;

	 

	public String createRetailer(String supname, String addr1, String addr2,
			String retCity, String retCountry, String retState, long Retpin)
			throws LMSException {

		String supName = supname;
		String addrLine1 = addr1;
		String addrLine2 = addr2;
		String city = retCity;
		String country = retCountry;
		String state = retState;
		long pin = Retpin;
		String countryCode = null;
		String stateCode = null;

		try {

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();

			String supplierDetails = QueryManager.selectST3SupplierDetails();
			ResultSet rs5 = stmt4.executeQuery(supplierDetails);
			while (rs5.next()) {

				String supNameDb = rs5.getString(TableConstants.SOM_ORG_NAME);
				if (supNameDb.equalsIgnoreCase(supName)) {
					System.out.println("Supplier Exists !!");
					return "ERROR";
				}

			}

			String getCountryCode = QueryManager.getST3CountryCode()
					+ " where name='" + country + "' ";
			ResultSet rs3 = stmt2.executeQuery(getCountryCode);
			// ResultSet rs3= stmt10.executeQuery("select country_code from
			// st_lms_country_master where name='"+country+"'");
			while (rs3.next()) {
				countryCode = rs3.getString(TableConstants.COUNTRY_CODE);

			}

			String getStateCode = QueryManager.getST3StateCode()
					+ " where name='" + state + "'and country_code = '"
					+ countryCode + "' ";
			ResultSet rs4 = stmt3.executeQuery(getStateCode);
			// ResultSet rs4= stmt11.executeQuery("select state_code from
			// st_lms_state_master where name='"+state+"'and country_code =
			// "+countryCode+" ");
			while (rs4.next()) {
				stateCode = rs4.getString(TableConstants.STATE_CODE);

			}

			String supplierDetail = QueryManager.insertST3SupplierDetails()
					+ " values('" + supName + "','" + addrLine1 + "','"
					+ addrLine2 + "','" + city + "','" + stateCode + "','"
					+ countryCode + "'," + pin + ") ";
			stmt1.executeUpdate(supplierDetail);
			// stmt1.executeUpdate("insert into st_se_supplier_master
			// (name,addr_line1,addr_line2,city,state_code,country_code,pin_code)
			// values('"+supName+"','"+addrLine1+"','"+addrLine2+"','"+city+"','"+stateCode+"','"+countryCode+"',"+pin+")");

			con.commit();
			return "SUCCESS";

		}

		catch (SQLException se) {

			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new LMSException("Error During Rollback", e);
			}
			se.printStackTrace();
			throw new LMSException(se);

		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				see.printStackTrace();
				throw new LMSException("Error During closing connection", see);
			}
		}
		// return null;

	}

}