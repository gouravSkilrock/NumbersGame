package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;

import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.scratchService.utility.VirnEncoderNDecoder;
import com.skilrock.lms.web.scratchService.utility.VirnEncoderNDecoder.EncryptionException;

public class VirnDecryptionHelper {
	VirnEncoderNDecoder encoder = null;

	VirnDecryptionHelper() {
		try {
			encoder = new VirnEncoderNDecoder();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			new LMSException(e);
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			new LMSException(e);
		} catch (EncryptionException e) {
			e.printStackTrace();
			new LMSException(e);
		}
	}

	public boolean checkTicketStatusForBo(String gameNbr, String bookNbr,
			String ticketNbrDigit, boolean isStrict, int gameId,
			Connection connection) throws SQLException {

		String query1 = QueryManager
				.getST4CurrentOwnerDetailsUsingGameBookNbr();
		PreparedStatement pstmt = connection.prepareStatement(query1);
		pstmt.setInt(1, gameId);
		pstmt.setString(2, bookNbr);
		ResultSet result = pstmt.executeQuery();
		System.out.println("pstmt == " + pstmt);
		if (result.next()) {
			String ownerType = result.getString("current_owner");
			String bookStatus = result.getString("book_status");
			if (("RETAILER".equalsIgnoreCase(ownerType.trim()) || "AGENT"
					.equalsIgnoreCase(ownerType.trim()))
					&& ("ACTIVE".equalsIgnoreCase(bookStatus) || "CLAIMED"
							.equalsIgnoreCase(bookStatus))) {

				if (isStrict) {
					String selTktDetail = "select ticket_nbr, game_id, status  from st_se_pwt_tickets_inv_? where ticket_nbr = ?";
					PreparedStatement tktInvPstmt = connection
							.prepareStatement(selTktDetail);
					tktInvPstmt.setInt(1, Integer.parseInt(gameNbr));
					tktInvPstmt.setString(3, bookNbr + "-" + ticketNbrDigit);
					System.out.println("tktInvPstmt = " + tktInvPstmt);
					ResultSet rs = tktInvPstmt.executeQuery();
					if (rs.next()) {
						return true;
					} else {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	public List<TicketBean> getGameWiseVerifiedTickets(List<String> tktList,
			int gameNbr, Connection connection, boolean isStrict)
			throws LMSException {
		List<TicketBean> verifyResults = new ArrayList<TicketBean>();

		try {

			// check the format of these tickets
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			List<TicketBean> tktBeanList = commHelper.isTicketFormatValid(
					tktList, gameNbr, connection);

			String tktNbrArr[] = null;
			TicketBean bean = null;
			for (TicketBean tktBean : tktBeanList) {
				if (tktBean.getIsValid()) {
					tktNbrArr = tktBean.getTicketNumber().split("-");
					if (!checkTicketStatusForBo(tktNbrArr[0], tktNbrArr[0]
							+ "-" + tktNbrArr[1], tktNbrArr[2], isStrict,
							tktBean.getTicketGameId(), connection)) {
						tktBean.setValid(false);
					}
					bean = tktBean;
				} else {
					bean = tktBean;
				}
				verifyResults.add(bean);
			}

			return verifyResults;

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Problem in Ticket validation; " + e);
		}

	}

	/**
	 * In This process
	 * 
	 * @param encSchemeType
	 * @return
	 */

	public Map<String, String> verifyNFetchEncodedVirn(String encSchemeType,
			String tktVerType, String[] virnCodeArr, List<PwtBean> pwtList) {
		// When VIRN and Ticket are verified Separately
		if ("VIRN_N_TKT_SEPERATE".equalsIgnoreCase(tktVerType.trim())) {
			// verifyVirnNTicketSeperateProcess(encSchemeType, ownerType,
			// virnCode, tktVerType);

		} else // VIRN and ticket in one bar code and a four digit code is
		// required to decrypt the data
		if ("VIRN_N_TKT_COMBINED".equalsIgnoreCase(tktVerType.trim())) {

		} else // 2W Encryption of virn & ticket(that are already 1W Encrypted
		// ) using code(1W)
		if ("2W_ENC_OF_TKT_VIRN".equalsIgnoreCase(tktVerType.trim())) {

		}
		return null;
	}

	public String verifyVirnNTicketSeperateProcess(String ownerType,
			String[] virnCodeArr, List<PwtBean> pwtList) {
		final String encSchemeType = "1W_ENC_OF_ALL";

		if ("1W_ENC_OF_ALL".equalsIgnoreCase(encSchemeType.trim())) {// 1W
			// Encryption
			// of
			// virn,
			// ticket
			// and
			// code
			// encodedVirn = create1WEncVirnBeanListWithFixedCode(br,
			// encVirnStrBuilder, gameNbrDigits,
			// maxRankDigits, game_id, rankDetailMap);
		} else if ("2W_ENC_OF_TKT".equalsIgnoreCase(encSchemeType.trim())) { // 2W
			// Encryption
			// of
			// ticket(key
			// is
			// virn+code)
			// and
			// 1W
			// encryption
			// of
			// virn
			// &
			// code
			// create2WEncVirnBeanListWithFixedCode(gameNbr, gameId,
			// virnCodeArr, DEFAULT_KEY, connection, pwtList)
		} else if ("2W_ENC_OF_TKT_VIRN".equalsIgnoreCase(encSchemeType.trim())) { // 2W
			// Encryption
			// of
			// virn
			// &
			// ticket(that
			// are
			// already
			// 1W
			// Encrypted
			// )
			// using
			// code(1W)
			// encodedVirn = create2WEncVirnBeanList(br, encVirnStrBuilder,
			// gameNbrDigits,
			// maxRankDigits, game_id, rankDetailMap);
		}
		return null;
	}
}
