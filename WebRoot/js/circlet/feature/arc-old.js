//draw arc in the single chromosome
function drawArc(datalist,trackobj,flag){
		
		//we will dynamically create a div for arc drawing
		
		
		//get the canvas for the arc draw
		var key = trackobj.key;
		var canvas1 = document.getElementById("canvas_"+key);
		var ctx1 = canvas1.getContext('2d');
	
		
		var cur_linew = parseInt(trackobj.lineWidth)
		
		
		
		if(flag == 1){ // custom interaction data
			ctx1.lineWidth = cur_linew;
		}
		
		var centerx=parseInt(canvas.width/2);
		var centery=parseInt(canvas.height/2);
	
		//arc data r1id,item.start,item.stop, r2id,c[1],c[3], parseFloat(lst[1])
		//we need to according to the chromosome of anchor and target to identify the show chrom and region
		//alert(datalist.length);
		var maxFreq =100;
		var sWidth=10;
		for(i=0; i<datalist.length; i++) {
			// r1id, start1,stop1, r2id, start2,stop2, score
			var item1=datalist[i];
			var anchorChr = item1.anchorChr+"";
			var desPos = item1.name;// 7:153000001-153200000,1	
			var start1 = desPos.indexOf(":");
			var start2 = desPos.indexOf("-");
			var start3 = desPos.indexOf(",");
			var deschr = desPos.substring(0,start1);
			var desstart = desPos.substring(start1+1,start2);
			var desstop = desPos.substring(start2+1,start3);
			var freq = parseInt(desPos.substring(start3+1,desPos.length) );
			var r1id=-1;
			var r2id =-1;

			for(k=0;k<region_lst.length;k++){
				if(anchorChr == region_lst[k].chr){
					r1id = k;
					break;
				}
			}
			
			for(k=0;k<region_lst.length;k++){
				if(deschr == region_lst[k].chr){
					r2id = k;
					break;
				}
			}
			
			if(r1id!=-1&&r2id!=-1){
							//filter the valid data
				if(flag ==2){
					cur_linew =  Math.min(freq,maxFreq)*sWidth/maxFreq ;
					cur_linew = parseInt(cur_linew + 1) ;
					//alert(cur_linew);
					ctx1.lineWidth = cur_linew;
					//console.log(cur_linew);
				}			
							
						
				var anchor_region_start = region_lst[r1id].start;
				var anchor_region_end = region_lst[r1id].end;
				var target_region_start = region_lst[r2id].start;
				var target_region_end = region_lst[r2id].end;
				if(Math.max(item1.start, anchor_region_start)>=Math.min(item1.stop,anchor_region_end) || Math.max(desstart,target_region_start)>=Math.min(desstop,target_region_end)) continue;
				
				//var r1id=item[0];
				//var r2id=item[3];
				//if(regionorder.indexOf(r1id)==-1 || regionorder.indexOf(r2id)==-1) continue;
				//if(Math.max(item[1],regions[r1id].dstart)>=Math.min(item[2],regions[r1id].dstop) ||
				//Math.max(item[4],regions[r2id].dstart)>=Math.min(item[5],regions[r2id].dstop)) continue;
			
				ctx1.strokeStyle = trackobj.color; //positive and negtive color
				// arc start point
				var r1 = region_offset[r1id] + (Math.max(item1.start,anchor_region_start) - anchor_region_start)*sf; 
				//var r1 hvobj.regionradianoffset[r1id]+(Math.max(item[1],regions[r1id].dstart)-regions[r1id].dstart)*sf;
				var x1=centerx+(radius-ideogramWidth/2)*Math.cos(r1);
				var y1=centery+(radius-ideogramWidth/2)*Math.sin(r1);
				// arc stop point
				var r2 = region_offset[r2id] + (Math.max(desstart,target_region_start) - target_region_start)*sf; 
				//var r2=hvobj.regionradianoffset[r2id]+(Math.max(item[4],regions[r2id].dstart)-regions[r2id].dstart)*sf;
				var x2=centerx+(radius-ideogramWidth/2)*Math.cos(r2);
				var y2=centery+(radius-ideogramWidth/2)*Math.sin(r2);
				var dist = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
				// compute arc center
				var rdiff = Math.abs(r2-r1);
				if(rdiff == Math.PI) {
					ctx1.beginPath();ctx1.lineTo(x1,y1);ctx1.lineTo(x2,y2);ctx1.stroke();ctx1.closePath();
				} else { //I need to read this script to understand how it draw the arc line
					// arc
					// radian of the center line between r1 and r2
					var r3 = rdiff/2+Math.min(r1,r2);
					// relative radian spanned between r1 and r2
					var rtrue=rdiff;
					if(rtrue>Math.PI) {
						rtrue=Math.PI*2-rdiff;
						r3+=Math.PI;
						if(r3>Math.PI*2) r3-=Math.PI*2;
					}
					// radian spanned by the arc
					var arc_radian = (Math.PI-rtrue)/2;
					var dist2 = (dist/2)/Math.tan(arc_radian/2); // incremental distance from circle radius
					// incremented circle radius
					var radius2 = dist2+Math.sqrt(Math.pow(radius-ideogramWidth/2,2)-Math.pow(dist/2,2));
					// arc center point
					var xc=centerx+radius2*Math.cos(r3);
					var yc=centery+radius2*Math.sin(r3);
					// radian of arc's center line
					var r5 = r3>Math.PI ? r3-Math.PI : r3+Math.PI;
					ctx1.beginPath();
					var arcradius=Math.sqrt(dist*dist/4+dist2*dist2);
					//alert("arc x="+xc+",y="+yc+",r5="+r5+",arc_radian="+arc_radian+",target start="+item1.start+",source start="+desstart);
					ctx1.arc(xc,yc,arcradius,r5-arc_radian/2,r5+arc_radian/2,false);
					ctx1.stroke();
					ctx1.closePath();
					console.log("draw arc"+ctx1.lineWidth);
				}
				
			}
			

		}		
}