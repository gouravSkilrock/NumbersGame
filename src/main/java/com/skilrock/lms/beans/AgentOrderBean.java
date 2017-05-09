/*
 * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an “AS IS”
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.beans;

import java.io.Serializable;

public class AgentOrderBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int agentId;
	private java.sql.Date orderDate;
	private int orderId;
	private String orderStatus;
	private int retailerId;
	private int retailerOrgId;
	private String retOrgName;

	public int getAgentId() {
		return agentId;
	}

	public java.sql.Date getOrderDate() {
		return orderDate;
	}

	public int getOrderId() {
		return orderId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public int getRetailerId() {
		return retailerId;
	}

	public int getRetailerOrgId() {
		return retailerOrgId;
	}

	public String getRetOrgName() {
		return retOrgName;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public void setOrderDate(java.sql.Date orderDate) {
		this.orderDate = orderDate;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setRetailerId(int retailerId) {
		this.retailerId = retailerId;
	}

	public void setRetailerOrgId(int retailerOrgId) {
		this.retailerOrgId = retailerOrgId;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

}
