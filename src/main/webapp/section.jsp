<%--
  Created by IntelliJ IDEA.
  User: Francesco Monzillo
  Date: 23/01/2024
  Time: 12:13
  To change this template use File | Settings | File Templates.
--%>

<%Object account  = session.getAttribute("Account");
  Object role = session.getAttribute("accessRole");
  int accessRole;
%>
<div class="sidebar">
  <div class="sidebar_content">
    <%if(account == null) {%>
        <!--<a href="<%=request.getContextPath()%>/userSignUpServlet">Sign-Up</a>-->
        <a href="<%=request.getContextPath()%>/.auth/login/google">Login</a>
    <%}else {
      accessRole = (int) role;

      if(accessRole == 1){%>
        <a href="<%=request.getContextPath()%>/visualizeOrdersServlet?active=1">Spedizioni in corso</a>
        <a href="<%=request.getContextPath()%>/visualizeOrdersServlet?active=0">Storico Ordini</a>
    <%}else if(accessRole == 2){%>

        <a href="<%=request.getContextPath()%>/orderSubmitting.jsp">Nuovo Ordine</a>
        <a href="<%=request.getContextPath()%>/createContract.jsp">Nuovo Contratto</a>
        <a href="<%=request.getContextPath()%>/visualizeOrdersServlet">Storico Ordini</a>
    <%}else{%>
        <a href="<%=request.getContextPath()%>/visualizeOrdersServlet?active=1">Spedizioni in corso</a>
        <a href="<%=request.getContextPath()%>/visualizeOrdersServlet?active=0">Storico Ordini</a>
    <%}%>
        <a onclick="logoutEvent()">Logout</a>
    <%}%>


  </div>
</div>


<script>

    function logoutEvent(){
        $.ajax({
            type: 'POST',
            dataType: 'jsonp',
            url: '<%=request.getContextPath()%>/.auth/logout',
            data: {
            },
            statusCode: {
                200: function() {
                    location.href = '<%=request.getContextPath()%>/userLogoutServlet';
                },
                404: function (){
                    console.log("Impossible to logout?")
                    location.href = '<%=request.getContextPath()%>/homepage.jsp';
                }
            },
            error: function (){
                console.log("Impossible to logout? PT.2")
                location.href = '<%=request.getContextPath()%>/homepage.jsp';
            }
        });


    }

</script>
