<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.*,com.zucc.demo.model.*"%>
<!DOCTYPE HTML>

<html>
	<head>
		<title>Recommendation</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<!--[if lte IE 8]><script src="assets/js/ie/html5shiv.js"></script><![endif]-->
		<link rel="stylesheet" href="assets/css/main.css" />
		<!--[if lte IE 8]><link rel="stylesheet" href="assets/css/ie8.css" /><![endif]-->
	</head>
	<body class="homepage">

	<%
    String id = (String) request.getParameter("id");

    %>

		<div id="page-wrapper">
			<!-- Header -->
				<div id="header-wrapper">
					<header id="header" class="container">

						<!-- Logo -->
							<div id="logo">
								<h1><a href="index.jsp">Book Recommendation</a></h1>
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

			<!-- Banner -->
				<div id="banner-wrapper">
					<div id="banner" class="box container">
						<div class="row">
							<div class="7u 12u(medium)">
								<h2 id="id">Hello user <%=id%>.</h2>
								<p style="font-size:42px">Books are the ever-burning lamps of accumulated wisdom.</p>
							</div>
							<div class="5u 12u(medium)">
								<ul>
									<li><a href="http://localhost:8080/BooksRs/personal?id=<%=id%>" class="button alt big icon fa-question-circle">Personal Info</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>

			<!-- Features -->
				<div id="features-wrapper">
					<div class="container">
						<div class="row">
    <%
	    List objlist=(List) request.getSession().getAttribute("list");
	    if(objlist!=null){
            for(int i=0;i<objlist.size();i++){
         	    BookVo book = (BookVo) objlist.get(i);
    %>
                        <div class="4u 12u(medium)" style="float:left">
								<!-- Box -->
									<section class="box feature" style="height:800px">
										<a href="#" class="image featured"><img style="height:450px" src="<%=book.getImage_URL_L()%>" alt="" /></a>
										<div class="inner">
											<header>
												<span style="font-size:20px;font-weight:bold"><%=book.getBook_title()%></span><br>
												<span><%=book.getBook_Author()%></span><br>
												<span><%=book.getPublisher()%> / <%=book.getYear_of_Publication()%></span><br>
												<span><a href="javascript:void(0)" title="<%=book.getIsbn()%>" onclick="value(title)">评分</a></span>
											</header>
										</div>
									</section>
						</div>
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
                    window.open(url+searchword);
                }

                function value(isbn){
                    var score = prompt("请输入您的评分","");
                    var url = "http://localhost:8080/BooksRs/individual?id="
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
                                    alert(response.content);
                                    window.location.href = url + <%=id%>;
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