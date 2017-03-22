
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/jquery-1.9.1.js" ></script>

<title>circlet view</title>
<script type="text/javascript">
	var bandlst;
</script>
</head>

<body onload="loadLayout()">
<div id="container">
	<div id="content">
	  <div id="left-column1" style="margin-left:5px; width:95%;">
			<div class="header_border">
				<div class="header_content" style="position:relative;">
					 <div>Go to&nbsp;
					   <input type="text" name="position" id="curpos" value="hs7:27053397..28053397" style="width:200px;"/>&nbsp;&nbsp;in Dataset&nbsp;<select style="width:200px;" id="dset" name="dataset"><option value="GSE43070">GSE43070(IMR90)</option><option>CID000002</option></select><input type="button" value="go" onclick="singleChrom()"/></div>
                     
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
				  </select>&nbsp;Zoom out&nbsp;<input type="button" value="1x" onclick="zoomout_func(2)" />&nbsp;<input type="button" value="3x" onclick="zoomout_func(3)" />&nbsp;<input type="button" value="10x" onclick="zoomout_func(10)"/></div>
					 
					 <div id="err" style="margin-top:10px;margin-bottom:10px; color:navy; font-size:medium; font-family:sans-serif; display:none;">
					 	Detailed view is limited to 5 Mbp.The histogram density view will be showed.<br/> 
						If you want tow show the detail view ,click and drag on one of the scalebars to make a smaller selection.
					 </div>
                     <div id="genome_region" style="margin-top:20px;">
					 	
                     	<div style="position: relative; z-index: 103;"><canvas id="genome" width="900" height="40"></canvas></div>
						<div id="choose_indicator" style="position: absolute; border: 1px solid rgb(128, 166, 255); z-index: 104; display: none;">
							<div style="background-color: blue; opacity: 0.1; height: 100%; width: 100%;"></div>
						</div>
                     </div>
					 <!-- this used to show static picture for all data track of single chromosome or just interaction data of whole genome-->
					 <div id="showpic">
					 
					 </div>
					 
					 <div style="position: absolute;display: block; padding-right: 10px; height:700px;">
						 <div style="margin-top:10px;position: relative; z-index: 100; " align="left">
							<canvas id="canvas" width="666" height="676"></canvas>
						 </div>
					 </div> 
					 <div>
                    	 <canvas id="glasspane" style="position: absolute; z-index: 101; display: none; "></canvas> 
					 </div>
					
		  </div>		
		</div>
        <div style="clear: both;"></div>
	</div>

</div>
<script type="text/javascript" src="/circosweb/js/circos_simple.js"></script>
<script type="text/javascript" language="javascript">

function singleChrom(){
	show_mode =1;
	zoomin_func(1);
}
//zoom in
function zoomin_func(fold){
	if(show_mode ==1){//single
		var querypos = $("#curpos").val();
		var index1 = querypos.indexOf(":");
		var index2 = querypos.indexOf(".");
		chr = querypos.substring(0,index1);
		pos_start = querypos.substring(index1+1,index2);
		pos_end = querypos.substring(index2+2,querypos.length);
		pos_start = parseInt(pos_start);
		pos_end = parseInt(pos_end);
		
		var res = zoomin(pos_start,pos_end,fold);
		if(res <0){		
			return ;
		}
		querypos = chr+":"+ideogram_start+".."+ideogram_end;
		$("#curpos").val(querypos);	
	
	}else if(show_mode ==2){
		genome_zoomin(fold);
	
	}



	

}

//zoom out
function zoomout_func(fold){
	if(show_mode ==1){
		var querypos = $("#curpos").val();
		var index1 = querypos.indexOf(":");
		var index2 = querypos.indexOf(".");
		chr = querypos.substring(0,index1);
		pos_start = querypos.substring(index1+1,index2);
		pos_end = querypos.substring(index2+2,querypos.length);
		pos_start = parseInt(pos_start);
		pos_end = parseInt(pos_end);
		
		zoomout(pos_start,pos_end,fold);
		querypos = chr+":"+ideogram_start+".."+ideogram_end;
		$("#curpos").val(querypos);	
	}else if(show_mode ==2){
		genome_zoomout(fold);
	}
	
}

function changeScope(){
	var scope = $("#scopeid").val();
	var querypos = $("#curpos").val();
	var index1 = querypos.indexOf(":");
	var index2 = querypos.indexOf(".");
	chr = querypos.substring(0,index1);
	pos_start = querypos.substring(index1+1,index2);	
	pos_start = parseInt(pos_start);
	scope = parseInt(scope);
	pos_end = pos_start + scope;
	
	ideogram_start = pos_start;
	ideogram_end = pos_end ;
	
	querypos = chr+":"+ideogram_start+".."+ideogram_end;
	$("#curpos").val(querypos);	
	//var canvas = document.getElementById("canvas");
	//var ctx = canvas.getContext('2d');
	computeRegionRadian();
	
}


</script>

</body>
</html>

