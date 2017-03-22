<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/menu.js"></script>
<title>Delta</title>
</head>


<body>
<div id="container" style="height:600px;">
	<jsp:include page="/inc/header.jsp" />
	
	
	<div id="content" >
    	
    
		<div id="left-column1" style="margin-left:5px;width:800px; ">
		  <div class="header_border">
				<div class="content_header">
				Welcome to Delta Platform.</div>
			    <div class="header_content" >
					Delta is an integrated platform for Hi-C data analysis and visualization. Three visualization views are provided: Genome View, Topological View and Physical View. Delta supports seamlessly data access and data exchange among these three views.
					  <div align="center" style="padding-top:10px;">
					<table id="__01" width="400" height="319" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="2">
			<a href="/pages/pipeline/useown.jsp"><img src="/circosweb/images/delta_home/delta_home_01.gif" width="200" height="97" ></a></td>
		<td colspan="2">
			<a href="/pages/pipeline/pipeline.jsp"><img src="/circosweb/images/delta_home/delta_home_02.gif" width="200" height="97" ></a></td>
	</tr>
	<tr>
		<td>
			<a href="/jbrowse/index.html?data=1478854512827&loc=11%3A4500000..6500000&tracks=TAD"><img src="/circosweb/images/delta_home/delta_home_03.gif" width="141" height="82"></a></td>
		<td colspan="2">
			<a href="/pages/visualization/topo_viewm.jsp?conf=1478854512827&loc=11%3A4500000..6500000&tracks=Interaction"><img src="/circosweb/images/delta_home/delta_home_04.gif" width="120" height="82" ></a></td>
		<td>
			<a href="/pages/visualization/physical_view.jsp?conf=1478854512827&loc=11%3A4500000..6500000&tracks=3dmodel"><img src="/circosweb/images/delta_home/delta_home_05.gif" width="139" height="82" ></a></td>
	</tr>
	<tr>
		<td colspan="4">
			<img src="/circosweb/images/delta_home/delta_home_06.gif" width="400" height="139" ></td>
	</tr>
	<tr>
		<td>
			<img src="/circosweb/images/delta_home/split.gif" width="141" height="1"></td>
		<td>
			<img src="/circosweb/images/delta_home/split.gif" width="59" height="1"></td>
		<td>
			<img src="/circosweb/images/delta_home/split.gif" width="61" height="1" ></td>
		<td>
			<img src="/circosweb/images/delta_home/split.gif" width="139" height="1" ></td>
	</tr>
</table>
					
					</div>
					<div class="header">Genome View</div>
					<div class="header_content">Genome View use traditional genome browse view to show Hi-C data features as well as other omics data. Various data sources have been preloaded in to Genome View such as Encode, Ensembl, Hi-C data set.</div>
					<div class="header">
						Topological View
					</div>
					<div class="header_content">
						Topological View uses Circos-like view to show Hi-C data features as well as other omics data, such as Encode, Ensembl Gene. 
					</div>
					<div class="header">
						Physical View
					</div>
					<div class="header_content">
						Physical View uses three-dimensional view to show Hi-C data features as well as other omics data, such as Encode, Ensembl Gene. In Physical View, users can do the following things:<br/>
						1) load a 3d physical model for chromatin <br/>
						2) add annotated data into 3d model <br/>
						3) load genome view data in parallel to physical view<br/>
					</div>
				</div>
		  </div>
	  </div>
	  
	  <div id="right-column1" style="margin-left:10px;width:250px;">
	  	<div class="header_border" style="padding-top:0px;margin-top:0px;">
			<div class="header">What's new?</div>
			<div class="header_content">
				<div>1.Physical View is now available to access(2017-03-07).</div>
				<div>2.Topological View is now available to access(2016-12-01).</div>
				<div>3.Encode data has been integrated into Genome View(2016-05-10).</div>
				<div>4.Genome View is now available to access(2016-05-01).</div>
				
			</div>
		</div>
		<div class="header_border">
			<div class="header">Useful link</div>
			<div class="header_content">
				<div><a href="http://3cdb.big.ac.cn" target="_blank">3CDB</a></div>
				<div><a href="http://3dgd.biosino.org/protein/page/index.jsp" target="_blank">3DGD</a></div>
				<div><a href="http://www.3dgenome.org" target="_blank">3D Genome Browse</a></div>
				<div><a href="http://epigenomegateway.wustl.edu" target="_blank">WashU Epigenome Browser</a></div>
				<div><a href="http://hyperbrowser.uio.no/3d" target="_blank">Hi-Browse</a></div>
				<div><a href="http://aidenlab.org/juicebox" target="_blank">JuiceBox</a></div>
				<div><a href="http://sgt.cnag.cat/3dg/" target="_blank">TADKit</a></div>
			</div>
		</div>
      </div>
		
	</div>
	<jsp:include page="/inc/footer.jsp" />
</div>

<script type="text/javascript" language="javascript">
		showTabs('0');
</script>
</body>
</html>
