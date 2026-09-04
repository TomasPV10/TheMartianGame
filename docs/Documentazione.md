# Relazione Tecnica e Documentazione di Progetto: The Martian Game

## Indice
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
**The Martian Game** si configura come un elaborato software complesso, ideato, progettato e sviluppato interamente come prova pratica di verifica per il superamento dell'esame del corso universitario di *Metodi Avanzati di Programmazione*. L'intento principale del lavoro non risiede unicamente nella realizzazione di un'applicazione ludica funzionante, bensì nell'applicazione rigorosa, sistematica e avanzata dei paradigmi di programmazione orientata agli oggetti (OOP), dei principi di progettazione software pulita (*Clean Code*) e dei pattern architetturali più consolidati all'interno dell'ecosistema di sviluppo **Java**.

Il progetto integra molteplici aspetti avanzati della tecnologia Java, tra cui:
* La progettazione di interfacce utente grafiche reattive e personalizzate mediante il framework **Java Swing**.
* L'elaborazione e l'analisi sintattica del linguaggio naturale tramite un modulo *Parser* customizzato.
* La gestione della persistenza dei dati strutturati mediante database relazionali in-memory (`H2`) interfacciati tramite driver **JDBC** (Java DataBase Connectivity).
* La gestione della concorrenza attraverso l'uso di thread multipli asincroni per il controllo dei timer di gioco e dei flussi multimediali.
* L'utilizzo di strutture dati formali con relative specifiche algebriche, ed espressioni funzionali basate su *Lambda Expressions* e *Stream API*.

---

### 1.2 Analisi Dettagliata della Trama e dell'Ambientazione Narrativa
Anno 2046: a causa di un guasto ai reattori della base marziana *Ares Station*, i sistemi vitali si disattivano innescando una decompressione. Il giocatore si risveglia nel modulo abitativo e deve gestire le riserve d'ossigeno decrescenti, esplorando la stazione e risolvendo enigmi per ripristinare l'energia ed evacuare prima dell'esaurimento dell'aria.

---

### 1.3 Modellazione dei Personaggi, Ruoli e Layout Topologico della Stazione
L'architettura del mondo di gioco prevede entità distinte con ruoli e responsabilità ben definiti:

* **Il Sopravvissuto (Player)**: L'attore principale controllato dall'utente. Possiede attributi dinamici quali lo stato di salute, una barra dell'ossigeno decrescente nel tempo, un inventario a capacità limitata e metodi per lo spostamento e l'interazione con gli oggetti.
* **Ares AI**: L'intelligenza artificiale di bordo della stazione. Funge da entità narrante e sistema di feedback testuale, inviando notifiche sullo stato dei sistemi, messaggi d'errore in caso di comandi non validi o suggerimenti contestuali per guidare il giocatore.

#### Topologia dei Moduli della Stazione (*Ares Station*)
Il mondo di gioco è strutturato come un grafo orientato di stanze collegate tra loro da direzioni cardinali (Nord, Sud, Est, Ovest):
1. **Modulo Abitativo / Airlock**: La zona di risveglio del giocatore, caratterizzata dalla presenza della porta d'uscita verso la superficie marziana e collegata direttamente al corridoio centrale.
2. **Substation (Sala Reattori)**: Il cuore energetico della base, attualmente disattivato. Richiede l'utilizzo di componenti di ricambio per essere riavviato.
3. **Laboratorio & Serra**: Moduli scientifici adibiti alla ricerca botanica e chimica, nei quali è possibile rinvenire composti utili e chiavi magnetiche.
4. **Centro Comunicazioni & Sicurezza**: La sala di controllo protetta da protocolli di cifratura avanzati e terminali con inserimento di PIN numerici.
5. **Deposito Risorse (Supply Depot)**: Area di stoccaggio pesante in cui sono custoditi attrezzi di alta precisione e kit di emergenza per la sopravvivenza.

---

## 2. Analisi dei Requisiti e Scelte Architetturali

### 2.1 Specifiche dei Requisiti Funzionali e Non Funzionali del Sistema
* **Requisiti Funzionali**:
  * L'applicazione deve permettere l'inserimento di comandi testuali in linguaggio naturale tramite una console grafica interattiva.
  * Il sistema deve analizzare i comandi, identificare verbi e oggetti, e restituire un feedback immediato testuale e visivo.
  * Il motore di gioco deve aggiornare in tempo reale la posizione del giocatore, lo stato dei reattori e la quantità di ossigeno residuo.
  * La GUI deve aggiornare la mappa topologica in tempo reale e gestire la visualizzazione dell'inventario.

* **Requisiti Non Funzionali**:
  * **Modularità e Manutenibilità**: Il codice deve essere suddiviso in package logici indipendenti per facilitare il testing e la manutenzione.
  * **Portabilità**: L'intero software deve girare su qualsiasi sistema operativo dotato di Java SE Runtime Environment senza dipendenze esterne native complesse.
  * **Reattività**: I calcoli in background (timer e audio) non devono causare blocchi o rallentamenti dell'interfaccia grafica (Event Dispatch Thread).

---

### 2.2 Pattern Architetturali Adottati (MVC, DAO, Observer)
Al fine di garantire standard ingegnereschi elevati, il progetto adotta tre pattern fondamentali:
1. **Model-View-Controller (MVC)**:
   * *Model*: Comprende le classi di dominio (`Room`, `GameObject`, `Player`) e la logica di stato.
   * *View*: Costituita dai componenti Java Swing (`MartianGameFrame`, `MapPanel`).
   * *Controller*: Gestisce il flusso degli eventi, riceve l'input dal parser e modifica il modello di conseguenza.
2. **Data Access Object (DAO)**:
   * Isola la logica di accesso al database relazionale H2, mascherando le query SQL e fornendo metodi orientati agli oggetti (`RoomDAO`, `ObjectDAO`) per il recupero delle entità.
3. **Observer Pattern**:
   * Sfruttato per disaccoppiare il motore di gioco dai pannelli di visualizzazione grafica. Quando lo stato del giocatore o l'ossigeno variano, gli osservatori registrati aggiornano automaticamente i widget grafici senza polling continuo.

---

### 2.3 Diagramma delle Dipendenze e Architettura dei Moduli Maven
Il progetto è configurato come un pacchetto Maven standard, sfruttando il file `pom.xml` per la gestione centralizzata delle dipendenze esterne (driver JDBC H2, librerie di testing JUnit e dipendenze per la gestione audio). La struttura delle cartelle rispecchia fedelmente la suddivisione in package Java standard, garantendo l'assenza di dipendenze circolari tra i moduli di presentazione e quelli di persistenza.

---

## 3. Implementazione del Motore di Gioco e Logica di Dominio
Il motore di gioco (*Game Engine*) coordina e governa l'intero ciclo di vita della sessione interattiva. All'avvio dell'applicazione, il sistema esegue una sequenza di inizializzazione che prevede:
1. La connessione al database relazionale **H2** in-memory tramite driver JDBC.
2. Il caricamento dei dati strutturali relativi alle stanze, alle loro adiacenze e agli oggetti posizionati all'interno della base marziana.
3. L'istanza del componente grafico principale e l'avvio dei thread di sottofondo dedicati alla gestione della colonna sonora, al timer di consumo dell'ossigeno e al sistema di particelle grafiche.

Durante il normale svolgimento della partita, il giocatore interagisce inserendo comandi testuali nell'apposita casella di input della GUI. Ciascuna stringa viene intercettata dal controller di gioco e inoltrata al modulo `Parser`. Una volta ottenuto l'oggetto strutturato contenente l'azione e l'eventuale complemento (oggetto o direzione), il motore verifica la validità dell'azione in base allo stato corrente della stanza. Se l'azione è lecita (ad esempio, il movimento verso una direzione aperta o la raccolta di un oggetto con flag `pickupable` impostato a `true`), lo stato del mondo viene aggiornato e l'evento viene notificato ai componenti visivi registrati tramite il pattern Observer.

---

## 4. Diagramma delle Classi e Struttura dei Package
L'architettura del software è rigorosamente organizzata in package logici distinti per separare il motore di gioco dalla sua implementazione specifica:

1. **`TheMartianGame.GraphicUserInterface`**: Comprende la finestra principale `MartianGameFrame` (gestita tramite `CardLayout`), il pannello topologico vettoriale `MapPanel` e le logiche di rendering per le animazioni, i controlli del volume e i popup di fine partita.
2. **`TheMartianGame`**: Contiene la classe principale del dominio applicativo (`MartianGame`), che gestisce la topologia della mappa, le regole di sblocco e lo stato vitale del giocatore.
3. **`Game.Type`**: Raggruppa le entità fondamentali del framework di base, tra cui le classi astratte per `Room`, `GameObject` e le definizioni dei comandi.
4. **`Game.Parser`**: Raggruppa le classi deputate all'analisi lessicale e sintattica dell'input testuale inserito dall'utente.

---

## 5. Specifica Algebrica delle Strutture Dati
All'interno della progettazione formale del sistema, le strutture dati fondamentali sono state definite mediante specifiche algebriche astratte per garantirne la correttezza logica.

### 5.1 Struttura Dati: Insieme (Set)
Collezione non ordinata di elementi priva di duplicati, impiegata principalmente per la memorizzazione e la verifica rapida delle parole da ignorare (*stopwords*).

* **Specifica Sintattica**:
  * `newSet()` $\rightarrow$ `Insieme`
  * `isEmpty(Insieme)` $\rightarrow$ `Boolean`
  * `add(Insieme, Elemento)` $\rightarrow$ `Insieme`
  * `remove(Insieme, Elemento)` $\rightarrow$ `Insieme`
  * `contains(Insieme, Elemento)` $\rightarrow$ `Boolean`
  * `getSize(Insieme)` $\rightarrow$ `Integer`

* **Specifica Semantica**:
  * `isEmpty(newSet()) = true`
  * `isEmpty(add(s, e)) = false`
  * `contains(newSet(), e) = false`
  * `contains(add(s, e), e') = if (e' == e) then true else contains(s, e')`
  * `getSize(newSet()) = 0`
  * `getSize(add(s, e)) = if contains(s, e) then getSize(s) else getSize(s) + 1`

---

### 5.2 Struttura Dati: Lista (List)
Collezione ordinata e indicizzata utilizzata per la gestione sequenziale dell'inventario del giocatore e degli oggetti presenti nelle stanze.

* **Specifica Sintattica**:
  * `newList()` $\rightarrow$ `Lista`
  * `isEmpty(Lista)` $\rightarrow$ `Boolean`
  * `add(Lista, Elemento, Integer)` $\rightarrow$ `Lista`
  * `remove(Lista, Integer)` $\rightarrow$ `Lista`
  * `getItem(Lista, Integer)` $\rightarrow$ `Elemento`
  * `getSize(Lista)` $\rightarrow$ `Integer`

* **Specifica Semantica**:
  * `isEmpty(newList()) = true`
  * `isEmpty(add(l, e, i)) = false`
  * `getSize(newList()) = 0`
  * `getSize(add(l, e, i)) = getSize(l) + 1`
  * `getItem(newList(), i) = error`
  * `getItem(add(l, e, i), i') = if (i' == i) then e else getItem(l, i')`

---

## 6. Applicazione Avanzata degli Argomenti del Corso

### 6.1 Il Parser Lessicale e Sintattico Avanzato
Il modulo `Parser` rappresenta l'interfaccia linguistica del sistema. Quando l'utente inserisce una frase, il parser esegue le seguenti operazioni sequenziali:
1. **Normalizzazione**: Conversione di tutti i caratteri in minuscolo e rimozione della punteggiatura superflua.
2. **Tokenizzazione**: Suddivisione della stringa in un array di singoli token testuali.
3. **Filtraggio delle Stopwords**: Rimozione di articoli, preposizioni e congiunzioni non rilevanti mediante un confronto con un set precaricato in memoria (`HashSet<String>`).
4. **Riconoscimento dei Comandi**: Corrispondenza dei token rimasti con i verbi riconosciuti dal sistema (`CommandType`) e ricerca dei sostantivi corrispondenti agli oggetti presenti nella stanza corrente o nell'inventario del giocatore, restituendo infine un oggetto strutturato `ParserOutput`.

### 6.2 Persistenza dei Dati tramite JDBC e Database H2 Embedded
L'applicazione sfrutta un database relazionale **H2 embedded** per la gestione e l'inizializzazione del mondo di gioco. I vantaggi di questa scelta includono l'assenza di configurazioni di server esterni e la rapidità di esecuzione in-memory. All'avvio, il sistema esegue script SQL strutturati per la creazione delle tabelle relazionali:

* **Tabella `Rooms`**: Memorizza la topologia della base spaziale (`id`, `name`, `description`, e gli identificativi delle stanze adiacenti `north`, `south`, `east`, `west`).
* **Tabella `Objects`**: Contiene i dati relativi agli elementi interattivi (`id`, `name`, `description`, il flag booleano `pickupable` e il riferimento `location_id` alla stanza o all'inventario).

L'accesso ai dati è incapsulato attraverso l'utilizzo del pattern DAO, garantendo un codice pulito e privo di query SQL sparse nella logica di business.

### 6.3 Gestione della Concorrenza e dei Timer asincroni
Per assicurare un'esperienza fluida e non bloccare il thread principale dell'interfaccia grafica (Event Dispatch Thread - EDT), l'architettura fa uso combinato di Timer e Thread asincroni:
* **`javax.swing.Timer`**: Utilizzati in modo estensivo per le operazioni che richiedono l'aggiornamento sicuro dei componenti visivi. Gestiscono l'animazione delle particelle nel menu, il lampeggio d'emergenza della barra dell'ossigeno e l'effetto "macchina da scrivere" (*Typewriter*) per il rendering graduale dei testi.
* **Thread Indipendenti**: Le operazioni di I/O pesanti (come il caricamento e la riproduzione in loop delle tracce audio `Clip`) e gli effetti di distorsione visiva (come lo *Screen Shake* durante i danni all'Hub) sono incapsulati all'interno di Thread dedicati che operano in background.

### 6.4 Utilizzo di Lambda Expressions e Stream API
L'adozione delle Stream API e delle espressioni Lambda introdotte a partire da Java 8 permette di gestire le collezioni di dati in modo dichiarativo, conciso ed estremamente efficiente. Ad esempio, la ricerca di un oggetto specifico all'interno dell'inventario del giocatore viene implementata nel modo seguente:

```
java
public GameObject findObjectInInventory(String targetName) {
    return inventory.stream()
            .filter(item -> item.getName().equalsIgnoreCase(targetName))
            .findFirst()
            .orElse(null);
}
```

### 6.5 Gestione dei File e dei Flussi I/O Multimediali
L'applicazione fa un uso avanzato dei flussi di I/O di Java per il caricamento dinamico degli asset multimediali. La colonna sonora e gli effetti speciali (in formato .wav) vengono recuperati dal classpath dell'applicazione tramite getResourceAsStream(). I dati grezzi vengono incapsulati in un BufferedInputStream e convertiti in AudioInputStream per garantire una riproduzione senza latenze tramite le API di javax.sound.sampled. I controlli del volume convertono linearmente i valori dello slider in decibel logaritmici applicati direttamente su FloatControl.Type.MASTER_GAIN.

### 6.6 GUI Personalizzata in Java Swing e Animazioni Custom
L'interfaccia utente è realizzata interamente in Java Swing puro senza engine esterni:

MapPanel (Graphics2D): Sfrutta il rendering vettoriale (RenderingHints.KEY_ANTIALIASING) e un Timer dedicato per generare un effetto radar a impulsi concentrici sulla stanza corrente del giocatore.

Pannello di Gioco Integrato: Include una barra superiore con monitor O2 e il pulsante per uscire dalla missione in qualsiasi momento, un pad direzionale per gli spostamenti e una gestione dinamica dell'inventario.

Focus e Overlay Finestre (GlassPane): Alla fine della partita (vittoria o game over), viene attivato un overlay semi-trasparente sul GlassPane della finestra principale che oscura la schermata di gioco sottostante. Contestualmente, viene aperta una JDialog priva di bordi (undecorated) che disabilita i controlli di gioco e offre le opzioni per riavviare la missione, tornare al menu iniziale o uscire dal programma.