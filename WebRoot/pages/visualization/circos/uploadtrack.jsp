

<html>
<head>
<script type="text/javascript" src="/circosweb/js/jquery-ui.js" ></script>
<script type="text/javascript" src="/circosweb/js/jquery.form.js" ></script>
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />

</head>
<body>
<div class="header_content" id="physical-upload">
	<form action="/circosweb/pipeline/circletAddTrack.action" method="post" enctype="multipart/form-data" onSubmit="return checkEmpty()">
	<input type="hidden" name="circosTrack.category" value="Add Track"/>
	<table width="458" cellpadding="5" cellspacing="0">
		<tbody>
				<tr><td>Dataset Name<span style="color:red;">*</span></td><td><input id="updatasetid"  type="text" name="circosTrack.dataset" value="" style="width:200px;"/>&nbsp;<img src="/circosweb/images/help.gif" title="To upload multiple data track into the same view,please fill the same dataset name"/> <span id="iddataseterr" style="color:red;"></span></td></tr>
				<tr><td width="123">Track Name<span style="color:red;">*</span></td>
				<td width="313"><input type="text" name="circosTrack.name" value="" style="width:200px;" id="idtrackname"/>&nbsp;<img src="/circosweb/images/help.gif" title="An unique name consitutes by charactersA-Za-z,numbers0-9 and _ without white spaces" /><span id="idnameerr" style="color:red;"></span></td></tr>
				<tr><td>Organism</td><td><select style="width:200px;" name="circosTrack.organism">
				<option value="hg18">Human(hg18)</option>
				<option value="hg19">Human(hg19)</option></select></td></tr>
				<tr><td height="30">Glyph Type</td>
				<td><select style="width:200px;" name="circosTrack.glyph"><option value="arc">Interaction</option><option value="histogram">Histogram</option><option value="gene">Gene</option></select></td></tr>
				
				<tr><td height="30">File Format</td>
				<td><select name="circosTrack.storeclass" style="width:200px;"><option value="GFF3">GFF3</option></select></td></tr>
				<!--<tr><td>Catagory</td><td><input type="text" name="circosTrack.category" value="" style="width:200px;"/></td></tr> -->
				
				<!--<tr><td>Unique Key</td><td><input type="text" name="circosTrack.key" value="" style="width:200px;"/></td></tr>	 -->
							
				<tr><td>Upload File<span style="color:red;">*</span></td><td><input name="myFile" type="file" size="35" id="idmyfile"/><span><a href="/circosweb/pages/help/help_topoview.jsp#31" style="color:red;">format</a></span><span id="idfileerr" style="color:red;"></td></tr>
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
	
	var binsize = $("#updatasetid").val();
	if(binsize == ""){
		res = false;
		$("#updatasetid").html("Dataset name can not be empty");
	}else{
		$("#updatasetid").html("");
	}
	
	return res;

}

</script>

</body>

</html>