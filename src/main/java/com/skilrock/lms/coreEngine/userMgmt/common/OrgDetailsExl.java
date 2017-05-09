package com.skilrock.lms.coreEngine.userMgmt.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.DateFormats;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.web.userMgmt.common.ProcessSearchOrgAction;

public class OrgDetailsExl extends LocalizedTextUtil {

	private Locale locale;
	private WritableCellFormat dateFormat;
	DateFormat format = new SimpleDateFormat("dd-MMM-yy");
	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	private WritableCellFormat numberFormat;
	private int rowNo = 1;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;

	public OrgDetailsExl() throws WriteException {

		locale = Locale.getDefault();
		numberFormat = new WritableCellFormat(NumberFormats.FORMAT3);
		numberFormat.setFont(new WritableFont(WritableFont.TIMES, 12));
		numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		numberFormat.setWrap(false);

		times = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 12));
		times.setWrap(false);
		times.setBorder(Border.ALL, BorderLineStyle.THIN);

		timesBoldUnderline = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE));
		timesBoldUnderline.setWrap(false);
		timesBoldUnderline.setAlignment(Alignment.CENTRE);
		timesBoldUnderline.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		timesBoldUnderline.setBackground(Colour.GREY_25_PERCENT);

		dateFormat = new WritableCellFormat(DateFormats.FORMAT2);
		dateFormat.setFont(new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD));
		dateFormat.setWrap(false);
		dateFormat.setAlignment(Alignment.RIGHT);

		headerFormat = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 15));
		headerFormat.setWrap(false);
		headerFormat.setAlignment(Alignment.CENTRE);

		headerDateFormat = new WritableCellFormat(DateFormats.FORMAT4);
		headerDateFormat.setFont(new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD));
		headerDateFormat.setWrap(false);
		headerDateFormat.setAlignment(Alignment.CENTRE);

		headingLabel = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 12));
		headingLabel.setWrap(false);
		headingLabel.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		headingLabel.setAlignment(Alignment.LEFT);
		headingLabel.setBackground(Colour.GRAY_25);

		headingNumberFormat = new WritableCellFormat(NumberFormats.FORMAT3);
		headingNumberFormat.setFont(new WritableFont(WritableFont.TIMES, 12));
		headingNumberFormat.setWrap(false);
		headingNumberFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		headingNumberFormat.setBackground(Colour.GRAY_25);
	}

	private void addCaption(WritableSheet sheet, int column, int row, String s,
			int width) throws RowsExceededException, WriteException {
		Label headingLabel;
		headingLabel = new Label(column, row, s, timesBoldUnderline);
		sheet.setColumnView(column, width);
		sheet.addCell(headingLabel);
	}

	private void addLabel(WritableSheet sheet, int column, int row, Object s)
			throws WriteException, RowsExceededException {
		Label headingLabel;
		headingLabel = new Label(column, row, s.toString(), times);
		sheet.addCell(headingLabel);
	}

	private void addNumber(WritableSheet sheet, int column, int row, Double amt)
			throws WriteException, RowsExceededException {
		Number headingNumberFormat;

		headingNumberFormat = new Number(column, row, amt, numberFormat);
		sheet.addCell(headingNumberFormat);
	}

	private void createContent(WritableSheet sheet,
			List<OrganizationBean> orgDtlList, String orgType) throws WriteException,
			RowsExceededException, ParseException {
		String countryDeployed = (String) ServletActionContext
				.getServletContext().getAttribute("COUNTRY_DEPLOYED");
		// create Caption
		addCaption(sheet, 0, ++this.rowNo, findDefaultText("label.org.name", locale), 30);
		addCaption(sheet, 1, this.rowNo, findDefaultText("label.org.type", locale), 20);
		addCaption(sheet, 2, this.rowNo, findDefaultText("label.org.status", locale), 20);
		addCaption(sheet, 3, this.rowNo, findDefaultText("label.prnt.org.name", locale), 30);
		addCaption(sheet, 4, this.rowNo, findDefaultText("label.pwt.scrp", locale), 14);
		if ("RETAILER".equals(orgType)) {
			addCaption(sheet, 5, this.rowNo, findDefaultText("label.trmnl.id", locale), 20);
			addCaption(sheet, 6, this.rowNo, findDefaultText("label.crdtlmt", locale), 20);
			addCaption(sheet, 7, this.rowNo, findDefaultText("label.ext.credit.limit", locale), 20);
			addCaption(sheet, 8, this.rowNo, findDefaultText("label.rem.day", locale), 12);
			if ("KENYA".equalsIgnoreCase(countryDeployed)) {
				addCaption(sheet, 9, this.rowNo, findDefaultText("label.ledger.bal", locale), 20);
				addCaption(sheet, 10, this.rowNo, findDefaultText("label.blnce", locale), 20); // changed
				// amit
				// on 22/09/10
				addCaption(sheet, 11, this.rowNo, findDefaultText("label.secur.dpst", locale), 20);
			} else {
				addCaption(sheet, 9, this.rowNo, findDefaultText("label.ledger.bal", locale), 20); // changed
				// amit
				// on 22/09/10
				addCaption(sheet, 10, this.rowNo, findDefaultText("label.secur.dpst", locale), 20);
			}
		} else {
			addCaption(sheet, 5, this.rowNo, findDefaultText("label.crdtlmt", locale), 20);
			addCaption(sheet, 6, this.rowNo, findDefaultText("label.ext.credit.limit", locale), 20);
			addCaption(sheet, 7, this.rowNo, findDefaultText("label.rem.day", locale), 12);
			if ("KENYA".equalsIgnoreCase(countryDeployed)) {
				addCaption(sheet, 8, this.rowNo, findDefaultText("label.ledger.bal", locale), 20);
				addCaption(sheet, 9, this.rowNo, findDefaultText("label.blnce", locale), 20); // changed
				// amit
				// on 22/09/10
				addCaption(sheet, 10, this.rowNo, findDefaultText("label.secur.dpst", locale), 20);
			} else {
				addCaption(sheet, 8, this.rowNo, findDefaultText("label.ledger.bal", locale), 20); // changed
				// amit
				// on 22/09/10
				addCaption(sheet, 9, this.rowNo, findDefaultText("label.secur.dpst", locale), 20);
			}
		}

		for (OrganizationBean bean : orgDtlList) {
			addLabel(sheet, 0, ++this.rowNo, bean.getOrgName());
			addLabel(sheet, 1, this.rowNo, bean.getOrgType());
			addLabel(sheet, 2, this.rowNo, bean.getOrgStatus());
			addLabel(sheet, 3, this.rowNo, bean.getParentOrgName());
			addLabel(sheet, 4, this.rowNo, bean.getPwtScrapStatus());
			if("RETAILER".equals(orgType)) {
				addLabel(sheet, 5, this.rowNo, bean.getSerialNo());
				addNumber(sheet, 6, this.rowNo, bean.getOrgCreditLimit());
				addNumber(sheet, 7, this.rowNo, bean.getExtendedCredit());
				addLabel(sheet, 8, this.rowNo, bean.getExtendsCreditLimitUpto());
				if ("KENYA".equalsIgnoreCase(countryDeployed)) {
					addNumber(sheet, 9, this.rowNo, bean.getLedgerBalance());
					addNumber(sheet, 10, this.rowNo, bean.getAvailableCredit() - bean.getClaimableBal()); // changed by amit on
					// 22/09/10
					addNumber(sheet, 11, this.rowNo, bean.getSecurityDeposit());
				} else {
					addNumber(sheet, 9, this.rowNo, bean.getAvailableCredit()
							- bean.getClaimableBal()); // changed by amit on
					// 22/09/10
					addNumber(sheet, 10, this.rowNo, bean.getSecurityDeposit());
				}
			} else {
				addNumber(sheet, 5, this.rowNo, bean.getOrgCreditLimit());
				addNumber(sheet, 6, this.rowNo, bean.getExtendedCredit());
				addLabel(sheet, 7, this.rowNo, bean.getExtendsCreditLimitUpto());
				if ("KENYA".equalsIgnoreCase(countryDeployed)) {
					addNumber(sheet, 8, this.rowNo, bean.getLedgerBalance());
					addNumber(sheet, 9, this.rowNo, bean.getAvailableCredit()
							- bean.getClaimableBal()); // changed by amit on
					// 22/09/10
					addNumber(sheet, 10, this.rowNo, bean.getSecurityDeposit());
				} else {
					addNumber(sheet, 8, this.rowNo, bean.getAvailableCredit()
							- bean.getClaimableBal()); // changed by amit on
					// 22/09/10
					addNumber(sheet, 9, this.rowNo, bean.getSecurityDeposit());
				}
			}

		}

	}

	public void createExlForOrgDetails(List<OrganizationBean> orgDtlList,
			WritableWorkbook workbk, ProcessSearchOrgAction filterVal)
			throws IOException, WriteException, ParseException {
		WritableWorkbook workbook = workbk;
		workbook.createSheet("OrgDetails_" + format.format(new Date()), 0);
		WritableSheet exlSheet = workbook.getSheet(0);
		createHeader(exlSheet);
		createFilterEntries(exlSheet, filterVal);
		createContent(exlSheet, orgDtlList, filterVal.getOrgType());
		workbook.write();
		workbook.close();
	}

	private void createFilterEntries(WritableSheet sheet,
			ProcessSearchOrgAction filterVal) throws WriteException,
			ParseException {

		if (filterVal.getOrgName() != null
				&& !"".equals(filterVal.getOrgName().trim())) {
			sheet
					.addCell(new Label(0, ++this.rowNo, findDefaultText("label.org.name", locale),
							times));
			sheet.addCell(new Label(1, this.rowNo, filterVal.getOrgName(),
					times));
		}
		if (filterVal.getOrgType() != null
				&& !"-1".equals(filterVal.getOrgType().trim())) {
			sheet
					.addCell(new Label(0, ++this.rowNo, findDefaultText("label.org.type", locale),
							times));
			sheet.addCell(new Label(1, this.rowNo, filterVal.getOrgType(),
					times));
		}
		if (filterVal.getOrgStatus() != null
				&& !"-1".equals(filterVal.getOrgStatus().trim())) {
			sheet.addCell(new Label(0, ++this.rowNo, findDefaultText("label.org.status", locale),
					times));
			sheet.addCell(new Label(1, this.rowNo, filterVal.getOrgStatus(),
					times));
		}
		if (filterVal.getParentCompName() != null
				&& !"".equals(filterVal.getParentCompName().trim())) {
			sheet.addCell(new Label(0, ++this.rowNo,
					findDefaultText("label.prnt.org.name", locale), times));
			sheet.addCell(new Label(1, this.rowNo, filterVal
					.getParentCompName(), times));
		}
		if(filterVal.getOrgType() != null && "RETAILER".equals(filterVal.getOrgType())) {
			sheet.addCell(new Label(0, ++this.rowNo, findDefaultText("label.terminal.status", locale), times));
			sheet.addCell(new Label(1, this.rowNo, filterVal.getOrgType(), times));
		}
		
		if (filterVal.getCrLimit() != null
				&& !"".equals(filterVal.getCrLimit())) {
			sheet.addCell(new Label(0, ++this.rowNo, findDefaultText("label.crdtlmt", locale), times));
			sheet.addCell(new Label(1, this.rowNo, filterVal.getCrLimitSign()
					+ filterVal.getCrLimit(), times));
		}
		if (filterVal.getExtendCrLimit() != null
				&& !"".equals(filterVal.getExtendCrLimit())) {
			sheet.addCell(new Label(0, ++this.rowNo, findDefaultText("label.ext.credit.limit", locale),
					times));
			sheet.addCell(new Label(1, this.rowNo, filterVal
					.getExtendCrLimitSign()
					+ filterVal.getExtendCrLimit(), times));
		}
		if (filterVal.getAvlblCrLimit() != null
				&& !"".equals(filterVal.getAvlblCrLimit())) {
			sheet.addCell(new Label(0, ++this.rowNo, findDefaultText("label.avail.crd.limit", locale),
					times));
			sheet.addCell(new Label(1, this.rowNo, filterVal
					.getAvlblCrLimitSign()
					+ filterVal.getAvlblCrLimit(), times));
		}
		if (filterVal.getSecurityDeposit() != null
				&& !"".equals(filterVal.getSecurityDeposit().trim())) {
			sheet
					.addCell(new Label(0, ++this.rowNo, findDefaultText("label.secur.dpst", locale),
							times));
			sheet.addCell(new Label(1, this.rowNo, filterVal
					.getSecurityDepositSign()
					+ filterVal.getSecurityDeposit(), times));
		}
		if (filterVal.getPwtScrapStatus() != null
				&& !filterVal.getPwtScrapStatus().trim().equals("")
				&& !filterVal.getPwtScrapStatus().equals("-1")) {
			sheet.addCell(new Label(0, ++this.rowNo, findDefaultText("label.pwt.scrp", locale), times));
			sheet.addCell(new Label(1, this.rowNo, filterVal
					.getPwtScrapStatus(), times));
		}
	}

	private void createHeader(WritableSheet sheet) throws WriteException,
			ParseException {
		// mergeCells(col1, row1, col2, row2)
		sheet.mergeCells(0, this.rowNo, 3, this.rowNo);
		// label(colNo, rowNo, data, format)
		sheet.addCell(new Label(0, this.rowNo, "Date : "
				+ format.format(new Date()), dateFormat));

		this.rowNo += 1;
		// mergeCells(col1, row1, col2, row2)
		sheet.mergeCells(0, this.rowNo, 3, this.rowNo);
		sheet.addCell(new Label(0, this.rowNo, findDefaultText("label.org.detail", locale),
				headerFormat));

	}

}
