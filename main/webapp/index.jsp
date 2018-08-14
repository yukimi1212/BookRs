<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html >
<head>
  <meta charset="UTF-8">
  <title>Login</title>

  <link rel='stylesheet prefetch' href='http://fonts.googleapis.com/css?family=Open+Sans'>
  <link rel="stylesheet" href="css/style.css">

</head>

<body>
<p class="tip">Book Recommendation</p>

<div ng-app="myApp" ng-controller="myCtrl">
<div class="cont">
  <div class="form sign-in">
    <h2>Welcome</h2>

    <label>
      <span>ID</span>
      <input type="text" id="userid" />
    </label>
    <label>
      <span>Password</span>
      <input type="password" id="userpwd"/>
    </label>
    <button type="button" class="submit" onclick="check()">Sign In</button>

  </div>
  <div class="sub-cont">
    <div class="img">
      <div class="img__text m--up">
        <h2>New here?</h2>
        <p>Sign up and discover great amount of new books!</p>
      </div>
      <div class="img__text m--in">
        <h2>One of us?</h2>
        <p>If you already has an account, just sign in. We've missed you!</p>
      </div>
      <div class="img__btn">
        <span class="m--up">Sign Up</span>
        <span class="m--in">Sign In</span>
      </div>
    </div>
    <div class="form sign-up">
      <h2></h2>
      <label>
        <span>Location</span>
        <input type="text" id="location"/>
      </label>
      <label>
        <span>Age</span>
        <input type="text" id="age"/>
      </label>
      <label>
        <span>Password</span>
        <input type="password" id="pwd"/>
      </label>
      <button type="button" class="submit" onclick="add()">Sign Up</button>
    </div>
  </div>
</div>
</div>

<a href="https://dribbble.com/shots/3306190-Login-Registration-form" target="_blank" class="icon-link">
  <img src="http://icons.iconarchive.com/icons/uiconstock/socialmedia/256/Dribbble-icon.png">
</a>
<a href="https://twitter.com/NikolayTalanov" target="_blank" class="icon-link icon-link--twitter">
  <img src="https://cdn1.iconfinder.com/data/icons/logotypes/32/twitter-128.png">
</a>
  
    <script src="js/index.js"></script>
    <script type="text/javascript">
        function check(){
            var id = document.getElementById("userid").value;
            var pwd = document.getElementById("userpwd").value;
            var url = "http://localhost:8080/BooksRs/individual?id="
            var xmlhttp = new XMLHttpRequest();
                    xmlhttp.open("POST", "check", true);
                    xmlhttp.setRequestHeader("Content-type",
                            "application/x-www-form-urlencoded");
                    xmlhttp.send("id=" + id + "&pwd=" + pwd);
                    xmlhttp.onreadystatechange = function() {
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                            var response = xmlhttp.responseText;
                            response = JSON.parse(response);
                            if (response.flag) {
                            	window.open(url+id);
                            }
                            else{
                            	alert(response.content);
                            }
                        }
                    };
        }

        function add(){
                    var location = document.getElementById("location").value;
                    var age = document.getElementById("age").value;
                    var pwd = document.getElementById("pwd").value;
                    var url = "http://localhost:8080/BooksRs/individual?id="
                    var xmlhttp = new XMLHttpRequest();
                            xmlhttp.open("POST", "add", true);
                            xmlhttp.setRequestHeader("Content-type",
                                    "application/x-www-form-urlencoded");
                            xmlhttp.send("pwd=" + pwd + "&location=" + location + "&age=" + age );
                            xmlhttp.onreadystatechange = function() {
                                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                                    var response = xmlhttp.responseText;
                                    response = JSON.parse(response);
                                    if (response.flag) {
                                    	window.open(url+response.content);
                                    }
                                    else{
                                    	alert(response.content);
                                    }
                                }
                            };
                }
    </script>
</body>
</html>
