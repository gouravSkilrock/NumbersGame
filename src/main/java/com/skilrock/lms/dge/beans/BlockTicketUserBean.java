package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class BlockTicketUserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int gameId;
	private int doneByUserId;
    private String userName;
	private String status;
	private String ticketNumber;
	private Timestamp updatedTime;
	private String purpose;
	private String responseMessage;
	private int responseCode;
	private int retUserId;
	private int eventId;
	private int drawId;
	private int barCodeCount;
	private String countryName;
	
	public BlockTicketUserBean() {
	}

	public BlockTicketUserBean(BlockTicketUserBeanBuilder blockTicketUserBeanBuilder) {
		this.gameId = blockTicketUserBeanBuilder.gameId;
		this.doneByUserId = blockTicketUserBeanBuilder.doneByUserId;
		this.status = blockTicketUserBeanBuilder.status;
		this.ticketNumber = blockTicketUserBeanBuilder.ticketNumber;
		this.updatedTime = blockTicketUserBeanBuilder.updatedTime;
		this.purpose = blockTicketUserBeanBuilder.purpose;
		this.responseMessage = blockTicketUserBeanBuilder.responseMessage;
		this.responseCode = blockTicketUserBeanBuilder.responseCode;
		this.retUserId = blockTicketUserBeanBuilder.retUserId;
		this.eventId = blockTicketUserBeanBuilder.eventId;
		this.drawId = blockTicketUserBeanBuilder.drawId;
		this.barCodeCount = blockTicketUserBeanBuilder.barCodeCount;
		this.countryName = blockTicketUserBeanBuilder.countryName;
		this.userName=blockTicketUserBeanBuilder.userName;
	}
	public String getCountryName() {
		return countryName;
	}
	
	public void setUserName(String userName)
	{
		this.userName=userName;
	}
	public int getGameId() {
		return gameId;
	}

	public int getDoneByUserId() {
		return doneByUserId;
	}

	public String getStatus() {
		return status;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public String getPurpose() {
		return purpose;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public int getRetUserId() {
		return retUserId;
	}

	public int getEventId() {
		return eventId;
	}

	public int getDrawId() {
		return drawId;
	}

	public int getBarCodeCount() {
		return barCodeCount;
	}
	public String getUserName()
	{
		return userName;
	}

	public static class BlockTicketUserBeanBuilder {

		private int gameId;
		private int doneByUserId;
        private String userName;
		private String status;
		private String ticketNumber;
		private Timestamp updatedTime;
		private String purpose;
		private String responseMessage;
		private int responseCode;
		private int retUserId;
		private int eventId;
		private int drawId;
		private int barCodeCount;
		private String countryName;


		public BlockTicketUserBeanBuilder(String ticketNumber) {
			this.ticketNumber = ticketNumber;
		}
		
		public BlockTicketUserBeanBuilder() {}
		
		public BlockTicketUserBeanBuilder gameId(int gameId) {
			this.gameId = gameId;
			return this;
		}
		public BlockTicketUserBeanBuilder doneByUserId(int doneByUserId) {
			this.doneByUserId = doneByUserId;
			return this;
		}
		public BlockTicketUserBeanBuilder status(String status) {
			this.status = status;
			return this;
		}
		public BlockTicketUserBeanBuilder updatedTime(Timestamp updatedTime) {
			this.updatedTime = updatedTime;
			return this;
		}
		public BlockTicketUserBeanBuilder purpose(String purpose) {
			this.purpose = purpose;
			return this;
		}
		public BlockTicketUserBeanBuilder responseMessage(String responseMessage) {
			this.responseMessage = responseMessage;
			return this;
		}
		public BlockTicketUserBeanBuilder ticketNumber(String ticketNumber) {
			this.ticketNumber = ticketNumber;
			return this;
		}
		public BlockTicketUserBeanBuilder responseCode(int responseCode) {
			this.responseCode = responseCode;
			return this;
		}
		public BlockTicketUserBeanBuilder retUserId(int retUserId) {
			this.retUserId = retUserId;
			return this;
		}
		public BlockTicketUserBeanBuilder eventId(int eventId) {
			this.eventId = eventId;
			return this;
		}
		public BlockTicketUserBeanBuilder drawId(int drawId) {
			this.drawId = drawId;
			return this;
		}
		public BlockTicketUserBeanBuilder barCodeCount(int barCodeCount) {
			this.barCodeCount = barCodeCount;
			return this;
		}
		public BlockTicketUserBeanBuilder countryName(String countryName) {
			this.countryName = countryName;
			return this;
		}
		public BlockTicketUserBeanBuilder userName(String userName) {
			this.userName = userName;
			return this;
		}
		public BlockTicketUserBean build() {
			return new BlockTicketUserBean(this);
		}

	}
}
