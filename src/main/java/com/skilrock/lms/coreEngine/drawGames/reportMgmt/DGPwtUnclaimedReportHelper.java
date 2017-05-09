package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.AgentPwtDetailBean;
import com.skilrock.lms.dge.beans.DrawPwtDetailBean;
import com.skilrock.lms.dge.beans.RetPwtDetailBean;

public class DGPwtUnclaimedReportHelper {
	static Log logger = LogFactory.getLog(DGPwtUnclaimedReportHelper.class);

	@SuppressWarnings("unchecked")
	public List<AgentPwtDetailBean> fetchDGPwtUnclaimed(UserInfoBean userBean,
			String startDate, String endDate) {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		DrawPwtDetailBean drawPwtBean = new DrawPwtDetailBean();
		List<AgentPwtDetailBean> agtList = new ArrayList<AgentPwtDetailBean>();
		Map<Integer, List<DrawPwtDetailBean>> unclaimRetMap = new HashMap<Integer, List<DrawPwtDetailBean>>();
		try {
			sReq.setServiceName(ServiceName.REPORTS_MGMT);
			sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UNCLM);
			IServiceDelegate delegate = ServiceDelegate.getInstance();

			drawPwtBean.setStartDate(startDate);
			drawPwtBean.setEndDate(endDate);
			sReq.setServiceData(drawPwtBean);
			sRes = delegate.getResponse(sReq);
			Connection con = DBConnect.getConnection();
			if (sRes.getIsSuccess()) {

				Type type = new TypeToken<Map<Integer, List<DrawPwtDetailBean>>>() {
				}.getType();
				
				unclaimRetMap = (Map<Integer, List<DrawPwtDetailBean>>)new Gson().fromJson( (JsonElement)sRes.getResponseData(), type);
				
				Map<Integer, Integer> retAgtMap = new HashMap<Integer, Integer>();
				Map<Integer, Double> agtTotWinMap = new HashMap<Integer, Double>();
				Map<Integer, Double> agtClaimedMap = new HashMap<Integer, Double>();
				Map<Integer, Double> agtClaimedAtBoMap = new HashMap<Integer, Double>();
				Map<Integer, Double> agtUnClaimed7Map = new HashMap<Integer, Double>();
				Map<Integer, Map<Integer, Double>> agtRetTotWinMap = new HashMap<Integer, Map<Integer, Double>>();
				Map<Integer, Map<Integer, Double>> agtRetUnclm7Map = new HashMap<Integer, Map<Integer, Double>>();
				Map<Integer, Map<Integer, Double>> agtRetClaimedMap = new HashMap<Integer, Map<Integer, Double>>();
				List<Integer> gameList = new ArrayList<Integer>();
				Date currDate = new Date();
				String retAgtQry = "select user.user_id,org.parent_id as AgtOrgId from st_lms_user_master user,st_lms_organization_master org where "
						+ " user.organization_id = org.organization_id and org.organization_type = 'RETAILER'";
				PreparedStatement pstmt = con.prepareStatement(retAgtQry);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					retAgtMap.put(rs.getInt("user_id"), rs.getInt("AgtOrgId"));
					agtTotWinMap.put(rs.getInt("AgtOrgId"), 0.0);
					agtClaimedMap.put(rs.getInt("AgtOrgId"), 0.0);
					agtClaimedAtBoMap.put(rs.getInt("AgtOrgId"), 0.0);
					agtUnClaimed7Map.put(rs.getInt("AgtOrgId"), 0.0);
					agtRetTotWinMap.put(rs.getInt("AgtOrgId"),
							new HashMap<Integer, Double>());
					agtRetUnclm7Map.put(rs.getInt("AgtOrgId"),
							new HashMap<Integer, Double>());
					agtRetClaimedMap.put(rs.getInt("AgtOrgId"),
							new HashMap<Integer, Double>());
				}
				pstmt = con
						.prepareStatement(" select game_id, game_name from st_dg_game_master where game_status = 'OPEN'");
				rs = pstmt.executeQuery();
				while (rs.next()) {
					gameList.add(rs.getInt("game_id"));
				}
				for (int i = 0; i < gameList.size(); i++) {
					int gameId = gameList.get(i);
					if( unclaimRetMap!=null  && unclaimRetMap.get(gameId) !=  null){
							List<DrawPwtDetailBean> repList = unclaimRetMap.get(gameId);
							for (int j = 0; j < repList.size(); j++) {
								DrawPwtDetailBean tempBean = repList.get(j);
								Date drawDate = new SimpleDateFormat("yyyy-MM-dd")
										.parse(tempBean.getDrawDateTime());
								long diff = currDate.getTime() - drawDate.getTime();
								System.out.println("diff b/w drawDate:" + drawDate
										+ " and currDate: " + currDate + " is :"
										+ (diff / (1000 * 60 * 60 * 24)));
								boolean is7days = (diff / (1000 * 60 * 60 * 24) > 7);
								tempBean.setUnclmAftr7Days(is7days);
								Map<Integer, Double> retWinMap = tempBean
										.getTotalWinMap();
								Set<Entry<Integer, Double>> retSet = retWinMap
										.entrySet();
								Iterator<Entry<Integer, Double>> itr = retSet
										.iterator();
								while (itr.hasNext()) {
									Map.Entry<Integer, Double> retEntry = itr.next();
									if(retAgtMap.containsKey(retEntry.getKey()))
									if (tempBean.isUnclmAftr7Days()) {
										agtUnClaimed7Map.put(
												retAgtMap.get(retEntry.getKey()),
												agtUnClaimed7Map.get(retAgtMap
														.get(retEntry.getKey()))
														+ retWinMap.get(retEntry
																.getKey()));
										double prevAmt = 0.0;
										if (agtRetUnclm7Map.get(
												retAgtMap.get(retEntry.getKey())).get(
												retEntry.getKey()) != null)
											prevAmt = agtRetUnclm7Map.get(
													retAgtMap.get(retEntry.getKey()))
													.get(retEntry.getKey());
										agtRetUnclm7Map.get(
												retAgtMap.get(retEntry.getKey())).put(
												retEntry.getKey(),
												prevAmt + retEntry.getValue());
									}
									if(retAgtMap.containsKey(retEntry.getKey()))
									agtTotWinMap.put(
											retAgtMap.get(retEntry.getKey()),
											agtTotWinMap.get(retAgtMap.get(retEntry
													.getKey()))
													+ retWinMap.get(retEntry.getKey()));
									double prevAmt1 = 0.0;
									if(retAgtMap.containsKey(retEntry.getKey()))
									if (agtRetTotWinMap.get(
											retAgtMap.get(retEntry.getKey())).get(
											retEntry.getKey()) != null)
										prevAmt1 = agtRetTotWinMap.get(
												retAgtMap.get(retEntry.getKey())).get(
												retEntry.getKey());
									if(retAgtMap.containsKey(retEntry.getKey()))
									agtRetTotWinMap.get(
											retAgtMap.get(retEntry.getKey())).put(
											retEntry.getKey(),
											prevAmt1 + retEntry.getValue());
		
								}
								// calculating pwt claimed @ bo end for agents...
								Map<Integer, Double> retBoClaimedMap = tempBean
										.getTotalClaimedAtBoMap();
								Set<Entry<Integer, Double>> retBoClmSet = retBoClaimedMap
										.entrySet();
								Iterator<Entry<Integer, Double>> itr1 = retBoClmSet
										.iterator();
								while (itr1.hasNext()) {
									Entry<Integer, Double> clmBoEntry = itr1.next();
									if(retAgtMap.containsKey(clmBoEntry.getKey()))
									agtClaimedAtBoMap.put(
											retAgtMap.get(clmBoEntry.getKey()),
											agtClaimedAtBoMap.get(retAgtMap
													.get(clmBoEntry.getKey()))
													+ retBoClaimedMap.get(clmBoEntry
															.getKey()));
								}// end of while
								String dirPlrAgt = "select som.name as agtname,som.organization_id as agtOrgId, totDirPlrPwt from(select agent_org_id as agt_id, sum(pwt_amt)as totDirPlrPwt "
										+ "from st_dg_agt_direct_plr_pwt where draw_id = "
										+ tempBean.getDrawId()
										+ " and game_id ="
										+ gameId
										+ " group by agent_org_id)agt,st_lms_organization_master som where som.organization_type= 'AGENT' and som.organization_id = agt.agt_id order by name";
								String pwtClaimedAgt = "select name,organization_id, totPwtAgt  from(select som.parent_id as agt_id, sum(totPwt) as totPwtAgt from"
										+ "(select retailer_org_id as ret_id, sum(pwt_amt)as totPwt from st_dg_ret_pwt_"
										+ gameId
										+ " where draw_id="
										+ tempBean.getDrawId()
										+ " group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by name";
								String retPwtQry = "select name,ret_id,parent_id, totPwt from (select retailer_user_id as ret_id,retailer_org_id, sum(pwt_amt)as totPwt from st_dg_ret_pwt_"
										+ gameId
										+ " where draw_id="
										+ tempBean.getDrawId()
										+ " group by retailer_org_id)pwt,st_lms_organization_master so where so.organization_id = pwt.retailer_org_id";
								PreparedStatement pwtPstmt = con
										.prepareStatement(pwtClaimedAgt);
								System.out.println("query for total claimed in LMS:::"
										+ pwtClaimedAgt);
								ResultSet pwtRs = pwtPstmt.executeQuery();
								while (pwtRs.next()) {
									agtClaimedMap.put(
											pwtRs.getInt("organization_id"),
											agtClaimedMap.get(pwtRs
													.getInt("organization_id"))
													+ pwtRs.getDouble("totPwtAgt"));
		
								}
								pwtPstmt = con.prepareStatement(dirPlrAgt);
								System.out
										.println("query for total directplr in LMS:::"
												+ dirPlrAgt);
								pwtRs = pwtPstmt.executeQuery();
								while (pwtRs.next()) {
									agtClaimedMap.put(pwtRs.getInt("agtOrgId"),
											agtClaimedMap.get(pwtRs.getInt("agtOrgId"))
													+ pwtRs.getDouble("totDirPlrPwt"));
								}
		
								pwtPstmt = con.prepareStatement(retPwtQry);
								pwtRs = pwtPstmt.executeQuery();
								while (pwtRs.next()) {
									double tempAmt = 0.0;
									if (agtRetClaimedMap.get(pwtRs.getInt("parent_id"))
											.get(pwtRs.getInt("ret_id")) != null)
										tempAmt = agtRetClaimedMap.get(
												pwtRs.getInt("parent_id")).get(
												pwtRs.getInt("ret_id"));
									agtRetClaimedMap
											.get(pwtRs.getInt("parent_id"))
											.put(pwtRs.getInt("ret_id"),
													tempAmt + pwtRs.getDouble("totPwt"));
								}
		
							}// end of draw wise loop
							System.out
									.println("Agent Map with total claimed+directplr for game no "
											+ gameId + "::" + agtClaimedMap);
							System.out
									.println("Agent Map with total unclaimed after 7 days without deducting claimed for game no "
											+ gameId + "::" + agtUnClaimed7Map);
					}
				}// end of game wise loop

				// finding unclaimed after 7 days for agents ......

				Set<Entry<Integer, Double>> agtSet = agtUnClaimed7Map
						.entrySet();
				Iterator<Entry<Integer, Double>> itr1 = agtSet.iterator();
				while (itr1.hasNext()) {
					Map.Entry<Integer, Double> agtEntry = itr1.next();
					double amt = agtUnClaimed7Map.get(agtEntry.getKey())
							- agtClaimedMap.get(agtEntry.getKey())
							- agtClaimedAtBoMap.get(agtEntry.getKey());
					agtUnClaimed7Map.put(agtEntry.getKey(), amt > 0.0 ? amt
							: 0.0);
				}// end of iterator

				// finding unclaimed after 7 days for retailers ......

				Set<Entry<Integer, Map<Integer, Double>>> retSet1 = agtRetUnclm7Map
						.entrySet();
				Iterator<Entry<Integer, Map<Integer, Double>>> itr2 = retSet1
						.iterator();
				while (itr2.hasNext()) {
					Entry<Integer, Map<Integer, Double>> retEntry = itr2.next();
					Map<Integer, Double> tempRetU7Map = agtRetUnclm7Map
							.get(retEntry.getKey());
					Map<Integer, Double> tempRetClmMap = agtRetClaimedMap
							.get(retEntry.getKey());
					Iterator<Integer> retItr = tempRetU7Map.keySet().iterator();
					while (retItr.hasNext()) {
						int key = retItr.next();
						double retAmt = tempRetU7Map.get(key);
						if (tempRetClmMap.get(key) != null) {
							tempRetU7Map.put(key,
									retAmt - tempRetClmMap.get(key));
						}
					}
				}// end of iterator

				System.out.println("Agent Map for total unclaimed amt in LMS"
						+ agtTotWinMap);
				String orgCodeQry = " name orgCode  ";

				if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
					orgCodeQry = " org_code orgCode ";

				} else if ((LMSFilterDispatcher.orgFieldType)
						.equalsIgnoreCase("CODE_NAME")) {
					orgCodeQry = " concat(org_code,'_',name)  orgCode ";

				} else if ((LMSFilterDispatcher.orgFieldType)
						.equalsIgnoreCase("NAME_CODE")) {
					orgCodeQry = " concat(name,'_',org_code)  orgCode ";

				}
				pstmt = con
						.prepareStatement("select organization_id,"
								+ orgCodeQry
								+ " from st_lms_organization_master where organization_type = 'AGENT' and organization_status != 'TERMINATE'  order by "
								+ QueryManager.getAppendOrgOrder());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					AgentPwtDetailBean agtBean = new AgentPwtDetailBean();
					List<RetPwtDetailBean> retList = new ArrayList<RetPwtDetailBean>();
					int agtOrgId = rs.getInt("organization_id");
					agtBean.setAgtOrgId(agtOrgId);
					agtBean.setName(rs.getString("orgCode"));
					if (agtClaimedMap.get(agtOrgId) != null) {
						agtBean.setClaimedAmt(agtClaimedMap.get(agtOrgId));
					} else {
						agtBean.setClaimedAmt(0.0);
					}
					if (agtTotWinMap.get(agtOrgId) != null) {
						agtBean.setTotalWinAmt(agtTotWinMap.get(rs
								.getInt("organization_id")));
					} else {
						agtBean.setTotalWinAmt(0.0);
					}
					if (agtUnClaimed7Map.get(agtOrgId) != null) {
						agtBean.setUnclaimedAmtAftr7Days(agtUnClaimed7Map
								.get(agtOrgId));
					} else {
						agtBean.setUnclaimedAmtAftr7Days(0.0);
					}
					if (agtClaimedAtBoMap.get(agtOrgId) != null) {
						agtBean.setClaimedAtBoAmt(agtClaimedAtBoMap
								.get(agtOrgId));
					} else {
						agtBean.setClaimedAtBoAmt(0.0);
					}

					Map<Integer, Double> retTotWinMap = agtRetTotWinMap
							.get(agtOrgId);
					Map<Integer, Double> retClaimedMap = agtRetClaimedMap
							.get(agtOrgId);
					Map<Integer, Double> retUnclm7Map = agtRetUnclm7Map
							.get(agtOrgId);
					// RetPwtDetailBean retBean = new RetPwtDetailBean();
					if (retTotWinMap != null && retTotWinMap.size() > 0) {
						Iterator<Integer> retItr = retTotWinMap.keySet()
								.iterator();
						while (retItr.hasNext()) {
							int retUsrId = retItr.next();
							RetPwtDetailBean retBean = new RetPwtDetailBean();
							retBean.setRetOrgId(retUsrId);
							retBean.setName(AjaxRequestHelper
									.getOrgNameFromUserId(retUsrId, "RETAILER"));
							if (retTotWinMap.get(retUsrId) != null) {
								retBean.setTotalWinAmt(retTotWinMap
										.get(retUsrId));
							} else {
								retBean.setTotalWinAmt(0.0);
							}
							if (retClaimedMap.get(retUsrId) != null) {
								retBean.setClaimedAmt(retClaimedMap
										.get(retUsrId));
							} else {
								retBean.setClaimedAmt(0.0);
							}
							if (retUnclm7Map.get(retUsrId) != null) {
								retBean.setUnclaimedAmtAftr7Days(retUnclm7Map
										.get(retUsrId));
							} else {
								retBean.setUnclaimedAmtAftr7Days(0.0);
							}
							retList.add(retBean);
						}
					}
					agtBean.setRetDetailList(retList);

					agtList.add(agtBean);
				}// end of agent beans
			}// end of dge success

		} catch (Exception e) {
			e.printStackTrace();
		}
		return agtList;
	}

	public String getOrgAdd(int orgId) throws LMSException {
		String orgAdd = "";
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id = ?");
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				orgAdd = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return orgAdd;
	}

}
