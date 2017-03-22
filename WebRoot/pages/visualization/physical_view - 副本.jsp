<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/circosweb/css/jquery-ui.css" />
<link href="/circosweb/css/layout-default-latest.css" rel="stylesheet" type="text/css" />
<link href="/circosweb/css/jquery.ad-gallery.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" language="javascript">
var chrom=1;
var pos_start=1;
var pos_end=42000000;			

var bin_size=1000000;//1mb
var move_flag = false;
var mv_sx=-1;
var mv_sy=-1;
var mv_ex=-1;
var mv_ey=-1;

</script>
<script type="text/javascript" src="/circosweb/js/menu.js"></script>
<script type="text/javascript" src="/circosweb/js/jquery-1.9.1.js" ></script>
<script type="text/javascript" src="/circosweb/js/3Dmol.js"></script>
<!--
<script type="text/javascript" src="/circosweb/js/jquery.ad-gallery.js"></script>

<script type="text/javascript" src="/circosweb/js/zzsc.js"></script>  -->

<script type="text/javascript" src="/circosweb/js/jquery.layout-latest.js"></script>
<title>Physical view</title>

<style type="text/css">
 ul {
    list-style-image:url(/circosweb/images/list-style.gif);
  }
  pre {
    font-family: "Lucida Console", "Courier New", Verdana;
    border: 1px solid #CCC;
    background: #f2f2f2;
    padding: 10px;
  }
  code {
    font-family: "Lucida Console", "Courier New", Verdana;
    margin: 0;
    padding: 0;
  }

  #gallery {
  width:500px;
    padding: 30px;
    background: #ffffff;
  }
  #descriptions {
    position: relative;
    height: 50px;
    background: #EEE;
    margin-top: 10px;
    width: 640px;
    padding: 10px;
    overflow: hidden;
  }
    #descriptions .ad-image-description {
      position: absolute;
    }
      #descriptions .ad-image-description .ad-description-title {
        display: block;
      }
 .pane {
display:	none; /* will appear when layout inits */
}
</style>
<script type="text/javascript" language="javascript">
$(function(){
	$("#dmenu").load("/circosweb/pages/visualization/physical/detail.jsp");
	 $('#container').layout();
});
</script>
</head>

<body>
<jsp:include page="/inc/header.jsp" />
<div id="container" style="margin: 0 0 ;padding: 0 0 ;">

	<div class="pane ui-layout-center" style="height:900px;">
	<div id="content">

			<div class="header_border">
				<div class="header_content" style="position:relative; margin-left:0px;padding-left:0px;">
				
                    
                  		<div>Go to&nbsp;&nbsp;&nbsp;&nbsp;
					   <input type="text" name="position" id="curpos" value="chr22" style="width:200px;"/>&nbsp;&nbsp;in Dataset&nbsp;<select style="width:200px;" id="dset" name="dataset"><option value="GSE43070">Test Data</option><option>CID000002</option></select><input type="button" value="go" onclick="drawpic()"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="genome" onclick="GenomeView()"/>&nbsp;<input type="button" value="topology"/></div>
					   
					   <div>Bin size&nbsp;
					   <select id="idbin" style="width:200px;" onchange="ChangeBinSize()">
					   <option value="1000000">1Mb</option>
					   <option value="10000">10Kb</option>
					   <option value="1000">1Kb</option>>
					   </select>
					   </div>
					 <div style="padding-top:20px;">
						 <div class="header">1.Consensus 3D physical image(which illustrated by BACH) </div>
						 <div class="header_content">
						 	<table cellpadding="5" cellspacing="0">
								<tbody>
									<tr><td><input type="button" onclick="changeLineModel()" value="Line"></td><td><input type="button"onclick="changeSphareModel()" value="Sphere"></td></tr>
								</tbody>
							</table>
						</div>
						 
						 <div id="trackContainer" style="position: absolute;display: block; padding-right: 0px; height:800px;">
							 <div id="gldiv" style="width: 900px; height: 700px; margin: 0; padding: 0; border: 0;position:relative;z-index:1;"></div>
							  <div id="indicator" style="margin-top:-700px; padding:0; border:0; position:relative; z-index:2; background-color:#FF0000;opacity:0.4; display:none;">	
							 </div>
							
						 </div>
					 
						 <!--begin draw physical view-->
							
						 <!--finish draw physical view-->
					
						
					
					
					 
                     <div class="header_content" id="layerone" style="display:none;">   <img  src="/circosweb/images/wait.gif"/></div>
					 <div class="header_content" style="margin-left:0px;margin-top:750px;">
                         <div class="header">2.Ensemble 3D physical image(which illustrated by BACH-MIX)</div>
                          <div id="gallery" class="ad-gallery">
							  <div class="ad-image-wrapper">
									 <div id="gldiv1" style="width: 600px; height: 500px; margin: 0; padding: 0; border: 0;position:relative;"></div>
							  </div>
							  <div class="ad-controls">
							  </div>
							  <div class="ad-nav">
								<div class="ad-thumbs">
								  <ul class="ad-thumb-list">
									<li>
									  <a href="/circosweb/images/2.jpg" link="/circosweb/json/physical/GSE43070/BATCHMIX/p1.xyz">
										<img src="/circosweb/images/p1.png" width="90px" height="60px" title="BATCHMIX output1" alt="The data genereated by BACHMIX" class="image1">
									  </a>
									</li>
									<li>
									  <a href="/circosweb/images/3.jpg" link="/circosweb/json/physical/GSE43070/BATCHMIX/p1.xyz">
										<img src="/circosweb/images/p2.png" width="90px" height="60px"  title="BATCHMIX output 2" alt="The data genereated by BACHMIX" class="image2">
									  </a>
									</li>
								  </ul>
								</div>
							  </div>
							</div>
						
						  </div>
					 </div>
				 </div>
		  </div>		

        <div style="clear: both;"></div>
		
		<!--display menu-->
		<div id="dmenu" style="display:none;"></div>
		
	</div>
	
	
</div>
<div class="pane ui-layout-west">
		<div class="header_content" id="trackid">
			<p class="header">Track</p>
			<p><input id="gene_panel" type="checkbox" onclick="drawGeneTrack()" />Gene Density</p>	
			<p><input id="tad_panel" type="checkbox" onclick="drawTADTrack()" />TAD</p>	
         </div> 
	
	</div>

</div>
	<jsp:include page="/inc/footer.jsp" />
<script type="text/javascript" language="javascript">
var glviewer = null;
var cmodel= null; // consencus model
var view_model=-1;
var moldata = null;

var glviewer1 = null;

function testDrawLine(){



}


//this is used to change bin size
function ChangeBinSize(){
	var cur_bin = $("#idbin").val();

	if(cur_bin == 1000000){
		drawBACH("/circosweb/json/physical/GSE43070/batch.xyz");
	}else if(cur_bin  == 10000){
		drawBACH("/circosweb/json/physical/GSE43070/batch.xyz");
	}else if(cur_bin ==1000){
		drawBACH("/circosweb/json/physical/GSE43070/larger.xyz");
	}

}

function drawAno(){
$.get("/circosweb/json/physical/GSE43070/batch_ano.xyz",function(value){
			
			//set default value for position

			document.getElementById("gldiv").style.display="none";
			document.getElementById("gldivano").style.display="block";
		
				var xyzval = value;
				
				var data1 = xyzval;
				if(glviewer1 == null){
					glviewer1 = $3Dmol.createViewer("gldivano");
				}
				glviewer1.setBackgroundColor(0x000000);
		
				 m = glviewer1.addModel(data1, "xyz");
		
				glviewer1.setStyle({},{sphere:{radius:0.05,color:"white",clickable: true, callback: showJumpMenu},line:{linewidth:1,color:"white"}});
			
							
				glviewer1.render();
				glviewer1.zoomTo();
			
			});
	

}

//drawBACH("/circosweb/json/physical/GSE43070/test.txt");
//drawTrack();
	function drawBACH(path){
	
			document.getElementById("gldiv").style.display="block";
			document.getElementById("gldivano").style.display="none";
		if(glviewer!=null){
			glviewer.removeAllShapes();
			glviewer.removeModel(cmodel);
			glviewer.clear();
			glviewer.render();
			glviewer.zoomTo();

		}
		var position = chrom+":"+pos_start+".."+pos_end;
		$("#curpos").val(position);
		
		$.get(path,function(value){
			
			//set default value for position

		
		
		
				var xyzval = value;
				
				moldata = data = xyzval;
				if(glviewer == null){
					glviewer = $3Dmol.createViewer("gldiv");
				}
				view_model =2;
				glviewer.setBackgroundColor(0x000000);
		
				cmodel = m = glviewer.addModel(data, "xyz");
		
				glviewer.setStyle({},{sphere:{radius:0.05,color:"white",clickable: true, callback: showJumpMenu},line:{linewidth:1,color:"white"}});
			//	glviewer.setStyle({},{line:{linewidth:1,color:"white"}});//can not add click event
				//glviewer.setStyle({},{line:{}}); 
				//glviewer.mapAtomProperties($3Dmol.applyPartialCharges);
				/*var atoms = m.selectedAtoms({});
				var i=0;
				//alert(atoms.length);
				for(i = 0 ;i<atoms.length;i++){
					
					var atom1 = atoms[i];
					var start1 = new $3Dmol.Vector3(atom1.x,atom1.y,atom1.z);
					glviewer.addSphere({center:start1,radius:0.04, color:"white",clickable: true, callback: function(){
						showJumpMenu();
					
					}});
				}
				//alert("i="+i);
				glviewer.setStyle({});
				
				
				for ( i=1;i<atoms.length;i++) {
					var preatom = atoms[i-1];
					var atom1 = atoms[i];
					var start1 = new $3Dmol.Vector3(preatom.x,preatom.y,preatom.z);
					var end1 = new $3Dmol.Vector3(atom1.x,atom1.y,atom1.z);
					glviewer.addLine({start:start1,end:end1,color:0xffffff});
				}*/
							
				glviewer.render();
				glviewer.zoomTo();
			
			});
	
	
	}

	function drawpic(){
		var pos = $("#curpos").val();
		var dataset = $("#dset").val();
		var params = {"position":pos,"dataset":dataset};
		drawBACH("/circosweb/json/physical/GSE43070/batch.xyz");
	}
	
	//this is used to 	
	function withdrawGeneTrack(){
	
	
	}
	
	
	
	
	
	
	//this is used to draw gene density
	function drawGeneTrack(){		
		var tadcheck=$("#gene_panel");
		if(tadcheck.is(":checked")){
			//here, we need to represent the value as different coclor in the physical model
			//first, we need to load the value, may be float value 1,2,3,4,5,6,7 for each atoms 
			
			$.get("/circosweb/json/physical/GSE43070/gene.txt",function(value){
				if(value != null ){
					var arry= value.split(",");
					for(var i=0;i<arry.length;i++){
						//then change the value to heatmap color
						var cr = getHeatmapColor(arry[i]);
						glviewer.setStyle({serial:i}, {sphere:{radius:0.04,color:cr},line:{linewidth:1,color:"white"}});						
						glviewer.render();
					}
				}
			});
		}
	}
	
	
	//this is used to draw tad tarck
	function drawTADTrack(){
		//glviewer.removeAllShapes();
		var tadtrack=$("#tad_panel");
		if(tadtrack.is(":checked")){
			$.get("/circosweb/json/physical/GSE43070/tad.txt",function(value){
				if(value != null){
						var arry = value.split("|");
						for(i=0;i<arry.length;i++){
							var tad_domain = arry[i].split(";"); // on color for each one
							
							var paint_color = getColor(i);
							for(j=0;j<tad_domain.length;j++){
								var tad_elem = tad_domain[j].split(",");
								
								for(k=0;k<tad_elem.length;k++){
									
									var ai= tad_elem[k];
									var ai1=null;
									if(k<tad_elem.length-1){
										ai1= tad_elem[k+1];
									}
									
									if(view_model == 2){ //sphare
										glviewer.addStyle({serial:ai}, {sphere:{radius:0.05,color:paint_color}});
									}else{ //line
										var atom1_arry = cmodel.selectedAtoms({serial:ai});
										var atom1= null;
										if(atom1_arry.length>0){
											atom1 = atom1_arry[0];
										}
										var atom2= null;
										if(ai1 != null){
											var atom2_arry= cmodel.selectedAtoms({serial:ai1});
											if(atom2_arry.length >0 ){
												atom2 = atom2_arry[0];
											}
											var start1 = new $3Dmol.Vector3(atom1.x,atom1.y,atom1.z);
											var end1 = new $3Dmol.Vector3(atom2.x,atom2.y,atom2.z);
											console.log("line model "+atom1.x+","+atom1.y+","+atom1.z+";"+atom2.x+","+atom2.y+","+atom2.z+" "+paint_color);
											glviewer.addLine({start:start1,end:end1,color:paint_color});
											
										}	
										//
										
										
																				
									}
								}
								
								glviewer.render();
							
							}	
						
						}
				}
			});	
		}
	}
	
	
	
	function getColor(index){
		var colors=["red","yellow","blue","orange","green","purple"];
		
		return colors[index];
	
	}
	
	//show a menu of chrom position
	//position: chr1:1...120000
	//link: jbrowse/physical
	//we will use this.x this.y this.z to link to the source position of this atom
	function showJumpMenu(){
		//get the position of the seletcted item
		console.log("jump menu="+this.x+","+this.y+","+this.z);
		
		//then show a div
			
	
	}


	function showAddCube(){
		
		
	
	}

	
	function getHeatmapColor(quality){
	
	
			var colorbrewer = {
				YlOrRd: {
					3: ["#ffeda0","#feb24c","#f03b20"],
					4: ["#ffffb2","#fecc5c","#fd8d3c","#e31a1c"],
					5: ["#ffffb2","#fecc5c","#fd8d3c","#f03b20","#bd0026"],
					6: ["#ffffb2","#fed976","#feb24c","#fd8d3c","#f03b20","#bd0026"],
					7: ["#ffffb2","#fed976","#feb24c","#fd8d3c","#fc4e2a","#e31a1c","#b10026"],
					8: ["#ffffcc","#ffeda0","#fed976","#feb24c","#fd8d3c","#fc4e2a","#e31a1c","#b10026"],
					9: ["0xffffcc","0xffeda0","0xfed976","0xfeb24c","0xfd8d3c","0xfc4e2a","0xe31a1c","0xbd0026","0x800026"]
				}
	   		};
	
			var color_range = colorbrewer.YlOrRd[9];
			var min_val = 0;
			var max_val = 1;
			var qual_score =  quality;
			var tc = parseInt((qual_score-min_val)*8/(max_val-min_val));
			var tc_color = color_range[tc];
			return tc_color;
	}
	
	
	
	//this is used to draw the 3d position as sphare
	function changeSphareModel(){
				view_model=2;
				glviewer.removeAllShapes();
				glviewer.render();
				
				
				glviewer.setStyle({},{sphere:{radius:0.05,color:"white",clickable: true, callback: showJumpMenu},line:{linewidth:0.5,color:"white"}});
				glviewer.render();
	}
	
	
	
	//this is used to draw the 3d position as line and color
	function changeLineModel(){
		glviewer.removeAllShapes();
		glviewer.render();
		view_model=1;
		/*var atoms = cmodel.selectedAtoms({});
		for ( i=1;i<atoms.length;i++) {
						var preatom = atoms[i-1];
						var atom1 = atoms[i];
						var start1 = new $3Dmol.Vector3(preatom.x,preatom.y,preatom.z);
						var end1 = new $3Dmol.Vector3(atom1.x,atom1.y,atom1.z);
						glviewer.addLine({start:start1,end:end1,color:"red",linewidth:3,clickable:true,callback: function(){
						showJumpMenu();
						}});
				}*/	
			glviewer.setStyle({},{line:{linewidth:1,color:"white"}});
			
		   // glviewer.setStyle({},{line:{linewidth:1,color:"blue"}}); 
			glviewer.render();
		}
		
	
	

	
	showTabs('3');
</script>

</body>
</html>

