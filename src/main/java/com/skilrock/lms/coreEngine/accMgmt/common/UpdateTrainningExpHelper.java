package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.skilrock.lms.beans.TrainingExpenseBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class UpdateTrainningExpHelper {

	public Map<Integer, TrainingExpenseBean> fetchAgentTrainingExp(
			String trainingType,int gameNo, int serviceId) {
		Connection con = DBConnect.getConnection();
		TrainingExpenseBean bean = null;
		Map<Integer, TrainingExpenseBean> orgTrainMap = new LinkedHashMap<Integer, TrainingExpenseBean>();
		try {
			/*String selQry = "select organization_id,name,value,training_exp_var from (select organization_id,name,ifnull(training_exp_var,0.0) training_exp_var from st_lms_organization_master left outer join st_lms_agent_"
					+ trainingType.toLowerCase()
					+ "_trng_exp_var_mapping  on organization_id=agent_org_id where organization_type='AGENT') tr,(select value from st_lms_property_master where property_code='"
					+ trainingType.toLowerCase() + "_training_exp_per') dtr";*/
			
			String orgCodeQry = " name orgCode  ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode  ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat( org_code,'_',name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
			

			}
			//String selQry="select organization_id,orgCode,training_exp_default,training_exp_var,training_exp_extra,extra_training_exp_var from (select organization_id,"+orgCodeQry+" ,ifnull(training_exp_var,0.0) training_exp_var,ifnull(extra_training_exp_var,0.0)extra_training_exp_var from st_lms_organization_master left outer join (select agent_org_id,training_exp_var,extra_training_exp_var from st_lms_agent_"+trainingType.toLowerCase()+"_trng_exp_var_mapping  where game_id=?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default,training_exp_extra  from st_lms_agent_default_"+trainingType.toLowerCase()+"_training_exp where game_id=?) dtr order by "+QueryManager.getAppendOrgOrder();
//			String selQry="select organization_id,orgCode,training_exp_default,training_exp_var,training_exp_extra,extra_training_exp_var from (select organization_id,orgCode,training_exp_default,training_exp_var,training_exp_extra,extra_training_exp_var from (select organization_id,"+orgCodeQry+" ,ifnull(training_exp_var,0.0) training_exp_var,ifnull(extra_training_exp_var,0.0)extra_training_exp_var from st_lms_organization_master left outer join (select agent_org_id,training_exp_var,extra_training_exp_var from st_lms_agent_"+trainingType.toLowerCase()+"_trng_exp_var_mapping  where game_id=?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default,training_exp_extra  from st_lms_agent_default_"+trainingType.toLowerCase()+"_training_exp where game_id=?) dtr order by "+QueryManager.getAppendOrgOrder()+"  ) main where main.orgCode not in (select name orgCode from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id = um.organization_id where um.status ='TERMINATE');";

			//String selQry="select organization_id,orgCode  ,value,training_exp_var from (select organization_id,"+orgCodeQry+" ,ifnull(training_exp_var,0.0) training_exp_var from st_lms_organization_master left outer join (select agent_org_id,training_exp_var from st_lms_agent_"+trainingType.toLowerCase()+"_trng_exp_var_mapping  where game_id=?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default value from st_lms_agent_default_"+trainingType.toLowerCase()+"_training_exp where game_id=?) dtr order by "+QueryManager.getAppendOrgOrder();
			String selQry = "select organization_id,orgCode,training_exp_default,training_exp_var,training_exp_extra,extra_training_exp_var,incentive_exp_default,incentive_exp_var from (select organization_id,orgCode,training_exp_default,training_exp_var,training_exp_extra,extra_training_exp_var,incentive_exp_default,incentive_exp_var from (select organization_id,"+orgCodeQry+",ifnull(training_exp_var,0.0) training_exp_var,ifnull(extra_training_exp_var,0.0)extra_training_exp_var,ifnull(incentive_exp_var,0.0) incentive_exp_var from st_lms_organization_master left outer join (select agent_org_id,training_exp_var,extra_training_exp_var,incentive_exp_var from st_lms_agent_"+trainingType.toLowerCase()+"_trng_exp_var_mapping  where service_id = ? and game_id=?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default,training_exp_extra,incentive_exp_default from st_lms_agent_default_"+trainingType.toLowerCase()+"_training_exp where service_id = ? and game_id=?) dtr order by "+QueryManager.getAppendOrgOrder()+") main where main.orgCode not in (select name orgCode from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id = um.organization_id where um.status ='TERMINATE');";
			PreparedStatement pstmt = con.prepareStatement(selQry);
			pstmt.setInt(1, serviceId);
			pstmt.setInt(2, gameNo);
			pstmt.setInt(3, serviceId);
			pstmt.setInt(4, gameNo);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TrainingExpenseBean();
				bean.setOrgId(rs.getInt("organization_id"));
				bean.setOrgName(rs.getString("orgCode"));
				bean.setTrainingPer(rs.getDouble("training_exp_default"));
				bean.setTrainingPerVariance(rs.getDouble("training_exp_var"));
				bean.setExtraTrainingPer(rs.getDouble("training_exp_extra"));
				bean.setExtraTrainingPerVariance(rs.getDouble("extra_training_exp_var"));
				bean.setIncentivePer(rs.getDouble("incentive_exp_default"));
				bean.setIncentivePerVariance(rs.getDouble("incentive_exp_var"));
				orgTrainMap.put(rs.getInt("organization_id"), bean);
			}
		/*	String selQry="select organization_id,orgCode  ,value,training_exp_var from (select organization_id,"+orgCodeQry+" ,ifnull(training_exp_var,0.0) training_exp_var from st_lms_organization_master left outer join (select agent_org_id,training_exp_var from st_lms_agent_"+trainingType.toLowerCase()+"_trng_exp_var_mapping  where game_id=?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default value from st_lms_agent_default_"+trainingType.toLowerCase()+"_training_exp where game_id=?) dtr order by "+QueryManager.getAppendOrgOrder();
			PreparedStatement pstmt = con.prepareStatement(selQry);
			pstmt.setInt(1, gameNo);
			pstmt.setInt(2, gameNo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TrainingExpenseBean();
				bean.setOrgId(rs.getInt("organization_id"));
				bean.setOrgName(rs.getString("orgCode"));
				bean.setTrainingPer(rs.getDouble("value"));
				bean.setTrainingPerVariance(rs.getDouble("training_exp_var"));
				orgTrainMap.put(rs.getInt("organization_id"), bean);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return orgTrainMap;
	}

	public Map<Integer, TrainingExpenseBean> fetchDailyAgentTrainingExp(
			String trainingType,int gameNo, int serviceId) {
		Connection con = DBConnect.getConnection();
		TrainingExpenseBean bean = null;
		Map<Integer, TrainingExpenseBean> orgTrainMap = new LinkedHashMap<Integer, TrainingExpenseBean>();
		try {
			/*String selQry = "select organization_id,name,value,training_exp_var from (select organization_id,name,ifnull(training_exp_var,0.0) training_exp_var from st_lms_organization_master left outer join st_lms_agent_"
					+ trainingType.toLowerCase()
					+ "_trng_exp_var_mapping  on organization_id=agent_org_id where organization_type='AGENT') tr,(select value from st_lms_property_master where property_code='"
					+ trainingType.toLowerCase() + "_training_exp_per') dtr";*/
			
			String orgCodeQry = " name orgCode  ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode  ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat( org_code,'_',name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
			

			}
			//add extra commission variance for slotted time
			//String selQry="select organization_id,orgCode,training_exp_default,training_exp_var,training_exp_extra,extra_training_exp_var from (select organization_id,"+orgCodeQry+" ,ifnull(training_exp_var,0.0) training_exp_var,ifnull(extra_training_exp_var,0.0)extra_training_exp_var from st_lms_organization_master left outer join (select agent_org_id,training_exp_var,extra_training_exp_var from st_lms_agent_"+trainingType.toLowerCase()+"_trng_exp_var_mapping  where game_id=?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default,training_exp_extra  from st_lms_agent_default_"+trainingType.toLowerCase()+"_training_exp where game_id=?) dtr order by "+QueryManager.getAppendOrgOrder();
//			String selQry="select organization_id,orgCode,training_exp_default,training_exp_var,training_exp_extra,extra_training_exp_var from (select organization_id,orgCode,training_exp_default,training_exp_var,training_exp_extra,extra_training_exp_var from (select organization_id,"+orgCodeQry+" ,ifnull(training_exp_var,0.0) training_exp_var,ifnull(extra_training_exp_var,0.0)extra_training_exp_var from st_lms_organization_master left outer join (select agent_org_id,training_exp_var,extra_training_exp_var from st_lms_agent_"+trainingType.toLowerCase()+"_trng_exp_var_mapping  where game_id=?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default,training_exp_extra  from st_lms_agent_default_"+trainingType.toLowerCase()+"_training_exp where game_id=?) dtr order by "+QueryManager.getAppendOrgOrder()+"  ) main where main.orgCode not in (select name orgCode from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id = um.organization_id where um.status ='TERMINATE' );";
			//String selQry="select organization_id,orgCode  ,value,training_exp_var from (select organization_id,"+orgCodeQry+" ,ifnull(training_exp_var,0.0) training_exp_var from st_lms_organization_master left outer join (select agent_org_id,training_exp_var from st_lms_agent_"+trainingType.toLowerCase()+"_trng_exp_var_mapping  where game_id=?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default value from st_lms_agent_default_"+trainingType.toLowerCase()+"_training_exp where game_id=?) dtr order by "+QueryManager.getAppendOrgOrder();
			String selQry = "select organization_id,orgCode,training_exp_default,training_exp_var,training_exp_extra,extra_training_exp_var,incentive_exp_default,incentive_exp_var from (select organization_id,orgCode,training_exp_default,training_exp_var,training_exp_extra,extra_training_exp_var,incentive_exp_default,incentive_exp_var from (select organization_id,"+orgCodeQry+",ifnull(training_exp_var,0.0) training_exp_var,ifnull(extra_training_exp_var,0.0)extra_training_exp_var,ifnull(incentive_exp_var,0.0) incentive_exp_var from st_lms_organization_master left outer join (select agent_org_id,training_exp_var,extra_training_exp_var,incentive_exp_var from st_lms_agent_"+trainingType.toLowerCase()+"_trng_exp_var_mapping  where service_id = ? and  game_id=?) map on organization_id=map.agent_org_id where organization_type='AGENT') tr,(select training_exp_default,training_exp_extra,incentive_exp_default  from st_lms_agent_default_"+trainingType.toLowerCase()+"_training_exp where service_id = ? and game_id=?) dtr order by "+QueryManager.getAppendOrgOrder()+") main where main.orgCode not in (select name orgCode from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id = um.organization_id where um.status ='TERMINATE' );";
			PreparedStatement pstmt = con.prepareStatement(selQry);
			pstmt.setInt(1, serviceId);
			pstmt.setInt(2, gameNo);
			pstmt.setInt(3, serviceId);
			pstmt.setInt(4, gameNo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TrainingExpenseBean();
				bean.setOrgId(rs.getInt("organization_id"));
				bean.setOrgName(rs.getString("orgCode"));
				bean.setTrainingPer(rs.getDouble("training_exp_default"));
				bean.setTrainingPerVariance(rs.getDouble("training_exp_var"));
				bean.setExtraTrainingPer(rs.getDouble("training_exp_extra"));
				bean.setExtraTrainingPerVariance(rs.getDouble("extra_training_exp_var"));
				bean.setIncentivePer(rs.getDouble("incentive_exp_default"));
				bean.setIncentivePerVariance(rs.getDouble("incentive_exp_var"));
				orgTrainMap.put(rs.getInt("organization_id"), bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return orgTrainMap;
	}
	
	public void updateAgentTrExp(int[] agtId, double[] trVar, double[] extraTrVar, double[] incentiveVar, int userId, String trType, Map<Integer, TrainingExpenseBean> tranOrgMap, int gameNo, int serviceId) {
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			StringBuilder agtIdStr = new StringBuilder("");
			for (int i = 0; i < agtId.length; i++) {
				if (tranOrgMap.get(agtId[i]).getTrainingPerVariance() != trVar[i] || tranOrgMap.get(agtId[i]).getExtraTrainingPerVariance() != extraTrVar[i] || (serviceId == 7 && tranOrgMap.get(agtId[i]).getIncentivePerVariance() != incentiveVar[i])) {
					agtIdStr.append(agtId[i] + ",");
				}
			}
			agtIdStr.deleteCharAt(agtIdStr.length() - 1);

			StringBuilder insHisTlbQry = new StringBuilder();
			insHisTlbQry.append("insert into st_lms_agent_"+ trType.toLowerCase()+ "_trng_exp_var_mapping_history (agent_org_id, service_id,game_id,training_exp_var,extra_training_exp_var,");
			if(serviceId == 7){
				insHisTlbQry.append("incentive_exp_var,");
			}
			insHisTlbQry.append("updated_by_user_id,updated_date) select agent_org_id, service_id, game_id,training_exp_var,extra_training_exp_var,");
			if(serviceId == 7){
				insHisTlbQry.append("incentive_exp_var,");
			}
			insHisTlbQry.append(		
					userId + ",now() from st_lms_agent_"
					+ trType.toLowerCase()
					+ "_trng_exp_var_mapping where service_id = " + serviceId
					+ " and game_id=" + gameNo + " and agent_org_id in(" + agtIdStr
					+ ")");
			
			PreparedStatement pstmt = con.prepareStatement(insHisTlbQry.toString());
			pstmt.executeUpdate();

			String delQry = "delete from st_lms_agent_" + trType.toLowerCase() + "_trng_exp_var_mapping where service_id = " + serviceId + " and game_id=" + gameNo + " and agent_org_id in(" + agtIdStr + ")";
			pstmt = con.prepareStatement(delQry);
			pstmt.executeUpdate();
			
			StringBuilder insMainQry = new StringBuilder();
			insMainQry.append("insert into st_lms_agent_" + trType.toLowerCase() + "_trng_exp_var_mapping (agent_org_id, service_id, game_id,training_exp_var,extra_training_exp_var");
			if(serviceId == 7){
				insMainQry.append(",incentive_exp_var");
			}
			insMainQry.append(") values");
			for (int i = 0; i < agtId.length; i++) {
				if (tranOrgMap.get(agtId[i]).getTrainingPerVariance() != trVar[i] || tranOrgMap.get(agtId[i]).getExtraTrainingPerVariance() != extraTrVar[i] || (serviceId == 7 && tranOrgMap.get(agtId[i]).getIncentivePerVariance() != incentiveVar[i])) {
					insMainQry.append("(" + agtId[i] + "," + serviceId + "," + gameNo + "," + trVar[i] + "," + extraTrVar[i]);
					if(serviceId == 7){
						insMainQry.append(","+ incentiveVar[i]);
					}
					insMainQry.append("),");
				}
			}
			
			insMainQry.deleteCharAt(insMainQry.length() - 1);
			pstmt = con.prepareStatement(insMainQry.toString());
			pstmt.executeUpdate();
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
	}

	public void updateDailyAgentTrExp(int[] agtId, double[] trVar, double[] extraTrVar, double[] incentiveVar, int userId, String trType, Map<Integer, TrainingExpenseBean> tranOrgMap, int gameNo, int serviceId) {
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			StringBuilder agtIdStr = new StringBuilder("");
			for (int i = 0; i < agtId.length; i++) {
				if (tranOrgMap.get(agtId[i]).getTrainingPerVariance() != trVar[i] || tranOrgMap.get(agtId[i]).getExtraTrainingPerVariance() != extraTrVar[i] || (serviceId == 7 && tranOrgMap.get(agtId[i]).getIncentivePerVariance() != incentiveVar[i])) {
					agtIdStr.append(agtId[i] + ",");
				}
			}
			agtIdStr.deleteCharAt(agtIdStr.length() - 1);
			StringBuilder insHisTlbQry = new StringBuilder();
			insHisTlbQry.append("insert into st_lms_agent_"+ trType.toLowerCase()+ "_trng_exp_var_mapping_history (agent_org_id, service_id,game_id,training_exp_var,extra_training_exp_var,");
					if(serviceId == 7){
						insHisTlbQry.append("incentive_exp_var,");
					}
					insHisTlbQry.append("updated_by_user_id,updated_date) select agent_org_id, service_id, game_id,training_exp_var,extra_training_exp_var,");
					if(serviceId == 7){
						insHisTlbQry.append("incentive_exp_var,");
					}
					insHisTlbQry.append(		
					userId + ",now() from st_lms_agent_"
					+ trType.toLowerCase()
					+ "_trng_exp_var_mapping where service_id = " + serviceId
					+ " and game_id=" + gameNo + " and agent_org_id in(" + agtIdStr
					+ ")");
			PreparedStatement pstmt = con.prepareStatement(insHisTlbQry.toString());
			pstmt.executeUpdate();

			String delQry = "delete from st_lms_agent_" + trType.toLowerCase() + "_trng_exp_var_mapping where service_id = " + serviceId + " and game_id=" + gameNo + " and agent_org_id in(" + agtIdStr + ")";
			pstmt = con.prepareStatement(delQry);
			pstmt.executeUpdate();
			StringBuilder insMainQry = new StringBuilder();
			insMainQry.append("insert into st_lms_agent_" + trType.toLowerCase() + "_trng_exp_var_mapping (agent_org_id, service_id, game_id,training_exp_var,extra_training_exp_var");
			if(serviceId == 7){
				insMainQry.append(",incentive_exp_var");
			}
			insMainQry.append(") values");
			for (int i = 0; i < agtId.length; i++) {
				if (tranOrgMap.get(agtId[i]).getTrainingPerVariance() != trVar[i] || tranOrgMap.get(agtId[i]).getExtraTrainingPerVariance() != extraTrVar[i] || (serviceId == 7 && tranOrgMap.get(agtId[i]).getIncentivePerVariance() != incentiveVar[i])) {
					insMainQry.append("(" + agtId[i] + "," + serviceId + "," + gameNo + "," + trVar[i] + "," + extraTrVar[i]);
					if(serviceId == 7){
						insMainQry.append(","+ incentiveVar[i]);
					}
					insMainQry.append("),");
				}
			}
			insMainQry.deleteCharAt(insMainQry.length() - 1);
			pstmt = con.prepareStatement(insMainQry.toString());
			pstmt.executeUpdate();
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
	}
}
