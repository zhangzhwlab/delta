// JavaScript Document
var tID=0;
var subtID=0;
function showTabs(ID){  
	//alert("showtabes");
		 if(ID!=tID){  
		 //alert(ID+","+tID);
        eval("document.getElementById('menu"+[tID]+"').className='off';");  
        eval("document.getElementById('menu"+[ID]+"').className='on';");
        tID=ID;  
   	}  
}  
	
function showSubTabs(ID){  
   if(ID!=subtID){  
        eval("document.getElementById('sub"+[subtID]+"').className='suboff';");  
        eval("document.getElementById('sub"+[ID]+"').className='subon';");
        subtID=ID;  
   	}  
}  
