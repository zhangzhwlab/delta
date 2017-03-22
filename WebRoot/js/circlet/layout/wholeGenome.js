
/*this used to draw whole genome picture,need to compute each chromosome radian and the offset angle
*we need to get the statistics data of the whole genome
*when user view scope become small,then another statics data will show
*/
function draw_wholeGenome(sel_chrom_lst){
	$("#chromid").prop("disabled",'disabled');
	$("#curpos").prop("disabled",'disabled');
	$("#chromid").prop("readonly",'readonly');
	$("#curpos").prop("readonly",'readonly');
	region_lst =  sel_chrom_lst;
	show_mode =2;
	//set cookies
	Cookies.set("view_mode",show_mode);	
	$("#genome_region").css("display","none");
	compute_genomeregion(region_lst);
}


/****for multiple chromosomes, region_lst region have mutiple values
********************************/
function compute_genomeregion(regionlst){
		ctx.clearRect(0, 0, canvas.width, canvas.height);
		if(regionlst !=undefined || regionlst !=null ){
		//compute radian
		var shownum =0;
		totalbp =0;
		for(i=0;i<regionlst.length;i++){
			totalbp += regionlst[i].end - regionlst[i].start;
			shownum ++;
		}	
		
		if(totalbp == 0){
			return ;
		}
		sf = (Math.PI*2-spacing*shownum)/totalbp;
		
		for(i=0;i<regionlst.length;i++){
			//region_radian[i] =  sf*(regionlst[i].end-regionlst[i].start) ;
			regionlst[i].rgradian = sf*(regionlst[i].end-regionlst[i].start) ;
			track_radius_set[i] = radius + ideogramWidth;
		}
		bpperpx=parseInt(totalbp/((Math.PI*2-spacing*shownum)*radius));

		bandlst=null;
		//if shownum ==1
		if(shownum == 1){ // one single chrom,then show the genome ideogram
			$("#genome_region").css("display","block");
			var cur_chr=$("#chromid").val();
			ajax_getbandlst(config.ideogramJson,cur_chr); 
		}else{ // multiple chroms,only show circlet ideogram
			$("#genome_region").css("display","none");
			ajax_get_allchr_bandlst(config.ideogramJson,regionlst);
			//showlayout();
		}
	
		reDrawAllSelectedTrack();		
	}	
}


/***************************************
*the whole genome zoom in 
*/
function genome_zoomin(fold){
	if(region_lst !=undefined || region_lst !=null ){
		for(i=0;i<region_lst.length;i++){
			var iend = region_lst[i].end;
			var istart = region_lst[i].start;
			
			var z_start = parseInt((iend + istart) / 2 - (iend - istart)
				/ (fold * 2));

		    var z_end =  parseInt((iend + istart) / 2 + (iend - istart)
				/ (fold * 2));
			region_lst[i].end = z_end;
			region_lst[i].start = z_start;
			
		}
		
	}
	compute_genomeregion(region_lst);

	
}


/**********************************************
*the whole genome zoom out
*/
function genome_zoomout(fold){
	if(region_lst !=undefined ||region_lst != null){
		for(i=0;i<region_lst.length;i++){
			var iend = region_lst[i].end;
			var istart = region_lst[i].start;
			
			var z_start =  parseInt((istart+iend)/2 - (iend-istart)*fold/2 );
			if(z_start <=0){
				z_start = 1;
			}

			var z_end = parseInt((istart+iend)/2 + (iend-istart)*fold/2 );

			region_lst[i].end = z_end;
			region_lst[i].start = z_start;
			
		}
		
		
	}
	compute_genomeregion(region_lst);

	
}