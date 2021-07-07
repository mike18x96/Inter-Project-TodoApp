Zadanie 1 - setup
=========================

#### 1. Zaimportować aplikację
- skopiować repozytorium git z projektem startowym
       
- zaimportować projekt startowy do IDE (zalecane IntelliJ Idea)

- zaimportować w git bazową wersje aplikacji z tagiem "0.1"

        git checkout 0.1        

#### 2. Uruchomic aplikacje lokalnie

- z poziomu IDE (RightMouseClick na Application -> Run)
- za pomocą Mavena (użyć komend z pliku README)
- za pomocą Mavena z IDE 
     - zaznaczyć odpowiednie kroki w Lifecycle, 
     - nast RMC -> "run Maven Build"

Każdorazowo sprawdzić w przeglądarce czy aplikacja działa (adres w pliku README)
     
#### 3. Utworzyć własny branch GITa i go spushować 

Każdy tworzy własny branch, dev na którym od tej pory będzie pracował. Niech branch nazywa się skrótem 
utworzonym z litery imienia oraz nazwiska zakonczonego _dev (np. `jkowalski_dev` dla Jana Kowalskiego)
 
        git checkout -b <i_nazwisko_dev>
        
Następnie należy spushować nowych branch _dev na serwer 

        git push -u origin <i_nazwisko_dev>

Branch "master" będzie zawierał punkty wejścia do kolejnych zadań oraz rozwiązania poprzednich.

**Nie wolno komitować do "mastera"!!.**        

**Do <i_nazwisko_dev> wolno komitować tylko przez merge request.**
#### 4. Utworzyć branch GIT na którym rozwiązne będzie zadanie
Stworzyc branch na którym będzie rozwiązane zadania a następnie zmergowane do brancha _dev przez merge request. Niech branch nazywa się skrótem
utworzonym z litery imienia oraz nazwiska zakonczonego numerem zadania (np. `jkowalski_ex1` dla Jana Kowalskiego i zadania 1)

        git checkout -b <i_nazwisko_ex1>

#### 5. Dodać możliwość używania standardowych endpointów SpringBoot
     
- dodać zależność mavena w pliku `pom.xml` do artefaktu `spring-boot-starter-actuator`
- w pliku `application.properties` dodać wpis `management.endpoints.web.exposure.include=*`
- zbudować i uruchomić aplikację
- otworzyć w przeglądarce endpointy
  - `http://localhost:8080/actuator/info`
  - `http://localhost:8080/actuator/health`
#### 6. Zmienić port serwera

- w pliku `application.properties` dodać wpis ustawiający port Tomcata na 8181
- uruchomić aplikację i sprawdzić `/info` oraz `/health` w przeglądarce
- przestawić port serwera na 8080

#### 7. Zapisać własne zmiany

- Sprawdzenie brancha, na którym jesteśmy

         git branch
- Lokalny commit: 
    - z konsoli 
         
             git commit -m "komentarz" 
    - lub z IDE         


- Wypchnięcie brancha z rozwiązaniem na serwer
         
         git push -u origin <i_nazwisko_ex1>
         