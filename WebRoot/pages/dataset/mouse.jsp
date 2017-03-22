<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/menu.js"></script>
<title>3D Genome Visualization</title>
</head>


<body>
<div id="container">
	<jsp:include page="/inc/header.jsp" />
	<div id="content">
    	
    
		<div id="right-column1" style="margin-left:5px;width:250px;">
		  <div class="header_border">
				<div class="block"><img src="/circosweb/images/breadcrumb.gif" />Public Dataset</div>
			<ul>
			
				<li><a href="/circosweb/pages/dataset/dataset.jsp">1. Public Data for Human</a></li>
				<li><a href="/circosweb/pages/dataset/mouse.jsp">2.Public Data for Mouse</a></li>
				
			</ul>	
		
		  </div>
	  </div>
	  
	  <div id="left-column1" style="margin-left:10px;width:980px;margin-top:10px;">
	  	<div class="header_border" style="padding-top:0px;margin-top:0px;height:1100px;">
			<div class="header">Mouse Dataset</div>
			<div class="header_content">
				<p class="tracktitle">1. Data from ENCODE</p>
				<table width="641" cellpadding="0" cellspacing="0" class="table1 table1-border">
					<tbody>
						<tr><th width="95">Cell Type</th>
						<th width="180">Data Type</th>
						<th width="364">Data Track</th>
						</tr>
						<tr >
						  <td rowspan="2">C2C12</td>
						  <td>Signal</td>
						  <td>H3k27me3/H3k4me3/RNA Seq</td>
						</tr>
					     <tr ><td>Peak</td><td>H3k4me3/H3k27me3</td></tr>
						
						
						<tr class="active"><td rowspan="2">CH12</td><td>Signal</td>
						<td>CTCF/H3k27me3/Repli-chip</td>
						</tr>
						<tr class="active">
						  
						  <td>Peak</td><td>CTCF/H3k4me3/H3k27me3</td></tr>
						<tr><td rowspan="2">Cortex</td><td>Signal</td>
						<td>CTCF/H3k27ac/H3k4me3/Pol2/RNA Seq</td>
						</tr>
						<tr>
						  
						  <td>Peak</td><td>CTCF/H3k27ac/H3k4me3/Pol2</td></tr>
						
						
						<tr  class="active">
						  <td rowspan="2">ES-Bruce4</td>
						  <td>Sinal</td>
						<td>CTCF/H3K27ac/H3K27me3/H3K4me3/RNA seq </td>
						</tr>
						<tr  class="active"><td>Peak</td>
						<td>CTCF/H3K27ac/H3K27me3/H3K4me3/P300</td>
						</tr>
						<tr >
						  <td rowspan="2">ES-E14</td>
						  <td>Sinal</td>
						<td>&nbsp;</td>
						</tr>
						<tr ><td>Peak</td>
						<td>H3K27ac/H3K4me3</td>
						</tr>
						<tr  class="active">
						  <td rowspan="2">MEF</td>
						  <td>Sinal</td>
						<td>CTCF/H3K27ac/Pol2/H3K4me3/RNA seq</td>
						</tr>
						<tr  class="active"><td>Peak</td>
						<td>CTCF/H3K27ac/Pol2/H3K4me1</td>
						</tr>
					</tbody>
			  </table>
			
			</div>
			
			<div class="header_content">
				<p class="tracktitle">2. Data from other public resources</p>
				<table width="482" cellpadding="0" cellspacing="0" class="table1 table1-border">
					<tbody>
						<tr><th width="80">Data Type</th>
						<th width="151">Source</th>
						<th width="249">Version</th>
						</tr>
						<tr><td>Gene</td><td>Ensembl</td><td></td></tr>
					</tbody>
			  </table>
			</div>
		</div>
		
      </div>
		
	</div>
	<jsp:include page="/inc/footer.jsp" />
</div>

<script type="text/javascript" language="javascript">
		showTabs('5');
</script>
</body>
</html>
