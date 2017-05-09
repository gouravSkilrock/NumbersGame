package com.skilrock.lms.embedded.scratchService.orderMgmt.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.db.DBConnect;

public class GameInfo extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

		// get game name and id for given game number

		String query = "select game_nbr,game_name from st_game_master";
		 
		Connection con = DBConnect.getConnection();

		StringBuilder stBuilder = new StringBuilder("<games>");
		try {
			PreparedStatement pStatement = con.prepareStatement(query);
			ResultSet rs = pStatement.executeQuery();
			while (rs.next()) {
				stBuilder.append("<game>");
				stBuilder.append("<no>" + rs.getInt(1) + "</no>");
				stBuilder.append("<name>" + rs.getString(2) + "</name>");
				stBuilder.append("</game>");
			}
			stBuilder.append("</games>");
			con.close();
			System.out.println(stBuilder);
		} catch (Exception e) {

			e.printStackTrace();
		}

		// return null;

	}

	private HttpServletRequest request;

	private HttpServletResponse response;

	public void getGameData() throws IOException {
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_SCRATCH");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					"Scratch Game Not Avaialbe".getBytes());
			return;
		}

		String query = "select game_nbr,game_name from st_se_game_master where game_status='OPEN' or game_status='SALE_CLOSE' or game_status='SALE_HOLD'";
		 
		Connection con = DBConnect.getConnection();
		int i = 0;
		StringBuilder stBuilder = new StringBuilder(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><gameDetails>");
		try {
			PreparedStatement pStatement = con.prepareStatement(query);
			ResultSet rs = pStatement.executeQuery();

			while (rs.next()) {
				i++;
				stBuilder.append("<game id='" + i + "'>");
				stBuilder.append("<gameNo>" + rs.getInt(1) + "</gameNo>");
				stBuilder
						.append("<gameName>" + rs.getString(2) + "</gameName>");
				stBuilder.append("</game>");
			}
			stBuilder.append("</gameDetails>");
			con.close();
			System.out.println(stBuilder);
			response.getOutputStream().write(stBuilder.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

}
