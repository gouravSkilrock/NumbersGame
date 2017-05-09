package com.skilrock.lms.coreEngine.drawGames.pwtMgmt;

public enum PwtStatus {
	
	MISSING(1),CLAIM_PLR_RET_UNCLM(2),CLAIM_PLR_RET_CLM(3),CLAIM_PLR(4),CLAIM_RET_TEMP(5),CLAIM_PLR_RET_TEMP(6),CLAIM_AGT_TEMP(7),CLAIM_RET_AGT_TEMP(8)
	,CLAIM_AGT(9),CLAIM_RET(10),CLAIM_PLR_TEMP(11),CLAIM_PLR_RET(12),REQUESTED(13),PND_MAS(14),PND_PAY(15),CLAIM_AGT_AUTO(16),CLAIM_PLR_BO(17)
	,CLAIM_PLR_AGT_UNCLM_DIR(18),CLAIM_PLR_AGT_CLM_DIR(19),CLAIM_PLR_AGT_TEMP(20),CLAIM_RET_CLM(21),CLAIM_RET_CLM_AUTO(22),CLAIM_RET_UNCLM(23)
	,CANCELLED_PERMANENT(24),CLAIM_PLR_RET_CLM_DIR(25),CLAIM_PLR_RET_UNCLM_DIR(26),CLAIM_AGT_CLM_AUTO(27);

	private int status;

	private PwtStatus(int c) {
		status = c;
	}

	public int getStatus() {
		return status;
	}

	public static boolean contains(String sts) {
		try {
			PwtStatus.valueOf(sts);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
