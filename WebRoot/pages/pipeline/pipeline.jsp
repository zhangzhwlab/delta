<%@page language="java" import="java.util.Date" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/circosweb/js/menu.js"></script>
<script type="text/javascript" src="/circosweb/js/jquery-1.9.1.js"></script>

<title>Delta-analysis</title>
</head>

<body>
<div id="container">
	<jsp:include page="/inc/header.jsp" />
	<div id="content">
	  <div id="right-column1" style="margin-left:10px;width:20%;margin-top:10px;">
	  	
			<div class="block"><img src="/circosweb/images/breadcrumb.gif" />Pipeline</div>
			<ul>
			
				<li><a href="#general">1. Basic Information</a></li>
				<li><a href="#tad">2. Call TAD Parameters</a></li>
				<li><a href="#3dmodel">3. 3D Model Parameters</a></li>
			
			</ul>	
			<div class="block"><img src="/circosweb/images/breadcrumb.gif" />Test Data</div>
			<div class="header_content">
				<b>Organism:</b> hg19<br/> <b>Cell line:</b> K562<br/><b>Bin size:</b> 50,000<br/> 
				<b>First bin:</b><br/>chr11:4,500,000-4,550,000<br/>
				<b>Last bin:</b><br/>chr11:6,500,000-6,550,000<br/>
				<b>Download: </b><a href="/circosweb/download/50kmatrix.tar.gz">matrix file</a>
			</div>	
			<div class="block"><img src="/circosweb/images/breadcrumb.gif" />Demo Result</div>
			<div class="header_content"><a target="_blank" href="/pipeline/showResult.action?jobid=1478854512827">Show Demo</a></div>
		

	  	
	  </div>
	  <div id="left-column1" style="margin-left:5px; width:60%;">
			<div class="header_border">
			<%
				long ljogid = new Date().getTime();
				String jobid = ljogid+"";
			%>
				
				<form action="/circosweb/pipeline/pipelineProcess.action" method="post" id="pipeform" onsubmit="return checkEmpty()">
				<input type="hidden" name="pipeBean.matrixFile" id="idmatrixfile"/>
				<input type="hidden" name="pipeBean.featureFile" id="idfeaturefile" />
				<input type="hidden" name="pipeBean.startPosition" value="0" />
				<input type="hidden" name="pipeBean.jobid" value="<%=jobid%>" />
				<div class="header_content">
				<div class="header" id="basic">1.Upload Data</div>
				<div class="instruction"> we recommend to compress the uploaded file as *.zip or *.tar.gz and the size should be less than 2GB.<br/>
				However,we do not support large file to compute online because of the limited resource. In this case, we recommend to <a href="http://hicp.big.ac.cn/pages/download/download.jsp">download</a> our software package to analyze and visualize data result.
				</div>
				<div class="header_content" style="position:relative;">
					<table width="893" cellpadding="5" cellspacing="0">
						<tbody>
						     <tr><td colspan="2"><input type="checkbox" id="idusetest" name="pipeBean.useTest" value="1" onclick="checkUseTestData()">Use test data</td></tr>
						     <tr><td colspan="2" id="idrowmatrix">Upload  HiC matrix file&nbsp;&nbsp;</td>
							</tr>
							
							<tr><td colspan="2">
							<iframe src="/circosweb/pages/pipeline/fileupload.jsp?flag=1&amp;jobid=<%=jobid%>" id="idfmatrix" width="500px" height="80px">
							</iframe>
							</td></tr>
							</tbody>
							</table>
				 </div>
				 <div class="header" id="basic">2.Basic information</div>
				 <div class="instruction">Set basic information for the uploaded file, which will be used in the workflow. The detailed  parameters are as follows:<br/>
				 &nbsp;&nbsp; &nbsp;Bin Size: the bin size of Hi-C contact matrix file. If the uploaded file has high resolution,we suggest you compute an interested region instead of the whole chromosomes.<br/>
				 &nbsp;&nbsp; &nbsp;Start Bin: the absolute bin index of start position, can be computed by chromsome position/binsize.The minimum value must be 1. <br/>
				 &nbsp;&nbsp; &nbsp; End bin: the absolute bin index of end  position, can be computed by chromosome position/binsize <br/>
				
				 </div>
				 <div class="header_content" style="position:relative;">
				 	<table>
					<tbody>	
							<tr>
								<td width="471">Organism</td>
								<td width="400">
									<select style="width:200px;" name="pipeBean.organism">
									<option value="hg19">Human(hg19)</option>
									<option value="hg18">Human(hg18)</option>
									<option value="mmu">Mouse(mm9)</option>
							  </select>							  </td>
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
				</div>
					<div class="header" id="tad">3.Call Interaction Loop Parameters</div>
					<div class="instruction"><a href="http://www.unc.edu/~yunmli/FastHiC/tutorial.php" target="_blank">FastHIC</a> is used to call Interaction Loop</div>
					<div class="header_content"> 
					<table width="647" cellpadding="5" cellspacing="0">
						<tbody>
							<tr>
							  <td width="393">interaction loop filter probability</td>
							  <td width="232"><input type="text" style="width:200px;" name="pipeBean.fastpval" value="0.95"/></td></tr>
						</tbody>
					</table>
					</div>
			
					<div class="header" id="tad">4.Call TAD Parameters</div>
					<div class="instruction"><a href="http://compbio.cs.brown.edu/projects/tadtree/" target="_blank">TADtree</a> is used to call TAD</div>
					<div class="header_content"> 
					<table width="647" cellpadding="5" cellspacing="0">
						<tbody>
							<tr>
							  <td width="393">maximum allowable bin size for a TAD(S)</td>
							  <td width="232"><input type="text" style="width:200px;" name="pipeBean.maxbin" value="6"/></td></tr>
							 <tr><td>the trade-off between sensitivity and specificity(Gamma)</td><td><input type="text" style="width:200px;" name="pipeBean.gamma" value="1000" /></td></tr>
							 <tr><td>the total number of TADs allowed on a given chromosome(N)</td><td><input type="text" style="width:200px;" name="pipeBean.tadnumber" value="20" /></td>
							 </tr>
							 <tr>
							 <td>
							 the minimum scale over which changes in interaction preference can be 
robustly detected, p and q are given in units of bins</td>
							 
							 <td valign="top">p<input type="text" value="2" name="pipeBean.pval" style="width:85px">&nbsp;q<input type="text" name="pipeBean.qval" value="3" style="width:85px;"></td></tr>
						</tbody>
				  </table>
				  </div>
			
			
					<div class="header" id="3dmodel">5.3D Physical Model Parameters</div>
					<div class="instruction"><a href="http://www.people.fas.harvard.edu/~junliu/BACH/" target="_blank">BACH</a> or <a href="http://calla.rnet.missouri.edu/mogen/" target="_blank">MOGEN</a> is used to compute the spatial structure of the uploaded file.</div>
					<div class="header_content">
						Choose Predict Software&nbsp;&nbsp;&nbsp;&nbsp;<select id="idChooseModel" name="pipeBean.physicalModel" style="width:200px;" onchange="choosePModel()"><option value="BACH">BACH</option><option value="MOGEN">MOGEN</option></select>
					</div>
					<div id="idBACHParams" class="header_content">
						<table width="952" cellpadding="5" cellspacing="0">
							<tbody>
								<tr><td><strong>BACH Parameters:</strong></td></tr>
								<tr><td>used enzyme</td><td><select name="pipeBean.enzyme" style="width:200px;">
								<option value="HindIII">HindIII</option>
								<option value="MboI" selected="selected">MboI</option>
								<option value="NcoI">NcoI</option>
								</select></td></tr>
								<tr><td>reads length of sequencing</td><td><input value="101" name="pipeBean.readslen" style="width:200px;" /></td></tr>
								<tr><td width="391">number of particles in SIS(-K)</td>
								<td width="539"><input type="text" value="100" name="pipeBean.numparticle" style="width:200px;" /></td></tr>
								<tr><td>number of enrichment in SIS(-MP)</td><td><input type="text" value="10" name="pipeBean.numenrich" style="width:200px;"/></td></tr>
								<tr><td>number of Gibbs sampler iterations(-NG)</td><td><input type="text" value="5000" name="pipeBean.sampleiter" style="width:200px;" /></td></tr>
								<tr><td>length of tune interval in HMC(-NT)</td><td><input type="text" value="50" name="pipeBean.interval" style="width:200px;"/></td></tr>
								<tr><td>step size of leap frog in HMC(-L)</td><td><input type="text" value="50" name="pipeBean.stepsize" style="width:200px;" /></td></tr>
								
							</tbody>
					  </table>
					</div>
					<div id="idMOGENParams" class="header_content" style="display:none;">
						<table width="952" cellpadding="5" cellspacing="0">
							<tbody>
								<tr><td><strong>MOGEN Parameters:</strong></td></tr>
								<tr><td width="394">maximum distance between 2 adjacent points)</td>
								<td width="536"><input type="text" value="1.5" name="pipeBean.mogenAdjacentDist" style="width:200px;" /></td></tr>
								<tr><td style="vertical-align:top;">contact distance, points that are in contact should have square distance less than this</td><td>contact distance<input type="text" value="6.0" name="pipeBean.mogenContactDist" style="width:60px;"/><br/>positive minimum distance <input type="text" value="0.2" name="pipeBean.mogenPosMinDist" style="width:30px;"/><br/>negtive max distance <input type="text" value="50" name="pipeBean.mogenNegMaxDist" style="width:60px;"/></td></tr>
								
								
								<tr><td>increase this parameter to improve contact score,but will decrease non-contact score</td><td><input type="text" value="200.0" name="pipeBean.mogenPosMaxDistWeight" style="width:200px;" /></td></tr>
								<tr><td>increase this parameter if adjacent points are to close to each other</td><td><input type="text" value="1.0" name="pipeBean.mogenPosMinDistWeight" style="width:200px;"/></td></tr>
								<tr><td>increase this parameter to improve non-contact score, (but will decrease contact score)</td><td><input type="text" value="40.0" name="pipeBean.mogenNegMinDistWeight" style="width:200px;" /></td></tr>
								<tr><td>increase this parameter to prevent the structure from spanning too much (make the structure smaller)</td><td><input type="text" value="1.0" name="pipeBean.mogenNegMaxDistWeight" style="width:200px;" /></td></tr>
								<tr><td>learning rate for the optimization process</td><td><input type="text" value="0.01" name="pipeBean.mogenLearnRate" style="width:200px;" /></td></tr>
								<tr><td>maximum iteration numbers</td><td><input type="text" value="200000" name="pipeBean.mogenMaxIterator" style="width:200px;" /></td></tr>
								
							</tbody>
					  </table>
					</div>
					
					<div class="header">6.Email Information</div>
					<div class="instruction">Please provide an email address for your result, or bookmark the result page by pressing down the 'Bookmup this page' button on the top of the result page.</div>
					<div class="header_content">
						<table width="947" cellpadding="5" cellspacing="0">
							<tbody>
								<tr><td width="390">Email<span style="color:red;">*</span></td>
								<td width="535"><input type="text" name="pipeBean.email" id="idemail" style="width:200px;" /><span id="iderremail" style="color:red;"></span></td></tr>
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
	
	function choosePModel(){
		var modelflag = $("#idChooseModel").val();
		if(modelflag == "BACH"){
			$("#idBACHParams").css("display","");
			$("#idMOGENParams").css("display","none");
		}else if(modelflag == "MOGEN"){
			$("#idBACHParams").css("display","none");
			$("#idMOGENParams").css("display","");
		}
	
	}
	
	function checkEmpty(){
		 var res= true;
		 var idbinsize =$("#idbinsize").val();
		 if(idbinsize == null || idbinsize.length ==0 ){
		 	res = false;
			$("#iderrbinsize").html("bin size can not be empty");
			return res;
		 }
		 var idchr = $("#idchr").val();
		 var idstrart = $("#idstart").val();
		 var idend = $("#idend").val();
		 
		 if((idchr==null || idchr.length ==0) || (idstart==null ||idstart.length==0)||(idend ==null || idend.length ==0 )){
			res = false;
			$("#iderrend").html("chromsome ,start, end can not be empty");
			return res;
		}		 
		 
		 var idemail = $("#idemail").val();
		 if(idemail == null || idemail.length ==0 ){
		 	res = false;
				$("#iderremail").html("email can not be empty");
			return res;
		 }
		 
		 
		 return res;
	
	
	}
	
	
	
	function checkUseTestData(){
		if($("#idusetest").prop("checked")){
			disableform("pipeform",true);
		    document.getElementById("idfeaturefile").value= "/share/disk1/work/bioinformatics/tangbx/hic/testdata/test_feature.txt";
			document.getElementById("idmatrixfile").value= "/share/disk1/work/bioinformatics/tangbx/hic/testdata/test_matrix.txt";
		}else{
		 
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
				$("#idfmatrix").hide(); 
				$("#idffeature").hide(); 
				$("#idrowfeature").hide();
				$("#idrowmatrix").hide();
		}else{
			$("#idfmatrix").show(); 
		   $("#idffeature").show(); 
		   $("#idrowfeature").show();
		   $("#idrowmatrix").show();
		   
		}

	
	
}






</script>



</body>
</html>

