<%@ taglib prefix="s" uri="/struts-tags"  %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page isELIgnored="false"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/resmsis/css/styles.css" rel="stylesheet" type="text/css" />
</head>
<body>
   <!--start header-->
    <div id="logo"><img src="/resmsis/images/logo.gif" alt="logo" /></div>
    <div id="nav" >
       <table width="960" style="color:#ffffff; margin-top: 0px;">
		<tbody>
		  <tr>
                      <td width="80"  style="text-align:center;font-size:14px;padding-top:5px;padding-bottom: 5px;"><a href="/resmsis/index.jsp" id="menu0" style="color:#ffffff;text-decoration: none;">首页</a> </td>
			<td width="80"  style="text-align:center;font-size:14px;"><a id="menu1" href="/resmsis/rank/RankAction.action" style="color:#ffffff;text-decoration: none;"> 列表统计</a></td>
			<td width="120"  style="text-align:center;font-size:14px;">数据库统计
			  <s:set name="siten" value="sitename" />
			  <select style="width:200px;" id="db" onChange="chooseDB()">
				<option value="">所有站点</option>
				<option <s:if test="#siten == 'waprna'">selected</s:if> value="waprna">转录组流程服务waprna</option>
				<option value="rice" <s:if test="#siten == 'rice'">selected</s:if>>水稻基因组数据库rice</option>
                                <option value="silkworm" <s:if test="#siten == 'silkworm'">selected</s:if>>家蚕基因组数据库silkworm</option>
                                <option value="influenza" <s:if test="#siten == 'influenza'">selected</s:if>>流感病毒基因组数据库influenza</option>
                                <option value="hrgd" <s:if test="#siten == 'hrgd'">selected</s:if>>杂种优势数据库hrgd</option>
                                <option value="cbb" <s:if test="#siten == 'cbb'">selected</s:if>>章张组网站cbb</option>
                                <option value="platform" <s:if test="#siten == 'platform'">selected</s:if>>平台网站platform</option>
                                <option value="gene" <s:if test="#siten == 'gene'">selected</s:if>>徐磊基因科普gene</option>
                                <option value="casmap" <s:if test="#siten == 'casmap'">selected</s:if>>凌少平组casmap</option>
                                <option value="lims2" <s:if test="#siten == 'lims2'">selected</s:if>>实验室信息管理系统lims2</option>
                                <option value="portal" <s:if test="#siten == 'portal'">selected</s:if>>政务网portal</option>
                                <option value="forum" <s:if test="#siten == 'forum'">selected</s:if>>平台论坛forum</option>
                                <option value="lei" <s:if test="#siten == 'lei'">selected</s:if>>雷老师组网站lei</option>
                                <option value="csdb" <s:if test="#siten == 'csdb'">selected</s:if>>科学数据库csdb</option>
                                <option value="nsdc" <s:if test="#siten == 'nsdc'">selected</s:if>>基础科学共享网nsdc</option>
                                <option value="omics" <s:if test="#siten == 'omics'">selected</s:if>>组学数据库英文版omics</option>
                                <option value="evolgenius" <s:if test="#siten == 'evolgenius'">selected</s:if>>胡老师组Eviewer服务</option>
                                <option value="software" <s:if test="#siten == 'software'">selected</s:if>>软件服务网站software</option>
                                <option value="prevmed" <s:if test="#siten == 'prevmed'">selected</s:if>>Alexander服务</option>
                                <option value="chicken" <s:if test="#siten == 'chicken'">selected</s:if>>家鸡基因组数据库chicken</option>

			</select></td>
                        <%
                               Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
                               int month=c.get(Calendar.MONTH)+1;
                               String st=c.get(Calendar.YEAR)+"-"+month+"-"+c.get(Calendar.DAY_OF_MONTH);
                         %>
                        <td width="100"  style="text-align:center;font-size:14px;"><a id="menu4" href="/resmsis/webstatus/AllwebstatusAction.action?date=<%=st%>" style="color:#ffffff;text-decoration: none;"> 所有网站状态</a></td>
                        <td width="100"  style="text-align:center;font-size:14px;"><a href="/resmsis/alldb/dbstaAction.action?sitename=<s:property value='sitename' />" style="color:#ffffff;text-decoration: none;">网站数据资源量</a></td>
                        <td width="100" style="text-align:center;font-size:14px;"><a href="/resmsis/pages/background/login.jsp" style="color:#ffffff;text-decoration: none;">管理后台</a></td>
		  </tr>
		</tbody>
	  </table>
   </div>
   
<!--end intro-->
<div id="intro">
<div class="group_bannner_left" >
  <div id="hgroup">
    <h1 style="font-size:17px;">web服务访问统计系统</h1>
    <h2 style="font-size: 14px;">web服务访问统计系统用于解析统计web网站的访问日志文件，用户在界面上可以浏览检索不同时间段的网站访问量信息 </h2>
  </div>
  <div style="margin-top:10px;">
  <div class="button black" ><a href="#">更多</a></div>
  </div>
</div>
</div>
   <!--end menu-->

   <!--end header-->
 
<div class="clear"></div>
<script language="javascript" type="text/javascript">

		function chooseDB(){			
			var obj = document.getElementById("db").value;
			window.location="/resmsis/browse/DatabaseBrowseAction.action?sitename="+obj;
		}

               
	</script>
</body>
</html>
