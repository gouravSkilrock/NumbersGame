package com.skilrock.ipe.instantprint;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.skilrock.ipe.instantprint package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _SaveStartGame_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "saveStartGame");
	private final static QName _CancelTicketProcessResponse_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/",
			"cancelTicketProcessResponse");
	private final static QName _InventoryUploadResponse_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "inventoryUploadResponse");
	private final static QName _OnStartGameData_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "onStartGameData");
	private final static QName _ReprintTicket_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "reprintTicket");
	private final static QName _GameInventoryDetailsBean_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "gameInventoryDetailsBean");
	private final static QName _SavePurchaseTicket_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "savePurchaseTicket");
	private final static QName _GameBean_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "gameBean");
	private final static QName _TicketPurchaseBean_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "ticketPurchaseBean");
	private final static QName _ReprintTicketResponse_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "reprintTicketResponse");
	private final static QName _UpdateClaimStatus_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "updateClaimStatus");
	private final static QName _UpdateClaimStatusResponse_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/",
			"updateClaimStatusResponse");
	private final static QName _NewGameUploadResponse_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "newGameUploadResponse");
	private final static QName _GameBasicDetailBean_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "gameBasicDetailBean");
	private final static QName _NewGameUpload_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "newGameUpload");
	private final static QName _PayPwtTicketResponse_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "payPwtTicketResponse");
	private final static QName _SavePurchaseTicketResponse_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/",
			"savePurchaseTicketResponse");
	private final static QName _OnStartGameDataResponse_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "onStartGameDataResponse");
	private final static QName _InventoryUpload_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "inventoryUpload");
	private final static QName _PayPwtTicket_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "payPwtTicket");
	private final static QName _CancelTicketProcess_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "cancelTicketProcess");
	private final static QName _SaveStartGameResponse_QNAME = new QName(
			"http://instantPrint.ipe.skilrock.com/", "saveStartGameResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.skilrock.ipe.instantprint
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link SaveStartGameResponse }
	 * 
	 */
	public SaveStartGameResponse createSaveStartGameResponse() {
		return new SaveStartGameResponse();
	}

	/**
	 * Create an instance of {@link GameBean.ImageSizeMap.Entry }
	 * 
	 */
	public GameBean.ImageSizeMap.Entry createGameBeanImageSizeMapEntry() {
		return new GameBean.ImageSizeMap.Entry();
	}

	/**
	 * Create an instance of {@link GameBean.ImageDataMap }
	 * 
	 */
	public GameBean.ImageDataMap createGameBeanImageDataMap() {
		return new GameBean.ImageDataMap();
	}

	/**
	 * Create an instance of {@link UpdateClaimStatusResponse }
	 * 
	 */
	public UpdateClaimStatusResponse createUpdateClaimStatusResponse() {
		return new UpdateClaimStatusResponse();
	}

	/**
	 * Create an instance of {@link StartGameBean.GameMap.Entry }
	 * 
	 */
	public StartGameBean.GameMap.Entry createStartGameBeanGameMapEntry() {
		return new StartGameBean.GameMap.Entry();
	}

	/**
	 * Create an instance of {@link StringArray }
	 * 
	 */
	public StringArray createStringArray() {
		return new StringArray();
	}

	/**
	 * Create an instance of {@link SaveStartGame }
	 * 
	 */
	public SaveStartGame createSaveStartGame() {
		return new SaveStartGame();
	}

	/**
	 * Create an instance of {@link NewGameUpload }
	 * 
	 */
	public NewGameUpload createNewGameUpload() {
		return new NewGameUpload();
	}

	/**
	 * Create an instance of {@link OnStartGameData }
	 * 
	 */
	public OnStartGameData createOnStartGameData() {
		return new OnStartGameData();
	}

	/**
	 * Create an instance of {@link GameBean.ActiveBookMap }
	 * 
	 */
	public GameBean.ActiveBookMap createGameBeanActiveBookMap() {
		return new GameBean.ActiveBookMap();
	}

	/**
	 * Create an instance of {@link CancelTicketProcess }
	 * 
	 */
	public CancelTicketProcess createCancelTicketProcess() {
		return new CancelTicketProcess();
	}

	/**
	 * Create an instance of {@link UpdateClaimStatus }
	 * 
	 */
	public UpdateClaimStatus createUpdateClaimStatus() {
		return new UpdateClaimStatus();
	}

	/**
	 * Create an instance of {@link TicketPurchaseBean.AdvMsg }
	 * 
	 */
	public TicketPurchaseBean.AdvMsg createTicketPurchaseBeanAdvMsg() {
		return new TicketPurchaseBean.AdvMsg();
	}

	/**
	 * Create an instance of {@link GameBean.ImageTypeMap.Entry }
	 * 
	 */
	public GameBean.ImageTypeMap.Entry createGameBeanImageTypeMapEntry() {
		return new GameBean.ImageTypeMap.Entry();
	}

	/**
	 * Create an instance of {@link GameInventoryDetailsBean }
	 * 
	 */
	public GameInventoryDetailsBean createGameInventoryDetailsBean() {
		return new GameInventoryDetailsBean();
	}

	/**
	 * Create an instance of {@link ReprintTicketResponse }
	 * 
	 */
	public ReprintTicketResponse createReprintTicketResponse() {
		return new ReprintTicketResponse();
	}

	/**
	 * Create an instance of {@link InventoryUpload }
	 * 
	 */
	public InventoryUpload createInventoryUpload() {
		return new InventoryUpload();
	}

	/**
	 * Create an instance of {@link TicketPurchaseBean.AdvMsg.Entry }
	 * 
	 */
	public TicketPurchaseBean.AdvMsg.Entry createTicketPurchaseBeanAdvMsgEntry() {
		return new TicketPurchaseBean.AdvMsg.Entry();
	}

	/**
	 * Create an instance of {@link InventoryUploadResponse }
	 * 
	 */
	public InventoryUploadResponse createInventoryUploadResponse() {
		return new InventoryUploadResponse();
	}

	/**
	 * Create an instance of {@link GameBean.ImageDataMap.Entry }
	 * 
	 */
	public GameBean.ImageDataMap.Entry createGameBeanImageDataMapEntry() {
		return new GameBean.ImageDataMap.Entry();
	}

	/**
	 * Create an instance of {@link CancelTicketProcessResponse }
	 * 
	 */
	public CancelTicketProcessResponse createCancelTicketProcessResponse() {
		return new CancelTicketProcessResponse();
	}

	/**
	 * Create an instance of {@link GameBean.ImageSizeMap }
	 * 
	 */
	public GameBean.ImageSizeMap createGameBeanImageSizeMap() {
		return new GameBean.ImageSizeMap();
	}

	/**
	 * Create an instance of {@link PayPwtTicket }
	 * 
	 */
	public PayPwtTicket createPayPwtTicket() {
		return new PayPwtTicket();
	}

	/**
	 * Create an instance of {@link TicketPurchaseBean }
	 * 
	 */
	public TicketPurchaseBean createTicketPurchaseBean() {
		return new TicketPurchaseBean();
	}

	/**
	 * Create an instance of {@link GameBean }
	 * 
	 */
	public GameBean createGameBean() {
		return new GameBean();
	}

	/**
	 * Create an instance of {@link GameBasicDetailBean }
	 * 
	 */
	public GameBasicDetailBean createGameBasicDetailBean() {
		return new GameBasicDetailBean();
	}

	/**
	 * Create an instance of {@link StartGameBean }
	 * 
	 */
	public StartGameBean createStartGameBean() {
		return new StartGameBean();
	}

	/**
	 * Create an instance of {@link StartGameBean.GameMap }
	 * 
	 */
	public StartGameBean.GameMap createStartGameBeanGameMap() {
		return new StartGameBean.GameMap();
	}

	/**
	 * Create an instance of {@link NewGameUploadResponse }
	 * 
	 */
	public NewGameUploadResponse createNewGameUploadResponse() {
		return new NewGameUploadResponse();
	}

	/**
	 * Create an instance of {@link OnStartGameDataResponse }
	 * 
	 */
	public OnStartGameDataResponse createOnStartGameDataResponse() {
		return new OnStartGameDataResponse();
	}

	/**
	 * Create an instance of {@link GameBean.ImageTypeMap }
	 * 
	 */
	public GameBean.ImageTypeMap createGameBeanImageTypeMap() {
		return new GameBean.ImageTypeMap();
	}

	/**
	 * Create an instance of {@link SavePurchaseTicketResponse }
	 * 
	 */
	public SavePurchaseTicketResponse createSavePurchaseTicketResponse() {
		return new SavePurchaseTicketResponse();
	}

	/**
	 * Create an instance of {@link PayPwtTicketResponse }
	 * 
	 */
	public PayPwtTicketResponse createPayPwtTicketResponse() {
		return new PayPwtTicketResponse();
	}

	/**
	 * Create an instance of {@link PwtBean }
	 * 
	 */
	public PwtBean createPwtBean() {
		return new PwtBean();
	}

	/**
	 * Create an instance of {@link GameBean.ActiveBookMap.Entry }
	 * 
	 */
	public GameBean.ActiveBookMap.Entry createGameBeanActiveBookMapEntry() {
		return new GameBean.ActiveBookMap.Entry();
	}

	/**
	 * Create an instance of {@link SavePurchaseTicket }
	 * 
	 */
	public SavePurchaseTicket createSavePurchaseTicket() {
		return new SavePurchaseTicket();
	}

	/**
	 * Create an instance of {@link ReprintTicket }
	 * 
	 */
	public ReprintTicket createReprintTicket() {
		return new ReprintTicket();
	}

	/**
	 * Create an instance of {@link ArrayList }
	 * 
	 */
	public ArrayList createArrayList() {
		return new ArrayList();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SaveStartGame }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "saveStartGame")
	public JAXBElement<SaveStartGame> createSaveStartGame(SaveStartGame value) {
		return new JAXBElement<SaveStartGame>(_SaveStartGame_QNAME,
				SaveStartGame.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link CancelTicketProcessResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "cancelTicketProcessResponse")
	public JAXBElement<CancelTicketProcessResponse> createCancelTicketProcessResponse(
			CancelTicketProcessResponse value) {
		return new JAXBElement<CancelTicketProcessResponse>(
				_CancelTicketProcessResponse_QNAME,
				CancelTicketProcessResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link InventoryUploadResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "inventoryUploadResponse")
	public JAXBElement<InventoryUploadResponse> createInventoryUploadResponse(
			InventoryUploadResponse value) {
		return new JAXBElement<InventoryUploadResponse>(
				_InventoryUploadResponse_QNAME, InventoryUploadResponse.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link OnStartGameData }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "onStartGameData")
	public JAXBElement<OnStartGameData> createOnStartGameData(
			OnStartGameData value) {
		return new JAXBElement<OnStartGameData>(_OnStartGameData_QNAME,
				OnStartGameData.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link ReprintTicket }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "reprintTicket")
	public JAXBElement<ReprintTicket> createReprintTicket(ReprintTicket value) {
		return new JAXBElement<ReprintTicket>(_ReprintTicket_QNAME,
				ReprintTicket.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GameInventoryDetailsBean }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "gameInventoryDetailsBean")
	public JAXBElement<GameInventoryDetailsBean> createGameInventoryDetailsBean(
			GameInventoryDetailsBean value) {
		return new JAXBElement<GameInventoryDetailsBean>(
				_GameInventoryDetailsBean_QNAME,
				GameInventoryDetailsBean.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link SavePurchaseTicket }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "savePurchaseTicket")
	public JAXBElement<SavePurchaseTicket> createSavePurchaseTicket(
			SavePurchaseTicket value) {
		return new JAXBElement<SavePurchaseTicket>(_SavePurchaseTicket_QNAME,
				SavePurchaseTicket.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GameBean }{@code
	 * >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "gameBean")
	public JAXBElement<GameBean> createGameBean(GameBean value) {
		return new JAXBElement<GameBean>(_GameBean_QNAME, GameBean.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link TicketPurchaseBean }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "ticketPurchaseBean")
	public JAXBElement<TicketPurchaseBean> createTicketPurchaseBean(
			TicketPurchaseBean value) {
		return new JAXBElement<TicketPurchaseBean>(_TicketPurchaseBean_QNAME,
				TicketPurchaseBean.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ReprintTicketResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "reprintTicketResponse")
	public JAXBElement<ReprintTicketResponse> createReprintTicketResponse(
			ReprintTicketResponse value) {
		return new JAXBElement<ReprintTicketResponse>(
				_ReprintTicketResponse_QNAME, ReprintTicketResponse.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link UpdateClaimStatus }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "updateClaimStatus")
	public JAXBElement<UpdateClaimStatus> createUpdateClaimStatus(
			UpdateClaimStatus value) {
		return new JAXBElement<UpdateClaimStatus>(_UpdateClaimStatus_QNAME,
				UpdateClaimStatus.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link UpdateClaimStatusResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "updateClaimStatusResponse")
	public JAXBElement<UpdateClaimStatusResponse> createUpdateClaimStatusResponse(
			UpdateClaimStatusResponse value) {
		return new JAXBElement<UpdateClaimStatusResponse>(
				_UpdateClaimStatusResponse_QNAME,
				UpdateClaimStatusResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link NewGameUploadResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "newGameUploadResponse")
	public JAXBElement<NewGameUploadResponse> createNewGameUploadResponse(
			NewGameUploadResponse value) {
		return new JAXBElement<NewGameUploadResponse>(
				_NewGameUploadResponse_QNAME, NewGameUploadResponse.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GameBasicDetailBean }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "gameBasicDetailBean")
	public JAXBElement<GameBasicDetailBean> createGameBasicDetailBean(
			GameBasicDetailBean value) {
		return new JAXBElement<GameBasicDetailBean>(_GameBasicDetailBean_QNAME,
				GameBasicDetailBean.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link NewGameUpload }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "newGameUpload")
	public JAXBElement<NewGameUpload> createNewGameUpload(NewGameUpload value) {
		return new JAXBElement<NewGameUpload>(_NewGameUpload_QNAME,
				NewGameUpload.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link PayPwtTicketResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "payPwtTicketResponse")
	public JAXBElement<PayPwtTicketResponse> createPayPwtTicketResponse(
			PayPwtTicketResponse value) {
		return new JAXBElement<PayPwtTicketResponse>(
				_PayPwtTicketResponse_QNAME, PayPwtTicketResponse.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link SavePurchaseTicketResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "savePurchaseTicketResponse")
	public JAXBElement<SavePurchaseTicketResponse> createSavePurchaseTicketResponse(
			SavePurchaseTicketResponse value) {
		return new JAXBElement<SavePurchaseTicketResponse>(
				_SavePurchaseTicketResponse_QNAME,
				SavePurchaseTicketResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link OnStartGameDataResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "onStartGameDataResponse")
	public JAXBElement<OnStartGameDataResponse> createOnStartGameDataResponse(
			OnStartGameDataResponse value) {
		return new JAXBElement<OnStartGameDataResponse>(
				_OnStartGameDataResponse_QNAME, OnStartGameDataResponse.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link InventoryUpload }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "inventoryUpload")
	public JAXBElement<InventoryUpload> createInventoryUpload(
			InventoryUpload value) {
		return new JAXBElement<InventoryUpload>(_InventoryUpload_QNAME,
				InventoryUpload.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link PayPwtTicket }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "payPwtTicket")
	public JAXBElement<PayPwtTicket> createPayPwtTicket(PayPwtTicket value) {
		return new JAXBElement<PayPwtTicket>(_PayPwtTicket_QNAME,
				PayPwtTicket.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link CancelTicketProcess }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "cancelTicketProcess")
	public JAXBElement<CancelTicketProcess> createCancelTicketProcess(
			CancelTicketProcess value) {
		return new JAXBElement<CancelTicketProcess>(_CancelTicketProcess_QNAME,
				CancelTicketProcess.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link SaveStartGameResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://instantPrint.ipe.skilrock.com/", name = "saveStartGameResponse")
	public JAXBElement<SaveStartGameResponse> createSaveStartGameResponse(
			SaveStartGameResponse value) {
		return new JAXBElement<SaveStartGameResponse>(
				_SaveStartGameResponse_QNAME, SaveStartGameResponse.class,
				null, value);
	}

}
