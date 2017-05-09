package com.skilrock.lms.web.ola.reportsMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.skilrock.lms.beans.OlaPlayerDetailsBean;
import com.skilrock.lms.common.db.DBConnect;

public class OlaBoSearchPlayerHelper {

	public ArrayList<OlaPlayerDetailsBean> searchPlr(int retOrgId,
			int walletId, String regToDate, String regFromDate, String alias,
			String regType) {
		Connection con = DBConnect.getConnection();
		ArrayList<OlaPlayerDetailsBean> plrDetailsBeanList = new ArrayList<OlaPlayerDetailsBean>();

		try {
			con.setAutoCommit(false);
			String query = "";
			PreparedStatement ps = null;
			ResultSet rs = null;

			if (alias.equalsIgnoreCase("") || alias == null) {

				if (walletId == -1) {

					if (!regType.equalsIgnoreCase("-1")) {

						query = "select * from (select player_id,username,name,email,DATE(registration_date) registration_date,wallet_name,case when username is null then 'DIRECT' else 'OLA' end as regType from ("
								+ " (select username,concat(fname,' ',lname) as name,email,registration_date  from st_ola_player_master  ) pm"
								+ " right join st_ola_affiliate_plr_mapping apm on username=player_id left join st_ola_wallet_master wm on apm.wallet_id=wm.wallet_id )where (( registration_date<=? and registration_date>=?) or registration_date is null)  and ref_user_org_id=? )a	where regType=? ";
						ps = con.prepareStatement(query);
						ps.setString(1, regToDate);
						ps.setString(2, regFromDate);
						ps.setInt(3, retOrgId);
						ps.setString(4, regType);

					} else {

						query = "select player_id,username,name,email,DATE(registration_date) registration_date,wallet_name,case when username is null then 'DIRECT' else 'OLA' end as regType from ("
								+ " (select username,concat(fname,' ',lname) as name,email,registration_date  from st_ola_player_master ) pm"
								+ " right join st_ola_affiliate_plr_mapping apm on username=player_id left join st_ola_wallet_master wm on apm.wallet_id=wm.wallet_id )where  (( registration_date<=? and registration_date>=?) or registration_date is null)  and ref_user_org_id=? ";
						ps = con.prepareStatement(query);
						ps.setString(1, regToDate);
						ps.setString(2, regFromDate);
						ps.setInt(3, retOrgId);

					}

				} else {

					if (!regType.equalsIgnoreCase("-1")) {

						query = "select * from (select player_id,username,name,email,DATE(registration_date) registration_date,wallet_name,case when username is null then 'DIRECT' else 'OLA' end as regType from ("
								+ " (select username,concat(fname,' ',lname) as name,email,registration_date,wallet_id  from st_ola_player_master  ) pm"
								+ " right join st_ola_affiliate_plr_mapping apm on username=player_id left join st_ola_wallet_master wm on apm.wallet_id=wm.wallet_id )where  (( registration_date<=? and registration_date>=?) or registration_date is null)  and  apm.wallet_id=?   and  ref_user_org_id=? )a	where  regType=? ";
						ps = con.prepareStatement(query);
						ps.setString(1, regToDate);
						ps.setString(2, regFromDate);
						ps.setInt(3, walletId);
						ps.setInt(4, retOrgId);
						ps.setString(5, regType);

					} else {

						query = "select player_id,username,name,email,DATE(registration_date) registration_date,wallet_name,case when username is null then 'DIRECT' else 'OLA' end as regType from ("
								+ " (select username,concat(fname,' ',lname) as name,email,registration_date,wallet_id  from st_ola_player_master ) pm"
								+ " right join st_ola_affiliate_plr_mapping apm on username=player_id left join st_ola_wallet_master wm on apm.wallet_id=wm.wallet_id )where  (( registration_date<=? and registration_date>=?) or registration_date is null)  and   apm.wallet_id=?  and ref_user_org_id=? ";
						ps = con.prepareStatement(query);
						ps.setString(1, regToDate);
						ps.setString(2, regFromDate);
						ps.setInt(3, walletId);
						ps.setInt(4, retOrgId);

					}

				}

			} else {
				if (walletId == -1) {

					if (!regType.equalsIgnoreCase("-1")) {

						query = "select * from (select player_id,username,name,email,DATE(registration_date) registration_date,wallet_name,case when username is null then 'DIRECT' else 'OLA' end as regType from ("
								+ " (select username,concat(fname,' ',lname) as name,email,registration_date  from st_ola_player_master ) pm"
								+ " right join st_ola_affiliate_plr_mapping apm on username=player_id left join st_ola_wallet_master wm on apm.wallet_id=wm.wallet_id )where  (( registration_date<=? and registration_date>=?) or registration_date is null)  and  ref_user_org_id=? and player_id like ?)a	where regType=? ";
						ps = con.prepareStatement(query);
						ps.setString(1, regToDate);
						ps.setString(2, regFromDate);
						ps.setInt(3, retOrgId);
						ps.setString(4, alias + "%");
						ps.setString(5, regType);

					} else {

						query = "select player_id,username,name,email,DATE(registration_date) registration_date,wallet_name,case when username is null then 'DIRECT' else 'OLA' end as regType from ("
								+ " (select username,concat(fname,' ',lname) as name,email,registration_date  from st_ola_player_master ) pm"
								+ " right join st_ola_affiliate_plr_mapping apm on username=player_id left join st_ola_wallet_master wm on apm.wallet_id=wm.wallet_id )where   (( registration_date<=? and registration_date>=?) or registration_date is null)  and ref_user_org_id=? and player_id like ? ";
						ps = con.prepareStatement(query);
						ps.setString(1, regToDate);
						ps.setString(2, regFromDate);
						ps.setInt(3, retOrgId);
						ps.setString(4, alias + "%");

					}

				} else {

					if (!regType.equalsIgnoreCase("-1")) {

						query = "select * from (select player_id,username,name,email,DATE(registration_date) registration_date,wallet_name,case when username is null then 'DIRECT' else 'OLA' end as regType from ("
								+ " (select username,concat(fname,' ',lname) as name,email,registration_date,wallet_id  from st_ola_player_master ) pm"
								+ " right join st_ola_affiliate_plr_mapping apm on username=player_id left join st_ola_wallet_master wm on apm.wallet_id=wm.wallet_id )where   (( registration_date<=? and registration_date>=?) or registration_date is null)  and apm.wallet_id=?  and  ref_user_org_id=? and player_id like ?)a	where regType=? ";
						ps = con.prepareStatement(query);
						ps.setString(1, regToDate);
						ps.setString(2, regFromDate);
						ps.setInt(3, walletId);
						ps.setInt(4, retOrgId);
						ps.setString(5, alias + "%");
						ps.setString(6, regType);

					} else {

						query = "select player_id,username,name,email,DATE(registration_date) registration_date,wallet_name,case when username is null then 'DIRECT' else 'OLA' end as regType from ("
								+ " (select username,concat(fname,' ',lname) as name,email,registration_date,wallet_id  from st_ola_player_master  ) pm"
								+ " right join st_ola_affiliate_plr_mapping apm on username=player_id left join st_ola_wallet_master wm on apm.wallet_id=wm.wallet_id )where   (( registration_date<=? and registration_date>=?) or registration_date is null)  and apm.wallet_id=?   and ref_user_org_id=? and player_id like ?";
						ps = con.prepareStatement(query);
						ps.setString(1, regToDate);
						ps.setString(2, regFromDate);
						ps.setInt(3, walletId);
						ps.setInt(4, retOrgId);
						ps.setString(5, alias + "%");

					}

				}

			}
			System.out.println("SearchPlayer:" + ps);
			rs = ps.executeQuery();
			while (rs.next()) {
				OlaPlayerDetailsBean plrDetailBean = new OlaPlayerDetailsBean();
				if (rs.getString("username") == null) {
					plrDetailBean.setUsername(rs.getString("player_id"));
					plrDetailBean.setFirstName("NA");
					plrDetailBean.setEmail("NA");
					plrDetailBean.setPlrRegDate("NA");
					plrDetailBean.setWalletname(rs.getString("wallet_name"));
					plrDetailBean.setRegType(rs.getString("regType"));
					plrDetailsBeanList.add(plrDetailBean);
				} else {
					plrDetailBean.setUsername(rs.getString("player_id"));
					plrDetailBean.setFirstName(rs.getString("name"));
					plrDetailBean.setEmail(rs.getString("email"));
					plrDetailBean.setPlrRegDate(rs
							.getString("registration_date"));
					plrDetailBean.setWalletname(rs.getString("wallet_name"));
					plrDetailBean.setRegType(rs.getString("regType"));
					plrDetailsBeanList.add(plrDetailBean);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return plrDetailsBeanList;
	}
}
