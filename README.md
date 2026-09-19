# Shipping Assignment Application (ShApp)

ShApp is a cloud-based web application designed to centralize and streamline the post-purchase logistics and order dispatch dynamics that arise from online shopping. 

The application establishes a protocol where couriers are integrated as highly available resources. E-commerce platforms or dispatchers can assign shipments to them based on pre-negotiated contracts and dynamically calculated matching properties.

---

## Table of Contents
- [Core Objectives](#core-objectives)
- [System Roles & Features](#system-roles--features)
- [Cloud Architecture & Services](#cloud-architecture--services)
- [Key Workflows](#key-workflows)
  - [1. Assignment Dispatcher Workflow](#1-assignment-dispatcher-workflow)
  - [2. Courier Workflow](#2-courier-workflow)
  - [3. End User Workflow](#3-end-user-workflow)

---

## Core Objectives <a name="core-objectives"></a>

By leveraging cloud infrastructure, ShApp addresses critical operational needs:

*   **Scalability**: Automatically adjusts resources in real time to match fluctuating demand (utilizing App Service, Function App, MySQL Database, and Service Bus).
*   **High Availability**: Ensures continuous operations through redundancy and failover mechanisms (utilizing App Service, MySQL Database, Service Bus).
*   **Load Balancing**: Offloads heavy processing and manages task distribution using messaging queues.
*   **Robust Security**: Employs an Azure Application Gateway with a Web Application Firewall (WAF) to mitigate risks like SQL Injection, DDoS, and cross-site scripting.
*   **Identity Management**: Outsources authentication to a trusted external system, integrating Azure AD B2C with Google as a Social Identity Provider.

---

## System Roles & Features <a name="system-roles--features"></a>

ShApp features tailored web interfaces and permissions for three distinct user roles:

### 1. Assignment Dispatcher
*   **Order Creation**: Input new delivery requests with specific details (dimensions, weight, destination, etc.).
*   **Contract Management**: Define and register delivery contracts with affiliated couriers, outlining agreed-upon service-level properties (e.g., international shipping support, fragile handling, weight limits, delivery timelines).
*   **Order Tracking**: View and manage both active and historical orders.

### 2. Courier
*   **Express Interest**: Listen to dispatcher order boards and submit interest in fulfilling pending orders.
*   **Update Shipments**: Provide progress updates (e.g., "In transit", "Delivered") on actively assigned shipments.
*   **History**: Monitor past and active delivery assignments.

### 3. End User (Customer)
*   **Status Tracking**: View real-time active and past order history.
*   **Email Notifications**: Receive automated email notifications immediately whenever the courier updates the shipment status.

---

## Cloud Architecture & Services <a name="cloud-architecture--services"></a>

The application relies on a modern, event-driven Azure architecture:

<img  align = "center"  src = "https://github.com/francesco-monzillo/ShApp/blob/web_app/ShappArc2.png">


*   **Azure App Service**: Hosts the core web application, facilitating balanced hosting and server-side workflow orchestration.
*   **Azure Service Bus**: Acts as a fully managed message broker. It handles decoupling, load balancing, and reliable Pub/Sub messaging via *Topics* and *Subscriptions*.
*   **Azure Function App**: An event-driven serverless component executing backend logic. Key functions include:
    *   `addTopic`: Creates a dedicated Service Bus topic when a new dispatcher registers.
    *   `contractStipulatedWithCourier`: Adds a courier subscription to the dispatcher's topic.
    *   `publishOrderToQueue`: Publishes order details to a topic.
    *   `sendUpdateToUser`: Triggers transactional updates via email.
*   **Azure AD B2C**: Provides identity management, relying on Google as the federated Social Identity Provider.
*   **Azure Application Gateway & WAF**: Secures the virtual network entry point against malicious traffic.

---

## Key Workflows <a name="key-workflows"></a>

### 1. Assignment Dispatcher Workflow <a name="1-assignment-dispatcher-workflow"></a>
1.  **Registration/Login**: The dispatcher logs in via Google Auth.
2.  **Topic Provisioning**: An Azure Function automatically registers a dedicated Service Bus topic for that dispatcher.
3.  **Contract Stipulation**: When a contract is established with a courier, an Azure Function creates a subscription for that courier on the dispatcher's topic.
4.  **Order Placement**: The dispatcher creates an order, which triggers the Function App to publish a message containing order parameters to the topic.
5.  **Assignment Algorithm**: A background thread (`CheckInterestThread`) periodically checks for couriers expressing interest:
    *   The order is assigned to the courier sharing the highest number of matching properties compared to their registered contract.
    *   In the event of a tie, the system selects the courier who submitted their interest first.

### 2. Courier Workflow <a name="2-courier-workflow"></a>
*   **Active Listening**: The courier remains connected as a subscriber to one or more dispatcher topics, receiving real-time notifications about newly created orders.
*   **Interest Registration**: Utilizing internal business logic, the courier chooses whether to apply. If they apply, a request is sent to the App Service to log their interest.
*   **Shipment Updates**: Once assigned, the courier uses the web portal to input state updates, which are immediately processed to update the database and notify the customer.

### 3. End User Workflow <a name="3-end-user-workflow"></a>
*   **Access**: The user authenticates using Google.
*   **Monitoring**: The user checks shipment timelines on their dashboard or receives real-time progress updates directly in their email inbox.

<div id = "badges">
  
  <a href="https://shappweb.azurewebsites.net/">
    <img src="https://img.shields.io/badge/ShApp-blue?style=for-the-badge">
  </a>

</div>
