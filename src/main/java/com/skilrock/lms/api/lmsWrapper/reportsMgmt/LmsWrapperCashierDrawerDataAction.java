package com.skilrock.lms.api.lmsWrapper.reportsMgmt;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCashierDrawerDataReportBean;
import com.skilrock.lms.api.lmsWrapper.common.WrapperUtility;
import com.skilrock.lms.beans.CashChqPmntBean;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.CashierDrawerDataReportBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashChqReportsHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CashierDrawerDataHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;

public class LmsWrapperCashierDrawerDataAction {

	public HashMap<String,LmsWrapperCashierDrawerDataReportBean> fetchCashierWiseDrawerData(LmsWrapperCashierDrawerDataReportBean cashierDrawerDataBean) throws NumberFormatException, LMSException, ParseException {
		
		String reportTime=cashierDrawerDataBean.getReportTime();
		String reportType=cashierDrawerDataBean.getReportType();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Timestamp startDate = null;
		Timestamp endDate = null;
        Date firstDate=null;
		Date lastDate=null;
		boolean isExpand = false;
		String name = "-1";
		int userId=WrapperUtility.getUserIdFromUserName(cashierDrawerDataBean.getUserName());
		if("Date Wise".equalsIgnoreCase(reportTime)){
			try {
				
				startDate = new Timestamp(( format.parse(
						cashierDrawerDataBean.getStartDate()).getTime()));
				firstDate=new Date(startDate.getTime());
				endDate = new Timestamp((format.parse(
						cashierDrawerDataBean.getEndDate()).getTime()
						+ 24 * 60 * 60 * 1000 - 1000));
				lastDate=new Date(endDate.getTime()+1000);
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			
		}else{
			Calendar calendar=Calendar.getInstance();
			 calendar.set(Calendar.HOUR_OF_DAY, 0);
			    calendar.set(Calendar.MINUTE, 0);
			    calendar.set(Calendar.SECOND, 0);
			    calendar.set(Calendar.MILLISECOND, 0);
			System.out.println(new Timestamp(calendar.getTimeInMillis()));
			startDate=new Timestamp(calendar.getTimeInMillis());
			endDate=new Timestamp(calendar.getTimeInMillis()+ 24 * 60 * 60 * 1000 - 1000);
			System.out.println(calendar.getTime());
			firstDate=new Date(calendar.getTimeInMillis());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			System.out.println(calendar.getTime());
			lastDate=new Date(calendar.getTimeInMillis());
		}
		
		String lastArchDate;
		
		lastArchDate = CommonMethods.getLastArchDate();
		SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date oldDate = formatOld.parse(cashierDrawerDataBean.getStartDate());
		System.out.println("last archieve date"+lastArchDate);
		Calendar calStart = Calendar.getInstance();
		Calendar calLast = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date devLastDate = format1.parse(lastArchDate);
		java.util.Date devStartDate = format1.parse(format1.format(oldDate));
		calStart.setTime(devStartDate);
		calLast.setTime(devLastDate);
		
		if(calStart.before(calLast) || calStart.equals(calLast) || name.equalsIgnoreCase("-1"))
		{
			isExpand = true;
		}
		else if(calStart.after(calLast))
		{
			isExpand = false;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		HashMap<String,LmsWrapperCashierDrawerDataReportBean> wrapperCashierDrawerDataReportBeanMap=new HashMap<String, LmsWrapperCashierDrawerDataReportBean>();
		CashierDrawerDataHelper helper=new CashierDrawerDataHelper();
		CashChqReportsHelper oldhelper = new CashChqReportsHelper(firstDate,lastDate);
		List<CashChqReportBean> chqDataList=null;
		
		List<Integer> agtOrgIdList = oldhelper.getAgentOrgList();
		//SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		//Timestamp startDate = new Timestamp(dateFormat.parse(cashierDrawerDataBean.getStartDate()).getTime());
		//Timestamp endDate = new Timestamp(dateFormat.parse(cashierDrawerDataBean.getEndDate()).getTime());
		OrganizationTerminateReportHelper.getTerminateAgentListForRep(startDate, endDate);
		List<Integer> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdIntTypeList;
		agtOrgIdList.removeAll(terminateAgentList);
		
		
		/*//remove terminate Agent
		OrganizationTerminateReportHelper.getTerminateAgentListForRep(new Timestamp(firstDate.getTime()),new Timestamp(lastDate.getTime()+ 1*24*60*60*1000-1000) );
		List<Integer> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdIntTypeList;
          System.out.println("Terminate agent List::"+terminateAgentList);
          agtOrgIdList.removeAll(terminateAgentList);
		*/
          
		CashChqReportBean cashReportBean=null;
		CashChqPmntBean cashPaymentBean=null;
          
		if ("Agentwise".equalsIgnoreCase(reportType)) {
			chqDataList = oldhelper.getCashChqDetail(agtOrgIdList,Integer.parseInt(name),isExpand);
			for(int i=0;i<chqDataList.size();i++){
				cashReportBean=chqDataList.get(i);
				LmsWrapperCashierDrawerDataReportBean wrapperCashierDrawerDataReportBean = new LmsWrapperCashierDrawerDataReportBean();
				wrapperCashierDrawerDataReportBean.setBankDeposit(cashReportBean.getBankDeposit());
				wrapperCashierDrawerDataReportBean.setCheqBounce(cashReportBean.getCheqBounce());
				wrapperCashierDrawerDataReportBean.setCredit(cashReportBean.getCredit());
				wrapperCashierDrawerDataReportBean.setDebit(cashReportBean.getDebit());
				wrapperCashierDrawerDataReportBean.setTotalCash(cashReportBean.getTotalCash());
				wrapperCashierDrawerDataReportBean.setTotalChq(cashReportBean.getTotalChq());
				wrapperCashierDrawerDataReportBean.setNetAmt(cashReportBean.getNetAmt());
				wrapperCashierDrawerDataReportBean.setName(cashReportBean.getName());
				wrapperCashierDrawerDataReportBean.setUserId(cashReportBean.getOrgId());
				wrapperCashierDrawerDataReportBean.setTotalWinningAmt("0.0");
				wrapperCashierDrawerDataReportBeanMap.put(cashReportBean.getName(), wrapperCashierDrawerDataReportBean);

			}
			
			
		} else if("Daywise".equalsIgnoreCase(reportType)){
			chqDataList = oldhelper.getCashChqDetailDayWise(Integer.parseInt(name),isExpand);
			for(int i=0;i<chqDataList.size();i++){
				cashReportBean=chqDataList.get(i);
				LmsWrapperCashierDrawerDataReportBean wrapperCashierDrawerDataReportBean = new LmsWrapperCashierDrawerDataReportBean();
				wrapperCashierDrawerDataReportBean.setBankDeposit(cashReportBean.getBankDeposit());
				wrapperCashierDrawerDataReportBean.setCheqBounce(cashReportBean.getCheqBounce());
				wrapperCashierDrawerDataReportBean.setCredit(cashReportBean.getCredit());
				wrapperCashierDrawerDataReportBean.setDebit(cashReportBean.getDebit());
				wrapperCashierDrawerDataReportBean.setTotalCash(cashReportBean.getTotalCash());
				wrapperCashierDrawerDataReportBean.setTotalChq(cashReportBean.getTotalChq());
				wrapperCashierDrawerDataReportBean.setNetAmt(cashReportBean.getNetAmt());
				wrapperCashierDrawerDataReportBean.setName(cashReportBean.getName());
				wrapperCashierDrawerDataReportBean.setUserId(cashReportBean.getOrgId());
				wrapperCashierDrawerDataReportBean.setTotalWinningAmt("0.0");
				wrapperCashierDrawerDataReportBeanMap.put(cashReportBean.getName(), wrapperCashierDrawerDataReportBean);

			}
			
		} else if("Userwise".equalsIgnoreCase(reportType) || "Self".equals(reportType)){
			
		List<CashierDrawerDataReportBean> drawerDataReportBeanList=helper.fetchCashierWiseDrawerData(startDate,endDate,userId,reportType);
		CashierDrawerDataReportBean drawerDataReportBean=null;
		
		
		for(int i=0;i<drawerDataReportBeanList.size();i++){
			drawerDataReportBean=drawerDataReportBeanList.get(i);
			LmsWrapperCashierDrawerDataReportBean wrapperCashierDrawerDataReportBean = new LmsWrapperCashierDrawerDataReportBean();
			wrapperCashierDrawerDataReportBean.setBankDeposit(drawerDataReportBean.getBankDeposit());
			wrapperCashierDrawerDataReportBean.setCheqBounce(drawerDataReportBean.getCheqBounce());
			wrapperCashierDrawerDataReportBean.setCredit(drawerDataReportBean.getCredit());
			wrapperCashierDrawerDataReportBean.setDebit(drawerDataReportBean.getDebit());
			wrapperCashierDrawerDataReportBean.setTotalCash(drawerDataReportBean.getTotalCash());
			wrapperCashierDrawerDataReportBean.setTotalChq(drawerDataReportBean.getTotalChq());
			wrapperCashierDrawerDataReportBean.setTotalWinningAmt(drawerDataReportBean.getWinnnigAmt());
			wrapperCashierDrawerDataReportBean.setNetAmt(drawerDataReportBean.getNetAmt());
			wrapperCashierDrawerDataReportBean.setName(drawerDataReportBean.getName());
			wrapperCashierDrawerDataReportBean.setUserId(drawerDataReportBean.getUserId());
			
			wrapperCashierDrawerDataReportBeanMap.put(drawerDataReportBean.getName(), wrapperCashierDrawerDataReportBean);
		}
		}else if("AGENTWISEVOUCHER".equals(reportType)){
			int orgId=WrapperUtility.getAgentIdFromAgentName(cashierDrawerDataBean.getName());
			 List<CashChqPmntBean> paymentBeanList = oldhelper.getCashChqDetailDateWise(orgId,-1);
				for(int i=0;i<paymentBeanList.size();i++){
					cashPaymentBean=paymentBeanList.get(i);
					LmsWrapperCashierDrawerDataReportBean wrapperCashierDrawerDataReportBean = new LmsWrapperCashierDrawerDataReportBean();
					wrapperCashierDrawerDataReportBean.setPaymentAmount(cashPaymentBean.getPaymentAmount());
					wrapperCashierDrawerDataReportBean.setPaymentType(cashPaymentBean.getPaymentType());
					wrapperCashierDrawerDataReportBean.setVoucherNo(cashPaymentBean.getVoucherNo());
					wrapperCashierDrawerDataReportBean.setDate(cashPaymentBean.getDate());
					wrapperCashierDrawerDataReportBean.setBankName(cashPaymentBean.getBankName());
					wrapperCashierDrawerDataReportBeanMap.put(cashPaymentBean.getVoucherNo()+cashPaymentBean.getDate()+cashPaymentBean.getPaymentAmount(), wrapperCashierDrawerDataReportBean);
				}
		}else if("DAYWISEVOUCHER".equals(reportType)){
			 List<CashChqPmntBean> paymentBeanList = oldhelper.getCashChqDetailAgentWise(cashierDrawerDataBean.getEndDate(), cashierDrawerDataBean.getEndDate()+ " 23:59:59",-1,isExpand,"-1","-1");
			
				for(int i=0;i<paymentBeanList.size();i++){
					cashPaymentBean=paymentBeanList.get(i);
					LmsWrapperCashierDrawerDataReportBean wrapperCashierDrawerDataReportBean = new LmsWrapperCashierDrawerDataReportBean();
					wrapperCashierDrawerDataReportBean.setPaymentAmount(cashPaymentBean.getPaymentAmount());
					wrapperCashierDrawerDataReportBean.setPaymentType(cashPaymentBean.getPaymentType());
					wrapperCashierDrawerDataReportBean.setVoucherNo(cashPaymentBean.getVoucherNo());
					wrapperCashierDrawerDataReportBean.setDate(cashPaymentBean.getDate());
					wrapperCashierDrawerDataReportBean.setBankName(cashPaymentBean.getBankName());
					wrapperCashierDrawerDataReportBeanMap.put(cashPaymentBean.getVoucherNo()+cashPaymentBean.getDate()+cashPaymentBean.getPaymentAmount(), wrapperCashierDrawerDataReportBean);
				}
		}else if("CASHIERWISEVOUCHER".equals(reportType)){
            int userId1=WrapperUtility.getUserIdFromUserName(cashierDrawerDataBean.getName());
			chqDataList = oldhelper.getCashChqDetailUserAgentWise(agtOrgIdList,userId1);
			for(int i=0;i<chqDataList.size();i++){
				cashReportBean=chqDataList.get(i);
				LmsWrapperCashierDrawerDataReportBean wrapperCashierDrawerDataReportBean = new LmsWrapperCashierDrawerDataReportBean();
				wrapperCashierDrawerDataReportBean.setBankDeposit(cashReportBean.getBankDeposit());
				wrapperCashierDrawerDataReportBean.setCheqBounce(cashReportBean.getCheqBounce());
				wrapperCashierDrawerDataReportBean.setCredit(cashReportBean.getCredit());
				wrapperCashierDrawerDataReportBean.setDebit(cashReportBean.getDebit());
				wrapperCashierDrawerDataReportBean.setTotalCash(cashReportBean.getTotalCash());
				wrapperCashierDrawerDataReportBean.setTotalChq(cashReportBean.getTotalChq());
				wrapperCashierDrawerDataReportBean.setNetAmt(cashReportBean.getNetAmt());
				wrapperCashierDrawerDataReportBean.setName(cashReportBean.getName());
				wrapperCashierDrawerDataReportBean.setUserId(cashReportBean.getOrgId());
				wrapperCashierDrawerDataReportBean.setTotalWinningAmt("0.0");
				wrapperCashierDrawerDataReportBeanMap.put(cashReportBean.getName(), wrapperCashierDrawerDataReportBean);

			}
			
			
		
		}
		
		return wrapperCashierDrawerDataReportBeanMap;
		
	}
}
