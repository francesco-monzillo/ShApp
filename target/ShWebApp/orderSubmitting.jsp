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


        <p class="errors" id="streetNumberError"></p>
        <label for="streetNumber" class="placeholder">N°</label>
        <input id="streetNumber" class="input" name="streetNumber" type="number" placeholder="" required/>

        <p>Dimensioni</p>

        <p class="errors" id="lenghtError"></p>
        <label for="lenght" class="placeholder">Lunghezza</label>
        <input id="lenght" class="input" name="lenght" type="number" placeholder="" required/>


        <p class="errors" id="widthError"></p>
        <label for="width" class="placeholder">Larghezza</label>
        <input id="width" class="input" name="width" type="number" placeholder="" required/>


        <p class="errors" id="heightError"></p>
        <label for="height" class="placeholder">Altezza</label>
        <input id="height" class="input" name="height" type="number" placeholder="" required/>


        <p class="errors" id="weightError"></p>
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


        <p class="errors" id="emailError"></p>
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

  function checkEventualErrors(){

    var booleano = true;

    var state = document.getElementById("state").value;
    var country = document.getElementById("country").value;
    var district = document.getElementById("district").value;
    var zipCode = document.getElementById("zipCode").value;
    var street = document.getElementById("street").value;
    var streetnumber = document.getElementById("streetNumber").value;
    var lenght = document.getElementById("lenght").value;
    var width = document.getElementById("width").value;
    var height = document.getElementById("height").value;
    var weight = document.getElementById("weight").value;
    var email = document.getElementById("email").value;

    var containsNumbers = /\d/;
    var containsLetters = /[a-zA-Z]/;
    var isAnEmail = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

    if(state.trim().length === 0 || containsNumbers.test(state)){
      document.querySelector("#stateError").innerHTML = "Stato non valido: può contenere solo lettere";
      document.querySelector("#stateError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#stateError").innerHTML = "";
    }

    if(country.trim().length === 0 || containsNumbers.test(country)){
      document.querySelector("#countryError").innerHTML = "Paese non valido: può contenere solo lettere";
      document.querySelector("#countryError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#countryError").innerHTML = "";
    }

    if(district.trim().length === 0 || containsNumbers.test(district)){
      document.querySelector("#districtError").innerHTML = "Provincia non valida: può contenere solo lettere";
      document.querySelector("#districtError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#districtError").innerHTML = "";
    }


    if(zipCode.trim().length === 0 || containsLetters.test(zipCode)){
      document.querySelector("#zipCodeError").innerHTML = "CAP non valido: può contenere solo cifre";
      document.querySelector("#zipCodeError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#zipCodeError").innerHTML = "";
    }

    if(street.trim().length === 0 || containsNumbers.test(street)){
      document.querySelector("#streetError").innerHTML = "Via non valida: può contenere solo lettere";
      document.querySelector("#streetError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#streetError").innerHTML = "";
    }

    if(streetnumber.trim().length === 0 || containsLetters.test(streetnumber)){
      document.querySelector("#streetNumberError").innerHTML = "Numero di via non valido: può contenere solo cifre";
      document.querySelector("#streetNumberError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#streetNumberError").innerHTML = "";
    }

    if(lenght.trim().length === 0 || containsLetters.test(lenght)){
      document.querySelector("#lenghtError").innerHTML = "Lunghezza non valida: può contenere solo cifre";
      document.querySelector("#lenghtError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#lenghtError").innerHTML = "";
    }


    if(width.trim().length === 0 || containsLetters.test(width)){
      document.querySelector("#widthError").innerHTML = "Larghezza non valida: può contenere solo cifre";
      document.querySelector("#widthError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#widthError").innerHTML = "";
    }


    if(height.trim().length === 0 || containsLetters.test(height)){
      document.querySelector("#heightError").innerHTML = "Altezza non valida: può contenere solo cifre";
      document.querySelector("#heightError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#heightError").innerHTML = "";
    }

    if(weight.trim().length === 0 || containsLetters.test(weight)){
      document.querySelector("#weightError").innerHTML = "Peso non valido: può contenere solo cifre";
      document.querySelector("#weightError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#weightError").innerHTML = "";
    }


    if(email.trim().length === 0 || !isAnEmail.test(email)){
      document.querySelector("#emailError").innerHTML = "Email non valida";
      document.querySelector("#emailError").style.display = "block";
      booleano = false;
    }else{
      document.querySelector("#emailError").innerHTML = "";
    }


    return booleano;

  }

  function sendToQuery(){

    if(! checkEventualErrors() ){
      return;
    }

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
              200: function () {
                console.log("Order Published on topic")
                $.ajax({
                  type: 'POST',
                  dataType: 'text',
                  url: '<%=request.getContextPath()%>/verifyUpdateableUserServlet',
                  data: {
                    'email': email
                  },
                  success: function (risultato) {
                    $.ajax({
                      type: 'POST',
                      dataType: 'jsonp',
                      url: '<%=request.getServletContext().getAttribute("FunctionDomain")%>/SendUpdateToUser',
                      data: {
                        'code': '<%=System.getenv("functionDomainCodeAccess")%>',
                        'receiverAddress': email,
                        'subject': "ShApp: Inserimento Ordine",
                        'message': "Ciao " + risultato + "\nL'ordine da te commissionato presso <%=((AssignmentDispatcher) session.getAttribute("Account")).getName()%> è stato preso in carico sulla piattaforma ShApp, puoi visualizzarlo facendo il login sull'app"
                      },
                      statusCode: {
                        200: function () {
                          location.href = '<%=request.getContextPath()%>/homepage.jsp?orderPublished=1';
                        },
                        404: function () {
                          location.href = '<%=request.getContextPath()%>/homepage.jsp?orderPublished=1&errorMail1=true';
                        },
                      }
                    });
                  },
                  error: function () {
                    location.href = '<%=request.getContextPath()%>/homepage.jsp?orderPublished=1&errorMail2=true';
                  },
                });
              },
              404: function () {
                functionNegativeResult(); //Configurare questa funzione per fare in modo di allertare l'utente con la comparsa di
              }
            }//un elemento grafico nel caso in cui il publish dell'ordine sulla coda Azure Queue non è andato bene
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


</script>

</body>
</html>
