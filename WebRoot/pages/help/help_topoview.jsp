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
				
			<div class="block"><img src="/circosweb/images/breadcrumb.gif" />3.	Topological View for Hi-C Data</div>
			<ul>
					<li><a href="/circosweb/pages/help/help_topoview.jsp#31">3.1 Add own track</a></li>
					<li><a href="/circosweb/pages/help/help_topoview.jsp#32">3.2 Zoom in /out and change view scope</a></li>
					<li><a href="/circosweb/pages/help/help_topoview.jsp#33">3.3 Choose multiple chromosomes</a></li>
				
					<li><a href="/circosweb/pages/help/help_topoview.jsp#35">3.4 Jumping to Genome View and Physical View</a></li>
			</ul>	
				
		  </div>
	  </div>
	  
	  <div id="left-column1" style="margin-left:10px;width:980px;margin-top:10px;">
	  	<div class="header_border" style="padding-top:0px;margin-top:0px;">
	  	  <div class="header_content">
				<p class="tracktitle" id="circlet">3. Topological   View for Hi-C Data</p>
				<div>Delta uses a Circos-like style to show Hi-C  interaction and other omcis data. Topological view integrates the Ensembl Gene  as well as the ENCODE data. At the same time, Topological view also enable users  to upload own annotated data. Users can zoom in or out, change the view region  and export the whole page.<br/>
				  <br/>
					<img src="/circosweb/images/help/topo.png"  width="800" height="600" >
			</div>
				<div style="padding-top:10px; padding-bottom:10px; font-weight:bold;" id="31">3.1 Add own track</div>
				<div>Users can add own annotated data into Topological View by clicking “Upload” button. A web page like the following will be shown.<br/>
				<img src="/circosweb/images/help/10.jpg"  > <br/>
				<p>The  meaning of each field describes as follows.<br/>
				</p>
				<table cellspacing="0" class="table6">
					<tbody>
						<tr><th width="187">Field Name</th>
						<th width="781">Description</th>
						</tr>
						<tr><td>Dataset Name</td><td>A default dataset name will be generated. Users can modify it to an existed dataset name. </td></tr>
						<tr><td>Track Name</td><td>The name of track, which will be shown in the web  page</td>
						</tr>
						<tr><td>Organism</td><td>Currently only support hg18,hg19</td>
						</tr>
						<tr><td>Glyph Type</td>
						<td>Interaction: the track will be shown as an arc in the inner circle.<br />
						  Histogram: the track will be shown as a histogram line which used for the statistical data. <br/>
						  Gene: the track will be show as layered  gene.<br/>						</td></tr>
						<tr><td>File Format</td><td>Currently only GFF3 format supported</td></tr>
						
					</tbody>
				</table>
				<div style="color:blue; font-weight:bold; padding-top:5px; padding-bottom:5px;">1) The GFF3 format for Interaction Track</div>	
				<div class="note">An example of interaction loop file is as follows. The anchor and the target information is included in Note attribute and separated by "|".</div>
				<div>11	Hi-C	arc	4600000	5050000	.	.	.	ID=1;Name=1;Note=11:4600000-4650000|11:5000000-5050000<br/>
					11	Hi-C	arc	4600000	5100000	.	.	.	ID=2;Name=2;Note=11:4600000-4650000|11:5050000-5100000<br/>
					11	Hi-C	arc	4600000	5150000	.	.	.	ID=3;Name=3;Note=11:4600000-4650000|11:5100000-5150000<br/>
					11	Hi-C	arc	4600000	5200000	.	.	.	ID=4;Name=4;Note=11:4600000-4650000|11:5150000-5200000<br/>
				</div>
				<div style="color:blue; font-weight:bold; padding-top:5px; padding-bottom:5px;">2) The GFF3 format for Histogram Track</div>
				<div class="note">The score field in the 6 column must be filled.</div>	
				<div>
				11	encode	histone	4449556	4452889	1.7674419	.	.	ID=1336;Name=1336;<br/>
				11	encode	histone	4452889	4456222	2.409863	.	.	ID=1337;Name=1337;<br/>
				11	encode	histone	4456222	4459555	1.3539394	.	.	ID=1338;Name=1338;<br/>
				11	encode	histone	4459555	4462888	1.435	.	.	ID=1339;Name=1339;<br/>
				</div>
				<div style="color:blue; font-weight:bold; padding-top:5px; padding-bottom:5px;">3) The GFF3 format for Gene Track</div>
				<div>
				11	ensembl	protein_coding	4072500	4116681	.	+	.	ID=RRM1;Name=RRM1<br/>
				11	ensembl	protein_coding	4219862	4220446	.	-	.	ID=AC018793.12-1;Name=AC018793.12-1<br/>
				11	ensembl	protein_coding	4307671	4308255	.	+	.	ID=AC018793.12-2;Name=AC018793.12-2<br/>
				11	ensembl	protein_coding	4345157	4346101	.	-	.	ID=OR52B4;Name=OR52B4<br/>

				</div>
				</div>
				<div style="padding-top:10px; padding-bottom:10px; font-weight:bold;" id="32">3.2 Zoom in /out and change view scope</div>
				<div>Users can change view scope through three ways. First, users can move the mouse along linear chromosome ideogram. Second, users can click “zoom in” and “zoom out ” button. Third, users can change view scope along the Topological ideogram.<br/>
				<img src="/circosweb/images/help/Snap11.jpg" /><br/>
				<img src="/circosweb/images/help/Snap10.jpg" /><br/>
				<img src="/circosweb/images/help/Snap14.jpg" />
				</div>
				<div style="padding-top:10px; padding-bottom:10px; font-weight:bold;" id="33">3.3 Choose multiple chromosomes</div>
				<div>Users can click icon to open a web page for choosing multiple chromosomes. A circlet view for multiple chromosomes will be shown.<br/>
				<img src="/circosweb/images/help/Snap16.jpg" /> <img src="/circosweb/images/help/Snap18.jpg" />
				
				</div>
				
				<div style="padding-top:10px; padding-bottom:10px; font-weight:bold;" id="35">3.4 Jumping to Genome View and Physical View</div>
				<div>Users can click the “Goto Genome” button to go to the Genome View. All the selected features in the Topological View will be shown in the Genome View. <br/>
User can click the “Goto Physical” button to go to the Physical View. If users have selected the Hi-C dataset, a dialog with the supported 3D physical model will be shown as follows. All the selected features in the Topological View will be shown in the Physical View.
<br/>
				<img src="/circosweb/images/help/topo_3dmodel.png" />
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
