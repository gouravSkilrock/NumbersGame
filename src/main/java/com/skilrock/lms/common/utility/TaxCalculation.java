package com.skilrock.lms.common.utility;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

public class TaxCalculation {
	static Log logger = LogFactory.getLog(TaxCalculation.class);

	public double returnTaxAmount(double pwtAmount) throws IOException {
		double income_tax = 0.0;
		double returnIncomeTax = 0.0;
		double pwt_imit = 0.0;
		// Properties properties = new Properties();
		// InputStream inputStream = this.getClass().getClassLoader()
		// .getResourceAsStream("st_game_inventory.properties");
		// logger.debug(">>>>" + inputStream);
		// properties.load(inputStream);

		// String pwtLimit = properties.getProperty("pwt_limit");
		// String incomeTax = properties.getProperty("income_tax");
		ServletContext sc = ServletActionContext.getServletContext();
		String pwtLimit = (String) sc.getAttribute("PWT_LIMIT");
		String incomeTax = (String) sc.getAttribute("INCOME_TAX");
		returnIncomeTax = Double.parseDouble(incomeTax);
		pwt_imit = Double.parseDouble(pwtLimit);
		if (pwtAmount > pwt_imit) {

			income_tax = returnIncomeTax;

		}

		return income_tax;
	}

}