package com.skilrock.lms.rest.services.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ScratchWinningPaymentRequest {

	@NotNull(message = "Please provide requestId")
	@Size(min = 1, message = "RequestId cannot be empty")
	private String requestId;
	@NotNull(message = "Please provide TP UserId")
	@Size(min = 1, message = "TpUserId cannot be empty")
	private String tpUserId;
	@NotNull(message = "Please provide Ticket Number")
	@Size(min = 1, message = "Ticket Number cannot be empty")
	private String ticketNumber;
	@NotNull(message = "Please provide VIRN Number")
	@Size(min = 1, message = "VIRN Number cannot be empty")
	private String virnNumber;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTpUserId() {
		return tpUserId;
	}

	public void setTpUserId(String tpUserId) {
		this.tpUserId = tpUserId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getVirnNumber() {
		return virnNumber;
	}

	public void setVirnNumber(String virnNumber) {
		this.virnNumber = virnNumber;
	}

}
