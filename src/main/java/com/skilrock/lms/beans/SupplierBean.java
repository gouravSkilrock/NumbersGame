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

public class SupplierBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address1 = null;
	private String address2 = null;
	private String city = null;
	private String countrycode = null;
	private String name = null;
	private String phone = null;
	private long pincode;
	private String state = null;
	private String supplierId;

	public String getAddress1() {
		return address1;
	}

	public String getAddress2() {
		return address2;
	}

	public String getCity() {
		return city;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public long getPincode() {
		return pincode;
	}

	public String getState() {
		return state;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPincode(long pincode) {
		this.pincode = pincode;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

}
