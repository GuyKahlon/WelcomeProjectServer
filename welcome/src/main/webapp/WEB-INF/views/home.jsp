<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello Citi TLV!
</h1>

<P>  The time on the server is ${serverTime}. </P>
<P>  Found ${NumOfHosts} hosts (out of which ${NumOfActiveHosts} are active). </P>
<P>  Found ${NumOfGuests} guests. </P>
</body>
</html>
