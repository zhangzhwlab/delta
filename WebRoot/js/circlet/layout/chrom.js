/* call this before making the first plot or regions are added/removed
changing radius won't affect the radian
this must be called prior to running ajax to fetch data for wreath track
when referesh, all the radius need to reset to the initial radius
the region_lst param for single chromsome has only one value.
*/
function computeRegionRadian(){
	//track_radius_set[0] = radius + ideogramWidth;
	//cur_radius = radius+MCI/2;
	var showtotallen=ideogram_end-ideogram_start;
	
	region_lst=[{'chr':chr,'start':ideogram_start,'end':ideogram_end,'rgradian':0,'rgoffset':0}];
    $("#genome_region").css("display","");
	$("#chromid").removeAttr("disabled");
	$("#curpos").removeAttr("disabled");
		$("#chromid").removeAttr("readonly");
	$("#curpos").removeAttr("readonly");
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	
	var shownum = 1;
	if(showtotallen==0) return;
	sf = (Math.PI*2-spacing*shownum)/showtotallen; // radian per bp
    
	regionradianspan = sf*(ideogram_end-ideogram_start);
	//region_radian[0] = regionradianspan;
	region_lst[0].rgradian = regionradianspan;
	bpperpx = parseInt(showtotallen/((Math.PI*2-spacing*shownum)*radius));
	
	if(showtotallen > config.toomany_threshold){ //5 Mb
			$("#err").css("display","");
			ctx.clearRect(0, 0, canvas.width, canvas.height);	
	}else{
		$("#err").css("display","none");
	}	
}


//zoom in
//need to compute the showed region and the radian per pb and pb per pixle
function zoomin( istart, iend, fold){
		var range =  parseInt((iend - istart) / fold);

		if (range / 1000000 >= 10) {
			unit = 1000000;
		} else if (range / 10000 >= 10) {
			unit = 10000;
		} else if (range / 1000 >= 10) {
			unit = 1000;
		}

		ideogram_start = parseInt((iend + istart) / 2 - (iend - istart)
				/ (fold * 2));

		ideogram_end =  parseInt((iend + istart) / 2 + (iend - istart)
				/ (fold * 2));
				
		var scope_arry = validZoomScope(chr,ideogram_start,ideogram_end);
		ideogram_start = scope_arry[0];
		ideogram_end = scope_arry[1];
		
				
		computeRegionRadian();
		var cur_chr=$("#chromid").val();
	    ajax_getbandlst(config.ideogramJson,cur_chr);
}

//zoom out
function zoomout( istart, iend, fold){
	var range = parseInt((iend - istart) * fold);

	if (range / 1000000 >= 10) {
			unit = 1000000;
	} else if (range / 10000 >= 10) {
			unit = 10000;
	} else if (range / 1000 >= 10) {
			unit = 1000;
	}

	ideogram_start =  parseInt((istart+iend)/2 - (iend-istart)*fold/2 );
	if(ideogram_start <=0){
		ideogram_start = 1;
	}

	ideogram_end = parseInt((istart+iend)/2 + (iend-istart)*fold/2 );
	var scope_arry = validZoomScope(chr,ideogram_start,ideogram_end);
	ideogram_start = scope_arry[0];
	ideogram_end = scope_arry[1];
	
	
	computeRegionRadian();
	var cur_chr=$("#chromid").val();
	ajax_getbandlst(config.ideogramJson,cur_chr);
	
}

//this is used to identify the valid view scope of given chromosome
function validZoomScope(chrom,c_start,c_end){
	var v_end=c_end;
	var v_start=c_start;
	if(chrom_lst != null && chrom_lst.length >0 ){
		for(i=0;i< chrom_lst.length;i++){
			if( chrom == chrom_lst[i].chr){
				if(c_end > chrom_lst[i].end){
					v_end = chrom_lst[i].end;
				}
				if(c_start <chrom_lst[i].start ){
					v_start = chrom_lst[i].start;
				}
				break;
			}
			
		}
	}
	
	return [v_start,v_end];
	
}