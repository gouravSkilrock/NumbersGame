<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	</head>
	<style type="text/css">
html {
	height: 100%
}

body {
	height: 100%;
	margin: 0;
	padding: 0
}

#map_canvas {
	height: 100%
}
</style>



	<body>
		<script type="text/javascript"
			src="http://maps.google.com/maps/api/js?sensor=false&key=AIzaSyDTUJ7uhRPKgQSSptO_kyoSrfpmxNSn278"></script>
		<script type="text/javascript">
	var infowindow = null;

	function initialize() {
		var mapData = parent.document.getElementById("mapData").innerHTML;
		var cLatLng = parent.document.getElementById("latLong").value;
		var centerMap = new google.maps.LatLng(cLatLng.split('_')[0], cLatLng.split('_')[1]);

		var myOptions = {
			zoom : 6,
			center : centerMap,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		}
		var map = new google.maps.Map(document.getElementById("map_canvas"),
				myOptions);

		addMarker(mapData, map);
		infowindow = new google.maps.InfoWindow( {
			content : "loading..."
		});
		function addMarker(data, map) {
			var str = null;
			var lan = 0;
			var dataArr = data.split(",");
			for ( var i = 0; i < dataArr.length - 1; i++) {
				if (dataArr[i] != "") {
					var lanlon = dataArr[i].split(":")[2];
					if (lanlon != 0) {
						if (i != 0 && dataArr[i-1]!="") {
							var lanlon1 = dataArr[i-1].split(":")[2];
							lan = lanlon1.split('Nxt')[0];
						}
						if (lanlon.split('Nxt')[0] == lan)
							str = str + ",   " + dataArr[i].split(":")[1];
						else
							str = "Retailer Name: " + dataArr[i].split(":")[1];
						var siteLatLng = new google.maps.LatLng(lanlon
								.split('Nxt')[0], lanlon.split('Nxt')[1]);
    	    var pinColor = dataArr[i].split(":")[3];
		    var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
        	new google.maps.Size(21, 34),
      	    new google.maps.Point(0,0),
            new google.maps.Point(10, 34));
    		var pinShadow = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_shadow",
        	new google.maps.Size(40, 37),
        	new google.maps.Point(0, 0),
        	new google.maps.Point(12, 35));
						var marker = new google.maps.Marker( {
							position : siteLatLng,
							map : map,
							title : dataArr[i].split(":")[0].trim(),
							html : str,
               			    icon: pinImage,
			                shadow: pinShadow
						});

						google.maps.event.addListener(marker, "click",
								function() {
									infowindow.setContent(this.html);
									infowindow.open(map, this);
								});
					}
				}
			}
		}
	}
</script>
	</body>
	<body onload="initialize()">
		<div id="map_canvas" style="width: 100%; height: 100%"></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: GMap.jsp,v $ $Revision: 1.3 $
</code>