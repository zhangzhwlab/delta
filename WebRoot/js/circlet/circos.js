var bandlst;//ideogram band color
var bandlstmap={}; // bandlistmap , store chromosome ideogram data

var pos_start;
var pos_end;
var chr;
var radius=150;
var MCI=40; // 80
var LAYERSMCI=190; // track such as gene, need to hirearchy,have several layers 60

var ideogramWidth=14;	
var spacing=0.015;
var canvas_width=800;
var canvas_height=800;
var track_radius_set=[];

var cur_radius=radius+30;  // the current drawn radius
var ideogram_start;
var ideogram_end;
var sf; // radian per bp
var bpperpx; // bp per px
var regionradianspan;
var unit;
var ideoHeight = 14, // for plotting ideogram
cbarHeight = 9,
browser_scalebar_height=27;
var totalbp=159138663;
var selectedTrack=[];// this used to store the selected track's key,begin_radius,track height

var show_mode = 1; //if single chrom then 1,if whole genome,then 2
//&tracklist=0 reprent do not need left navibar

var cytoBandColor = [
	255, // gneg
	180, // gpos25
	120, // gpos50
	60, // gpos75
	0, // gpos100
	0, // gvar
	180, // stalk
	142, // gpos33
	57 // gpos66
	];
var colorCentral = {
// the longlst can be altered by user, a copy will be made for default setting
foreground:'#000000',
fg_r:0, fg_g:0, fg_b:0,
foregroundDim:'#ccc',
background:'#ffffff',
bg_faint:'rgba(100,100,100,.1)',
//pagebg:'rgba(217,217,206,0.7)',
pagebg:'#f1ede8',
iconbackground: "#AA839C", // light purple, manual button bg color
iconfill:'#956584',
tkentryfill:'rgba(204,204,255,0.5)',
};
var cytoWordColor = [0,0,255,255,255,255,0,255,255];
var centromereColor = "rgb(141,64,52)";
var ntbcolor = {g:'#3899c7', c:'#e05144', t:'#9238c7', a:'#89c738', n:'#858585'};

var navigator={"canvas":"null","chrbarheight":"14","rulerheight":"20","hlspacing":"2"};

var gflag = {
allow_packhide_tkdata:false,
browser:undefined
};

var outergflag ={
	outermove: null
} ;

var config={
	toomany_threshold:10000000,
	ideogramJson:null,
	interactionConfig:null,
	histoneConfig:null,
	geneTabixConfig:null
};

var interacionData;
var histoneData;

var canvas ;
var ctx ;	



var genome_canvas;
var glasspane ; // glasspane
var indicator ; //indicator along genome


//whole genome region_lst
var chrom_lst=[];

var region_lst=[];
var bararry=[]; // multiple bar store

var cachetrackpos={};//store cache track position
//var region_radian=[]; //arc length
//var region_offset=[]; // angle offset

var menu ;
var menuState=0;
var activeClassName = "right-menu--active";
var menuPosition;
var menuPositionX;
var menuPositionY;



var matrix=[1,0,0,1,0,0];

function loadLayout(){//ideogramwidth:14,

	var mylayout = $('#container').layout(
		   {
		panes: {
		   resizable:true,
		   slidable:true,
		   PaneResizing:true,
		   spacing_open:6,
			spacing_closed :6}
		
		  }
   
   );
	mylayout.sizePane("west", "18%");
	mylayout.sizePane("center", "72%");	
	
	var ishowtracklist = getQueryString("tracklist") ;
	ishowtracklist = parseInt(ishowtracklist);
	if(ishowtracklist == 0){
		mylayout.hide('west');
		
	}
	
	

	$("#chrompageid").load("/circosweb/pages/visualization/circos/chrom.jsp");
	$("#configpageid").load("/circosweb/pages/visualization/circos/config.jsp");
	$("#utrackid").load("/circosweb/pages/visualization/circos/uploadtrack.jsp");
	
	$("#windowsize-vertical").slider({
      orientation: "vertical",
      range: "min",
      min: 800,
	  step: 50,
      max: 1800,
	  showLabels:true,
	  showScale:true,
      value: 800,
      slide: function( event, ui ) {
	  $( "#windownamount" ).val(ui.value);
		$( "#windowsize-vertical" ).find("a").css("font-size","12px").text(ui.value);				
      },
	  stop: function( event, ui ) {
		var a = ui.value;
		 var b = ui.value;
		 var c = $("#radiusamount").val();
		 //delete line canvas
		 var track_showline = document.getElementById("track_showline");
		 if(track_showline!=null){
			 track_showline.remove();
		 }
		
		 
		 window.parent.config_setConfig(a,b,c);
	  }
    });
	var windowlabel = "<p style='font-size:10px;padding-top:0px;margin-left:0px;padding-left:10px;'>1800</p>";
	$("#windowsize-vertical").append(windowlabel);
	windowlabel = "<p style='font-size:10px;padding-top:170px;margin-left:0px;padding-left:15px;'>800</p>";
	$("#windowsize-vertical").append(windowlabel);
	windowlabel = "<p style='font-size:10px;padding-top:5px;margin-left:0px;padding-left:0px;'>Window</p>";
	$("#windowsize-vertical").append(windowlabel);
	
	
	$( "#windownamount" ).val( $( "#windowsize-vertical" ).slider( "value" ) );
	$( "#windowsize-vertical" ).find("a").css("font-size","12px").text(800 );
	$( "#radiussize-vertical" ).slider({
      orientation: "vertical",
      range: "min",
      min: 20,
	  step: 10,
      max: 200,
      value: 150,
      slide: function( event, ui ) {
		  $( "#radiusamount" ).val(ui.value);
		$( "#radiussize-vertical" ).find("a").css("font-size","12px").text(ui.value);		
      },
	  stop: function( event, ui ) {
		 var a = $("#windownamount").val();
		 var b = $("#windownamount").val();
		 var c = ui.value;
		 window.parent.config_setConfig(a,b,c);
	  }
    });
	windowlabel = "<p style='font-size:10px;padding-top:0px;margin-left:0px;padding-left:13px;'>200</p>";
	$("#radiussize-vertical").append(windowlabel);
	windowlabel = "<p style='font-size:10px;padding-top:70px;margin-left:0px;padding-left:15px;'>20</p>";
	$("#radiussize-vertical").append(windowlabel);
	windowlabel = "<p style='font-size:10px;padding-top:5px;margin-left:0px;padding-left:0px;'>Radius</p>";
	$("#radiussize-vertical").append(windowlabel);
	
	$( "#radiusamount" ).val( $( "#radiussize-vertical" ).slider( "value" ) );
	$( "#radiussize-vertical" ).find("a").css("font-size","12px").text(150);		
	canvas = document.getElementById("canvas");
	ctx = canvas.getContext('2d');	
	canvas.addEventListener('mousedown',circlet_view_make,false); //add canvas mouse move event along ideogram

	//mouse move to show the track label
	canvas.addEventListener("mousemove",extend_canvas_mouse_move,false);
	
	
	canvas.addEventListener("contextmenu",extend_canvas_cxtmenu_event,false);
	
	canvas.addEventListener("click",function(e){
		 //console.log("mouse click");
		 canvas.addEventListener('mousemove', extend_canvas_mouse_move, false);
		 canvas.removeEventListener('contextmenu', extend_canvas_cxtmenu_event, false);
		 $("#contextmenu_text_share").css("display","none");
	},false);
	
	
	genome_canvas = document.getElementById("genome");
	genome_canvas.addEventListener('mousedown',horizon_ideogram_make,false);
	
	glasspane = document.getElementById("glasspane"); // glasspane
	indicator = document.getElementById("choose_indicator"); //indicator along genome
	loadInitCircos();
	$("#err").css("display","none");	
	
	var isshowmenu = getQueryString("menu") ;
	isshowmenu = parseInt(isshowmenu);
	if(isshowmenu ==0){
		$("#logo").css("display","none");
		$("#menu").css("display","none");
		$("#idfooter_content").css("display","none");
	}
	
	var synphysical = getQueryString("showPhysical") ;
	if(synphysical == 1){
		document.getElementById("idshowpbtn").style.display="";
	}else{
		document.getElementById("idshowpbtn").style.display="none";
		
	}
	
	var notgotophysical = getQueryString("notGotoPhysical") ; 
	if(notgotophysical == 1){
		document.getElementById("idGotoOutPhysical").style.display="none";
	}else{
		document.getElementById("idGotoOutPhysical").style.display="";
		
	}
	
	var embedgenome = getQueryString("embedGenome") ; 
	if(embedgenome == 1){
		document.getElementById("idEmbedGenome").style.display="";
		document.getElementById("idOutlinkGenome").style.display="none";
	}else{
		document.getElementById("idEmbedGenome").style.display="none";
		document.getElementById("idOutlinkGenome").style.display="";
	}
	
}

/*this used to draw a genome picture using band and the choosed scope will be showed upon the picture by a blue box
*a single chromosome will be drawn
*/
function draw_genome(){

	var c=genome_canvas;
	//"chrbarheight":"14","rulerheight":"20","hlspacing":"2"
	//canvas.height=14+2*2+2+20;

	var g_ctx=genome_canvas.getContext('2d');
	g_ctx.clearRect(0,0,c.width, c.height);

	var ctotalbp = 0 ;
	for(i=0;i<chrom_lst.length;i++){
		var chromdata = chrom_lst[i];
		if(chr == chromdata.chr){
			ctotalbp = chromdata.end - chromdata.start;
			break;
		}		
	}
		
	var x=0;
	var lastchrxoffset=0, firstchrwidth=0, lastchrwidth=0;
	var imagewidth=c.width;
	
	for(i=0; i<region_lst.length; i++) {
		var chrlen=ctotalbp;
		var w=chrlen*(imagewidth-region_lst.length-1)/ctotalbp;
		if(i==0) firstchrwidth=w;
		if(i==region_lst.length-1) lastchrwidth=w;
		lastchrxoffset=x+1;
		drawGenomeIdeogram(
			getideogrambanddata(region_lst[i].chr, 0, ctotalbp, w),
			g_ctx, x, 2+1, w, ideoHeight, false); //getcytoband4region2plot
		x+=w+1;
	}
	// blue box
	
	var coord=ideogram_start;
//	console.log(coord+","+firstchrwidth+","+ctotalbp);
	var hlstart=Math.ceil(coord*firstchrwidth/ctotalbp);
	
	//r=this.regionLst[this.dspBoundary.vstopr];
	coord=ideogram_end;
	
	var ttmp_end =Math.ceil( lastchrxoffset+coord*lastchrwidth/ctotalbp);
	var hlstop=Math.max(hlstart+1,ttmp_end);
	
	g_ctx.strokeStyle='blue';
	
	g_ctx.strokeRect(Math.max(0,hlstart-.5), .5, hlstop-hlstart,14+2*2+1);
	
	// ruler
	/*if(_n.show_ruler && chrlst.length==1 && _n.rulerheight>0) {
		var chrlen=this.genome.scaffold.len[chrlst[0]];
		ctx.fillStyle=colorCentral.foreground_faint_5;
		drawRuler_basepair(ctx, chrlen, imagewidth, 0, _n.chrbarheight+2*_n.hlspacing+4);
	}*/
}

function getideogrambanddata(chrom, cstart, cstop, plotwidth)
{
/* given a query region, find cytoband data in it
for each band returns [name, plot length (pixel), coloridx, athead(bool), attail(bool)]
plotwidth: on screen width of this interval
 */
//if(!(chrom in this.cytoband)) return chrom;
var sf=plotwidth/(cstop-cstart);
var result = [];
var elen=ideogram_end;
if(Object.keys(bandlstmap).length > 0  ){
	var t_bandlst = bandlstmap[chrom];
	for(var i=0; i<t_bandlst.cytoband.length; i++) {
	var b = t_bandlst.cytoband[i];
	if(Math.max(cstart, b[1]) < Math.min(cstop, b[2])) {
		var thisstart = Math.max(cstart, b[1]);
		var thisstop = Math.min(cstop, b[2]);
		result.push([b[4], (thisstop-thisstart)*sf, b[3], thisstart==0, thisstop==elen]);
	}
}
	
}

return result;
}

//draw genome nevigator
function drawGenomeIdeogram(data, gctx, x, y, plotwidth, plotheight, tosvg) //drawIdeogramSegment_simple
{
/* only draws data within a region
args:
data: getideogrambanddata() output
x/y: starting plot position on canvas, must be integer
plotwidth: entire plotting width, only used to draw the blank rectangle
 */
gctx.font = "bold 8pt Sans-serif";
var mintextheight=13;
if(typeof(data)=='string') {
	// no cytoband data
	//var svgdata=[];
	gctx.strokeStyle = colorCentral.foreground;
	gctx.strokeRect(x,y+0.5,plotwidth,plotheight);
	//if(tosvg) svgdata.push({type:svgt_rect,x:x,y:y+.5,w:plotwidth,h:plotheight,stroke:gctx.strokeStyle});
	gctx.fillStyle = colorCentral.foreground;
	var s=data; // is chrom name
	var w = gctx.measureText(s).width;
	if(w<=plotwidth && plotheight>=mintextheight) {
		var y2=y+10+(plotheight-mintextheight)/2;
		gctx.fillText(s, x+(plotwidth-w)/2, y2);
		//if(tosvg) svgdata.push({type:svgt_text,x:x+(plotwidth-w)/2,y:y2,text:s,bold:true});
	}
	//return svgdata;
}
//var svgdata=[];
var previousIsCentromere = null;
for(var i=0; i<data.length; i++) {
	var band = data[i];
//	alert(band);
	if(band[2] >= 0) {
		
		gctx.fillStyle = 'rgb('+cytoBandColor[band[2]]+','+cytoBandColor[band[2]]+','+cytoBandColor[band[2]]+')';
		gctx.fillRect(x, y, band[1], plotheight);
		//alert(x+","+y+","+band[1]+","+plotheight);
		//if(tosvg) svgdata.push({type:svgt_rect,x:x,y:y,w:band[1],h:plotheight,fill:gctx.fillStyle});
		gctx.strokeStyle = colorCentral.foreground;
		gctx.beginPath();
		gctx.moveTo(x,0.5+y);
		gctx.lineTo(x+band[1],0.5+y);
		gctx.moveTo(x,plotheight-0.5+y);
		gctx.lineTo(x+band[1],plotheight-0.5+y);
		gctx.stroke();
		//if(tosvg) {
		//	svgdata.push({type:svgt_line,x1:x,y1:y+.5,x2:x+band[1],y2:y+.5});
		//	svgdata.push({type:svgt_line,x1:x,y1:plotheight-0.5+y,x2:x+band[1],y2:plotheight-0.5+y});
		//}
		var w = gctx.measureText(band[0]).width;
		if(w < band[1] && plotheight>=mintextheight) {
			gctx.fillStyle = 'rgb('+cytoWordColor[band[2]]+','+cytoWordColor[band[2]]+','+cytoWordColor[band[2]]+')';
			var y2=y+10+(plotheight-mintextheight)/2;
			gctx.fillText(band[0], x+(band[1]-w)/2, y2);
			//if(tosvg) svgdata.push({type:svgt_text,x:x+(band[1]-w)/2,y:y2,text:band[0],color:gctx.fillStyle,bold:true});
		}
		if(previousIsCentromere==true) {
			gctx.fillStyle = colorCentral.foreground;
			gctx.fillRect(x, y, 1, plotheight);
			//if(tosvg) svgdata.push({type:svgt_line,x1:x,y1:y,x2:x,y2:y+plotheight});
		}
		previousIsCentromere=false;
	} else {
		gctx.fillStyle = centromereColor;
		gctx.fillRect(x, 3+y, band[1], plotheight-5);
		//if(tosvg) svgdata.push({type:svgt_rect,x:x,y:y+3,w:band[1],h:plotheight-5,fill:gctx.fillStyle});
		var w = gctx.measureText('centromere').width;
		if(w < band[1]) {
			gctx.fillStyle = 'white';
			gctx.fillText('centromere', x+(band[1]-w)/2, 10+y);
		//	if(tosvg) svgdata.push({type:svgt_text,x:x+(band[1]-w)/2,y:y+10,color:gctx.fillStyle,text:'centromere',bold:true});
		}
		if(previousIsCentromere==false) {
			gctx.fillStyle = colorCentral.foreground;
			gctx.fillRect(x-1, y, 1, plotheight);
		//	if(tosvg) svgdata.push({type:svgt_line,x1:x-1,y1:y,x2:x-1,y2:y+plotheight});
		}
		previousIsCentromere=true;
	}
	if(band[3]) {
		// enclose head
		gctx.fillStyle = colorCentral.foreground;
		gctx.fillRect(x, y, 1, plotheight);
	//	if(tosvg) svgdata.push({type:svgt_line,x1:x,y1:y,x2:x,y2:y+plotheight});
	}
	if(band[4]) {
		// enclose tail
		gctx.fillStyle = colorCentral.foreground;
		gctx.fillRect(x+band[1], y, 1, plotheight);
	//	if(tosvg) svgdata.push({type:svgt_line,x1:x+band[1],y1:y,x2:x+band[1],y2:y+plotheight});
	}
	x += band[1];
}
//return svgdata;
}

//show unit and ideogram in one single chromosome
function showlayout(){

	var circlespacing=3; //spacing between circles
	var labelheight=50; //radius increment by the labels
	/*var wreathheight=0; //sum of wreath track height
	var wreathlst=[];
	for(i=0; i<wreathlst.length; i++)
			wreathheight+=wreathlst[i].qtc.height;
	if(wreathheight>0)
			wreathheight+=circlespacing;*/
	//wreath height is not used here
	canvas.width= canvas_width;//according to the track number compute the canvas width
	canvas.height= canvas_height;
	var centerx=parseInt(canvas.width/2);
	var centery=parseInt(canvas.height/2);
	var i=10; // 10bp

	
	//each chromosome need to get the  band lst
	var r=0;
	//this used to get cytoband data
	var radiusbase = radius;
		
	var cytoBandColor = [
	255, // gneg
	180, // gpos25
	120, // gpos50
	60, // gpos75
	0, // gpos100
	0, // gvar
	180, // stalk
	142, // gpos33
	57 // gpos66
	];
	var centromereColor = "rgb(141,64,52)";
	var bandlstmap_length = Object.keys(bandlstmap).length;
	for(i=0;i<region_lst.length;i++){
		
		//region_offset[i] = r;
		region_lst[i].rgoffset = r;
		
		var rr= region_lst[i].rgradian;
	
		
		if(bandlstmap_length <= 0 ){ //no band data
			ctx.lineWidth = ideogramWidth;
			ctx.strokeStyle='rgba(0,0,0,0.5)';
			ctx.beginPath();
			ctx.arc(centerx,centery,radiusbase,r,r+rr,false);
			ctx.stroke();
		}else{
			
				var rstopc=region_lst[i].end;
				var rstartc=region_lst[i].start;
				var t_rchr = region_lst[i].chr;
				var t_bandlst = bandlstmap[t_rchr];
				for(var j=0; j<t_bandlst.cytoband.length; j++) { //bandlst
					var band = t_bandlst.cytoband[j];
				    var band_start = parseInt(band[1]);
					var band_end = parseInt(band[2]);
					var band_c = parseInt(band[3]);
					if(band_start >= rstopc) break;
					if(band_end <= rstartc) continue;
					ctx.lineWidth = ideogramWidth;
					var strokecolor =  band_c<0 ? centromereColor : 'rgb('+cytoBandColor[band_c]+','+cytoBandColor[band_c]+','+cytoBandColor[band_c]+')';
					//alert(strokecolor);
					ctx.strokeStyle = strokecolor;
					ctx.beginPath(); 
					ctx.arc(centerx,centery,radiusbase,r+(Math.max(band_start,rstartc)-rstartc)*sf,r+(Math.min(band_end,rstopc)-rstartc)*sf,false); //band arc
					ctx.stroke();
				}
				
				// region bar span encloser
				ctx.lineWidth = 1;
				ctx.strokeStyle = 'black';
				ctx.beginPath();ctx.arc(centerx,centery,radiusbase+ideogramWidth/2,r,r+rr,false);ctx.stroke(); // outner arc
				ctx.beginPath();ctx.arc(centerx,centery,radiusbase-ideogramWidth/2,r,r+rr,false);ctx.stroke(); // inner arc
				
				// region bar ends encloser
				ctx.beginPath();
				ctx.moveTo(centerx+(radiusbase+ideogramWidth/2)* Math.cos(r),centery+(radiusbase+ideogramWidth/2)*Math.sin(r));
				ctx.lineTo(centerx+(radiusbase-ideogramWidth/2)* Math.cos(r),centery+(radiusbase-ideogramWidth/2)*Math.sin(r));
				ctx.moveTo(centerx+(radiusbase+ideogramWidth/2)* Math.cos(r+rr),centery+(radiusbase+ideogramWidth/2)*Math.sin(r+rr));
				ctx.lineTo(centerx+(radiusbase-ideogramWidth/2)* Math.cos(r+rr),centery+(radiusbase-ideogramWidth/2)*Math.sin(r+rr));
				ctx.stroke();
				ctx.closePath();
			
		}
		
		//draw chromosome
		var r1=r+rr/4;
		if(bandlstmap_length > 1){
			r1=r+rr/2;
		}
		var x0=centerx+(radiusbase+ideogramWidth+8)*Math.cos(r1); 
		var y0= centery+(radiusbase+ideogramWidth+8)*Math.sin(r1);
		ctx.fillStyle = 'red';
		ctx.fillText(region_lst[i].chr,x0,y0);
		
		
		
		//draw position scaler
		//need to draw ideogram ticks, by default is 10
		var shownum = 10;
		var bandlstmap_length = Object.keys(bandlstmap).length;
		if(bandlstmap_length > 1){
			shownum = 1;
		}
	
		if(radius>=60&& bandlstmap_length<6){
			var showlength = region_lst[i].end - region_lst[i].start;
			var ticklenth = showlength / shownum;
			var tickradian=(Math.PI*2-spacing*bandlstmap_length)/shownum;
			var k = 0;
			var r2 = radiusbase+ideogramWidth-3;
			for(k=0; k<=shownum; k++){
				var angle =r+rr +k*tickradian ;
								
				//compute the x and y axis along the circle
				var x0=centerx + (r2-5) * Math.cos(angle);
				var y0=centery + (r2-5) * Math.sin(angle);
				
				ctx.save();
				ctx.lineWidth = 1;
				ctx.strokeStyle = 'black';
				ctx.beginPath();
				ctx.translate(x0,y0);
				ctx.rotate(angle);
				
				if(bandlstmap_length > 1){
						if(k==0){
							ctx.moveTo(0,2);//0
							ctx.lineTo(5,2);//0
							ctx.stroke();
						}else{
							ctx.moveTo(0,5);//0
							ctx.lineTo(5,5);//0
							ctx.stroke();
						}
						
				}else{
						ctx.moveTo(0,2);//0
					ctx.lineTo(5,2);//0
					ctx.stroke();
				}
			
				
				var text_start_pos = region_lst[i].start ; 
				var tickpos = parseInt(text_start_pos) + parseInt(k*ticklenth );
				var ftext = tickpos+"";
				
				var fnum = formatNumber(ftext);
				ctx.fillStyle = 'black';
				if(bandlstmap_length > 1){
					if(k>=1 && k<=4){
						ctx.rotate(-Math.PI/2);
					}else if(k==0){
						ctx.rotate(Math.PI/2);
					}
					
					if(k==0){
						ctx.fillText(fnum,1,-8);
					}else if(k>=1 && k<=4){
						
						ctx.fillText(fnum,0,15);
					}
				}else{
					if(k>=1 && k<=4){
						ctx.rotate(-Math.PI/2);
					}else if(k==0){
						ctx.rotate(Math.PI/2+ 0.2 *tickradian);
					}else if(k==10){
						ctx.rotate(Math.PI/2- 0.2 *tickradian);
					}else{
						ctx.rotate(Math.PI/2);
					}
					
					if(k==0){
						ctx.fillText(fnum,0,-8);
					}else if(k==10){
						var interval = fnum.length * 6; //5
						ctx.fillText(fnum,-interval,-8);
					}else if(k>=1 && k<=4){
						var interval = parseInt(fnum.length * 5/2);
						ctx.fillText(fnum,-interval,15);
					}else{
						ctx.fillText(fnum,-interval,-8);
					}

				}
				
				ctx.restore();
			}
		}
		
		
		r+=rr+spacing;
	}
}
	




//when user click the select box to choose the view scope
function scopeViewChange(istart,iend){
	ideogram_start = istart;
	ideogram_end = iend;
	computeRegionRadian();
}



/*this used to get the density data of the given scope and visually show
*/
function viewHistogramDensityData(){
	// redraw genome and layout
	//draw_genome();
//	showlayout(ctx,canvas);	
		
	var datatype = "inter";
	var level = 1;
	ajax_getHistogramDensityData(datatype,level); // this used to draw interaction histogram density picture
	
	datatype = "histone";
	level = 2;
	ajax_getHistogramDensityData(datatype,level); // this used to draw histone histogram density picture
	
}

 
function clearCircle(x,y,r){
	ctx.strokeStyle="rgb(255,255,255)";
	ctx.beginPath();
	ctx.arc(x, y, r, 0, 2*Math.PI, false); 
	ctx.stroke();
	ctx.fillStyle = "#FFFFFF"; 
	ctx.fill(); 
}


function clearArc(x,y,r,width){
	ctx.lineWidth=width;
	ctx.strokeStyle="rgb(255,255,255)";
	ctx.beginPath();
	ctx.arc(x, y, r, 0, 2*Math.PI, false); 
	//ctx.fillStyle = "#FFFFFF"; 
	ctx.stroke();

}


function config_getConfig(){
	var config_size=[canvas_width,canvas_height,radius];
	return config_size;
	
}


function config_setConfig(twidth,theight,tradius){
	
	//clear ideogram
	clearCircle(canvas_width/2,canvas_height/2,radius);
	//clear selected track
	clearAllSelectedTrackCanvas();
	
	canvas_width = twidth;
	canvas_height = theight;
	radius = parseInt(tradius);
	cur_track_radius = radius;
	
	//clear canvas panel
	
	if(show_mode ==1){
		computeRegionRadian();
	}
	showlayout();
	
	reDrawAllSelectedTrack();
	
}



function get_selectTrack(category){
	
	var cur_ra;
	var cur_he;
	if(selectedTrack.length>0){
		for(k=0;k<selectedTrack.length;k++){
			var selObj = selectedTrack[k];
				if(selObj.key == category){//track
					cur_ra = selObj.radius;
					cur_he = selObj.height;
					break;
				}
		}							
    }
	
	return [cur_ra,cur_he];
}


function remove_selectTrack(name){
	var iden=[];
	var found =0;
	if(selectedTrack.length>0){
		for(k=0;k<selectedTrack.length;k++){
			var selObj = selectedTrack[k];
				if(selObj.key == name){//track
					iden.push(k) ;
					//console.log(name+","+selObj.radius);
				}
		}							
    }
	
	if(iden.length>0){
		for(i=0;i<iden.length;i++){
			//console.log("remove track at "+iden[i]);
			selectedTrack.splice(iden[i],1);
		}
		
	}
	
}


function clearAllSelectedTrackCanvas(){
	
	$("#trackid input[type='checkbox']").each(function(){
			if ($(this).is(":checked")) {
				var checkval = $(this).val();
				var arrys = checkval.split(",");
				var cur_track=$("#track_"+arrys[1]);
				if(cur_track != null){
					cur_track.remove();
					remove_selectTrack(arrys[1]);
					deleteTrackFromCookie(arrys[1]);
				}	
			}
	});
	
	
}


function getJumpLink(jumpchr,jumpstart,jumpend,jumpkey){
	
	var link_genome="";
	var link_physical="";
	var conf = getQueryString("conf");
	var opkey = jumpkey;
				
		if(conf != null){
			link_genome="http://"+window.location.host+"/jbrowse/index.html?data="+conf+"&loc="+jumpchr+"%3A"+jumpstart+".."+jumpend+"&tracks="+opkey;
				
			if(opkey == "transcript"){
				opkey = "ensembl_gene";
			}
			link_physical="http://"+window.location.host+"/circosweb/pages/visualization/physical_view.jsp?conf="+conf+"&loc="+jumpchr+"%3A"+jumpstart+".."+jumpend+"&tracks=3dmodel,"+opkey;
		}else{
			link_genome="http://"+window.location.host+"/jbrowse/index.html?data=human&loc="+jumpchr+"%3A"+jumpstart+".."+jumpend+"&tracks="+opkey;
			if(opkey == "transcript"){
				opkey = "ensembl_gene";
			}
			link_physical="http://"+window.location.host+"/circosweb/pages/visualization/physical_view.jsp?loc="+jumpchr+"%3A"+jumpstart+".."+jumpend+"&tracks="+opkey;
		}
		return [link_genome,link_physical];
}



function trackCircletAroundLine(linecanvas,t_track_key,centerx,centery){
	var line_ctx1 = linecanvas.getContext('2d');
	line_ctx1.clearRect(0,0,canvas_width,canvas_height);					
	var track_arry=findTrackRadius(t_track_key) ;
	var track_radius = parseInt(track_arry[0]);	
	var t_track_height = parseInt(track_arry[1]);
					
	line_ctx1.strokeStyle= "green";
	line_ctx1.setLineDash([10,5]);
	line_ctx1.lineWidth = 0.5;
	line_ctx1.beginPath();
	line_ctx1.arc(centerx,centery,track_radius+5,0, Math.PI*2-0.01,false);
	line_ctx1.stroke();
	line_ctx1.beginPath();
	if(t_track_key == "ensembl_gene"){
		line_ctx1.arc(centerx,centery,track_radius+t_track_height+10,0, Math.PI*2-0.01,false);
	}else{
		line_ctx1.arc(centerx,centery,track_radius+t_track_height+10,0, Math.PI*2-0.01,false);
	}
	line_ctx1.stroke();
}


//this used to clear cookies, which will mainly used by jumping from other views
function clearCookies(rpath){	
	Cookies.remove(rpath+'_track', { path: rpath });
	Cookies.remove(rpath+'_dataset', { path: rpath });
	Cookies.remove(rpath+'_position', { path: rpath });
	
}



//open genome view in a new window
function gotoGenome(){
	
	//here, we will identify whether there are multiple chromosomes
	if(region_lst.length > 1){
		//pop up a radio button box to choose
		var t_html="";
		for(i=0;i<region_lst.length;i++){
			var t_region = region_lst[i] ;
			t_html += '<input type=\"radio\" name=\"gochrom\" value=\"'+t_region.chr+":"+t_region.start+".."+t_region.end+'\"  />chr'+t_region.chr+"<br/>";	
		}
		
		$('<div></div>').appendTo('body')
							  .html('<div style=\"margin-top:10px;\">'+t_html+'</div>')
							  .dialog({
								  modal: true, title: 'Choose a chromosome', zIndex: 10000, autoOpen: true,
								  width: 'auto', resizable: false,						 
								  close: function (event, ui) {
									  //remove from cookie								 
									  $(this).remove();
								  },
								  buttons:{
									  Yes: function(){
										//here read the checked radio button
										   var p_pos= $('input[type="radio"][name="gochrom"]:checked').val();
										   var conf = getQueryString("conf");
											var ghrefurl;
											
											var jumptrack="";
											$("#trackid input[type='checkbox']").each(function(){				
															if ($(this).is(":checked")) {
																var checkval = $(this).val();
																var arrys = checkval.split(",");	
																if(arrys[1].indexOf("Interaction") > -1 ||(arrys[1].indexOf("ensembl_gene") > -1)){						
																	jumptrack += arrys[1]+"," ;
																}else{
																	jumptrack += arrys[1]+"_signal," ;
																}
																
															}
												});
											if(jumptrack.length>0){
													jumptrack = jumptrack.substring(0,jumptrack.length-1);
											}
											
											console.log("goto genome track="+jumptrack);
											
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
									  },
									  No: function(){
										$(this).dialog('close');
									  }
							}});
		
		
		
		
	}else{
			var p_pos = $("#curpos").val(); 
			var conf = getQueryString("conf");
			var ghrefurl;
			
			var jumptrack="";
			$("#trackid input[type='checkbox']").each(function(){				
							if ($(this).is(":checked")) {
								var checkval = $(this).val();
								var arrys = checkval.split(",");	
								if(arrys[1].indexOf("Interaction") > -1 ||(arrys[1].indexOf("ensembl_gene") > -1)){						
									jumptrack += arrys[1]+"," ;
								}else{
									jumptrack += arrys[1]+"_signal," ;
								}
								
							}
				});
			if(jumptrack.length>0){
					jumptrack = jumptrack.substring(0,jumptrack.length-1);
			}
			
			console.log("goto genome track="+jumptrack);
			
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
	

}

//embed genome in physical view
function gotoEmbedGenome(){
	var p_pos = $("#curpos").val(); 
	var conf = getQueryString("conf");
	var jumptrack="";
	$("#trackid input[type='checkbox']").each(function(){				
					if ($(this).is(":checked")) {
						var checkval = $(this).val();
						var arrys = checkval.split(",");	
						if(arrys[1] != "Interaction"){
							jumptrack += arrys[1]+"_signal," ;
						}else{
							jumptrack += arrys[1]+"," ;
						}
						
					}
		});
	if(jumptrack.length>0){
			jumptrack = jumptrack.substring(0,jumptrack.length-1);
	}
	
	window.parent.refereshEmbedGenome(conf,p_pos,jumptrack);
	
}

//this is used to synchronized to physical view
function RefreshPhysical(){
	var p_pos = $("#curpos").val(); 
	var conf = getQueryString("conf");
	
	var jumptrack="";
	$("#trackid input[type='checkbox']").each(function(){				
					if ($(this).is(":checked")) {
						var checkval = $(this).val();
						var arrys = checkval.split(",");					
					     jumptrack += arrys[1]+"," ;																		
						
					}
		});
	if(jumptrack.length>0){
			jumptrack = jumptrack.substring(0,jumptrack.length-1);
	}
	window.parent.refereshLocationFromGenomeView(conf,p_pos,jumptrack); ////conf,loc,tracks	
	
}

//open topological view in a new window
function gotoPhysicalView(){
	
		//here, we will identify whether there are multiple chromosomes
	        clearCookies("physical");
			var p_pos = $("#curpos").val(); 
			var conf = getQueryString("conf");
			
			var modelkeymap={"GSE35156_IMR90":"MOGEN_GSE35156_IMR90_40000","GSE18199_K562":"BACH_GSE18199_K562_1000000,MOGEN_GSE18199_K562_1000000","GSE18199_GM06690":"MOGEN_GSE18199_GM06690_1000000,MOGEN_GSE18199_GM06690_100000","GSE63525_K562_combined":"BACH_GSE63525_K562_combined_1000000,BACH_GSE63525_K562_combined_500000,MOGEN_GSE63525_K562_combined_1000000,MOGEN_GSE63525_K562_combined_500000,MOGEN_GSE63525_K562_combined_250000,MOGEN_GSE63525_K562_combined_100000","GSE63525_GM12878_combined":"MOGEN_GSE63525_GM12878_combined_100000,MOGEN_GSE63525_GM12878_combined_1000000,MOGEN_GSE63525_GM12878_combined_500000,MOGEN_GSE63525_GM12878_combined_250000,BACH_GSE63525_GM12878_combined_1000000","GSE63525_HUVEC_combined":"MOGEN_GSE63525_HUVEC_combined_100000,MOGEN_GSE63525_HUVEC_combined_1000000,MOGEN_GSE63525_HUVEC_combined_500000,MOGEN_GSE63525_HUVEC_combined_250000","GSE63525_HMEC_combined":"MOGEN_GSE63525_HMEC_combined_100000,MOGEN_GSE63525_HMEC_combined_1000000,MOGEN_GSE63525_HMEC_combined_500000,MOGEN_GSE63525_HMEC_combined_250000","GSE63525_IMR90_combined":"MOGEN_GSE63525_IMR90_combined_100000,MOGEN_GSE63525_IMR90_combined_1000000,MOGEN_GSE63525_IMR90_combined_500000,MOGEN_GSE63525_IMR90_combined_250000","GSE63525_KBM7_combined":"MOGEN_GSE63525_KBM7_combined_100000,MOGEN_GSE63525_KBM7_combined_1000000,MOGEN_GSE63525_KBM7_combined_500000,MOGEN_GSE63525_KBM7_combined_250000"};
			var cmodelkeymap = {};
			
			var jumptrack="";
			var pvmodel=[];
			$("#trackid input[type='checkbox']").each(function(){				
							if ($(this).is(":checked")) {
								var checkval = $(this).val();
								var arrys = checkval.split(",");
								var tr = arrys[1];
                                var hicindex = arrys[1].lastIndexOf('_');
								if(hicindex > -1){
									var hicstr = arrys[1].substring(0,hicindex);
									if(modelkeymap[hicstr]!=null ){
									if(cmodelkeymap[hicstr] == null ){									
										cmodelkeymap[hicstr] = hicstr ;	
										//
										var modelarry = modelkeymap[hicstr].split(",");
										if(modelarry != null ){
											
											for(var ji=0;ji<modelarry.length;ji++){
												var t_mstr = modelarry[ji];
												//identify whether pvmodel exists
												var idenflag  = 0 ;
												if(pvmodel.length > 0 ){
														for(var jp =0 ; jp< pvmodel.length ;jp++){
															var jp_mstr = pvmodel[jp];
															var mo_mstr = t_mstr+"_3dmodel";
															if(jp_mstr == mo_mstr){
																idenflag = 1; 
																break;															
															}
														
														}
												}
												
												if(idenflag == 0){
													pvmodel.push(t_mstr+"_3dmodel");
												}
													
											}
									   }
										
													
									} 
								}
								else{
										if( parseInt(tr.indexOf("Interaction") ) <0 ){
											jumptrack += tr+"," ;											
										}									
									} 
								}else{
									if( parseInt(tr.indexOf("Interaction") ) <0 ){
											jumptrack += tr+"," ;											
									}	
								}
							}
				});
			if(jumptrack.length>0){
					jumptrack = jumptrack.substring(0,jumptrack.length-1);
			}
				
		
	if(region_lst.length > 1){
		//pop up a radio button box to choose
		var t_html="";
		for(i=0;i<region_lst.length;i++){
			var t_region = region_lst[i] ;
			t_html += '<input type=\"radio\" name=\"gochrom\" value=\"'+t_region.chr+":"+t_region.start+".."+t_region.end+'\"  /> chr'+t_region.chr+"<br/>";	
		}
		
		$('<div></div>').appendTo('body')
							  .html('<div style=\"margin-top:10px;\">'+t_html+'</div>')
							  .dialog({
								  modal: true, title: 'Choose a chromosome', zIndex: 10000, autoOpen: true,
								  width: 'auto', resizable: false,						 
								  close: function (event, ui) {
									  //remove from cookie								 
									  $(this).remove();
								  },
								  buttons:{
									  Yes: function(){
										//here read the checked radio button
										   var p_pos= $('input[type="radio"][name="gochrom"]:checked').val();
										   var conf = getQueryString("conf");
											var ghrefurl;
											
											
												if(pvmodel.length>0){
				
														var t_html = '';
														if(conf != null && conf != "hg18" && conf!="hg19"){
															t_html = '<input class=\"re_select_track\" type=\"radio\" name=\"model\" value=\"3dmodel\" checked=\"checked\" />default 3dmodel<br/>'
														}
														for(i =0; i< pvmodel.length; i++){
															var modelname = pvmodel[i];
															t_html += '<input class=\"re_select_track\" type=\"radio\" name=\"model\" value=\"'+modelname+'\"  />'+modelname+"<br/>";
														}
														
														
														//then we need to popup a dialog ,with model name
															$('<div></div>').appendTo('body')
																	  .html('<div style=\"margin-top:10px;\">'+t_html+'</div>')
																	  .dialog({
																		  modal: true, title: 'Choose a 3d model', zIndex: 10000, autoOpen: true,
																		  width: 'auto', resizable: false,						 
																		  close: function (event, ui) {
																			  //remove from cookie								 
																			  $(this).remove();
																		  },
																		  buttons:{
																			  Yes: function(){
																				//here read the checked radio button
																				   var checkmodel= $('input[type="radio"][name="model"]:checked').val();
																				    var seljumptrack = jumptrack;
																				   
																				   
																					var seljumptrack = jumptrack;
																				   if(jumptrack.length> 0 ){
																						seljumptrack += ","+checkmodel;
																					}else{
																						seljumptrack = checkmodel;
																					}
																				
																					
																					var t_key1="";
																					var findex = checkmodel.indexOf("_");
																					var lindex = checkmodel.indexOf("_3dmodel");
																			
																					if(findex > -1 && lindex > -1){
																						var tstr = checkmodel.substring(findex+1,lindex);
																						 t_key1 = tstr+"_Interaction";
																						 seljumptrack += ","+t_key1;
																					}else if(checkmodel == "3dmodel"){
																						seljumptrack += ",Interaction";	
																					}
																					
																					
																					
																					if(conf == null){			
																						threfurl="http://"+window.location.host+"/pages/visualization/physical_view.jsp?loc="+p_pos;			
																						threfurl+="&tracks="+seljumptrack;			
																					}else{		
																						threfurl="http://"+window.location.host+"/pages/visualization/physical_view.jsp?conf="+conf+"&loc="+p_pos;
																						threfurl+="&tracks="+seljumptrack;			
																					}
																					
																					var a = document.getElementById("goto");
																					if(a == null){
																						a=document.createElement('a');
																						a.id="goto";
																						document.body.appendChild(a);
																					}
																					
																					
																					a.href = threfurl;
																					a.target="_blank";
																					
																					document.getElementById("goto").click();
																					$(this).dialog('close');
																			  },
																			  No: function(){
																				$(this).dialog('close');
																			  }
																	}});
														
													
													}else{ // default choose 3dmodel
														 var seljumptrack = jumptrack;
														if(conf != null && conf != "hg18" && conf != "hg19"){
															if(jumptrack.length> 0 ){
															 seljumptrack += ",3dmodel,Interaction";
															}else{
																seljumptrack = "3dmodel,Interaction";
															}
														}
														
														
													
									
													if(conf == null){			
														threfurl="http://"+window.location.host+"/pages/visualization/physical_view.jsp?loc="+p_pos;			
														threfurl+="&tracks="+seljumptrack;			
													}else{		
														threfurl="http://"+window.location.host+"/pages/visualization/physical_view.jsp?conf="+conf+"&loc="+p_pos;
														threfurl+="&tracks="+seljumptrack;			
													}
													
													var a = document.getElementById("goto");
													if(a == null){
														a=document.createElement('a');
														a.id="goto";
														document.body.appendChild(a);
													}
													
													
													a.href = threfurl;
													a.target="_blank";
													
													document.getElementById("goto").click();
														$(this).dialog('close');
													}
									  },
									  No: function(){
										$(this).dialog('close');
									  }
							}});
		
		
		
		
	}else{
				//clear physical cookie first
			
			
			
				//
			if(pvmodel.length>0){
				
				
				var t_html = '';
				if(conf != null && conf != "hg18" && conf!="hg19"){
						t_html = '<input class=\"re_select_track\" type=\"radio\" name=\"model\" value=\"3dmodel\" checked=\"checked\" />default 3dmodel<br/>'
				}
				for(i =0; i< pvmodel.length; i++){
					var modelname = pvmodel[i];
					t_html += '<input class=\"re_select_track\" type=\"radio\" name=\"model\" value=\"'+modelname+'\"  />'+modelname+"<br/>";
				}
				
				
				//then we need to popup a dialog ,with model name
					$('<div></div>').appendTo('body')
							  .html('<div style=\"margin-top:10px;\">'+t_html+'</div>')
							  .dialog({
								  modal: true, title: 'Choose a 3d model', zIndex: 10000, autoOpen: true,
								  width: 'auto', resizable: false,						 
								  close: function (event, ui) {
									  //remove from cookie								 
									  $(this).remove();
								  },
								  buttons:{
									  Yes: function(){
										//here read the checked radio button
										   var checkmodel= $('input[type="radio"][name="model"]:checked').val();
										    var seljumptrack = jumptrack;
										   if(jumptrack.length> 0 ){
												seljumptrack += ","+checkmodel;
											}else{
												seljumptrack = checkmodel;
											}
												
											var t_key1="";
											var findex = checkmodel.indexOf("_");
											var lindex = checkmodel.indexOf("_3dmodel");
																			
											if(findex > -1 && lindex > -1){
												var tstr = checkmodel.substring(findex+1,lindex);
												t_key1 = tstr+"_Interaction";
												seljumptrack += ","+t_key1;
											}else if(checkmodel == "3dmodel"){
												seljumptrack += ",Interaction";	
											}											
										   
											//clear cookie
											clearCookies("physical");
											
											if(conf == null){			
												threfurl="http://"+window.location.host+"/pages/visualization/physical_view.jsp?loc="+p_pos;			
												threfurl+="&tracks="+seljumptrack;			
											}else{		
												threfurl="http://"+window.location.host+"/pages/visualization/physical_view.jsp?conf="+conf+"&loc="+p_pos;
												threfurl+="&tracks="+seljumptrack;			
											}
											
											var a = document.getElementById("goto");
											if(a == null){
												a=document.createElement('a');
												a.id="goto";
												document.body.appendChild(a);
											}
											
											
											a.href = threfurl;
											a.target="_blank";
											
											document.getElementById("goto").click();
											$(this).dialog('close');
									  },
									  No: function(){
										$(this).dialog('close');
									  }
							}});			
			}else{ // default choose 3dmodel
				if(conf != null && conf != "hg18" && conf!="hg19"){
					if(jumptrack.length> 0 ){
						jumptrack += ",3dmodel,Interaction";
					}else{
						jumptrack = "3dmodel,Interaction";
					}
				}
				
				
			if(conf == null){			
				threfurl="http://"+window.location.host+"/pages/visualization/physical_view.jsp?loc="+p_pos;			
				threfurl+="&tracks="+jumptrack;			
			}else{		
				threfurl="http://"+window.location.host+"/pages/visualization/physical_view.jsp?conf="+conf+"&loc="+p_pos;
				threfurl+="&tracks="+jumptrack;			
			}
			
			var a = document.getElementById("goto");
			if(a == null){
				a=document.createElement('a');
				a.id="goto";
				document.body.appendChild(a);
			}
			
			
			a.href = threfurl;
			a.target="_blank";
			
			document.getElementById("goto").click();
				
			}
			
			
	}
}


// this used to feresh the circlet view from the physical view
function RefreshGenomeView (loc,tracks){
	if(loc!= null && loc.length>0){
		console.log("position="+loc);
		$("#curpos").val(loc);		
		//change the state of checked track	
	}else{
		return;
	}

	resetCircetView();
	//change the ideogram position and redraw all tracks
	if(tracks != null ){
		if(tracks.indexOf(",") > -1){
			var trackarry = tracks.split(",");
			for(var i = 0 ;i < trackarry.length ;i++){
				var curtrack = trackarry[i];
				if(curtrack.indexOf('_signal') > 0 ){
					curtrack = curtrack.substring(0,curtrack.length-7);
				}
				
				$("#tlst_"+curtrack).prop("checked",true);
			}
		}else{
			var curtrack = tracks;
				if(curtrack.indexOf('_signal') > 0 ){
					curtrack = curtrack.substring(0,curtrack.length-7);
				}
			$("#tlst_"+curtrack).prop("checked",true);
			
		}
			
	}	
	singleChrom();
}


//this is used to highlight a given region
function HighlightGenomeView(loc,tracks){
	
			console.log("=============circos HighlightGenomeView="+loc);
			if(loc != null ){
				
				var index1 = loc.indexOf(":");
				var index2 = loc.indexOf(".");
				var chrtmp = loc.substring(0,index1);
				var pos_starttmp = loc.substring(index1+1,index2);
				var pos_endtmp = loc.substring(index2+2,loc.length);
				
				//region_lst
				var rid=-1;
				for(k=0;k<region_lst.length;k++){
						if(chrtmp == region_lst[k].chr){
							rid = k;
							break;
						}
				}
				
				console.log("===========rid="+rid);
				if(rid == -1) return;
				var t_top= 0- canvas_height +2;
				t_top= t_top+'px';
				
				$("#divglasspane").css("margin-top",t_top);
				var pos=absolutePosition(canvas);
				var gleft = parseInt(pos[0]);
					glasspane.style.display='block';
					glasspane.style.left=gleft;
					//glasspane.style.top=pos[1];
					glasspane.width=canvas.width;
					glasspane.height=canvas.height;
					glasspane.style.zIndex = 2;
				
				var region_start = region_lst[rid].start;
				//alert("region start="+region_start);
				var region_end = region_lst[rid].end;
				var arc_offset = region_lst[rid].rgoffset;
				
				var arc_start = (pos_starttmp - region_start) * sf; 
				
				var arc_end = (pos_endtmp - region_start) * sf;
				var spanradian = arc_end - arc_start;
				arc_start += arc_offset;
				
				var drawradius = 80;
				var drawheight = cur_radius-100;
				//console.log("======="+arc_start+","+spanradian+","+drawradius+","+drawheight);
				
				outercircletview_paint_glasspane(arc_start,spanradian,drawradius, drawheight,"");
				
				
			}
	
}


