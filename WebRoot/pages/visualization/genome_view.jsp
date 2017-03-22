<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="/circosweb/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/circosweb/js/menu.js"></script>
<title>Genome view</title>
</head>

<body>
<div id="container">
	<div id="logo">		
    <div align="left">
      <table width="100%" cellpadding="0" cellspacing="0">
        <tbody>
          <tr>
             <td width="70%" height="95" style="font-size:30px; font-weight:bold;">Chromatin Interaction Database</td>
			
            
          </tr>
          </tbody>
          </table>
    </div>
  </div>
	<div id="menu">
		
	      <div align="left">
		   <ul>
			  	<li><a id="menu0" class="suboff" href="../../index.html">Home</a></li>
				
		        <li ><a id="menu1" class="suboff" href="../browse/browse.html">Browse</a></li>
		        <li ><a id="menu2" class="suboff" href="dataconfig/data_list.html">Search</a></li>
				<li><a id="menu0" class="subon" href="genome_view.html">Visualization</a></li>
		        <li ><a id="menu3" class="suboff" href="workflowconfig/app/app_list.html">Download</a></li>
		        <li ><a id="menu4" class="suboff" href="taskconfig/task_list.html">Help</a></li>
	        </ul>
      </div>
	</div>
	<div id="content">
	  <div id="left-column1" style="margin-left:5px; width:95%;">
			<div class="header_border">
				<div class="header_content">
					 <div class="header">Currently Available Reference Genome</div>
                     <div>
					 	<table width="370">
                        	<tbody>
                            	<tr>
                                    <td width="87">Group</td>
                                    <td width="271"><select style="width:200px;">
                                    <option value="Animalia">Animalia</option>
                                    <option value="Plantae">Plantae</option>
                                    <option value="Rest">Rest</option>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>Genome</td>
                                    <td><select style="width:200px;">
                                    	<option value="hg19">Human(hg19)</option>
                                        <option value="rheMac3">Rhesus macaque rheMac3</option>
                                    </select></td>
                                </tr>
                                <tr>
                                	<td>Position</td>
                                    <td><input type="text" style="width:200px;"/></td>
                                </tr>
                                
                                <tr><td colspan="2"><input type="button" value="Submit" onclick="initGenome()" />&nbsp;<input type="reset" value="Reset"/></td></tr>
                            </tbody>
                        
                        </table>
                  </div>
					
			  </div>
		  </div>		
		</div>
        <div style="clear: both;"></div>
	</div>
	<div id="footer">		 
	</div>
	<div style="font-size:12px;" align="center">
			&copy;BIG 2012, Beijing Institute of Genomics, Chinese Academy of Sciences
    </div>
	<div style="font-size:12px;padding-bottom:10px;" align="center">
		No.7 Beitucheng West Road, Chaoyang District, Beijing 100029, PR China 	
    </div>

</div>


</body>
</html>

