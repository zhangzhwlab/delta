define(['dojo/_base/declare',
           'dojo/_base/lang',
           './Box'],
       function(declare,
           lang,
           Box) {
		   
return declare(Box, {
_getQueryString: function(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
    },	
//重新构造显示在矩形里面的feature，需要用成对的相互作用区域计算出显示在当前这个长方形区域内的boxstart以及boxwidth
_getFeatureRectangle: function( viewArgs, feature ) {

    // lay out rects for each of the subfeatures
    //var subArgs = lang.mixin( {}, viewArgs );
    //subArgs.showDescriptions = subArgs.showLabels = false;
    //var subfeatures = feature.children();
	//var noteattribute = feature.get('note');
	//alert("get rectangle ="+noteattribute);
    // get the rects for the children
    var padding = 1;
    var fRect = {
        l: 0,
        h: 0,
		t: 0,
        showfeature: {},
        viewInfo: viewArgs,
        f: feature,
        glyph: this
    };
	var mait_end = 0;
	var view_end = 0;
	//note feature have value
	
		fRect.l = Infinity;
		
		
		var anchor_start = feature.get('start');
		var anchor_end = feature.get('end');
		//compute the box start;
		var boxstart ;
		var boxwidth ;
		
		var block = fRect.viewInfo.block;
		
		
		var scale = viewArgs.scale; //每个碱基所占的图片像素		
		var view_start = anchor_start;
		view_end = anchor_end;
		var argule = 0 ;
	
		
			//fRect.l =  block.bpToX(view_start); //将碱基转换为了图片像素
			//identify whether current window loation large than the view start
			boxstart = block.bpToX(view_start);
			
			
			boxwidth =  block.bpToX(view_end) - boxstart;
			/*if(view_start > scope_start){
				boxwidth = (view_end-scope_start) * scale - (view_start-scope_start)* scale;
			}else{
				boxwidth = (view_end-scope_start )* scale ;
			}*/
			var radius = Math.max(0,boxwidth/Math.SQRT2-1/2);
			var maxheight = radius - boxwidth/2;
			
			
			//var xian_length = Math.sqrt(radius*radius-boxwidth*boxwidth/4);
			fRect.l = boxstart ;
			//if(scope_start > view_start){
			//	fRect.l = scope_start_px ;
			//}
			//fRect.h = Math.max(0,radius - boxwidth/2);
			//fRect.h = maxheight;
			//fRect.h  = Math.max(20,maxheight/5 );
			//alert(fRect.h);
			//if(fRect.h >= 0 ){
				fRect.h = Math.max(20,radius - boxwidth/2);
				//fRect.h = radius;
			//}
			//fRect.w = boxwidth;
		
			fRect.showfeature={'boxstart':fRect.l,'boxwidth':boxwidth,'argule':argule};
			var startpix = view_start*scale;
			//console.log("start="+view_start+",view_end="+view_end+",radius="+radius+",boxstart="+boxstart+",scope_str="+scope_start_px+",boxwidth="+boxwidth+",absolute pos string="+block.toString()+",fRect.w="+fRect.w+",fRect.l="+fRect.l);
		

	
	
		fRect.rect = { l: fRect.l, h: fRect.h, w: fRect.w };
		 // no labels or descriptions if displayMode is collapsed, so stop here
		if( viewArgs.displayMode == "collapsed")
			return fRect;
	
	this._expandRectangleWithLabels( viewArgs, feature, fRect );
    this._addMasksToRect( viewArgs, feature, fRect );	
	return fRect;
},

/*
layoutFeature: function( viewInfo, layout, feature ) {
	//alert("Gene glyph ,layoutFeature");
    var fRect = this.inherited( arguments );
     
    return fRect;
},*/

  
//this used to draw arc line
renderFeature: function( context, fRect ) {//用canvas绘制Hic的topological associated domain图，传入进来的fRect是否已经包含了数据feature
				
		    if( this.track.displayMode != 'collapsed' )
				context.clearRect( Math.floor(fRect.l), fRect.t, Math.ceil(fRect.w-Math.floor(fRect.l)+fRect.l), fRect.h );
				
				/*if(fRect.showfeature.length ==0 ){
					return ;
				}*/
				
				var pr=184;
				var pg=0;
				var pb=138;
				var nr=0;
				var ng=99;
				var nb=133;
				var pcolorscore=4;
				var ncolorscore=-4;
				var yoffset= 0;
				var qual_score =  3;
				
			    var color= (qual_score>= 0) ? 
					'rgba('+pr+','+pg+','+pb+','+Math.min(1,qual_score/pcolorscore)+')' :
					'rgba('+nr+','+ng+','+nb+','+Math.min(1,qual_score/ncolorscore)+')';

				var boxwidth = fRect.showfeature.boxwidth;
				var boxstart = fRect.showfeature.boxstart;
				var centerx = boxstart + boxwidth/2;
				var centery = yoffset - boxwidth/2;
				var arcwidth=1; // TODO arc width auto-adjust
				var radius = Math.max(0,boxwidth/Math.SQRT2-arcwidth/2);
				var canvaswidth= document.getElementById("track_Gene").style.height ;
				console.log("source height="+canvaswidth);
				var index = canvaswidth.indexOf("px") ;
				var canvasstr = 320 ;
				if(index > -1){
					canvaswidth = canvaswidth.substring(0,index);
				}
				//canvaswidth += canvasstr;
				console.log("canvaswidth="+canvaswidth);
				//if(canvaswidth !=0 && radius< 350){
					context.strokeStyle = color;
					context.lineWidth = arcwidth;
					context.beginPath();
					//var angle = fRect.showfeature.argule;
					context.arc(centerx, centery, radius, 0.25*Math.PI, 0.75*Math.PI, false);
					console.log("centerx="+centerx+",centery="+centery+",radius="+radius);
					context.stroke();
				//}
				
				
	}

});
});
