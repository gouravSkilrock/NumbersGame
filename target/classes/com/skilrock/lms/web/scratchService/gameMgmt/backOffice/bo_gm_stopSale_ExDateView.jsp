<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<body>
			  <table border="0"  cellpadding="2" cellspacing="0">
			  <s:iterator value="#session.SALE_ClOSE_SEARCH_RESULTS">
			  <tr><td>
			  <s:div id="date" >
			  </s:div>
			  </td></tr>
			  
			  	<tr>
					<td>
						<label class="label"><s:text name="label.extended.sale.date"/><span class="required">*</span>:</label>
			    		<input  type="text" name="saleEndDate" id="saleEndDate" value="<s:property value="%{saleEndDate}"/>" readonly size="12">
			    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('saleEndDate'),'yyyy-mm-dd', this, '<s:property value="%{#session.presentDate}"/>', '<s:property value="%{#session.presentDate}"/>', '2030-12-31' )" />
			    	</td>
			    </tr>	
				<tr>
					<td>
						<label class="label"> <s:text name="label.extended"/> <s:text name="PWT"/> <s:text name="label.date"/><span class="required">*</span>:</label>
			    		<input  type="text" name="pwtEndDate" id="pwtEndDate" value="<s:property value="%{pwt_end_date}"/>" readonly size="12">
			    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('pwtEndDate'),'yyyy-mm-dd', this, '<s:property value="%{#session.presentDate}"/>', '<s:property value="%{#session.presentDate}"/>', '2030-12-31')" />
			   		</td>
			  </tr>
			  
			  
			 	   </s:iterator>
			   </table>
			  <s:set name="x" value="#session.presentDate"></s:set>
	          <s:hidden name="presentDate" value="%{x}"/>
			
			   
	<s:submit align="left" key="btn.extnd.date" cssClass="button" action="bo_gm_stopSale_ExtendDateSave" onkeypress="return isDateValid()" onclick="return isDateValid()"/>
	
	<br /><br /><br /><br />
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_stopSale_ExDateView.jsp,v $
$Revision: 1.3 $
</code>