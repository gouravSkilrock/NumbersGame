package com.skilrock.lms.common.utility;

import java.util.List;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.dge.beans.ManualWinningBean;
import com.skilrock.lms.dge.beans.ReportDrawBean;


public class MailSenderAfterPerformDraw extends Thread {
	ManualWinningBean winbean;

	public MailSenderAfterPerformDraw(ManualWinningBean mwBean) {
		winbean=new ManualWinningBean();
		winbean.setDrawIds(mwBean.getDrawIds());
		winbean.setGameNumber(mwBean.getGameNumber());
		winbean.setGameId(mwBean.getGameId());
	}

	public void run() {
		try {
			mailPerformedDrawDetails(this.winbean);
		} catch (LMSException e) {
			e.printStackTrace();
		}
	}

	// Method For Mailing Users Draw Wise
	public void mailPerformedDrawDetails(ManualWinningBean mwBean)
			throws LMSException {
		DrawGameMgmtHelper helper=new DrawGameMgmtHelper();
		List<ReportDrawBean> mailPerformedDrawList =helper.fetchDrawDateForMail(winbean);
		SendReportMailerMain sendReportMailerMain=new SendReportMailerMain();
		String response=sendReportMailerMain.sendMailAfterDrawPerform(mailPerformedDrawList);
		System.out.println(response);
	}

}
