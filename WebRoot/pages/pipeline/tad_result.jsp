<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="waitid"> waiting for result</div>

<div id="showid" style="display:none;">
<table width="391" cellpadding="5" cellspacing="0" class="table1">
	<tbody>
		<tr>
		
			<th width="106">Chrom</th>
			<th width="99">Start</th>
			<th width="122">End</th>
		</tr>
		<s:iterator value="gffList" id="gff3Format">
		<tr>
	
			<td style="text-align:center;"><s:property value="#gff3Format.seq"/></td>
			<td style="text-align:center;"><s:property value="#gff3Format.start"/></td>
			<td style="text-align:center;"><s:property value="#gff3Format.end"/></td>
		</tr>
		
		</s:iterator>
	</tbody>
</table>		
</div>	
		

<script type="text/javascript">
var showflag="<s:property value='resflag'/>"; //finish flag
if(showflag == 1 ||showflag == 2){

	document.getElementById("waitid").style.display="block";
	document.getElementById("showid").style.display="none";
}
else if(showflag ==3){
	document.getElementById("showid").style.display="block";
	document.getElementById("waitid").style.display="none";

}

if(showflag ==1){
	parent.document.getElementById("tadstatusid").innerHTML= "Start";

}else if(showflag == 2){
	parent.document.getElementById("tadstatusid").innerHTML= "Run";
}else if(showflag ==3){
	parent.document.getElementById("tadstatusid").innerHTML= "Finish";	
}

</script>		