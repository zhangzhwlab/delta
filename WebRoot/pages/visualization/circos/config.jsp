<html>
<head>
<script type="text/javascript" src="/circosweb/js/jquery-1.9.1.js" ></script>
<script type="text/javascript" src="/circosweb/js/jquery-ui.js" ></script>
<script type="text/javascript" src="/circosweb/js/jquery.form.js" ></script>
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" language="javascript">
 $(document).ready(function() {  
 loadConfigParam();
 });

</script>


</head>
<body>
<div id="dialog_config">
<div class="header_content" >
	<p>Canvas Size: &nbsp;&nbsp;&nbsp;width <input type="text" value="600" id="canvas_widthid" style="width:80px;" />&nbsp;&nbsp;height<input type="text" id="canvas_hid" value="600" style="width:80px;" /></p>
	<p>Ideogram Size: &nbsp;&nbsp;&nbsp;radius <input type="text" value="250" id="radiusid" style="width:80px;"/></p>
	
</div>
<div class="header_content"><input type="button" value="Submit" onClick="changeConfig()"></div>
</div>
<script type="text/javascript" language="javascript">
	function loadConfigParam(){
		var canvas_size = window.parent.config_getConfig();

			
		$("#canvas_widthid").val(canvas_size[0]);
		$("#canvas_hid").val(canvas_size[1]);
		$("#radiusid").val(canvas_size[2]);
	
	}
	
	function changeConfig(){
	var a = $("#canvas_widthid").val();
	var b = $("#canvas_hid").val();
	var c = $("#radiusid").val();
		window.parent.config_setConfig(a,b,c);
		
	}
</script>
</body>
</html>
