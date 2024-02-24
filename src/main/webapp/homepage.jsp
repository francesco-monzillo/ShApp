<%--
  Created by IntelliJ IDEA.
  User: Francesco Monzillo
  Date: 12/01/2024
  Time: 12:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*"%>
<html>
<head>
    <title>Homepage</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/generalLayout.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

</head>
<body>


<div class="layout">

    <%@include file="section.jsp"%>

    <div class="body">
        <% if(account == null) {%>
            <p id = "accType" > Access Type </p >
        <%}%>
        <div class="body_content">
            <% if(account == null) {%>
            <div>
                <p> Assignment Dispatcher: Genera nuovi ordini da assegnare, dichiara contratti con i corrieri e visualizza l'andamento degli ordini</p >
            </div>

            <div>
                <p > Courier: Visualizza gli ordini a te assegnati, aggiorna lo stato degli ordini e visualizza lo storico delle spedizioni </p >
            </div>

            <div>
                <p > End-User: Visualizza gli ordini effettuati da diversi e-commerce, negozi fisici, e qualsiasi altro dispatcher affiliato, e seguine gli aggiornamenti</p >
            </div>
            <%}else{
                accessRole = (int) role;
                if(accessRole == 1){

                }else if (accessRole == 2){

                }else{

                }
            }%>
        </div>
    </div>
</div>

</body>
</html>
