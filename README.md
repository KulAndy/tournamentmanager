# tournamentmanager

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
- [ ] wyniki filtrowane
- [ ] turnieje drużynowe
- [ ] harmonogram
- [X] otwieranie/import turnieju
- [X] ranking uzyskany fide
- [X] wiele grup
- [ ] generator dyplomów
- [ ] transmisja
- [ ] formularz na normę FA
- [ ] ceryfikat tytułu
- [X] import trf
- [ ] import pgn
- [X] import swsx

Skróty:
- CTRL + Q - zamknij program
- CTRL + S - zapisz
- CTRL + SHIFT + S - zapisz jako
- CTRL + O - otwórz
- z, x, c - "1-0", "0.5-0.5", "0-1" przy wpisywaniu wyników

Uruchamianie
## 1. Zapomocą Idea IntelliJ
- zainstaluj [Idee](https://www.jetbrains.com/idea/download/?section=windows)
- pobierz i rozpakuj ten [projekt](https://github.com/KulAndy/tournamentmanager/archive/refs/heads/master.zip)
- uruchom Idee
- File > Open i wybierz rozpakowany projekt folder
- w File > Project Structure > Project musi być SDK w wersji min 20 (inaczej kiliknij menu rozwijane > Add SDK)
- Run > Run 'Main' lub najechać na Main, PPM > Run 'Main'

## 2. Java i Maven
- Zainstaluj najnowszą [Javę](https://www.oracle.com/pl/java/technologies/downloads/), SDK min 20
- Zainstaluje [Maven](https://maven.apache.org/download.cgi) (w zależności od SDK Javy może nie być konieczne)
- pobierz i rozpakuj ten [projekt](https://github.com/KulAndy/tournamentmanager/archive/refs/heads/master.zip)
- przejdź do rozpakowanego katalogu
- w systemie windows
  - PPM > otwórz w terminalu / otwórz w oknie powershell
  - uruchom komendę .\mvnw.cmd dependency:resolve
  - żeby uruchomić wpisz komendę .\mvnw.cmd javafx:run
- w systemie linux/macos
  - przejdż w terminalu do katalogu (w zależności od środowiska graficznego może dać się to zrobić w eksploratorze plików)
  - wpisz komendę ./mvnw dependency:resolve
  - żeby uruchomić wpisz komendę ./mvnw javafx:run
