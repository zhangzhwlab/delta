<%@page language="java" import="java.util.Date" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/menu.js"></script>
<script type="text/javascript" src="/circosweb/js/jquery-1.9.1.js"></script>
<link rel="stylesheet" href="/circosweb/css/jquery-ui.css" />
<script type="text/javascript" src="/circosweb/js/jquery-ui.js" ></script> 
<title>Delta-analysis</title>
</head>

<body>
<div id="container">
	<jsp:include page="/inc/header.jsp" />
	<div id="content">
	  <div id="right-column1" style="margin-left:10px;width:20%;margin-top:10px;">
	  	
			<div class="block"><img src="/circosweb/images/breadcrumb.gif" />Upload own data </div>
			<ul>
				<li><a href="#hicmatrix">Upload Hi-C Matrix file</a></li>
				<li><a href="#interaction">Upload Hi-C interaction file</a></li>
				<li><a href="#tad">Upload Hi-C TAD file</a></li>
				<li><a href="#3dmodel">Upload Hi-C 3D model file</a></li>
			
			</ul>	
			<div class="block"><img src="/circosweb/images/breadcrumb.gif" />Test Data</div>
			<div class="header_content">
				<b>Organism:</b> hg19<br/> <b>Cell line:</b> K562<br/><b>Bin size:</b> 50,000<br/> 
				<b>First bin:</b> chr11:4,500,000-4,550,000<br/>
				<b>Last bin:</b>&nbsp;chr11:6,500,000-6,550,000<br/>
				<b>Download: </b>
				<a href="/circosweb/download/useown/50kmatrix.tar.gz">Hi-C matrix file</a> <br/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/circosweb/download/useown/test_loop.tar.gz">Hi-C Interaction file</a> <br/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/circosweb/download/useown/test_tad.tar.gz">Hi-C TAD file</a><br/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/circosweb/download/useown/test_model.tar.gz">Hi-C 3D model file</a>
			</div>	
			<div class="block"><img src="/circosweb/images/breadcrumb.gif" />Demo Result</div>
			<div class="header_content"><a target="_blank" href="/pipeline/showMyResult.action?jobid=1489978055322">Show Demo</a></div>
		

	  	
	  </div>
	  <div id="left-column1" style="margin-left:5px; width:60%;">
			<div class="header_border">
			<%
				long ljogid = new Date().getTime();
				String jobid = ljogid+"";
			%>
				
				<form action="/circosweb/pipeline/owndataProcess.action" method="post" id="pipeform" onsubmit="return checkEmpty()">
				<input type="hidden" name="pipeBean.hicMatrixFile" id="idmatrixfile" />
				<input type="hidden" name="pipeBean.matrixFile" id="idloopfile"/>
				<input type="hidden" name="pipeBean.featureFile" id="idmodelfile" />
				<input type="hidden" name="pipeBean.tadFile" id="idtadfile" />
				<input type="hidden" name="pipeBean.jobid" value="<%=jobid%>" />
				<div class="header_content">
				<div class="header" id="basic">Upload Data</div>
				<div class="instruction"> we recommend to compress the uploaded file as *.zip or *.tar.gz and the size should be less than 2GB.<br/>
				</div>
				<div class="header_content" style="position:relative;">
					<div id="iderr" style="color:red;display:none;"></div>
					
					<table width="893" cellpadding="5" cellspacing="0">
						<tbody>
						     <tr><td colspan="2"><input type="checkbox" id="idusetest" name="pipeBean.useTest" value="1" onclick="checkUseTestData()">Use test data</td></tr>
						     
						</tbody>
					</table>
					 <div class="instruction">
				 &nbsp;&nbsp; &nbsp;Bin Size: the bin size of Hi-C contact matrix file. If the uploaded file has high resolution, we suggest you compute an interested region instead of the whole chromosomes.<br/>
				 &nbsp;&nbsp; &nbsp;Start Bin: the absolute bin index of start position, can be computed by chromsome position/binsize.The minimum value must be 1 <br/>
				 &nbsp;&nbsp; &nbsp; End Bin: the absolute bin index of end  position, can be computed by chromosome position/binsize <br/>
				
				 </div>
	
				 	<table>
					<tbody>	
							<tr>
								<td width="260">Organism</td>
								<td width="611">
									<select style="width:200px;" name="pipeBean.organism">
									<option value="hg18">Human(hg18)</option>
									<option value="hg19">Human(hg19)</option>
									<option value="mmu">Mouse(mm9)</option>
							  </select>	
							  </td>
							</tr>
							
							<tr>
							<td>Cell Line</td>
							<td><input type="text" style="width:200px;" name="pipeBean.cellline" value="K562" /></td>
							</tr>
							
							<tr>
								<td>Bin size<span style="color:red;">*</span></td>
								<td><input type="text" style="width:200px;" id="idbinsize" name="pipeBean.binsize" value="50000">&nbsp;bp <span id="iderrbinsize" style="color:red;"></span></td>
							</tr>
							<!--
							<tr>
								<td>The start position of matrix<span style="color:red;">*</span></td>
								<td><input type="text" style="width:200px;" id="idbinsize" name="pipeBean.startPosition" value="0">&nbsp;bp <span id="iderrbinsize" style="color:red;"></span></td>
							</tr> -->
							
							<tr>
							<td valign="top">Scope<span style="color:red;">*</span></td>
							<td>
							<table width="229" cellpadding="0" cellspacing="0">
								<tbody>
									<tr><td width="90">
							Chromosome
							<br/>
							</td><td width="137"><input type="text" id="idchr" style="width:100px;" name="pipeBean.chrom" value="11" /><span id="iderrchr" style="color:red;"></span></td></tr>
								<tr><td>Start Bin</td><td><input type="text" style="width:100px" id="idstart" name="pipeBean.startbin" value="90" /><span id="iderrstart" style="color:red;"></span></td></tr>
								<tr><td>End Bin</td><td><input type="text" id="idend" style="width:100px;" name="pipeBean.endbin" value="130"><span id="iderrend" style="color:red;"></span></td></tr>
								</tbody>
							</table>
							</td>
							</tr>
							<!--
							<tr>
							  <td>Observed value filter</td>
							  <td>Min<input type="text" name="pipeBean.minObserv" style="width:60px" value="1" />&nbsp;&nbsp;Max<input type="text" name="pipeBean.maxObserv" style="width:60px;" value="400"/></td>
							</tr> -->
							
						</tbody>
				  </table>		               		

					<table id="iduptable">
						<tbody>
							<tr>
							  <td colspan="2" id="idrowmatrix">Upload Hi-C matrix file&nbsp;&nbsp;&nbsp;<a href="/pages/pipeline/choose.jsp#matrixfile">format</a>&nbsp;</td>
							</tr>
							<tr id="hicmatrix" ><td colspan="2">
							<iframe src="/pages/pipeline/fileupload_own.jsp?flag=4&amp;jobid=<%=jobid%>" id="idfloop" width="500px" height="80px" ></iframe>
							</td></tr>
						
						
						
							<tr>
							  <td colspan="2" id="idrowmatrix">Upload Hi-C interaction loop file&nbsp;&nbsp;&nbsp;<a href="/pages/pipeline/choose.jsp#loopfile">format</a>&nbsp;</td>
							</tr>
							<tr id="interaction" ><td colspan="2">
							<iframe src="/pages/pipeline/fileupload_own.jsp?flag=1&amp;jobid=<%=jobid%>" id="idfloop" width="500px" height="80px" ></iframe>
							</td></tr>
							
							
							 <tr id="tad">
							   <td colspan="2" id="idrowmatrix">Upload Hi-C TAD file&nbsp;&nbsp;&nbsp;<a href="/pages/pipeline/choose.jsp#tadfile">format</a>&nbsp;</td>
							 </tr>
							<tr><td colspan="2"><iframe src="/pages/pipeline/fileupload_own.jsp?flag=2&amp;jobid=<%=jobid%>" id="idftad" width="500px" height="80px"> </iframe></td>
							</tr>
								
							 <tr id="3dmodel"><td colspan="2" id="idrowmatrix">Upload Hi-C 3D structure file&nbsp;&nbsp;&nbsp;<a href="/pages/pipeline/choose.jsp#3dfile">format</a>&nbsp;</td></tr>
							<tr><td colspan="2">
							<iframe src="/pages/pipeline/fileupload_own.jsp?flag=3&amp;jobid=<%=jobid%>" id="idf3dmodel" width="500px" height="80px">	</iframe>
							</td></tr>
							</tbody>
							</table>
				 </div>
				<div class="header_content">
					<input type="submit" value="Submit" /> &nbsp;&nbsp;<input type="reset" value="Reset" />
				</div>
			  </form>
			
		  </div>		
		</div>
        <div style="clear: both;"></div>
		
	</div> <!--end content-->
	<jsp:include page="/inc/footer.jsp" />

</div>

<script language="javascript" type="text/javascript">
	showTabs('6');
	
	function checkEmpty(){
		 var res= true;
		 if($("#idusetest").prop("checked") == false){
		 	var matrixfile = $("#idmatrixfile").val();
		 	var loopfile = $("#idloopfile").val();
			var tadfile = $("#idtadfile").val();
			var modelfile = $("#idmodelfile").val();
			if((matrixfile == null || matrixfile.length ==0)&&(loopfile == null || loopfile.length ==0) && (tadfile == null ||tadfile.length == 0 )&& (modelfile == null || modelfile.length ==0) )
			{
				$("#iderr").html("You must upload one file of them");
				$("#iderr").css("display","block");
				res = false;
			}else{
				$("#iderr").css("display","none");
			}
		 } 
	 	return res;
	}
	
	
	
	function checkUseTestData(){
		if($("#idusetest").prop("checked") == true){
			disableform("pipeform",true);
			 document.getElementById("idmatrixfile").value= "/share/disk1/work/bioinformatics/tangbx/hic/testdata/useown/50k.matrix";
		    document.getElementById("idloopfile").value= "/share/disk1/work/bioinformatics/tangbx/hic/testdata/useown/test_loop.gff3";
			document.getElementById("idtadfile").value= "/share/disk1/work/bioinformatics/tangbx/hic/testdata/useown/test_tad.gff3";
			document.getElementById("idmodelfile").value= "/share/disk1/work/bioinformatics/tangbx/hic/testdata/useown/test_model.xyz";
		}else{
			document.getElementById("idmatrixfile").value="";
		     document.getElementById("idloopfile").value="";
			 document.getElementById("idtadfile").value="";
			 document.getElementById("idmodelfile").value="";
			disableform("pipeform",false);
		
		}
	
	
	}
	
	
	function disableform(idstr,flag){
	
	var formobj = document.getElementById(idstr);
	//alert(formobj);
	var frm_elements;
	if(formobj){
	
		frm_elements  = formobj.elements;
	}
	//alert(frm_elements);
	var readonly = "readonly";
	if(!flag){
		readonly="";
	}
	for (i = 0; i < frm_elements.length; i++) {    
		//alert(frm_elements.length);
		field_type = frm_elements[i].type.toLowerCase();
		switch (field_type) {    
		 case "text":
		    frm_elements[i].readOnly   = flag; 
		 	$("#idemail").prop('readonly', false);  
			break;   
		 case "password":     
		 case "textarea":             
		 case "radio":  		 	   
		 case "checkbox":    
		   frm_elements[i].readOnly   = flag;     
		  $("#idusetest").prop('readonly', false);
		  break;
		 case "select-one":    
		 case "select-multi":         
			frm_elements[i].readOnly   = flag;      
			break;     
		  default:         
			break;     
		} 
	}
	
		if(flag){
				$("#iduptable").hide(); 
		}else{
			$("#iduptable").show(); 

		   
		}	
}



</script>



</body>
</html>

