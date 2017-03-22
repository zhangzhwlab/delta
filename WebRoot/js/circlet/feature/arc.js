//draw arc in the single chromosome

var baricon = {
		data: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAKCAIAAAAPTiitAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAZSURBVChTY/hPBKC3IqIAVDFeMPgU/f8PALFiQsywa5B/AAAAAElFTkSuQmCC"
}

//used to store some information for baricon,then we need use it to triggle the popup div



function drawArc(datalist,trackobj,flag,layer){		
		//get the canvas for the arc draw
		var key = trackobj.key;
		var canvas1 = document.getElementById("canvas_"+key);
		var ctx1 = canvas1.getContext('2d');	
		if(layer ==0){
			ctx1.clearRect(0,0,1024, 1024);			
		}
		
		
		var cur_linew = parseInt(trackobj.lineWidth)
				
		if(flag == 1){ // custom interaction data
			ctx1.lineWidth = cur_linew;
		}	
	
		var centerx=parseInt(canvas.width/2);
		var centery=parseInt(canvas.height/2);
		bararry=[];
		var t_top= 0- canvas_height-8;
		if($("#bardiv").length ==0){
				var outdiv= $("<div/>",{
						id: "bardiv", 
						css:{
							position : 'relative', 
							background: 'white none repeat scroll 0 0',
							width: '200px',
							"margin-top": t_top
						}
			});
			outdiv.appendTo("#trackContainer");	
		}else{
			$("#bardiv").html("");
		}
		
		//arc data r1id,item.start,item.stop, r2id,c[1],c[3], parseFloat(lst[1])
		//we need to according to the chromosome of anchor and target to identify the show chrom and region
		var maxFreq =100;
		var sWidth=10;
		for(i=0; i<datalist.length; i++) {
			
			// r1id, start1,stop1, r2id, start2,stop2, score
			var item1=datalist[i];			
			var anchorChr = item1.anchorChr+"";
			var desPos = item1.name;// targetchr,score	
			var start3 = desPos.indexOf(",");
			var deschr = desPos.substring(0,start3);
			var desstart = item1.stop;
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
					ctx1.lineWidth = cur_linew;
				}			
							
						
				var anchor_region_start = region_lst[r1id].start;
				var anchor_region_end = region_lst[r1id].end;
				var target_region_start = region_lst[r2id].start;
				var target_region_end = region_lst[r2id].end;
			//	console.log("anchor region start ========="+anchor_region_start+","+anchor_region_end);
				if(Math.max(item1.start, anchor_region_start)>=anchor_region_end || Math.max(desstart,target_region_start)>=target_region_end || item1.start < anchor_region_start ) continue;
			
				ctx1.strokeStyle = trackobj.color; //positive and negtive color
				// arc start point
				var r1 = region_lst[r1id].rgoffset + (Math.max(item1.start,anchor_region_start) - anchor_region_start)*sf; 
				//var r1 hvobj.regionradianoffset[r1id]+(Math.max(item[1],regions[r1id].dstart)-regions[r1id].dstart)*sf;
				var x1=centerx+(radius-ideogramWidth/2)*Math.cos(r1);
				var y1=centery+(radius-ideogramWidth/2)*Math.sin(r1);
				// arc stop point
				var r2 = region_lst[r2id].rgoffset + (Math.max(desstart,target_region_start) - target_region_start)*sf; 
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
				}
				
			}
			
			//we need to draw an anchor bar along the outer ideogram when this interaction have multiple target
			//identify whether this anchor has drawn a bar
			var findanchor = 0 ;
			//console.log("anchor infomation "+item1.start+","+item1.anchorEnd);
			if(bararry.length >0 ){				
				for(var m=0;m<bararry.length;m++){
					var barobj = bararry[m];
					if(barobj.start == item1.start && barobj.stop == item1.anchorEnd){
						findanchor=1;
						break;
					}
					
				}
			}
			if(findanchor ==0 ){
				   
					if(item1.start>= ideogram_start && item1.anchorEnd <= ideogram_end && item1.target != null && item1.target.length >0 ){
						
					var barflag = 0 ;
					var bardivid="bardiv_"+item1.id;
					var t_top= 0- canvas_height-8;
					var div= $("<div/>",{
							id:bardivid, 
							css:{
								position : 'absolute', 
								"z-index": '600',
								width: '200px',
								"background-color": "#efefef",
								border: "solid 1px #399edb",
								display : 'none'
							}
					}).addClass("bardiv");
					var conf = getQueryString("conf");
						
						var p="<p style='background-color:#399edb;'><strong>Target List</strong> <span style='padding-left:25px;'><input type='button' value='Save as' onclick=\"saveTarget('"+item1.start+"','"+item1.anchorEnd+"','"+ideogram_start+"','"+ideogram_end+"','"+trackobj.storage+"')\" /></span></p>";
						p+="<p>Anchor:"+item1.start+","+item1.anchorEnd+"</p>";
						p+="<p>Target List</p><ul>";
						for(var j=0;j<item1.target.length;j++){
							var targetitem = item1.target[j];
							if(targetitem.targetStart>= ideogram_start && targetitem.targetStart<= ideogram_end && targetitem.stop >=ideogram_start && targetitem.stop<=ideogram_end){
							}else{
								//need to load this data into a div panel 	
								barflag = 1;
								var locpos = item1.anchorChr+"%3A"+item1.start+".."+targetitem.stop ;
																
								p+="<li><a href=\"#\">"+targetitem.anchorChr+" "+targetitem.targetStart+" "+targetitem.stop+"</a>";
															
								if(conf == null){
								
								    p+="<ul><li><a href='http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?loc="+item1.anchorChr+":"+item1.start+".."+targetitem.stop+"'>Zoom to</a></li>";
									p+="<li><a href='http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?loc="+targetitem.anchorChr+":"+targetitem.targetStart+".."+targetitem.stop+"'>Go to</a></li>";
									p+="<li><a target='_blank'  href='http://"+window.location.host+"/jbrowse/index.html?data=human&loc="+locpos+"'>Genome View</a></li>";
									p+="<li><a target='_blank'  href='http://"+window.location.host+"/pages/visualization/physical_view.jsp?loc="+locpos+"'>Physical View</a></li></ul></li>";
								}else{
									
									p+="<ul><li><a href='http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?conf="+conf+"&loc="+item1.anchorChr+":"+item1.start+".."+targetitem.stop+"'>Zoom to</a></li>";
									p+="<li><a href='http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?conf="+conf+"&loc="+targetitem.anchorChr+":"+targetitem.targetStart+".."+targetitem.stop+"'>Go to</a></li>";
									p+="<li><a target='_blank' href='http://"+window.location.host+"/jbrowse/index.html?data="+conf+"&loc="+locpos+"'>Genome View</a></li>";
									p+="<li><a target='_blank' href='http://"+window.location.host+"/pages/visualization/physical_view.jsp?conf="+conf+"&loc="+locpos+"'>Physical View</a></li></ul></li>";
								}
							}
							
						}
							
							div.html(p);
							div.appendTo("#bardiv");							
					}
					
					
					if(barflag == 1){
						// then draw bar
						ctx.save();				
						var r1 = region_lst[r1id].rgoffset + (Math.max(item1.start,anchor_region_start) - anchor_region_start)*sf; 
						//var r1 hvobj.regionradianoffset[r1id]+(Math.max(item[1],regions[r1id].dstart)-regions[r1id].dstart)*sf;
						var x1=centerx+(radius-ideogramWidth/2+4)*Math.cos(r1);
						var y1=centery+(radius-ideogramWidth/2+4)*Math.sin(r1);			
						ctx.translate(x1,y1);					
						var angle = r1;				
						ctx.rotate(angle);				
						//draw bar
						ctx.beginPath();
						ctx.lineWidth=1;
						ctx.fillStyle="red";
						ctx.fillRect(-3,0,6,6); 
						ctx.stroke();
						
						var barstore={"x":x1,"y":y1,"start":item1.start,"stop":item1.anchorEnd,"divid":bardivid};
						bararry.push(barstore);
						ctx.restore();
					}
			}
			
					
		}
					
}

function showtargetjumpmenu(anchorstart,itemid,anchorend,targetstart,targetend,anchorchr,targetchr,pindex){
	
	var left = $("#bardiv_"+itemid).position().left + 180;
	var top = $("#bardiv_"+itemid).position().top +10+ pindex *30;
	$("#_p"+itemid+"_"+pindex).css("background-color","#585858");
	$("#_p"+itemid+"_"+pindex).css("color","#ffffff");
	var div= $("<div/>",{
							id:"_jump"+itemid, 
							css:{
								position : 'absolute', 
								"z-index": '610',
								width: '100px',
								"background-color": "#efefef",
								border: "solid 1px #399edb",
								left: left+"px",
								top: top+"px",
								display : 'block'
							}
					});
					
	var p="<p><a href='http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?loc="+anchorchr+":"+anchorstart+".."+targetend+"'>Zoom to</a></p>";
	p+="<p><a href='http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?loc="+anchorchr+":"+anchorstart+".."+targetend+"'>Go to</a></p>";
	p+="<p><a href='#'>Genome View</a></p>";
	p+="<p><a href='#'>Physical View</a></p>";
	div.html(p);
	div.appendTo("#bardiv");
							
}


function hidetargetjumpmenu(itemid,pindex){
	$("#_p"+itemid+"_"+pindex).css("background-color","");
	$("#_p"+itemid+"_"+pindex).css("color","#000000");
$( "#_jump"+itemid ).remove();
}

//this is used to get target list with target end position larger than view scope
function saveTarget(acstart,acend,vstart,vend,storageFile){
	var params={"anchorStart":acstart,"anchorEnd":acend,"viewStart":vstart,"viewEnd":vend,"storageFile":storageFile};
	window.location.href="/circosweb/pipeline/saveAnchroTarget.action?anchorStart="+acstart+"&anchorEnd="+acend+"&viewStart="+vstart+"&viewEnd="+vend+"&storageFile="+storageFile;
	
}