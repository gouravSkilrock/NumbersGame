<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" import="java.util.*"%>

<%@ page import="com.skilrock.lms.beans.*" %>
<%String contextPath=(String)session.getAttribute("CONTEXT_PATH"); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
				List<DrawScheduleBeanResult> gameList = (List<DrawScheduleBeanResult>) session.getAttribute("DRAW_SCH_LIST");
				System.out.println("-----------------"+gameList.size());
				%>
	<head>

	</head>
	
	<link href="<%=contextPath%>/images/CSS/itgs.css" rel="stylesheet" type="text/css" />

	
	<body>
		<s:if test="#session.DRAW_SCH_LIST.size<1">
			<H3>No Result found..</H3>
		</s:if>
		<s:else>
			<H2>Draw Schedule Results...</H2>
			<%
				
						DrawScheduleBeanResult game;
						int j = gameList.size() / 10;
						int a = 0, x = 0;
						String style = null;
						int reminder = gameList.size() % 10;
			%>
			<%
				for (int i = 0; i < j; i++) {
						if (i != 0) {
							style = "none";
						} else {
							style = "block";
						}
						System.out.println("i---"+i+"--style---"+style+"   gameList--"+gameList.size());
			%>

			<table width="98%" class="table"  border="1"  bgcolor="#ffffff" id="searchTable<%=i%>"
				style="display: <%=style%>">
				<tr>
					<td  align="left" class="normalText">
						<b>Draw id</b>
					</td>
					<td  align="left" class="normalText">
						<b>Draw DateTime</b>
					</td>
					<td align="left" class="normalText">
						<b>Draw Day</b>
					</td>
					<td align="left" class="normalText">
						<b>Draw Status</b>
					</td>
					<td  align="left" class="normalText">
						<b>Draw <s:text name="Freeze" /> Time</b>
					</td>
				</tr>

				<%
					for (x = a; x < a + 10; x++) {
								game = gameList.get(x);
				%>



				<tr>
					<td  align="left" class="normalText">

						<%=game.getDrawId()%>
						
					</td>

					<td align="left" class="normalText">
						<%=game.getDrawDateTime()%>
					</td>

					<td  align="left" class="normalText">
						<%=game.getDrawDay()%>
					<td align="left" class="normalText">
						<%=game.getDrawStatus()%>
						
					</td>

					<td  align="left" class="normalText">
						<%=game.getFreezeDateTime()%>

					</td>
				</tr>



				<%
					}
				%><tr>
					<td>
						<%
							if (i != 0) {
						%>
						<input type="button" class="button" value="< Previous"  
							onclick="showTableSearch('searchTable<%=i - 1%>','searchTable<%=i%>')">
						&nbsp;&nbsp;&nbsp;
						<%
							}
						%>
						<%
							if (!((reminder==0) &&(i==j-1))) {
						%><input type="button" value="Next >" class="button"
							onclick="showTableSearch('searchTable<%=i + 1%>','searchTable<%=i%>')">
						<%
							} 
						%>
						
					</td>
				</tr>
			</table>

			<%
				a = a + 10;
					}
			%>
			<%
				if (gameList.size() <10) {
						style = "display";
					} else {
						style = "none";
					}
			%>
			<table width="100%" class="table" border="1"  bgcolor="#ffffff" id="searchTable<%=j%>"
				style="display: <%=style%>">
				<tr>
					<td  align="left" class="normalText">
						<b>Draw id</b>
					</td>
					<td align="left" class="normalText">
						<b>Draw DateTime</b>
					</td>
					<td align="left" class="normalText">
						<b>Draw Day</b>
					</td>
					<td align="left" class="normalText">
						<b>Draw Status</b>
					</td>
					<td  align="left" class="normalText">
						<b>Draw <s:text name="Freeze" /> Time</b>
					</td>
				</tr>
				<%
					for (int xx = x; xx < x + reminder; xx++) {
							game = gameList.get(xx);
				%>
				<tr>
					<td  align="left" class="normalText">

						<%=game.getDrawId()%>
						
					</td>

					<td  align="left" class="normalText">
						<%=game.getDrawDateTime()%>
					</td>

					<td  align="left" class="normalText">
						<%=game.getDrawDay()%>
					<td  align="left" class="normalText">
						<%=game.getDrawStatus()%>
						
					</td>

					<td " align="left" class="normalText">
						<%=game.getFreezeDateTime()%>

					</td>
				</tr>
				<%
					}
				%>
				 <%
					if (gameList.size() > 10) {
				%>
				<tr>
					<td>
						<input type="button" class="button" value="< Previous" 
							onclick="showTableSearch('searchTable<%=j - 1%>','searchTable<%=j%>')">
					</td>
				</tr>
				<%
					}
				%>
			</table>

		</s:else>
	</body>
</html>
