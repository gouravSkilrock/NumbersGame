
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	
<style type="text/css">
      
        .fixedColumn .fixedTable td
        {
            color: #FFFFFF;
            background-color: #187BAF;
            font-size: 12px;
            font-weight: normal;
        }
        
        .fixedHead td, .fixedFoot td
        {
            color: #FFFFFF;
            background-color: #187BAF;
            font-size: 12px;
            font-weight: normal;
       
            border: 1px solid #187BAF;
        }
        .fixedTable td
        {
            font-size: 8.5pt;
            background-color: #FFFFFF;
      		
            text-align: left;
            border: 1px solid #CEE7FF;
        }
    </style>
</head>
	<body>
	<div id="myt" style="display: none;">
	<s:if test="cityList.size>0">
<s:hidden id="cList1" value="%{cityCodeList}"></s:hidden>
								<table id="cbDiv" border="0">
									<tr>
										<td>
											<table  border="1" cellpadding="2" cellspacing="0" id="searchTable">
												<tr>
													<td>
														<b><input type="checkbox" id="selAll" name="cityName" value="-1" onclick="selAllChkBx();"/>Select All City's </b>
													</td>
												</tr>
												<tr>
													<td>
														<table id="tab0">
															<tr>
																<s:iterator value="cityList" status="retIndex">
																	<td>
																		<input type="checkbox" name="cList" value="<s:property />" onchange="buildTable('')" />
																		<s:property />
																	</td>
																	<s:if test="(#retIndex.index+1)%5==0">
															</tr>
															<tr>
																</s:if>
																</s:iterator>
															</tr>
														</table>
													</td>
												</tr>
											</table>	
										</td>
									</tr>
								</table>
								</s:if>
								</div>
		<div id="tableDiv_General" class="tableDiv">
			<table id="Open_Text_General" class="FixedTables">
				<thead>
					<s:if test="cityCodeList.size>0">
						<tr>
							<th style="height: 50px;">
								Date
							</th>
							<s:iterator value="cityCodeList">
								<th>
								<s:property />
								</th>
							</s:iterator>
						</tr>
					</s:if>
				</thead>

				<tbody>
					<s:if test="locationMap.size>0">
						<s:iterator value="locationMap">

							<tr>
								<td>
									<s:property value="%{key}" />
								</td>
								<s:iterator value="%{value}">
									<td>
										<s:property />
									</td>
								</s:iterator>
							</tr>
						</s:iterator>
						<tr>
							<td>
								&nbsp;
							</td>
							<td>
							</td>
						</tr>
					</s:if>
					<s:else>
						<tr>
							<td colspan="17" align="center">
								No Records to Process
							</td>
						</tr>
					</s:else>
				</tbody>
			</table>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retActivity_location_History.jsp,v $ $Revision:
	1.1.2.6 $
</code>