<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.*,com.zucc.demo.model.*"%>
<%@ page import="net.sf.json.JSONArray"%>
<%@ page import="com.alibaba.fastjson.JSON"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE HTML>

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
        String sword = (String) request.getParameter("searchWord");
        String result = (String) request.getParameter("result");
        Map map = JSON.parseObject(result);
        List<RatingVo> list = new ArrayList<RatingVo>();
        JSONArray jsonArray = JSONArray.fromObject(map.get("searchList"));
        list = JSONArray.toList(jsonArray,RatingVo.class);
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
                                    <li><input type="text" id="searchWord"></input></li>
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

        List objlist = list;
	    if(objlist!=null){
            for(int i=0;i<objlist.size();i++){
         	    RatingVo r = (RatingVo) objlist.get(i);
         	    String score = new String((r.getScore()).getBytes("iso-8859-1"), "utf-8");

    %>
							<hr>
							<li class="item" style="list-style-type:none;height:200px">
							    <div class="2u 12u(medium)" style="float:left;height:200px">
							        <img src="<%=r.getImg()%>">
							    </div>
							    <div class="10u 12u(medium)" style="float:left;height:200px">
							        <h3><%=r.getTitle()%></h3>
							        <span><%=r.getAuthor()%>  /  <%=r.getPublisher()%>  /  <%=r.getYear()%></span><br>
							        <span>ISBN:<%=r.getIsbn()%>  /  Rating:<%=score%></span>
							<%
                                if((score).equals("暂无评分")){
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
                                var searchWord = document.getElementById("searchWord").value;
                                var uurl = 'http://localhost:8080/BooksRs/search?id=<%=id%>&searchWord=';
                                $.ajax({
                                    type:'GET',
                                    url:'http://localhost:8080/BooksRs/book',
                                    async:true,
                                    data:{
                                        'id':<%=id%>,
                                        'searchWord':searchWord
                                    },
                                    dataType:'json',
                                    success:function(result){
                                        var jsonData = JSON.stringify(result);
                                        window.open(uurl+searchWord+"&result="+jsonData);
                                    },
                                    error:function(){
                                        alert("失败...");
                                    }
                                })
                            }

                            function value(isbn){
                                var score = prompt("请输入您的评分","");
                                var uurl = "http://localhost:8080/BooksRs/search?id=<%=id%>&searchWord=";
                                if (score!=null && score!="") {
                                    $.ajax({
                                            type:'POST',
                                            url:'http://localhost:8080/BooksRs/user',
                                            async:true,
                                            data:{
                                                _method:'PUT',
                                                'id':<%=id%>,
                                                'isbn':isbn,
                                                'score':score
                                            },
                                            dataType:'json',
                                            success:function(result){
                                                var jsonData = JSON.stringify(result);
                                                alert("评分成功！");
                                            },
                                            error:function(){
                                                alert("失败...");
                                            }
                                    })
                                }
                            }
            </script>
	</body>
</html>