var i18nObj = parent.frames[0].i18Obj;


function isDateValid()
{
  //alert("hello");
  var saleendDate = document.getElementById("saleEndDate");
  var saleDate= saleendDate.value;
   //alert(saleDate);
   
    var petendDate = document.getElementById("pwtEndDate");
    var pwtDate= petendDate.value;
    //alert(pwtDate);
    var presentDate=document.extendDate.presentDate.value
    //alert(presentDate)
    
    var saleDateDb=document.extendDate.saleDateDB.value
    var pwtDateDb=document.extendDate.pwtDateDB.value
    //alert("sale date from db "+ saleDateDb);
    //alert("pwt date from db "+ pwtDateDb);
    
    
    if(saleDate=="" || pwtDate=="")
    {
      document.getElementById("date").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.pls.donot.leave.date.blank')+"</font>";
      //alert("please enter Dates");
      return false;     
    
    }
    
    if(saleDate==saleDateDb && pwtDate==pwtDateDb)
    {
     document.getElementById("date").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.pls.change.dates.or.click.cancel')+"</font>";
     //alert("please change the dates or click cancel");
     return false;
    
    }
    
   if(saleDate==saleDateDb  && pwtDate!=pwtDateDb)
       {
         if(pwtDate > presentDate)
         {
         // alert("only pwt to be extended  set status to slae_hold");
          return true;
         }
         else
           {
            document.getElementById("date").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.pwt.date.grater.then.current.date')+"</font>";
           //alert("Enter valid pwt greater then current");
           return false;
           }       
       }
        
   if(pwtDate==pwtDateDb && saleDate!=saleDateDb)     
   
   {
     
     if(saleDate > presentDate && saleDate < pwtDate && pwtDate > presentDate)
     {
        //alert("only sale date to be extend set status to open");
        return true;
     }
       else
           {
            document.getElementById("date").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.sale.end.date.more.then.current.date.and.less.then.pwt.date')+"</font>"; 
            //alert("Enter valid Sale date greater then current or less then pwt");
           return false;
           } 
   } 
    
    if(pwtDate!=pwtDateDb && saleDate!=saleDateDb)
    {
      if(saleDate>presentDate && pwtDate>presentDate)
      {
       
        if(saleDate>pwtDate)
        {
         document.getElementById("date").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.sale.date.cannot.greater.then.pwt.date')+"</font>";
         //alert("sale can not be greater then pwt  here pwt< sed");
         return false; 
        }
        else
        {
         
         //alert("valid sale  and pwt date and sed < pwt  set status to open ");
         return true; 
        }
             
      }
      else
       {
        document.getElementById("date").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.pls.check.dates.entered.greater.then.current.date')+"</font>";
        //alert("please check for the valid dates  should be greater then current");
        return false;
       }
    
    }
   return false;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/gameMgmt/backOffice/js/closeSaleGameDetails.js,v $'] = '$Id: closeSaleGameDetails.js,v 1.3 2016/10/31 09:46:47 neeraj Exp $';