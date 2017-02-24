  <%@ page language="java" contentType="text/html; charset=utf-8"%>
    <%@ include file="./include.jsp" %>
    <!DOCTYPE html>
    <html>
    <head>
    <meta charset="UTF-8"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keywords" content="DataEye,游戏数据分析,泛娱乐"/>
    <meta name="description" content="DataEye是中国泛娱乐领军数据服务商，公司旗下主要有手游数据分析，移动广告效果追踪监测分析"/>
    <title><%=siteTitle%></title>
    <link rel="shortcut icon" href="https://www.dataeye.com/assets/img/favicon.ico" />
    <link rel="stylesheet" href="<%=CDN_PATH%>/assets-dist/css/monitor.bundle.f1f791b569e69cd6a614.css">
    </head>
    <body>
    <div id="container"></div>
    <script>
    (function() {
    // 是否使用mock
    var useMock = location.search.indexOf('mock') > -1
    var segments = location.pathname.split('/')
    var contextPath = segments[1] !== 'pages' ? '/' + segments[1] : ''

    window.App = {
    // 生产环境自动排除mock
    useMock: location.pathname.indexOf('-dev') > -1 && useMock,
    // 项目根路径
    CONTEXT_PATH: contextPath,
    uid: <%=userID%>,
    userName: '<%=userName%>'
    }
    })()
    </script>

    <script src="<%=CDN_PATH%>/assets-dist/<%=PROJECT_NAME%>/common.f1f791b569e69cd6a614..js"></script>
    <script src="<%=CDN_PATH%>/assets-dist/<%=PROJECT_NAME%>/app.c9cbceea085f0c03458f.js"></script>
