<html>
<script src="http://code.jquery.com/jquery-latest.min.js"
        type="text/javascript"></script>

<head>
	<title>Test Guest</title>
</head>
<body>
<h1>
	Test Guest
</h1>
<input id="searchGuest" type="submit" value="Search Guest"/>
<br/>
<input id="createGuest" type="submit" value="Create Guest"/>
<br/>
<input id="sendNotification" type="submit" value="Send Notification"/>
<br/>

<img id="img" src="/resources/liron.png" />



 <br/>
      <script type="text/javascript">

         $(document).ready(function () {

             $("#createGuest").click(function() {

                 $.ajax({

                                 type:"POST",
                                 url:"/guests",
                                 contentType:"application/json",
                                 data:JSON.stringify({
                                     firstName:"Dana",
                                     lastName:"Harari",
                                     email:"dana@gmail.com",
                                     phoneNumber:"0545791818"
                                 }),
                                 dataType: "html",
                                 success:function (responseText) {
                                     $("body").html(responseText);
                                 }
                             });


             });


                 $("#sendNotification").click(function() {

                              $.ajax({

                                              type:"POST",
                                              url:"/notifications?hostId=7&guestId=9",
                                              contentType:"application/json",
                                              dataType: "html",
                                              success:function (responseText) {
                                                  $("body").html(responseText);
                                              }
                                          });


                          });


                 $("#searchGuest").click(function() {

                    var imgElem = document.getElementById('img');
                    var imgData = JSON.stringify(getBase64Image(imgElem));
                     alert(imgData);
                               $.ajax({

                                              type:"POST",
                                              url:"/guests/search",
                                              contentType:"application/json",
                                              data: imgData,
                                              dataType: "html",
                                              success:function (responseText) {
                                                $("body").html(responseText);
                                              }
                                });


                  });




                  function getBase64Image(imgElem) {

                  // imgElem must be on the same server otherwise a cross-origin error will be thrown "SECURITY_ERR: DOM Exception 18"
                      var canvas = document.createElement("canvas");
                      canvas.width = imgElem.clientWidth;
                      canvas.height = imgElem.clientHeight;
                      var ctx = canvas.getContext("2d");
                      ctx.drawImage(imgElem, 0, 0);
                      var dataURL = canvas.toDataURL("image/png");
                      return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
                  }


         });

     </script>
</body>
</html>