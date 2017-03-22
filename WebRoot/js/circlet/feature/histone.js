	//draw histogram    one track
	
	
	
function drawHistogram(qchr,data,trackobj,curradius){
/*	var data = "{'data':[[0.780087,0.788532,0.837359,0.818699*/
	var key = trackobj.key;
	var canvas1 = document.getElementById("canvas_"+key);
	var ctx1 = canvas1.getContext('2d');


	var datalst = data;
	var centerx=parseInt(canvas.width/2);
	var centery=parseInt(canvas.height/2);
	var pointnum=0;
	pointnum += datalst.length;
	
	var circlespacing=10;
//	var pointradian=(Math.PI*2-spacing)/pointnum;
	var pointradian=0;
	//var radiusbase=radius+ideogramWidth/2+circlespacing +15;
	var radiusbase = curradius ;
	
	//compute the point radian to the current chrom view region
	var rid = -1;
	
	for(i=0;i<region_lst.length;i++){
		var ichr = parseInt(qchr);
		var tchr = parseInt(region_lst[i].chr);
		if(ichr == tchr){
			rid = i;
			pointradian = region_lst[i].rgradian/pointnum;
			break;
		}
	}
	
	
	if(pointradian == 0 || rid ==-1){
		return ;
	}
	
	
	
	// for each wreath track
	//var tkh= parseInt(trackobj.height);  // height of track
	var tkh = MCI;	
	// must be numerical
	// figure out max/min of this track
	var max=1,min=0;
	var baseline=(max>0 && min<0);
	var baselineh = baseline ? tkh*(0-min)/(max-min) : 0;
	var pcolor=trackobj.pcolor;
	var ncolor=trackobj.ncolor;
	
	// plot track
	var r= region_lst[rid].rgoffset; // radian offset
	// the json data is in same order as .regionorder, so must use j
	
	//console.log("pointradian="+pointradian+",radian="+r);
	
	var wdata=datalst;
	for(var k=0; k<wdata.length; k++) {
	if(baseline) {
		// use baseline
		if(wdata[k]>0) {
			var linewidth=Math.min(wdata[k],max)*(tkh-baselineh)/max;
			ctx1.strokeStyle=pcolor;
			ctx1.lineWidth=linewidth;
			ctx1.beginPath();
			ctx1.arc(centerx,centery,
			radiusbase+baselineh+linewidth/2,
			r+k*pointradian, r+(k+1)*pointradian,false);
			ctx1.stroke();
		} else if(wdata[k]<0) {
			var linewidth=Math.max(wdata[k],min)*baselineh/min;
			ctx1.strokeStyle=ncolor;
			ctx1.lineWidth=linewidth;
			ctx1.beginPath();
			ctx1.arc(centerx,centery,radiusbase+(baselineh-linewidth/2),r+k*pointradian, r+(k+1)*pointradian,false);
			ctx1.stroke();
		}
	}else {
			// no baseline
			
			var qualityval= wdata[k];
			  
			  if(qualityval>0) {
						
						var hinterval = max-min ;
						if(hinterval == 0 ){
							hinterval = 1;
						}
						var linewidth=Math.min(qualityval,max)*tkh/(2*hinterval);
						var drawlinewidth = linewidth > 1 ? linewidth:1;
						var drawradius = radiusbase+tkh/2+linewidth/2;
						ctx1.strokeStyle=pcolor;
						ctx1.lineWidth=drawlinewidth;
						ctx1.beginPath();
						ctx1.arc(centerx,centery,drawradius,r+k*pointradian, r+(k+1)*pointradian,false);
						ctx1.stroke();
						
					}else {
					
					   var linewidth=(0-qualityval)*tkh/(2*(max-min));
					   var drawlinewidth = linewidth > 1 ? linewidth:1;
						var minusradius = radiusbase+tkh/2-linewidth/2 ;
						ctx1.strokeStyle=pcolor;
						ctx1.lineWidth=drawlinewidth;
						ctx1.beginPath();
						ctx1.arc(centerx,centery,minusradius,r+k*pointradian, r+(k+1)*pointradian,false);

						ctx1.stroke();
					
				}
			
			
			/*
			if(min==0) {
				if(wdata[k]>0) {
					var linewidth=Math.min(wdata[k],max)*tkh/max;
					ctx1.strokeStyle=pcolor;
					ctx1.lineWidth=linewidth;
					ctx1.beginPath();
					ctx1.arc(centerx,centery,radiusbase+linewidth/2,r+k*pointradian, r+(k+1)*pointradian,false);
					ctx1.stroke();
				}
			} else {
				if(wdata[k]<0) {
				   var linewidth=Math.max(wdata[k],min)*tkh/min;
					ctx1.strokeStyle=ncolor;
					ctx1.lineWidth=linewidth;
					ctx1.beginPath();
					ctx1.arc(centerx,centery,radiusbase+tkh-linewidth/2,r+k*pointradian, r+(k+1)*pointradian,false);
					ctx1.stroke();
				}
			}*/
		}//else
	}//for	

	var seltrack ={"key":trackobj.key,"radius":curradius,"height":tkh,"track":trackobj};
	var iden = 0 ;
	var found =0;
	if(selectedTrack.length>0){
		for(k=0;k<selectedTrack.length;k++){
			var selObj = selectedTrack[k];
				if(selObj.key == key){//track
					iden = k ;
					found = 1;
					selObj.radius = curradius;
					selObj.height = tkh;
					break;
				}
		}							
    }
	if(found ==0 ){
		selectedTrack.push(seltrack);
	}
	
}


//this used to draw histogram density picture
//need to comptue the start region like above histogram
//and we need to get min and max value of this region
function draw_histogramDensity(qchr,data,trackobj,min,max,curradius){
	var trackkey = trackobj.key;
	//console.log(trackkey);
	var canvas1 = document.getElementById("canvas_"+trackkey);
	var ctx1 = canvas1.getContext('2d');
	
	
	var datalst = data;
	var centerx=parseInt(canvas.width/2);
	var centery=parseInt(canvas.height/2);
	var pointnum=0;
	pointnum += datalst.length;
	
	var pointradian=(Math.PI*2-spacing)/pointnum;
	var radiusbase=curradius;
	// for each wreath track
	//var tkh = parseInt(trackobj.height);	
	var tkh = MCI;
	
	var pcolor= trackobj.color!=''? trackobj.color:trackobj.pcolor;	
	
	// plot track
	var r=0; // radian offset
	var baseline=(max>0 && min<0);
	var baselineh = baseline ? tkh*(0-min)/(max-min) : 0;
	

	var rid = -1;
	
	for(i=0;i<region_lst.length;i++){
		var ichr = parseInt(qchr);
		var tchr = parseInt(region_lst[i].chr);
		if(ichr == tchr){
			rid = i;
			pointradian = region_lst[i].rgradian/pointnum;
			break;
		}
	}

	//alert(rid);
	if(pointradian == 0 || rid ==-1){
		return ;
	}
	
	var r= region_lst[rid].rgoffset; // radian offset
	
//	console.log("pointradian="+pointradian+",radian="+r+",region_lst[i].chr="+region_lst[rid].chr+",cur radius="+radiusbase+",data length="+datalst.length+",trackkey="+trackkey);
	
	
	var wdata=datalst;
	for(var k=0; k<wdata.length; k++) {
		/*if(min==0) {
				if(wdata[k]>0) {
						var linewidth=Math.min(wdata[k],max)*tkh/max;
						ctx1.strokeStyle=pcolor;
						ctx1.lineWidth=linewidth;
						ctx1.beginPath();
						ctx1.arc(centerx,centery,radiusbase+linewidth/2,r+k*pointradian, r+(k+1)*pointradian,false);
						ctx1.stroke();
				}
			}*/
			
			if(baseline) {
				// use baseline
				if(wdata[k]>0) {
					var linewidth=Math.min(wdata[k],max)*(tkh-baselineh)/max;
					ctx1.strokeStyle=pcolor;
					ctx1.lineWidth=linewidth;
					ctx1.beginPath();
					ctx1.arc(centerx,centery,
					radiusbase+baselineh+linewidth/2,
					r+k*pointradian, r+(k+1)*pointradian,false);
					ctx1.stroke();
				} else if(wdata[k]<0) {
					var linewidth=Math.max(wdata[k],min)*baselineh/min;
					ctx1.strokeStyle=pcolor;
					ctx1.lineWidth=linewidth;
					ctx1.beginPath();
					ctx1.arc(centerx,centery,radiusbase+(baselineh-linewidth/2),r+k*pointradian, r+(k+1)*pointradian,false);
					ctx1.stroke();
				}
		  }else {
			 var qualityval= wdata[k];
			  
			  if(qualityval>0) {
						
						var hinterval = max-min ;
						if(hinterval == 0 ){
							hinterval = 1;
						}
						var linewidth=Math.min(qualityval,max)*tkh/(2*hinterval);
						var drawlinewidth = linewidth > 1 ? linewidth:1;
						var drawradius = radiusbase+tkh/2+linewidth/2;
						ctx1.strokeStyle=pcolor;
						ctx1.lineWidth=drawlinewidth;
						ctx1.beginPath();
						ctx1.arc(centerx,centery,drawradius,r+k*pointradian, r+(k+1)*pointradian,false);
						ctx1.stroke();
						
					}else {
					
					   var linewidth=(0-qualityval)*tkh/(2*(max-min));
					   var drawlinewidth = linewidth > 1 ? linewidth:1;
						var minusradius = radiusbase+tkh/2-linewidth/2 ;
						ctx1.strokeStyle=pcolor;
						ctx1.lineWidth=drawlinewidth;
						ctx1.beginPath();
						ctx1.arc(centerx,centery,minusradius,r+k*pointradian, r+(k+1)*pointradian,false);

						ctx1.stroke();
					
				}
				
			}//else	
	}
	
	
	var seltrack ={"key":trackobj.key,"radius":curradius,"height":tkh,"track":trackobj};
	
	var iden = 0 ;
	var found =0;
	if(selectedTrack.length>0){
		for(k=0;k<selectedTrack.length;k++){
			var selObj = selectedTrack[k];
				if(selObj.key == trackkey){//track
					iden = k ;
					found = 1;
					selObj.radius = curradius;
					selObj.height = tkh;
					break;
				}
		}							
    }
	
	if(found ==0 ){
		selectedTrack.push(seltrack);	
	}
}

//the storage file is gff3, and need to draw histogram
//and we need to get min and max value of this region
function drawHistoneByGFF3(data,trackobj,min,max,curradius){
	var cachename=[];
	cachetrackpos[trackobj.key] = cachename;
	var key = trackobj.key;
	var canvas1 = document.getElementById("canvas_"+key);	
	ctx1 = canvas1.getContext('2d');
	var centerx=parseInt(canvas.width/2);
	var centery=parseInt(canvas.height/2);
	
	var baseline=(max>0 && min<0);
	var baselineh = baseline ? tkh*(0-min)/(max-min) : 0;

	var current_radius =curradius;
	var color = trackobj.color;
	//var tkh = parseInt(trackobj.lineWidth);
	var tkh = MCI;	
	
	for(i=0;i<data.length;i++){
		var gff = data[i];
		var gene_chr = gff.seq;
		var gene_start = parseInt(gff.start);
		var gene_end = parseInt(gff.end);
		var strand = gff.strand;
		

		
		var rid=-1;
		for(k=0;k<region_lst.length;k++){
				if(gene_chr == region_lst[k].chr){
					rid = k;
					break;
				}
		}
		if(rid == -1) continue;
		
		var region_start = region_lst[rid].start;
		//alert("region start="+region_start);
		var region_end = region_lst[rid].end;
		
		
		//compute start angle
		//alert(gene_start+","+region_start+","+gene_end+","+region_end);
		
		if( gene_start<region_start||gene_end > region_end){
			continue;
		}
		
		
		var arc_start;
		var arc_end ;
		var arc_offset = region_lst[rid].rgoffset ;
		
		arc_start = (gene_start - region_start) * sf; 
		arc_end = (gene_end - region_start) * sf;
		
		var qualityval = gff.score;
	
		//here we need to draw the histogram height
		if(baseline) {
				// use baseline
				if(qualityval>0) {
					var linewidth=Math.min(qualityval,max)*(tkh-baselineh)/max;
					ctx1.strokeStyle=color;
					ctx1.lineWidth=linewidth;
					ctx1.beginPath();
					ctx1.arc(centerx,centery,
					current_radius+baselineh+linewidth/2,
					arc_offset+arc_start, arc_offset+arc_end,false);
					ctx1.stroke();
				} else if(qualityval<0) {
					var linewidth=Math.max(qualityval,min)*baselineh/min;
					ctx1.strokeStyle=color;
					ctx1.lineWidth=linewidth;
					ctx1.beginPath();
					ctx1.arc(centerx,centery,current_radius+(baselineh-linewidth/2),arc_offset+arc_start, arc_offset+arc_end,false);
					ctx1.stroke();
				}
		  }else {
				// no baseline
					
					if(qualityval>0) {
						
						var hinterval = max-min ;
						if(hinterval == 0 ){
							hinterval = 1;
						}
						var linewidth=Math.min(qualityval,max)*tkh/(2*hinterval);
						//var drawlinewidth = linewidth > 1 ? linewidth:1;
						var drawlinewidth = linewidth ;
						var drawradius = current_radius +tkh/2+linewidth/2;
						ctx1.strokeStyle=color;
						ctx1.lineWidth=drawlinewidth;
						ctx1.beginPath();
						ctx1.arc(centerx,centery,drawradius,arc_offset+arc_start, arc_offset+arc_end,false);
						ctx1.stroke();
					//	console.log("qualityval="+qualityval+",drawlinewidth="+drawlinewidth+",drawradius="+drawradius);
					}else {
					
					   var linewidth=(0-qualityval)*tkh/(2*(max-min));
					  // var drawlinewidth = linewidth > 1 ? linewidth:1;
						var drawlinewidth = linewidth;
						var minusradius = current_radius+tkh/2-linewidth/2 ;
						ctx1.strokeStyle=color;
						ctx1.lineWidth=drawlinewidth;
						ctx1.beginPath();
						ctx1.arc(centerx,centery,minusradius,arc_offset+arc_start, arc_offset+arc_end,false);

						ctx1.stroke();
					//	console.log("qualityval="+qualityval+",drawlinewidth="+drawlinewidth+",drawradius="+minusradius);
				}
			}//else			
	}	
	
	//selected track value
	var seltrack ={"key":trackobj.key,"radius":curradius,"height":tkh,"track":trackobj};	
	var iden = 0 ;
	var found =0;
	if(selectedTrack.length>0){
		for(k=0;k<selectedTrack.length;k++){
			var selObj = selectedTrack[k];
				if(selObj.key == key){//track
					iden = k ;
					found = 1;
					selObj.radius = curradius;
					selObj.height = tkh;
					break;
				}
		}							
    }
	if(found ==0 ){
		selectedTrack.push(seltrack);
	}
}
