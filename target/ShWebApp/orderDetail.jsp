<%@ page import="java.lang.reflect.Array" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Beans.Updates" %>
<%@ page import="Model.Beans.Courier" %><%--
  Created by IntelliJ IDEA.
  User: Francesco Monzillo
  Date: 26/01/2024
  Time: 23:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dettaglio Ordine</title>

  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/generalLayout.css">
  <!--<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/formOrderSubmittingLayout.css">-->
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/visualizeOrderLayout.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

</head>
<body>

<div class="layout">

  <%@include file="section.jsp"%>

  <%Object o = session.getAttribute("Account");
    ArrayList<Updates> orderUpdates = (ArrayList<Updates>) request.getAttribute("orderUpdates");
  %>

  <div class="body detailBody">

    <div class="body_content">
      <div class="orderDetailDiv">
        <div class="orderDetailContainer">
          <p class="orderDetailsPar"><b>Data di Assegnazione:</b> <%=request.getParameter("orderAssignmentDate")%></p>
          <p class="orderDetailsPar"><b>Per conto di: </b><%=request.getParameter("orderDisp")%></p>
          <p class="orderDetailsPar"><b>Corriere: </b><%=request.getAttribute("orderCourier")%></p>
          <p class="orderDetailsPar"><b>Tracking Code: </b><%=request.getParameter("orderTrackingCode")%></p>
          <p class="orderDetailsPar"><b>Stato: </b><%=request.getParameter("orderState")%></p>
          <p class="orderDetailsPar"><b>Paese: </b><%=request.getParameter("orderCountry")%></p>
          <p class="orderDetailsPar"><b>Stato di consegna: </b><%=request.getAttribute("orderShipmentState")%></p>
          <p class="orderDetailsPar"><b>Descrizione Stato: </b><%=request.getAttribute("orderShipmentStateDescription")%></p>
          <p class="orderDetailsPar"><b>Lunghezza: </b><%=request.getParameter("orderLenght")%></p>
          <p class="orderDetailsPar"><b>Larghezza: </b><%=request.getParameter("orderWidth")%></p>
          <p class="orderDetailsPar"><b>Altezza: </b><%=request.getParameter("orderHeight")%></p>
          <p class="orderDetailsPar"><b>Peso (grammi): </b><%=request.getParameter("orderWeight")%></p>
        </div>

        <div class="updateContainer">
        <%if(orderUpdates == null){%>
        <p class="updatePars">No update available for this order...</p>
        <%}else{%>
          <%for(int i = 0; i < orderUpdates.size(); i++){
            Updates currUpdate = orderUpdates.get(i);%>

        <div class="updatesDiv">
          <p class="updatesPar diffColor"><%=i+1%>° aggiornamento</p>
          <p class="updatesPar"><b>State: </b><%=currUpdate.getState().getPhase()%></p>
          <p class="updatesPar"><b>Message: </b><%=currUpdate.getMessage()%></p>
        </div>

          <%}%>
        <%}%>
        </div>
      </div>

      <%if( (!(request.getAttribute("orderShipmentState")).equals("Consegnato")) && o instanceof Courier){%>
      <div class="buttonDiv">
        <button type="button" id="updateButton" onclick="updateOrder()">Aggiorna Stato</button>
      </div>
      <%}%>

      <form id="hiddenForm" action="updateOrder.jsp" method="post" hidden >
        <input type="hidden" name="id" id="id" value="<%=request.getAttribute("orderId")%>">
        <input type="hidden" name="trackingCode" id="trackingCode" value="<%=request.getParameter("orderTrackingCode")%>">
        <input type="hidden" name="orderDisp" id="orderDisp" value="<%=request.getParameter("orderDisp")%>">
      </form>

    </div>

  </div>

</div>


<script>

  function updateOrder(){
    //Implementare la chiamata corretta per l'aggiornamento dell'ordine
    var hiddenForm = document.getElementById("hiddenForm")

    hiddenForm.submit()

  }

</script>

</body>
</html>
