<%
    String relyingParty = request.getParameter("sp");

    if (relyingParty.equals("Reseller_Portal_OIDC")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("reseller_login.jsp");
        dispatcher.forward(request, response);
    } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("default_login.jsp");
        dispatcher.forward(request, response);
    }
%>