package com.skilrock.lms.web.instantPrint.gameMgmt.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.ipe.Bean.GameLMSBean;
import com.skilrock.ipe.instantprint.GameInventoryDetailsBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.instantPrint.common.IPEUtility;
import com.skilrock.lms.coreEngine.instantPrint.gameMgmt.common.GameInventoryUploadHelper;
import com.skilrock.lms.coreEngine.instantPrint.gameMgmt.common.NewGameUploadHelper;

public class GameInventoryUploadAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String gameType;
	private int gameId;
	private String startDate;
	private String saleendDate;
	private String pwtendDate;
	private String packNumberFrom;
	private String packNumberTo;
	private File invFile;

	public File getInvFile() {
		return invFile;
	}

	public void setInvFile(File invFile) {
		this.invFile = invFile;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSaleendDate() {
		return saleendDate;
	}

	public void setSaleendDate(String saleendDate) {
		this.saleendDate = saleendDate;
	}

	public String getPwtendDate() {
		return pwtendDate;
	}

	public void setPwtendDate(String pwtendDate) {
		this.pwtendDate = pwtendDate;
	}

	public String getPackNumberFrom() {
		return packNumberFrom;
	}

	public void setPackNumberFrom(String packNumberFrom) {
		this.packNumberFrom = packNumberFrom;
	}

	public String getPackNumberTo() {
		return packNumberTo;
	}

	public void setPackNumberTo(String packNumberTo) {
		this.packNumberTo = packNumberTo;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void fetchGameDetails() {
		PrintWriter out;

		try {
			out = getResponse().getWriter();

			String game_type = getGameType();
			System.out.println("" + game_type);
			if (game_type == null) {
				game_type = "";
			}

			Map<Integer, GameLMSBean> gameMap = IPEUtility.gameMap;

			String html = "";
			html = "<select class=\"option\" name=\"gameId\" id=\"game_Name\" onchange=\"gameDatesfromDB()\"><option class=\"option\" value=\"Please Select\">Please Select</option>";
			int i = 0;
			GameLMSBean bean = null;
			Iterator<Map.Entry<Integer, GameLMSBean>> gameMapItr = gameMap
					.entrySet().iterator();
			while (gameMapItr.hasNext()) {
				Map.Entry<Integer, GameLMSBean> pair = gameMapItr.next();
				bean = pair.getValue();
				int gameNo = bean.getGameNo();
				String name = bean.getGameName();
				i++;
				if (bean.getGameStatus().equalsIgnoreCase(game_type)) {
					html += "<option class=\"option\" value=\"" + pair.getKey()
							+ "\">" + gameNo + "-" + name + "</option>";
				}
			}
			html += "</select>";
			response.setContentType("text/html");
			out.print(html);
			System.out.println(html);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String saveInventory() throws ParseException {
		NewGameUploadHelper.onStartGame();
		GameInventoryUploadHelper helper = new GameInventoryUploadHelper();
		GameInventoryDetailsBean detailsBean = new GameInventoryDetailsBean();
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String dateFormat = (String) sc.getAttribute("date_format");
		Timestamp stDate = null;
		Timestamp saleEndDate = null;
		Timestamp pwtEndDate = null;
		stDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
				startDate).getTime());
		saleEndDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
				saleendDate).getTime());
		pwtEndDate = new Timestamp((new SimpleDateFormat(dateFormat)).parse(
				pwtendDate).getTime());
		int gameNo = IPEUtility.getGameNo(gameId);
		detailsBean.setGameNo(gameNo);
		detailsBean.setGameName(IPEUtility.getGameName(gameNo));
		detailsBean.setPackFrom(packNumberFrom);
		detailsBean.setPackTo(packNumberTo);
		detailsBean.setStartDate(stDate.getTime());
		detailsBean.setSaleEndDate(saleEndDate.getTime());
		detailsBean.setPwtEndDate(pwtEndDate.getTime());
		detailsBean.setVirnFile(invFile.getPath());

		helper.saveInventory(detailsBean);
		return SUCCESS;
	}

	public void getGameDates() throws LMSException {
		PrintWriter out;
		try {
			out = getResponse().getWriter();
			StringBuilder allDatesStr = new StringBuilder("");
			GameLMSBean gameBean = IPEUtility.getGameMap().get(gameId);
			HttpSession session = request.getSession();
			ServletContext sc = session.getServletContext();
			String dateFormat = (String) sc.getAttribute("date_format");
			Timestamp startDate = gameBean.getStartDate();
			Timestamp saleEndDate = gameBean.getSaleEndDate();
			Timestamp pwtEndDate = gameBean.getPwtEndDate();
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			allDatesStr.append(sdf.format(startDate) + "*"
					+ sdf.format(saleEndDate) + "*" + sdf.format(pwtEndDate));
			out.print(allDatesStr);
			System.out.println("success return");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
