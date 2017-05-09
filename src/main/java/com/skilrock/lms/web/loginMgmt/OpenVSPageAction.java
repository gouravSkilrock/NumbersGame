package com.skilrock.lms.web.loginMgmt;

import java.sql.Connection;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.beans.VSRegistrationDataBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.userMgmt.common.VirtualSportsIntegration;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSResponseBean;
import com.skilrock.lms.coreEngine.virtualSport.common.VSException;
import com.skilrock.lms.coreEngine.virtualSport.common.controllerImpl.CommonMethodsControllerImpl;

public class OpenVSPageAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String userType;
	private String finalURL;
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public OpenVSPageAction() {
		super(OpenVSPageAction.class);
	}
	

	public String getFinalURL() {
		return finalURL;
	}

	public void setFinalURL(String finalURL) {
		this.finalURL = finalURL;
	}

	public String openTab() throws Exception {
		String retHardwareId = null;
		String pinHash = null;
		String shopEntityId = null ;
		String printerEntityId = null ;
		String retailerEntityId = null ;
		String url = null;
		String sourceId = null;
		Connection conn = null ;
		VSRegistrationDataBean requiredData = null;
		try{
			requiredData = CommonMethodsControllerImpl.getInstance().verifyAndFetchVSUser(getUserBean().getUserName().trim());
			url = (String) LMSUtility.sc.getAttribute("VIRTUAL_BETTING_API_URL");
			sourceId = (String) LMSUtility.sc.getAttribute("VIRTUAL_BETTING_SOURCE_ID");
			pinHash = requiredData.getPassword().replaceAll("==", "--");
			retHardwareId = requiredData.getVsPrinterId();
			shopEntityId = requiredData.getVsShopEntityId();
			printerEntityId = requiredData.getVsPrinterEntityId();
			retailerEntityId = requiredData.getVsRetailerEntityId() ;
			VSResponseBean responseBean = null ;
			VSResponseBean sessionBean = null;
			VSRequestBean vsRequestBean = null;
			
			if("RETAILER".equalsIgnoreCase(userType)){
				if(shopEntityId == null || (retHardwareId == null && printerEntityId == null) || retailerEntityId == null){
					conn = DBConnect.getConnection();
					UserInfoBean userInfoBean = getUserBean() ;
					vsRequestBean = new VSRequestBean(userInfoBean.getOrgName(), userInfoBean.getUserOrgId(), userInfoBean.getUserName(), userInfoBean.getUserId(), userInfoBean.getUserName());
					sessionBean = VirtualSportsIntegration.Single.INSTANCE.getInstance().createSession();
				
					if(shopEntityId == null){
						responseBean = VirtualSportsIntegration.Single.INSTANCE.getInstance().createShop(sessionBean.getVsCommonResponseBean().getSessionToken(), vsRequestBean);
					}
					else if(retHardwareId == null && printerEntityId == null){
						responseBean = VirtualSportsIntegration.Single.INSTANCE.getInstance().createHardware(sessionBean.getVsCommonResponseBean().getSessionToken(), shopEntityId, vsRequestBean);
						retHardwareId = responseBean.getVsCommonResponseBean().getNewhardWareId();
						printerEntityId = responseBean.getVsCommonResponseBean().getNewEntityId();
						responseBean = VirtualSportsIntegration.Single.INSTANCE.getInstance().configureDefaultSettings(sessionBean.getVsCommonResponseBean().getSessionToken(), printerEntityId, url, sourceId);
					}
					else if(retailerEntityId == null){
						responseBean = VirtualSportsIntegration.Single.INSTANCE.getInstance().createRetailer(sessionBean.getVsCommonResponseBean().getSessionToken(), shopEntityId, vsRequestBean);
						responseBean = VirtualSportsIntegration.Single.INSTANCE.getInstance().resetPassword(sessionBean.getVsCommonResponseBean().getSessionToken(), responseBean.getVsCommonResponseBean().getTargetId(), pinHash);
					}
				}
				finalURL = url+"cashier/index.html?t="+retHardwareId+"&pinHash="+pinHash;
			} 
		} catch (VSException vs){
			vs.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeResource(conn);
		}
		
		return SUCCESS;
	}
}