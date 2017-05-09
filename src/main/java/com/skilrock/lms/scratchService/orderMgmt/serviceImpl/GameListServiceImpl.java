package com.skilrock.lms.scratchService.orderMgmt.serviceImpl;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.rest.scratchService.orderMgmt.daoImpl.OrderMgmtDaoImpl;
import com.skilrock.lms.scratchService.orderMgmt.controllerImpl.GameListResponse;
import com.skilrock.lms.scratchService.orderMgmt.controllerImpl.GameListResponseData;

public class GameListServiceImpl {
	OrderMgmtDaoImpl gameListDaoImpl=null;
	public GameListServiceImpl(){
		gameListDaoImpl=new OrderMgmtDaoImpl();
	}
	
	public GameListServiceImpl(OrderMgmtDaoImpl gameListDaoImpl){
		this.gameListDaoImpl=new OrderMgmtDaoImpl();
	}

	public List<GameListResponseData> getGameListServiceImpl() throws SQLException{
		GameListResponseData gameListResponseData=null;
		Connection connection=null;
		List<GameListResponseData> gameList =new ArrayList<GameListResponseData>();
		
		try{
			connection=DBConnect.getConnection();
			gameListResponseData=new GameListResponseData();
			gameList=gameListDaoImpl.getGameListDaoImpl(connection);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		
		return gameList;
		
	}
}
