# Task Manager
Applicazione per la gestione ed il tracciamento di attività in un team di sviluppo, realizzato con **Spring Boot** il backend e con **Angular** il frontend.

---

## Funzionalità

### Gestione Task
- **Workflow Dinamico**: Visualizzazione delle attività suddivise per stato: `Backlog`, `In Progress` e `Completed`.
- **Creazione Task**: Pagina dedicata per l'inserimento di nuovi task.
- **Monitoraggio Ore**: Possibilità di aggiungere ore lavorate con aggiornamento automatico del totale.
- **Cancellazione**: Rimozione sicura dei task.

### Collaborazione
- **Commenti**: Sistema che implementa i commenti per ogni task, con la visualizzazione in ordine cronologico.
- **Assegnamento**: Sistema che permette a ciascun utente di assegnare un task ad altri utenti. 

### Sicurezza
- **AuthGuard**: L'accesso alle pagine senza aver effettuato il login viene bloccato, reindirizzando automaticamente l'utente al Login.
- **Session**: La chiusura del browser e la successiva riapertura richiede nuovamente l'inserimento delle credenziali.

### Testing
- **Postman**: Tutte le API sono state testate mediante la creazione di collection di request postman.
  
---

## Tecnologie

### Backend
- **Java 21** & **Spring Boot 3.5.9**
- **Spring Data JPA**: Per la gestione della persistenza.
- **H2**: Supporto per database relazionali.
- **Lombok**: Riduzione del codice boilerplate.
- **Spring Web**: Creazione di API RESTful e gestione richieste HTTP.

### Frontend
- **Angular 21** & **Node.js 22.14**
- **Bootstrap 5.3.8**: Implementazione interfaccia responsive.

---

## Popolamento DB
Il progetto include un file che popola automaticamente all'avvio il database, affichè si possa testare e collaudare in modo immediato l'applicazione.
