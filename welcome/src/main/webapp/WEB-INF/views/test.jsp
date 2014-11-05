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
<input id="searchGuestIdo" type="submit" value="Search Guest Ido"/>
<br/>
<input id="searchGuestGuy" type="submit" value="Search Guest Guy"/>
<br/>
<input id="searchGuestLiron" type="submit" value="Search Guest Liron"/>
<br/>
<input id="createGuestIdo" type="submit" value="Create Guest Ido"/>
<br/>
<input id="createGuestGuy" type="submit" value="Create Guest Guy"/>
<br/>
<input id="createGuestLiron" type="submit" value="Create Guest Liron"/>
<br/>
<input id="createGuestKfir" type="submit" value="Create Guest Kfir"/>
<br/>
<input id="sendNotification" type="submit" value="Send Notification"/>
<br/>
<input id="searchByPhone" type="submit" value="Search By phone"/>
<br/>


<img id="img" src="/resources/0.jpg" />
<img id="img2" src="/resources/1.jpg" />
<img id="img3" src="/resources/2.jpg" />
<img id="imgLiron" src="/resources/liron.jpg" />
<img id="imgLiron2" src="/resources/liron2.jpg" />
<img id="imgLiron3" src="/resources/liron3.jpg" />
<img id="imgGuy" src="/resources/guy.jpg" />
<img id="imgGuy2" src="/resources/guy2.jpg" />
<img id="imgGuy3" src="/resources/guy3.jpg" />



 <br/>
      <script type="text/javascript">

         $(document).ready(function () {

             $("#createGuestIdo").click(function() {

                 $.ajax({

                                 type:"POST",
                                 url:"/guests",
                                 contentType:"application/json",
                                 data:JSON.stringify({
                                     firstName:"Ido",
                                     lastName:"Shaked",
                                     email:"Ido@gmail.com",
                                     phoneNumber:"1410445091911",
                                     picUrl:"1410445091911"
                                 }),
                                 dataType: "html",
                                 success:function (responseText) {
                                     $("body").html(responseText);
                                 }
                             });


             });

                  $("#createGuestGuy").click(function() {

                              $.ajax({

                                              type:"POST",
                                              url:"/guests",
                                              contentType:"application/json",
                                              data:JSON.stringify({
                                                  firstName:"Guy",
                                                  lastName:"Kahlon",
                                                  email:"guy@gmail.com",
                                                  phoneNumber:"0545791818",
                                                  picUrl:"1408897425463"
                                              }),
                                              dataType: "html",
                                              success:function (responseText) {
                                                  $("body").html(responseText);
                                              }
                                          });


                          });

                                  $("#createGuestKfir").click(function() {

                                                        $.ajax({

                                                                        type:"POST",
                                                                        url:"/guests",
                                                                        contentType:"application/json",
                                                                        data:JSON.stringify({
                                                                            firstName:"Kfir",
                                                                            lastName:"Tishbi",
                                                                            email:"kfir@gmail.com",
                                                                            phoneNumber:"0545791818",
                                                                            picUrl:"1415187436469"
                                                                        }),
                                                                        dataType: "html",
                                                                        success:function (responseText) {
                                                                            $("body").html(responseText);
                                                                        }
                                                                    });


                                                    });
                   $("#createGuestLiron").click(function() {

                                                                          $.ajax({

                                                                                          type:"POST",
                                                                                          url:"/guests",
                                                                                          contentType:"application/json",
                                                                                          data:JSON.stringify({
                                                                                              firstName:"Liron",
                                                                                              lastName:"Netzer",
                                                                                              email:"liron@gmail.com",
                                                                                              phoneNumber:"0545791818",
                                                                                              picUrl:"1234567891234"
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
                                              url:"/notifications?hostId=2&guestId=1",
                                              contentType:"application/json",
                                              dataType: "html",
                                              success:function (responseText) {
                                                  $("body").html(responseText);
                                              }
                                          });


                          });


                 $("#searchGuestIdo").click(function() {

                    var imgElem = document.getElementById('img');
                    var imgElem2 = document.getElementById('img2');
                    var imgElem3 = document.getElementById('img3');
                    var imgData = (getBase64Image(imgElem));
                    var imgData2 = (getBase64Image(imgElem2));
                    var imgData3 = (getBase64Image(imgElem3));
                     //alert(imgData);
                               $.ajax({

                                              type:"POST",
                                              url:"/guests/search",
                                              contentType:"application/json",
                                              data:JSON.stringify( {pictures: [imgData,imgData2,imgData3]}),
                                              dataType: "html",
                                              success:function (responseText) {
                                                $("body").html(responseText);
                                              }
                                });


                  });

                     $("#searchGuestLiron").click(function() {

                                      var imgElem = document.getElementById('imgLiron');
                                      var imgElem2 = document.getElementById('imgLiron2');
                                      var imgElem3 = document.getElementById('imgLiron3');
                                      var imgData = (getBase64Image(imgElem));
                                      var imgData2 = (getBase64Image(imgElem2));
                                      var imgData3 = (getBase64Image(imgElem3));
                                       //alert(imgData);
                                                 $.ajax({

                                                                type:"POST",
                                                                url:"/guests/search",
                                                                contentType:"application/json",
                                                                data:JSON.stringify( {pictures: [imgData,imgData2,imgData3]}),
                                                                dataType: "html",
                                                                success:function (responseText) {
                                                                  $("body").html(responseText);
                                                                }
                                                  });


                                    });


                      $("#searchGuestGuy").click(function() {

                                                                          var imgElem = document.getElementById('imgGuy');
                                                                          var imgElem2 = document.getElementById('imgGuy2');
                                                                          var imgElem3 = document.getElementById('imgGuy3');
                                                                          var imgData = (getBase64Image(imgElem));
                                                                          var imgData2 = (getBase64Image(imgElem2));
                                                                          var imgData3 = (getBase64Image(imgElem3));
                                                                           //alert(imgData);
                                                                                     $.ajax({

                                                                                                    type:"POST",
                                                                                                    url:"/guests/search",
                                                                                                    contentType:"application/json",
                                                                                                    data:JSON.stringify( {pictures: [imgData,imgData2,imgData3]}),
                                                                                                    dataType: "html",
                                                                                                    success:function (responseText) {
                                                                                                      $("body").html(responseText);
                                                                                                    }
                                                                                      });


                                                                        });



                      $("#searchByPhone").click(function() {

                                                                                     $.ajax({

                                                                                                    type:"GET",
                                                                                                    url:"/guests/searchByPhone?phoneNumber=0545791818",
                                                                                                    contentType:"application/json",
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