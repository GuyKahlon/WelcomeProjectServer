<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<script src="http://code.jquery.com/jquery-latest.min.js"
        type="text/javascript"></script>
<head>
	<title>Hosts Management</title>
</head>
<body>
<h3>Override DB hosts with hosts in Excel file</h3>
<p>Expected Excel file format:
<ul>
<li>First row: header</li>
<li>First column: first name</li>
<li>Second column: last name</li>
<li>Third column: email</li>
<li>Forth column: phone-number</li>
<li>Fifth column: isAcceleratorMember (optional column, to indicate that the host is a member of the accelerator set <b>true</b>, otherwise, leave empty)</li>
</ul></p><br/>
<p>The result of this action would be, that all hosts the exist in the DB, but not in the file, will be Deactivated.<br/>
Hosts that exist both in the DB and in the file will be updated according to file.<br/>
Hosts that exist in the file, but not in the DB, will be created.</p>

  <form method="POST" action="/hosts/overrideHosts" enctype="multipart/form-data">
        Select File to upload: <input type="file" name="file"/><br/><br/>
        <input type="submit" value="Upload Excel file">
    </form>

 <hr>
 <h3> Create Host </h3>
 <input id="createHost" type="submit" value="Create Host"/>
 <br/>
 <br/>
 <hr>
 <h3> Deactivate Host </h3>
 <input id="deactivateHost" type="submit" value="Deactivate Host"/>
 <br/>
 <br/>
 <hr>
 <h3> Capitalize Host names </h3>
 <input id="capitalizeHosts" type="submit" value="Capitalize Host names"/>

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


                 $("#capitalizeHosts").click(function() {

                                   $.ajax({
                                                               type:"POST",
                                                               url:"/hosts/capitalizeHosts",
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
