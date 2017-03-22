<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/menu.js"></script>
<title>Delta</title>
</head>


<body>
<div id="container">
	<jsp:include page="/inc/header.jsp" />
	<div id="content">
    	
    
		<div id="right-column1" style="margin-left:5px;width:250px;">
		  <div class="header_border">
				
			<div class="block"><img src="/circosweb/images/breadcrumb.gif" />How to use Delta</div>
			<ul>
			<li><a href="/circosweb/pages/help/help_pipeline.jsp#11">1.1 How to use pipeline?</a>
					<ul>
						<li><a href="/circosweb/pages/help/help_pipeline.jsp#111">1.1.1 Upload Matrix Data</a></li>
						<li><a href="/circosweb/pages/help/help_pipeline.jsp#112">1.1.2 General Parameters</a></li>
						<li><a href="/circosweb/pages/help/help_pipeline.jsp#113">1.1.3 Call TAD parameters</a></li>
						<li><a href="/circosweb/pages/help/help_pipeline.jsp#114">1.1.4 3D model parameters</a></li>
						<li><a href="/circosweb/pages/help/help_pipeline.jsp#115">1.1.5 Email</a></li>
					</ul>
				
				</li>
				<li><a href="/circosweb/pages/help/help_pipeline.jsp#12">1.2 The Description of Results</a>
					<ul>
						<li><a href="/circosweb/pages/help/help_pipeline.jsp#121">1.2.1 Job Status</a></li>
						<li><a href="/circosweb/pages/help/help_pipeline.jsp#122">1.2.2 The result of calling TAD</a></li>
						<li><a href="/circosweb/pages/help/help_pipeline.jsp#123">1.2.3 The result of calling peak</a></li>
						<li><a href="/circosweb/pages/help/help_pipeline.jsp#124">1.2.4 The result of 3d model</a></li>
					</ul>
				
				</li>
				
			</ul>	
				
		  </div>
	  </div>
	  
	  <div id="left-column1" style="margin-left:10px;width:980px;margin-top:10px;">
	  	<div class="header_border" style="padding-top:0px;margin-top:0px;">
			<div class="header" id="howto">How to use Delta?</div>
			<div class="header_content">
				<div>Delta is an integrated platform for Hi-C interaction data analysis and data visualization. Delta has three styles to visualize data online including Genome View, Topological View and Physical View. Furthermore, Delta houses public resources from ENSEMBL, ENCODE for Human and Mouse.</div>
				<p class="tracktitle">1. HiC Data Analyze Pipeline</p>
				<div>Hi-C data analyze pipeline allows users to upload own Hi-C data matrix to call TAD, interaction loop and predict 3D physical model. The results of pipeline can be directly loaded into Genome View, Topological View as well as Physical View. Delta uses <a href="http://compbio.cs.brown.edu/projects/tadtree/">TADtree</a> to call TAD, <a href="http://www.unc.edu/~yunmli/FastHiC/download.php">FastHic</a> to call interaction peak and  <a href="http://
www.fas.harvard.edu/,junliu/BACH/.">BACH</a> or MOGEN to predict 3D model. The whole procedure can be described as the following picture. 
				<br/>
				<br/>
				<img src="/circosweb/images/pipeline.jpg" />
				</div>
				<div id="11" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.1	How to use pipeline?</div>
				<div id="111" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.1.1	Upload Matrix Data</div>
				<div>
				  <p>Users  need to upload an observed interaction matrix M*N. Several rules need to follow  for matrix data:<br/>
1)The matrix data should only from one single  chromosome  <br/>
2)If the uploaded file has high resolution, we suggest  compute an interested region instead of the whole chromosomes.<br/>
<br/>
An  example of Interaction M*N Matrix data as follows:<br/>
11	197	175	154	140	147<br/>
197	11	210	138	124	98<br/>
175	210	33	348	143	110<br/>
154	138	348	44	176	171<br/>
140	124	143	176	55	448<br/>
147	98	110	171	448	66<br/>		
			  </p>
			  </div>
				<div id="112" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.1.2	General Parameters</div>
				<div>
				  <p>Users  need to fill several general parameters about the uploaded data, such as organism,  cell line, bin size and scope. All of these parameters will be used during the pipeline.<br/>
Bin size: the resolution size of Hi-C contact matrix file<br/>
Scope <br/>
Chromosome: the chromosome of Hi-C contact matrix file<br/>
Start Bin: the absolute bin index of start position, which can  be computed by chromsome position/binsize. The minimum value must be 1.<br/>
End Bin: End Bin: the absolute bin index of end position,  can be computed by chromosome position/binsize <br/>
				<img src="/circosweb/images/help/pipeline_param.png" />			  </p>
			  </div>
				<div id="113" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.1.3	Call TAD parameters</div>
				<div>
				Delta uses TADtree to call TAD. The needed parameters as follows:<br/>
				<img src="/circosweb/images/help/2.jpg" />
			  </div>
				<div id="114" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.1.4	3D model parameters</div>
				<div>
				Delta uses BACH or MOGEN to predict 3D physical  model.<br/>
				<br/>
				<p>The  required parameters and the default value for BACH are described as follows. Users need to set enzyme and reads length for the uploaded data to get an accurate model.<br/>
                  <img src="/circosweb/images/help/bach_parameter.jpg" />			  </p>
				  The required parameters and the default value for BACH are described as follows:<br/>
				  <img src="/circosweb/images/help/mogen_param.jpg" />	
			  </div>
				<div id="115" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.1.5	Email</div>
				<div>Delta needs users to fill email address to notify the results of pipeline. When users click “Submit”, an email will be sent to the filled email address.</div>
				<div id="12" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.2 The Description of Results</div>
				<div id="121" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.2.1 Job Status</div>
				<div>
				During the pipeline running procedure, the job status of each procedure will be updated immediately. When one procedure finished, the job status will be updated to finish.<br/>
				<img src="/circosweb/images/help/5.jpg" />
				
				</div>
				<div id="122" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.2.2 The result of calling TAD</div>
				<div>When calling TAD runs finished, the top 100 line of TAD result will be shown. Users can download the whole result file as well as view the result through Genome View.<br/>
				<img src="/circosweb/images/help/7.jpg" />
				</div>
				<div id="123" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.2.3 The result of calling peak</div>
				<div>
				When calling peak runs finished, the top 100 line of Peak result will be shown. Users can download the whole result file as well as view the result through Topological View.<br/>
				<img src="/circosweb/images/help/8.jpg" />
				</div>
				<div id="124" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">1.2.4 The result of 3d model</div>
				<div>
				When 3D model runs finished, the top 100 line of result will be shown. Users can download the whole result file as well as view the result through Physical View.<br/>
					<img src="/circosweb/images/help/9.jpg" />
				</div>	
				
		  </div>
		</div>
	  </div>
		
	</div>
	<jsp:include page="/inc/footer.jsp" />
</div>

<script type="text/javascript" language="javascript">
		showTabs('5');
</script>
</body>
</html>
