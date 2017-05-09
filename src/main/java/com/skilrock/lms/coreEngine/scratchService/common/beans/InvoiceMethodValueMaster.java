package com.skilrock.lms.coreEngine.scratchService.common.beans;

public class InvoiceMethodValueMaster {
	private String invoiceMethodName;
	private String valueType;
	private String value;

	public String getInvoiceMethodName() {
		return invoiceMethodName;
	}

	public void setInvoiceMethodName(String invoiceMethodName) {
		this.invoiceMethodName = invoiceMethodName;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "InvoiceMethodValueMaster [invoiceMethodName="
				+ invoiceMethodName + ", valueType=" + valueType + ", value="
				+ value + "]";
	}

}
