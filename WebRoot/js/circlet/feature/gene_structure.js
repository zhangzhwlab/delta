//this used to draw gene around the cirlet 
//region hs7:23053401..32053392
var gene_toomany =300;

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

//a gene will be showed in box style, no intron and exon structure. but the strand will be showed

function drawGene(data,trackobj,curradius){
	
	var key = trackobj.key;
	var canvas1 = document.getElementById("canvas_"+key);
	var ctx1 = canvas1.getContext('2d');

	
	var wreath_height = parseInt(trackobj.height);
	var oldrian =[];//six layer to display gene
	var avg_gene_height = parseInt(wreath_height/6);
	
	oldrian=[{"arcradian":0,"height":avg_gene_height},{"arcradian":0,"height":avg_gene_height*2},{"arcradian":0,"height":avg_gene_height*3},{"arcradian":0,"height":avg_gene_height*4},{"arcradian":0,"height":avg_gene_height*5},{"arcradian":0,"height":avg_gene_height*6}];
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
	
		
		var arc_start = (gene_start - region_start) * sf; 
		var arc_end = (gene_end - region_start) * sf;
		var arc_offset = region_offset[rid] ;
		
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
		
		if(found ==0){
			
			continue;
		}

		ctx1.strokeStyle= trackobj.color;
		ctx1.lineWidth= avg_gene_height - interval;
		ctx1.beginPath();
		ctx1.arc(centerx,centery,current_radius,arc_offset+arc_start, arc_offset+arc_end,false);

		ctx1.stroke();
		/*if(strand=="+"){
			 var strand_end = (gene_end - region_start+plusArrow.width) * sf
			 ctx1.strokeStyle=trackobj.pcolor;
			 ctx1.lineWidth= parseInt(plusArrow.height);
			 ctx1.beginPath();
			 ctx1.arc(centerx,centery,current_radius,arc_offset+arc_end,arc_offset+strand_end,false);
			 ctx1.stroke();
			 
			 
		}else if(strand=="-"){
			 var strand_end = (gene_start - region_start-plusArrow.width) * sf
			 ctx1.strokeStyle = trackobj.pcolor;
			 ctx1.lineWidth= plusArrow.height;
			 ctx1.beginPath();
			 ctx1.arc(centerx,centery,current_radius,arc_offset+strand_end,arc_offset+arc_start,false);
			 ctx1.stroke();
			 
			
		}*/
	}	
	
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
}