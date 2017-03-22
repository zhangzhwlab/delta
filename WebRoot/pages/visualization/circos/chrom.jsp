<html>
<head>
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/jquery-1.9.1.js" ></script>
<script type="text/javascript" src="/circosweb/js/jquery-ui.js" ></script>
<script type="text/javascript" src="/circosweb/js/jquery.form.js" ></script>

</head>
<body>
<div id="dialog_chrom" class="header_content">


 <div class="header_content"> <input type="checkbox" id="allchromid" onClick="selectAllChrom()" />All  &nbsp;&nbsp;<input type="button" value="Submit" onClick="checkSelectedChrom()"/></div>

<div class="header_content" id="chromdiv">


</div>





</div>
</body>
</html>

<script type="text/javascript" language="javascript">
	var sel_chrom_lst=[];
    var cur_chrom_lst;
	function configWholeChrom(){
		//alert("hello");
		var table=$("<table class=\"table1\" width=\"100%\">");
		var tr;
		var td;
		cur_chrom_lst = window.parent.parent_getChromLst();
		var exist_chrom = window.parent.parent_getRegionLst();
		for(i=0;i<cur_chrom_lst.length;i++){			
			var curObj = cur_chrom_lst[i];
			var idenflag = 0;
			 for(j=0;j<exist_chrom.length;j++){
			   		var exist_ob = exist_chrom[j];
					if(exist_ob.chr == curObj.chr){
						idenflag = 1;
						break;
					}
			   }
			 td=$("<td width=\"50\" height=\"10\"><input type=\"checkbox\"  value=\""+curObj.chr+"\"/>"+curObj.chr+"</td>");
			 if(idenflag == 1){
			  	td=$("<td width=\"50\" height=\"10\"><input type=\"checkbox\" checked=\"checked\"  value=\""+curObj.chr+"\"/>"+curObj.chr+"</td>");
			 }
			if(i%4 ==0 ){
			
			   tr=$("<tr></tr>");
			   tr.appendTo(table);
			   
			   
			   td.appendTo(tr);
			}else{
			  
			   td.appendTo(tr);
			}
		}

		$("#chromdiv").append(table);
	}
	
	//this used to check the selected chrom
	function checkSelectedChrom(){
		sel_chrom_lst=[];
		$("#chromdiv input[type='checkbox']").each(function() {
			if ($(this).is(":checked")) {
				var cur_chr = $(this).val(); // chromosome
				for(i=0;i<cur_chrom_lst.length;i++){
					var curObj = cur_chrom_lst[i];
					if(cur_chr == curObj.chr){
						sel_chrom_lst.push(curObj);
						break;
					}
				}
			}
   		 });
		window.parent.parent_drawWholeGenome(sel_chrom_lst);
		
	}
	
	
	function selectAllChrom(){
		
		if($("#allchromid").is(":checked")){
			$("#chromdiv input[type='checkbox']").each(function() {
				$(this).prop("checked",true);
			});
		
		}else{
			$("#chromdiv input[type='checkbox']").each(function() {
				$(this).prop("checked",false);
			});
		
		}
	}

</script>