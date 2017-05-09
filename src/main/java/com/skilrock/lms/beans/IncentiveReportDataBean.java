package com.skilrock.lms.beans;


public class IncentiveReportDataBean {
	private double saleAmt;
	private double winningAmt;
	private double nonWinAmt;
	private double incAmt;

	public IncentiveReportDataBean() {
	}

	public IncentiveReportDataBean(
			IncentiveReportDataBeanBuilder incentiveReportDataBeanBuilder) {
		this.saleAmt = incentiveReportDataBeanBuilder.saleAmt;
		this.winningAmt = incentiveReportDataBeanBuilder.winningAmt;
		this.nonWinAmt = incentiveReportDataBeanBuilder.nonWinAmt;
		this.incAmt = incentiveReportDataBeanBuilder.incAmt;
	}

	public double getSaleAmt() {
		return saleAmt;
	}

	public double getWinningAmt() {
		return winningAmt;
	}

	public double getNonWinAmt() {
		return nonWinAmt;
	}

	public double getIncAmt() {
		return incAmt;
	}

	public static class IncentiveReportDataBeanBuilder {

		private double saleAmt;
		private double winningAmt;
		private double nonWinAmt;
		private double incAmt;

		public IncentiveReportDataBeanBuilder() {
		}

		public IncentiveReportDataBeanBuilder setSaleAmt(double saleAmt) {
			this.saleAmt = saleAmt;
			return this;
		}

		public IncentiveReportDataBeanBuilder setWinningAmt(double winningAmt) {
			this.winningAmt = winningAmt;
			return this;
		}

		public IncentiveReportDataBeanBuilder setNonWinAmt(double nonWinAmt) {
			this.nonWinAmt = nonWinAmt;
			return this;
		}

		public IncentiveReportDataBeanBuilder setIncAmt(double incAmt) {
			this.incAmt = incAmt;
			return this;
		}

		public IncentiveReportDataBean build() {
			return new IncentiveReportDataBean(this);
		}

	}

}
