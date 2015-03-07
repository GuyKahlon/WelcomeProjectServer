<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<script src="http://code.jquery.com/jquery-latest.min.js"
        type="text/javascript"></script>
<head>
	<title>Hosts Management</title>
</head>
<body>
<h3>Upload Hosts File</h3>

  <form method="POST" action="/hosts/overrideHosts" enctype="multipart/form-data">
        Select File to upload: <input type="file" name="file"/><br/><br/>
        <input type="submit" value="Upload">
    </form>

 <hr>
 <h3> Create Host </h3>
 <input id="createHost" type="submit" value="Create Host"/>
 <br/>
 <br/>
 <hr>
 <h3> Deactivate Host </h3>
 <input id="deactivateHost" type="submit" value="Deactivate Host"/>

 <script type="text/javascript">

          $(document).ready(function () {

              $("#createHost").click(function() {

                  $.ajax({
                                  type:"POST",
                                  url:"/hosts",
                                  contentType:"application/json",
                                  data:JSON.stringify({
                                      firstName:"Ram",
                                      lastName:"Oren",
                                      email:"ram.oren@gmail.com",
                                      phoneNumber:"100100",
                                  }),
                                  dataType: "html",
                                  success:function (responseText) {
                                      $("body").html(responseText);
                                  }
                        });
				});


               $("#deactivateHost").click(function() {

                    $.ajax({
                                                type:"POST",
                                                url:"/hosts/1?active=false",
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
