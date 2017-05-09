package com.skilrock.lms.rest.scratchService.orderMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.rest.scratchService.orderMgmt.dao.OrderMgmtDao;
import com.skilrock.lms.scratchService.orderMgmt.controllerImpl.GameListResponseData;

public class OrderMgmtDaoImpl implements OrderMgmtDao {
	
	public List<GameListResponseData> getGameListDaoImpl(Connection connection) throws ScratchException{
		GameListResponseData gameListResponseData=null;
		List<GameListResponseData> gameList=null;
		String gameListQuery=""; 
		Statement statement=null;
		ResultSet resultSet=null;
		try{
			gameList=new ArrayList<GameListResponseData>();
			gameListQuery="select game_id,game_name,nbr_of_tickets_per_book,ticket_price from st_se_game_master where game_status='OPEN'";
			statement=connection.createStatement();
			resultSet=statement.executeQuery(gameListQuery);
			while(resultSet.next()){
				gameListResponseData=new GameListResponseData();
				gameListResponseData.setGameId(resultSet.getInt("game_id"));
				gameListResponseData.setGameName(resultSet.getString("game_name"));
				gameListResponseData.setNoOfTktsPerBook(resultSet.getInt("nbr_of_tickets_per_book"));
				gameListResponseData.setTicketPrice(resultSet.getInt("ticket_price"));
				gameList.add(gameListResponseData);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new ScratchException();
		}
		
		return gameList;
	}

}
