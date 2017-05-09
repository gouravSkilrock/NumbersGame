<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head></head>

<body>
	<%--  code changed by arun on requirement changed  
	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 	<tr ><th colspan="8" style="font-size:  13px;medium">Uploaded Game Details </th></tr>
		
		<tr>
			<th  align="left">Game Number</th><td align="left">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.gameNbr" /></td>
			<th rowspan = "3">&nbsp;</th>
			<th align="left">Ticket Price</th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.ticketPrice" /></td>
		</tr>
		<tr>
			<td colspan="5"><div id="game_name_error" style="color: red"></div></td>
			
		</tr>
		<tr>
			<th align="left">Game Name</th><td  align="left">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.gameName" /></td>		
			<th align="left">Tickets In Scheme</th><td align="right">&nbsp;<s:set name="noOfTicketInGameJsp" value="#session.gameDetailsMap.gameDetailBean.totalNbrOfTicketInGame"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("noOfTicketInGameJsp")) %></td>
		</tr>		
	</table><br/>
	

	<table  width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">	 
		<tr>			
	 		<th align="left">Total Tickets Qty : </th><td align="right">&nbsp;<s:set name="noOfTicketUploadedJsp" value="#session.gameDetailsMap.gameDetailBean.nbrOfTicketUploaded"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("noOfTicketUploadedJsp")) %></td>
			<th align="left">Tickets/Book : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.nbrTicketsPerBook" /></td>
	 		<th align="left">Start Date : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.startDate" /></td>
	 		
	 	</tr>
	 	<tr>
			<th align="left">Total Sales Value : </th><td align="right">&nbsp;<s:property value="%{#session.gameDetailsMap.gameDetailBean.totalSales}" /></td>	 		
	 		<th align="left">Books/Pack : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.nbrOfBooksPerPack" /></td>
	 		<th align="left">Sale End Date : </th> <td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.saleEndDate" /></td>	 		
	 	</tr>
	 	<tr>	 		
	 		<th align="left">Total Prize Fund : </th><td align="right">&nbsp;<s:set name="totalPrizeAmtJsp"		value="#session.gameDetailsMap.sumRowTotalTotalPrizeAmount" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPrizeAmtJsp")) %></td>
	 		<th align="left">Packs Uploaded : </th><td align="right">&nbsp;<s:property value="%{#session.gameDetailsMap.gameDetailBean.nbrOfPackInGame}" /></td>			
	 		<th align="left"><s:text name="PWT"/> End Date : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.pwtEndDate" /></td>
	 	</tr>
		</table><br/>
	 --%>
	 
	<table  width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">	 
		<tr ><th colspan="6" style="font-size:  13px;medium"><s:text name="label.uploaded.game.detailes"/> </th></tr>
		<tr>			
	 		<th  align="left"><s:text name="label.game.nbr"/></th><td align="left">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.gameNbr" /></td>
			<th align="left"><s:text name="label.ticket.price"/></th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.ticketPrice" /></td>
	 		<th align="left"><s:text name="label.start.date"/> : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.startDate" /></td>
	 		
	 	</tr>
	 	<tr>
			<th align="left"><s:text name="label.game.name"/></th><td  align="left">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.gameName" /></td>	 		
	 		<th align="left"><s:text name="label.tickets.book"/> : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.nbrTicketsPerBook" /></td>
	 		<th align="left"><s:text name="label.sale.end.date"/> : </th> <td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.saleEndDate" /></td>	 		
	 	</tr>
	 	<tr>	 		
	 		<th align="left"><s:text name="label.game.status"/></th><td  align="left">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.gameStatus" /></td>
	 		<th align="left"><s:text name="label.books.pack"/> : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.nbrOfBooksPerPack" /></td>			
	 		<th align="left"><s:text name="PWT"/><s:text name="label.end.date"/>  : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.pwtEndDate" /></td>
	 	</tr>
		</table><br/>
		
		
		<table  width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">	 
		<tr>
			<th>&nbsp;</th>
			<th><s:text name="label.in.scheme"/></th>
			<th><s:text name="label.actual.uploaded"/></th>
			<th><s:text name="label.current.sold.B->A"/></th>
			<th><s:text name="label.current.sold.A->R"/></th>
			<th><s:text name="label.current.sold.R"/></th>
			<th><s:text name="label.cancel.status"/></th>
		</tr>
		<tr>						
	 		<td align="left"><s:text name="label.tickets.quantity"/></td>
	 		<td align="right">&nbsp;<s:set name="noOfTicketInGameJsp" value="#session.gameDetailsMap.gameDetailBean.totalNbrOfTicketInGame"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("noOfTicketInGameJsp")) %></td>
			<td align="right">&nbsp;<s:set name="noOfTicketUploadedJsp" value="#session.gameDetailsMap.gameDetailBean.nbrOfTicketUploaded"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("noOfTicketUploadedJsp")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalNbrOfTicketSoldJsp" value="#session.gameDetailsMap.gameDetailBean.totalNbrOfTicketSold"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("totalNbrOfTicketSoldJsp")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalNbrOfTicketSoldAtoR" value="#session.gameDetailsMap.gameDetailBean.totalNbrOfTicketSoldAtoR"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("totalNbrOfTicketSoldAtoR")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalNbrOfTicketSoldAtR" value="#session.gameDetailsMap.gameDetailBean.totalNbrOfTicketSoldAtR"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("totalNbrOfTicketSoldAtR")) %></td>
	 		
	 		<td align="right">&nbsp;<s:set name="nbrOfTicketCancelJsp" value="#session.gameDetailsMap.gameDetailBean.nbrOfTicketCancel"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("nbrOfTicketCancelJsp")) %></td>	 		
	 	</tr>
	 	<tr>						
	 		<td align="left"><s:text name="label.books.quantity"/></td>
	 		<td align="right">&nbsp;<s:set name="noOfBooksInGameJsp" value="%{#session.gameDetailsMap.gameDetailBean.totalNbrOfTicketInGame/#session.gameDetailsMap.gameDetailBean.nbrTicketsPerBook}"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("noOfBooksInGameJsp")) %></td>
			<td align="right">&nbsp;<s:set name="noOfBooksUploadedJsp" value="%{#session.gameDetailsMap.gameDetailBean.nbrOfTicketUploaded/#session.gameDetailsMap.gameDetailBean.nbrTicketsPerBook}"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("noOfBooksUploadedJsp")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalNbrOfBooksSoldJsp" value="%{#session.gameDetailsMap.gameDetailBean.totalNbrOfTicketSold/#session.gameDetailsMap.gameDetailBean.nbrTicketsPerBook}"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("totalNbrOfBooksSoldJsp")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalNbrOfBooksSoldAtoRJsp" value="%{#session.gameDetailsMap.gameDetailBean.totalNbrOfTicketSoldAtoR/#session.gameDetailsMap.gameDetailBean.nbrTicketsPerBook}"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("totalNbrOfBooksSoldAtoRJsp")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalNbrOfBooksSoldAtRJsp" value="%{#session.gameDetailsMap.gameDetailBean.totalNbrOfTicketSoldAtR/#session.gameDetailsMap.gameDetailBean.nbrTicketsPerBook}"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("totalNbrOfBooksSoldAtRJsp")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalNbrOfBooksCanJsp" value="%{#session.gameDetailsMap.gameDetailBean.nbrOfTicketCancel/#session.gameDetailsMap.gameDetailBean.nbrTicketsPerBook}"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("totalNbrOfBooksCanJsp")) %></td>	 		
	 	</tr> 
	 	<tr>
	 		<td align="left"><s:text name="label.total.sale.value"/></td>
	 		<td align="right">&nbsp;<s:set name="totalSaleOfScheme" value="#session.gameDetailsMap.gameDetailBean.totalSaleOfScheme"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSaleOfScheme")) %></td>
			<td align="right">&nbsp;<s:set name="totalSaleOfUploaded" value="#session.gameDetailsMap.gameDetailBean.totalSales"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSaleOfUploaded")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalSaleOfSold" value="#session.gameDetailsMap.gameDetailBean.totalSaleOfSold"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSaleOfSold")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalSaleOfSoldAtoR" value="#session.gameDetailsMap.gameDetailBean.totalSaleOfSoldAtoR"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSaleOfSoldAtoR")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalSaleOfSoldAtR" value="#session.gameDetailsMap.gameDetailBean.totalSaleOfSoldAtR"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSaleOfSoldAtR")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalSaleOfCanJsp" value="#session.gameDetailsMap.gameDetailBean.totalSaleValueOfCancel"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSaleOfCanJsp")) %></td> 	 					
	 	</tr>
	 	<tr>
	 		<td align="left"><s:text name="label.total.prize.fund"/> </td>
	 		<td align="right">&nbsp;<s:set name="totalPrizeFundOfScheme" value="#session.gameDetailsMap.gameDetailBean.totalPrizeFundOfScheme"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPrizeFundOfScheme")) %></td>
			<td align="right">&nbsp;<s:set name="totalPrizeAmtJsp"		value="#session.gameDetailsMap.gameDetailBean.prizeFund" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPrizeAmtJsp")) %></td>
	 		<td align="right">&nbsp;<s:set name="totalPrizeFundOfSold"		value="#session.gameDetailsMap.gameDetailBean.totalPrizeFundOfSold" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPrizeFundOfSold")) %></td>	 					
	 		<td align="right">&nbsp;<s:set name="totalPrizeFundOfSoldAtoR"		value="#session.gameDetailsMap.gameDetailBean.totalPrizeFundOfSold" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPrizeFundOfSoldAtoR")) %></td>	 					
	 		<td align="right">&nbsp;<s:set name="totalPrizeFundOfSoldAtR"		value="#session.gameDetailsMap.gameDetailBean.totalPrizeFundOfSold" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPrizeFundOfSoldAtR")) %></td>	 					
	 		<td align="right">&nbsp;<s:set name="totalPrizeFundOfCanVirnJsp"		value="#session.gameDetailsMap.gameDetailBean.totalPrizeFundOfCancelVirn" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPrizeFundOfCanVirnJsp")) %></td>
	 	</tr>	 	
	 	<tr>
	 		<td align="left"><s:text name="label.prize.payout.ratio"/></td>
	 		<td align="right">&nbsp;<s:set name="prizePayOutRatioOfScheme" value="#session.gameDetailsMap.gameDetailBean.prizePayOutRatioOfScheme"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizePayOutRatioOfScheme")) %>%</td>
			<td align="right">&nbsp;<s:set name="prizePayOutRatioOfUploaded" value="#session.gameDetailsMap.gameDetailBean.prizePayOutRatioOfUploaded"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizePayOutRatioOfUploaded")) %>%</td>
	 		<td align="right">&nbsp;<s:set name="prizePayOutRatioOfSold" value="#session.gameDetailsMap.gameDetailBean.prizePayOutRatioOfSold"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizePayOutRatioOfSold")) %>%</td> 					
	 		<td align="right">&nbsp;<s:set name="prizePayOutRatioOfSoldAtoR" value="#session.gameDetailsMap.gameDetailBean.prizePayOutRatioOfSoldAtoR"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizePayOutRatioOfSoldAtoR")) %>%</td> 					
	 		<td align="right">&nbsp;<s:set name="prizePayOutRatioOfSoldAtR" value="#session.gameDetailsMap.gameDetailBean.prizePayOutRatioOfSoldAtR"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizePayOutRatioOfSoldAtR")) %>%</td> 					
	 		<td align="right">&nbsp;<s:set name="prizePayOutRatioOfCancelJsp" value="#session.gameDetailsMap.gameDetailBean.prizePayOutRatioOfCancel"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizePayOutRatioOfCancelJsp")) %>%</td>
	 	</tr>	
	 	
	 	</table><br/>
	 	
	 	
	 	<table  width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">	 
		<tr>			
	 		<th align="left"><s:property value="#application.TIER_MAP.AGENT" /> <s:text name="label.sale.profit"/> : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.agentSaleCommRate" /></td>
			<th align="left"><s:property value="#application.TIER_MAP.RETAILER" /> <s:text name="label.sale.profit"/> : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.retSaleCommRate" /></td>
	 		<th align="left"><s:text name="label.goodcause"/> : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.govCommRate" /></td>	 		
	 	</tr>
	 		<tr>			
	 		<th align="left"><s:property value="#application.TIER_MAP.AGENT" /> <s:text name="PWT"></s:text> <s:text name="label.profit"/> : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.agentPwtCommRate" /></td>
			<th align="left"><s:property value="#application.TIER_MAP.RETAILER" /> <s:text name="PWT"></s:text> <s:text name="label.profit"/> : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.retPwtCommRate" /></td>
	 		<th align="left"><s:text name="label.vat"/> : </th><td align="right">&nbsp;<s:property value="#session.gameDetailsMap.gameDetailBean.vat" /></td>	 		
	 	</tr>	 	
	 	</table><br/>
	 	
	 	
		<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">	 	
	 	<tr>
			<th align="center" colspan="5"><s:text name="label.details.game.scheme"/></th>	
			<th align="center" colspan="2"><s:text name="label.current.details.game"/></th>	 
	 	</tr>
	 	<tr>
	 		<th align="center"><s:text name="label.prize.amt"/></th>
	 		<th align="center"><s:text name="label.odds"/></th>
	 		<th align="center"><s:text name="label.nbr.of.winners"/></th>
	 		<th align="center"><s:text name="label.total.prize.amount"/></th>
	 		<th align="center"><s:text name="Prize"/> %</th>
			<th align="center"><s:text name="label.nbr.winners.rem"/></th>
			<th align="center"><s:text name="label.prize.amount.rem"/></th>
			<th align="center"><s:text name="label.nbr.winners.cancel"/></th>
	 	</tr>
	 	<s:set name="totalPrzRem" value="0"></s:set>
	 	<s:iterator  value="#session.gameDetailsMap.GamePrizeDetailsBeanList">
	 		<tr>
	 			<td align="right"><s:set name="prizeAmtJsp" value="prizeAmt"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizeAmtJsp")) %></td>
	 			<td align="right"><s:if test="%{odds!=0.0}">
	 			<s:set name="oddsJsp" value="odds"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("oddsJsp")) %>
	 			</s:if><s:else><s:text name="label.na"/></s:else></td>
	 			<td align="right"><s:set name="noOfWinnersJsp" value="noOfWinners"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("noOfWinnersJsp")) %></td>	 	
	 			<td align="right"><s:set name="totalPrizeAmtJsp" value="totalPrizeAmount"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPrizeAmtJsp")) %></td>
				<td align="right"><s:set name="prizeJsp" value="prize"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizeJsp")) %></td>
				<td  align="right"><s:set name="noOfPrizeRemJsp" value="noOfPrizeRem"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("noOfPrizeRemJsp")) %></td>
				<td  align="right"><s:set name="prizeAmtRemJsp" value="%{#prizeAmtJsp*#noOfPrizeRemJsp}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizeAmtRemJsp")) %></td>
				<td  align="right"><s:set name="noOfPrizecanJsp" value="noOfPrizeCancel"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("noOfPrizecanJsp")) %></td>
	 			<s:set name="totalPrzRem" value="%{#totalPrzRem+#prizeAmtRemJsp}"></s:set>
	 		</tr>
	 	</s:iterator>	 
	 		<tr>
	 			<th align="right"><s:text name="label.total.prizes"/></th>
	 			<th align="right"><s:set name="sumOddsJsp" value="#session.gameDetailsMap.sumRowOdds"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("sumOddsJsp")) %></th>
	 			<th align="right"><s:set name="sumNoOfWinnersJsp" value="#session.gameDetailsMap.sumRowNoOfWinners"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("sumNoOfWinnersJsp")) %></th>	 	
	 			<th align="right"><s:set name="sumTotalPrizeAmtJsp" value="#session.gameDetailsMap.sumRowTotalTotalPrizeAmount"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("sumTotalPrizeAmtJsp")) %></th>
	 			<th align="right"><s:set name="sumPrizeJsp" value="#session.gameDetailsMap.sumRowPrizePer"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("sumPrizeJsp")) %></th>
				<th  align="right"><s:set name="sumNoOfPrizeRemJsp" value="#session.gameDetailsMap.sumRowNoOfPrizeRem"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("sumNoOfPrizeRemJsp")) %></th>	 		
				<th  align="right"><s:set name="sumNoOfPrizeAmtRemJsp" value="#totalPrzRem"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("sumNoOfPrizeAmtRemJsp")) %></th>	 		
				<th  align="right"><s:set name="sumNoOfPrizeCanJsp" value="#session.gameDetailsMap.sumRowNoOfPrizeCan"/><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("sumNoOfPrizeCanJsp")) %></th>
	 		</tr>
	 	
	 </table>
	
	 	
</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_gameDetail_View.jsp,v $
$Revision: 1.3 $
</code>