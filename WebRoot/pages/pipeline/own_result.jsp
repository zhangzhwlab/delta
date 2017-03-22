<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<script type="text/javascript" src="/circosweb/js/jquery-1.9.1.js"></script>
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/menu.js"></script>
<title>HiC Visual Pipeline</title>
</head>

<body>
<div id="container">
	<jsp:include page="/inc/header.jsp" />
	 <div style="clear: both;"></div>
	<div id="content">
	  <div id="left-column1" style="margin-left:5px; width:70%;">
			<div class="header_border">
				<div class="header_content">
					<div class="header">JobID <s:property value="jobid"/> </div>
					
					<div class="header"><a target="_blank" href="/jbrowse/index.html?data=<s:property value='jobid'/>&loc=<s:property value='pipeBean.chrom'/>%3A<s:property value='pipeBean.chromStart'/>..<s:property value='pipeBean.chromEnd'/>&tracks=matrix,TAD">Genome View</a></div>
				   <div class="header"><a target="_blank" href="/circosweb/pages/visualization/topo_viewm.jsp?conf=<s:property value='jobid'/>&loc=<s:property value='pipeBean.chrom'/>%3A<s:property value='pipeBean.chromStart'/>..<s:property value='pipeBean.chromEnd'/>&tracks=Interaction">Topological View</a></div>
					
					<div class="header"><a target="_blank" href="/circosweb/pages/visualization/physical_view.jsp?conf=<s:property value='jobid'/>&loc=<s:property value='pipeBean.chrom'/>%3A<s:property value='pipeBean.chromStart'/>..<s:property value='pipeBean.chromEnd'/>&tracks=3dmodel">Physical View</a></div>
					
				</div>

		  </div>		
		</div>
		<div id="right-column1" style="margin-left:20px; width:25%; border:1px solid #eeeeee;">
			
			<div class="header">Setted Parameters</div>
			<div class="header_content">
				<table>
					<tbody>
						<tr><td><strong>organism</strong></td><td>
						<s:if test="pipeBean.organism == 'hsa'">
							Human
						</s:if>
						<s:else>
						   Mouse
						</s:else>
						</td></tr>
						<tr><td><strong>cell line</strong></td><td><s:property value="pipeBean.cellline"/></td></tr>
						<tr><td><strong>bin size</strong></td><td><s:property value="pipeBean.binsize"/></td></tr>
						<tr><td><strong>chromsome</strong></td><td><s:property value="pipeBean.chrom"/></td></tr>
						
					</tbody>
				
				</table>
			
		</div>
		<div class="header">Reference</div>
		<div class="header_content">
			<table>
				<tbody>
					<tr><td>1. Weinreb C, Raphael BJ. Identification of hierarchical chromatin domains. Bioinformatics.2016 Jun 1;32(11):1601-9.</td></tr>
					<tr><td>2. Hu M, Deng K, Qin Z, Dixon J, Selvaraj S, Fang J, Ren B, Liu JS. Bayesian Inference of Spatial Organizations of Chromosomes. PLoS Comput Biol. 2013;9(1):e1002893.</td></tr>
					<tr><td>3.Xu Z, Zhang G, Wu C, Li Y, Hu M. FastHiC: a fast and accurate algorithm to detect long-range chromosomal interactions from Hi-C data. Bioinformatics.2016 May 3.</td></tr>
					<tr><td>4.Buels R, Yao E, Diesh CM, Hayes RD, Munoz-Torres M, Helt G, Goodstein DM3,4, Elsik CG, Lewis SE, Stein L, Holmes IH. JBrowse: a dynamic web platform for genome visualization and analysis. Genome Biol. 2016 Apr 12;17(1):66.</td></tr>
				</tbody>
			</table>
		</div>

		</div>
		
		
        <div style="clear: both;"></div>
		
	</div> <!--end content-->
	<jsp:include page="/inc/footer.jsp" />

</div>

<script language="javascript" type="text/javascript">
	showTabs('6');
	var tadflag =0;
	var peakflag =0 ;
	var bachflag =0 ;
	var hours=[0,0,0];
	var minutes=[0,0,0];
	
	var seconds=[0,0,0];
	
	var tadt=null;
	var peakt=null;
	var bacht=null;
	
	
	
	var curjobid="<s:property value='#parameters.jobid'/>";
		var binsize="<s:property value='pipeBean.binsize'/>";
		var chrom="<s:property value='pipeBean.chrom'/>";
		var organism="<s:property value='pipeBean.organism'/>";
	
	var submittime = new Date().getTime();
	updateResult();
	if(tadflag != 3){
		startTime(1);
	}
	if(peakflag !=3){
		startTime(2);
	}
	if(bachflag !=3){
		startTime(3);
	}
	
	if(tadflag != 3 || peakflag != 3 || bachflag !=3){
		window.setInterval("updateResult()",60000);
			
	}

	
	
	function updateResult(){
		// go for each module
		var url="/ajax/ajaxTADresult.action";
		
		var params={'jobid':curjobid};
		if(tadflag != 3){
		   
			$.ajax({
					url: url+"?time="+new Date().getTime(),
					type:'post',
					dataType:'json',
					data:params,
					success:function(value){
						tadflag = value.resflag;
						if(value.resflag == 1){
							$("#tadstatusid").text= "Start";
							 
						
						}else if(value.resflag ==2){
							$("#tadstatusid").text= "Run";
							
						
						}else if(value.resflag ==3){							
							$("#tadstatusid").text= "Finish";
							if(tadt != null){
								stopTime(1);
							}
							document.getElementById("tadtimeid").innerHTML= value.endTime;
						}
					    var turl = "/pipeline/showTAD.action?jobid="+curjobid;
						$("#tadframeid").attr("src",turl);
					},
					error:function(e){
					  alert("get tad result error");
					}
			   });
		
		}
		
			   
		//peak
		if(peakflag != 3){
		
			url="/ajax/ajaxPeakresult.action";
			$.ajax({
						url: url+"?time="+new Date().getTime(),
						type:'post',
						dataType:'json',
						data:params,
						success:function(value){
							peakflag = value.resflag;
							if(value.resflag == 1){
								$("#peakstatusid").text= "Start";
								
							
							}else if(value.resflag ==2){
								$("#peakstatusid").text= "Run";
								
							
							}else if(value.resflag ==3){							
								$("#peakstatusid").text= "Finish";
								if(peakt != null){
									stopTime(2);
								}
								
								document.getElementById("peaktimeid").innerHTML= value.endTime;
							}
							var turl = "/pipeline/showPeak.action?jobid="+curjobid+"&pipeBean.binsize="+binsize+"&pipeBean.chrom="+chrom+"&pipeBean.organism="+organism;
							$("#peakframeid").attr("src",turl);
						},
						error:function(e){
						  alert("get peak result error");
						}
				   });
		
		}
		
		
		
		
		//bach
		if(bachflag !=3 ){

		 url="/ajax/ajaxBachresult.action";
		$.ajax({
					url: url+"?time="+new Date().getTime(),
					type:'post',
					dataType:'json',
					data:params,
					success:function(value){
						bachflag = value.resflag;
						if(value.resflag == 1){
							$("#modelstatusid").text= "Start";
							
						
						}else if(value.resflag ==2){
							$("#modelstatusid").text= "Run";
							
						
						}else if(value.resflag ==3){							
							$("#modelstatusid").text= "Finish";
							if(bacht != null){
								stopTime(3);
							}
							
							document.getElementById("3dmodeltimeid").innerHTML= value.endTime;
						}
					    var turl ="/pipeline/showBACH.action?jobid="+curjobid+"&pipeBean.organism="+organism+"&pipeBean.chrom="+chrom;
						$("#bachframeid").attr("src",turl);
					},
					error:function(e){
					  alert("get bach result error");
					}
			   });
		
		}
			   
	}
	
	

	
	function startTime(flag){ 
		 
	  seconds[flag-1]++; 
	  if(seconds[flag-1]>=60){ 
		seconds[flag-1] = 0; 
		minutes[flag-1]++; 
	  } 
	  if(minutes[flag-1]>=60){ 
		minutes[flag-1] = 0; 
		hours[flag-1]++; 
	  }

	  if(flag == 1){
	  	 document.getElementById("tadtimeid").innerHTML=j(hours[flag-1])+":"+j(minutes[flag-1])+":"+j(seconds[flag-1]); 
	 	 tadt=setTimeout("startTime("+flag+")", 1000); 
	  }else if(flag ==2){
	  	 document.getElementById("peaktimeid").innerHTML=j(hours[flag-1])+":"+j(minutes[flag-1])+":"+j(seconds[flag-1]); 
	 	 peakt=setTimeout("startTime("+flag+")", 1000); 
	  }else if(flag ==3){
	     document.getElementById("3dmodeltimeid").innerHTML=j(hours[flag-1])+":"+j(minutes[flag-1])+":"+j(seconds[flag-1]); 
	 	 bacht=setTimeout("startTime("+flag+")", 1000); 
	  } 
	  
	} 
	
	function stopTime(flag){
		if(flag == 1){
			window.clearTimeout(tadt);
		}else if(flag ==2){
			window.clearTimeout(peakt);
		
		}else if(flag ==3){
			window.clearTimeout(bacht);
		}
			
	}
	
	
	
	
	
	 function j(arg){ 
	  return arg>=10 ? arg : "0" + arg; 
	} 
	
	
	function bookmarksite(title, url) {
		if (window.sidebar) // firefox
			window.sidebar.addPanel(title, url, "");
		else if (window.opera && window.print) { // opera
			var elem = document.createElement('a');
			elem.setAttribute('href', url);
			elem.setAttribute('title', title);
			elem.setAttribute('rel', 'sidebar');
			elem.click();
		} else if (document.all)// ie
			window.external.AddFavorite(url, title);
		else {
			alert("click \"ctrl+D\" to add this URL to your favorites list.");
		}
	}
	
</script>



</body>
</html>

