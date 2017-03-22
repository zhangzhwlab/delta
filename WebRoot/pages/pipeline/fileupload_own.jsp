<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<title>file upload</title>
<script src="/circosweb/js/jquery-1.9.1.js"></script>
<script src="/circosweb/js/jquery.form.js"></script>

<script type="text/javascript" language="javascript">

$(document).ready(function() {
var options = {
        beforeSend : function() {
                $("#progressbox").show();
                // clear everything
                $("#progressbar").width('0%');
                $("#message").empty();
                $("#percent").html("0%");
        },
        uploadProgress : function(event, position, total, percentComplete) {
                $("#progressbar").width(percentComplete + '%');
                $("#percent").html(percentComplete + '%');

                // change message text to red after 50%
                if (percentComplete > 50) {
                $("#message").html("<font color='red'>File Upload is in progress</font>");
                }
        },
        success : function() {
                $("#progressbar").width('100%');
                $("#percent").html('100%');
        },
        complete : function(response) {
        $("#message").html("<font color='blue'>Your file has been uploaded!</font>");
		var flagparam = "<s:property value='#parameters.flag'/>";
		var uploadfile = document.getElementById("myfile").value;
			var lastin = uploadfile.lastIndexOf("/");
			if(lastin>-1){
				uploadfile = uploadfile.substring(lastin,uploadfile.length);
			}else{
				lastin =  uploadfile.lastIndexOf("\\"); //windows
				if(lastin>-1){
					uploadfile = uploadfile.substring(lastin+1,uploadfile.length);
				}
			}
			if(uploadfile.indexOf('.tar.gz')>-1){
				uploadfile = uploadfile.substring(0,uploadfile.length-7);
			}
			else if(uploadfile.indexOf('.zip')>-1){
				uploadfile = uploadfile.substring(0,uploadfile.length-4);
			}
		
		
		
		if(flagparam==1){
			parent.document.getElementById("idloopfile").value=uploadfile ;
		}else if(flagparam ==2){
			parent.document.getElementById("idtadfile").value= uploadfile;
		}else if(flagparam == 3){
			parent.document.getElementById("idmodelfile").value= uploadfile;
		} else if(flagparam == 4){
			parent.document.getElementById("idmatrixfile").value= uploadfile;
		}
		
		
		
        },
        error : function() {
       	 $("#message").html("<font color='red'> ERROR: unable to upload files</font>");
        }
};
	$("#UploadForm").ajaxForm(options);
});

</script>
</head>

<body>


<form id="UploadForm" action="/pipeline/uploadNeedFile.action" method="post" enctype="multipart/form-data">
	<input type="hidden" name="curjobid" value="<s:property value='#parameters.jobid'/>" />
	<input type="hidden" name="fileflag" value="<s:property value='#parameters.flag'/>" />
    <input type="file" size="60" id="myfile" name="file"> 
     <input type="submit" value="Upload">
       <div id="progressbox">
         <div id="progressbar"></div>
         <div id="percent">0%</div>
       </div>
 <br />
<div id="message"></div>
</form>
</body>
</html>

