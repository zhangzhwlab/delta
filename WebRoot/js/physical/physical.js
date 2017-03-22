
//when users checked show genome view, this will be called
function showGenomeView(){
			var checked = $("#gviewid");
			if(checked.prop("checked")){
				mylayout.show('east');
				mylayout.sizePane("west", "20%");
				mylayout.sizePane("east", "35%");
				mylayout.sizePane("center", "35%");
			
				$("#genomeviewid").css("display","block");
				
				var p_pos = $("#curpos").val(); 
								
				var conf = getQueryString("conf");							
				var jumptrack="";
				
				$("#trackid input[type='checkbox']").each(function(){				
							if ($(this).is(":checked")) {
								var checkval = $(this).val();
								if(checkval !== undefined && checkval.length>0 && checkval.indexOf(",")> -1){
									
									var arrys = checkval.split(",");					
									if(arrys[1].indexOf('3dmodel') < 0) {
										if(arrys[1] != 'ensembl_gene' && arrys[1].indexOf("TAD") <0 && arrys[1].indexOf("Interaction") <0 ){
											jumptrack += arrys[1]+"_signal," ;	
										}else{
											jumptrack += arrys[1]+"," ;	
										}
															
									}
								}
								
							}
				});
				if(jumptrack.length > 0){
					jumptrack = jumptrack.substring(0,jumptrack.length-1);	
				}
				
			
				if(conf != null){
				   var ghrefurl="http://"+window.location.host+"/jbrowse/index.html?showPhysical=1&data="+conf+"&nav=1&overview=0&menu=0&loc="+p_pos+"&notGotoPhysical=1&showembedTopo=1";
					
					ghrefurl+="&tracks="+jumptrack;
					
				
				
					$("#gviewframeid").attr("src",ghrefurl);
				}else{
					var ghrefurl="http://"+window.location.host+"/jbrowse/index.html?showPhysical=1&data=human&nav=1&overview=0&menu=0&loc="+p_pos+"&notGotoPhysical=1&showembedTopo=1";
					
					ghrefurl+="&tracks="+jumptrack;
					
	
					$("#gviewframeid").attr("src",ghrefurl);
				}
			
				//set cookie
				addTrackToCookie("genomeview");
				
			
			}else{
				mylayout.hide('east');
				
				$("#genomeviewid").css("display","none");
				$("#genepanelid").css("padding-top","800px");
				deleteTrackFromCookie("genomeview");
			}		
}


//when check on one track,we need to synchronized to genome view
function synchronizeAllToGenomeView(tchr,tstart,tend){
	var jumptracks="";
	$("#trackid input[type='checkbox']").each(function(){				
				if ($(this).is(":checked")) {
					var checkval = $(this).val();
					var arrys = checkval.split(",");					
					if(arrys[1].indexOf('3dmodel') < 0) {	//	arrys[1] != '3dmodel'			
						if(arrys[1] != 'ensembl_gene'){
							jumptracks += arrys[1]+"_signal," ;	
						}else{
							jumptracks += arrys[1]+"," ;	
						}						
					}
				}
	});
	if(jumptracks.length > 0){
		jumptracks = jumptracks.substring(0,jumptracks.length-1);
	}
	
	return getJumpLinkWithTrack(tchr,tstart,tend,jumptracks);
	
}



//when check on one track,we need to synchronized to genome view
function synchronizeAllToGenomeViewTracksName(){
	var jumptracks="";
	$("#trackid input[type='checkbox']").each(function(){				
				if ($(this).is(":checked")) {
					var checkval = $(this).val();
					if(checkval != "on" ){
						if(checkval.indexOf(",") > -1 ){
							var arrys = checkval.split(",");					
							if(arrys[1].indexOf('3dmodel') < 0 ) { //arrys[1] != '3dmodel'			
								if(arrys[1] != 'ensembl_gene'){
									jumptracks += arrys[1]+"_signal," ;	
								}else{
									jumptracks += arrys[1]+"," ;	
								}						
							}
							
						}
						
					}
					
				}
	});
	if(jumptracks.length > 0){
		jumptracks = jumptracks.substring(0,jumptracks.length-1);
	}
	return jumptracks;
	
	
}



// jump link to genome view with track
function getJumpLinkWithTrack(chr,start,end,jumptrack){
	var conf = getQueryString("conf");
	var ghrefurl;
	if(conf == null){
			ghrefurl="http://"+window.location.host+"/jbrowse/index.html?showPhysical=1&data=human&loc="+chr+"%3A"+start+".."+end;			
			ghrefurl+="&tracks="+jumptrack;
						
	}else{
			ghrefurl="http://"+window.location.host+"/jbrowse/index.html?showPhysical=1&data="+conf+"&loc="+chr+"%3A"+start+".."+end;			
			ghrefurl+="&tracks="+jumptrack;
						
	}
	return ghrefurl;
}

//jump link without track name, this will be used when uncheck one track
function getJumpLinkWithoutTrack(chr,start,end,curtrack){
	
	//remove given track from current
				
		var jumptrack="";
		$("#trackid input[type='checkbox']").each(function(){				
					if ($(this).is(":checked")) {
						var checkval = $(this).val();
						var arrys = checkval.split(",");					
						if(arrys[1].indexOf('3dmodel') < 0) {	//arrys[1] != '3dmodel'
							if(arrys[1] != 'ensembl_gene'){
								if(arrys[1] !=curtrack ){
									jumptrack += arrys[1]+"_signal," ;	
								}								
							}else{
								jumptrack += arrys[1]+"," ;	
							}						
						}
					}
		});
		if(jumptrack.length>0){
			jumptrack = jumptrack.substring(0,jumptrack.length-1);
		}
		
		
		
		var conf = getQueryString("conf");	
		if(conf == null){
			ghrefurl="http://"+window.location.host+"/jbrowse/index.html?showPhysical=1&data=human&loc="+chr+"%3A"+start+".."+end;
			
			ghrefurl+="&tracks="+jumptrack;
						
		}else{
			ghrefurl="http://"+window.location.host+"/jbrowse/index.html?showPhysical=1&data="+conf+"&loc="+chr+"%3A"+start+".."+end;
			
			ghrefurl+="&tracks="+jumptrack;
						
		}
	console.log(ghrefurl);
	
	return ghrefurl;
}

//jump link to genome view and circlet view
function getJumpLink(chr,start,end){
		var conf = getQueryString("conf");
		//we need to get the selected tracks from physical view itself
		
		var jumptrack="";
		$("#trackid input[type='checkbox']").each(function(){				
					if ($(this).is(":checked")) {
						var checkval = $(this).val();
						var arrys = checkval.split(",");					
						if(arrys[1].indexOf('3dmodel') < 0) { //arrys[1] != '3dmodel'

							if(arrys[1] != 'ensembl_gene'){
								jumptrack += arrys[1]+"_signal," ;	
							}else{
								jumptrack += arrys[1]+"," ;	
							}												
						}
					}
		});
		if(jumptrack.length>0){
			jumptrack = jumptrack.substring(0,jumptrack.length-1);
		}
		
		var ghrefurl,threfurl;
		if(conf == null){
			ghrefurl="http://"+window.location.host+"/jbrowse/index.html?showPhysical=1&data=human&loc="+chr+"%3A"+start+".."+end;			
			ghrefurl+="&tracks="+jumptrack;
			
			threfurl="http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?loc="+chr+"%3A"+start+".."+end;
			if(jumptrack.length >0){
				threfurl+="&tracks=Interaction,"+jumptrack;
			}
		}else{
			ghrefurl="http://"+window.location.host+"/jbrowse/index.html?showPhysical=1&data="+conf+"&loc="+chr+"%3A"+start+".."+end;			
			ghrefurl+="&tracks="+jumptrack;
			
			
		
			threfurl="http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?conf="+conf+"&loc="+chr+"%3A"+start+".."+end;
			if(jumptrack.length >0){
				threfurl+="&tracks=Interaction,"+jumptrack;
			}
						
		}
				
		return [ghrefurl,threfurl];
		
}


//open genome view in a new window
function gotoGenome(){
	var p_pos = $("#curpos").val(); 
	var conf = getQueryString("conf");
	var ghrefurl;
	
	var jumptrack="";
		$("#trackid input[type='checkbox']").each(function(){				
					if ($(this).is(":checked")) {
						var checkval = $(this).val();
						var arrys = checkval.split(",");					
						if(arrys[1].indexOf('3dmodel') < 0) { //arrys[1] != '3dmodel'

							if(arrys[1] != 'ensembl_gene'){
								jumptrack += arrys[1]+"_signal," ;	
							}else{
								jumptrack += arrys[1]+"," ;	
							}												
						}
					}
		});
		if(jumptrack.length>0){
			jumptrack = jumptrack.substring(0,jumptrack.length-1);
		}
	
	
	if(conf != null){
	   ghrefurl="http://"+window.location.host+"/jbrowse/index.html?data="+conf+"&nav=1&overview=0&menu=0&loc="+p_pos+"&tracks="+jumptrack;					
	}else{
	   ghrefurl="http://"+window.location.host+"/jbrowse/index.html?data=human&nav=1&overview=0&menu=0&loc="+p_pos+"&tracks="+jumptrack;		
	}
	var a = document.getElementById("goto");
	if(a == null){
		$('body').append('<a href="" id="goto" target="_blank"></a>');
	}

	$('#goto').attr('href', ghrefurl);
	$('#goto').get(0).click();

}


//open topological view in a new window
function gotoTopoView(){
	clearCookies("topo");
	var jumptrack="";
		$("#trackid input[type='checkbox']").each(function(){				
					if ($(this).is(":checked")) {
						var checkval = $(this).val();
						var arrys = checkval.split(",");					
						if(arrys[1] != '3dmodel') {
							jumptrack += arrys[1]+"," ;																	
						}
					}
		});
		if(jumptrack.length>0){
			jumptrack = jumptrack.substring(0,jumptrack.length-1);
		}
	
	
	
	var p_pos = $("#curpos").val(); 
	var conf = getQueryString("conf");
	
	var threfurl;
	if(conf == null){			
		threfurl="http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?loc="+p_pos;			
		threfurl+="&tracks="+jumptrack;			
	}else{		
		threfurl="http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?conf="+conf+"&loc="+p_pos;
		threfurl+="&tracks="+jumptrack;			
	}
	var a = document.getElementById("goto");
	if(a == null){
		$('body').append('<a href="" id="goto" target="_blank"></a>');
	}
	

	$('#goto').attr('href', threfurl);
	$('#goto').get(0).click();
	
}

//save the whole canvas image as pdf
function savePageAs(){
	if(glviewer != null ){
		var exportcanvas = glviewer.get3dmodelCanvas();
		
		var imgWidth = 210; 
		var pageHeight = 295;  
		var imgHeight = exportcanvas.height * imgWidth / exportcanvas.width;
		var heightLeft = imgHeight;

		
		var pdf = new jsPDF('p', 'mm');
		var imgData = exportcanvas.toDataURL('image/png');

		// due to lack of documentation; try setting w/h based on unit
		pdf.addImage(imgData,'PNG',0,0,imgWidth,imgHeight); 
		
		/*
		heightLeft -= pageHeight;

		  while (heightLeft >= 0) {
			position = heightLeft - imgHeight;
			doc.addPage();
			doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
			heightLeft -= pageHeight;
		  }
		*/
		
		pdf.save('image.pdf'); //the generated pdf that contains the image gets trimmed
		
		
	}
	
}


function clearCookies(rpath){
	
	Cookies.remove(rpath+'_track', { path: rpath });
	Cookies.remove(rpath+'_dataset', { path: rpath });
	Cookies.remove(rpath+'_position', { path: rpath });
	
}

function refereshEmbedTopology(conf,loc,defaultTracks){
	var threfurl="";
	 if(conf == null){			
		threfurl="http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?loc="+loc+"&menu=0&showPhysical=1&notGotoPhysical=1&embedGenome=1";			
		threfurl+="&tracks="+defaultTracks;			
	}else{		
		threfurl="http://"+window.location.host+"/pages/visualization/topo_viewm.jsp?conf="+conf+"&loc="+loc+"&menu=0&showPhysical=1&notGotoPhysical=1&embedGenome=1";
		threfurl+="&tracks="+defaultTracks;			
	}
	$("#gviewframeid").attr("src",threfurl);
}


//refresh iframe from circlet view
function refereshEmbedGenome(conf,loc,defaultTracks){
	if(conf != null){
				   var ghrefurl="http://"+window.location.host+"/jbrowse/index.html?showPhysical=1&data="+conf+"&nav=1&overview=0&menu=0&loc="+loc+"&notGotoPhysical=1&showembedTopo=1";
					
					ghrefurl+="&tracks="+defaultTracks;
					
					$("#gviewframeid").attr("src",ghrefurl);
				}else{
					var ghrefurl="http://"+window.location.host+"/jbrowse/index.html?showPhysical=1&data=human&nav=1&overview=0&menu=0&loc="+loc+"&notGotoPhysical=1&showembedTopo=1";
					
					ghrefurl+="&tracks="+defaultTracks;
					
	
					$("#gviewframeid").attr("src",ghrefurl);
				}
	
}




