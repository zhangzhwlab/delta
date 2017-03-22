var config={
	s_bin:5,
	matrix_width:585,
	matrix_height:420,
	matrix_file:""
}
var chrom_lst=[];
var binsize;
var qbin_start;
var qbin_end;
var resolution;

var matrix_width_px=config.matrix_width;
var matrix_height_px=config.matrix_height;

var cur_point_x_bin =1;
var px_per_bin = matrix_width_px/binsize;
var py_per_bin = matrix_height_px/binsize;

var s_bin = config.s_bin; // 5*5
//115,//285
function loadLayout(){
	$("#waitid").css("display","none");
	document.getElementById("matriximg").addEventListener("mousedown",mouse_down_func,false);

	loadInitCircos();
}

function mouse_down_func(e){
	e.preventDefault();
	
	var point_pos = getPosition(e);
	
    document.getElementById("matriximg").addEventListener("mousemove",mouse_move_func,false);
	document.getElmentById("matriximg").addEventListener("mouseup",mouse_up_func,false);
	
}

function mouse_up_func(e){
	
}


function mouse_move_func(e){
	

		px_per_bin = matrix_width_px/binsize;
		py_per_bin = matrix_height_px/binsize ;
		var point_pos = getPosition(e);
		
		var point_x = point_pos.x;
		var point_y = point_pos.y;
		if(point_x >=105 && point_x <= 690 && point_y>=280 && point_y<= 690){
			
		//alert(point_x+","+point_y);
		var r_point_x = point_x - 105;
		var r_point_y = point_y - 270 ;
		var point_x_bin = Math.ceil( r_point_x / px_per_bin);
		var point_y_bin = Math.ceil( r_point_y / py_per_bin );
		//alert(point_x_bin+","+point_y_bin);
		cur_point_x_bin = point_x_bin;
		var matrix_left = ( point_x_bin -1)* px_per_bin +95;
		var px_matrix_left = matrix_left+"px";
		var matrix_top =  ( point_y_bin -1) * py_per_bin +120;
		var px_matrix_top= matrix_top + "px";
		//alert(s_bin);
		var matrix_width = s_bin * px_per_bin;
		var px_matrix_width = matrix_width+"px";
		var matrix_height = s_bin * py_per_bin ;
		var px_matrix_height = matrix_height +"px";
		//alert(matrix_left+","+matrix_top+","+matrix_width+","+matrix_height);
		
		var matrix_right = matrix_left +matrix_width;
		var px_matrix_right = matrix_right+"px";
		var matrix_bottom = matrix_top+ matrix_height;
		var px_matrix_bottom = matrix_bottom+"px" ;
		$("#area").css("left",px_matrix_left);
		$("#area").css("top",px_matrix_top);
		$("#area").css("width",px_matrix_width);
		$("#area").css("height",px_matrix_height);
		
		
		//add mouse click event
		document.getElementById("matriximg").addEventListener("click",mouse_click_func,false);
		
		
		}
}

function mouse_click_func(e){
	e.preventDefault();
	qbin_start = parseInt(cur_point_x_bin);
	qbin_end = parseInt(qbin_start) + s_bin -1;
	var q_chr = $("#chromid").val();
	var query_bin = q_chr+":"+qbin_start+".."+qbin_end;
	$("#curpos").val(query_bin);
	
	binsize = qbin_end - qbin_start;
	px_per_bin = matrix_width_px/binsize;
	py_per_bin = matrix_height_px/binsize;
	if( binsize <= 550 ){
		//draw_matrix_bysvg();
		draw_with_d3();
	}else{
		ajax_getMatrixPic();
	}
		
}


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
//zoom function only apply to the binsize

//zoom in
function zoomin(binstart,binend,fold){
	var range =  parseInt((binend - binstart) / fold);

	qbin_start = parseInt((binend + binstart) / 2 - (binend - binstart)
				/ (fold * 2));

	qbin_end =  parseInt((binend + binstart) / 2 + (binend - binstart)
				/ (fold * 2));
				
	var q_chr = $("#chromid").val();
	var query_bin = q_chr+":"+qbin_start+".."+qbin_end;
	$("#curpos").val(query_bin);
	
	binsize = qbin_end - qbin_start;
	px_per_bin = matrix_width_px/binsize;
	py_per_bin = matrix_height_px/binsize;
	
	if( binsize <= 550 ){
		//draw_matrix_bysvg();
		draw_with_d3();
	}else{
		ajax_getMatrixPic();
	}

}

//zoom out
function zoomout(binstart,binend,fold){
	var range = parseInt((binend - binstart) * fold);
	if(range > binsize){
		qbin_start =1;
		qbin_end=binsize;
	}

	qbin_start =  parseInt((binstart+binend)/2 - (binend-binstart)*fold/2 );
	if(qbin_start <=0){
		qbin_start = 1;
	}

	qbin_end = parseInt((binstart+binend)/2 + (binend-binstart)*fold/2 );
	var q_chr = $("#chromid").val();
	var query_bin = q_chr+":"+qbin_start+".."+qbin_end;
	$("#curpos").val(query_bin);
	
	binsize = qbin_end - qbin_start;
	px_per_bin = matrix_height_px/binsize;
	py_per_bin = matrix_height_px/binsize;
	
	if( binsize <= 550 ){
		//draw_matrix_bysvg();
		draw_with_d3();
	}else{
		ajax_getMatrixPic();
	}

}


function draw_with_d3(){
	var width = 600,
    height = 480;
	var params={"binStart":qbin_start,"binEnd":qbin_end};
		$.ajax({
		url:'/circosweb/ajax/drawMatrixByjs.action?time='+new Date().getTime(),
	    type:'post',
		dataType:'json',
		data:params,
		success:function(value){
			//if(value.jsonStr!=null){
						//d3.json("/circosweb/json/heatmap.json?_="+new Date().getTime(), function(error, heatmap) {
						  //if (error) throw error;
						  var heatmap = eval("("+value.jsonStr+")");
						  var svgobj =  d3.select("svg");
						  if(svgobj != null){
							  svgobj.remove();
						  }
						  
						  var canvasobj = d3.select("canvas");
						  if(canvasobj !=null ){
							  canvasobj.remove();
						  }
						  
						  var margin = {top: 20, right: 20, bottom: 60, left: 20},
							swidth = width - margin.left - margin.right,
							sheight = height - margin.top - margin.bottom;

						  
							//alert(heatmap[0].length);
						  var dx = heatmap[0].length,
							  dy = heatmap.length;

						  // Fix the aspect ratio.
						  // var ka = dy / dx, kb = height / width;
						  // if (ka < kb) height = width * ka;
						  // else width = height / ka;

						  var canvasx = d3.scale.linear()
							  .domain([qbin_start, qbin_end+1])
							  .range([0, swidth]);
						var svgx = d3.scale.linear()
							  .domain([(qbin_start)*resolution/(1000*1000), (qbin_end+1)*resolution/(1000*1000)])
							  .range([0, swidth]);

							  var canvasy = d3.scale.linear()
							  .domain([qbin_start, qbin_end+1])
							  .range([sheight, 0]);
							  
						  var svgy = d3.scale.linear()
							  .domain([(qbin_start)*resolution/(1000*1000), (qbin_end+1)*resolution/(1000*1000)])
							  .range([sheight, 0]);
							  
						
						var color_range = colorbrewer.YlOrRd[9];
						  var color = d3.scale.quantile()
							  .domain([1,6])
							  .range(color_range);

						  var formatAxis = d3.format("0.2f");
						  var xAxis = d3.svg.axis()
							  .scale(svgx)
							  .tickFormat(formatAxis)
							  .ticks(5)
							  .orient("bottom");

						  var yAxis = d3.svg.axis()
							  .scale(svgy).tickFormat(formatAxis)
							  .ticks(5)
							  .orient("left");

						  d3.select("#heatmapdiv").append("canvas")
							    .attr("width", width)
								.attr("height", height)
							  .style("width", width+ "px")
							  .style("height", height + "px")
							  .call(drawImage);

						  var svg = d3.select("#heatmapdiv").append("svg")
								
							  .attr("width", width)
							  .attr("height", height);

							svg.append("g")
						  .attr("class", "x axis")
						  .attr("transform", "translate(30,400)")
						  .call(xAxis)
						  .call(removeZero);
						  
						  svg.append("text")
							.attr("x", (width / 2))             
							.attr("y", height-40)
							.attr("text-anchor", "middle")  
							.style("font-size", "16px") 
							.style("text-decoration", "none")  
							.text("Chrom21 MB(resolution:40kb)");

						  svg.append("g")
							  .attr("class", "y axis")
							  .attr("transform", "translate(0,0)")
							  .call(yAxis)
							  .call(removeZero);  
							  
							svg.append("text")
							.attr("x", (width / 2))             
							.attr("y", 0 - (margin.top / 2))
							.attr("text-anchor", "middle")  
							.style("font-size", "16px") 
							.style("text-decoration", "none")  
							.text("log2(Interaction Matrix) Chrom21 MB(resolution:40kb)");
							  
							  
						var legend = svg.selectAll('g.legendEntry')
								.data(color.range())
								.enter()
								.append('g').attr('class', 'legendEntry');

							legend
								.append('rect')
								.attr("x", function(d, i) {
								   return i * 60;
								})
								.attr("y", height-30)
							   .attr("width", 540)
							   .attr("height", 15)
							   .style("stroke", "black")
							   .style("stroke-width", 1)
							   .style("fill", function(d){return d;}); 
								   //the data objects are the fill colors

							legend
								.append('text')
								.attr("x", function(d, i) {
								   return i * 60;
								}) //leave 5 pixel space after the <rect>
								.attr("y", height-10)
								.attr("dy", "0.8em") //place text one line *below* the x,y point
								.text(function(d,i) {
									var extent = color.invertExtent(d);
									//extent will be a two-element array, format it however you want:
									var format = d3.format("0.2f");
									return format(+extent[0]);
								});	  
							  

						  // Compute the pixel colors; scaled by CSS.
						  function drawImage(canvas) {
							var context = canvas.node().getContext("2d");
							context.clearRect(0,0,width,height);
						//	var	image = context.createImageData(dx, dy);
							var p_y = qbin_end+1;
							var p_x = qbin_start;
							for (var ty = dy-1, p = -1; ty > -1; --ty) {
								
								p_x = qbin_start+1;
							  for (var tx = 0; tx < dx; ++tx) {
								//console.log(ty+","+tx); 
									 context.beginPath();
								  console.log(canvasx(p_x)+","+canvasy(p_y)+","+px_per_bin+","+py_per_bin);
								  context.rect(canvasx(p_x), canvasy(p_y), px_per_bin, py_per_bin);
								  var tc = color(heatmap[ty][tx]);
								  context.fillStyle= tc;
								  context.fill();
								  context.closePath()
								  p_x++;
								  
								   
								/*var c = d3.rgb(color(heatmap[ty][tx]));
								image.data[++p] = c.r;
								image.data[++p] = c.g;
								image.data[++p] = c.b;
								image.data[++p] = 255;*/
							  }
							  p_y--;
							}

							//context.putImageData(image, 0, 0);
						  }

						  function removeZero(axis) {
							axis.selectAll("g").filter(function(d) { return !d; }).remove();
						  }
						//});
					
					
			//}

		},
		error:function(e){
			alert("error");
		}
		
	});
	
	


	$("#matriximg").css("display","none");
	//area
	$("#area").css("display","none");
	$("#waitid").css("display","none");

}


function ajax_getMatrixPic(){
	$("svg").css("display","none");
	$("canvas").css("display","none");
	var q_chr = $("#chromid").val();
	//var dataset = $("#dset").val();
	var params={"chrom":q_chr,"binStart":qbin_start,"binEnd":qbin_end,"dataset":config.matrix_file};
	$("#waitid").css("display","block");
	//ajax used to get density data and draw histogram picture
	$.ajax({
		url:'/circosweb/ajax/drawMatrix.action',
	    type:'post',
		dataType:'json',
		data:params,
		success:function(value){
			if(value.image!=null){
				$("#matriximg").css("display","block");
					//area
					$("#area").css("display","block");
					$("#waitid").css("display","none");
				$("#matriximg").attr("src",value.image);
			}

		},
		error:function(e){
			alert("error");
		}
	}); 
	
}
