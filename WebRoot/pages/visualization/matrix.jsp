
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/jquery-1.9.1.js" ></script>
<title>Matrix HeatMap</title>
<style>
svg,
canvas {
  position: absolute;
}

.axis .text {
  font: 10px sans-serif;
  color:#000000;
}

.axis path,
.axis line {
  fill: none;
  stroke: #000;
  shape-rendering: crispEdges;
}

.axis path {
  display: none;
}
</style>
</head>

<body onload="loadLayout()">
<div id="container">
	<jsp:include page="/inc/header.jsp" />
	<div id="content">
	  <div id="left-column1" style="margin-left:5px; width:95%;">
			<div class="header_border">
				<div class="header_content" style="position:relative;">
					<div>Go to&nbsp;<select style="width:60px;" id="chromid" onchange="ChooseChrom()"><option value="chr21" selected="selected">chr21</option>
					<option value="chr12">chr12</option>
					</select>
					   <input type="text" name="position" id="curpos" value="chr21:1..1174" style="width:200px;"/>&nbsp;&nbsp;in Dataset&nbsp;<select style="width:200px;" id="dset" name="dataset"><option value="hES" selected="selected">hES</option><option value="GM12878">GM12878</option></select>&nbsp;<input type="button" value="go" onclick="singleChrom()"/>&nbsp;&nbsp;<strong>View Set:</strong><input id="genomech" type="checkbox" onclick="toggleGenome()">Genome View</div>
                     
				  <div style="padding-top:10px;">Zoom in <input type="button" value="1x" onclick="zoomin_func(2)"/>&nbsp;<input type="button" value="3x" onclick="zoomin_func(3)" />&nbsp;
				   BinSize<select id="scopeid" style="width:100px;" onchange="changeScope()">
				  		<option value="5" selected="selected">5*5</option>
						<option value="10">10*10</option>
						<option value="20">20*20</option>
						<option value="50">50*50</option>
						<option value="100">100*100</option>
						<option value="200">200*200</option>						
				  </select>&nbsp;Zoom out&nbsp;<input type="button" value="1x" onclick="zoomout_func(2)" />&nbsp;<input type="button" value="3x" onclick="zoomout_func(3)" />
				  
				  <span id="waitid">
				  <img src="/circosweb/images/wait.gif" border="0" />
				  </span>
				  </div>
					<div id="heatmapdiv" style="z-index:1; position:relative; width:800px;height:650px;">
						<img id="matriximg" src="../../images/1m.png" border="0" usemap="#matrix" />
						

					</div>
	                <div id="area" class="area" style=" width:20px; height:20px; position:absolute; left:105px; top:130px;" > 
					
					</div>			
				</div>
			</div>
		  </div>		
		</div>
        <div style="clear: both;"></div>
		<div class="header_border"  id="genomeid" style="display:none;margin-left:5px;width:95%">
		<div class="header">Genome View</div>
		<div class="header_content" > <!--genoview-->
			<iframe id="genomeframe" src="#" width="95%" height="600px"></iframe>
		</div>
		</div>
	</div> <!--end content-->
	<jsp:include page="/inc/footer.jsp" />

</div>
<script language="javascript" src="/circosweb/js/matrix.js"></script>
<script language="javascript" src="/circosweb/js/matrix/d3.v3.min.js"></script>
<script language="javascript" src="/circosweb/js/matrix/colorbrewer.js"></script>
<script language="javascript" type="text/javascript">
		showTabs('6');
	function loadInitCircos(){
	$.ajax({
				url:'/circosweb/ajax/initMatrix.action',
				type:'post',
				dataType:'json',
				async: false,
				success:function(data){
					$("#dset").empty();
					$.each(data.datasetList,loadDataset);
					InitMatirxDataSet();
				},
				error:function(){
					alert("Init circos datasets fail.");			
				}
			
			});
	}
	
	function loadDataset(index,value){
		var option="<option value='"+value.conf+"'>"+value.name+"</option>";
		$("#dset").append(option);
	}
	
	function InitMatirxDataSet(){
		var curconf = $("#dset").val();
		var params={"curDataset":curconf};
	
	$.ajax({
		url : '/circosweb/ajax/loadMatrixDataSet.action',
		type : 'post',
		dataType : 'json',
		data : params,
		async: false,
		success : function(data){
			config.matrix_file = data.matrixTrack.matrixFile;
			//alert(config.matrix_file);
			if(data.speciesJson != undefined && data.speciesJson != null ){
				$("#chromid").empty();
				chrom_lst=[];

			    $.get(data.speciesJson,function(result){
					result = eval(result);

					for(i=0;i<result.length;i++){
						var chromdata = result[i];
						//alert(chromdata.name);
						var option = "<option value='"+chromdata.name+"'>"+chromdata.name+"</option>";
						if(i==0){
							selectvar = chromdata.name;
						}
						$("#chromid").append(option);
						
						var temp_start = parseInt(chromdata.qbin_start);
						var temp_end = parseInt(chromdata.qbin_end);
						alert(chromdata.qbin_start);
						var chrom_arr={"chr":chromdata.name,"qbin_start":temp_start,"qbin_end":temp_end,"resolution":chromdata.resolution};
						chrom_lst.push(chrom_arr);
					}
					
					$("#chromid").find("option[text='"+selectvar+"']").attr("selected",true);
				    ChooseChrom();  
				});
			}
		},
		error : function(){
			alert("load tracks data error!");
		}
	});
	
	}
	
	function ChooseChrom(){
		 var cur_chr = $("#chromid").val();
		 for(i=0;i<chrom_lst.length;i++){
			var chrom_data = chrom_lst[i];
			var s_chr = chrom_data.chr+"";
			if(s_chr == cur_chr){
				 qbin_start = parseInt(chrom_data.qbin_start);
				 qbin_end = parseInt(chrom_data.qbin_end);
				 bin_size = qbin_end - qbin_start +1;
				 resolution = chrom_data.resolution;
				var tquerypos = s_chr+":"+qbin_start+".."+qbin_end;
				$("#curpos").val(tquerypos);
				break;	
			}
		}
		 zoomin_func(1);
	}
	
	//choose single chrom
	function singleChrom(){
		zoomin_func(1);
	
	}
	
	function zoomin_func(fold){
		var querypos = $("#curpos").val();
		var index1 = querypos.indexOf(":");
		var index2 = querypos.indexOf(".");
		var chr = querypos.substring(0,index1);
		qbin_start = querypos.substring(index1+1,index2);
		qbin_end = querypos.substring(index2+2,querypos.length);
		qbin_start = parseInt(qbin_start);
		qbin_end = parseInt(qbin_end);
	
	
		var bin_start = qbin_start;
		var bin_end = qbin_end;
		zoomin(bin_start,bin_end,fold);
	}
	
	function zoomout_func(fold){
	
		var querypos = $("#curpos").val();
		var index1 = querypos.indexOf(":");
		var index2 = querypos.indexOf(".");
		var chr = querypos.substring(0,index1);
		qbin_start = querypos.
		substring(index1+1,index2);
		qbin_end = querypos.substring(index2+2,querypos.length);
		qbin_start = parseInt(qbin_start);
		qbin_end = parseInt(qbin_end);
	
	
		var bin_start = qbin_start;
		var bin_end = qbin_end;
		zoomout(bin_start,bin_end,fold);
	}
	
	function changeScope(){
	//document.getElementById("matriximg").removeEventListener("mousemove",mouse_move_func,false);
		s_bin = $("#scopeid").val();
		s_bin = parseInt(s_bin);
	//document.getElementById("matriximg").addEventListener("mousemove",mouse_move_func,false);	
		
	}
	
	
	function toggleGenome(){
		var checkg = $("#genomech").is(":checked");
		//alert(checkg);
		if(checkg == true){
			$("#genomeid").css("display","block");
			var url = "http://jbrowse.big.ac.cn/index.html?data=3cdb%2Fjson%2Fhuman&loc=5%3A1..53714931&tracks=ARC&highlight=" ;
			$("#genomeframe").attr("src",url);
		}else{
			$("#genomeid").css("display","none");
			$("#genomeframe").attr("src","#");
		}
	}
	

</script>



</body>
</html>

