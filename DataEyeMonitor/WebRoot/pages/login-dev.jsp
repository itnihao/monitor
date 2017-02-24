<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<title>运维监控平台-登录</title>
<script src="http://code.jquery.com/jquery-1.12.0.min.js"></script>
<style>
*{
    margin: 0;
    padding: 0;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
}
img{
  border: 0;
}
html,body {
	height: 100%;
}
.login {
	background-color: #2b3541;
}
.login .logo {
    margin: 60px auto 0;
    padding: 15px;
    text-align: center;
}
.login .content {
    background-color: #eceef1;
    width: 400px;
    margin: 40px auto 10px;
    padding: 10px 30px 30px;
    overflow: hidden;
    position: relative;
}
.login .copyright {
    text-align: center;
    margin: 0 auto 30px 0;
    padding: 10px;
    color: #7a8ca5;
    font-size: 13px;
    font-family: 微软雅黑;
}
.login .content .form-title {
    font-weight: 300;
    margin-bottom: 25px;
}
.login .content h3 {
    color: #222;
    text-align: center;
    font-size: 28px;
    font-weight: 400!important;
}
.login .content .form-actions .btn {
	outline: none;
	border: none;
	color: #fff;
	font-family: 微软雅黑;
	background-color: #57a3f1;
	font-size: 16px;
	width: 100%;
    margin-top: 1px;
    font-weight: 600;
    padding: 10px 20px!important;
}
.login .content .form-actions .btn:hover {
	background-color: #4391e1;
}
.login-error{display:none;width:330px;margin:0 auto;color:red;}
.box {
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#6699FF', endColorstr='#6699FF'); /*  IE */
/* 	background-image:linear-gradient(bottom, #6699FF 0%, #6699FF 100%); */
/* 	background-image:-o-linear-gradient(bottom, #6699FF 0%, #6699FF 100%); */
/* 	background-image:-moz-linear-gradient(bottom, #6699FF 0%, #6699FF 100%); */
/* 	background-image:-webkit-linear-gradient(bottom, #6699FF 0%, #6699FF 100%); */
/* 	background-image:-ms-linear-gradient(bottom, #6699FF 0%, #6699FF 100%); */
	background-image:url("../images/login2015.jpg");
	
	margin: 0 auto;
	position: relative;
	width: 100%;
	height: 100%;
}
.login-box {
	width: 100%;
	max-width:500px;
	height: 400px;
	position: absolute;
	top: 50%;

	margin-top: -200px;
	/*设置负值，为要定位子盒子的一半高度*/
	
}
@media screen and (min-width:500px){
	.login-box {
		left: 50%;
		/*设置负值，为要定位子盒子的一半宽度*/
		margin-left: -250px;
	}
}	

.form {
	width: 100%;
	max-width:500px;
	height: 275px;
	margin: 25px auto 0px auto;
	padding-top: 25px;
}	
.login-content {
	height: 300px;
	width: 100%;
	max-width:500px;
	background-color: rgba(255, 250, 2550, .6);
	float: left;
}		
.input-group-addon {
	background-color: #dddddd;
    display: block;
    width: 40px;
    height: 40px;
    line-height: 40px;
    color: #fff;
    text-align: center;
    position: absolute;
    left: 1px;
    top: 1px;
    outline: 0;
    z-index: 1;
}	
	
.input-group {
	margin: 0px 0px 30px 0px !important;
}
.form-control,
.input-group {
	height: 40px;
	position: relative;
}
.form-control {
    height: 42px !important;
    display: block;
    width: 100%;
    height: 30px;
    padding: 6px 12px 6px 46px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #555;
    background-color: #fff;
    background-image: none;
    border: 1px solid #ddd;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
    -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
}

.form-group {
	margin-bottom: 0px !important;
}
.login-title {
	padding: 20px 10px;
	background-color: rgba(0, 0, 0, .6);
}
.login-title h1 {
	margin-top: 10px !important;
}
.login-title small {
	color: #fff;
}

.link p {
	line-height: 20px;
	margin-top: 30px;
}
.btn-sm {
	padding: 8px 24px !important;
	font-size: 16px !important;
}
.input-group-addon.user {
background-image: url("data:image/jpg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAABkAAD/4QMpaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjAtYzA2MCA2MS4xMzQ3NzcsIDIwMTAvMDIvMTItMTc6MzI6MDAgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzUgV2luZG93cyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpEMzdCREJCN0M3MjIxMUU1OEI0MTlCODRFN0ZDQ0U0MyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpEMzdCREJCOEM3MjIxMUU1OEI0MTlCODRFN0ZDQ0U0MyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkQzN0JEQkI1QzcyMjExRTU4QjQxOUI4NEU3RkNDRTQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkQzN0JEQkI2QzcyMjExRTU4QjQxOUI4NEU3RkNDRTQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+/+4AJkFkb2JlAGTAAAAAAQMAFQQDBgoNAAAFTgAABd0AAAaaAAAHh//bAIQAAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQICAgICAgICAgICAwMDAwMDAwMDAwEBAQEBAQECAQECAgIBAgIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMD/8IAEQgAKQApAwERAAIRAQMRAf/EAK4AAQACAQUAAAAAAAAAAAAAAAAGBwMBAgQFCQEBAQEAAAAAAAAAAAAAAAAAAAIBEAABAwMFAQAAAAAAAAAAAAABAgMEIDAFABBAIRNBEQEAAQMDAgAPAAAAAAAAAAABAhESAwAgITFRMGFxgZHB0SIyQlJikhMzEgEAAAAAAAAAAAAAAAAAAABQEwEAAQMDAwQDAQAAAAAAAAABEQAhMSBBUTBhcRCBobGRwdHh/9oADAMBAAIRAxEAAAH10AAANdy0cV9ldduAZmXzqoIuP1IExqbUnYXN1dUgDkGE2gAAA//aAAgBAQABBQKsAkt4SP5yo5jP7tr83GXUPN5OQiRK3xEdh9aQlIysWMI9Hq7okqNj/9oACAECAAEFArx60KPnA//aAAgBAwABBQK+eL//2gAIAQICBj8CE//aAAgBAwIGPwIT/9oACAEBAQY/At4HK8B3dUySyOROZDQH7SnTy6nhWtvR7xSo7ITpWycZ06VtRpXx6jlg1jMqes8zqUsfwxiYyX1W1V9LsyuaJP8AXZZFeObqqfN00RiEYhQDgDsGp5iEIZhjRj7t10y6sTiTzt/pk/OXt1VVe7y+B//aAAgBAQMBPyHWCCgASowAF1WgkKIeKuSiD3VE4oY4ivOhh7mjvG17KjFlXXSHGycJQ96uNTocwIhBZPE6H1Qa053yEIZkvQsxDglgFgCm3JXBjsiBMxOnNvZ3LRfdYpY5ZRTa6ytuj//aAAgBAgMBPyHWEsGae1DOgszQlXLnRGW8+mMdf//aAAgBAwMBPyHoFJHjQkkUXKsY40b+hfPX/9oADAMBAAIRAxEAABAAAAXkAYQAUgAAAAAAD//aAAgBAQMBPxDWge4RkOKYALrSgmGpiQfgXH6bYnbt4G0LJaYNALkqNgOMIbMwxMxWD8oRQpLCoDYNRADMg1kc8VxDK8HqdYoRCgkxogZGKExZ+cEQ5ABAU9kSwmaUuJFJmJ02RacmygSGSAvsVEDO3gAlwABLg6P/2gAIAQIDAT8Q1oWYxxnu2ogx86gdxjQoODSIOInyJI+5Qs8k/AfMT7xoAQw2I4t+/wBU3zTcGC47R8e3X//aAAgBAwMBPxDWoEuKSku9WjuJPr7E0SHIinsQ8UGTmx87/htobg2j+UAYrI8Jm+f9xfr/AP/Z");
}
.input-group-addon.password  {
background-image: url("data:image/jpg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAABkAAD/4QMpaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjAtYzA2MCA2MS4xMzQ3NzcsIDIwMTAvMDIvMTItMTc6MzI6MDAgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzUgV2luZG93cyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpGRTVGMDg0QUM3MjIxMUU1ODA2N0Q4MUREQjIxNDQ0OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpGRTVGMDg0QkM3MjIxMUU1ODA2N0Q4MUREQjIxNDQ0OSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkZFNUYwODQ4QzcyMjExRTU4MDY3RDgxRERCMjE0NDQ5IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkZFNUYwODQ5QzcyMjExRTU4MDY3RDgxRERCMjE0NDQ5Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+/+4AJkFkb2JlAGTAAAAAAQMAFQQDBgoNAAAFZQAABfMAAAakAAAHa//bAIQAAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQICAgICAgICAgICAwMDAwMDAwMDAwEBAQEBAQECAQECAgIBAgIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMD/8IAEQgAKQApAwERAAIRAQMRAf/EAMEAAQACAgMAAAAAAAAAAAAAAAADBgUHAQIJAQEBAQEAAAAAAAAAAAAAAAAAAgMBEAABAgUEAwEAAAAAAAAAAAACAAEQMAMEBSAhMUJBMhM0EQACAAMDBw0BAAAAAAAAAAABAhESAwAgITFRsSITIzMQMEFhcdEyUpKywnMEFBIAAQIHAQAAAAAAAAAAAAAAASAwAEARIUFxwRITAQABAwIFAwUBAAAAAAAAAAERACExIFEQMPBhgUGR4UBxobHB0f/aAAwDAQACEQMRAAAB9dAAADly2O1fiLoDO6TcMqwXLp9QBc9Y2rlevY11zeQHcmIiMAAAH//aAAgBAQABBQLXymw9y7GBUyjjqBV7qtjbqpe5mgQXEcH+pZ7iLEQr7VkRkUn/2gAIAQIAAQUCmsh20t6LtO//2gAIAQMAAQUCnPuusfK6Tv/aAAgBAgIGPwJ31iLpOx2R/9oACAEDAgY/An6qOx1//9oACAEBAQY/Ar8BlsIvQVzAikzmbryKREWZHErKYMD0G5TlK7plrNNHFUdYgQB1sbf07SlLtUYazzBFhhw4ZBnttyVlreGEZt2iAzYQuVPob30+T83bV+FyKsVOcEjRbi1PW3fbWZmh5iTp5n//2gAIAQEDAT8h1goBUgASq2ADK05G9gNFDdKMSd6butmg/Cd8OiOpegeqy1hMHeoHEhps2wQfQspfYMCgYWSJcQupV0zbRZspEpjaUMW4DoPOoss5iTExyf/aAAgBAgMBPyHkjNzQbKwt7jFS8n9/yjMakP45/wD/2gAIAQMDAT8h5BWNDtVqDakkbIH730OHA6jbnv/aAAwDAQACEQMRAAAQAAADDAHpAHFAEEAAAA//2gAIAQEDAT8Q1vQU8uABUMAXWhs7ZW2IAEJ5ZEXGr+RIwkioyBQRFEdBaGH249AQkeopScE3LJtchIt27S4sv5Z5iXqZmMcqPvcoyay0VIEhjFuFH2PRa7IExmOT/9oACAECAwE/ENYKwXWm0hdMx18d6AZDRFAPkTIeEntaKt1SzWNlJ+h8Uobyy+NHRtuDp88//9oACAEDAwE/ENagS2CkILYcT189qRUOdD82opKEQneL+JlqGY8wqY97ES3xo/Nf19DJ/9k=");
}
</style>
<script type="text/javascript">
  $(function(){
    $("#loginbtn").focus();
    // 绑定Enter键
    $(document).keydown(function(event){
      if(event.keyCode==13){
        login();
      }
    });
    $("#loginbtn").click(function(){
      login();
    });
  })

  function login(){
    var paramStr = $('#formlogin').serialize()
  	var paramArr = paramStr.split('&')
  	if (!paramArr[0].split('=')[1]) {
		alert('没有填写邮箱')
		return
  	}
  	if (!paramArr[1].split('=')[1]) {
		alert('没有填写密码')
		return
  	}
    $.ajax({
      type: "post",
      url:"../login.do",
      data:$('#formlogin').serialize(),
      async: true,
      success: function(data) {
        if (data.statusCode == 402) {
          $(".login-error").show();
          $(".login-error").html("用户名或密码错误。");
        } else {
          window.location.href="../pages/index-dev.jsp";
        }
      }
    });
  }
</script>

<body class="login">
	<div class="menu-toggler sidebar-toggler"></div>
	<div class="logo">
		<a href="https://www.dataeye.com/"> <img src="https://www.dataeye.com/ptlogin/assets/img/logo.png" />
		</a>
	</div>
	<div class="content">
		<form class="login-form" id="formlogin">
			<h3 class="form-title font-green">登 录</h3>
			<div class="login-error"></div>
			<div class="form-group">
				<div class="input-group">
					<span class="input-group-addon user"></span> <input
						class="form-control" type="text" autocomplete="off"
						placeholder="邮箱" name="email" />
				</div>
			</div>
			<div class="form-group">
				<div class="input-group">
					<span class="input-group-addon password"></span> <input
						class="form-control" type="password" autocomplete="off"
						placeholder="密码" name="password" />
				</div>
			</div>
			<div class="form-actions">
				<button type="button" id="loginbtn" class="btn btn-primary btn-lg btn-block">登 录</button>
			</div>
		</form>
	</div>
	<div class="copyright">2016 © DataEye 粤ICP备13074195-1号</div>
</body>
</html>
