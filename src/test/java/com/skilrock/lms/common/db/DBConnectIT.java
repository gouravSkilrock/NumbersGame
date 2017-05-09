package com.skilrock.lms.common.db;

import java.sql.Connection;

import org.junit.Assert;
import org.junit.Test;

public class DBConnectIT {

	@Test
	public void connectionIsEstablished(){
		Connection connection = DBConnect.getConnection();
		Assert.assertNotNull(connection);
		DBConnect.closeCon(connection);
	}
}
