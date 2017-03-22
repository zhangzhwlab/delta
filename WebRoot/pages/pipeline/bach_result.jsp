<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="waitid"> waiting for result</div>

<div id="showid" style="display:none;">
<table width="880" cellpadding="5" cellspacing="0" class="table1">
	<tbody>
		<tr>
		
			<th width="82">Chrom</th>
			<th width="126">Start</th>
			<th width="124">End</th>
			<th width="206">x,y,z</th>
		</tr>
		<s:iterator value="xyzList" id="bach">
		<tr>
	
			<td style="text-align:center;"><s:property value="#bach.chr"/></td>
			<td style="text-align:center;"><s:property value="#bach.start"/></td>
			<td style="text-align:center;"><s:property value="#bach.end"/></td>
			<td style="text-align:center;"><s:property value="#bach.x"/>,<s:property value="#bach.y"/>,<s:property value="#bach.z"/></td>
		</tr>
		
		</s:iterator>
	</tbody>
</table>		
</div>	
		

<script type="text/javascript">
var showflag="<s:property value='resflag'/>";
if(showflag == 1 ||showflag == 2){
	document.getElementById("waitid").style.display="block";
	document.getElementById("showid").style.display="none";
}
else if(showflag ==3){
	document.getElementById("showid").style.display="block";
	document.getElementById("waitid").style.display="none";

}
if(showflag ==1){
	parent.document.getElementById("modelstatusid").innerHTML= "Start";

}else if(showflag == 2){
	parent.document.getElementById("modelstatusid").innerHTML= "Run";
}else if(showflag ==3){
	parent.document.getElementById("modelstatusid").innerHTML= "Finish";	
}

</script>		