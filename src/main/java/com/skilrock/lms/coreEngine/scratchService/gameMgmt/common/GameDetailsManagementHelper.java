package com.skilrock.lms.coreEngine.scratchService.gameMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.beans.GameDetailsManagementBean;
import com.skilrock.lms.beans.GamePrizeDetailsManagementBean;
import com.skilrock.lms.common.db.DBConnect;

public class GameDetailsManagementHelper {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public Map<String, Object> getGameDetails(String gameName) {
		String gameArr[] = gameName.split("-");
		int gameId = Integer.parseInt(gameArr[0]);
		// gameArr[0] = game_id, gameArr[1] = game_nbr, gameArr[2] =
		// game_status,
		Map<String, Object> gameDetails = new TreeMap<String, Object>();
		try {
			conn = DBConnect.getConnection();

			// get the game details from game_master table
			String getGameDetails = "select aa.game_name, aa.ticket_price, aa.start_date, aa.sale_end_date, aa.pwt_end_date, aa.nbr_of_tickets, aa.nbr_of_tickets_per_book, aa.nbr_of_books_per_pack, aa.agent_sale_comm_rate, aa.agent_pwt_comm_rate, aa.retailer_sale_comm_rate, aa.retailer_pwt_comm_rate , aa.govt_comm_rate, aa.game_status, vat_amt, aa.tickets_in_scheme, aa.prize_payout_ratio, (ifnull(bb.books_sale,0) * aa.nbr_of_tickets_per_book) 'books_sale',(ifnull(bb.books_sale_ret,0) * aa.nbr_of_tickets_per_book) 'books_sale_ret',(ifnull(bb.books_sold,0)) 'books_sold' from (( select * from st_se_game_master where game_id =? )aa left join (select books_sale,books_sale_ret,books_sold,xx.game_id from (select count(book_nbr) 'books_sale',game_id from st_se_game_inv_status  where game_id =? and current_owner != 'BO' group by game_id) xx,(select count(book_nbr) 'books_sale_ret',game_id from st_se_game_inv_status  where game_id =? and current_owner = 'RETAILER' group by game_id) yy,(select game_id, sum(sold_tickets) 'books_sold' from st_se_game_ticket_inv_history group by game_id) zz where xx.game_id=yy.game_id and zz.game_id=xx.game_id) bb on aa.game_id = bb.game_id)";
			pstmt = conn.prepareStatement(getGameDetails);
			pstmt.setInt(1, gameId);
			pstmt.setInt(2, gameId);
			pstmt.setInt(3, gameId);
			rs = pstmt.executeQuery();
			System.out.println("get game details == " + pstmt);
			GameDetailsManagementBean gameDetailBean = new GameDetailsManagementBean();

			if (rs.next()) {
				gameDetailBean.setGameName(rs.getString("game_name"));
				gameDetailBean.setGameNbr(Integer.parseInt(gameArr[1]));
				gameDetailBean.setTicketPrice(rs.getDouble("ticket_price"));
				gameDetailBean
						.setStartDate(rs.getDate("start_date").toString());
				gameDetailBean.setSaleEndDate(rs.getDate("sale_end_date")
						.toString());
				gameDetailBean.setPwtEndDate(rs.getDate("pwt_end_date")
						.toString());

				gameDetailBean.setNbrOfTicketUploaded(rs
						.getLong("nbr_of_tickets"));
				gameDetailBean.setTotalNbrOfTicketInGame(rs
						.getLong("tickets_in_scheme"));
				gameDetailBean.setTotalNbrOfTicketSold(rs.getInt("books_sale"));

				gameDetailBean.setNbrTicketsPerBook(rs
						.getInt("nbr_of_tickets_per_book"));
				gameDetailBean.setNbrOfBooksPerPack(rs
						.getInt("nbr_of_books_per_pack"));
				gameDetailBean.setNbrOfPackInGame(rs.getInt("nbr_of_tickets")
						/ (rs.getInt("nbr_of_tickets_per_book") * rs
								.getInt("nbr_of_books_per_pack")));
				gameDetailBean.setTotalNbrOfTicketSoldAtoR(rs
						.getLong("books_sale_ret"));
				gameDetailBean.setTotalNbrOfTicketSoldAtR(rs
						.getLong("books_sold"));
				gameDetailBean.setGameName(rs.getString("game_name"));
				gameDetailBean.setAgentSaleCommRate(rs
						.getDouble("agent_sale_comm_rate"));
				gameDetailBean.setAgentPwtCommRate(rs
						.getDouble("agent_pwt_comm_rate"));
				gameDetailBean.setRetSaleCommRate(rs
						.getDouble("retailer_sale_comm_rate"));
				gameDetailBean.setRetPwtCommRate(rs
						.getDouble("retailer_pwt_comm_rate"));
				gameDetailBean.setGovCommRate(rs.getDouble("govt_comm_rate"));
				gameDetailBean.setVat(rs.getDouble("vat_amt"));

				gameDetailBean.setGameStatus(rs.getString("game_status"));

				// nbrOfTicketsUploaded * ticketPrice
				gameDetailBean.setTotalSales(rs.getInt("nbr_of_tickets")
						* rs.getDouble("ticket_price"));
				// tickets_in_scheme * ticketPrice
				gameDetailBean.setTotalSaleOfScheme(rs
						.getLong("tickets_in_scheme")
						* rs.getDouble("ticket_price"));
				// (books_sale) * ticketPrice
				gameDetailBean.setTotalSaleOfSold(rs.getInt("books_sale")
						* rs.getDouble("ticket_price"));
				gameDetailBean.setTotalSaleOfSoldAtoR(rs
						.getInt("books_sale_ret")
						* rs.getDouble("ticket_price"));
				gameDetailBean.setTotalSaleOfSoldAtR(rs.getInt("books_sold")
						* rs.getDouble("ticket_price"));
				// set the prize pay out ratio value
				gameDetailBean.setPrizePayOutRatioOfScheme(rs
						.getDouble("prize_payout_ratio"));
				// gameDe

			}
			System.out.println(gameDetails);

			rs.close();
			pstmt.close();

			// get the game details from game_master table
			String getGamePrizeDetails = "select prize_amt, prize_level, total_no_of_prize, (total_no_of_prize-(no_of_prize_claim+no_of_prize_cancel))'no_of_prize_rem', no_of_prize_cancel from st_se_rank_master where game_id = ? order by prize_amt ";
			pstmt = conn.prepareStatement(getGamePrizeDetails);
			pstmt.setInt(1, gameId);
			rs = pstmt.executeQuery();
			System.out.println("get game details from rank master == " + pstmt);
			List<GamePrizeDetailsManagementBean> GamePrizeDetailsBeanList = new ArrayList<GamePrizeDetailsManagementBean>();
			GamePrizeDetailsManagementBean gamePrizeDetailBean = null;

			long totalNbrWinners = 0, totalNbrOfPrizeRemaing = 0, totalNbrOfPrize = 0, sumOfNbrOfPrizeCancel = 0;
			double totalTotalPrizeAmount = 0.0, sumOfTotalPrizeFundSold = 0, sumOfTotalPrizeFundCan = 0;
			long ticketsUploaded = gameDetailBean.getNbrOfTicketUploaded();

			while (rs.next()) {
				gamePrizeDetailBean = new GamePrizeDetailsManagementBean();
				if (rs.getDouble("prize_amt") != 0.0) {
					gamePrizeDetailBean.setPrizeAmt(rs.getDouble("prize_amt"));
					gamePrizeDetailBean.setPrizeLevel(rs
							.getString("prize_level"));
					gamePrizeDetailBean.setNoOfPrizeCancel(rs
							.getLong("no_of_prize_cancel"));
					sumOfNbrOfPrizeCancel = sumOfNbrOfPrizeCancel
							+ gamePrizeDetailBean.getNoOfPrizeCancel();
					sumOfTotalPrizeFundCan = sumOfTotalPrizeFundCan
							+ gamePrizeDetailBean.getNoOfPrizeCancel()
							* gamePrizeDetailBean.getPrizeAmt();
					// set odds
					// if(rs.getInt("total_no_of_prize")-rs.getInt("no_of_prize_rem")==
					// 0) gamePrizeDetailBean.setOdds(0.0);
					// else
					if (rs.getInt("total_no_of_prize") != 0) {
						gamePrizeDetailBean.setOdds(1.0 * ticketsUploaded
								/ rs.getInt("total_no_of_prize"));
					} else {
						gamePrizeDetailBean.setOdds(0.0);
					}
					totalNbrOfPrize = totalNbrOfPrize
							+ rs.getInt("total_no_of_prize");

					// no. of winners
					// gamePrizeDetailBean.setNoOfWinners(rs.getInt("total_no_of_prize")-
					// rs.getInt("no_of_prize_rem"));
					gamePrizeDetailBean.setNoOfWinners(rs
							.getInt("total_no_of_prize"));
					totalNbrWinners = totalNbrWinners
							+ gamePrizeDetailBean.getNoOfWinners();

					// total prize Amount
					gamePrizeDetailBean.setTotalPrizeAmount(rs
							.getDouble("prize_amt")
							* gamePrizeDetailBean.getNoOfWinners());
					totalTotalPrizeAmount = totalTotalPrizeAmount
							+ gamePrizeDetailBean.getTotalPrizeAmount();

					// no. of prize remaining
					gamePrizeDetailBean.setNoOfPrizeRem(rs
							.getInt("no_of_prize_rem"));
					totalNbrOfPrizeRemaing = totalNbrOfPrizeRemaing
							+ gamePrizeDetailBean.getNoOfPrizeRem();

					// prize fund for sold ticket
					sumOfTotalPrizeFundSold = sumOfTotalPrizeFundSold
							+ (rs.getInt("total_no_of_prize") - rs
									.getInt("no_of_prize_rem"))
							* rs.getDouble("prize_amt");

					// set prize in %
					gamePrizeDetailBean.setPrize(gamePrizeDetailBean
							.getTotalPrizeAmount()
							* 100 / gameDetailBean.getTotalSales());

					GamePrizeDetailsBeanList.add(gamePrizeDetailBean);
				}
			}

			gameDetailBean.setPrizePayOutRatioOfUploaded(totalTotalPrizeAmount
					/ gameDetailBean.getTotalSales() * 100);

			if (gameDetailBean.getTotalSaleOfSold() != 0) {
				gameDetailBean
						.setPrizePayOutRatioOfSold(sumOfTotalPrizeFundSold
								/ gameDetailBean.getTotalSaleOfSold() * 100);
			} else {
				gameDetailBean.setPrizePayOutRatioOfSold(0);

			}
			if (gameDetailBean.getTotalSaleOfSoldAtoR() != 0) {
				gameDetailBean
						.setPrizePayOutRatioOfSoldAtoR(sumOfTotalPrizeFundSold
								/ gameDetailBean.getTotalSaleOfSoldAtoR() * 100);
			} else {
				gameDetailBean.setPrizePayOutRatioOfSoldAtoR(0);

			}
			if (gameDetailBean.getTotalSaleOfSoldAtR() != 0) {
				gameDetailBean
						.setPrizePayOutRatioOfSoldAtR(sumOfTotalPrizeFundSold
								/ gameDetailBean.getTotalSaleOfSoldAtR() * 100);
			} else {
				gameDetailBean.setPrizePayOutRatioOfSoldAtR(0);

			}

			// fetch Cancel books details
			String fetchCanBooksDetQuery = "select count(*) 'cancel_books' from st_se_game_inv_status where book_status = 'MISSING' and game_id = ?";
			pstmt = conn.prepareStatement(fetchCanBooksDetQuery);
			pstmt.setInt(1, gameId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				gameDetailBean.setNbrOfTicketCancel(rs.getInt("cancel_books")
						* gameDetailBean.getNbrTicketsPerBook());
				gameDetailBean.setTotalSaleValueOfCancel(rs
						.getInt("cancel_books")
						* gameDetailBean.getNbrTicketsPerBook()
						* gameDetailBean.getTicketPrice());
			}

			if (gameDetailBean.getTotalSaleValueOfCancel() != 0) {
				gameDetailBean
						.setPrizePayOutRatioOfCancel(sumOfTotalPrizeFundCan
								/ gameDetailBean.getTotalSaleValueOfCancel()
								* 100);
			} else {
				gameDetailBean.setPrizePayOutRatioOfCancel(0);
			}
			gameDetailBean
					.setTotalPrizeFundOfCancelVirn(sumOfTotalPrizeFundCan);

			// set the prize fund values
			gameDetailBean.setTotalPrizeFundOfScheme(gameDetailBean
					.getPrizePayOutRatioOfScheme()
					* gameDetailBean.getTotalSaleOfScheme() / 100);
			gameDetailBean.setPrizeFund(totalTotalPrizeAmount);

			// gameDetailBean.setTotalPrizeFundOfSold(gameDetailBean.getPrizePayOutRatioOfSold()
			// * gameDetailBean.getTotalSaleOfSold()/100);
			gameDetailBean.setTotalPrizeFundOfSold(sumOfTotalPrizeFundSold);

			gameDetails.put("gameDetailBean", gameDetailBean);
			gameDetails.put("GamePrizeDetailsBeanList",
					GamePrizeDetailsBeanList);

			gameDetails.put("percentage", totalTotalPrizeAmount * 100
					/ gameDetailBean.getTotalSales());
			gameDetails.put("sumRowPrize", "Total Prize");
			gameDetails.put("sumRowOdds", 1.0 * ticketsUploaded
					/ totalNbrOfPrize);
			gameDetails.put("sumRowNoOfWinners", totalNbrWinners);
			gameDetails.put("sumRowTotalTotalPrizeAmount",
					totalTotalPrizeAmount);
			gameDetails.put("sumRowNoOfPrizeRem", totalNbrOfPrizeRemaing);
			gameDetails.put("sumRowNoOfPrizeCan", sumOfNbrOfPrizeCancel);

			gameDetails.put("sumRowPrizePer", totalTotalPrizeAmount * 100
					/ gameDetailBean.getTotalSales());

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return gameDetails;

	}

	public Map<String, String> getGameList() {
		Map<String, String> gameList = new TreeMap<String, String>();
		try {
			conn = DBConnect.getConnection();
			String getGameList = "select game_id, game_nbr, game_name, game_status from st_se_game_master where game_status != 'NEW' order by game_nbr";
			pstmt = conn.prepareStatement(getGameList);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String key = rs.getInt("game_id") + "-" + rs.getInt("game_nbr")
						+ "-" + rs.getString("game_status");
				String value = rs.getInt("game_nbr") + "-"
						+ rs.getString("game_name");
				gameList.put(key, value);
			}
			System.out.println(gameList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return gameList;
	}

}
