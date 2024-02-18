<%@ page import="Model.DAOs.StateDAO" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Beans.State" %>
<%@ page import="Model.Exceptions.NotFoundException" %>
<%@ page import="Model.DAOs.OrdersDAO" %>
<%@ page import="Model.Beans.Orders" %>
<%@ page import="Model.DAOs.UserDAO" %>
<%@ page import="Model.Beans.User" %><%--
  Created by IntelliJ IDEA.
  User: Francesco Monzillo
  Date: 27/01/2024
  Time: 22:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Aggiorna Ordine</title>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/generalLayout.css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/formOrderSubmittingLayout.css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/updateSubmitting.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

</head>
<body>

<div class="layout">
  <%@include file="section.jsp"%>


  <div class="body" style="display:grid;">
    <div class="body_content" style="place-self: center;align-self: center;">

      <div id="formDiv" style="display: inline;">
        <form id="form" action="homepage.jsp" method="post">

          <div id="insideForm">

            <div class="title">Inserire Stato e Messaggio (Opzionale)</div>


            <%DataSource ds = (DataSource)request.getServletContext().getAttribute("DataSource");

              StateDAO stateDAO = new StateDAO(ds);
              ArrayList<State> states = null;

              try{
                states = stateDAO.retrieveAll();
              }catch (NotFoundException e){

              }catch (Exception e){
                System.out.println(e);
              }

              OrdersDAO ordersDAO = new OrdersDAO(ds);

              UserDAO userDAO = new UserDAO(ds);

              String email = null;
              boolean validUser = false;

              try{
                Orders o = ordersDAO.retrieve(Integer.parseInt(request.getParameter("id")));
                email = o.getOrderFinalUser().getEmail();
              }catch (NotFoundException e){
                System.out.println("Ordine raggiunto dal dettaglio dell'ordine, ma ora sembra sparito... impossibile... debuggare ulteriormente");
              }catch (Exception e){
                System.out.println(e);
              }

              User user = null;

              if(email != null) {
                try {
                  user = userDAO.retrieve(email);
                  validUser = true;
                }catch (NotFoundException e){

                }catch (Exception e){
                  System.out.println(e);
                }
              }

            %>

            <p class="errors" id="stateError"></p>
            <label for="stateSelection" class="placeholder">Stato</label>

            <select oninput="updateDescriptionPar()" id="stateSelection" class="input" name="stateSelection" type="text" placeholder=""  required>
              <option value="" selected disabled hidden>Scegli Stato</option>

              <%for (int i = 0; i < states.size(); i++){

                State currState = states.get(i);%>
              <option class="optionDarkColor" value="<%=currState.getPhase()%>"><%=currState.getPhase()%></option>
              <option id="<%=currState.getPhase()%>" disabled hidden value="<%=currState.getDescription()%>"></option>

              <%}%>
            </select>


            <p>Descrizione Stato</p>
            <b>
              <p id="descriptionPar"></p>
            </b>

            <p class="errors" id="messageError"></p>
            <label for="message" class="placeholder">Messaggio</label>
            <textarea id="message" class="input" name="message" placeholder="Inserire Messaggio qui (Massimo 1024 caratteri)" maxlength="1024"></textarea>

            <input type="hidden" id="userEmail" value="<%=email%>">
            <input type="hidden" id="orderId" value="<%=request.getParameter("id")%>">
            <input type="hidden" id="userValid" value="<%=validUser%>">
            <%if(user != null){%>
            <input type="hidden" id="nameOfUser" value="<%=user.getName()%>">
            <%}%>

            <div id="buttonContainer">
              <button id="sendUpdateToOrder" type="button" style="width: 40%;" onclick="sendUpdate()">Invia Aggiornamento</button>
            </div>
          </div>
        </form>
      </div>

    </div>
  </div>

</div>

<script>

  function updateDescriptionPar(){
    var descPar = document.getElementById("descriptionPar")
    var selectState = document.getElementById("stateSelection");
    var inputToLook = document.getElementById(selectState.value);

    descPar.innerHTML = inputToLook.value;

  }

  function sendUpdate(){

    var formElement = document.getElementById("form")

    var userEmail = document.getElementById("userEmail")

    var selectState = document.getElementById("stateSelection")

    var textAreaElement = document.getElementById("message")

    if(userEmail.value !== "" && userEmail.value !== "null"){
      var validUserToNotify = document.getElementById("userValid")

      if(validUserToNotify.value === "true"){
        var nameOfUser = document.getElementById("nameOfUser")

        var additionalMessageString = ""

        if(textAreaElement.value.length > 0)
          additionalMessageString = "\nCon il seguente messaggio allegato:\n" + textAreaElement.value


        var updateOrder = $.ajax({
          type: 'POST',
          url: '<%=request.getContextPath()%>/insertUpdateServlet',
          data: {
            'orderId':'<%=request.getParameter("id")%>',
            'statePhase': selectState.value,
            'message': textAreaElement.value
          },
          statusCode: {
            200: function() {
              var sendAlertToUser = $.ajax({
                type: 'POST',
                dataType: 'jsonp',
                url: '<%=request.getServletContext().getAttribute("FunctionDomain")%>/SendUpdateToUser',
                data: {
                  'code': '<%=System.getenv("functionDomainCodeAccess")%>',
                  'receiverAddress': userEmail.value,
                  'subject': "ShApp: Aggiornamento Ordine",
                  'message': "Ciao "+ nameOfUser.value +"\nL'ordine da te commissionato presso <%=request.getParameter("orderDisp")%> con codice di tracciamento: <%=request.getParameter("trackingCode")%>\nÈ stato aggiornato allo stato: " + selectState.value + additionalMessageString
                },
                statusCode: {
                  200: function() {
                    formElement.submit()
                  },
                  404: function (){
                    functionNegativeResult(); //Configurare questa funzione per fare in modo di allertare l'utente con la comparsa di
                  }             //un elemento grafico nel caso in cui il publish dell'ordine sulla coda Azure Queue non è andato bene
                },
                success: function (res){

                }
              });
            },
            400: function (){
              updateOrderError(updateOrder); //Configurare questa funzione per fare in modo di allertare l'utente con la comparsa di
            }               //un elemento grafico nel caso in cui il login non è andato a buon fine
          }
        }).responseText;

      }
    }else{
      $.ajax({
        type: 'POST',
        url: '<%=request.getContextPath()%>/insertUpdateServlet',
        data: {
          'orderId':'<%=request.getParameter("id")%>',
          'statePhase': selectState.value,
          'message': textAreaElement.value
        },
        statusCode: {
          200: function() {
            formElement.submit()
          },
          400: function (){
            updateOrderError(updateOrder); //Configurare questa funzione per fare in modo di allertare l'utente con la comparsa di
          }               //un elemento grafico nel caso in cui il login non è andato a buon fine
        }
      });
    }

  }

  function functionNegativeResult(){
    location.href = '<%=request.getContextPath()%>/homepage.jsp?error=updateFunction'
  }
  
  function updateOrderError() {
    
  }

</script>


</body>
</html>
