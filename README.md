# Tournament manager

Tournament manager for free use written in Java.

Features:

- [X] adding players
- [X] removing players
- [X] Swiss system using JavaFO
- [X] saving round results
- [X] displaying the current round
- [X] results
- [X] tiebreakers
- [X] number of rounds
- [X] manual pairing
- [X] displaying all rounds
- [X] round-robin
- [X] Elo rating increment
- [X] obtained PL ranking
- [X] half-bye
- [X] removing players from a round
- [X] removing players from the tournament
- [X] setting tournament descriptive data
- [X] FIDE tournament registration (registration form)
- [X] FIDE report (TRF)
- [ ] FIDE titles
- [ ] prints
- [X] assigning starting numbers
- [X] checking a player in CR (ChessResults)
- [ ] custom engine
- [X] filtered results
- [ ] team tournaments
- [X] schedule
- [X] opening/importing a tournament
- [X] obtained FIDE ranking
- [X] multiple groups
- [ ] diploma generator
- [ ] transmission
- [ ] form for FA norm (FIDE Arbiter)
- [ ] title certificate
- [X] importing TRF
- [X] importing PGN
- [X] importing SWSX

Shortcuts:

- CTRL + Q - close the program
- CTRL + S - save
- CTRL + SHIFT + S - save as
- CTRL + O - open
- z, x, c - "1-0", "0.5-0.5", "0-1" when entering results

# Running

## 1. Using IntelliJ IDEA

- Install [IntelliJ IDEA](https://www.jetbrains.com/idea/download/?section=windows)
- Download and unzip this [project](https://github.com/KulAndy/tournamentmanager/archive/refs/heads/master.zip)
- Launch IntelliJ IDEA
- File > Open and select the unzipped project folder
- In File > Project Structure > Project, set the SDK to at least version 20 (otherwise, click the dropdown menu > Add SDK)
- Run > Run 'Main' or right-click on Main > Run 'Main'

## 2. Java and Maven

- Install the latest [Java](https://www.oracle.com/pl/java/technologies/downloads/), SDK min 20
- Download and unzip this [project](https://github.com/KulAndy/tournamentmanager/archive/refs/heads/master.zip)
- Go to the unzipped directory
- For Windows:
    - Right-click > open in terminal / open in PowerShell
    - To run, type the command .\mvnw.cmd javafx:run (dependencies will be installed on the first run)
- For Linux/MacOS:
    - Navigate to the directory in the terminal (depending on the graphical environment, this might be possible via the file explorer)
    - To run, type the command ./mvnw javafx:run (executable permissions may need to be added, e.g., chmod +x ./mvnw; dependencies will be installed on the first run)

In case of issues:

- For Windows:
    - Run the command .\mvnw.cmd dependency:resolve
- For Linux/MacOS:
    - Enter the command ./mvnw dependency:resolve
- For both:
    - Install [Maven](https://maven.apache.org/download.cgi) following the instructions provided.

# Tournament manager

Menedżer turniejowy do wolnego użytku napisany w javie

Funkcje:

- [X] dodawanie graczy
- [X] usuwanie graczy
- [X] szwajcar z użycie javafo
- [X] zapisywanie wyników rundy
- [X] wyświetlanie aktualnej rundy
- [X] wyniki
- [X] tiebreake
- [X] liczba rund
- [X] manualne kojarzenie
- [X] wyświetlanie wszystkich rund
- [X] kołówka
- [X] przyrost elo
- [X] ranking uzyskany pl
- [X] half-bye
- [X] usuwanie graczy z rundy
- [X] usuwanie graczy z turnieju
- [X] ustalanie danych opisowych turnieju
- [X] rejestracja turnieju fide (formularz rejestracyjny)
- [X] sprawozdanie fide (trf)
- [ ] tytuły fide
- [ ] wydruki
- [X] przydział numerów startowych
- [X] sprawdzanie zawodnika w cr
- [ ] własny silnik
- [X] wyniki filtrowane
- [ ] turnieje drużynowe
- [X] harmonogram
- [X] otwieranie/import turnieju
- [X] ranking uzyskany fide
- [X] wiele grup
- [ ] generator dyplomów
- [ ] transmisja
- [ ] formularz na normę FA
- [ ] ceryfikat tytułu
- [X] import trf
- [X] import pgn
- [X] import swsx

Skróty:

- CTRL + Q - zamknij program
- CTRL + S - zapisz
- CTRL + SHIFT + S - zapisz jako
- CTRL + O - otwórz
- z, x, c - "1-0", "0.5-0.5", "0-1" przy wpisywaniu wyników

# Uruchamianie

## 1. Zapomocą Idea IntelliJ

- zainstaluj [Idee](https://www.jetbrains.com/idea/download/?section=windows)
- pobierz i rozpakuj ten [projekt](https://github.com/KulAndy/tournamentmanager/archive/refs/heads/master.zip)
- uruchom Idee
- File > Open i wybierz rozpakowany projekt folder
- w File > Project Structure > Project musi być SDK w wersji min 20 (inaczej kiliknij menu rozwijane > Add SDK)
- Run > Run 'Main' lub najechać na Main, PPM > Run 'Main'

## 2. Java i Maven

- Zainstaluj najnowszą [Javę](https://www.oracle.com/pl/java/technologies/downloads/), SDK min 20
- pobierz i rozpakuj ten [projekt](https://github.com/KulAndy/tournamentmanager/archive/refs/heads/master.zip)
- przejdź do rozpakowanego katalogu
- w systemie windows
    - PPM > otwórz w terminalu / otwórz w oknie powershell
    - żeby uruchomić wpisz komendę .\mvnw.cmd javafx:run (przy pierwszym uruchomieniu zostaną zainstalowane zależności)
- w systemie linux/macos
    - przejdż w terminalu do katalogu (w zależności od środowiska graficznego może dać się to zrobić w eksploratorze
      plików)
    - żeby uruchomić wpisz komendę ./mvnw javafx:run (może być potrzebne dodanie wykonywalności np. poprzez chmod +x
      ./mvnw; (przy pierwszym uruchomieniu zostaną zainstalowane zależności))

w przypadku problemów

- w systemie windows
    - uruchom komendę .\mvnw.cmd dependency:resolve
- w systemie linux/macos
    - wpisz komendę ./mvnw dependency:resolve
- w obu
    - Zainstaluj [Maven](https://maven.apache.org/download.cgi) zgodnie z instrukcją