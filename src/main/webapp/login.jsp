<%--
  Created by IntelliJ IDEA.
  User: Francesco Monzillo
  Date: 22/01/2024
  Time: 12:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/LoginLayout.css">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;600&display=swap" rel="stylesheet">
    <!--Stylesheet-->
    <style media="screen">
        *,
        *:before,
        *:after{
            padding: 0;
            margin: 0;
            box-sizing: border-box;
        }

        form{
            height: 520px;
            width: 400px;
            background-color: rgba(255,255,255,0.13);
            position: absolute;
            transform: translate(-50%,-50%);
            top: 50%;
            left: 50%;
            border-radius: 10px;
            backdrop-filter: blur(10px);
            border: 2px solid rgba(255,255,255,0.1);
            box-shadow: 0 0 40px rgba(8,7,16,0.6);
            padding: 50px 35px;
        }
        form *{
            font-family: 'Poppins',sans-serif;
            color: #ffffff;
            letter-spacing: 0.5px;
            outline: none;
            border: none;
        }
        form h3{
            font-size: 32px;
            font-weight: 500;
            line-height: 42px;
            text-align: center;
        }

        label{
            display: block;
            margin-top: 30px;
            font-size: 16px;
            font-weight: 500;
        }
        input{
            display: block;
            height: 50px;
            width: 100%;
            background-color: rgba(255,255,255,0.07);
            border-radius: 3px;
            padding: 0 10px;
            margin-top: 8px;
            font-size: 14px;
            font-weight: 300;
        }
        ::placeholder{
            color: #e5e5e5;
        }
        button{
            margin-top: 50px;
            width: 100%;
            background-color: #ffffff;
            color: #080710;
            padding: 15px 0;
            font-size: 18px;
            font-weight: 600;
            border-radius: 5px;
            cursor: pointer;
        }

    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
</head>
<body>


<form id="loginForm">
    <h3>Login Here</h3>

    <label for="email">E-Mail</label>
    <input type="email" name ="email" placeholder="Email" id="email" pattern="[AZaz09]*@[A-Za-z0-9].com" title="Inserire una mail valida">

    <label for="password">Password</label>
    <input type="password" name ="password" placeholder="Password" id="password" pattern="[A-Za-z0-9]+" title="Inserire una password (non lasciare il campo vuoto)">

    <button type="button" onclick="verify()" id="formButton" >Log In</button>
</form>


<script>

    var loginErrorPrompted = false;

    function verify() {

        var formEmail = document.getElementById("email").value;
        var formPass = document.getElementById("password").value;

        $.ajax({
            type: 'POST',
            url: 'verifyAccountPresenceServlet',
            data: {
                'email': formEmail,
                'password': formPass
            },
            statusCode: {
                200: function() {
                    location.href = '<%=request.getContextPath()%>/homepage.jsp';
                },
                404: function (){
                    noResult(); //Configurare questa funzione per fare in modo di allertare l'utente con la comparsa di
                }               //un elemento grafico nel caso in cui il login non è andato a buon fine
            }
        });


    }

    function noResult(){

        if(loginErrorPrompted)
            return;
        else
            loginErrorPrompted = true;

        var form = document.getElementById("loginForm");

        const div = document.createElement("div")

        const para = document.createElement("p");
        para.setAttribute("id","errorPara");

        const node = document.createTextNode("Login non riuscito... Email o password sbagliate... riprovare");  //Creazione paragraph di errore
        para.appendChild(node);

        div.appendChild(para)

        form.appendChild(div);
    }
</script>

</body>
</html>
