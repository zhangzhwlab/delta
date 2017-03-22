<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="cn.ac.big.circos.util.Page"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		
	</head>

	<body>
		<%
			
			Page myPage = (Page) session.getAttribute("myPage");
			if(myPage != null){

			int pageSize = myPage.getPageSize();
			int pageNo = myPage.getPageNo();
			int rowCount = myPage.getRowCount();
			int firstPageNo = myPage.getFirstPageNo();
			int lastPageNo = myPage.getLastPageNo();
			
			
			int nextPageNo = pageNo + 1;
			int prevPageNo = pageNo - 1;

		%> 
		<table width="90%" class="browse">
			<tr>
				<td width="21%">
					Item&nbsp;
						<s:if test="page.rowCount>0">
							<s:property value="page.rowFrom" />
						</s:if>
						<s:else>0</s:else>
						&nbsp;
					
					&nbsp;-&nbsp;
					
					<s:property value="page.rowTo" />
					&nbsp;
					
					of
					<s:property value="page.rowCount" />
					
			  </td>
				

				<td width="79%">
					<a href="#" onClick='javascript:processGeneMap3DModel("<%=firstPageNo%>","1000","<%=rowCount%>")'>First</a>
					<s:if test="page.isHasPreviousPage==1">
						<a href="#" onClick='javascript:processGeneMap3DModel("<%=prevPageNo%>","1000","<%=rowCount%>")'>Prev</a>
					</s:if>
					<s:else>
						<a disabled="disabled">Prev</a>
					</s:else>
					<input type="text" size="3" id="gono" name="gono" value="<s:property value='page.pageNo'/>">of
					<s:property value="page.lastPageNo" />
					<s:if test="page.isHasNextPage==1">
						<a href="#" onClick='javascript:processGeneMap3DModel("<%=nextPageNo%>","1000","<%=rowCount%>")'>Next</a>
					</s:if>
					<s:else>
						<a disabled="disabled">Next</a>
					</s:else>
					<a href="#" onClick='javascript:processGeneMap3DModel("<%=lastPageNo%>","1000","<%=rowCount%>")'>Last</a>
					
			  </td>
		</table>
		<%
			}
		%>
	</body>
</html>
