<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.*,com.zucc.demo.model.*"%>
<%@ page import="net.sf.json.JSONArray"%>

<!DOCTYPE HTML>
	<head>
		<title>Information</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<!--[if lte IE 8]><script src="assets/js/ie/html5shiv.js"></script><![endif]-->
		<link rel="stylesheet" href="assets/css/main.css" />
		<!--[if lte IE 8]><link rel="stylesheet" href="assets/css/ie8.css" /><![endif]-->
	</head>
	<body class="no-sidebar">

	<%
        String id = (String) request.getParameter("id");
        String result = (String) request.getParameter("result");

        List<RatingVo> list = new ArrayList<RatingVo>();
        JSONArray jsonArray = JSONArray.fromObject(result);
        list = JSONArray.toList(jsonArray,RatingVo.class);
    %>

		<div id="page-wrapper">
			<!-- Header -->
				<div id="header-wrapper">
					<header id="header" class="container">

						<!-- Logo -->
							<div id="logo">
								<h1><a href="http://localhost:8080/BooksRs/individual?id=<%=id%>">Personal Information</a></h1>
							</div>
					</header>
				</div>

			<!-- Main -->
				<div id="main-wrapper">
					<div class="container">
						<div id="content">

							<!-- Content -->

                                    <h2>已评书籍</h2><br>


    <%
	    List objlist=list;
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

            <script>
			window.alert = function(str)
            {
                var shield = document.createElement("DIV");
                shield.id = "shield";
                shield.style.position = "absolute";
                shield.style.left = "50%";
                shield.style.top = "50%";
                shield.style.width = "280px";
                shield.style.height = "150px";
                shield.style.marginLeft = "-140px";
                shield.style.marginTop = "-110px";
                shield.style.zIndex = "25";
                var alertFram = document.createElement("DIV");
                alertFram.id="alertFram";
                alertFram.style.position = "absolute";
                alertFram.style.width = "350px";
                alertFram.style.height = "150px";
                alertFram.style.left = "50%";
                alertFram.style.top = "50%";
                alertFram.style.marginLeft = "-140px";
                alertFram.style.marginTop = "-110px";
                alertFram.style.textAlign = "center";
                alertFram.style.lineHeight = "150px";
                alertFram.style.zIndex = "300";
                strHtml = "<ul style=\"list-style:none;margin:0px;padding:0px;width:100%\">\n";
                strHtml += " <li style=\"background:#CCCCCC;text-align:left;padding-left:20px;font-size:14px;font-weight:bold;height:25px;line-height:25px;border:1px solid #F9CADE;color:white\">[个人信息]</li>\n";
                strHtml += " <li style=\"background:#EEEEEE;text-align:left;font-size:12px;height:150px;line-height:35px;border-left:1px solid #F9CADE;border-right:1px solid #F9CADE;color:#565656\">"+str+"</li>\n";
                strHtml += " <li style=\"background:#CCCCCC;text-align:center;font-weight:bold;height:40px;line-height:25px; border:1px solid #F9CADE;\"><input type=\"button\" value=\"确 定\" onclick=\"doOk()\" style=\"width:80px;height:30px;background:#969696;color:white;border:1px solid white;font-size:14px;line-height:5px;outline:none;margin-top: 4px\"/></li>\n";
                strHtml += "</ul>\n";
                alertFram.innerHTML = strHtml;
                document.body.appendChild(alertFram);
                document.body.appendChild(shield);
                this.doOk = function(){
                    alertFram.style.display = "none";
                    shield.style.display = "none";
                }
                alertFram.focus();
                document.body.onselectstart = function(){return false;};
            }
            </script>

	</body>
</html>