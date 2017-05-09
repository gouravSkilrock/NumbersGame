package com.skilrock.lms.common.utility;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.common.db.DBConnect;

public class LatLongFromCellId extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(LatLongFromCellId.class);
	private int userId;
	private int CID;
	private int LAC;

	public LatLongFromCellId(int userId, int CID, int LAC) {
		this.userId = userId;
		this.CID = CID;
		this.LAC = LAC;
	}

	private void getAndUpdateLatLong() throws Exception {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String> future = executor.submit(new UpdateLatLongTask(userId,
				CID, LAC));
		try {
			LOGGER.info("Thread Started..");
			future.get(5, TimeUnit.SECONDS);
			LOGGER.info("Thread Finished!..");
		} catch (TimeoutException e) {
			LOGGER.info("Thread Terminated!..");
		}
		executor.shutdownNow();
	}

	public void run() {
		try {
			getAndUpdateLatLong();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class UpdateLatLongTask implements Callable<String> {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateLatLongTask.class);
	private static final String API_URL = "http://www.google.com/glm/mmap";
	private int userId;
	private int CID;
	private int LAC;
	private double lat;
	private double lon;

	public UpdateLatLongTask(int userId, int CID, int LAC) {
		this.userId = userId;
		this.CID = CID;
		this.LAC = LAC;
	}

	@Override
	public String call() {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			if (RqsLocation(CID, LAC)) {
				con = DBConnect.getConnection();
				ps = con.prepareStatement("UPDATE st_lms_ret_offline_master SET lat=?, lon=? WHERE user_id=?");
				ps.setDouble(1, lat);
				ps.setDouble(2, lon);
				ps.setInt(3, userId);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, ps);
		}
		return null;
	}

	private boolean RqsLocation(int cid, int lac) throws Exception {
		boolean result = false;
		URL url = new URL(API_URL);
		URLConnection conn = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) conn;
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setConnectTimeout(1000);
		httpConn.connect();

		OutputStream outputStream = httpConn.getOutputStream();
		WriteData(outputStream, cid, lac);
		InputStream inputStream = httpConn.getInputStream();
		DataInputStream dataInputStream = new DataInputStream(inputStream);

		dataInputStream.readShort();
		dataInputStream.readByte();
		int code = dataInputStream.readInt();
		if (code == 0) {
			lat = (double) dataInputStream.readInt() / 1000000D;
			lon = (double) dataInputStream.readInt() / 1000000D;
			LOGGER.info("Lat:" + lat + "Lon:" + lon);
			result = true;
		}
		return result;
	}

	/**
	 * This method fakes android client when connecting to the google api in
	 * order to resolve cellID into lon/lat pair.
	 */
	private void WriteData(OutputStream out, int cid, int lac)
			throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(out);

		// initializing request
		dataOutputStream.writeShort(21);
		dataOutputStream.writeLong(0);

		// faking android device
		dataOutputStream.writeUTF("en");
		dataOutputStream.writeUTF("Android");
		dataOutputStream.writeUTF("1.0");
		dataOutputStream.writeUTF("Web");

		// typical session
		dataOutputStream.writeByte(27);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(3);
		dataOutputStream.writeUTF("");

		// specifying parameters of the request
		dataOutputStream.writeInt(cid);
		dataOutputStream.writeInt(lac);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);

		// closing values, obligatory
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);

		dataOutputStream.flush();
	}

}
