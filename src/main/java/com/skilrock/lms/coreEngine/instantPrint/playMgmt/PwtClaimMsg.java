package com.skilrock.lms.coreEngine.instantPrint.playMgmt;

public class PwtClaimMsg {
	private static enum status {
		MISSING, CLAIM_RET, RETURN, CLAIM_AGT, CLAIM_AGT_AUTO, CLAIM_AGT_TEMP, CLAIM_PLR_BO, CLAIM_PLR_RET_UNCLM, CLAIM_PLR_RET_CLM, CLAIM_PLR_RET_CLM_DIR, CLAIM_PLR_RET_UNCLM_DIR, CLAIM_PLR_AGT_CLM_DIR, CLAIM_PLR_AGT_TEMP, CLAIM_RET_AGT_TEMP, CLAIM_RET_CLM, CLAIM_RET_CLM_AUTO, CLAIM_RET_UNCLM, CLAIM_PLR_AGT_UNCLM_DIR, REQUESTED, PND_MAS, PND_PAY
	}

	public static String pwtMsg(String claimStatus) {
		String pwtMsg = null;
		try {
			switch (status.valueOf(claimStatus)) {
			case MISSING:
				pwtMsg = "Ticket staus is MISSING";
				break;
			case CLAIM_RET:
				pwtMsg = "Already paid to Retailer";
				break;
			case RETURN:
				pwtMsg = "Ticket once Verified and Return to Player for Verification as Direct Player PWT at BO";
				break;
			case CLAIM_AGT:
				pwtMsg = "Already paid to Agent";
				break;
			case CLAIM_AGT_AUTO:
				pwtMsg = "Already paid to Agent as auto scrap.";
				break;
			case CLAIM_AGT_TEMP:
				pwtMsg = "Already paid to Agent AS Bulk by bo";
				break;
			case CLAIM_PLR_BO:
				pwtMsg = "Already paid as Direct Player PWT By BO";
				break;
			case CLAIM_PLR_RET_UNCLM:
				pwtMsg = "Already paid To Player By Retailer";
				break;
			case CLAIM_PLR_RET_CLM:
				pwtMsg = "Paid to Player by Retailer and pending to claim by Retailer at Agent as auto scrap.";
				break;
			case CLAIM_PLR_RET_CLM_DIR:
				pwtMsg = "Paid to Player by Retailer and pending to claim by Retailer at Agent as auto scrap.";
				break;
			case CLAIM_PLR_RET_UNCLM_DIR:
				pwtMsg = "Paid to Player by Retailer and pending to claim by Retailer at Agent as Ticket Verification.";
				break;
			case CLAIM_PLR_AGT_CLM_DIR:
				pwtMsg = "Paid to Player by Agent and pending to claim at BO by Agent.";
				break;
			case CLAIM_PLR_AGT_TEMP:
				pwtMsg = "Already paid to Agent AS Bulk by BO";
				break;
			case CLAIM_RET_AGT_TEMP:
				pwtMsg = "Already paid to Retailer AS Bulk by Agent";
				break;
			case CLAIM_RET_CLM:
				pwtMsg = "Paid to Retailer as Claimable";
				break;
			case CLAIM_RET_CLM_AUTO:
				pwtMsg = "Paid to Retailer as Claimable by auto scrap";
				break;
			case CLAIM_RET_UNCLM:
				pwtMsg = "Paid to Retailer pending to claim at bo as Ticket Verification.";
				break;
			case CLAIM_PLR_AGT_UNCLM_DIR:
				pwtMsg = "Paid to player by Agent";
				break;
			case REQUESTED:
				pwtMsg = "This Ticket has Beean Already claimed and requested for Approval";
				break;
			case PND_MAS:
				pwtMsg = "This Ticket has Beean requested for BO Master Approval";
				break;
			case PND_PAY:
				pwtMsg = "This Ticket has Beean Approved And Pending to Payment";
				break;
			default:
				pwtMsg = "Undefined Status of Ticket's PWT";
			}
		} catch (NullPointerException e) {
			pwtMsg = "Error in Pwt Status Message";
		} catch (IllegalArgumentException e) {
			pwtMsg = "Undefined Status of Ticket's PWT";
		}
		return pwtMsg;
	}
}
