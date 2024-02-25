<h1>ShApp Platform</h1>

<div id = "introduction">
  <p>'ShApp' (Shipping Assignment app) si propone come una piattaforma che ha l'obiettivo di centralizzare la gestione delle dinamiche che si originano dagli acquisti di prodotti on-line.</p>
  
  <p>Più in dettaglio, ShApp offre supporto a tre viste principali, ognuna delle quali rappresenta un suo caso d'uso:</p>

  <ul>
    <li>Assignment Dispatcher</li>
    <li>Corriere</li>
    <li>Utente Finale</li>
  </ul>

<p style:"font-weight= bold">In seguito, vedremo i workFlow associati ad ognuno di essi.</p>

<p>La piattaforma, per poter essere distribuita e offerta al pubblico, sfrutta il meccanismo di hosting ed altri servizi di Azure, allo scopo di migliorare la sua resa su diversi aspetti:</p>

<ul>
  
  <li style="font-size:1.5em""><b>Scalabilità</b></li>
    <p style="padding-left:30px">L'applicazione risultante, deve essere in grado di aumentare o ridurre la capacità delle risorse IT, impiegate per la distribuzione, in base all'aumento o riduzione della domanda, in tempo reale</p>
    <p>Servizi di riferimento: App Service, Function App, MySQL Database, Service Bus</p>
  
  <li style="font-size:1.5em"><b>Disponibilità</b></li>
    <p style="padding-left:30px">In caso di eventuali malfunzionamenti o fallimenti, anche di portata geografica estesa, l'applicazione deve essere in grado di mantenersi comunque operativa</p>
    <p style="padding-left:60px">Più in dettaglio, quest'obiettivo viene raggiunto tramite la <b>ridondanza</b> e i meccanismi di <b>failover</b> offerti da diverse soluzioni in Azure</p>
    <p>Servizi di riferimento: App serivce, MySQL Database, Service Bus</p>

    
  <li style="font-size:1.5em"><b>Bilanciamento del carico</b></li>
    <p style="padding-left:30px">L'applicazione sfrutta la <b>computazione</b> e i <b>meccanismi specializzati per il Cloud</b> di Azure in modo tale da doversi trovare a gestire solo i carichi di lavoro leggeri, senza doversi preoccupare di implementare vari algoritmi di distribuzione del carico di lavoro</p>
    <p>Servizio di riferimento: Service Bus</p>
  
  <li style="font-size:1.5em"><b>Postura di sicurezza di base</b></li>
    <p style="padding-left:30px">L'applicazione sfrutta un Application Gateway, collegato ad una rete virtuale, la quale, grazie all'impostazione di molteplici regole per un Web Application Firewall, rifiuta le richieste in ingresso che sono formattate in modo potenzialmente dannoso o addirittura volontariamente malevolo. Tra i vari inconvenienti scansati abbiamo: SQL Injection, DDOS, Attacchi tramite scripting, ecc...</p>
  
  <li style="font-size:1.5em"><b>Gestione esterna delle identità</b></li>
    <p style="padding-left:30px">L'applicazione non gestisce, in maniera diretta, l'autenticazione degli utenti, bensì fa uso della funzionalità di Social Identity Provider offerta da Google. In questo modo, ShApp, può beneficiare di un servizio di autenticazione sofisticato già pronto, potendosi concentrare ed evolvere sulla sua missione principale</p>
    <p>Servizio di riferimento: Azure AD B2C (con Google come Social Identity Provider)</p>

</ul>
  
  
</div>



<div id = "ShAppArchitecture">
  
  <h1>Architettura Cloud</h1>
  <img  align = "center"  src = "https://github.com/francesco-monzillo/ShApp/blob/main/ShAppArchitectureDiagram.png">


  <h2>WorkFlow</h2>

  <ul>
    <li><h2>Assignment Dispatcher</h2></li>
      <p>Il soggetto interessato, si registra con ruolo di Assignment Dispatcher (facendo l'accesso su Google). A questo punto, viene interpellata la Function App, la quale, tramite la sua identità gestita assegnata dal sistema, accede all'API di gestione del bus di servizio e inserisce un nuovo topic (identificato dal nome dell'Assignment Dispatcher).Successivamente, dopo aver fatto il login può</p>
        <ul>
          <li>rendere noto un contratto stipulato con un corriere</li>
          <li>segnalare l'esistenza di un nuovo ordine ai corrieri con i quali ha stipulato un contratto</li>
        </ul>
        <p>In entrambi i casi, viene interpellata la Function App, con la differenza che, nel primo caso, la Function App interagisce con il Service Bus per creare una nuova sottoscrizione (identificata dal nome del Corriere), la quale sarà associata al topic corrispondente all'Assignment Dispatcher che ha fatto l'accesso. Nel secondo caso, la Function App si occupa di fare il Publish di un messaggio sul topic (identificato dal nome dell'Assignment Dispatcher che ha fatto l'accesso). Tale messaggio è un JSON che verrà tradotto in una struttura dati che contiene tutte le informazioni, di interesse per un corriere, riguardanti l'ordine appena inserito. In attesa di ricevere riscontri, dai corrieri che riceveranno tale messaggio,(tutti coloro che hanno un contratto attivo con l'Assignment Dispatcher in questione), verrà controllata, ogni 5 minuti (periodo scelto a scopo di dimostrazione del funzionamento del sistema), l'esistenza di almeno un corriere interessato alla spedizione dell'ordine appena inserito. In caso positivo, l'ordine verrà assegnato al corriere che condivide più proprietà con l'ordine inserito rispetto al quelle concordate nel contratto, oppure, a parità di proprietà condivise, si sceglie il corriere che ha comunicato prima il suo interesse</p>
<img align = "center" src = "https://github.com/francesco-monzillo/ShApp/blob/main/VisualizeOrderAppScreenShot.png">
    <li><h2>Corriere</h2></li>
      <p>La vista del corriere, prevede due possibili punti d'accesso o di interazione con la piattaforma. Un corriere può</p>
      <ul>
        <li>Fare l'accesso sulla piattaforma Web</li>
        <li>Fare l'accesso come subscriber su uno o più topic sui quali è in ascolto</li>
      </ul>
    <p>Nel primo caso, potrà segnalare degli avanzamenti sugli ordini la cui spedizione è a suo carico; in tal modo gli utenti finali, se loggati sulla piattaforma, riceveranno una mail contenente l'avviso.</p>
    <p>Nel secondo caso, il corriere potrà rimanere in ascolto attivo su uno o più topic per poter ricevere l'avviso su un ordine che deve essere commissionato da un Assignment Dispatcher. A questo punto, usando una qualche logica di business, il corriere deciderà se interessarsi o meno alla possibilità di spedire gli elementi contenuti nell'ordine. In caso positivo, verrà inviata una richiesta all' App Service, il quale, si occuperà di registrare l'interesse proveniente da tale corriere.</p>
    <li><h2>Utente Finale</h2></li>
      <p>L'utente finale può registrarsi con il corrispondente ruolo (facendo l'accesso su Google). Di seguito, sarà in grado di visionare gli aggiornamenti sugli ordini che sono stati comunicati dai rispettivi corrieri responsabili</p>
  </ul>

</div>

<div id = "badges">
  
  <a href="https://shappweb.azurewebsites.net/">
    <img src="https://img.shields.io/badge/ShApp-blue?style=for-the-badge">
  </a>

</div>
