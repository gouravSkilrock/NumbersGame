package com.skilrock.ipe.instantprint;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "IInstantPrint", targetNamespace = "http://instantPrint.ipe.skilrock.com/")
public interface IInstantPrint {

	/**
	 * 
	 * @param ticketBean
	 * @return returns com.skilrock.ipe.instantprint.TicketPurchaseBean
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "cancelTicketProcess", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.CancelTicketProcess")
	@ResponseWrapper(localName = "cancelTicketProcessResponse", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.CancelTicketProcessResponse")
	public TicketPurchaseBean cancelTicketProcess(
			@WebParam(name = "ticketBean", targetNamespace = "") TicketPurchaseBean ticketBean);

	/**
	 * 
	 * @param invGameBean
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "inventoryUpload", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.InventoryUpload")
	@ResponseWrapper(localName = "inventoryUploadResponse", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.InventoryUploadResponse")
	public String inventoryUpload(
			@WebParam(name = "invGameBean", targetNamespace = "") GameInventoryDetailsBean invGameBean);

	/**
	 * 
	 * @param gameBean
	 * @return returns com.skilrock.ipe.instantprint.GameBasicDetailBean
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "newGameUpload", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.NewGameUpload")
	@ResponseWrapper(localName = "newGameUploadResponse", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.NewGameUploadResponse")
	public GameBasicDetailBean newGameUpload(
			@WebParam(name = "gameBean", targetNamespace = "") GameBasicDetailBean gameBean);

	/**
	 * 
	 * @return returns com.skilrock.ipe.instantprint.StartGameBean
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "onStartGameData", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.OnStartGameData")
	@ResponseWrapper(localName = "onStartGameDataResponse", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.OnStartGameDataResponse")
	public StartGameBean onStartGameData();

	/**
	 * 
	 * @param pwtBean
	 * @return returns com.skilrock.ipe.instantprint.PwtBean
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "payPwtTicket", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.PayPwtTicket")
	@ResponseWrapper(localName = "payPwtTicketResponse", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.PayPwtTicketResponse")
	public PwtBean payPwtTicket(
			@WebParam(name = "pwtBean", targetNamespace = "") PwtBean pwtBean);

	/**
	 * 
	 * @param ticketBean
	 * @return returns com.skilrock.ipe.instantprint.TicketPurchaseBean
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "reprintTicket", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.ReprintTicket")
	@ResponseWrapper(localName = "reprintTicketResponse", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.ReprintTicketResponse")
	public TicketPurchaseBean reprintTicket(
			@WebParam(name = "ticketBean", targetNamespace = "") TicketPurchaseBean ticketBean);

	/**
	 * 
	 * @param ticketBean
	 * @return returns com.skilrock.ipe.instantprint.TicketPurchaseBean
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "savePurchaseTicket", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.SavePurchaseTicket")
	@ResponseWrapper(localName = "savePurchaseTicketResponse", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.SavePurchaseTicketResponse")
	public TicketPurchaseBean savePurchaseTicket(
			@WebParam(name = "ticketBean", targetNamespace = "") TicketPurchaseBean ticketBean);

	/**
	 * 
	 * @param gameId
	 * @return returns boolean
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "saveStartGame", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.SaveStartGame")
	@ResponseWrapper(localName = "saveStartGameResponse", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.SaveStartGameResponse")
	public boolean saveStartGame(
			@WebParam(name = "gameId", targetNamespace = "") Integer gameId);

	/**
	 * 
	 * @param pwtBean
	 * @return returns com.skilrock.ipe.instantprint.PwtBean
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "updateClaimStatus", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.UpdateClaimStatus")
	@ResponseWrapper(localName = "updateClaimStatusResponse", targetNamespace = "http://instantPrint.ipe.skilrock.com/", className = "com.skilrock.ipe.instantprint.UpdateClaimStatusResponse")
	public PwtBean updateClaimStatus(
			@WebParam(name = "pwtBean", targetNamespace = "") PwtBean pwtBean);

}