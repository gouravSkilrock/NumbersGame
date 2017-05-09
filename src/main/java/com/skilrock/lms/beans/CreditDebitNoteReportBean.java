package com.skilrock.lms.beans;

import java.io.Serializable;

/**
 * This class acts as Bean Class for CR-DR Report
 * 
 * @author umesh
 * 
 */

public class CreditDebitNoteReportBean implements Serializable {
	private static final long serialVersionUID = -5297806764042390464L;

	private int userOrgId;
	private String userOrgName;

	private double crOthers;
	private double crAgainstLooseBooksReturn;
	private double crAgainstFaultyRechargeVouchers;

	private double crOthersTot;
	private double crAgainstLooseBooksReturnTot;
	private double crAgainstFaultyRechargeVouchersTot;

	private double drOthers;
	private double drAgainstLooseBooksReturn;
	private double drAgainstFaultyRechargeVouchers;

	private double drOthersTot;
	private double drAgainstLooseBooksReturnTot;
	private double drAgainstFaultyRechargeVouchersTot;

	public double getCrOthersTot() {
		return crOthersTot;
	}

	public void setCrOthersTot(double crOthersTot) {
		this.crOthersTot = crOthersTot;
	}

	public double getCrAgainstLooseBooksReturnTot() {
		return crAgainstLooseBooksReturnTot;
	}

	public void setCrAgainstLooseBooksReturnTot(
			double crAgainstLooseBooksReturnTot) {
		this.crAgainstLooseBooksReturnTot = crAgainstLooseBooksReturnTot;
	}

	public double getCrAgainstFaultyRechargeVouchersTot() {
		return crAgainstFaultyRechargeVouchersTot;
	}

	public void setCrAgainstFaultyRechargeVouchersTot(
			double crAgainstFaultyRechargeVouchersTot) {
		this.crAgainstFaultyRechargeVouchersTot = crAgainstFaultyRechargeVouchersTot;
	}

	public double getDrOthersTot() {
		return drOthersTot;
	}

	public void setDrOthersTot(double drOthersTot) {
		this.drOthersTot = drOthersTot;
	}

	public double getDrAgainstLooseBooksReturnTot() {
		return drAgainstLooseBooksReturnTot;
	}

	public void setDrAgainstLooseBooksReturnTot(
			double drAgainstLooseBooksReturnTot) {
		this.drAgainstLooseBooksReturnTot = drAgainstLooseBooksReturnTot;
	}

	public double getDrAgainstFaultyRechargeVouchersTot() {
		return drAgainstFaultyRechargeVouchersTot;
	}

	public void setDrAgainstFaultyRechargeVouchersTot(
			double drAgainstFaultyRechargeVouchersTot) {
		this.drAgainstFaultyRechargeVouchersTot = drAgainstFaultyRechargeVouchersTot;
	}

	public int getUserOrgId() {
		return userOrgId;
	}

	public void setUserOrgId(int userOrgId) {
		this.userOrgId = userOrgId;
	}

	public double getCrOthers() {
		return crOthers;
	}

	public void setCrOthers(double crOthers) {
		this.crOthers = crOthers;
	}

	public double getCrAgainstFaultyRechargeVouchers() {
		return crAgainstFaultyRechargeVouchers;
	}

	public void setCrAgainstFaultyRechargeVouchers(
			double crAgainstFaultyRechargeVouchers) {
		this.crAgainstFaultyRechargeVouchers = crAgainstFaultyRechargeVouchers;
	}

	public double getDrOthers() {
		return drOthers;
	}

	public void setDrOthers(double drOthers) {
		this.drOthers = drOthers;
	}

	public double getDrAgainstFaultyRechargeVouchers() {
		return drAgainstFaultyRechargeVouchers;
	}

	public void setDrAgainstFaultyRechargeVouchers(
			double drAgainstFaultyRechargeVouchers) {
		this.drAgainstFaultyRechargeVouchers = drAgainstFaultyRechargeVouchers;
	}

	public double getCrAgainstLooseBooksReturn() {
		return crAgainstLooseBooksReturn;
	}

	public void setCrAgainstLooseBooksReturn(double crAgainstLooseBooksReturn) {
		this.crAgainstLooseBooksReturn = crAgainstLooseBooksReturn;
	}

	public double getDrAgainstLooseBooksReturn() {
		return drAgainstLooseBooksReturn;
	}

	public void setDrAgainstLooseBooksReturn(double drAgainstLooseBooksReturn) {
		this.drAgainstLooseBooksReturn = drAgainstLooseBooksReturn;
	}

	public String getUserOrgName() {
		return userOrgName;
	}

	public void setUserOrgName(String userOrgName) {
		this.userOrgName = userOrgName;
	}

}
