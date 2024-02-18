<%@ page import="Model.DAOs.ShippingPropertyDAO" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="Model.Beans.ShippingProperty" %>
<%@ page import="Model.Exceptions.NotFoundException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.DAOs.CourierDAO" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page import="Model.Beans.Courier" %>
<%@ page import="Model.Beans.AssignmentDispatcher" %>
<%@ page import="Model.DAOs.AssignmentDispatcherDAO" %>
<%@ page import="Model.DAOs.ContractDAO" %><%--
  Created by IntelliJ IDEA.
  User: Francesco Monzillo
  Date: 26/01/2024
  Time: 11:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create a Contract with a Courier</title>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/generalLayout.css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/formOrderSubmittingLayout.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
</head>
<body>
<div class="layout">
  <!-- Inserimento sezione tramite l'include -->
  <%@include file="section.jsp"%>

  <div class="body">
    <div class="body_content">
      <div id="formDiv">
        <form id="form" action="orderPublishingServlet" method="post">

          <div id="insideForm">

            <div class="title">Associa un Corriere affiliato</div>

            <%

              DataSource ds = (DataSource) request.getServletContext().getAttribute("DataSource");
              CourierDAO courierDAO = new CourierDAO(ds);
              ContractDAO contractDAO = new ContractDAO(ds);

              ArrayList<Courier> couriers = null;
              AssignmentDispatcher assDisp = (AssignmentDispatcher) session.getAttribute("Account");
              ArrayList<Courier> couriersContract = null;

              boolean agreementsFound = true;

              boolean atLeastOneCourier = false;
              try{
                couriersContract = contractDAO.assDispRetrieveCouriersCurrent(assDisp.getEmail());
              }catch (NotFoundException e){
                agreementsFound = false;
              }catch (Exception e){
                System.out.println("Error trying to retrieve contract associated with Assignment Dispatcher");
                agreementsFound = false;
              }

              try{
                couriers = courierDAO.retrieveAll();

            %>
              <label for="couriers" class="placeholder">Corriere</label>
              <select class="input" name="couriers" id="couriers">

                <%for(int i = 0; i < couriers.size(); i++){
                  Courier c = couriers.get(i);
                  boolean courierWithContract = false;

                  if(agreementsFound) {
                    for (int j = 0; j < couriersContract.size(); j++) {
                      if ( c.getEmail().equals(couriersContract.get(j).getEmail()) ) {
                        courierWithContract = true;
                      }
                    }
                  }

                  if(!courierWithContract){
                    atLeastOneCourier = true;
                  %>
                    <option value="<%=c.getName()%>"><%=c.getName()%></option>
                  <%
                  }
                }%>
              </select>
              <%
                if(!atLeastOneCourier){%>
                  <p>Hai stipulato contratti con tutti i corrieri disponibili...</p>
              <%}%>
              <%}catch (NotFoundException e){%>
              <p>Non c'è ancora nessun corriere disponibile... il servizio sarà operativo quando ce ne sarà almeno uno</p>
            <%}%>

            <%
              ShippingPropertyDAO shippingPropertyDAO = new ShippingPropertyDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
              try{
                ArrayList<ShippingProperty> properties = shippingPropertyDAO.retrieveAll();%>

            <div class="propertiesDiv">
              <p>Proprietà di Spedizione Pattuite</p>

              <%for(int i = 0; i < properties.size(); i++) {
                String currName= properties.get(i).getName();
              %>
              <div class="propertyDetailDiv">
                <input type="checkbox" class="propertyCheckbox" id="<%=currName%>" name="<%=currName%>" value="<%=currName%>">
                <label style="display: inline-block" for="<%=currName%>"><%=currName%></label><br>
              </div>
              <%}%>
            </div>
            <%}catch (NotFoundException e) {%>
                <p>Non ci sono proprietà definite nel sistema... il servizio di selezione delle proprietà sarà disponibile quando ce ne saràa almeno una</p>
            <%}catch (Exception e){
              System.out.println(e);
            }%>

            <p class="errors" id="initial_date_error"></p>
            <label for="data_inizio"><b>Data di inizio</b></label>
            <input class="input" type="date" placeholder="Inizio del contratto" name="data_inizio" id="data_inizio">

            <p class="errors" id="final_date_error"></p>
            <label for="data_fine"><b>Data di fine</b></label>
            <input class="input" type="date" placeholder="Fine del contratto" name="data_fine" id="data_fine">



            <button type="button" class="submit" onclick="createContract()">Invia</button>

            <div style="color: transparent;">HiddenDiv</div>

          </div>
        </form>
      </div>
    </div>
  </div>
</div>


<%
  AssignmentDispatcherDAO assignmentDispatcherDAO = new AssignmentDispatcherDAO(ds);
  AssignmentDispatcher assignmentDispatcher = (AssignmentDispatcher) session.getAttribute("Account");

  String assDispEmail = null;

  if(assignmentDispatcher != null)
    assDispEmail = assignmentDispatcher.getEmail();
%>
<script>
  var today = new Date()

  var yy = today.getFullYear()
  var mm = today.getMonth() + 1 // the months are indexed starting with 0
  var dd = today.getDate()

  if (dd < 10) {
    dd = '0' + dd;
  }

  if (mm < 10) {
    mm = "0" + mm;
  }

  var dateStr = yy + "-" + mm + "-" + dd



  var input = document.getElementById("data_inizio")
  input.setAttribute("min", dateStr)

  input = document.getElementById("data_fine")
  input.setAttribute("min", dateStr)

  function createContract() {

    if (checkEventualErrors()) {

      var courier = document.getElementById("couriers").value;

      var dataInizio = document.getElementById("data_inizio").value;
      var dataFine = document.getElementById("data_fine").value;

      var propertiesList = document.getElementsByClassName("propertyCheckbox");

      var checkedProperties = [];

      for (var i = 0; i < propertiesList.length; i++) {
        if (propertiesList.item(i).checked) {
          checkedProperties.push(propertiesList.item(i).value);
        }
      }


      var insertedOrderId = $.ajax({
        type: 'POST',
        url: '<%=request.getContextPath()%>/createContractServlet',
        data: {
          'courier': courier,
          'properties': JSON.stringify(checkedProperties),
          'dispatcher': "<%=assDispEmail%>",
          'dataInizio': dataInizio,
          'dataFine': dataFine
        },
        statusCode: {
          200: function () {
            $.ajax({
              type: 'POST',
              dataType: 'jsonp',
              url: '<%=request.getServletContext().getAttribute("FunctionDomain")%>/contractStipulatedWithCourier',
              data: {
                'code': '<%=System.getenv("functionDomainCodeAccess")%>',
                'courierName': courier,
                'topicName': "<%=assignmentDispatcher.getName()%>",
              },
              statusCode: {
                200: function () {
                  location.href = '<%=request.getContextPath()%>/homepage.jsp?contractEstablished=true';
                },
                404: function () {
                  subscriberNotCreated(); //Configurare questa funzione per fare in modo di allertare l'utente con la comparsa di
                }             //un elemento grafico nel caso in cui il publish dell'ordine sulla coda Azure Queue non è andato bene
              },
              success: function (res) {

              }
            });
          },
          400: function () {
            contractCreationError(); //Configurare questa funzione per fare in modo di allertare l'utente con la comparsa di
          }               //un elemento grafico nel caso in cui il login non è andato a buon fine
        }
      }).responseText;
    }
  }


  function subscriberNotCreated(){
      location.href = '<%=request.getContextPath()%>/homepage.jsp?QueueError=1'
  }

  function contractCreationError(){
      location.href = '<%=request.getContextPath()%>/homepage.jsp?SystemError=1'
  }

  function checkEventualErrors(){


    today = yy + "-" + mm + "-" + dd;

    var booleano = true;

    var data_inizio = document.getElementById("data_inizio").value;
    var data_fine = document.getElementById("data_fine").value;

    if(data_inizio === "" || data_inizio<today) {
      document.querySelector("#initial_date_error").innerHTML = "Inserire una data valida: non vuota e precedente alla data odierna";
      document.querySelector("#initial_date_error").style.display = "block";
      booleano = false;
    }

    if(data_fine === "" || data_fine<today) {
      document.querySelector("#final_date_error").innerHTML = "Inserire una data valida: non vuota e precedente alla data odierna";
      document.querySelector("#final_date_error").style.display = "block";
      booleano = false;
    }

    return booleano;
  }

</script>



</body>
</html>
