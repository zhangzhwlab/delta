
<html>
<head>
<script type="text/javascript" src="/circosweb/js/jquery-ui.js" ></script>
<script type="text/javascript" src="/circosweb/js/jquery.form.js" ></script>
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />

</head>
<body>
<div class="header_content" id="custom_track">
	<form action="/circosweb/pipeline/physicalAddTrack.action" method="post" enctype="multipart/form-data" onSubmit="return checkEmpty()">
	<input type="hidden" name="circosTrack.category" value="Custom Track"/>
	<input type="hidden" name="circosTrack.color" value="red"/>
	<table width="608" cellpadding="5" cellspacing="0">
		<tbody>
				<tr><td>Dataset Name</td><td><input id="updatasetid" type="text" name="circosTrack.dataset" value="" style="width:200px;"/>&nbsp;<img src="/images/help.gif" title="A default dataset will be generated, but you can modify it to an existed dataset name." /></td></tr>
				<tr><td width="117">Track Name<span style="color:red;">*</span></td>
				<td width="319"><input type="text" name="circosTrack.name" value="" style="width:200px;" id="idtrackname"/> <img src="/circosweb/images/help.gif" title="An unique name consitutes by charactersA-Za-z,numbers0-9 and _ without white spaces" /><span id="idnameerr" style="color:red;"></span></td></tr>
				<tr><td></td><td style="padding-top:0px;font-size:12px;">(such as 3dmodel_k562_10000)</td></tr>
				<tr><td>Organism</td><td><select style="width:200px;" name="circosTrack.organism">
				<option value="hg18">Human(hg18)</option>
				<option value="hg19">Human(hg19)</option>
				</select></td></tr>
				<tr><td height="30">Glyph Type</td>
				<td><select style="width:200px;" name="circosTrack.glyph" id="idglyph" onChange="changeFileFormat()">
				<option value="3dmodel" selected="selected">3dmodel</option>
				<option value="gene">Gene</option>
				<option value="circle">Annotated feature</option>
				<option value="peak">Interaction</option>
				<option value="tad">TAD</option>
				</select> <img src="/circosweb/images/help.gif" title="3dmodel represents a physical structure, please access help information for XYZ and JSON" /></td></tr>
				<!--<tr><td>Color</td><td><input type="text" name="circosTrack.color" value="red"  style="width:200px;"/></td></tr> -->
				<!--<tr><td>Catagory</td><td><input type="text" name="circosTrack.category" value="" style="width:200px;"/></td></tr> -->
				<!--<tr><td>Unique Key</td><td><input type="text" name="circosTrack.key" value="" style="width:200px;"/></td></tr> -->				
				<tr><td>File format</td>
				<td><select id="3dmodel_format"  style="width:200px;" name="circosTrack.storeclass">
				<option value="xyz" selected="selected">XYZ</option>
				<option value="json">JSON</option>
				</select>&nbsp;<img src="/circosweb/images/help.gif" title="Please find the file format from help document" /></td></tr>
				
				<tr id="3dmodel_binsize"><td>Resolution size<span style="color:red;">*</span></td><td><input type="text" id="idbinsize" name="circosTrack.binsize" value="" style="width:200px;"/>&nbsp;<img src="/circosweb/images/help.gif" title="The resolution size of the uploaded model file" /><span id="idbinsizeerr" style="color:red;"></span></td></tr>
				<tr id="err_3dmodel_binsize"><td></td><td style="padding-top:0px;font-size:12px;">(such as 50000)</td></tr>
				
				
				<tr><td>Upload File<span style="color:red;">*</span></td><td><input name="myFile" type="file" size="35" id="idmyfile" /><span><a href="http://124.16.129.16:9612/circosweb/pages/help/help_physicalview.jsp#41" style="color:red;">format</a></span><span id="idfileerr" style="color:red;"></span></td></tr>
		</tbody>
  </table>
	<div class="header_content">
		<input type="submit" value="Submit" />
	
	</div>

</form>
</div>

<script type="text/javascript" language="javascript">
	var timestr = new Date().getTime();
	$("#updatasetid").val(timestr);
	
	function changeFileFormat(){
		var glyph = $("#idglyph").val();
		if(glyph=="3dmodel"){
			$("#3dmodel_format").empty();
			var option="<option value=\"xyz\">XYZ</option>";
			$("#3dmodel_format").append(option);
			option="<option value=\"json\">JSON</option>";
			$("#3dmodel_format").append(option);
			$("#3dmodel_binsize").css("display","");
			$("#3dmodel_startbin").css("display","");	
			$("#err_3dmodel_binsize").css("display","");
			$("#err_3dmodel_startbin").css("display","");		
		}else{
			$("#3dmodel_format").empty();
			var option="<option value=\"GFF3\">GFF3</option>";
			$("#3dmodel_format").append(option);
			$("#3dmodel_binsize").css("display","none");
			$("#3dmodel_startbin").css("display","none"); //err_3dmodel_binsize
			$("#err_3dmodel_binsize").css("display","none");
			$("#err_3dmodel_startbin").css("display","none");
		}
	}

function checkEmpty(){
	var res = true;
	var trackname = $("#idtrackname").val();
	if(trackname == ""){
		res = false;
		$("#idnameerr").html("The track name can not be empty");
	}else{
		$("#idnameerr").html("");
	}
	var myfile = $("#idmyfile").val();
	
	if(myfile == ""){
		res = false;
		$("#idfileerr").html("The upload file can not be empty");
	}else{
	
	$("#idfileerr").html("");
	}
	
	var binsize = $("#idbinsize").val();
	if(binsize == ""){
		res = false;
		$("#idbinsizeerr").html("The resolution size can not be empty");
	}else{
		$("#idbinsizeerr").html("");
	}
	
	return res;

}


</script>

</body>

</html>