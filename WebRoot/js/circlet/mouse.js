
/********************************
*  when mouse down,then user can choose scope view range along the circlet
we need to compute the absolution position of the mouse 
*/

function circlet_view_make(event){
	if(event.button!=0) return;
	event.preventDefault();
	$(".right-menu-class").each(function(){
		$(this).remove();
	});
	var pos=absolutePosition(canvas);
	var centerx=parseInt(canvas.width/2);
	var centery=parseInt(canvas.height/2);
	//get absolute center position of canvas
	var cx=pos[0]+centerx;
	var cy=pos[1]+centery;	
	var t_top= 0- canvas_height +2;
	t_top= t_top+'px';
	
	$("#divglasspane").css("margin-top",t_top);
	var lefttop=get_page_left_top();
	
	var divscoll_left=  document.getElementById('idpagecontent').scrollLeft;
    var divscoll_top =  document.getElementById('idpagecontent').scrollTop;
	
	//get absolute position of mouse
	var mx=event.clientX+lefttop[0]+divscoll_left;
	var my=event.clientY+lefttop[1]+divscoll_top;
	
	//identify the mouse position, the mouse must be located in 
	var distance = get_point_distance(cx,cy,mx,my);
	if(distance>= (radius-ideogramWidth-5 ) && distance <= (radius+ideogramWidth+5)){
		document.body.addEventListener('mousemove', circlet_view_move, false);//circlet_view_move
		document.body.addEventListener('mouseup', circlet_view_select, false);//circlet_view_select
			gflag.hvblobmove={
			cx:cx,
			cy:cy,
			mx:mx,
			my:my,
			old_radian:get_point_radian(cx,cy,mx,my),
			clockwise:null,
			move_radian:null,
			region_start:ideogram_start
		};

		var gleft = parseInt(pos[0]);
		glasspane.style.display='block';
		glasspane.style.left=gleft;
		//glasspane.style.top=pos[1];

		glasspane.width=canvas.width;
		glasspane.height=canvas.height;
		glasspane.style.zIndex = 201;
		
	}else if(distance > (radius+ideogramWidth+15)){ // when the mouse move to the outer track and selected along one track, then we need to draw a red line 
		
		document.body.addEventListener('mousemove', outercirclet_view_move, false);//outer circlet track move
		document.body.addEventListener('mouseup', outercirclet_view_select, false);//outer circlet track select
		
		
		var t_track_key = findNearestRadiusAccordingDistance(distance);
				
		outergflag.outermove={
			cx:cx,
			cy:cy,
			mx:mx,
			my:my,
			old_radian:get_point_radian(cx,cy,mx,my),
			clockwise:null,
			track_key:t_track_key,
			end_radian:null,
			move_radian:null
		};
		var gleft = parseInt(pos[0]);
		glasspane.style.display='block';
		glasspane.style.left=gleft;
		//glasspane.style.top=pos[1];
		glasspane.width=canvas.width;
		glasspane.height=canvas.height;
		glasspane.style.zIndex = 2;
	
	}
		
}


/*mouse move event, when the mouse click down,this is used to capature mouse move 
*/
function circlet_view_move(event){
	var bm=gflag.hvblobmove;
	//get mouse position
	var lefttop=get_page_left_top();
	var divscoll_left=  document.getElementById('idpagecontent').scrollLeft;
    var divscoll_top =  document.getElementById('idpagecontent').scrollTop;
	
	var x=event.clientX+lefttop[0]+divscoll_left,y=event.clientY+lefttop[1]+divscoll_top;
	//canvas center abosolute position bm.cx,bm.cy
	var curr_radian=get_point_radian(bm.cx,bm.cy,x,y);
	
	//only allow cursor moving +-pi/2 of old radian
	var tmp=get_directionspan_circularmove(bm.old_radian,curr_radian);
	if(tmp[0]==null) {circlet_view_select();return;}
	bm.clockwise=tmp[0];
	bm.move_radian=tmp[1];
	
	//need to modify this code in case of multiple chromosomes
	var r_start = -1;
	var r_span = -1;
	var m_target_region_idex=get_mouse_move_region(bm.old_radian);
	if(m_target_region_idex > -1){
		r_start=region_lst[m_target_region_idex].rgoffset;
		r_span=region_lst[m_target_region_idex].rgradian;
	}
	
	
	
	if(bm.clockwise) { //mouse move direction , clockwise
		if(bm.old_radian+bm.move_radian > (r_start+r_span)) {
			bm.move_radian=r_start+r_span-bm.old_radian;
		} 
		circletview_paint_glasspane(bm.old_radian,bm.move_radian);
		
	} else { //mouse move direction, not clockwise
		if(bm.old_radian-bm.move_radian<r_start) {
			bm.move_radian=bm.old_radian-r_start;
		} 
		circletview_paint_glasspane(bm.old_radian-bm.move_radian,bm.move_radian);
		
	}	
}

/*when user release mouse along the ideogram
*/
function circlet_view_select()
{
	glasspane.style.display='none';
	document.body.removeEventListener('mousemove', circlet_view_move, false);
	document.body.removeEventListener('mouseup', circlet_view_select, false);
	var bm=gflag.hvblobmove;
	if(bm.clockwise==null) return;
	//var r=bm.vobj.regions[bm.ridx];
	
	//here, we need to identify the mouse located which chromosome region when mutiple chromosomes exist
	var r_start = -1;
	var r_span = -1;
	
	var querypos = null;
	var m_target_region_idex=get_mouse_move_region(bm.old_radian);
	if(m_target_region_idex > -1){
		var region_selected = region_lst[m_target_region_idex];
		var dstart=region_selected.start;
		var dstop=region_selected.end;
		var cur_span = region_selected.rgradian;
		var sf1=(dstop-dstart)/cur_span;
		var rr_start=region_selected.rgoffset;//region_offset[m_target_region_idex];
		
		
		if(bm.clockwise) {
			ideogram_start =parseInt(sf1*(bm.old_radian-rr_start)) + parseInt(dstart); //ideogram_start =parseInt(sf1*(bm.old_radian-rr_start)) + parseInt(bm.region_start)
		} else {
			ideogram_start =parseInt(sf1*(bm.old_radian-rr_start-bm.move_radian))+ parseInt(dstart); //ideogram_start =parseInt(sf1*(bm.old_radian-rr_start-bm.move_radian))+ parseInt(bm.region_start)
		}
		
	    ideogram_end = parseInt(ideogram_start)+parseInt(sf1*bm.move_radian); //  ideogram_end = parseInt(ideogram_start)+parseInt(sf1*bm.move_radian);
		pos_end = ideogram_end;
		pos_start = ideogram_start;			
	
		chr = region_selected.chr;
		querypos = chr+":"+ideogram_start+".."+ideogram_end;
		$("#chromid").val(chr);
		//update select region
		var queryscope = ideogram_end - ideogram_start +1;
		var queryfmt = formatNumber(queryscope);
		$("#formatscopeid").html(queryfmt+" bp");
	}
	show_mode=1;
	if(querypos != null){
		Cookies.set("topo_position",querypos, { path: 'topo' });
		$("#curpos").val(querypos);	
		computeRegionRadian();
		ajax_getbandlst(config.ideogramJson,chr);
		reDrawAllSelectedTrack();
	}
	
	
}



/*this is used to draw glass_pane when mouse move along the ideogram
*here, we need to adjust the radius
*/
function circletview_paint_glasspane(startradian,spanradian)
{
	var centerx=parseInt(canvas.width/2);
	var centery=parseInt(canvas.height/2);

	var x=centerx, y=centery;
	var ctx=glasspane.getContext('2d');
	ctx.clearRect(0,0,glasspane.width,glasspane.height);
	ctx.strokeStyle='rgba(255,102,51,.3)';
	ctx.beginPath();
	ctx.lineWidth=ideogramWidth; // draw the selected idogram
	ctx.arc(x,y,radius,startradian,startradian+spanradian,false);
	ctx.stroke();

	ctx.strokeStyle='rgb(245,61,0)';
	ctx.lineWidth=1;
	ctx.beginPath();
	var r=radius-ideogramWidth-1;
	ctx.moveTo(x+r*Math.cos(startradian),y+r*Math.sin(startradian));
	r=radius+1+ideogramWidth;//radius +3
	ctx.lineTo(x+r*Math.cos(startradian),y+r*Math.sin(startradian));
	ctx.arc(x,y,r,startradian,startradian+spanradian,false);
	r=radius-ideogramWidth-1;//radius-ideogramWidth-5
	ctx.lineTo(x+r*Math.cos(startradian+spanradian),y+r*Math.sin(startradian+spanradian));
	ctx.arc(x,y,r,startradian+spanradian,startradian,true);
	ctx.stroke();
}



//genome move 
//process choose view scope along the genome by mouse
function horizon_ideogram_make(event)
{
	/* both on trunk and spliter
	*/

	if(event.button != 0) return; // only process left click
	event.preventDefault();
	var center_leftpos = $("#genome_region").offset().left;
	var indicator_toppos = $("#genome_region").offset().top;
	//console.log("genome region pos="+center_leftpos+","+indicator_toppos);
	var pos = absolutePosition(event.target);
	//console.log("event target="+pos[0]+","+pos[1]);
	//console.log("mouse xy="+event.clientX+","+event.clientY);
	var lefttop = get_page_left_top();
	var indicator_left = event.clientX + lefttop[0]-center_leftpos+9; // 10 may be need to adjust
	
	var event_x = event.clientX + lefttop[0];
	//console.log("mouse absolute pos="+event_x);
	indicator.style.display = "block";
	indicator.style.left = indicator_left +"px";//+ parseInt(lefttop[0])
	var synphysical = getQueryString("showPhysical") ;
	if(synphysical == 1){
		indicator.style.top = (pos[1]- 55)+"px";
	}else{
		indicator.style.top = (pos[1]- 102)+"px";
	}
	indicator.style.width = "1px";
	//alert("pos0="+pos[0]+",pos1="+pos[1]);
	indicator.style.height = event.target.clientHeight +"px";
	gflag.navigator={
		canvas:event.target,
		x:event.clientX+lefttop[0],
		xcurb:pos[0]};
	document.body.addEventListener("mousemove", horizon_ideogram_move, false);
	document.body.addEventListener("mouseup", horizon_ideogram_select, false); 
}

/**this will show a blue shadow blox to show the choosed scope view
***/
function horizon_ideogram_move(event)
{
	var lefttop = get_page_left_top();
	var pos = absolutePosition(event.target);
	var currx = event.clientX + lefttop[0];
	var n=gflag.navigator;
	if(currx > n.x) {
		if(currx < n.xcurb+n.canvas.width) {
			//alert("currx < n.xcurb+n.canvas.width,"+indicator.style.width+",currx="+currx+",n.x="+n.x);
			indicator.style.width =( parseInt(currx) - parseInt(n.x))+"px";
		}
	} else {
		if(currx >= n.xcurb) {
			//alert("currx >= n.xcurb,"+indicator.style.width);
			indicator.style.width = (parseInt(n.x) - parseInt(currx))+"px";
			var center_leftpos = $("#genome_region").offset().left;
			indicator.style.left = currx-center_leftpos+9+"px";
		
		}
	}
}



function horizon_ideogram_select(event)
{
	document.body.removeEventListener("mousemove", horizon_ideogram_move, false);
	document.body.removeEventListener("mouseup", horizon_ideogram_select, false);
	indicator.style.display='none';
	var n=gflag.navigator;

	// in order to keep the select indicator the same as the genome canvas drawn ,we need to minus 9 px
	var x = parseInt(indicator.style.left)-9; 
	var w = parseInt(indicator.style.width);

	if(w==0) return;
	//var jt = n.bbj.juxtaposition.type;
	var chromlen = 0 ;
	var chromend = 0 ;
	for(i=0;i<chrom_lst.length;i++){
		var chromdata = chrom_lst[i];
		if(chr == chromdata.chr){
			chromlen = chromdata.end - chromdata.start;
			chromend = chromdata.end;
			break;
		}		
	}
	
	
	var coord1=Math.ceil((x/genome_canvas.width) * chromlen);
	var coord2=Math.ceil((x+w)/genome_canvas.width * chromlen);
	if(coord2 > chromend){
		coord2 = chromend;
	}

	if( coord1==coord2) return;
	ideogram_start = coord1;  // ideogram start position
	ideogram_end = coord2;   // ideogram end position
	
	
	
	
	//alert("coord1="+coord1+",coord2="+coord2);
	//then we need to redraw genome and circos view
	var querypos = chr+":"+ideogram_start+".."+ideogram_end;
	$("#curpos").val(querypos);	
	
	//update select region
	var queryscope = ideogram_end - ideogram_start +1;
	var queryfmt = formatNumber(queryscope);
	$("#formatscopeid").html(queryfmt+" bp");
	
	Cookies.set("topo_position",querypos, { path: 'topo'});
	
	computeRegionRadian();	
	
	var cur_chr=$("#chromid").val();
	ajax_getbandlst(config.ideogramJson,cur_chr);
	
	reDrawAllSelectedTrack();
}

/*
*this used to get mouse click absolute position
*/
function getPosition(e) {
  var posx = 0;
  var posy = 0;

  if (!e) var e = window.event;

  if (e.pageX || e.pageY) {
    posx = e.pageX;
    posy = e.pageY;
  } else if (e.clientX || e.clientY) {
    posx = e.clientX + document.body.scrollLeft + 
                       document.documentElement.scrollLeft;
    posy = e.clientY + document.body.scrollTop + 
                       document.documentElement.scrollTop;
  }

  return {
    x: posx,
    y: posy
  }
}

/*this used to get absolute position of given object
*/
function absolutePosition(obj)
{
	var c = [0,0];
	if(obj.offsetParent) {
		var o2 = obj;
		do {
			var b=parseInt(o2.style.borderLeftWidth);
			c[0] += o2.offsetLeft+(isNaN(b)?0:b);
			b=parseInt(o2.style.borderTopWidth);
			c[1] += o2.offsetTop+(isNaN(b)?0:b);
		} while(o2 = o2.offsetParent);
	}
	return c;
}

/***********************************
* compute the distance between two points
*/
function get_point_distance(mx,my,x,y){
	var dis = Math.sqrt((x-mx)*(x-mx)+(y-my)*(y-my));
	return dis ;
}

/**
*get left and top of current page
******/
function get_page_left_top(){
	var pagepos=[0,0];
	
	if (document.compatMode == "BackCompat") {	
		pagepos[0] = document.body.scrollLeft;
		pagepos[1] = document.body.scrollTop;
	}
	else { //document.compatMode == \"CSS1Compat\"	
		pagepos[0] = document.documentElement.scrollLeft == 0 ? document.body.scrollLeft : document.documentElement.scrollLeft;
		pagepos[1] = document.documentElement.scrollTop == 0 ? document.body.scrollTop : document.documentElement.scrollTop;
	}
	
	return pagepos;
	
}


/*get radian of point
*/
function get_point_radian(centerx,centery,x,y)
{
/* 0 is horizontal to right, clockwise for increasing radian, gives radian 0-2pi
args:
centerx/y:
x/y:
*/
var r=Math.atan((y-centery)/(x-centerx));
if(x<centerx) {
	r+=Math.PI;
} else if(y<centery) {
	r+=2*Math.PI;
}
return r;
}

/*when mouse move ,it can be move to left direction of current point
 or the right dirction of current point
*/
function get_directionspan_circularmove(oldr,newr)
{
/* args:
oldr: previous radian
newr: new radian
input are always positive, from get_point_radian()
figure out move direction (true for clockwise) and moved radian (always positive)
allowed move radian within +-PI of old radian
*/
var esc=0.1;
if(oldr<=Math.PI/2) {
	// 1st
	var a=newr-Math.PI-oldr;
	if(a>=-esc && a<=esc) return [null,null];
	if(newr<oldr) return [false,oldr-newr];
	if(newr<oldr+Math.PI) return [true,newr-oldr];
	// switching to 3rd or 4th
	return [false, Math.PI*2-newr+oldr];
}
if(oldr<=Math.PI) {
	// 2nd
	var a=newr-Math.PI-oldr;
	if(a>=-esc && a<=esc) return [null,null];
	if(newr<oldr) return [false, oldr-newr];
	if(newr<oldr+Math.PI) return [true,newr-oldr];
	// jump from 1st to 4th
	return [false,Math.PI*2-newr+oldr];
}
if(oldr<=Math.PI*.15) {
	// 3rd
	var a=oldr-Math.PI-newr;
	if(a>=-esc && a<=esc) return [null,null];
	if(newr>oldr) return [true,newr-oldr];
	if(newr<oldr-Math.PI) {
		// jump from 4th to 1st
		return [true,Math.PI*2-oldr+newr];
	}
	return [false,oldr-newr];
}
// 4th
var a=oldr-Math.PI-newr;
if(a>=-esc && a<=esc) return [null,null];
if(newr>oldr) return [true,newr-oldr];
if(newr<Math.PI-(Math.PI*2-oldr)) {
	// jump from 4th to 1st or 2nd
	return [true,Math.PI*2-oldr+newr];
}
return [false,oldr-newr];
}

/*
*this is used to identify current mouse locate in which chromosome or region
*m_b_radian mouse begin radian
*/
function get_mouse_move_region(m_b_radian){
	   var index =-1 ;
		if(region_lst !=undefined || region_lst !=null ){
		for(i=0;i<region_lst.length;i++){
			var t_region = region_lst[i];
			
			var r = t_region.rgoffset;//region_offset[i]; // radian start
			var rr = t_region.rgradian;//region_radian[i]; //radian span
			if(m_b_radian>=r && m_b_radian<= r+rr ){
				index=i;			
				break;
			}	
		}		
	}
	return index;
	
}


//when mouse move on the top of canvas, pop up dialog information
function extend_canvas_mouse_move(e){
	
	 canvas.addEventListener('contextmenu', extend_canvas_cxtmenu_event, false);
			var pos=absolutePosition(canvas);
			var centerx=parseInt(canvas.width/2);
			var centery=parseInt(canvas.height/2);
			
			//get absolute center position of canvas
			var cx=pos[0]+centerx;
			var cy=pos[1]+centery;	

			var lefttop=get_page_left_top();
	
			
			//div scroll left and top
			var divscoll_left=  document.getElementById('idpagecontent').scrollLeft;
			var divscoll_top =  document.getElementById('idpagecontent').scrollTop;
			divscoll_left = parseInt(divscoll_left);
			divscoll_top = parseInt(divscoll_top);
			
			//get absolute position of mouse
			var mx=e.clientX+lefttop[0] + divscoll_left;
			var my=e.clientY+lefttop[1] + divscoll_top;	
			
			
			//identify the mouse position, the mouse must be located in 
			var distance = get_point_distance(cx,cy,mx,my);		
			var submargin= 230;	//230
			var westsize = $("#westid").width();
			var t_track_key = findNearestRadiusAccordingDistance(distance);
	
			if(t_track_key != null){
				
				var leftsubmargin = westsize+30; // 225
				$("#label_"+t_track_key).css("left",mx-leftsubmargin).css("top",my-submargin);
				$("#label_"+t_track_key).css("display","block");
				
				//by default, we will draw a circlet line to show between this track
				var construct_track={"key":"showline"};
				var line_canvas1 = document.getElementById("canvas_"+construct_track.key);
				
				if(line_canvas1==null){
					var track_showline = $("#track_showline");
					if( track_showline.length <=0){
						track_create(construct_track,99);
						line_canvas1 = document.getElementById("canvas_"+construct_track.key);
					}
					
					
				}
				
				
				//we will show a draw line to ideogram 
				var cachename = cachetrackpos[t_track_key];
				//identify cache track 
				
				if(line_canvas1 != null){
					var t_div=$("#tracktext_"+t_track_key);
					if(t_div.length>0){
						t_div.empty();
					}else{
						t_div=$("<div/>",{
						id:"tracktext_"+t_track_key
						});
						t_div.appendTo($("#right_menu_"+t_track_key));
				    }
							
					//here we will use line_canvas to draw a track circlet line , find this track radius
					trackCircletAroundLine(line_canvas1,t_track_key,centerx,centery);
				
				
					var mouse_angle_pos = 180/Math.PI*Math.atan2((my-cy),(mx-cx));
					var mouse_angle;
					if(mouse_angle_pos >0 && mouse_angle_pos<=180 ){
						mouse_angle= mouse_angle_pos; // this is a reverse angle
					}else if(mouse_angle_pos >-180 && mouse_angle_pos<=0){
						mouse_angle = 360-Math.abs(mouse_angle_pos);
					}
					
						
					var mouse_radius = Math.sqrt((my-cy)*(my-cy)+(mx-cx)*(mx-cx));							
					var line_ctx1 = line_canvas1.getContext('2d');
			
				if(cachename!=null&&cachename.length>0){
					for(var i=0;i<cachename.length;i++){
						var cacheobj = cachename[i];
						var angle = cacheobj[0];
					
						var arcr = cacheobj[1];
						var arcr_height=cacheobj[2]; 
						var trackstrand = cacheobj[3];
						var angle1 = cacheobj[4];
						var gobj = cacheobj[5];
						var gname=  gobj.name;
						var gpos  = gobj.seq+":"+gobj.start+".."+gobj.end; 
						
						var x,y,x1,y1;
						var startangle;
						var endangle;
						if(trackstrand=='+'||trackstrand=="."){
						//	x = centerx+(arcr+arcr_height*3/4)*Math.cos(angle);
						//	y = centery+(arcr+arcr_height*3/4)*Math.sin(angle);
						//	x1 = centerx+(arcr+arcr_height*3/4)*Math.cos(angle1);
					    //    y1 = centery+(arcr+arcr_height*3/4)*Math.sin(angle1);
							x = centerx+(cur_radius)*Math.cos(angle);
							y = centery+(cur_radius)*Math.sin(angle);
							x1 = centerx+(cur_radius)*Math.cos(angle1);
					        y1 = centery+(cur_radius)*Math.sin(angle1);
							
							
							
							startangle = angle1*180/Math.PI;
							endangle = angle*180/Math.PI;
						}else if(trackstrand=='-'){
							//x = centerx+(arcr+arcr_height/4)*Math.cos(angle);
						//	y = centery+(arcr+arcr_height/4)*Math.sin(angle);
							//x1 = centerx+(arcr+arcr_height/4)*Math.cos(angle1);
					     //   y1 = centery+(arcr+arcr_height/4)*Math.sin(angle1);
							x = centerx+(cur_radius)*Math.cos(angle);
							y = centery+(cur_radius)*Math.sin(angle);
							x1 = centerx+(cur_radius)*Math.cos(angle1);
					        y1 = centery+(cur_radius)*Math.sin(angle1);
							
							startangle = angle*180/Math.PI;
							endangle = angle1*180/Math.PI;
						}
						
						
					//	console.log("mouse="+mouse_angle+","+mouse_radius+",angle="+angle+","+angle1+","+arcr+","+(arcr+arcr_height));
					//	console.log("mouse pos="+mx+","+my);
						
						if(mouse_radius >= arcr && mouse_radius <=(arcr+arcr_height) && mouse_angle >= startangle && mouse_angle<= endangle ){
							//we need to draw a line from current track to ideogram 
							var temp_leftmargin =430; // 225
							var temp_submargin = submargin;
							if(mouse_angle >= 0 && mouse_angle<=90){
								temp_leftmargin = westsize+30; // 225
							}else if(mouse_angle > 90 && mouse_angle<=180){
								temp_leftmargin = westsize+255; // 225
								temp_submargin = 350 ;
							}else if(mouse_angle > 180 && mouse_angle<=270){
								temp_leftmargin =westsize +255; // 225
								temp_submargin = 330 ;
							}							
							else if(mouse_angle > 270 && mouse_angle<360){
								temp_leftmargin = westsize+25; // 225
								temp_submargin = 330 ;
							}
							$("#label_"+t_track_key).css("left",mx-temp_leftmargin).css("top",my-temp_submargin);
							$("#right_menu_"+t_track_key).css("height","80px");
						
							var t_div_text="Name:"+gname+"<br/>Strand:"+trackstrand+"<br/>Position:"+gpos;
							t_div.html(t_div_text);
							
							//jumplink use current track position
							var jumplink = getJumpLink(gobj.seq,gobj.start,gobj.end,t_track_key);
							$("#right_menu_"+t_track_key+" #genomelink").attr("href",jumplink[0]);
							$("#right_menu_"+t_track_key+" #phylink").attr("href",jumplink[1]);

							var start_posx = centerx+radius*Math.cos(angle);
							var start_posy = centery+radius*Math.sin(angle);
							
						    var start_posx1 = centerx+radius*Math.cos(angle1);
							var start_posy1 = centery+radius*Math.sin(angle1);
								
							//console.log("start_position="+start_posx+","+start_posy+",end="+(x+ divscoll_left)+","+(y+divscoll_top));
							line_ctx1.beginPath();
							line_ctx1.setLineDash([10,5]);
							line_ctx1.lineWidth = 0.5;
							line_ctx1.strokeStyle = 'red';
							line_ctx1.moveTo(start_posx,start_posy);
							line_ctx1.lineTo(x,y);	
							
							line_ctx1.stroke();
							line_ctx1.setLineDash([10,5]);
							line_ctx1.lineWidth = 0.5;						
							line_ctx1.moveTo(start_posx1,start_posy1);
							line_ctx1.lineTo(x1,y1);
							line_ctx1.stroke();
							break;
						}else{
							$("#right_menu_"+t_track_key).css("height","0px");
							//line_ctx1.clearRect(0,0,canvas_width,canvas_height);
							trackCircletAroundLine(line_canvas1,t_track_key,centerx,centery);
						}
					   
					}
				 }
				}
				
				
				
			}else{ // mouse move out of the  track ,then all the popup dialog should dispear
				$(".track-label").css("display","none");
				$("#contextmenu_"+t_track_key).css("display","none");
			}
			
			submargin= 340;	
			var pheight = $("#trackContainer").height() ;
	        var subheight = canvas_width - pheight ;
			if(subheight<0){
				subheight = 0 ;
			}
			var temp_submargin = 230 + subheight;
			var temp_leftmargin = westsize+50;
			//console.log("pheight="+pheight+",temp_submargin="+temp_submargin+",divscoll_top"+divscoll_top);
		    //we need to triggle bardiv (anchor target)
			if(bararry.length >0 ){
				for(var i=0;i< bararry.length;i++){
					var barstore = bararry[i];
					var barx = barstore.x + pos[0];
					var bary = barstore.y + pos[1];
					var barwidth = 6* Math.sqrt(2);
					
					if(mx >= (barx-barwidth/2) && mx <=(barx+barwidth/2) && my >= (bary - barwidth/2) && my<=(bary + barwidth/2)){
						//console.log("mouse pos "+mx+","+my+"  ,bar store pos="+barx+","+bary+" "+barstore.divid);
						$("#"+barstore.divid).css("left",mx-temp_leftmargin).css("top",my-temp_submargin).css("display","block");
						break;
					}else{
						$("#"+barstore.divid).css("display","none");
					}
					
				}	
			}
				
}


//when mouse right click, a context menu will be shown
function extend_canvas_cxtmenu_event(e){
			
		 e.preventDefault();
		 canvas.removeEventListener('mousemove', extend_canvas_mouse_move, false);
		var pos=absolutePosition(canvas);
			var centerx=parseInt(canvas.width/2);
			var centery=parseInt(canvas.height/2);
			
			//get absolute center position of canvas
			var cx=pos[0]+centerx;
			var cy=pos[1]+centery;	

			var lefttop=get_page_left_top();
	
			
			//div scroll left and top
			var divscoll_left=  document.getElementById('idpagecontent').scrollLeft;
			var divscoll_top =  document.getElementById('idpagecontent').scrollTop;
			divscoll_left = parseInt(divscoll_left);
			divscoll_top = parseInt(divscoll_top);
			
			//get absolute position of mouse
			var mx=e.clientX+lefttop[0] + divscoll_left;
			var my=e.clientY+lefttop[1] + divscoll_top;	
			
			var distance = get_point_distance(cx,cy,mx,my);		
			var submargin= 230;	//230
			var westsize = $("#westid").width();
			var t_track_key = findNearestRadiusAccordingDistance(distance);
	
			if(t_track_key != null){
				//console.log("context menu="+t_track_key);
				var leftsubmargin = westsize+30; // 225
				var contextmenudiv = $("#contextmenu_share");
			if(contextmenudiv.length<=0){
				contextmenudiv = $('<div/>',{
					id:"contextmenu_share", 
					css:{
						position : 'absolute', 
						left: '0px',
						top: canvas_height/2+'px',
						'font-size':'12px',
						display: 'none'
					}
				});		
				var topdiv = $("#idcirclet") ;
				contextmenudiv.appendTo(topdiv); // topdiv
			}
					
				
				
				$("#label_"+t_track_key).css("display","none");
				if(contextmenudiv != null){
					$("#contextmenu_share").css("left",mx-leftsubmargin).css("top",my-submargin);
				
					$("#contextmenu_share").css("display","block");
					$("#contextmenu_share").html("");
				}
				var contextmenutext_div= $("#contextmenu_text_share");
				if(contextmenutext_div.length<=0){
					contextmenutext_div= $("<div/>",{
					id:"contextmenu_text_share",
					css:{
						'border':'1px solid #000000',
						'background':'white',
						'color':'#ffffff',
						'height':'80px',
						'width':'100px',
						'font-size':'12px'
					}
					});
				}
				if(contextmenutext_div != null){
					
					var link_genome="";
					var link_physical="";
					var conf = getQueryString("conf");
					var opkey = t_track_key;
					
					if(conf != null){
						link_genome="http://"+window.location.host+"/jbrowse/index.html?data="+conf+"&loc="+chr+"%3A"+ideogram_start+".."+ideogram_end+"&tracks="+t_track_key;
					
						if(opkey == "transcript"){
							opkey = "ensembl_gene";
						}
					   link_physical="http://"+window.location.host+"/circosweb/pages/visualization/physical_view.jsp?conf="+conf+"&loc="+chr+"%3A"+ideogram_start+".."+ideogram_end+"&tracks=3dmodel,"+opkey;
					}else{
						link_genome="http://"+window.location.host+"/jbrowse/index.html?data=human&loc="+chr+"%3A"+ideogram_start+".."+ideogram_end+"&tracks="+t_track_key;
						if(opkey == "transcript"){
							opkey = "ensembl_gene";
						}
						 link_physical="http://"+window.location.host+"/circosweb/pages/visualization/physical_view.jsp?loc="+chr+"%3A"+ideogram_start+".."+ideogram_end+"&tracks="+opkey;
					}
				
					var htmltext ="<p style='line-height:1;width:80px;'><a href='javascript:deleteOneTrackFromCanvas(\""+t_track_key+"\",\"track\")' onclick='deleteOneTrackFromCanvas(\""+t_track_key+"\",\"track\")'>Delete track</a></p>";
					
					
					
					var cachename = cachetrackpos[t_track_key];
					var mouse_angle_pos = 180/Math.PI*Math.atan2((my-cy),(mx-cx));
					var mouse_angle;
					if(mouse_angle_pos >0 && mouse_angle_pos<=180 ){
						mouse_angle= mouse_angle_pos; // this is a reverse angle
					}else if(mouse_angle_pos >-180 && mouse_angle_pos<=0){
						mouse_angle = 360-Math.abs(mouse_angle_pos);
					}
					
				var idenfindtrack = 0 ;	
				var mouse_radius = Math.sqrt((my-cy)*(my-cy)+(mx-cx)*(mx-cx));							
				
			
				if(cachename!=null&&cachename.length>0){
					for(var i=0;i<cachename.length;i++){
						var cacheobj = cachename[i];
						var angle = cacheobj[0];
					
						var arcr = cacheobj[1];
						var arcr_height=cacheobj[2]; 
						var trackstrand = cacheobj[3];
						var angle1 = cacheobj[4];
						var gobj = cacheobj[5];
						var gname=  gobj.name;
						var gpos  = gobj.seq+":"+gobj.start+".."+gobj.end; 
						
						var x,y,x1,y1;
						var startangle;
						var endangle;
						if(trackstrand=='+'||trackstrand=="."){
							x = centerx+(arcr+arcr_height*3/4)*Math.cos(angle);
							y = centery+(arcr+arcr_height*3/4)*Math.sin(angle);
							x1 = centerx+(arcr+arcr_height*3/4)*Math.cos(angle1);
					        y1 = centery+(arcr+arcr_height*3/4)*Math.sin(angle1);
							
							startangle = angle1*180/Math.PI;
							endangle = angle*180/Math.PI;
						}else if(trackstrand=='-'){
							x = centerx+(arcr+arcr_height/4)*Math.cos(angle);
							y = centery+(arcr+arcr_height/4)*Math.sin(angle);
							x1 = centerx+(arcr+arcr_height/4)*Math.cos(angle1);
					        y1 = centery+(arcr+arcr_height/4)*Math.sin(angle1);
							startangle = angle*180/Math.PI;
							endangle = angle1*180/Math.PI;
						}
						
						
					//	console.log("mouse="+mouse_angle+","+mouse_radius+",angle="+angle+","+angle1+","+arcr+","+(arcr+arcr_height));
					//	console.log("mouse pos="+mx+","+my);
						if(mouse_radius >= arcr && mouse_radius <=(arcr+arcr_height) && mouse_angle >= startangle && mouse_angle<= endangle ){
							//we need to draw a line from current track to ideogram 
							var temp_leftmargin =430; // 225
							var temp_submargin = submargin;
							if(mouse_angle >= 0 && mouse_angle<=90){
								temp_leftmargin = westsize+30; // 225
							}else if(mouse_angle > 90 && mouse_angle<=180){
								temp_leftmargin = westsize+255; // 225
								temp_submargin = 350 ;
							}else if(mouse_angle > 180 && mouse_angle<=270){
								temp_leftmargin =westsize +255; // 225
								temp_submargin = 330 ;
							}							
							else if(mouse_angle > 270 && mouse_angle<360){
								temp_leftmargin = westsize+25; // 225
								temp_submargin = 330 ;
							}
							
							//jumplink use current track position
							var jumplink = getJumpLink(gobj.seq,gobj.start,gobj.end,t_track_key);
							htmltext +="<p style='line-height:1;width:80px;'><a href='"+jumplink[0]+"' id='genomelink' target='_blank'>Genome View</a></p>";
							htmltext+="<p style='line-height:1;width:80px;'><a href='"+jumplink[1]+"' id='phylink' target='_blank'>Physical View</a></p>";
							idenfindtrack = 1;
							break;
							
						}					
					}
				}
				if(idenfindtrack == 0 ){
					htmltext +="<p style='line-height:1;width:80px;'><a href='"+link_genome+"' id='genomelink' target='_blank'>Genome View</a></p>";
					htmltext+="<p style='line-height:1;width:80px;'><a href='"+link_physical+"' id='phylink' target='_blank'>Physical View</a></p>";
				}
					
					contextmenutext_div.html(htmltext);
					
					contextmenutext_div.appendTo(contextmenudiv); 
				}
								
			
			}else{
				$("#contextmenu_text_share").css("display","none");
				
			}
}


//when mouse move along the outer circlet track, a highlight and align line will be shown
function outercirclet_view_move(event){
	var bm=outergflag.outermove;
	//get mouse position
	var lefttop=get_page_left_top();
	var divscoll_left=  document.getElementById('idpagecontent').scrollLeft;
    var divscoll_top =  document.getElementById('idpagecontent').scrollTop;
	
	var x=event.clientX+lefttop[0]+divscoll_left,y=event.clientY+lefttop[1]+divscoll_top;
	//canvas center abosolute position bm.cx,bm.cy
	var curr_radian=get_point_radian(bm.cx,bm.cy,x,y);
	bm.end_radian = curr_radian ;	
	if(bm.track_key!= null ){
		var track_arry= findTrackRadius(bm.track_key);
		var track_tradius = parseInt(track_arry[0]);
		var track_height = parseInt(track_arry[1]);
		var drawradius = track_tradius -10;
		var spanradian = bm.end_radian - bm.old_radian;
		if(bm.end_radian< bm.old_radian){
			spanradian = bm.old_radian - bm.end_radian;
			outercircletview_paint_glasspane(bm.end_radian,spanradian,drawradius,track_height,bm.track_key); //startradian,spanradian,drawradius
		}else{
			outercircletview_paint_glasspane(bm.old_radian,spanradian,drawradius,track_height,bm.track_key); //startradian,spanradian,drawradius
		}
		
	}
}


//when mouse up along the outer circlet track, the aligned line will be shown
function outercirclet_view_select()
{

	document.body.removeEventListener('mousemove', outercirclet_view_move, false);
	document.body.removeEventListener('mouseup', outercirclet_view_select, false);
	var bm=outergflag.outermove;
		
	//here ,we began to draw red line between track and ideogram
	var x,y,x1,y1;
	var angle = bm.old_radian;
	var angle1 = bm.end_radian;
	if(bm.track_key!= null ){
		var track_arry= findTrackRadius(bm.track_key);
		var track_tradius = parseInt(track_arry[0]);
		track_tradius +=5 ;
		var construct_track={"key":"showline"};
		
		var line_ctx1=glasspane.getContext('2d');
		
		var centerx=parseInt(canvas.width/2);
		var centery=parseInt(canvas.height/2);
		
		//x = centerx+track_tradius*Math.cos(angle);
		//y = centery+track_tradius*Math.sin(angle);
		//x1 = centerx+track_tradius*Math.cos(angle1);
		//y1 = centery+track_tradius*Math.sin(angle1);
		
		x = centerx+cur_radius*Math.cos(angle);
		y = centery+cur_radius*Math.sin(angle);
		x1 = centerx+cur_radius*Math.cos(angle1);
		y1 = centery+cur_radius*Math.sin(angle1);
			
		var start_posx = centerx+radius*Math.cos(angle);
		var start_posy = centery+radius*Math.sin(angle);
								
		var start_posx1 = centerx+radius*Math.cos(angle1);
		var start_posy1 = centery+radius*Math.sin(angle1);
			
			
			
		line_ctx1.beginPath();
		line_ctx1.setLineDash([10,5]);
		line_ctx1.lineWidth = 0.5;
		line_ctx1.strokeStyle = 'red';
		line_ctx1.moveTo(start_posx,start_posy); // the ideogram position
		line_ctx1.lineTo(x,y);	
								
		line_ctx1.stroke();
		line_ctx1.setLineDash([10,5]);
		line_ctx1.lineWidth = 0.5;						
		line_ctx1.moveTo(start_posx1,start_posy1); // the ideogram position
		line_ctx1.lineTo(x1,y1);
		line_ctx1.stroke();			
	}
}


function outercircletview_paint_glasspane(startradian,spanradian,drawradius,track_height,trackkey)
{
	var centerx=parseInt(canvas.width/2);
	var centery=parseInt(canvas.height/2);

	var x=centerx, y=centery;

	var ctx=glasspane.getContext('2d');
	ctx.clearRect(0,0,glasspane.width,glasspane.height);
	ctx.strokeStyle='rgba(255,102,51,.3)';
	ctx.beginPath();

	if(trackkey == "ensembl_gene"){
		ctx.lineWidth=track_height+5; // draw the selected track region
		ctx.arc(x,y,drawradius+track_height/2+15,startradian,startradian+spanradian,false);
	}else{
		
	  
		ctx.lineWidth=track_height+5; // draw the selected track region
		var t_draw = drawradius+track_height-5;
		var t_arc_end = startradian+spanradian;
		ctx.arc(x,y,t_draw,startradian,t_arc_end,false);
	}
	
	ctx.stroke();
	//ctx.endPath();
}
