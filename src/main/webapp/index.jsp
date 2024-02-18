<html>
<body>



<form id="submitForm" method="post" action="userCallBackLoginServlet" hidden>
    <input id="authenticationResponse" type="hidden" name="passedData">

</form>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script>

    var auth_data = $.ajax({
        type: 'GET',
        url: '<%=request.getContextPath()%>/.auth/me',
        data: {
        },
        success: function(response) {
            var resp = document.getElementById("authenticationResponse");
            resp.value = JSON.stringify(response)
            document.getElementById("submitForm").submit()
        },
        error: function (){
            location.href= '<%=request.getContextPath()%>/homepage.jsp'
        }
    }).responseText;

</script>

</body>
</html>
