var binsize=1174;
var qbin_start=1;
var qbin_end=1174;

var matrix_width_px=585;
var matrix_height_px=420;

var cur_point_x_bin =1;
var px_per_bin = 585/binsize;
var py_per_bin = 420/binsize;

var s_bin = 5; // 5*5
//115,//285
function loadLayout(){
	$("#waitid").css("display","none");
	document.getElementById("matriximg").addEventListener("mousemove",mouse_move_func,false);
	
}


function mouse_move_func(e){
	
		e.preventDefault();
		px_per_bin = 585/binsize;
		py_per_bin = 420/binsize ;
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
	qbin_start = cur_point_x_bin;
	qbin_end = qbin_start + s_bin -1;
	var q_chr = $("#chromid").val();
	var query_bin = q_chr+":"+qbin_start+".."+qbin_end;
	$("#curpos").val(query_bin);
	ajax_getMatrixPic();
	binsize = qbin_end - qbin_start;	
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
	if( binsize <= 20 ){
		//draw_matrix_bysvg();
		draw_with_d3();
	}else{
		ajax_getMatrixPic();
	}
	
    
	
		//var t_range_scope = ideogram_end - ideogram_start;
		//if(t_range_scope > toomany_threshold){ //5 Mb
			//$("#err").css("display","");
			//return -1;
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
	ajax_getMatrixPic();
	binsize = qbin_end - qbin_start;
}


function draw_with_d3(){
	var width = 585,
    height = 420;
	var params={"qbin_start":qbin_start,"qbin_end":qbin_end};
		$.ajax({
		url:'/circosweb/ajax/drawMatrixByjs.action',
	    type:'post',
		dataType:'json',
		data:params,
		success:function(value){
			if(value.jsonStr!=null){
						d3.json("/circosweb/json/heatmap.json", function(error, heatmap) {
						  if (error) throw error;
							alert(heatmap.length);
						  var dx = heatmap.length,
							  dy = heatmap.length;

						  // Fix the aspect ratio.
						  // var ka = dy / dx, kb = height / width;
						  // if (ka < kb) height = width * ka;
						  // else width = height / ka;

						  var x = d3.scale.linear()
							  .domain([qbin_start, qbin_end])
							  .range([0, width]);

						  var y = d3.scale.linear()
							  .domain([qbin_start, qbin_end])
							  .range([height, 0]);

						  var color = d3.scale.linear()
							  .domain([1, 0, 8])
							  .range(colorbrewer.YlOrRd[3]);

						  var xAxis = d3.svg.axis()
							  .scale(x)
							  .orient("bottom")
							  .ticks(binsize);

						  var yAxis = d3.svg.axis()
							  .scale(y)
							  .orient("left").ticks(binsize);

						  d3.select("#heatmapdiv").append("canvas")
							  .attr("width", dx)
							  .attr("height", dy)
							  .style("width", width + "px")
							  .style("height", height + "px")
							  .call(drawImage);

						  var svg = d3.select("#heatmapdiv").append("svg")
							  .attr("width", width)
							  .attr("height", height);

						  svg.append("g")
							  .attr("class", "x axis")
							  .attr("transform", "translate(0," + height + ")")
							  .call(xAxis.tickSize(-height, 0, 0).tickFormat(""))
							  .call(removeZero);

						  svg.append("g")
							  .attr("class", "y axis")
							  .call(yAxis.tickSize(-width, 0, 0).tickFormat(""))
							  .call(removeZero);

						  // Compute the pixel colors; scaled by CSS.
						  function drawImage(canvas) {
							var context = canvas.node().getContext("2d"),
								image = context.createImageData(dx, dy);

							for (var y = 0, p = -1; y < dy; ++y) {
							  for (var x = 0; x < dx; ++x) {
								var c = d3.rgb(color(heatmap[y][x]));
								image.data[++p] = c.r;
								image.data[++p] = c.g;
								image.data[++p] = c.b;
								image.data[++p] = 255;
							  }
							}

							context.putImageData(image, 0, 0);
						  }

						  function removeZero(axis) {
							axis.selectAll("g").filter(function(d) { return !d; }).remove();
						  }
						});
					
					
			}

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


/*
*when the query binsize =100*100,we will use third party js to draw the heatmap
*/
function draw_matrix_bysvg(){
	
	var params={"qbin_start":qbin_start,"qbin_end":qbin_end};
	$("#matriximg").css("display","none");
	//area
	$("#area").css("display","none");
	$("#waitid").css("display","none");
	$.ajax({
		url:'/circosweb/ajax/drawMatrixByjs.action',
	    type:'post',
		dataType:'json',
		data:params,
		success:function(value){
			if(value.jsonStr!=null){
					
					
					var x=[],y=[];// = _.range(qbin_start, qbin_end, 1), y =_.range(qbin_start, qbin_end, 1);
					var xcount = 0 ;
					for(i=qbin_start;i<=qbin_end;i++){
						x[xcount++] = i ;
					}
					
					xcount = 0 ;
					for(i=qbin_start;i<=qbin_end;i++){
						y[xcount++] = i ;
					}
					
					var  z = eval ("(" + value.jsonStr + ")");
					
					var data = [{
						x: x,
						y: y,
						z: z,
						type: 'heatmap',
						colorscale: 'YIOrRd'
					}],
					layout = {
						title: 'ESC',
						showlegend: true,
						legend: {
							x: 0,
							y: 500
						},
						width: 685,
						height: 525,
						xaxis: {
							title : 'Chromsome21',
							nticks: 15,
							ticks: 'inside',
							showgrid: false,
							zeroline: false
						},
						yaxis:{
							title: 'log2 interaction matrix',
							nticks: 15,
							ticks: 'inside',
							showgrid:false,
							zeroline:false
						}
					};
					var divid = "heatmapdiv";
					//$('#' + divid).width(685);
					//$('#' + divid).height(525);

				Plotly.plot(divid, data, layout,{showLink: false,staticPlot: true});
			}

		},
		error:function(e){
			alert("error");
		}
		
	});

	
}




function ajax_getMatrixPic(){
	var q_chr = $("#chromid").val();
	var dataset = $("#dset").val();
	var params={"chrom":q_chr,"qbin_start":qbin_start,"qbin_end":qbin_end,"dataset":dataset};
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
