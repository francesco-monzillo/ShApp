<%@ page import="Model.DAOs.ShippingPropertyDAO" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Beans.ShippingProperty" %>
<%@ page import="Model.Exceptions.NotFoundException" %>
<%@ page import="Model.Beans.AssignmentDispatcher" %>
<%@ page import="Model.DAOs.AssignmentDispatcherDAO" %><%--
  Created by IntelliJ IDEA.
  User: Francesco Monzillo
  Date: 23/01/2024
  Time: 22:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Crea un Ordine</title>

  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/generalLayout.css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/formOrderSubmittingLayout.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
</head>
<body>

<div class="layout">

  <%@include file="section.jsp"%>

  <div class="body">
    <div class="body_content">
      <div id="formDiv">
      <form id="form" action="orderPublishingServlet" method="post">

        <div id="insideForm">

        <div class="title">Crea Ordine</div>

        <p>Locazione</p>

        <p class="errors" id="stateError"></p>
        <label for="state" class="placeholder">Stato</label>
        <input id="state" class="input" name="state" type="text" placeholder="" required/>

        <p class="errors" id="countryError"></p>
        <label for="country" class="placeholder">Paese</label>
        <input id="country" class="input" name="country" type="text" placeholder="" required/>

        <p class="errors" id="districtError"></p>
        <label for="district" class="placeholder">Provincia</label>
        <input id="district" class="input" name="district" type="text" placeholder="" required/>

        <p class="errors" id="zipCodeError"></p>
        <label for="zipCode" class="placeholder">CAP</label>
        <input id="zipCode" class="input" name="zipCode" type="text" placeholder="" required/>

        <p class="errors" id="streetError"></p>
        <label for="street" class="placeholder">Via</label>
        <input id="street" class="input" name="street" type="text" placeholder="" required/>


        <label for="streetNumber" class="placeholder">N°</label>
        <input id="streetNumber" class="input" name="streetNumber" type="number" placeholder="" required/>

        <p>Dimensioni</p>

        <label for="lenght" class="placeholder">Lunghezza</label>
        <input id="lenght" class="input" name="lenght" type="number" placeholder="" required/>

        <label for="width" class="placeholder">Larghezza</label>
        <input id="width" class="input" name="width" type="number" placeholder="" required/>

        <label for="height" class="placeholder">Altezza</label>
        <input id="height" class="input" name="height" type="number" placeholder="" required/>

        <label for="weight" class="placeholder">Peso (Grammi)</label>
        <input id="weight" class="input" name="weight" type="number" placeholder="" required/>

        <%ShippingPropertyDAO shippingPropertyDAO = new ShippingPropertyDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
          try{
            ArrayList<ShippingProperty> properties = shippingPropertyDAO.retrieveAll();%>

            <div class="propertiesDiv">
              <p>Proprietà di Spedizione</p>

            <%for(int i = 0; i < properties.size(); i++) {
              String currName= properties.get(i).getName();
            %>
            <div class="propertyDetailDiv">
              <input type="checkbox" class="propertyCheckbox" id="<%=currName%>" name="<%=currName%>" value="<%=currName%>">
              <label style="display: inline-block" for="<%=currName%>"><%=currName%></label><br>
            </div>
            <%}%>
            </div>
          <%}catch (NotFoundException e) {

          }catch (Exception e){
            System.out.println(e);
          }%>
        <p>Contatto</p>

        <label for="email" class="placeholder">Email Utente</label>
        <input id="email" class="input" name="email" type="email" placeholder="" required/>

        <button type="button" class="submit" onclick="sendToQuery()">Invia</button>

          <div style="color: transparent;">HiddenDiv</div>

        </div>
      </form>
      </div>
    </div>
  </div>

</div>

</form>


<script>


  <%AssignmentDispatcher assDisp = (AssignmentDispatcher)session.getAttribute("Account");
    String assDispName = null;
  if(assDisp != null)
    assDispName = assDisp.getName();
  %>

  function sendToQuery(){

    var propertiesList = document.getElementsByClassName("propertyCheckbox");

    var checkedProperties = [];

    for(var i = 0; i < propertiesList.length; i++){
      if(propertiesList.item(i).checked){
        checkedProperties.push(propertiesList.item(i).value);
      }
    }

    var state = document.getElementById("state").value;
    var country = document.getElementById("country").value;
    var district = document.getElementById("district").value;
    var zipCode = document.getElementById("zipCode").value;
    var street = document.getElementById("street").value;
    var streetNumber = document.getElementById("streetNumber").value;
    var lenght = document.getElementById("lenght").value;
    var width = document.getElementById("width").value;
    var height = document.getElementById("height").value;
    var weight = document.getElementById("weight").value;
    var email = document.getElementById("email").value;

    $.ajax({
      type: 'POST',
      url: '<%=request.getContextPath()%>/orderPublishingServlet',
      data: {
        'state': state,
        'country': country,
        'district': district,
        'zipCode': zipCode,
        'street': street,
        'streetNumber': streetNumber,
        'lenght': lenght,
        'width': width,
        'height': height,
        'weight': weight,
        'email': email,
        'properties': JSON.stringify(checkedProperties)
      },
      success: function(result) {
          console.log("Id dell'ordine appena inserito : " + result)
          var orderPublished = new OrderPublish(parseInt(result), lenght, width, height, weight, new Date().toISOString(), null, null, null, state, country, district,zipCode, street, streetNumber,checkedProperties)//Continuare la costruzione dell'ordine da passare in formato json
          $.ajax({
            type: 'POST',
            dataType: 'jsonp',
            url: '<%=request.getServletContext().getAttribute("FunctionDomain")%>/PublishOrderToQueue',
            data: {
              'code': '<%=System.getenv("functionDomainCodeAccess")%>',
              'message': JSON.stringify(orderPublished),
              'topicName': "<%=assDispName%>"
            },
            statusCode: {
              200: function() {
                location.href = '<%=request.getContextPath()%>/homepage.jsp?orderPublished=1';
              },
              404: function (){
                functionNegativeResult(); //Configurare questa funzione per fare in modo di allertare l'utente con la comparsa di
              }             //un elemento grafico nel caso in cui il publish dell'ordine sulla coda Azure Queue non è andato bene
            },
          });
        },
        error: function (){
          orderCreationError(); //Configurare questa funzione per fare in modo di allertare l'utente con la comparsa di
        }               //un elemento grafico nel caso in cui il login non è andato a buon fine
      });

    class OrderPublish {
      //private ArrayList<ShippingProperty> properties;


      constructor(id, lenght, width, height, weight, creationDate, assignmentDate,expectedDeliveryDate, trackingCode, state, country, district, zipCode, street, streetNumber, properties) {
          this.id = id
          this.lenght = lenght
          this.width = width
          this.height = height
          this.weight = weight
          this.creationDate = creationDate
          this.assignmentDate = assignmentDate
          this.expectedDeliveryDate = expectedDeliveryDate
          this.trackingCode = trackingCode
          this.state = state
          this.country = country
          this.district = district
          this.zipCode = zipCode
          this.street = street
          this.streetNumber = streetNumber
          this.properties = properties
      }

    }


    function orderCreationError(){

    }

    function functionNegativeResult(){

    }

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
