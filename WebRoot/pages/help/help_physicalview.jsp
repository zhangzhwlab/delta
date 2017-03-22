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
				
			<div class="block"><img src="/circosweb/images/breadcrumb.gif" />4.	Physical View for HiC Data</div>
			<ul>
			<li><a href="/circosweb/pages/help/help_physicalview.jsp#41">4.1	Add own track</a></li>
					<li><a href="/circosweb/pages/help/help_physicalview.jsp#42">4.2	Use public data source</a></li>
					<li><a href="/circosweb/pages/help/help_physicalview.jsp#43">4.3	Open Genome View</a></li>
					<li><a href="/circosweb/pages/help/help_physicalview.jsp#44">4.4	Change between Sphere and Line style</a></li>
					<li><a href="/circosweb/pages/help/help_physicalview.jsp#45">4.5 Jumping to Genome View and Topological View</a></li>
			</ul>	
				
		  </div>
	  </div>
	  
	  <div id="left-column1" style="margin-left:10px;width:980px;margin-top:10px;">
	  	<div class="header_border" style="padding-top:0px;margin-top:0px;">
	  	  <div class="header_content">
				<p class="tracktitle" id="physical">4.Physical View for HiC Data</p>
				<div>
				Delta uses 3D sphere-stick and line style to show 3D model. Physical view integrates Ensembl gene, ENCODE data to provide annotated information. A genome view is also parallel provided in the physical view interface. Users can zoom in or out, rotate, move in physical view. They can also upload own data into physical view.<br/><br/>
					<img src="/circosweb/images/help/3dmodel_3.png"  width="800" >
				</div>
				<div id="41" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">4.1	Add own track</div>
				<div>Users can upload file to add own track into current physical view. When clicking “Upload”, a web page will be shown like the following:<br/>
				<img src="/circosweb/images/help/physical_upload.png" /> <br/>
				The meaning of filled field describes as followings. <br/>
				<table cellspacing="0" class="table6">
					<tbody>
						<tr><th width="206">Field Name</th>
						<th width="762">Description</th>
						</tr>
						<tr><td>Dataset Name</td><td>A default dataset name will be generated. Users can modify it to an existed dataset name. </td></tr>
						<tr><td>Track Name</td><td>The name of track, which will be shown in the  web page</td>
						</tr>
						<tr><td>Organism</td><td>Currently only supports hg18,hg19 </td>
						</tr>
						<tr><td>Glyph Type</td><td>3dmodel: the track will be shown as a 3D sphere-stick structure.<br/>
Gene: the track will be shown as a colored surrounded circle.<br/>
Annotated feature: the track will be shown as a colored surrounded circle.<br/>
Interaction: the track will be shown as a dashed line<br/>
TAD: the track will be shown as colored shadows.<br/>


						</td></tr>
						<tr><td>File format</td><td><p>For  3dmodel, Delta supports the xyz and JSON format</p>
						  For other glyph types, Delta only supports GFF3 format.</td></tr>
					</tbody>
				</table>
				<div style="color:blue; font-weight:bold; padding-top:5px; padding-bottom:5px;">1) The 3D model file format</div>	
				
				<div>A custom xyz file contains x, y, z and position <br/>
4<br/>
xyz<br/>
C 0.00 0.00 0.00 1:1..1000000<br/>
C 0.00 1.00 0.00 1:1000000..2000000<br/>
C 0.00 1.00 1.00 1:2000000..3000000<br/>
C 0.00 0.00 1.00 1:3000000..4000000<br/>
A JSON file contains x, y, z and position<br/>
[{"name":"C1","chr":"1","start":1,"end":10000,"x":0.0000,"y":0.0000,"z":0.0000},{"name":"C2","chr":"1","start":10001,"end":20000,"x":0.2123,"y":0.0000,"z":0.0000},{"name":"C3","chr":"1","start":20001,"end":30000,"x":0.1291,"y":0.1493,"z":0.0000}]<br/>

				</div>
				<div style="color:blue; font-weight:bold; padding-top:5px; padding-bottom:5px;">2) The GFF3 format for Gene</div>
			
				<div>
				11	ensembl	protein_coding	4072500	4116681	.	+	.	ID=RRM1;Name=RRM1<br/>
11	ensembl	protein_coding	4219862	4220446	.	-	.	ID=AC018793.12-1;Name=AC018793.12-1<br/>
11	ensembl	protein_coding	4307671	4308255	.	+	.	ID=AC018793.12-2;Name=AC018793.12-2<br/>
11	ensembl	protein_coding	4345157	4346101	.	-	.	ID=OR52B4;Name=OR52B4<br/>

				</div>
				<div style="color:blue; font-weight:bold; padding-top:5px; padding-bottom:5px;">3) The GFF3 format for annotated feature</div>
				<div>
				11	encode	histone	4449556	4452889	1.7674419	.	.	ID=1336;Name=1336;Min=0.0;Max=37<br/>
11	encode	histone	4452889	4456222	2.409863	.	.	ID=1337;Name=1337;Min=0.0;Max=37<br/>
11	encode	histone	4456222	4459555	1.3539394	.	.	ID=1338;Name=1338;Min=0.0;Max=37<br/>
11	encode	histone	4459555	4462888	1.435	.	.	ID=1339;Name=1339;Min=0.0;Max=37<br/>
11	encode	histone	4462888	4466221	1.3724136	.	.	ID=1340;Name=1340;Min=0.0;Max=37<br/>


				</div>	
				
				<div style="color:blue; font-weight:bold; padding-top:5px; padding-bottom:5px;">4) The GFF3 format for Interaction </div>
				<div>
			11   hic  arc     4600000 5050000  .       .       .       ID=87;Name=87;Note=11:4600000-4650000|11:5000000-5050000<br/>
11      hic     arc     4600000 5100000 .       .       .   ID=88;Name=88;Note=11:4600000-4650000|11:5050000-5100000<br/>
11      hic     arc     4600000 5150000 .       .       .   ID=89;Name=89;Note=11:4600000-4650000|11:5100000-5150000<br/>
			</div>	
				
				<div style="color:blue; font-weight:bold; padding-top:5px; padding-bottom:5px;">5) The GFF3 format for TAD </div>
				<div>
			11	hic	tad	4500000	4650000	.	.	.	ID=1;Name=1;<br/>
11	hic	tad	5300000	5450000	.	.	ID=2;Name=2;<br/>
11	hic	tad	5450000	5650000	.	.	.	ID=3;Name=3;<br/>
11	hic	tad	6200000	6400000	.	.	.	ID=4;Name=4;<br/>

			</div>	
				
				</div>
				
				
				<div id="42" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">4.2	Use public data source</div>
				<div>Delta integrates public data resource from Encode, Ensembl for users to annotate the 3D model. 
For Ensembl Gene, uses can click the “human/Ensembl Gene” track, then fill the gene name in the text box or check “show genes all”. The colored rounded circle will be shown in the 3D model. Users can pin the given gene in the 3D model, then the zoom or rotate operation will focus on this gene.
.<br/>
  <img src="/circosweb/images/help/3dmodel_showgene.png"  /> <br/>

	<img src="/circosweb/images/help/Snap23.jpg" width="600px" height="600px" /><br/>
	For Encode, when users check track name, the colored rounded line will be shown in the 3D model. Multiple tracks can be checked. Delta has prepared different statistical scale for the Encode data. A check box for “Always redraw” will activate this function. When the zoom scale change among [144,146], (146,148], (148,150], different statistical scale will be activated.<br/>
		<img src="/circosweb/images/help/3dmodel_encode.png" /><br/>
	
</div>
				<div id="43" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">4.3	Open Genome View</div>
				<div>When checked “Show Genome View”, a genome view will be shown at the same time. All the operations in the Physical view can be synchronized to Genome View immediately, and the operations in the Genome view also can be synchronized to Physical View by click “Refresh physical view” button.<br/>
				Furthermore, click the sphere in the 3D model, a highlight yellow region will be shown in the Genome View.<br/>
				<img src="/circosweb/images/help/3dmodel_sphere.png"  /><br/>
				Use “shift” key together with the left mouse to click the interaction, a highlight green interaction track will be shown in the Genome View.<br/>
				<img src="/circosweb/images/help/3dmodel_interaction.png" /><br/>
				</div>
				<div id="44" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">4.4	Change between Sphere and Line style</div>
				<div>Physical view provides two styles for users to view 3D model. The first one is sphere, all the atom will be shown as sphere-stick style. The second one is line style, the 3D model will be shown as a line. Users can change between Sphere and Line style by clicking button “Sphere” and “Line”. When checking “Show Label”, the name of atom will be shown.<br/>
					<img src="/circosweb/images/help/Snap25.jpg" width="600px" height="600px" />
				</div>
				<div id="45" style="padding-top:10px; padding-bottom:10px; font-weight:bold;">4.5 Jumping to Genome View and Topological View</div>
				<div>Users can click the “Goto Genome” button to go to the Genome View. All the selected features in the Physical View will be shown in the Genome View.<br/>
User can click the “Goto Topological” button to go to the Topological View. All the selected features in the Physical View will be shown in the Topological View.

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
