<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="com.dataeye.omp.module.cmdb.employee.Employee"%>
<%@page import="com.dataeye.omp.constant.Constant.SessionName" %>

<%
  Boolean isLogin = false;
  int userID = 1;
  String userName = "";

  Employee user = (Employee) session.getAttribute(SessionName.CURRENT_USER);
  if (user != null) {
    userID = user.getId();
    userName = user.getName();
    isLogin = true;
  }
  String appContext = request.getContextPath();
	if (user == null) {
		response.setContentType("text/html; charset=utf-8");
		//response.sendRedirect(appContext + "/admin/pages/login.jsp");
		String url = appContext + "/pages/login.jsp";
		out.print("<script>top.location.href='"+url+"'</script>");
		return;
	}
 // 生产环境资源域名前缀
   String CDN_PATH = "https://www.dataeye.com/static";

  // 项目名称
  String PROJECT_NAME = "monitor";
  // 本地开发默认资源路径
  String WEBPACK_PATH = "http://127.0.0.1:8080";
  // 站点默认标题
  String siteTitle = "运维监控平台 - DataEye";
%>
