/*
*this is used to create line between two positions 
*/
function drawLine(qchr,data,trackobj,curradius){
/*	var data = "{'data':[[0.780087,0.788532,0.837359,0.818699*/
	var key = trackobj.key;
	console.log(key);
	var canvas1 = document.getElementById("canvas_"+key);
	var ctx1 = canvas1.getContext('2d');
	ctx1.clearRect(0,0,canvas1.width, canvas1.height);

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
			pointradian = region_radian[i]/pointnum;
			break;
		}
	}	
	if(pointradian == 0){
		return ;
	}
	
	
	
	// for each wreath track
	var tkh= parseInt(trackobj.height);
		
	// must be numerical
	// figure out max/min of this track
	var max=1,min=0;
	var baseline=(max>0 && min<0);
	var baselineh = baseline ? tkh*(0-min)/(max-min) : 0;
	var pcolor=trackobj.pcolor;
	var ncolor=trackobj.ncolor;
	
	// plot track
	var r= region_offset[rid]; // radian offset
	// the json data is in same order as .regionorder, so must use j
			
	var wdata=datalst;
	var pre_track_draw =-1;
	var cur_track_draw =-1;
	for(var k=0; k<wdata.length; k++) {
		ctx1.beginPath();
	

		if(k>0){
			var linewidth=Math.min(wdata[k-1],max)*(tkh-baselineh)/max;
			pre_track_draw = radiusbase+baselineh+linewidth/2;
			var r1 = (k-1)*pointradian;
			ctx1.strokeStyle=pcolor;

			ctx1.moveTo(centerx+pre_track_draw*Math.cos(r1),centery+pre_track_draw*Math.sin(r1));
			
			linewidth=Math.min(wdata[k],max)*(tkh-baselineh)/max;
			cur_track_draw=radiusbase+baselineh+Math.min(wdata[k],max)*(tkh-baselineh)/max;
			var r2 = k*pointradian;
			ctx1.lineTo(centerx + cur_track_draw*Math.cos(r2),centery+cur_track_draw*Math.sin(r2));
		}		
		ctx1.stroke();		
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



