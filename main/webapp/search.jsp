<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.*,com.zucc.demo.model.*"%>
<!DOCTYPE HTML>
<!--
	Verti by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>Search</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<!--[if lte IE 8]><script src="assets/js/ie/html5shiv.js"></script><![endif]-->
		<link rel="stylesheet" href="assets/css/main.css" />
		<!--[if lte IE 8]><link rel="stylesheet" href="assets/css/ie8.css" /><![endif]-->
	</head>
	<body class="no-sidebar">

<%
        String id = (String) request.getParameter("id");
        String word = (String) request.getParameter("searchword");
%>
		<div id="page-wrapper">

			<!-- Header -->
				<div id="header-wrapper">
					<header id="header" class="container">

						<!-- Logo -->
							<div id="logo">
								<h1><a href="http://localhost:8080/BooksRs/personal?id=<%=id%>">Book Search</a></h1>
							</div>

						<!-- Nav -->
                            <nav id="nav">
								<ul>
                                    <li><input type="text" id="searchword"></input></li>
									<li><a href="javascript:void(0)" onclick="dosearch()">Search</a></li>
								</ul>
							</nav>

					</header>
				</div>

			<!-- Main -->
				<div id="main-wrapper">
					<div class="container">
						<div id="content">

							<!-- Content -->
<%
	    List objlist=(List) request.getSession().getAttribute("slist");
	    if(objlist!=null){
            for(int i=0;i<objlist.size();i++){
         	    RatingVo r = (RatingVo) objlist.get(i);
    %>
							<hr>
							<li class="item" style="list-style-type:none;height:200px">
							    <div class="2u 12u(medium)" style="float:left;height:200px">
							        <img src="<%=r.getImg()%>">
							    </div>
							    <div class="10u 12u(medium)" style="float:left;height:200px">
							        <h3><%=r.getTitle()%></h3>
							        <span><%=r.getAuthor()%>  /  <%=r.getPublisher()%>  /  <%=r.getYear()%></span><br>
							        <span>ISBN:<%=r.getIsbn()%>  /  Rating:<%=r.getScore()%></span>
							<%
                                if((r.getScore()).equals("暂无评分")){
							%>
							        <span><a href="javascript:void(0)" title="<%=r.getIsbn()%>" onclick="value(title)">评分</a></span>
                            <%
                                }
                            %>
							        </div>

                            </li>
    <%
        			}
              }
    %>

						</div>
					</div>
				</div>

			</div>

		<!-- Scripts -->

			<script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
			<script src="assets/js/jquery.dropotron.min.js"></script>
			<script src="assets/js/skel.min.js"></script>
			<script src="assets/js/util.js"></script>
			<!--[if lte IE 8]><script src="assets/js/ie/respond.min.js"></script><![endif]-->
			<script src="assets/js/main.js"></script>

            <script type="text/javascript">
                            function dosearch(){
                                var searchword = document.getElementById("searchword").value;
                                var url = 'http://localhost:8080/BooksRs/search?id=<%=id%>&searchword=';
                                window.location.href = url+searchword;
                            }

                            function value(isbn){
                                var score = prompt("请输入您的评分","");
                                var url = "http://localhost:8080/BooksRs/search?id=<%=id%>&searchword=<%=word%>"
                                if (score!=null && score!="") {
                                    var xmlhttp = new XMLHttpRequest();
                                    xmlhttp.open("POST", "value", true);
                                    xmlhttp.setRequestHeader("Content-type",
                                                "application/x-www-form-urlencoded");
                                    xmlhttp.send("id=" + <%=id%> + "&isbn=" + isbn + "&score=" + score);
                                    xmlhttp.onreadystatechange = function() {
                                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                                            var response = xmlhttp.responseText;
                                            response = JSON.parse(response);
                                            if (response.flag) {
                                                window.location.href = url;
                                            }
                                            else{
                                                alert(response.content);
                                            }
                                        }
                                    };

                                }

                            }
            </script>
	</body>
</html>