//this used to draw gene around the cirlet 
//region hs7:23053401..32053392
var gene_toomany =300;
var show_gene_label= 5;
var ctx1;
var  plusArrow = {
             data: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAFCAYAAACXU8ZrAAAATUlEQVQIW2NkwATGQKFYIG4A4g8gacb///+7AWlBmNq+vj6V4uLiJiD/FRBXA/F8xu7u7kcVFRWyMEVATQz//v0Dcf9CxaYRZxIxbgIARiAhmifVe8UAAAAASUVORK5CYII=",
             width: 500,
             height: 1
         };

var minusArrow = {
             data: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAFCAYAAACXU8ZrAAAASklEQVQIW2NkQAABILMBiBcD8VkkcQZGIAeEE4G4FYjFent764qKiu4gKXoPUjAJiLOggsxMTEwMjIwgYQjo6Oh4TLRJME043QQA+W8UD/sdk9IAAAAASUVORK5CYII=",
             width: 500,
             height:1
         };
var connectLine = {
		data: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAKCAIAAAAPTiitAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAZSURBVChTY/hPBKC3IqIAVDFeMPgU/f8PALFiQsywa5B/AAAAAElFTkSuQmCC"
	
}



//a gene will be showed in box style, no intron and exon structure. but the strand will be showed

function drawGene(data,trackobj,curradius){
	var cachename=[];
	cachetrackpos[trackobj.key] = cachename;
	var key = trackobj.key;
	var canvas1 = document.getElementById("canvas_"+key);	
	ctx1 = canvas1.getContext('2d');

	
	var wreath_height = parseInt(LAYERSMCI);
	var showname= $("#tlst_"+trackobj.key+"_showname");
	//if(showname.is(":checked")){
		//wreath_height = 100;
	//}
	
	
	var oldrian =[];//six layer to display gene
	var avg_gene_height = parseInt(wreath_height/19);
	
	oldrian=[{"arcradian":0,"height":avg_gene_height},{"arcradian":0,"height":avg_gene_height*2},{"arcradian":0,"height":avg_gene_height*3},{"arcradian":0,"height":avg_gene_height*4},{"arcradian":0,"height":avg_gene_height*5},{"arcradian":0,"height":avg_gene_height*6},{"arcradian":0,"height":avg_gene_height*7},{"arcradian":0,"height":avg_gene_height*8},{"arcradian":0,"height":avg_gene_height*9},{"arcradian":0,"height":avg_gene_height*10},{"arcradian":0,"height":avg_gene_height*11},{"arcradian":0,"height":avg_gene_height*12},{"arcradian":0,"height":avg_gene_height*13},{"arcradian":0,"height":avg_gene_height*14},{"arcradian":0,"height":avg_gene_height*15},{"arcradian":0,"height":avg_gene_height*16},{"arcradian":0,"height":avg_gene_height*17},{"arcradian":0,"height":avg_gene_height*18},{"arcradian":0,"height":avg_gene_height*19}];
	for(i=0;i<data.length;i++){
		var gff = data[i];
		var gene_chr = gff.seq;
		var gene_start = parseInt(gff.start);
		var gene_end = parseInt(gff.end);
		var strand = gff.strand;
		
		var centerx=parseInt(canvas.width/2);
		var centery=parseInt(canvas.height/2);
		
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
		
		var current_radius ;
	//	alert("cur track radius="+cur_track_radius);
		var color = trackobj.color;
		var linewidth = parseInt(trackobj.lineWidth);
		var arc_start;
		var arc_end ;
		var arc_offset = region_lst[rid].rgoffset;
		
		arc_start = (gene_start - region_start) * sf; 
		arc_end = (gene_end - region_start) * sf;
	
		//identify where to draw this gene
		var current_gene_start_radian = arc_offset+arc_start;
		var found = 0;
		
		var interval = 2;

		for(j=0;j<oldrian.length;j++){
			var cur_oldrian = oldrian[j];
			if(current_gene_start_radian> cur_oldrian.arcradian){
				current_radius = curradius + cur_oldrian.height;
				
				cur_oldrian.arcradian = arc_offset+arc_end;
				found =1;
				break;
			}
			
		}
		
		if(found ==0){ //here we need enlarge the oldrian array 
			/*var old_rian_length = oldrian.length;
			for(var m=old_rian_length+1;m<=old_rian_length*2; m++ ){
				var add_radian = {"arcradian":0,"height":avg_gene_height*m};
				oldrian.push(add_radian);
			}	
			//continue;
			for(j=old_rian_length+1;j<oldrian.length;j++){
			var cur_oldrian = oldrian[j];
			if(current_gene_start_radian> cur_oldrian.arcradian){
				current_radius = curradius + cur_oldrian.height;
				
				cur_oldrian.arcradian = arc_offset+arc_end;
				found =1;
				break;
			}	
			
		 }*/
		 continue;
		}
		
		
		//we need to identify whether this gene has children
		if(gff.children != null && gff.children.length>0 ){
			//draw exon
			
			for(var j=0;j<gff.children.length;j++){
				var subchildren = gff.children[j];
				var child_start = parseInt(subchildren.start);
				var child_end = parseInt(subchildren.end);
				if( child_start<region_start||child_end > region_end){
					continue;
				}
				
				var child_arc_start = (child_start - region_start) * sf; 
				var child_arc_end = (child_end - region_start) * sf;
				//console.log("exon "+child_start+","+child_end);
				
				ctx1.strokeStyle= trackobj.color;
				ctx1.lineWidth= avg_gene_height - interval;
				ctx1.beginPath();
				ctx1.arc(centerx,centery,current_radius,arc_offset+child_arc_start, arc_offset+child_arc_end,false);
				ctx1.stroke();
				
				//we need to draw to a connected line
				if(j>0){
					var pre_end ;
					var pre_end_arc ;
					var cur_start;
					if(strand=="+"){ // current end connect to previous start
						var presubchildren = gff.children[j-1];
						pre_end = parseInt(presubchildren.end);
						var pre_start = parseInt(presubchildren.start); 
						pre_end_arc = (pre_end - region_start) * sf;
						cur_start = child_arc_start;
						//	console.log("pre pos=("+pre_start+","+pre_end+"),cur startpos=("+child_start+","+child_end+"),connect pos"+pre_end+",curstart="+child_start+",angle="+(arc_offset+pre_end_arc)+","+(arc_offset+cur_start));
						ctx1.lineWidth = 1;
						ctx1.strokeStyle= "black";
						ctx1.beginPath();
						
						ctx1.arc(centerx,centery,current_radius,arc_offset+pre_end_arc, arc_offset+cur_start,false);
						
						
						ctx1.stroke();
						
					}else if(strand=="-"){
						var presubchildren = gff.children[j-1];
						pre_end = parseInt(presubchildren.end);
						pre_end_arc = (pre_end - region_start) * sf;	
						var pre_start = parseInt(presubchildren.start); 						
						cur_start = child_arc_start;	
						ctx1.lineWidth = 1;
						ctx1.strokeStyle= "black";
						ctx1.beginPath();
						
						ctx1.arc(centerx,centery,current_radius,arc_offset+pre_end_arc, arc_offset+cur_start,false);
						
					
						ctx1.stroke();						
					}
					
						
				}			
			}
		}else{
				ctx1.strokeStyle= trackobj.color;
				ctx1.lineWidth= avg_gene_height - interval;
				//var draw_start = arc_offset+arc_start;
			//	var draw_end = arc_offset+arc_end;
				//console.log(ctx1.lineWidth+","+trackobj.color+","+draw_start+","+draw_end);
				ctx1.beginPath();
				ctx1.arc(centerx,centery,current_radius,arc_offset+arc_start, arc_offset+arc_end,false);
				ctx1.stroke();
		}
	
		if(strand=="+"|| strand=="."){			  
			   
				var angle = arc_offset+arc_end;
				if(angle < Math.PI*2){ //there must be leave some space to drawn arrow
					ctx1.save();
					var x = centerx+(current_radius+avg_gene_height/4)*Math.cos(angle);
					var y = centery+(current_radius+avg_gene_height/4)*Math.sin(angle);
					if(strand=="+"){
						ctx1.translate(x, y);        
								// rotate the rect
						ctx1.rotate(angle+Math.PI*0.5);		
						var image = new Image();
						image.src= plusArrow.data;	
						if(image.complete){
							onloadHandler.call(image);
						}else{
							image.onload = onloadHandler;  //注意这里跟上面的区别，img.src赋值 与 绑定 onload事件的顺序相反  
						}		
					}
								
					
					var angle1 =  arc_offset+arc_start;
				
				
					var cachearry=new Array(angle,current_radius-avg_gene_height/2,avg_gene_height,strand,angle1,gff);//length
					cachename.push(cachearry);
					
					
					//gene name
					if(showname.is(":checked")){
						if(angle > 0 && angle <  Math.PI ){
							ctx1.rotate(Math.PI);
							var genelength = gff.name.length*6;
							ctx1.fillText(gff.name,-genelength,-5);	
						}else{
							
							ctx1.fillText(gff.name,0,0);	
						}
						
					}
					ctx1.restore();	
				}
				
				
		}else if(strand=="-"){
				
				var angle = arc_offset+arc_start;
				if(angle < Math.PI*2){
					ctx1.save();				
					var x = centerx+(current_radius-avg_gene_height/4)*Math.cos(angle);
					var y = centery+(current_radius-avg_gene_height/4)*Math.sin(angle);		
					ctx1.translate(x, y);  
					ctx1.rotate(angle+Math.PI*1.5);
					var image = new Image();
					image.src= plusArrow.data;	
					if(image.complete){
						onloadHandler.call(image);
					}else{
						image.onload = onloadHandler;  //注意这里跟上面的区别，img.src赋值 与 绑定 onload事件的顺序相反  
					}			
					
					var angle1 =  arc_offset+arc_end;
					var cachearry=new Array(angle,current_radius-avg_gene_height/2,avg_gene_height ,strand,angle1,gff);
					cachename.push(cachearry);
					
					//show name		
					
					if(showname.is(":checked")){
						if(angle > 0 && angle <  Math.PI ){
							//ctx1.rotate(Math.PI);
							var genelength = 0;
							ctx1.fillText(gff.name,genelength,15);	
						}else{
							ctx1.rotate(Math.PI);
							var genelength = gff.name.length*6;
							ctx1.fillText(gff.name,-genelength,10);	
						}
						
					}				
					ctx1.restore();	
				}				
		}
	}	
	
	//update LAYERSMCI 
	//wreath_height = avg_gene_height * oldrian.length;
	
	cachetrackpos[trackobj.key] = cachename;
	//selected track value
	var seltrack ={"key":trackobj.key,"radius":curradius,"height":wreath_height,"track":trackobj};	
	var iden = 0 ;
	var found =0;
	if(selectedTrack.length>0){
		for(k=0;k<selectedTrack.length;k++){
			var selObj = selectedTrack[k];
				if(selObj.key == key){//track
					iden = k ;
					found = 1;
					selObj.radius = curradius;
					selObj.height = wreath_height;
					break;
				}
		}							
    }
	if(found ==0 ){
		selectedTrack.push(seltrack);
	}
	return wreath_height;
}



function onloadHandler(){
	ctx1.drawImage(this, 0, 0);		
}