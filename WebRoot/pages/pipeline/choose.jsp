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
	<div id="content" style="height:1060px;">
		 <div class="header_content">Delta tool supports two manners to upload your custom data and visualize the results. One is to upload annotated data and directly visualize the result, the other is to upload the Hi-C matrix data and use the pipeline to analysis the data and then visualize the results online.</div>
		 <div class="header">1.Upload your custom data<input type="button" value="GO"  onclick="window.location.href='useown.jsp'"/></div>
		<div class="header_content">
			Upload any files in given format regarding the interaction loop, TAD and 3D model for visualization. The required formats are described below.
		</div>
		<div class="header">2.Use the pipeline <input type="button" value="GO"  onclick="window.location.href='pipeline.jsp'"/></div>
		<div class="header_content">
			Delta tool applies <a href="http://compbio.cs.brown.edu/projects/tadtree/" target="_blank">TADtree</a>, <a href="http://www.unc.edu/~yunmli/FastHiC/tutorial.php" target="_blank">FastHiC</a> interaction loop and TAD. It constructs 3D model based on the uploaded interaction matrix file by using <a href="http://www.people.fas.harvard.edu/~junliu/BACH/" target="_blank">BACH</a> or <a href="http://calla.rnet.missouri.edu/mogen/" target="_blank">MOGEN</a>.	
		</div>
		
		<div class="header_content" style="padding-top:20px;">
			<div id="matrixfile" style="padding-top:5px;padding-bottom:5px;">	<strong>1)The supported Hi-C matrix file format </strong> </div>
			<div>
				An example of Hi-C matrix file is as follows.<br/><br/>
				11	197	175	154	140	147<br/>
				197	11	210	138	124	98<br/>
				175	210	33	348	143	110<br/>
				154	138	348	44	176	171<br/>
				140	124	143	176	55	448<br/>
				147	98	110	171	448	66<br/>
			</div>
			
			
		   <div id="loopfile" style="padding-top:5px;padding-bottom:5px;">	<strong>2)The supported interaction loop file format: GFF3. </strong> </div>
			<div>
			An example of interaction loop file is as follows. The anchor and the target information is included in Note attribute and separated by "|".<br/><br/>
			
			11      hic     arc     4600000 5050000 .       .       .       ID=87;Name=87;Note=11:4600000-4650000|11:5000000-5050000<br/>
			11      hic     arc     4600000 5100000 .       .       .       ID=88;Name=88;Note=11:4600000-4650000|11:5050000-5100000<br/>
			11      hic     arc     4600000 5150000 .       .       .       ID=89;Name=89;Note=11:4600000-4650000|11:5100000-5150000<br/>		
			</div>
			<div id="tadfile" style="padding-top:5px;padding-bottom:5px;"><strong>3)The supported TAD file format: GFF3. </strong> </div>
			<div>
			An example of TAD GFF3 file is as follows. <br/><br/>
			
			11      hic     tad     4500000 4650000 .       .       .       ID=1;Name=1 <br/>
			11      hic     tad     5300000 5450000 .       .       .       ID=2;Name=2 <br/>
			11      hic     tad     5450000 5650000 .       .       .       ID=3;Name=3 <br/>
			11      hic     tad     6200000 6400000 .       .       .       ID=4;Name=4 <br/>
			</div>
			
			<div id="3dfile" style="padding-top:5px;padding-bottom:5px;"><strong>4)The supported 3D model file format: XYZ, JSON. </strong> </div>
			<div>
			An example of 3D model XYZ file is as follows. <br/><br/>
		5 <br/>
		BACH OUTPUT<br/>
		C 0.0000 0.0000 0.0000 11:4500000..4550000<br/>
		C 0.1056 0.0000 0.0000 11:4550000..4600000<br/>
		C 0.2047 0.0229 0.0000 11:4600000..4650000<br/>
		C 0.2698 0.1251 0.0544 11:4650000..4700000<br/>
		C 0.2780 0.1991 0.1230 11:4700000..4750000<br/>
		<br/>
		Line 1 is the atom numbers. In this case, it is 5,<br/>
		Line 2 is a description information. In this case, it is BACH OUTPUT <br/>
		Line 3-7 is the position of atoms. Each atom starts with Carbon element, and follows the X,Y,Z position. The last field is the genome position of this atom.
		
						
			</div>
			
			
			
		</div>
		
	 	<div style="clear: both;"></div>
	</div>
	<jsp:include page="/inc/footer.jsp" />
</div>
</body>
<script type="text/javascript" language="javascript">
	showTabs('6');
//here ,we use a dialog to trigger the different action
function loadPage(){
	$('<div></div>').appendTo('body')
				 .html('<div style=\"margin-top:10px;font-size:12px; align:center;\"><p style=\"margin-top:20px;margin-bottom:20px;padding-top:10px;padding-bottom:10px;font-size:16px; background-color: gray; width:150px; text-align:center;\"><a href=\"useown.jsp\">Upload own data</a></p><p style=\"margin-top:20px;margin-bottom:20px;padding-top:10px;padding-bottom:10px;font-size:16px; background-color: gray; width:150px;  text-align:center;\"><a href=\"pipeline.jsp\">Use the pipeline</a></p></div>')
				 .dialog({
				 modal: true,title:'Choose from the panel', zIndex:200 , autoOpen: true,
					width: '400',height: '400' , 
					resizable: false,						 
					close: function (event, ui) {
						$(this).remove();						
					}
				});

}

</script>

</html>

