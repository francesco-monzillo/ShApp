<%--
  Created by IntelliJ IDEA.
  User: Francesco Monzillo
  Date: 02/02/2024
  Time: 10:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registrati</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/generalLayout.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/formOrderSubmittingLayout.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/signUp.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
</head>
<body>

<div class="layout">

    <%@include file="section.jsp"%>

    <div class="body">
        <div class="body_content">
            <div id="formDiv">
                <form id="form" action="homepage.jsp" method="post">
                    <div id="insideForm">

                        <div class="title">Registrati</div>

                        <p class="errors" id="radioError"></p>
                        <div style="display:grid;">
                            <div class="radioInput">
                                <label onclick="organize('1')"><input type="radio" name="accountType" value="1"/>Utente Finale</label>
                            </div>

                            <div class="radioInput">
                                <label onclick="organize('2')"><input type="radio" name="accountType" value="2"/>AssignmentDispatcher</label>
                            </div>

                            <div class="radioInput">
                                <label onclick="organize('3')"><input type="radio" name="accountType" value="3"/>Corriere</label>
                            </div>
                        </div>

                        <input type="hidden" id="signalSuccess" name="signalSignUpSuccess">

                        <p class="errors" id="nameError"></p>
                        <label id="nameLabel" for="name" class="placeholder" style="display: none">Nome</label>
                        <input id="name" class="input" name="name" type="text" placeholder="" required hidden/>

                        <p class="errors" id="surnameError"></p>
                        <label id="surnameLabel" for="surname" class="placeholder" style="display:none;">Cognome</label>
                        <input id="surname" class="input" name="surname" type="text" placeholder="" required hidden/>

                        <p class="errors" id="districtError"></p>
                        <label id="phoneNumberLabel"for="phoneNumber" class="placeholder" style="display: none">Telefono</label>
                        <input id="phoneNumber" class="input" name="phoneNumber" type="tel" placeholder="" required hidden/>

                        <label onclick="signUpUser()"><button type="button">Invio</button></label>

                    </div>

                </form>

                <form style="display: none" action="verifyAccountPresenceServlet" id="emailAlreadyRegisteredCaseForm" method="post">

                    <input type="hidden" name="email" value="<%=request.getAttribute("email")%>">

                </form>

            </div>

        </div>

    </div>
</div>

</body>


<script>
    function organize(value){


        var name = document.getElementById("name")
        var nameLabel = document.getElementById("nameLabel")

        var surname = document.getElementById("surname")
        var surnameLabel = document.getElementById("surnameLabel")

        var phoneNumber = document.getElementById("phoneNumber")
        var phoneNumberLabel = document.getElementById("phoneNumberLabel")

        if(value === "1"){

            name.hidden = false
            nameLabel.style.display = "block"

            surname.hidden = false
            surnameLabel.style.display = "block"

            phoneNumber.hidden = false
            phoneNumberLabel.style.display = "block"

        }else{

            name.hidden = false
            nameLabel.style.display = "block"

            surname.hidden = true
            surnameLabel.style.display = "none"

            phoneNumber.hidden = false
            phoneNumberLabel.style.display = "block"

        }

    }
</script>

<script>

    function signUpUser(){

        var checkedElement = document.querySelector('input[name="accountType"]:checked');
        var name = document.getElementById("name");
        var surname = document.getElementById("surname");
        var phone = document.getElementById("phoneNumber");

        $.ajax({
            type: 'POST',
            dataType: 'jsonp',
            url: '<%=request.getContextPath()%>/signUpNewUser',
            data: {
                'email': '<%=request.getAttribute("email")%>',
                'name': name.value,
                'surname': surname.value,
                'phoneNumber': phone.value,
                'accountType': checkedElement.value
            },
            statusCode: {
                200: function() {
                    if(checkedElement.value === '2') {
                        $.ajax({
                            type: 'POST',
                            dataType: 'jsonp',
                            url: '<%=request.getServletContext().getAttribute("FunctionDomain")%>/addTopic',
                            data: {
                                'code': '<%=System.getenv("functionDomainCodeAccess")%>',
                                'topicName': name.value
                            },
                            statusCode: {
                                200: function () {
                                    document.getElementById("form").submit()
                                },
                            },
                            error: function (res) {
                                location.href = '<%=request.getContextPath()%>/homepage.jsp?error=topicAddError'
                            }
                        });
                    }else {
                        document.getElementById("form").submit()
                    }
                },
                400: function (){
                    document.getElementById("emailAlreadyRegisteredCaseForm").submit()
                },
                401: function (){
                    var nameError = document.getElementById("nameError");
                    nameError.innerHTML = "Ogni corriere o Assignment Dispatcher devono avere nomi unici... Provare con un altro"
                },
                404: function (){
                    location.href = '<%=request.getContextPath()%>/homepage.jsp?error=signUp'
                },
                405: function (){
                    var radioError = document.getElementById("radioError")
                    radioError.innerHTML = "Tipo di account passato non valido... selezionare una tra queste tre opzioni"
                }
            },

        });
    }

</script>

</html>
