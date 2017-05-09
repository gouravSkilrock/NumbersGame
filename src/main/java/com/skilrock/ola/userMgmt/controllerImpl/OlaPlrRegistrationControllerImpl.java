package com.skilrock.ola.userMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrorProperty;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.ola.accMgmt.controllerImpl.OlaRetDepositControllerImpl;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositResponseBean;
import com.skilrock.ola.api.PlayerLotteryIntegration;
import com.skilrock.ola.commonMethods.daoImpl.OlaCommonMethodDaoImpl;
import com.skilrock.ola.javaBeans.CountryDataBean;
import com.skilrock.ola.userMgmt.daoImpl.OlaPlrRegistrationDaoImpl;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationRequestBean;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationResponseBean;

public class OlaPlrRegistrationControllerImpl  {
	
	static Log logger = LogFactory.getLog(OlaPlrRegistrationControllerImpl.class);
	
	public List<CountryDataBean> getCountryListMap() throws LMSException, GenericException {
		List<CountryDataBean> countryData = null;
		try{
			 countryData = new OlaPlrRegistrationDaoImpl().getCountryListMap();
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		}catch(Exception e){
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}
		return countryData;
	}
	
	public 	OlaPlayerRegistrationResponseBean  registerPlayer(OLADepositRequestBean depositReqBean,UserInfoBean userBean,OlaPlayerRegistrationRequestBean playerBean) throws LMSException, GenericException{
		OlaPlayerRegistrationResponseBean registrationResponseBean = null;	
		OlaPlrRegistrationDaoImpl registrationDao=new OlaPlrRegistrationDaoImpl();
		Connection con = DBConnect.getConnection();
		try{
			con.setAutoCommit(false);
			playerBean.setRegType("OLA");
			registrationDao.registerPlayer(playerBean, con);
			
			if("PLAYER_LOTTERY".equals(depositReqBean.getWalletDevName())){
				// call PMS API 
				registrationResponseBean= PlayerLotteryIntegration.newPMSPlayerRegistration(playerBean);
			}else if("TabletGaming".equals(depositReqBean.getWalletDevName()) || "GroupRummy".equals(depositReqBean.getWalletDevName()) ||"KhelPlayRummy".equals(depositReqBean.getWalletDevName()) ||"ALA_WALLET".equals(depositReqBean.getWalletDevName())){
				OLAUtility.newKpRummyPlayerRegistration(playerBean,depositReqBean.getWalletId());
				registrationResponseBean=new OlaPlayerRegistrationResponseBean();
				if(playerBean.isSuccess()){
					registrationResponseBean.setSuccess(true);
				}else{
					throw new LMSException(0,playerBean.getMsg());
				}
				
			}
			
			if(registrationResponseBean.isSuccess()){
				registrationDao.updateAccountIdAndPassword(playerBean, con);
				if("TabletGaming".equals(depositReqBean.getWalletDevName()) || "GroupRummy".equals(depositReqBean.getWalletDevName()) || "KhelPlayRummy".equals(depositReqBean.getWalletDevName()) ||"ALA_WALLET".equals(depositReqBean.getWalletDevName())){
					boolean isBind=OLAUtility.bindPlrAtKpRummy((playerBean.getUsername()==null || playerBean.getUsername().isEmpty())?playerBean.getPhone():playerBean.getUsername(), userBean.getUserOrgId(), depositReqBean.getWalletId());
					if(isBind){
						OlaCommonMethodDaoImpl.bindPlrNAffiliate(con, playerBean.getPlayerId(),userBean,depositReqBean.getWalletId());
					}else{
						throw new LMSException(0,LMSErrors.PLAYER_BINDING_ERROR_MESSAGE);
					}
				}
				con.commit();
				OlaRetDepositControllerImpl retDepControllerImpl = new OlaRetDepositControllerImpl();
				if(depositReqBean.getDepositAmt()>0){
					OLADepositResponseBean depResBean=null;
					try{
						depResBean =retDepControllerImpl.olaRetPlrDeposit(depositReqBean, userBean);	
					}catch (LMSException e) {
						if(e.getErrorCode()==10001){
							throw new LMSException(0,"Registration Succesfully!! "+LMSErrorProperty.getPropertyValue(e.getErrorCode())+e.getErrorMessage()+") !!");
						}else{
							throw new LMSException(0,"Registration Succesfully!! "+LMSErrorProperty.getPropertyValue(e.getErrorCode()));
						}
						
					}
					
					if(depResBean.isSuccess()){
						registrationResponseBean.setSuccess(true);
					}else{
						throw new LMSException(LMSErrors.REG_BUT_NOT_DEPOSIT_ERROR_CODE);
					}
				}else{
					registrationResponseBean.setSuccess(true);
				}
				
				//String returnType="true";
				
			}else{
				throw new LMSException(LMSErrors.PMS_REG_PLAYER_FAILED_ERROR_CODE);
			}		
		} catch (LMSException e) {
			e.printStackTrace();
			throw e;
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		}catch(Exception e){
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}
		return registrationResponseBean;
		//return "true";	
	}
	
	
	
	



}
