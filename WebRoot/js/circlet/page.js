/*this is used to create an individual div for each track and we can do futhur operation for each track
*each div has a id , track_[key]
* a track label
* a canvas object
* to decide the z-index
*/
var zindex=100; // default canvas index is 101
var t_count =0;

function track_create(trackobj,trackindex){
	t_count ++;
	var label_h = 0 ;
	if(t_count %2 ==0){
		label_h = -20;
	}else{
		label_h = 0 ;
	}
	var t_zindex = zindex - trackindex;
	var t_width = canvas_width;  // 660
	var t_height = canvas_height;//660
	
	var t_top= 0- t_height-8;
	t_top= t_top+'px';
	var div = $('<div/>',{
		id:"track_"+trackobj.key, 
		css:{
			position : 'relative', 
			background: 'white none repeat scroll 0 0',
			'margin-top' : t_top,
			'font-size':'12px'
		}
	});
	
	//get track radius and decide left distance

	var labeldiv= $("<div/>",{
		id:"label_"+trackobj.key,
		css:{
			position:'absolute',			
			left: '0px',
			top: t_height/2+label_h+'px',
			display: 'none'
		}
	}).addClass("track-label");
	var close_img = $("<img/>",{
		src:"/circosweb/images/tabClose.png",
		id:"img_"+trackobj.key
	});
	close_img.appendTo(labeldiv);
	var span_text = $("<span/>",{
		text:trackobj.key
	});
	span_text.appendTo(labeldiv);
	
	var itemmenu_div = $("<div/>",{
		id:"menu_"+trackobj.key
	}).addClass("track-menu-button");
	itemmenu_div.appendTo(labeldiv);
	
	
	var menu_div= $("<div/>",{
				id:"right_menu_"+trackobj.key,
				css:{
					'border':'1px solid #000000',
					'background':'blue',
					'color':'#ffffff',
					'height':'0px',
					'font-size':'12px'
				}
				});
			
				
				menu_div.appendTo(labeldiv);	

	
	
	var track_canvas=$("<canvas/>",{
		id:"canvas_"+trackobj.key,
		css:{
			position:'absolute',
			left:'150px',
			top:'8px',
			'z-index':t_zindex
		}

	}).attr("width",t_width).attr("height",t_height);
	
	
	
	
	labeldiv.appendTo(div);
	track_canvas.appendTo(div);
	div.appendTo("#trackContainer");
	
	//register mouse over event
	
	
	//close bind click event,then need to romove existed track and adjust circlet layout
	$("#img_"+trackobj.key).click(function(){	
		deleteOneTrackFromCanvas(trackobj.key,trackobj.glyph);
	});		
}

function showLableTrack(e){
			e.preventDefault();
			var pos=absolutePosition(this);
			var centerx=parseInt(t_width/2);
			var centery=parseInt(t_height/2);
			//get absolute center position of canvas
			var cx=pos[0]+centerx;
			var cy=pos[1]+centery;	
			var lefttop=get_page_left_top();
			
			
			//get absolute position of mouse
			var mx=e.clientX+lefttop[0];
			var my=e.clientY+lefttop[1];
			
			//identify the mouse position, the mouse must be located in 
			var distance = get_point_distance(cx,cy,mx,my);
			var track_arry = findTrackRadius(trackobj.key);
			var track_radius = parseInt(track_arry[0]);
			
			console.log("radius="+track_radius+" "+distance);
			
			if(distance>= track_radius && distance <= (track_radius+50)){
				labeldiv.css("display","block"); // set th
			}else{
				labeldiv.css("display","none"); 
			}

}
		



