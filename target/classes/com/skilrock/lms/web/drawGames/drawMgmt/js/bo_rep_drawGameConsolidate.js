var i18nObj = parent.frames[0].i18Obj;

function validate(){

		clearDiv();
		var gameNo =$("#gameNo").val();
		var frmDate =new Date($("#start_date").val());
		var toDate =new Date($("#end_Date").val());
		if(gameNo==-1){
			$("#gameNo_e").html(i18nObj.prop('error.select.game.name'));
			return false;
			
		}
			if(!isValidInt(gameNo)){
				$("#gameNo_e").html(i18nObj.prop('error.inv.game.name'));
				return false;
			}
			
		
		if(toDate<frmDate){
			$("#date_e").html(i18nObj.prop('error.enddate.gt.strtdate'));
			return false;
		}
			
		return true;
		
	
}

function isValidInt(id){
	
	if ( (id+"").match(/^\d+$/) ) {
			  return true;
		}
	
	return false;
	
}
function 	clearDiv(){
	$("#date_e").html(" ");
	$("#gameNo_e").html(" ");
	
}
function isValidInt(id){
	if ( (id+"").match(/^\d+$/) ) {
	  return true;
	}
	return false;
}
function clearDiv(){
	$("#date_e").html(" ");
	$("#gameNo_e").html(" ");
	
}
var colspan=0;
function check(chkBoxId){
	if($('input[name='+chkBoxId+']').is(':checked')){
	    colspan=$(".column").attr("colspan");
	    colspan=parseInt(colspan);
		if($('input[name='+chkBoxId+']').val()=='No Of Tickets'){
			     colspan=colspan+1;
			     $(".column").attr("colspan",colspan);
			 	 $(".noOfTkts").show();
		}
		if($('input[name='+chkBoxId+']').val()=='Sale'){
			     colspan=colspan+1;
			     $(".column").attr("colspan",colspan);
			     $(".sale").show();
		}
		if($('input[name='+chkBoxId+']').val()=='Winning Amount'){
			     colspan=colspan+1;
			     $(".column").attr("colspan",colspan);
			     $(".winAmt").show(); 
		}
		if($('input[name='+chkBoxId+']').val()=='Claimed Amount'){
			     colspan=colspan+1;
			     $(".column").attr("colspan",colspan);
			     $(".claimedAmt").show();
		}
	}else{
	    colspan=$(".column").attr("colspan");
	    colspan=parseInt(colspan);
		if($('input[name='+chkBoxId+']').val()=='No Of Tickets'){
			     colspan=colspan-1;
			     $(".column").attr("colspan",colspan);
				 $(".noOfTkts").hide();
	    }
		if($('input[name='+chkBoxId+']').val()=='Sale'){
	    	     colspan=colspan-1;
	    	     $(".column").attr("colspan",colspan);
	    	     $(".sale").hide(); 
	    }
		if($('input[name='+chkBoxId+']').val()=='Winning Amount'){
	    	     colspan=colspan-1;
	    	     $(".column").attr("colspan",colspan);
	    	     $(".winAmt").hide(); 
		}
		if($('input[name='+chkBoxId+']').val()=='Claimed Amount'){
		         colspan=colspan-1;
		         $(".column").attr("colspan",colspan);
		         $(".claimedAmt").hide();
	    }
	}
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawGameConsolidate.js,v $'] = '$Id: bo_rep_drawGameConsolidate.js,v 1.3 2016/10/31 09:46:59 neeraj Exp $';