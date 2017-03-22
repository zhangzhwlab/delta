
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/menu.js"></script>
<link rel="stylesheet" href="/circosweb/css/jquery-ui.css" />
<link href="/circosweb/css/layout-default-latest.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/jquery-1.9.1.js" ></script>
<script type="text/javascript" src="/circosweb/js/jquery-ui.js" ></script> 
<script type="text/javascript" src="/circosweb/js/jquery.layout-latest.js"></script>
<title>Topological view</title>
<script type="text/javascript">
	var bandlst;
	
</script>

<style type="text/css">

.pane {
display:	none; /* will appear when layout inits */
}
</style>

</head>

<body onload="loadLayout()">
<jsp:include page="/inc/header.jsp" />
<table><tbody><tr><td><input id="idshowpbtn"  style="display:none;" type="button" value="Refresh physical view" onclick="RefreshPhysical()" /></td></tr></tbody></table>
<div id="container">



<div id="idpagecontent" class="pane ui-layout-center">
				<div class="header_content" style="position:relative;margin-top:0px;padding-top:0px;">
					 <div>Go to&nbsp;<select style="width:50px;" id="chromid" onchange="ChooseChrom(2)"></select>
					   <input type="text" name="position" id="curpos" value="1:1..249250621" style="width:150px;"/><span id="formatscopeid">1 Mb</span>&nbsp;&nbsp;<select style="width:200px; display:none;" id="dset" name="dataset" onchange="loadInitTrack()"></select>&nbsp;<input type="button" value="go" onclick="singleChrom()"/>&nbsp;&nbsp;<img src="/circosweb/images/chroms.jpg"  onclick="loadWholeGenomePage()" alt="chromosomes" />&nbsp;<!--<img src="/circosweb/images/wheels.png" onclick="loadConfigPage()"> &nbsp;&nbsp;&nbsp;&nbsp;--><input type="button" value="Upload" onclick="uploadTrackDialog()" />&nbsp;<input id="idOutlinkGenome" type="button" value="Goto Genome" onclick="gotoGenome()"><input style="display:none;" id="idEmbedGenome" type="button" value="Goto Genome" onclick="gotoEmbedGenome()"><input id="idGotoOutPhysical" type="button" value="Goto Physical" onclick="gotoPhysicalView()"/><input type="button" value="Export" onclick="savePageAs()" /><img src="/circosweb/images/wait.gif" id="waitexport" style="display:none;" /></div>
                     
				  <div style="padding-top:10px;">Zoom in <input type="button" value="1x" onclick="zoomin_func(2)"/>&nbsp;<input type="button" value="3x" onclick="zoomin_func(3)" />&nbsp;<input type="button" value="10x" onclick="zoomin_func(10)" />&nbsp;
				   <select id="scopeid" style="width:200px;" onchange="changeScope()">
				  		<option value="1000000" selected="selected">Show 1 Mbp</option>
						<option value="500000">Show 500 kbp</option>
						<option value="20000">Show 200 kbp</option>
						<option value="100000">Show 100 kbp</option>
						<option value="50000">Show 50 kbp</option>
						<option value="20000">Show 20 kbp</option>
						<option value="10000">Show 10 kbp</option>
						<option value="5000">Show 5 kbp</option>
						<option value="2000">Show 2 kbp</option>
						<option value="1000">Show 1 kbp</option>
						<option value="200">Show 200 bp</option>
						<option value="100">Show 100 bp</option>
				  </select>&nbsp;Zoom out&nbsp;<input type="button" value="1x" onclick="zoomout_func(2)" />&nbsp;<input type="button" value="3x" onclick="zoomout_func(3)" />&nbsp;<input type="button" value="10x" onclick="zoomout_func(10)"/><input type="hidden" id="windownamount"/>&nbsp;&nbsp;  <input type="hidden" id="radiusamount" /></div>
					 <!--
					 <div id="err" style="margin-top:10px;margin-bottom:10px; color:navy; font-size:medium; font-family:sans-serif; display:none;">
					 	Detailed view is limited to 5 Mbp.The histogram density view will be showed.<br/> 
						If you want tow show the detail view ,click and drag on one of the scalebars to make a smaller selection.
					 </div> -->
                     <div id="genome_region" style="margin-top:20px;">
					 	
                     	<div style="position: relative; z-index: 104;"><canvas id="genome" width="900" height="40"></canvas></div>
						<div id="choose_indicator" style="position: absolute; border: 1px solid rgb(128, 166, 255); z-index: 105; display: none;">
							<div style="background-color: blue; opacity: 0.1; height: 100%; width: 100%;"></div>
						</div>
                     </div>
					 <!-- this used to show static picture for all data track of single chromosome or just interaction data of whole genome-->
					 <div id="showpic">
					 
					 </div>
					 
					 <div  id="trackContainer" style="position: absolute;display: block; padding-right: 10px; height:800px; background-color:#ffffff;">
						 <div id="idcirclet" style="margin-top:10px;position: relative; z-index: 101; width:1000px;" align="left">
						
							<canvas id="canvas" width="800" height="800" style="margin-left:150px;"></canvas>
						 </div>
						 
					    <div id="divglasspane" style="position: relative;">
                    	     <canvas id="glasspane" style="position: absolute; padding-left:150px;display: none;  z-index: 201;"></canvas> 
					     </div>
						 
					 </div> 
					 
					 <div id="windowsize-vertical" style="height:200px;position:absolute;z-index:999; margin-left:5px; "></div>
					 <div id="radiussize-vertical" style="height:100px; position:absolute;z-index:900; margin-left: 50px; background:red; "></div>
					 
					 
					
					
					                     
			  </div>
		<div id="chrompageid" style="display:none;"></div>
		 <div id="configpageid" style="display:none;"></div>
		  <div id="utrackid" style="display:none;"></div>
        <div style="clear: both;"></div>
		
		<div class="header_border"  id="genomeid" style="display:none;margin-left:5px; width:95%;">
			<div class="header">Genome View</div>
			<div class="header_content" > <!--genoview-->
				<iframe id="genomeframe" src="#" width="95%" height="600px"></iframe>
			</div>
		</div>	
</div>
	<div class="pane ui-layout-west" style="width:200px;" id="westid">
		<div class="header_content" id="trackid" style="margin:0 0 ; padding: 0 0 ; line-height:1;">
						
         </div> 
	
	</div>	
		
	
</div>
	
	<jsp:include page="/inc/footer.jsp" />
<script type="text/javascript" src="/circosweb/js/cookie.js"></script>
	<script type="text/javascript" src="/circosweb/js/jspdf.min.js"></script>
<script type="text/javascript" src="/circosweb/js/circlet/page.js"></script>
<script type="text/javascript" src="/circosweb/js/circlet/mouse.js"></script>
<script type="text/javascript" src="/circosweb/js/circlet/layout/chrom.js"></script>
<script type="text/javascript" src="/circosweb/js/circlet/layout/wholeGenome.js"></script>
<script type="text/javascript" src="/circosweb/js/circlet/circos.js"></script>
<script type="text/javascript" src="/circosweb/js/circlet/feature/line.js"></script>
<script type="text/javascript" src="/circosweb/js/circlet/feature/gene.js"></script>
<script type="text/javascript" src="/circosweb/js/circlet/feature/arc.js"></script>
<script type="text/javascript" src="/circosweb/js/circlet/feature/histone.js"></script>
<script type="text/javascript" src="/circosweb/js/circlet/ajaxdata.js"></script>

<script type="text/javascript" language="javascript" src="/circosweb/js/circlet/init.js"></script>
<link href="/circosweb/css/circlet/circos.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" language="javascript">
		showTabs('2');
</script>
</body>
</html>

