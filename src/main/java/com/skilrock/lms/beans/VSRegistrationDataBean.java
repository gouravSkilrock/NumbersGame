package com.skilrock.lms.beans;

public class VSRegistrationDataBean {
	private final String vsShopEntityId;
	private final String vsPrinterId;
	private final String vsPrinterEntityId;
	private final String vsRetailerEntityId;
	private final String password;

	public static class Builder {
		private String vsShopEntityId;
		private String vsPrinterId;
		private String vsPrinterEntityId;
		private String vsRetailerEntityId;
		private String password;

		public Builder vsShopEntityId(String id) {
			vsShopEntityId = id;
			return this;
		}

		public Builder vsPrinterId(String id) {
			vsPrinterId = id;
			return this;
		}

		public Builder vsPrinterEntityId(String id) {
			vsPrinterEntityId = id;
			return this;
		}

		public Builder vsRetailerEntityId(String id) {
			vsRetailerEntityId = id;
			return this;
		}

		public Builder password(String id) {
			password = id;
			return this;
		}

		public VSRegistrationDataBean build() {
			return new VSRegistrationDataBean(this);
		}
	}

	private VSRegistrationDataBean(Builder builder) {
		vsShopEntityId = builder.vsShopEntityId;
		vsPrinterId = builder.vsPrinterId;
		vsPrinterEntityId = builder.vsPrinterEntityId;
		vsRetailerEntityId = builder.vsRetailerEntityId;
		password = builder.password;
	}

	public String getVsShopEntityId() {
		return vsShopEntityId;
	}

	public String getVsPrinterId() {
		return vsPrinterId;
	}

	public String getVsPrinterEntityId() {
		return vsPrinterEntityId;
	}

	public String getVsRetailerEntityId() {
		return vsRetailerEntityId;
	}

	public String getPassword() {
		return password;
	}

}
