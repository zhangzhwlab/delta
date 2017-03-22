<%@ taglib prefix="s" uri="/struts-tags"%>



<div id="waitid"> waiting for result</div>

<div id="showid" style="display:none;">
<table width="699" cellpadding="5" cellspacing="0" class="table1">
							<tbody>
								<tr>
								
									<th width="51">Segment1_chrom</th>
									<th width="75">Segment1_start</th>
								  	<th width="496">Segment1_end</th>
								  	<th>Segment2_chrom</th>
									<th>Segment2_start</th>
									<th>Segment2_end</th>
								</tr>
								<s:iterator value="ibList" id="ib">
								<tr>
									
									<td style="text-align:center;"><s:property value="#ib.anchorchr"/></td>
									<td style="text-align:center;"><s:property value="#ib.anchorstart"/></td>
									<td style="text-align:center;"><s:property value="#ib.anchorend"/></td>
									<td style="text-align:center;"><s:property value="#ib.targetchr"/></td>
									<td style="text-align:center;"><s:property value="#ib.targetstart"/></td>
									<td style="text-align:center;"><s:property value="#ib.targetend"/></td>
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
	parent.document.getElementById("peakstatusid").innerHTML= "Start";

}else if(showflag == 2){
	parent.document.getElementById("peakstatusid").innerHTML= "Run";
}else if(showflag ==3){
	parent.document.getElementById("peakstatusid").innerHTML= "Finish";	
}

</script>
