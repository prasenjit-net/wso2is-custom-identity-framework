<%@ page import="org.owasp.encoder.Encode" %>
<jsp:directive.include file="init-url.jsp"/>

<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <title>Custom Reseller Portal Login</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet"
    	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
    	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
    	crossorigin="anonymous">

    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>
    <!-- Custom styles for this template -->
    <link href="css/floating-labels.css" rel="stylesheet">
  </head>
  <body>
  <form action="<%=commonauthURL%>" method="post" id="loginForm" class="form-signin">
  <div class="text-center mb-4">
    <img class="mb-4" src="https://getbootstrap.com/docs/4.4/assets/brand/bootstrap-solid.svg" alt="" width="72" height="72">
  </div>

  <div class="form-label-group">
	<input class="form-control" type="text" id="username-orig" name="username-orig" placeholder="Enter username" required autofocus>
	<input type="hidden" id="username" name="username">
    <label for="inputEmail">Email address</label>
  </div>

  <div class="form-label-group">
    <input class="form-control" type="password" id="password" name="password" placeholder="Enter password" required>
    <label for="inputPassword">Password</label>
  </div>

  <div class="checkbox mb-3">
    <label>
      <input id="chkRemember" type="checkbox" name="chkRemember"> Remember me
    </label>
  </div>
  <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    <input type="hidden" name="sessionDataKey" value='<%=Encode.forHtmlAttribute(request.getParameter("sessionDataKey"))%>'/>
</form>
<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="js/append-tenant.js" type="text/javascript"></script>
</body>
</html>
