<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Beans.Orders" %>
<%@ page import="Model.DAOs.StateDAO" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="Model.Beans.Updates" %>
<%@ page import="Model.Exceptions.NotFoundException" %><%--
  Created by IntelliJ IDEA.
  User: Francesco Monzillo
  Date: 23/01/2024
  Time: 12:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <%if(request.getParameter("active") == null || Integer.parseInt(request.getParameter("active")) == 0){%>
      <title>Storico Ordini</title>
  <%}else{%>
      <title>Spedizioni in corso</title>
  <%}%>

  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/generalLayout.css">
  <!--<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/formOrderSubmittingLayout.css">-->
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/visualizeOrderLayout.css">

</head>
<body>

  <div class="layout">
    <!-- Inserimento sezione tramite l'include -->
    <%@include file="section.jsp"%>

    <div class="body">
      <div class="body_content">
      <% Object o = request.getAttribute("orderList");%>
      <div id="ordersContainer">
      <%if(o == null){%>
          <p>Non ci sono ordini</p>
      <%}else{
        ArrayList<Orders> orders = (ArrayList<Orders>) o;
        boolean atLeastOneVisualized = false;
        for(int i = 0; i < orders.size(); i++){
          Orders order = orders.get(i);
          System.out.println(order);%>

        <%StateDAO stateDAO = new StateDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
          Updates update = null;
          String orderShipmentState = null;
          try{
            update = stateDAO.retrieveLastUpdateByOrder(order);
            orderShipmentState = update.getState().getPhase();
          }catch (NotFoundException e){
            orderShipmentState = "In lavorazione";
          }catch (Exception e){
            System.out.println(e);
          }
          if(request.getAttribute("onlyNotAlreadyDelivered") != null){
        if(! orderShipmentState.equals("Consegnato")){
          atLeastOneVisualized = true;
        %>
        <div class="ordersDiv" style="height: 76%" id="<%=order.getId()%>">
          <form action="<%=request.getContextPath()%>/orderDetailsServlet" method="post">
            <p class="titlePar">Assegnato in data <%=order.getAssignmentDate()%></p>
            <p class="littleDetailsPars">Per conto di: <%=order.getOrderDisp().getName()%></p>
            <p class="littleDetailsPars">Data di creazione: <%=order.getCreationDate()%></p>
            <p class="littleDetailsPars">Stato: <%=order.getState()%></p>
            <p class="littleDetailsPars">Paese: <%=order.getCountry()%></p>
            <p class="littleDetailsPars">Stato di consegna: <%=orderShipmentState%></p>

            <input type="hidden" name="orderId" value="<%=order.getId()%>">
            <input type="hidden" name="orderDisp" value="<%=order.getOrderDisp().getName()%>"> <!--Preoccuparsi di ottenere anche il nome del dispatcher-->
            <input type="hidden" name="orderCreationDate" value="<%=order.getCreationDate()%>">
            <input type="hidden" name="orderAssignmentDate" value="<%=order.getAssignmentDate()%>">
            <input type="hidden" name="orderShipmentState" value="<%=orderShipmentState%>">
            <input type="hidden" name="orderState" value="<%=order.getState()%>">
            <input type="hidden" name="orderCountry" value="<%=order.getCountry()%>">
            <input type="hidden" name="orderLenght" value="<%=order.getLength()%>">
            <input type="hidden" name="orderWidth" value="<%=order.getWidth()%>">
            <input type="hidden" name="orderHeight" value="<%=order.getHeight()%>">
            <input type="hidden" name="orderWeight" value="<%=order.getWeight()%>">
            <input type="hidden" name="orderTrackingCode" value="<%=order.getTrackingCode()%>">

            <button type="submit"  class="orderDetailsButton">Dettagli</button>
          </form>
        </div>
        <%}%>
      <%}else{
        atLeastOneVisualized = true;
      %>
        <div class="ordersDiv" id="<%=order.getId()%>">
          <form action="<%=request.getContextPath()%>/orderDetailsServlet" method="post">
            <p class="titlePar">Assegnato in data <%=order.getAssignmentDate()%></p>
            <p class="littleDetailsPars">Per conto di: <%=order.getOrderDisp().getName()%></p>
            <p class="littleDetailsPars">Data di creazione: <%=order.getCreationDate()%></p>
            <p class="littleDetailsPars">Stato: <%=order.getState()%></p>
            <p class="littleDetailsPars">Paese: <%=order.getCountry()%></p>
            <p class="littleDetailsPars">Stato di consegna: <%=orderShipmentState%></p>

            <input type="hidden" name="orderId" value="<%=order.getId()%>">
            <input type="hidden" name="orderDisp" value="<%=order.getOrderDisp().getName()%>">
            <input type="hidden" name="orderCreationDate" value="<%=order.getCreationDate()%>">
            <input type="hidden" name="orderAssignmentDate" value="<%=order.getAssignmentDate()%>">
            <input type="hidden" name="orderState" value="<%=order.getState()%>">
            <input type="hidden" name="orderCountry" value="<%=order.getCountry()%>">
            <input type="hidden" name="orderLenght" value="<%=order.getLength()%>">
            <input type="hidden" name="orderWidth" value="<%=order.getWidth()%>">
            <input type="hidden" name="orderHeight" value="<%=order.getHeight()%>">
            <input type="hidden" name="orderWeight" value="<%=order.getWeight()%>">
            <input type="hidden" name="orderTrackingCode" value="<%=order.getTrackingCode()%>">

            <button type="submit"  class="orderDetailsButton">Dettagli</button>
          </form>
        </div>
      <%}%>

        <%}
          if(request.getAttribute("onlyNotAlreadyDelivered") != null){
            if(!atLeastOneVisualized){%>
              <p>Non ci sono ordini attivi al momento...</p>
        <%}%>
      <%}%>
      <%}%>
      </div>
      </div>
    </div>
  </div>
</body>
</html>
