var SELECTPEAK = null;

/*
*this is used to draw a peak
*we will use a line to connect one pair track
*two parameters of 3d model needed : binsize and start_bin
**************************************************/
function drawPeakTrack(trackobj){
	
	var storageclass = trackobj.storage;
	//current scope large than 20MB ( 20 fold), warn choose a small region
	var ajaxurl="";
	if(storageclass=="GFF3" || storageclass=="gff3"){
		ajaxurl="/circosweb/ajax/ajaxGetGPeakff3file.action";		
	}else if(storageclass=="mysql"){
		
		ajaxurl="/circosweb/ajax/ajaxGetGPeakFromTable.action";
	}
	
	
	var p_pos = $("#curpos").val();
	var file = trackobj.file;
	file=resolveURL(file);
	var params={"param1":file,"param2":p_pos,"binsize":trackobj.binsize};
	$.ajax({
				url : ajaxurl,
				type : 'post',
				dataType : 'json',
				data : params,
				async: false,
				success : function(data){
					//delete exitsed line
					if(data.gffList != null){
							if(data.gffList.length > 1000 ){
								$('<div></div>').appendTo('body')
							  .html('<div style=\"margin-top:10px;\">The number of interaction is '+data.gffList.length+' which is more than 1000<br/> Please choose a small region</div>')
							  .dialog({
								  modal: true, title: 'warn', zIndex: 10000, autoOpen: true,
								  width: 'auto', resizable: false,						 
								  close: function (event, ui) {
									  //remove from cookie
									
									  $(this).remove();
								  }
							});
							return;
								
								
							}else{
							if(cylinderMap[trackobj.key] != null && cylinderMap[trackobj.key].length>0){
								var cylinderarry = cylinderMap[trackobj.key] ;
								for(var mcindex =0 ;mcindex<cylinderarry.length;mcindex++){
									var cshape = cylinderarry[mcindex];
									if(cshape != null ){
										glviewer.removeShape(cshape);
									}
									
								}	
									glviewer.render();
									cylinderMap[trackobj.key]=[];										
							}else{
								cylinderMap[trackobj.key]=[];
							}
							cylinderMap["peakline"]=[];
						var atoms = cmodel.selectedAtoms({});
						
					
						for(j=0;j<data.gffList.length;j++){								
							var gffobj = data.gffList[j]; //each gff object
							if(gffobj != null ){
								var gffattr = gffobj.attributes;
								var gffnote = gffattr["Note"];
								if(gffnote != null){
									var noteval = gffnote[0]; //11:4600000-4650000|11:5000000-5050000  anchor|target
									
									//parse anchor and target
									var notearry = noteval.split("|");
									if(notearry != null && notearry.length == 2){
										var anchrindex = notearry[0].indexOf(":");
										var anchr_chrom = notearry[0].substring(0,anchrindex);
										var anpos = notearry[0].substring(anchrindex+1,notearry[0].length);
										var anchorarry = anpos.split("-");
										var tarchrindex = notearry[1].indexOf(":");
										var tar_chrom = notearry[1].substring(0,tarchrindex);
										if(anchr_chrom != tar_chrom){
										    continue;
										}
										
										var tarpos = notearry[1].substring(tarchrindex+1,notearry[1].length);
									
										var targetarry = tarpos.split("-");
										var anchoratomindex = -1;
										var targetatonindex = -1;
										
										var model_startbin = parseInt(start_bin);
										
										//get the atom index
										
										
										
										
										
										if(anchorarry != null && anchorarry.length == 2){
											var anstart = parseInt(anchorarry[0]);
											var anend = parseInt(anchorarry[1]);
											var anchorstart = (anstart+anend)/2;
											for(var m=0;m<atoms.length;m++){
											var atomobj = atoms[m];
											
											
											if(anchorstart >= atomobj.properties.start && anchorstart<= atomobj.properties.end ){
												anchoratomindex = m;
												break;
											}	
										   }
											
										}
										if(targetarry !=null && targetarry.length == 2){
											var tstart = parseInt(targetarry[0]);
											var tend = parseInt(targetarry[1]);
											var targetstart = (tstart+tend)/2;
											
											
											for(var m=0;m<atoms.length;m++){
												var atomobj = atoms[m];
												if(targetstart >= atomobj.properties.start && targetstart<= atomobj.properties.end ){
													targetatonindex = m;
													break;
												}	
											}
										}
										
									//	console.log("peak index = "+anchoratomindex+","+targetatonindex);
										
										if(anchoratomindex > -1 && targetatonindex > -1 && anchoratomindex != targetatonindex && anchoratomindex< atoms.length && targetatonindex< atoms.length){
											//begin draw line
										
											var beginatom = atoms[anchoratomindex];
											var b1 = new $3Dmol.Vector3(beginatom.x,beginatom.y,beginatom.z);
											var endatom = atoms[targetatonindex];
											var e1 = new $3Dmol.Vector3(endatom.x,endatom.y,endatom.z);
											
											if(glviewer != null){
												
												//add a transpancy cylinder
												var cylinder = glviewer.addCylinder({start: b1,end: e1,radius:0.01, tocap:true,color:"red",alpha:0.1,anchor:anpos,target:tarpos,clickable:true, callback: function(){	
												
													//interaction peak													
													//this.color='white';
													//this.tocap= false;
													//this.fromcap= true;
													this.alpha= 0.90;
													
													glviewer.render();	
																							   
													SELECTPEAK = this;
													var jump_pos = "";
													if(cylinderMap["peakline"] != null && cylinderMap["peakline"].length>0){
														var cylinderarry = cylinderMap["peakline"] ;
														for(var mcindex =0 ;mcindex<cylinderarry.length;mcindex++){
															var cpeakdata = cylinderarry[mcindex];
															
															var cshape = cpeakdata[0] ;
															if(cshape.shapePosition == this.shapePosition){
																jump_pos = cpeakdata[1];
																break;
															}
														}										
													}
													if(jump_pos.length>0){
														var peakval = splitPeakNote(jump_pos);
														var item_pos = peakval[0]+":"+peakval[1]+".."+peakval[2];
													
														//when genome view check,need to synchronized to genome view
														var checked = $("#gviewid");
														if(checked.prop("checked")){
																	
																var jumptracks = synchronizeAllToGenomeViewTracksName();
																var iframe = document.getElementById("gviewframeid");
																if (iframe) {
																	var iframeContent = (iframe.contentWindow || iframe.contentDocument);												 
																	iframeContent.RefreshGenomeView(item_pos,jumptracks);
																}					
																	// $("#gviewframeid").attr("src",jumpArry[0]);
														}														
													}
												}});
												
											    
												
												var peakdata=[cylinder,notearry];
												cylinderMap["peakline"].push(peakdata);
												
												cylinderMap[trackobj.key].push(cylinder);
												
												var lin = glviewer.addLine({start:b1,end:b1,linewidth:10,color:"red"});
												
												cylinderMap[trackobj.key].push(lin);
												var k=0;
												var t_linearry = computeDashLineArray(b1,e1);
												if(t_linearry!= null && t_linearry.length>0){
													
													for(k=0;k<t_linearry.length;k+=2){
														var t_vec = t_linearry[k] ;
													//	console.log("draw begin ="+t_vec.x+","+t_vec.y+","+t_vec.z);
														if(k==0){
															 lin.addLine({start:{x:b1.x,y:b1.y,z:b1.z},end:{x:t_vec.x,y:t_vec.y,z:t_vec.z}});
															
														}else{
															
															var t_vec1 = t_linearry[k-1];
															lin.addLine({start:{x:t_vec.x,y:t_vec.y,z:t_vec.z},end:{x:t_vec1.x,y:t_vec1.y,z:t_vec1.z}});
													
														}
														
													}
												 }
												var t_vec = t_linearry[t_linearry.length-1];
												if(t_vec != null ){
													 lin.addLine({start:t_vec,end:e1});
											
												}
											
												
											}
											
										}
										
									}
								}
								
							}																			
							
						}
						glviewer.render();
								
					    }
							

					}					  
				},
				error: function(){
					alert("get histone data error");
				}
		  });	
			
			
}


function uncheckDrawPeakTrack(trackobj){
	if(cylinderMap[trackobj.key] != null && cylinderMap[trackobj.key].length>0){
		var cylinderarry = cylinderMap[trackobj.key] ;
		for(var mcindex =0 ;mcindex<cylinderarry.length;mcindex++){
			var cshape = cylinderarry[mcindex];
			if(cshape != null ){
				glviewer.removeShape(cshape);
			}
			
		}	
			glviewer.render();
			cylinderMap[trackobj.key]=[];										
	}
	
}

/***
*this is used to draw TAD track
* surface package all the 3d model
*************************************************/
function drawTADSurfaceTrack(trackobj){
	var storageclass = trackobj.storage;
	
	
	
	if(storageclass=="GFF3" || storageclass=="gff3"){
			var p_pos = $("#curpos").val();
			var file = trackobj.file;
			file=resolveURL(file);
			var params={"param1":file,"param2":p_pos};
			$.ajax({
				url : '/circosweb/ajax/ajaxGetGff3file.action',
				type : 'post',
				dataType : 'json',
				data : params,
				async: false,
				success : function(data){
					if(data.gffList != null){
						
						if(data.gffList.length>200){
							//	var rangescope = pos_end - pos_start;
							//	var threshpeak = 12 * bin_size;
								//if(rangescope > threshpeak ){
									//popup window
							   $('<div></div>').appendTo('body')
														  .html('<div style=\"margin-top:10px;\">The number of TAD is '+data.gffList.length+', which is more than 200.<br/>Please choose a small region</div>')
														  .dialog({
															  modal: true, title: 'warn', zIndex: 10000, autoOpen: true,
															  width: 'auto', resizable: false,						 
															  close: function (event, ui) {
																  //remove from cookie
																
																  $(this).remove();
															  }
														});
									return;
									
								//}
							
							
						}else{
							removeTrackColorMap(trackobj.key);
							if(cylinderMap[trackobj.key] != null && cylinderMap[trackobj.key].length>0){
									var cylinderarry = cylinderMap[trackobj.key] ;
									for(var mcindex =0 ;mcindex<cylinderarry.length;mcindex++){
										var cshape = cylinderarry[mcindex];
										if(cshape != null ){
											glviewer.removeShape(cshape);
										}
										
									}	
										glviewer.render();
										cylinderMap[trackobj.key]=[];										
							}else{
									cylinderMap[trackobj.key]=[];
							}

							var atoms =cmodel.selectedAtoms({});	
							var countgff = -1;
							for(j=0;j<data.gffList.length;j++){	//tad 							
							var gffobj = data.gffList[j]; //each gff object
							if(gffobj != null ){
								countgff ++;
								var tadstart = parseInt(gffobj.start);
								var tadend = parseInt(gffobj.end);
								
								var drawcolorindex = j% 6;
								var drawcolor="white";
								if(countgff %2 ==0 ){
									drawcolor = "white";
								}else {
									drawcolor = "cyan";
								}
								
								//var drawcolor = colorMap[drawcolorindex];	
							
								
								ShowTADColorMap(trackobj.category,trackobj.key,gffobj.id,countgff,drawcolor);
								
								var startbin = parseInt(start_bin);
								
								if(tadstart >=0 && tadend > 0 ){
									
									/*var startatom = parseInt(tadstart/bin_size);
									startatom = startatom - startbin ;
									var endatom = parseInt(tadend/bin_size );
									endatom = endatom-startbin +1 ;
									var atomarry = [];
									var str = "";
									console.log("======startatom="+startatom+","+endatom+"=============");
									if(startatom<0 ||endatom < 0  ||startatom > atoms.length){
										continue;
									}
									if( endatom > atoms.length){
										endatom = atoms.length +1;
									}*/
									//find start atom index
									var startatom=0;
									var endatom=0;
									for(var m=0;m<atoms.length;m++){
										var atomobj = atoms[m];
										if(tadstart >= atomobj.properties.start && tadstart<= atomobj.properties.end ){
											startatom = m;
											break;
										}	
									}
									
									for(var m=0;m<atoms.length;m++){
										var atomobj = atoms[m];
										if(tadend >= atomobj.properties.start && tadend<= atomobj.properties.end ){
											endatom = m;
											break;
										}	
									}
									
									
									
									//find end atom index
									
									//console.log("=====start atom="+startatom+",endatom="+endatom+"==================");
									
									
									for(var i=startatom;i<endatom;i++){
										var beginatom = atoms[i];
										var b1 = null;
										if(beginatom != null && beginatom!== undefined){
											b1= new $3Dmol.Vector3(beginatom.x,beginatom.y,beginatom.z);
										}
										
										
										var nextatom = i+1;
										var b2 = null;
										
										if(nextatom <= endatom){
											var enda= atoms[nextatom];
											if(enda != null && enda !== undefined){
												b2 = new $3Dmol.Vector3(enda.x,enda.y,enda.z);	
											}
																					
										}									
										if(b2 != null && b1 != null){									
											var tsphere=glviewer.addSphere({center:b1,radius:0.04,color:drawcolor,alpha:0.6});
											var tcynlinder = glviewer.addCylinder({start: b1,end: b2,radius:0.04, tocap:true,color:drawcolor,alpha:0.6});
											cylinderMap[trackobj.key].push(tsphere);
											cylinderMap[trackobj.key].push(tcynlinder);
										}									
									}																		
									var beginatom = atoms[endatom];
									if(beginatom != null ){
										var b1 = new $3Dmol.Vector3(beginatom.x,beginatom.y,beginatom.z);
										var tsphere=glviewer.addSphere({center:b1,radius:0.04,color:drawcolor,alpha:0.6});
										cylinderMap[trackobj.key].push(tsphere);
									}
																		
								}								
							  }								
						}
						
					
						glviewer.render();
							
							
						}
						
						
						    
					}
				},
				error: function(){
					alert("get histone data error");
				}
		  });
	}		  
	
}


function uncheckDrawTADSurfaceTrack(trackobj){
	if(cylinderMap[trackobj.key] != null && cylinderMap[trackobj.key].length>0){
		var cylinderarry = cylinderMap[trackobj.key] ;
		for(var mcindex =0 ;mcindex<cylinderarry.length;mcindex++){
			var cshape = cylinderarry[mcindex];
			if(cshape != null ){
				glviewer.removeShape(cshape);
			}
			
		}	
			glviewer.render();
			cylinderMap[trackobj.key]=[];										
	}
	
}


// compute line equational between two point in 3d
// split 4 segment
function computeDashLineArray(start,end){
	var vector = [end.x-start.x,end.y-start.y,end.z-start.z];
	var lineAarry = new Array();
	var inter = 0.05;
	for(i=inter ;i<1;){
		var x = vector[0]*i+start.x;
		var y = vector[1]*i +start.y;
		var z = vector[2] *i +start.z;
		//console.log(x+","+y+","+z);
		var t_vec = new $3Dmol.Vector3(x,y,z);
		lineAarry.push(t_vec);		
		i+=inter;
	}
	return lineAarry;	
}


//this is used to split peak value
function splitPeakNote(notearry){
	var anchorstart=0;
	var targetstart=0;	
	var peak_chrom;
	if(notearry != null && notearry.length == 2){
		var anchrindex = notearry[0].indexOf(":");
		peak_chrom =  notearry[0].substring(0,anchrindex);
		var anpos = notearry[0].substring(anchrindex+1,notearry[0].length);
		var anchorarry = anpos.split("-");
		var tarchrindex = notearry[1].indexOf(":");
		var tarpos = notearry[1].substring(tarchrindex+1,notearry[1].length);
									
		var targetarry = tarpos.split("-");
		var anchoratomindex = -1;
		var targetatonindex = -1;
										
	
		if(anchorarry != null && anchorarry.length == 2){
			anchorstart = parseInt(anchorarry[0]);								
		}
		if(targetarry !=null && targetarry.length == 2){
			targetstart = parseInt(targetarry[1]);
		}
	
	}
	return [peak_chrom,anchorstart,targetstart];
}

	