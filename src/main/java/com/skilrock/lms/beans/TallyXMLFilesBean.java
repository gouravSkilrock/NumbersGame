package com.skilrock.lms.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ENVELOPE")
@XmlType(propOrder = { "header", "body" })
public class TallyXMLFilesBean {
	private Header header;
	private Body body;

	public Header getHeader() {
		return header;
	}

	@XmlElement(name = "HEADER", type = Header.class)
	public void setHeader(Header header) {
		this.header = header;
	}

	public static class Header {
		private String tallyString = "Import Data";

		public String getTallyString() {
			return tallyString;
		}

		@XmlElement(name = "TALLYREQUEST")
		public void setTallyString(String tallyString) {
			this.tallyString = tallyString;
		}
	}

	public Body getBody() {
		return body;
	}

	@XmlElement(name = "BODY", type = Body.class)
	public void setBody(Body body) {
		this.body = body;
	}

	public static class Body {
		private ImportData importData;

		public ImportData getImportData() {
			return importData;
		}

		@XmlElement(name = "IMPORTDATA", type = ImportData.class)
		public void setImportData(ImportData importData) {
			this.importData = importData;
		}

		@XmlType(propOrder = { "requestDesc", "requestData" })
		public static class ImportData {
			private RequestDesc requestDesc;
			private RequestData requestData;

			public RequestDesc getRequestDesc() {
				return requestDesc;
			}

			@XmlElement(name = "REQUESTDESC", type = RequestDesc.class)
			public void setRequestDesc(RequestDesc requestDesc) {
				this.requestDesc = requestDesc;
			}

			public RequestData getRequestData() {
				return requestData;
			}

			@XmlElement(name = "REQUESTDATA", type = RequestData.class)
			public void setRequestData(RequestData requestData) {
				this.requestData = requestData;
			}

			public static class RequestDesc {
				private String reportName = "Vouchers";
				private StaticVariables staticVariables;

				public String getReportName() {
					return reportName;
				}

				@XmlElement(name = "REPORTNAME")
				public void setReportName(String reportName) {
					this.reportName = reportName;
				}

				public StaticVariables getStaticVariables() {
					return staticVariables;
				}

				@XmlElement(name = "STATICVARIABLES", type = StaticVariables.class)
				public void setStaticVariables(StaticVariables staticVariables) {
					this.staticVariables = staticVariables;
				}

				public static class StaticVariables {
					private String svCurrenCompany = "WINLOT GLOBAL RESOURCES LIMITED - (-2014) - (From 1-Jan-2014)";

					public String getSvCurrenCompany() {
						return svCurrenCompany;
					}

					@XmlElement(name = "SVCURRENTCOMPANY")
					public void setSvCurrenCompany(String svCurrenCompany) {
						this.svCurrenCompany = svCurrenCompany;
					}
				}

			}

			public static class RequestData {
				private TallyMessage tallyMessage;

				public TallyMessage getTallyMessage() {
					return tallyMessage;
				}

				@XmlElement(name = "TALLYMESSAGE", type = TallyMessage.class)
				public void setTallyMessage(TallyMessage tallyMessage) {
					this.tallyMessage = tallyMessage;
				}

				@XmlAccessorType(XmlAccessType.NONE)
				@XmlType(propOrder = { "xmlsUdf", "voucher" })
				public static class TallyMessage {
					private String xmlsUdf = "TallyUDF";
					private Voucher voucher;

					public String getXmlsUdf() {
						return xmlsUdf;
					}

					@XmlAttribute(name = "xmlns:UDF")
					public void setXmlsUdf(String xmlsUdf) {
						this.xmlsUdf = xmlsUdf;
					}

					public Voucher getVoucher() {
						return voucher;
					}

					@XmlElement(name = "VOUCHER", type = Voucher.class)
					public void setVoucher(Voucher voucher) {
						this.voucher = voucher;
					}

					@XmlAccessorType(XmlAccessType.NONE)
					@XmlType(propOrder = {"date", "guid", "narration",
							"voucherTypeName", "voucherNumber",
							"partyLedgerName", "cstFormIssueType",
							"cstFormRecvType", "fbtPaymentType", "vchGstClass",
							"enteredBy", "diffActualQty", "audited",
							"forJobCosting", "isOptional", "effectiveDate",
							"useForInterest", "useForGainLoss", "useForGodownTransfer",
							"useForCompound", "alterId", "exciseOpening", "useForFinalProduction",
							"isCancelled", "hasCashFlow", "isPostdated", "useTrackingNumber",
							"isInvoice", "mfgJournal", "hasDiscounts", "asPaySlip",
							"isCostCentre", "isDeleted", "asOriginal","allLedger" })
					public static class Voucher {
						private String remoteId = "";
						private String vchtype;
						private String action = "Create";
						private String date;
						private String guid = "";
						private String narration;
						private String voucherTypeName;
						private String voucherNumber = "";
						private String partyLedgerName;
						private String cstFormIssueType="";
						private String cstFormRecvType="";
						private String fbtPaymentType = "Default";
						private String vchGstClass="";
						private String enteredBy = "wgrl";
						private String diffActualQty = "No";
						private String audited = "No";
						private String forJobCosting = "No";
						private String isOptional = "No";
						private String effectiveDate;
						private String useForInterest = "No";
						private String useForGainLoss = "No";
						private String useForGodownTransfer = "No";
						private String useForCompound = "No";
						private String alterId="";
						private String exciseOpening = "No";
						private String useForFinalProduction = "No";
						private String isCancelled = "No";
						private String hasCashFlow = "No";
						private String isPostdated = "No";
						private String useTrackingNumber = "No";
						private String isInvoice = "No";
						private String mfgJournal = "No";
						private String hasDiscounts = "No";
						private String asPaySlip = "No";
						private String isCostCentre = "No";
						private String isDeleted = "No";
						private String asOriginal = "No";
						private List<AllLedgerEntries> allLedger;

						public String getRemoteId() {
							return remoteId;
						}

						@XmlAttribute(name = "REMOTEID")
						public void setRemoteId(String remoteId) {
							this.remoteId = remoteId;
						}

						public String getVchtype() {
							return vchtype;
						}

						@XmlAttribute(name = "VCHTYPE")
						public void setVchtype(String vchtype) {
							this.vchtype = vchtype;
						}

						public String getAction() {
							return action;
						}

						@XmlAttribute(name = "ACTION")
						public void setAction(String action) {
							this.action = action;
						}

						public String getDate() {
							return date;
						}

						@XmlElement(name = "DATE")
						public void setDate(String date) {
							this.date = date;
						}

						public String getGuid() {
							return guid;
						}

						@XmlElement(name = "GUID")
						public void setGuid(String guid) {
							this.guid = guid;
						}

						public String getNarration() {
							return narration;
						}

						@XmlElement(name = "NARRATION")
						public void setNarration(String narration) {
							this.narration = narration;
						}

						public String getVoucherTypeName() {
							return voucherTypeName;
						}

						@XmlElement(name = "VOUCHERTYPENAME")
						public void setVoucherTypeName(String voucherTypeName) {
							this.voucherTypeName = voucherTypeName;
						}

						public String getVoucherNumber() {
							return voucherNumber;
						}

						@XmlElement(name = "VOUCHERNUMBER")
						public void setVoucherNumber(String voucherNumber) {
							this.voucherNumber = voucherNumber;
						}

						public String getPartyLedgerName() {
							return partyLedgerName;
						}

						@XmlElement(name = "PARTYLEDGERNAME")
						public void setPartyLedgerName(String partyLedgerName) {
							this.partyLedgerName = partyLedgerName;
						}

						public String getCstFormIssueType() {
							return cstFormIssueType;
						}

						@XmlElement(name = "CSTFORMISSUETYPE")
						public void setCstFormIssueType(String cstFormIssueType) {
							this.cstFormIssueType = cstFormIssueType;
						}

						public String getCstFormRecvType() {
							return cstFormRecvType;
						}

						@XmlElement(name = "CSTFORMRECVTYPE")
						public void setCstFormRecvType(String cstFormRecvType) {
							this.cstFormRecvType = cstFormRecvType;
						}

						public String getFbtPaymentType() {
							return fbtPaymentType;
						}

						@XmlElement(name = "FBTPAYMENTTYPE")
						public void setFbtPaymentType(String fbtPaymentType) {
							this.fbtPaymentType = fbtPaymentType;
						}

						public String getVchGstClass() {
							return vchGstClass;
						}

						@XmlElement(name = "VCHGSTCLASS")
						public void setVchGstClass(String vchGstClass) {
							this.vchGstClass = vchGstClass;
						}

						public String getEnteredBy() {
							return enteredBy;
						}

						@XmlElement(name = "ENTEREDBY")
						public void setEnteredBy(String enteredBy) {
							this.enteredBy = enteredBy;
						}

						public String getDiffActualQty() {
							return diffActualQty;
						}

						@XmlElement(name = "DIFFACTUALQTY")
						public void setDiffActualQty(String diffActualQty) {
							this.diffActualQty = diffActualQty;
						}

						public String getAudited() {
							return audited;
						}

						@XmlElement(name = "AUDITED")
						public void setAudited(String audited) {
							this.audited = audited;
						}

						public String getForJobCosting() {
							return forJobCosting;
						}

						@XmlElement(name = "FORJOBCOSTING")
						public void setForJobCosting(String forJobCosting) {
							this.forJobCosting = forJobCosting;
						}

						public String getIsOptional() {
							return isOptional;
						}

						@XmlElement(name = "ISOPTIONAL")
						public void setIsOptional(String isOptional) {
							this.isOptional = isOptional;
						}

						public String getEffectiveDate() {
							return effectiveDate;
						}

						@XmlElement(name = "EFFECTIVEDATE")
						public void setEffectiveDate(String effectiveDate) {
							this.effectiveDate = effectiveDate;
						}

						public String getUseForInterest() {
							return useForInterest;
						}

						@XmlElement(name = "USEFORINTEREST")
						public void setUseForInterest(String useForInterest) {
							this.useForInterest = useForInterest;
						}

						public String getUseForGainLoss() {
							return useForGainLoss;
						}

						@XmlElement(name = "USEFORGAINLOSS")
						public void setUseForGainLoss(String useForGainLoss) {
							this.useForGainLoss = useForGainLoss;
						}

						public String getUseForGodownTransfer() {
							return useForGodownTransfer;
						}

						@XmlElement(name = "USEFORGODOWNTRANSFER")
						public void setUseForGodownTransfer(
								String useForGodownTransfer) {
							this.useForGodownTransfer = useForGodownTransfer;
						}

						public String getUseForCompound() {
							return useForCompound;
						}

						@XmlElement(name = "USEFORCOMPOUND")
						public void setUseForCompound(String useForCompound) {
							this.useForCompound = useForCompound;
						}

						public String getAlterId() {
							return alterId;
						}

						@XmlElement(name = "ALTERID")
						public void setAlterId(String alterId) {
							this.alterId = alterId;
						}

						public String getExciseOpening() {
							return exciseOpening;
						}

						@XmlElement(name = "EXCISEOPENING")
						public void setExciseOpening(String exciseOpening) {
							this.exciseOpening = exciseOpening;
						}

						public String getUseForFinalProduction() {
							return useForFinalProduction;
						}

						@XmlElement(name = "USEFORFINALPRODUCTION")
						public void setUseForFinalProduction(
								String useForFinalProduction) {
							this.useForFinalProduction = useForFinalProduction;
						}

						public String getIsCancelled() {
							return isCancelled;
						}

						@XmlElement(name = "ISCANCELLED")
						public void setIsCancelled(String isCancelled) {
							this.isCancelled = isCancelled;
						}

						public String getHasCashFlow() {
							return hasCashFlow;
						}

						@XmlElement(name = "HASCASHFLOW")
						public void setHasCashFlow(String hasCashFlow) {
							this.hasCashFlow = hasCashFlow;
						}

						public String getIsPostdated() {
							return isPostdated;
						}

						@XmlElement(name = "ISPOSTDATED")
						public void setIsPostdated(String isPostdated) {
							this.isPostdated = isPostdated;
						}

						public String getUseTrackingNumber() {
							return useTrackingNumber;
						}

						@XmlElement(name = "USETRACKINGNUMBER")
						public void setUseTrackingNumber(
								String useTrackingNumber) {
							this.useTrackingNumber = useTrackingNumber;
						}

						public String getIsInvoice() {
							return isInvoice;
						}

						@XmlElement(name = "ISINVOICE")
						public void setIsInvoice(String isInvoice) {
							this.isInvoice = isInvoice;
						}

						public String getMfgJournal() {
							return mfgJournal;
						}

						@XmlElement(name = "MFGJOURNAL")
						public void setMfgJournal(String mfgJournal) {
							this.mfgJournal = mfgJournal;
						}

						public String getHasDiscounts() {
							return hasDiscounts;
						}

						@XmlElement(name = "HASDISCOUNTS")
						public void setHasDiscounts(String hasDiscounts) {
							this.hasDiscounts = hasDiscounts;
						}

						public String getAsPaySlip() {
							return asPaySlip;
						}

						@XmlElement(name = "ASPAYSLIP")
						public void setAsPaySlip(String asPaySlip) {
							this.asPaySlip = asPaySlip;
						}

						public String getIsCostCentre() {
							return isCostCentre;
						}

						@XmlElement(name = "ISCOSTCENTRE")
						public void setIsCostCentre(String isCostCentre) {
							this.isCostCentre = isCostCentre;
						}

						public String getIsDeleted() {
							return isDeleted;
						}

						@XmlElement(name = "ISDELETED")
						public void setIsDeleted(String isDeleted) {
							this.isDeleted = isDeleted;
						}

						public String getAsOriginal() {
							return asOriginal;
						}

						@XmlElement(name = "ASORIGINAL")
						public void setAsOriginal(String asOriginal) {
							this.asOriginal = asOriginal;
						}

						public List<AllLedgerEntries> getAllLedger() {
							return allLedger;
						}

						@XmlElement(name = "ALLLEDGERENTRIES.LIST", type = AllLedgerEntries.class)
						public void setAllLedger(
								List<AllLedgerEntries> allLedger) {
							this.allLedger = allLedger;
						}

						@XmlType(propOrder = { "ledgerName", "gstClass",
								"isDeemedPositive", "ledgerFromItem",
								"removeZeroEntries", "isPartyLedger", "amount" })
						public static class AllLedgerEntries {
							private String ledgerName;
							private String gstClass="";
							private String isDeemedPositive;
							private String ledgerFromItem="No";
							private String removeZeroEntries="No";
							private String isPartyLedger;
							private String amount;

							public String getLedgerName() {
								return ledgerName;
							}

							@XmlElement(name = "LEDGERNAME")
							public void setLedgerName(String ledgerName) {
								this.ledgerName = ledgerName;
							}

							public String getGstClass() {
								return gstClass;
							}

							@XmlElement(name = "GSTCLASS")
							public void setGstClass(String gstClass) {
								this.gstClass = gstClass;
							}

							public String getIsDeemedPositive() {
								return isDeemedPositive;
							}

							@XmlElement(name = "ISDEEMEDPOSITIVE")
							public void setIsDeemedPositive(
									String isDeemedPositive) {
								this.isDeemedPositive = isDeemedPositive;
							}

							public String getLedgerFromItem() {
								return ledgerFromItem;
							}

							@XmlElement(name = "LEDGERFROMITEM")
							public void setLedgerFromItem(String ledgerFromItem) {
								this.ledgerFromItem = ledgerFromItem;
							}

							public String getRemoveZeroEntries() {
								return removeZeroEntries;
							}

							@XmlElement(name = "REMOVEZEROENTRIES")
							public void setRemoveZeroEntries(
									String removeZeroEntries) {
								this.removeZeroEntries = removeZeroEntries;
							}

							public String getIsPartyLedger() {
								return isPartyLedger;
							}

							@XmlElement(name = "ISPARTYLEDGER")
							public void setIsPartyLedger(String isPartyLedger) {
								this.isPartyLedger = isPartyLedger;
							}

							public String getAmount() {
								return amount;
							}

							@XmlElement(name = "AMOUNT")
							public void setAmount(String amount) {
								this.amount = amount;
							}

							@Override
							public String toString() {
								return "AllLedgerEntries [amount=" + amount
										+ ", gstClass=" + gstClass
										+ ", isDeemedPositive="
										+ isDeemedPositive + ", isPartyLedger="
										+ isPartyLedger + ", ledgerFromItem="
										+ ledgerFromItem + ", ledgerName="
										+ ledgerName + ", removeZeroEntries="
										+ removeZeroEntries + "]";
							}

						}

						@Override
						public String toString() {
							return "Voucher [Date=" + date + ", action="
									+ action + ", allLedger=" + allLedger
									+ ", alterId=" + alterId + ", asOriginal="
									+ asOriginal + ", asPaySlip=" + asPaySlip
									+ ", audited=" + audited
									+ ", cstFormIssueType=" + cstFormIssueType
									+ ", cstFormRecvType=" + cstFormRecvType
									+ ", diffActualQty=" + diffActualQty
									+ ", effectiveDate=" + effectiveDate
									+ ", enteredBy=" + enteredBy
									+ ", exciseOpening=" + exciseOpening
									+ ", fbtPaymentType=" + fbtPaymentType
									+ ", forJobCosting=" + forJobCosting
									+ ", guid=" + guid + ", hasCashFlow="
									+ hasCashFlow + ", hasDiscounts="
									+ hasDiscounts + ", isCancelled="
									+ isCancelled + ", isCostCentre="
									+ isCostCentre + ", isDeleted=" + isDeleted
									+ ", isInvoice=" + isInvoice
									+ ", isOptional=" + isOptional
									+ ", isPostdated=" + isPostdated
									+ ", mfgJournal=" + mfgJournal
									+ ", narration=" + narration
									+ ", partyLedgerName=" + partyLedgerName
									+ ", remoteId=" + remoteId
									+ ", useForCompound=" + useForCompound
									+ ", useForFinalProduction="
									+ useForFinalProduction
									+ ", useForGainLoss=" + useForGainLoss
									+ ", useForGodownTransfer="
									+ useForGodownTransfer
									+ ", useForInterest=" + useForInterest
									+ ", useTrackingNumber="
									+ useTrackingNumber + ", vchGstClass="
									+ vchGstClass + ", vchtype=" + vchtype
									+ ", voucherNumber=" + voucherNumber
									+ ", voucherTypeName=" + voucherTypeName
									+ "]";
						}
					}

					@Override
					public String toString() {
						return "TallyMessage [voucher=" + voucher
								+ ", xmlsUdf=" + xmlsUdf + "]";
					}
				}
			}

			@Override
			public String toString() {
				return "ImportData [requestData=" + requestData
						+ ", requestDesc=" + requestDesc + "]";
			}
		}
	}

	@Override
	public String toString() {
		return "TestMainBean [body=" + body + ", header=" + header + "]";
	}

}
