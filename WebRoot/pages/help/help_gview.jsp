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
				
			<div class="block"><img src="/circosweb/images/breadcrumb.gif" />2.	Genome View for Hi-C Data </div>
			<ul>
			
				<li><a href="/circosweb/pages/help/help_gview.jsp#21">2.1	Add own track</a></li>
				<li><a href="/circosweb/pages/help/help_gview.jsp#22">2.2	Jumping to Circlet View and Physical View</a></li>
			</ul>	
				
		  </div>
	  </div>
	  
	  <div id="left-column1" style="margin-left:10px;width:980px;margin-top:10px;">
	  	<div class="header_border" style="padding-top:0px;margin-top:0px;">
			<div class="header" id="howto">How to use Delta?</div>
			<div class="header_content">
				<p class="tracktitle" id="genome">2. Genome View for Hi-C Data</p>
				<div>Delta uses JBrowse to show Hi-C features and other omics data. Delta integrates Ensembl Gene, ENCODE data such as ChipSeq, DNase Seq. At the same time, Delta also provides Hi-C features such as Matrix, TAD and interaction peak from public papers such as GSE63525,GSE35156 and GSE18199.<br/><br/>
				Delta uses different glyphs to represent different features. It represents Matrix as a triangle heatmap, TAD as a rectangle as well as interaction peak as an arc line. Users can pan left or right, zoom in or out, toggle tracks on or off in the Genome View. And they can also export the whole web page as a high resolution picture.<br/><br/>
				<img src="/circosweb/images/help/genome.jpg" width="800" height="600" />
				
			  </div>
			   <div id="21" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">
			   
			     <p class="tracktitle" id="genome">2.1	Add own track</p>
			   Users can click the “Track”->”Open track file or URL” menu to upload own data and a dialog as follows will be shown. After upload the file, users need to set the file format from “Files and URLS” and set the display glyph from “New Tracks”. <br/>
			   		<img src="/circosweb/images/help/genome_upload.png"  />
			   
			   </div>
			   
			  <div id="22" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">
			    <p class="tracktitle" id="genome">2.2	Jumping to Circlet View and Physical View</p>
			  Users can click the “Goto Topological” button to go to the Topological View. All the selected features in the Genome View will be shown in the Topological View.<br/>
User can click the “Goto Physical” button to go to the Physical View. If users have selected the Hi-C dataset, a dialog with the supported 3D physical model will be shown as follows. All the selected features in the Genome View will be shown in the Physical View.<br/>
	<img src="/circosweb/images/help/genome_3dmodel.jpg"  />
			   
			  
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
