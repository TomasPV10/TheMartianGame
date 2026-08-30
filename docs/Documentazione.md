# Relazione Tecnica e Documentazione di Progetto: The Martian Game

## Indice della Parte 1
1. [Introduzione e Scopo del Progetto](#1-introduzione-e-scopo-del-progetto)
   1. [Contesto Accademico, Motivazioni e Obiettivi Didattici](#11-contesto-accademico-motivazioni-e-obiettivi-didattici)
   2. [Analisi Dettagliata della Trama e dell'Ambientazione Narrativa](#12-analisi-dettagliata-della-trama-e-dellambientazione-narrativa)
   3. [Modellazione dei Personaggi, Ruoli e Layout Topologico della Stazione](#13-modellazione-dei-personaggi-ruoli-e-layout-topologico-della-stazione)
2. [Analisi dei Requisiti e Scelte Architetturali](#2-analisi-dei-requisiti-e-scelte-architetturali)
   1. [Specifiche dei Requisiti Funzionali e Non Funzionali](#21-specifiche-dei-requisiti-funzionali-e-non-funzionali)
   2. [Pattern Architetturali Adottati (MVC, DAO, Observer)](#22-pattern-architetturali-adottati-mvc-dao-observer)
   3. [Diagramma delle Dipendenze e Architettura dei Moduli Maven](#23-diagramma-delle-dipendenze-e-architettura-dei-moduli-maven)
3. [Implementazione del Motore di Gioco e Logica di Dominio](#3-implementazione-del-motore-di-gioco-e-logica-di-dominio)
4. [Diagramma delle Classi e Struttura dei Package](#4-diagramma-delle-classi-e-struttura-dei-package)
5. [Specifica Algebrica delle Strutture Dati](#5-specifica-algebrica-delle-strutture-dati)
   1. [Struttura Dati: Insieme (Set)](#51-struttura-dati-insieme-set)
   2. [Struttura Dati: Lista (List)](#52-struttura-dati-lista-list)
6. [Applicazione Avanzata degli Argomenti del Corso](#6-applicazione-avanzata-degli-argomenti-del-corso)
   1. [Il Parser Lessicale e Sintattico Avanzato](#61-il-parser-lessicale-e-sintattico-avanzato)
   2. [Persistenza dei Dati tramite JDBC e Database H2 Embedded](#62-persistenza-dei-dati-tramite-jdbc-e-database-h2-embedded)
   3. [Gestione della Concorrenza e dei Timer asincroni](#63-gestione-della-concorrenza-e-dei-timer-asincroni)
   4. [Utilizzo di Lambda Expressions e Stream API](#64-utilizzo-di-lambda-expressions-e-stream-api)
   5. [Gestione dei File e dei Flussi I/O Multimediali](#65-gestione-dei-file-e-dei-flussi-io-multimediali)
   6. [GUI Personalizzata in Java Swing e Animazioni Custom](#66-gui-personalizzata-in-java-swing-e-animazioni-custom)

---

## 1. Introduzione e Scopo del Progetto

### 1.1 Contesto Accademico, Motivazioni e Obiettivi Didattici
**The Martian Game** si configura come un elaborato software complesso, ideato, progettato e sviluppato interamente come prova pratica di verifica per il superamento dell'esame del corso universitario di *Metodi Avanzati di Programmazione*[cite: 4]. L'intento principale del lavoro non risiede unicamente nella realizzazione di un'applicazione ludica funzionante, bensì nell'applicazione rigorosa, sistematica e avanzata dei paradigmi di programmazione orientata agli oggetti (OOP), dei principi di progettazione software pulita (*Clean Code*) e dei pattern architetturali più consolidati all'interno dell'ecosistema di sviluppo **Java**[cite: 4].

Il progetto integra molteplici aspetti avanzati della tecnologia Java, tra cui[cite: 4]:
* La progettazione di interfacce utente grafiche reattive e personalizzate mediante il framework **Java Swing**[cite: 4].
* L'elaborazione e l'analisi sintattica del linguaggio naturale tramite un modulo *Parser* customizzato[cite: 4].
* La gestione della persistenza dei dati strutturati mediante database relazionali in-memory (`H2`) interfacciati tramite driver **JDBC** (Java DataBase Connectivity)[cite: 4].
* La gestione della concorrenza attraverso l'uso di thread multipli asincroni per il controllo dei timer di gioco e dei flussi multimediali[cite: 4].
* L'utilizzo di strutture dati formali con relative specifiche algebriche, ed espressioni funzionali basate su *Lambda Expressions* e *Stream API*[cite: 4].

---

### 1.2 Analisi Dettagliata della Trama e dell'Ambientazione Narrativa
La narrazione è ambientata in un futuro prossimo, precisamente nell'anno 2046, sullo sfondo ostile e inospitale del pianeta Marte[cite: 4]. Il giocatore veste i panni di un ingegnere aerospaziale di turno all'interno della base di ricerca scientifica avanzata denominata *Ares Station*[cite: 4]. La quiete operativa della missione viene bruscamente interrotta da un evento catastrofico imprevisto: a causa di un cedimento strutturale dei condotti o di un sovraccarico critico nei reattori di fusione principali, i sistemi di contenimento energetico subiscono un arresto anomalo, innescando una progressiva e pericolosa decompressione dei compartimenti stagni della stazione[cite: 4].

Il protagonista si risveglia disorientato all'interno del modulo abitativo principale, con l'allarme generale della base attivo e i monitor di sistema che segnalano una perdita costante e irreversibile delle riserve d'ossigeno[cite: 4]. La dinamica di gioco impone una gestione rigorosa del tempo e delle risorse: l'utente deve esplorare metodicamente i diversi ambienti della stazione, interagire con i terminali informatici danneggiati, raccogliere strumenti utili (come chiavi inglesi, schede di accesso e kit di saldatura) e risolvere enigmi logici per ripristinare il flusso energetico e attivare la procedura di evacuazione d'emergenza prima che il livello di ossigeno si azzeri completamente, determinando la fine della partita (*Game Over*)[cite: 4].

---

### 1.3 Modellazione dei Personaggi, Ruoli e Layout Topologico della Stazione
L'architettura del mondo di gioco prevede entità distinte con ruoli e responsabilità ben definiti[cite: 4]:

* **Il Sopravvissuto (Player)**: L'attore principale controllato dall'utente[cite: 4]. Possiede attributi dinamici quali lo stato di salute, una barra dell'ossigeno decrescente nel tempo, un inventario a capacità limitata e metodi per lo spostamento e l'interazione con gli oggetti[cite: 4].
* **Ares AI**: L'intelligenza artificiale di bordo della stazione[cite: 4]. Funge da entità narrante e sistema di feedback testuale, inviando notifiche sullo stato dei sistemi, messaggi d'errore in caso di comandi non validi o suggerimenti contestuali per guidare il giocatore[cite: 4].

#### Topologia dei Moduli della Stazione (*Ares Station*)[cite: 4]
Il mondo di gioco è strutturato come un grafo orientato di stanze collegate tra loro da direzioni cardinali (Nord, Sud, Est, Ovest)[cite: 4]:
1. **Modulo Abitativo / Airlock**: La zona di risveglio del giocatore, caratterizzata dalla presenza della porta d'uscita verso la superficie marziana e collegata direttamente al corridoio centrale[cite: 4].
2. **Substation (Sala Reattori)**: Il cuore energetico della base, attualmente disattivato[cite: 4]. Richiede l'utilizzo di componenti di ricambio per essere riavviato[cite: 4].
3. **Laboratorio & Serra**: Moduli scientifici adibiti alla ricerca botanica e chimica, nei quali è possibile rinvenire composti utili e chiavi magnetiche[cite: 4].
4. **Centro Comunicazioni & Sicurezza**: La sala di controllo protetta da protocolli di cifratura avanzati e terminali con inserimento di PIN numerici[cite: 4].
5. **Deposito Risorse (Supply Depot)**: Area di stoccaggio pesante in cui sono custoditi attrezzi di alta precisione e kit di emergenza per la sopravvivenza[cite: 4].

---

## 2. Analisi dei Requisiti e Scelte Architetturali

### 2.1 Specifiche dei Requisiti Funzionali e Non Funzionali del Sistema
* **Requisiti Funzionali**[cite: 4]:
  * L'applicazione deve permettere l'inserimento di comandi testuali in linguaggio naturale tramite una console grafica interattiva[cite: 4].
  * Il sistema deve analizzare i comandi, identificare verbi e oggetti, e restituire un feedback immediato testuale e visivo[cite: 4].
  * Il motore di gioco deve aggiornare in tempo reale la posizione del giocatore, lo stato dei reattori e la quantità di ossigeno residuo[cite: 4].
  * La GUI deve aggiornare la mappa topologica in tempo reale e gestire la visualizzazione dell'inventario[cite: 4].

* **Requisiti Non Funzionali**[cite: 4]:
  * **Modularità e Manutenibilità**: Il codice deve essere suddiviso in package logici indipendenti per facilitare il testing e la manutenzione[cite: 4].
  * **Portabilità**: L'intero software deve girare su qualsiasi sistema operativo dotato di Java SE Runtime Environment senza dipendenze esterne native complesse[cite: 4].
  * **Reattività**: I calcoli in background (timer e audio) non devono causare blocchi o rallentamenti dell'interfaccia grafica (Event Dispatch Thread)[cite: 4].

---

### 2.2 Design Pattern Applicati (MVC, DAO, Observer)
Al fine di garantire standard ingegnereschi elevati, il progetto adotta tre pattern fondamentali[cite: 4]:
1. **Model-View-Controller (MVC)**[cite: 4]:
   * *Model*: Comprende le classi di dominio (`Room`, `GameObject`, `Player`) e la logica di stato[cite: 4].
   * *View*: Costituita dai componenti Java Swing (`MartianGameFrame`, `MapPanel`)[cite: 4].
   * *Controller*: Gestisce il flusso degli eventi, riceve l'input dal parser e modifica il modello di conseguenza[cite: 4].
2. **Data Access Object (DAO)**[cite: 4]:
   * Isola la logica di accesso al database relazionale H2, mascherando le query SQL e fornendo metodi orientati agli oggetti (`RoomDAO`, `ObjectDAO`) per il recupero delle entità[cite: 4].
3. **Observer Pattern**[cite: 4]:
   * Sfruttato per disaccoppiare il motore di gioco dai pannelli di visualizzazione grafica[cite: 4]. Quando lo stato del giocatore o l'ossigeno variano, gli osservatori registrati aggiornano automaticamente i widget grafici senza polling continuo[cite: 4].

---

### 2.3 Diagramma delle Dipendenze e Architettura dei Moduli Maven
Il progetto è configurato come un pacchetto Maven standard, sfruttando il file `pom.xml` per la gestione centralizzata delle dipendenze esterne (driver JDBC H2, librerie di testing JUnit e dipendenze per la gestione audio)[cite: 4]. La struttura delle cartelle rispecchia fedelmente la suddivisione in package Java standard, garantendo l'assenza di dipendenze circolari tra i moduli di presentazione e quelli di persistenza[cite: 4].

# Relazione Tecnica e Documentazione di Progetto: The Martian Game (Parte 2)

## Indice della Parte 2


---

## 3. Implementazione del Motore di Gioco e Logica di Dominio
Il motore di gioco (*Game Engine*) coordina e governa l'intero ciclo di vita della sessione interattiva[cite: 4]. All'avvio dell'applicazione, il sistema esegue una sequenza di inizializzazione che prevede[cite: 4]:
1. La connessione al database relazionale **H2** in-memory tramite driver JDBC[cite: 4].
2. Il caricamento dei dati strutturali relativi alle stanze, alle loro adiacenze e agli oggetti posizionati all'interno della base marziana[cite: 4].
3. L'istanza del componente grafico principale e l'avvio dei thread di sottofondo dedicati alla gestione della colonna sonora, al timer di consumo dell'ossigeno e al sistema di particelle grafiche[cite: 4].

Durante il normale svolgimento della partita, il giocatore interagisce inserendo comandi testuali nell'apposita casella di input della GUI[cite: 4]. Ciascuna stringa viene intercettata dal controller di gioco e inoltrata al modulo `Parser`[cite: 4]. Una volta ottenuto l'oggetto strutturato contenente l'azione e l'eventuale complemento (oggetto o direzione), il motore verifica la validità dell'azione in base allo stato corrente della stanza[cite: 4]. Se l'azione è lecita (ad esempio, il movimento verso una direzione aperta o la raccolta di un oggetto con flag `pickupable` impostato a `true`), lo stato del mondo viene aggiornato e l'evento viene notificato ai componenti visivi registrati tramite il pattern Observer[cite: 4].

---

## 4. Diagramma delle Classi e Struttura dei Package
L'architettura del software è rigorosamente organizzata in package logici distinti per separare il motore di gioco dalla sua implementazione specifica:

1. **`TheMartianGame.GraphicUserInterface`**: Comprende la finestra principale `MartianGameFrame` (gestita tramite `CardLayout`), il pannello topologico vettoriale `MapPanel` e le logiche di rendering per le animazioni, i controlli del volume e i popup di fine partita.
2. **`TheMartianGame`**: Contiene la classe principale del dominio applicativo (`MartianGame`), che gestisce la topologia della mappa, le regole di sblocco e lo stato vitale del giocatore.
3. **`Game.Type`**: Raggruppa le entità fondamentali del framework di base, tra cui le classi astratte per `Room`, `GameObject` e le definizioni dei comandi.
4. **`Game.Parser`**: Raggruppa le classi deputate all'analisi lessicale e sintattica dell'input testuale inserito dall'utente[cite: 4].

---

## 5. Specifica Algebrica delle Strutture Dati
All'interno della progettazione formale del sistema, le strutture dati fondamentali sono state definite mediante specifiche algebriche astratte per garantirne la correttezza logica[cite: 4].

### 5.1 Struttura Dati: Insieme (Set)
Collezione non ordinata di elementi priva di duplicati, impiegata principalmente per la memorizzazione e la verifica rapida delle parole da ignorare (*stopwords*)[cite: 4].

* **Specifica Sintattica**[cite: 4]:
  * `newSet()` $\rightarrow$ `Insieme`
  * `isEmpty(Insieme)` $\rightarrow$ `Boolean`
  * `add(Insieme, Elemento)` $\rightarrow$ `Insieme`
  * `remove(Insieme, Elemento)` $\rightarrow$ `Insieme`
  * `contains(Insieme, Elemento)` $\rightarrow$ `Boolean`
  * `getSize(Insieme)` $\rightarrow$ `Integer`

* **Specifica Semantica**[cite: 4]:
  * `isEmpty(newSet()) = true`[cite: 4]
  * `isEmpty(add(s, e)) = false`[cite: 4]
  * `contains(newSet(), e) = false`[cite: 4]
  * `contains(add(s, e), e') = if (e' == e) then true else contains(s, e')`[cite: 4]
  * `getSize(newSet()) = 0`[cite: 4]
  * `getSize(add(s, e)) = if contains(s, e) then getSize(s) else getSize(s) + 1`[cite: 4]

---

### 5.2 Struttura Dati: Lista (List)
Collezione ordinata e indicizzata utilizzata per la gestione sequenziale dell'inventario del giocatore e degli oggetti presenti nelle stanze[cite: 4].

* **Specifica Sintattica**[cite: 4]:
  * `newList()` $\rightarrow$ `Lista`
  * `isEmpty(Lista)` $\rightarrow$ `Boolean`
  * `add(Lista, Elemento, Integer)` $\rightarrow$ `Lista`
  * `remove(Lista, Integer)` $\rightarrow$ `Lista`
  * `getItem(Lista, Integer)` $\rightarrow$ `Elemento`
  * `getSize(Lista)` $\rightarrow$ `Integer`

* **Specifica Semantica**[cite: 4]:
  * `isEmpty(newList()) = true`[cite: 4]
  * `isEmpty(add(l, e, i)) = false`[cite: 4]
  * `getSize(newList()) = 0`[cite: 4]
  * `getSize(add(l, e, i)) = getSize(l) + 1`[cite: 4]
  * `getItem(newList(), i) = error`[cite: 4]
  * `getItem(add(l, e, i), i') = if (i' == i) then e else getItem(l, i')`[cite: 4]

---

## 6. Applicazione Avanzata degli Argomenti del Corso

### 6.1 Il Parser Lessicale e Sintattico Avanzato
Il modulo `Parser` rappresenta l'interfaccia linguistica del sistema[cite: 4]. Quando l'utente inserisce una frase, il parser esegue le seguenti operazioni sequenziali[cite: 4]:
1. **Normalizzazione**: Conversione di tutti i caratteri in minuscolo e rimozione della punteggiatura superflua[cite: 4].
2. **Tokenizzazione**: Suddivisione della stringa in un array di singoli token testuali[cite: 4].
3. **Filtraggio delle Stopwords**: Rimozione di articoli, preposizioni e congiunzioni non rilevanti mediante un confronto con un set precaricato in memoria (`HashSet<String>`)[cite: 4].
4. **Riconoscimento dei Comandi**: Corrispondenza dei token rimasti con i verbi riconosciuti dal sistema (`CommandType`) e ricerca dei sostantivi corrispondenti agli oggetti presenti nella stanza corrente o nell'inventario del giocatore, restituendo infine un oggetto strutturato `ParserOutput`[cite: 4].

### 6.2 Persistenza dei Dati tramite JDBC e Database H2 Embedded
L'applicazione sfrutta un database relazionale **H2 embedded** per la gestione e l'inizializzazione del mondo di gioco[cite: 4]. I vantaggi di questa scelta includono l'assenza di configurazioni di server esterni e la rapidità di esecuzione in-memory[cite: 4]. All'avvio, il sistema esegue script SQL strutturati per la creazione delle tabelle relazionali[cite: 4]:

* **Tabella `Rooms`**: Memorizza la topologia della base spaziale (`id`, `name`, `description`, e gli identificativi delle stanze adiacenti `north`, `south`, `east`, `west`)[cite: 4].
* **Tabella `Objects`**: Contiene i dati relativi agli elementi interattivi (`id`, `name`, `description`, il flag booleano `pickupable` e il riferimento `location_id` alla stanza o all'inventario)[cite: 4].

L'accesso ai dati è incapsulato attraverso l'utilizzo del pattern DAO, garantendo un codice pulito e privo di query SQL sparse nella logica di business[cite: 4].

### 6.3 Gestione della Concorrenza e dei Timer asincroni
Per assicurare un'esperienza fluida e non bloccare il thread principale dell'interfaccia grafica (Event Dispatch Thread - EDT), l'architettura fa uso combinato di Timer e Thread asincroni:
* **`javax.swing.Timer`**: Utilizzati in modo estensivo per le operazioni che richiedono l'aggiornamento sicuro dei componenti visivi. Gestiscono l'animazione delle particelle nel menu, il lampeggio d'emergenza della barra dell'ossigeno e l'effetto "macchina da scrivere" (*Typewriter*) per il rendering graduale dei testi.
* **Thread Indipendenti**: Le operazioni di I/O pesanti (come il caricamento e la riproduzione in loop delle tracce audio `Clip`) e gli effetti di distorsione visiva (come lo *Screen Shake* durante i danni all'Hub) sono incapsulati all'interno di Thread dedicati che operano in background.

### 6.4 Utilizzo di Lambda Expressions e Stream API
L'adozione delle Stream API e delle espressioni Lambda introdotte a partire da Java 8 permette di gestire le collezioni di dati in modo dichiarativo, conciso ed estremamente efficiente[cite: 4]. Ad esempio, la ricerca di un oggetto specifico all'interno dell'inventario del giocatore viene implementata nel modo seguente[cite: 4]:

```java
public GameObject findObjectInInventory(String targetName) {
    return inventory.stream()
            .filter(item -> item.getName().equalsIgnoreCase(targetName))
            .findFirst()
            .orElse(null);
}