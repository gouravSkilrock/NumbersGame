 package com.skilrock.lms.coreEngine.drawGames.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.skilrock.lms.beans.PromoGameBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.web.drawGames.common.Util;

public class DGPromoScheme {

	
	public static List<PromoGameBean> getAvailablePromoGamesNew(String gameName,double totalPurchAmt,List<String> drawNamePurchList) {
		List<PromoGameBean> promoGameslist = new ArrayList<PromoGameBean>();
		PromoGameBean promobean = null;
		int saleGameId=Util.getGameId(gameName);
        Connection con=DBConnect.getConnection();
        String fetchPromoDetail="select promo_game_id,promo_ticket_type,promo_game_type,no_of_free_tickets,no_of_draws,game_name_dev as promo_game_name from st_dg_promo_scheme ps,st_dg_game_master gm where ps.promo_game_id=gm.game_id and sale_game_id="+saleGameId+"  and status='ACTIVE'";
		try {
			java.sql.PreparedStatement pstmt=con.prepareStatement(fetchPromoDetail);
			ResultSet rs=pstmt.executeQuery();
			
				while(rs.next()){
					promobean = new PromoGameBean();
					promobean.setPromoGameNo(rs.getInt("promo_game_id"));
					promobean.setPromoGametype(rs.getString("promo_game_type"));
					promobean.setPromoTicketType(rs.getString("promo_ticket_type"));
					if(rs.getString("promo_game_name") != null){
						promobean.setPromoGameName(rs.getString("promo_game_name"));
						}
					if(rs.getString("no_of_free_tickets") != null){
					promobean.setNoOfPromoTickets(rs.getInt("no_of_free_tickets"));
					}
					if(rs.getString("no_of_draws") != null){
						promobean.setNoOfDraws(rs.getInt("no_of_draws"));
						}
					promoGameslist.add(promobean);
				}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			DBConnect.closeCon(con);
		}
		/*
		if ("Fortune".equalsIgnoreCase(gameName)) {
			promobean = new PromoGameBean();
			promobean.setPromoGameNo(Util.getGameNumber("RaffleGame"));
			promobean.setPromoGametype("RAFFLE");
			promobean.setPromoTicketType("REFERENCE");
			promoGameslist.add(promobean);
		}
         
			if ("Zimlotto".equalsIgnoreCase(gameName)) {
				promobean = new PromoGameBean();
				promobean.setPromoGameNo(Util.getGameNumber("RaffleGame"));
				promobean.setPromoGametype("RAFFLE");
				promobean.setPromoTicketType("ORIGINAL");
				promoGameslist.add(promobean);
			}		
	    
		if ("Zimlottotwo".equalsIgnoreCase(gameName)) {
			promobean = new PromoGameBean();
			promobean.setPromoGameNo(Util.getGameNumber("RaffleGame1"));
			promobean.setPromoGametype("RAFFLE");
			promobean.setPromoTicketType("ORIGINAL");
			promobean.setNoOfPromoTickets(1);
			promoGameslist.add(promobean);
		}	
		ServletContext sc = ServletActionContext.getServletContext();
		if ("Zimlottotwo".equalsIgnoreCase(gameName)) {
			promobean = new PromoGameBean();
			promobean.setPromoGameNo(Util.getGameNumber("Zimlottothree"));
			promobean.setPromoGametype("Zimlottothree");
			promobean.setPromoTicketType("ORIGINAL");
			String noOfPromoTickets=(String) sc.getAttribute("NO_OF_PROMO_TICKET");
			promobean.setNoOfPromoTickets(Integer.parseInt(noOfPromoTickets));
			promoGameslist.add(promobean);
		}
		if ("Tanzanialotto".equalsIgnoreCase(gameName)) {
			promobean = new PromoGameBean();
			promobean.setPromoGameNo(Util.getGameNumber("RaffleGame"));
			promobean.setPromoGametype("RAFFLE");
			promobean.setPromoTicketType("REFERENCE");
			promoGameslist.add(promobean);
		}
		
		if ("Keno".equalsIgnoreCase(gameName)) {
			promobean = new PromoGameBean();
			promobean.setPromoGameNo(Util.getGameNumber("RaffleGame"));
			promobean.setPromoGametype("RAFFLE");
			promobean.setPromoTicketType("ORIGINAL");
			promoGameslist.add(promobean);
		}
		
		if ("BonusBalllotto".equalsIgnoreCase(gameName)) {
			promobean = new PromoGameBean();
			promobean.setPromoGameNo(Util.getGameNumber("RaffleGame"));
			promobean.setPromoGametype("RAFFLE");
			promobean.setPromoTicketType("REFERENCE");
			promoGameslist.add(promobean);
		}
		  
		  if("Keno".equalsIgnoreCase(gameName)){
			  boolean isPromoDraw=false;
			  List<String> KenoIndoorGamelist=  KenoConstants.KENO_INDOORDRAWNAME_LIST;
			  for (String drawNamePurch : drawNamePurchList) {
				if(KenoIndoorGamelist.contains(drawNamePurch)){
					isPromoDraw = true;
					break;
				}
				
			}
			  if(totalPurchAmt >= 50 && isPromoDraw){
				  promobean = new PromoGameBean();
				  promobean.setPromoGameNo(Util.getGameNumber("Zerotonine"));
				  promobean.setPromoGametype("Zerotonine");
				  promobean.setPromoTicketType("ORIGINAL");
				  promoGameslist.add(promobean);
			  }
			  
			}
        
		  		
		  if ("Keno".equalsIgnoreCase(gameName)) {
				promobean = new PromoGameBean();
				promobean.setPromoGameNo(Util.getGameNumber("RaffleGame"));
				promobean.setPromoGametype("RAFFLE");
				promobean.setPromoTicketType("ORIGINAL");
				promoGameslist.add(promobean);
			}
	       
		  
		  
//		  if("KenoTwo".equalsIgnoreCase(gameName) && totalPurchAmt > 50){
//			  promobean = new PromoGameBean();
//			  promobean.setPromoGameNo(Util.getGameNumber("Zerotonine"));
//			  promobean.setPromoGametype("Zerotonine");
//			  promobean.setPromoTicketType("ORIGINAL");
//			  promoGameslist.add(promobean); 
//		  }  

		*/
		return promoGameslist;

	}
	
}
