<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> --%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>

<html>
<head>
	<title>Home</title>
</head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/CSS.css">
	
	<body>
		<div class="container">
			<div class="header">
				<t:insertAttribute name="header"/>
			</div>
			<hr/>
			<div class="center">
				<t:insertAttribute name="content"/>
			</div>
			<div class="footer">
				<t:insertAttribute name="footer"/>
			</div>
		</div>
		<t:insertAttribute name="modal"/>
	</body>
	<script type="text/javascript" src="./script/formCheck.js"></script>
	<script type="text/javascript" src="./script/modal.js"></script>
	<script type="text/javascript" src="./script/script.js"></script>
</html>