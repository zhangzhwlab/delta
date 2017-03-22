<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />


<script type="text/javascript" src="/circosweb/js/menu.js"></script>

<script type="text/javascript" src="/circosweb/js/jquery-1.9.1.js" ></script>
<script type="text/javascript" src="/circosweb/js/physical/3Dmol.js"></script>
<script type="text/javascript" src="/circosweb/js/jquery-ui.js"></script>
<script type="text/javascript" src="/circosweb/js/jquery.layout-latest.js"></script>

<link rel="stylesheet" href="/circosweb/css/jquery-ui.css" />
<link href="/circosweb/css/layout-default-latest.css" rel="stylesheet" type="text/css" />
<title>Physical view</title>

<style type="text/css">
 .pane {
	display:	none; /* will appear when layout inits */
}
							
.transparent_class {
  /* IE 8 */
  -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=20)";

  /* IE 5-7 */
  filter: alpha(opacity=20);

  /* Netscape */
  -moz-opacity: 0.2;

  /* Safari 1.x */
  -khtml-opacity: 0.2;

  /* Good browsers */
  opacity: 0.2!important;
}


*{
z-index:inherit;
}

</style>
<script type="text/javascript" language="javascript">

var mylayout = null;

$(function(){
	//$("#dmenu").load("/circosweb/pages/visualization/physical/detail.jsp");
	$("#dupload").load("/circosweb/pages/visualization/physical/uploadtrack.jsp");
	
});

</script>
</head>

<body>
<jsp:include page="/inc/header.jsp" />

<div id="container" style="margin: 0 0 ;padding: 0 0 ; height:1500px;">
		

	<div id="centerid" class="pane ui-layout-center" >

			
				<div class="header_content" style="position:relative; margin-left:0px;padding-left:0px;width:100%;">
				
                    
                  		<div>Go to&nbsp;&nbsp;&nbsp;&nbsp;<select style="width:50px;" id="chromid" onchange="ChooseChrom(2)"></select>
					   <input type="text" name="position" id="curpos" value="chr22" style="width:150px;"/>&nbsp;&nbsp;<select style="width:200px; display:none;" id="dset" name="dataset" onchange="loadInitTrack()"></select><input type="button" value="GO" onclick="drawQuery3D()"/>&nbsp;<input type="checkbox" id="gviewid" onclick="showGenomeView()"/>Show Genome View&nbsp;<input type="button" value="Upload" onclick="showUploadDialog()" />&nbsp;<input type="button" value="Goto Genome" onclick="gotoGenome()"><input type="button" value="Goto Topology" onclick="gotoTopoView()"/><input type="button" value="Export" onclick="savePageAs()" style="width:50px" /><img src="/circosweb/images/wait.gif" id="waitexport" style="display:none;" /><input type="hidden" id="windownamount"/></div>	
					   <a id="my_link" style="display:none;"> </a>
  						<div id="firing_div" style="display:none;"></div>	
						 <div>
						 	<table cellpadding="5" cellspacing="0">
								<tbody>
									<tr>
									<td valign="top"><input type="button" onclick="changeLineModel()" value="Line"></td>
									<td valign="top"><input type="button"onclick="changeSphareModel()" value="Sphere"></td>
									<td valign="top"><input type="checkbox" id="labelid" onclick="showOrHideLabel()" value="1" />Show Label </td>
									<td valign="top"> 
									Always redraw<input name="redrawtrack" type="radio" value="1" />Yes <br/> <input name="redrawtrack" type="radio" value="2" checked="checked">No
									</td>
																		
									<td style="padding-left:5px;" valign="top"><span class="note" style="font-size:10px; font-weight:bold;padding-right:20px;">Note: use "Ctrl"+left mouse to move 3D model;&nbsp;&nbsp;use "Alt" +left mouse to capture partial 3D model; <br/>use "Shift"+left mouse to highlight Interaction in Genome View</span></td>
									
									</tr>
									
								</tbody>
							</table>
						</div>
						 <!--begin draw physical view--> 
						 <div>

							 <table id="hisheatmapid" style="padding-bottom:5px;">
								<tr><td id="featuremapid"></td></tr>
							 </table>						 						
							 <div id="trackContainer" style="position: absolute;display: block; padding-right: 0px; height:800px;z-index:5;">
								 <div id="gldiv" style=" height: 800px; margin: 0; padding: 0; border: 0;position:relative;z-index:6;">
								 
									
								 </div>
	
								  <div id="indicator" style="margin-top:-800px; padding:0; border:0; position:relative; z-index:20; background-color:#FF0000;opacity:0.4; display:none;">	
								 </div>
								 <div id="tracktext" style=" padding:0; border:0; background-color:#ffffff; position:relative; z-index:999; display:none;">
									
								 </div>
								  <div id="waitimgid" style=" display:none;position: absolute;top: 200px; left: 50%; z-index:8; width: 64px;height: 66px;">
									 <img id="img-spinner" src="/circosweb/images/ajax-loader.gif" alt="Loading" width="64" height="64"/>
								 </div>
									
								<div id="idcontextmenu" style="height:40px;width:40px;position:relative;z-index:10010; margin-left:0px; background-color:#FFFFFF;"></div>
							 </div>
							<div id="windowsize-vertical" style="height:200px;position:absolute;z-index:10005; margin-left:10px;margin-top:10px; "></div>
							
							
							
						 </div>
						 
						
						 <!--finish draw physical view-->
					</div>
					<div style="clear:both;"></div>
					
					<div id="genepanelid" style="display:block; padding-top:800px;">
							<div class="header_content">
								<div>
								<input type="button" value="Show Gene" onclick="showGenePanel()" />&nbsp;&nbsp;<input type="button" value="Hide Gene" onclick="hideGenePanel()" /></div>
								
								<div id="idgenetable" style="height:400px; overflow:scroll;">
									
								</div>
							</div>
						</div> 
			 </div>	

        <div style="clear: both;"></div>
		
		<!--display menu-->
		<div id="dmenu" style="display:none;"></div>
		<div id="dupload" style="display:none;"></div>
		
		
	
	<div class="pane ui-layout-west" id="westid">
		 <div>
		 	<table cellpadding="5" cellspacing="0">
				<tbody>
					<tr><td>Please select one dataset </td></tr>
					<tr><td>
					<!--get 3d model from mysql -->
					<select id="idPhyModel" onchange="choose3DmodelFunc(1)">
						
					</select></td></tr>
					<tr><td>Resolution Size&nbsp;<select id="idBinsize" onchange="choose3DmodelByBinsizeFunc()"></select></td></tr>
					<tr><td>According Features</td></tr>
				</tbody>
			</table>

		 	
		 </div>
		 <div  id="trackid">
			<table id="idPhyModelFeature">
				<tbody>				
				</tbody>
			</table>
			 <div  id="trackidlist">
			 
			 </div>
         </div> 
	
	</div>
	
	 <div class="pane ui-layout-east" id="eastid">
		<div id="genomeviewid" style="display:none;margin-top:20px; ">
			<iframe id="gviewframeid"  style="height:930px;width:100%; padding-left:0px;margin-left:0px;">				
			</iframe>
		</div>	
	</div>
	

</div>


	<jsp:include page="/inc/footer.jsp" />
	<script type="text/javascript" src="/circosweb/js/cookie.js"></script>
	<script type="text/javascript" src="/circosweb/js/jspdf.min.js"></script>
	<script type="text/javascript" language="javascript" src="/circosweb/js/physical/track.js"></script>
	<script type="text/javascript" language="javascript" src="/circosweb/js/physical/init.js"></script>
	<script type="text/javascript" language="javascript" src="/circosweb/js/physical/physical.js"></script>
	
	<script type="text/javascript" language="javascript">
		showTabs('3');
		
		//when check show genome view, we will use the default screen as the div width,
		// however,we will also support user to drag toward physical view and genome view

	    function showGenePanel(){
			$("#idgenetable").css("display","block");
		
		}
		
		function hideGenePanel(){
			
			$("#idgenetable").css("display","none");
		}
		

	</script>

</body>
</html>

