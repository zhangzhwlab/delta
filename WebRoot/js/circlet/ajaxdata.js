
function ajax_getbandlst(ideogram,curchr){
  //  alert("ajax_getbandlst");
    bandlstmap={};
	$.get(ideogram+"/"+curchr+".txt",function(result){
		  var t_bandlst= eval ("(" + result + ")");		 
		 bandlstmap[curchr] = t_bandlst;
		 if(t_bandlst != null){
			 draw_genome();
			 showlayout();			 
		 }
		
	});
}	

//get all chromosome ideogram band
function ajax_get_allchr_bandlst(ideogram,regionlst){
  //  alert("ajax_getbandlst");
   bandlstmap={};
 
   for(i=0;i<regionlst.length;i++){
		var  t_region_chr = regionlst[i].chr;	
		$.ajax({
					url:ideogram+"/"+t_region_chr+".txt",
					type:'get',
					async:false,
					success:function(result){
						 var t_bandlst= eval ("(" + result + ")");	
						  bandlstmap[t_region_chr] = t_bandlst;
					},
					error:function(e){
					  alert("get ideogram band error");
					}
			   });
    }

		showlayout();			 
  	
}

//this used to get interation data and draw
//modify ajax data url to get interaction data
function ajax_getInteractiondata(trackobj){

	var t_toomany = trackobj.toomany;
	if(t_toomany ==0 ){
		t_toomany = config.toomany_threshold;
	}
	
		var canvas1 = document.getElementById("canvas_"+trackobj.key);
		var ctx1 = canvas1.getContext('2d');	
		/*ctx1.clearRect(0,0,1024, 1024);
		if(region_lst.length == 1){
			
			var centerx=parseInt(canvas.width/2);
			var centery=parseInt(canvas.height/2);
			
			var imageObj = new Image();
			var imageleft = centerx ;
			var imagetop =  centery;
			
			imageObj.onload= function() {
				ctx1.drawImage(imageObj,imageleft,imagetop);
			}
			imageObj.src="http://"+window.location.host+"/circosweb/images/loader.gif";
		}*/
		
		var ajaxurl = "";
			if(trackobj.storeclass == "GFF3" || trackobj.storeclass=="gff3"){
				ajaxurl="/circosweb/ajax/getChromatinInterData.action";
			}else if(trackobj.storeclass == "mysql"){
				ajaxurl="/circosweb/ajax/getChromatinInterDataFromTable.action";
			}
			
			
	
	
	for(i=0;i<region_lst.length;i++){
		    var q_chr = region_lst[i].chr;
			var q_start = region_lst[i].start;
			var q_end = region_lst[i].end;
			
			var params;
			var querylen = parseInt(q_end) - parseInt(q_start);
			var flag = 1;
			
			//console.log("t_toomany="+t_toomany);
			if(querylen > t_toomany && trackobj.statisFile != null &&  trackobj.statisFile.length >0 ){
				params={"chrom":q_chr,"start":q_start,"stop":q_end,"storageFile":trackobj.statisFile};
				flag =2;
			}else{
				var storagefile = trackobj.storage;
				storagefile = storagefile.replace("{refseq}",q_chr);
				params={"chrom":q_chr,"start":q_start,"stop":q_end,"storageFile":storagefile,"param":trackobj.table,"binsize":trackobj.binsize};
			}
			
			
			
			//alert("get interaction data");
			$.ajax({
					url: ajaxurl,
					type:'post',
					dataType:'json',
					data:params,
					success:function(value){
						interacionData=value.data;
						if(interacionData!=null || interacionData!=undefined ){
							drawArc(interacionData,trackobj,flag,i);
							var track_radius = radius;
							var left_margin = canvas_width/2+track_radius;
							$("#label_"+trackobj.key).css("left",left_margin);
							
						}
						
					},
					error:function(e){
					  ctx1.clearRect(0,0,1024, 1024);
					  alert("get interaction data error");
					}
			   });
	}
}


//this used to get histone mark quality data and draw histogram
function ajax_getHistoneDataAndDraw(trackobj){
	// console.log("cur radius="+cur_radius);
	
	
	 var tempcur_arry = findTrackRadius(trackobj.key);
	var  tempcur_radius = parseInt(tempcur_arry[0]);
		
		 var tcur_radius = cur_radius; // cur_radius+ MCI/2
		 if(tempcur_radius >0 ){
			 tcur_radius = tempcur_radius;
		 }

	
	var canvas1 = document.getElementById("canvas_"+trackobj.key);
		 var ctx1 = canvas1.getContext('2d');
		 ctx1.clearRect(0,0,canvas1.width, canvas1.height);
	
	for(i=0;i<region_lst.length;i++){
		    var q_chr = region_lst[i].chr;
			var q_start = region_lst[i].start;
			var q_end = region_lst[i].end;
			var params;
			var querylen = parseInt(q_end) - parseInt(q_start);
			var t_statbin = computeStatBinSize(querylen);
			
			if(t_statbin >1 && trackobj.statisFile != null){ // statis file
			 var t_statisFile = trackobj.statisFile;
				t_statisFile=t_statisFile.replace("{binsize}",t_statbin);
				params={"chrom":q_chr,"start":q_start,"stop":q_end,"histoneBinSize":t_statbin,"storageFile":t_statisFile};
				//ajax used to get density data and draw histogram picture
				$.ajax({
				   url:'/circosweb/ajax/getHistogramStatics.action',
				   type:'post',
				   dataType:'json',
				   data:params,
				   success:function(value){
					  
					  
					   draw_histogramDensity(value.chrom,value.qualityData,trackobj,value.maxVal,tcur_radius);
					   //fix label position
					   
					   // we find radius from memory, when window size or radius changed, the radius also need to change
					   var track_arry = findTrackRadius(trackobj.key); 
					   var  track_radius = parseInt(track_arry[0]);
					   
					   var left_margin = canvas_width/2+track_radius;
					   $("#label_"+trackobj.key).css("left",left_margin);
					    
					 },
				   error:function(e){
							alert("error");
						}
				   }); 
				
			}else{ // custom
				params ={"chrom":q_chr,"start":q_start,"stop":q_end,"storageFile":trackobj.storage};
			
				$.ajax({
					   url:'/circosweb/ajax/getQualityData.action',
					   type:'post',
					   dataType:'json',
					   data:params,
					   success:function(value){
						   if(value.qualityData != undefined || value.qualityData !=null ){
							  drawHistogram(value.chrom,value.qualityData,trackobj,tcur_radius);
							  var track_radius = findTrackRadius(trackobj.key);
							   var left_margin = canvas_width/2+track_radius;
							   $("#label_"+trackobj.key).css("left",left_margin);
							  
						   }					   
						 },
					   error:function(e){
								alert("error");
							}
					   });			
			}			
	}	
	
	if(tempcur_radius ==0){
		cur_radius += MCI;
	}	
	console.log("draw finish radius="+cur_radius);
}


//getTabixGff
function ajax_getTabixGff3Data(trackobj){
	     var canvas1 = document.getElementById("canvas_"+trackobj.key);
		 var ctx1 = canvas1.getContext('2d');
		 ctx1.clearRect(0,0,1024, 1024);
		 //if the track have existed in the selected  track,then we use the radius there
		 var tmpcur_arry = findTrackRadius(trackobj.key);
		 
		 var tempcur_radius = parseInt(tmpcur_arry[0]);
		 var tcur_radius = cur_radius; //cur_radius+ MCI/2
	
		 if(tempcur_radius >0 ){
			 tcur_radius = tempcur_radius;
		 }
		
		var gene_add_height = MCI;
		var cur_toomany = 10 ;
		
		 //console.log(" ajax_getTabixGff3Data draw "+trackobj.key+" radius="+tcur_radius);
		
		if(region_lst.length == 1){
			var imageObj = new Image();
			var imageleft = canvas_width/2 -20;
			var imagetop =  canvas_width/2 - tcur_radius - MCI;
			if(trackobj.key == "ensembl_gene") {
				imagetop =  canvas_width/2 - tcur_radius - LAYERSMCI;
			}
			imageObj.onload= function() {
				ctx1.drawImage(imageObj,imageleft,imagetop);
			}
			imageObj.src="http://"+window.location.host+"/circosweb/images/loader.gif";
		}
		
		
		/*if(trackobj.key == "ensembl_gene") {
			cur_toomany = 5000000;
		}else if(trackobj.toomany > 0){
			cur_toomany = trackobj.toomany;
		}*/
		
		/*
		1~20kb      1bp
		20kb~50kb   2bp
		50kb~100kb  5bp
		100kb~200kb 10bp
		100kb~500kb 25bp
		500kb~1mb   50bp
		1mb~5mb     250bp
		1mb~10mb    500bp
		10mb~20mb   1,000bp
		10mb~50mb   2,500bp
		50mb~100mb  5,000bp
		100mb~200mb 10,000bp
		*/
		 
		for(i=0;i<region_lst.length;i++){
		    var q_chr = region_lst[i].chr;
			var q_start = region_lst[i].start;
			var q_end = region_lst[i].end;
			var querylen = parseInt(q_end) - parseInt(q_start);
			var params;
			var t_statbin = computeStatBinSize(querylen);
			var param_statbin = t_statbin;
			

			if(t_statbin > cur_toomany && trackobj.statisFile != null){ // statis file
				var t_statisFile = trackobj.statisFile;
				t_statisFile= t_statisFile.replace("{binsize}",t_statbin);
				params={"chrom":q_chr,"start":q_start,"stop":q_end,"histoneBinSize":param_statbin,"storageFile":t_statisFile};
				//ajax used to get density data and draw histogram picture
				$.ajax({
				   url:'/circosweb/ajax/getHistogramStatics.action',
				   type:'post',
				   dataType:'json',
				   data:params,
				   success:function(value){
					   if(region_lst.length == 1){
						   ctx1.clearRect(0,0,1024, 1024);
					   }
					   draw_histogramDensity(value.chrom,value.qualityData,trackobj,value.minVal,value.maxVal,tcur_radius);
					   var track_arry = findTrackRadius(trackobj.key);
					   
					   var track_radius = parseInt(track_arry[0]);
					   var left_margin = canvas_width/2+track_radius;
					   $("#label_"+trackobj.key).css("left",left_margin);							  		 
					},
				   error:function(e){
							alert("error");
						}
				   }); 				
			}else{
				gene_add_height = LAYERSMCI ;
				params ={"chrom":q_chr,"start":q_start,"stop":q_end,"storageFile":trackobj.storage};
				//load a wait image

				$.ajax({
					   url:'/circosweb/ajax/getTabixGff.action',
					   type:'post',
					   dataType:'json',
					   data:params,
					   success:function(value){		
					  if(region_lst.length == 1){
						   ctx1.clearRect(0,0,1024, 1024);
					   }					   
						   if(value.gffData != undefined || value.gffData !=null ){						 
							 drawHistoneByGFF3(value.gffData,trackobj,value.minVal,value.maxVal,tcur_radius);
							  
							  var track_arry = findTrackRadius(trackobj.key);
							  var track_radius = parseInt(track_arry[0]);
							 // console.log("adjust track_radius="+track_radius);
							   var left_margin = canvas_width/2+track_radius;
							   $("#label_"+trackobj.key).css("left",left_margin);
							   						  
						   }
						 },
					   error:function(e){
								alert("error");
							}
					   });
			}
			
			
	}	
	if(tempcur_radius ==0){
		cur_radius += MCI;							
	}	  

}

//this is used to get gene data
function ajax_getGeneData(trackobj){
	var canvas1 = document.getElementById("canvas_"+trackobj.key);
		 var ctx1 = canvas1.getContext('2d');
		 ctx1.clearRect(0,0,1024, 1024);
		 //if the track have existed in the selected  track,then we use the radius there
		 var tmpcur_arry = findTrackRadius(trackobj.key);
		 
		 var tempcur_radius = parseInt(tmpcur_arry[0]);
		 var tcur_radius = cur_radius; //cur_radius+ MCI/2
	
		 if(tempcur_radius >0 ){
			 tcur_radius = tempcur_radius;
		 }
		
		var gene_add_height = MCI;
		var cur_toomany =0;


		
		 //console.log(" ajax_getTabixGff3Data draw "+trackobj.key+" radius="+tcur_radius);
		
		if(region_lst.length == 1){
			var imageObj = new Image();
			var imageleft = canvas_width/2 -20;
			var imagetop =  canvas_width/2 - tcur_radius - LAYERSMCI
			
			imageObj.onload= function() {
				ctx1.drawImage(imageObj,imageleft,imagetop);
			}
			imageObj.src="http://"+window.location.host+"/circosweb/images/loader.gif";
		}
		
		 
		for(i=0;i<region_lst.length;i++){
		    var q_chr = region_lst[i].chr;
			var q_start = region_lst[i].start;
			var q_end = region_lst[i].end;
			var querylen = parseInt(q_end) - parseInt(q_start);
			var params;
			var t_statbin = computeStatBinSize(querylen);
			var param_statbin = t_statbin;
			cur_toomany = 2000; //5mb 
			param_statbin = trackobj.histoneBin;

			if(t_statbin > cur_toomany && trackobj.statisFile != null){ // statis file
				var t_statisFile = trackobj.statisFile;
				t_statisFile= t_statisFile.replace("{binsize}",t_statbin);
				params={"chrom":q_chr,"start":q_start,"stop":q_end,"histoneBinSize":param_statbin,"storageFile":t_statisFile};
				//ajax used to get density data and draw histogram picture
				$.ajax({
				   url:'/circosweb/ajax/getHistogramStatics.action',
				   type:'post',
				   dataType:'json',
				   data:params,
				   success:function(value){
					   if(region_lst.length == 1){
						   ctx1.clearRect(0,0,1024, 1024);
					   }
					   draw_histogramDensity(value.chrom,value.qualityData,trackobj,value.minVal,value.maxVal,tcur_radius);
					   var track_arry = findTrackRadius(trackobj.key);
					   
					   var track_radius = parseInt(track_arry[0]);
					   var left_margin = canvas_width/2+track_radius;
					   $("#label_"+trackobj.key).css("left",left_margin);							  		 
					},
				   error:function(e){
							alert("error");
						}
				   }); 				
			}else{
				gene_add_height = LAYERSMCI ;
				var position = q_chr+":"+q_start+".."+q_end;
				var organism = trackobj.organism ;
				params ={"chrom":q_chr,"start":q_start,"stop":q_end,"position":position,"organism":organism,"storageFile":trackobj.storage};
				//load a wait image
				var url = "";
				if(trackobj.storeclass =="GFF3" || trackobj.storeclass == "gff3"){
					url = "/circosweb/ajax/getTabixGff.action";
				}else{
					url = "/circosweb/ajax/ajaxFindGivenScopeGene.action";
				}
				$.ajax({
					   url: url,
					   type:'post',
					   dataType:'json',
					   data:params,
					   success:function(value){		
					  if(region_lst.length == 1){
						   ctx1.clearRect(0,0,1024, 1024);
					   }					   
						   if(value.gffData != undefined || value.gffData !=null ){						 
							  if(trackobj.glyph=="gene"){
								 drawGene(value.gffData,trackobj,tcur_radius);
							  }else if(trackobj.glyph=="histogram"){
								  drawHistoneByGFF3(value.gffData,trackobj,value.minVal,value.maxVal,tcur_radius);
							  }
							  
							  var track_arry = findTrackRadius(trackobj.key);
							  var track_radius = parseInt(track_arry[0]);
							 // console.log("adjust track_radius="+track_radius);
							   var left_margin = canvas_width/2+track_radius;
							   $("#label_"+trackobj.key).css("left",left_margin);
							   						  
						   }
						 },
					   error:function(e){
								alert("error");
							}
					   });
			}
			
			
	}	
	if(tempcur_radius ==0){
		cur_radius += gene_add_height;
										
	}	  
		
}


/*1~20kb      1bp         1bp
#20kb~50kb   2bp         10bp 
#50kb~100kb  5bp         25bp
#100kb~200kb 10bp        50bp
#200kb~500kb 25bp        100bp
#500kb~1mb   50bp        250bp
#1mb~2mb     50bp        500bp
#2mb~5mb     250bp       1,000bp 
#5mb~10mb    500bp       2,000bp
#10mb~20mb   1,000bp     5,000bp
#20mb~50mb   2,500bp     10,000bp
#50mb~100mb  5,000bp     20,000bp
#100mb~200mb 10,000bp    40,000bp
#*/

function computeStatBinSize(querylen){
	var t_statbin = 1;
			if(querylen>=20000 && querylen<50000){
				t_statbin = 25; //10
			}else if(querylen>=50000 && querylen<100000){
				t_statbin = 25;
			}else if(querylen>=100000 && querylen<200000){
				t_statbin = 50;
			}else if(querylen>=200000 && querylen<500000){
				t_statbin = 100;
			}else if(querylen>=500000 && querylen<1000000){
				t_statbin = 250;
			}else if(querylen>=1000000 && querylen<2000000){
				t_statbin = 500;
			}else if(querylen>=2000000 && querylen<5000000){
				t_statbin = 1000;
			}
			else if(querylen>=5000000 && querylen<10000000){
				t_statbin = 2000;
			}else if(querylen>=10000000 && querylen<20000000){
				t_statbin = 5000;
			}else if(querylen>=20000000 && querylen<50000000){
				t_statbin = 10000;
			}else if(querylen>=50000000 && querylen<100000000){
				t_statbin = 20000;
			}else if(querylen>=100000000 ){
				t_statbin = 40000;
			}
	
	return t_statbin;
}


