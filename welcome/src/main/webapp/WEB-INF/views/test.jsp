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
<input id="createGuest" type="submit" value="Create Guest"/>
<input id="sendNotification" type="submit" value="Send Notification"/>

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

                               $.ajax({

                                              type:"POST",
                                              url:"/guests/search",
                                              contentType:"application/json",
                                              dataType: "html",
                                              success:function (responseText) {
                                                $("body").html(responseText);
                                              }
                                });


                  });


         });

     </script>
</body>
</html>