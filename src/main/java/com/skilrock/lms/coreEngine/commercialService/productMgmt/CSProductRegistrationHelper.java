package com.skilrock.lms.coreEngine.commercialService.productMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CSProductBean;
import com.skilrock.lms.common.db.DBConnect;

public class CSProductRegistrationHelper {
	static Log logger = LogFactory.getLog(CSProductRegistrationHelper.class);
	public String getActiveProductCategories(){
		String html = "<select class=\"option\" name=\"catId\" id=\"catId\"><option value=\"-1\">--please select--</option>";
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt;
		ResultSet rs;
		try{
			String query = "select category_id, category_code, description from st_cs_product_category_master where status = 'Active'";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()){
				html += "<option value=\""+ rs.getInt("category_id") +"\">"+ rs.getString("category_code") +"</option>";
			}
			html += "</select>";
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return html;
	}
	
	public String getActiveOperators(){
		String html = "<select class=\"option\" name=\"operatorCode\" id=\"operatorCode\"><option value=\"-1\">--please select--</option>";
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt;
		ResultSet rs;
		try{
			String query = "select operator_code, operator_name from st_cs_operator_master where status = 'Active'";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()){
				html += "<option value=\""+ rs.getString("operator_code") +"\">"+ rs.getString("operator_name") +"</option>";
			}
			html += "</select>";
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return html;
	}
	
	public String getActiveCircles(){
		String html = "<select class=\"option\" name=\"circleCode\" id=\"circleCode\"><option value=\"-1\">--please select--</option>";
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt;
		ResultSet rs;
		try{
			String query = "select circle_code, circle_name from st_cs_circle_master where status = 'Active'";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()){
				html += "<option value=\""+ rs.getString("circle_code") +"\">"+ rs.getString("circle_name") +"</option>";
			}
			html += "</select>";
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return html;
	}
	
	public static List<Integer> getActiveCategoriesList(){
		List<Integer> catList = new ArrayList<Integer>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt;
		ResultSet rs;
		try{
			String query = "select category_id, category_code, description from st_cs_product_category_master where status = 'Active'";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()){
				catList.add(rs.getInt("category_id"));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return catList; 
	}
	
	public CSProductBean registerProductInDb(CSProductBean prodBean){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			//check if product already exists
			pstmt = con.prepareStatement("select product_id from st_cs_product_master where product_code = ? and operator_code = ? and circle_code = ? and denomination = ?");
			pstmt.setString(1, prodBean.getProductCode());
			pstmt.setString(2, prodBean.getOperatorCode());
			pstmt.setString(3, prodBean.getCircleCode());
			pstmt.setDouble(4, prodBean.getDenomination());
			rs = pstmt.executeQuery();
			if(rs.next()){
				prodBean.setStatus("DUP_PROD_NUM");
				return prodBean;
			}
			// insert into st_cs_product_master
			pstmt = con.prepareStatement("insert into st_cs_product_master(product_code, category_id, description,operator_code, circle_code, denomination, country_code, supplier_name, unit_price, retailer_comm, agent_comm, jv_comm, good_cause_comm, vat_comm, status) values ('"
					                       + prodBean.getProductCode() +"','"
					                       + prodBean.getCategoryId() +"','"
					                       + prodBean.getDesc() +"','"
					                       + prodBean.getOperatorCode() +"','"
					                       + prodBean.getCircleCode() +"','"
					                       + prodBean.getDenomination() +"','KEN','"
					                       + prodBean.getSupplierName() +"','"
					                       + prodBean.getUnitPrice() +"','"
					                       + prodBean.getRetailerComm() +"','"
					                       + prodBean.getAgentComm() +"','"
					                       + prodBean.getJvComm() +"','"
					                       + prodBean.getGoodCause() +"','"
					                       + prodBean.getVat() +"','ACTIVE')");
			logger.debug("insert in product master: "+pstmt);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			int prodId = -1;
			if(rs.next()){
				prodId = rs.getInt(1);
			}
			prodBean.setProductId(prodId);
			
			//insert into product details
			pstmt = con.prepareStatement("insert into st_cs_product_details (product_id,talktime,validity,admin_fee,service_tax, recharge_instructions) values ("
											+ prodBean.getProductId() +","
											+ prodBean.getTalktime() +","
											+ prodBean.getValidity() +","
											+ prodBean.getAdminFee() +","
											+ prodBean.getServiceTax()+",'"
											+ prodBean.getRechargeInstruction() +"')");
			pstmt.executeUpdate();
			con.commit();
			prodBean.setStatus("SUCCESS"); //SUCCESS: product registered successfully
		}catch(Exception e){
			try{
				con.rollback();
				e.printStackTrace();
			}catch(SQLException sqe){
				sqe.printStackTrace();
			}
			
		}finally{
			try{
				con.close();
			}catch(SQLException sqe){
				sqe.printStackTrace();
			}
		}
		return prodBean;
	}
}
