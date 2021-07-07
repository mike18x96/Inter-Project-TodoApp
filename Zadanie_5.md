Zadanie 5 - Guava, walidacja, testy
=========================


#### 1. Zaimportować aplikację w wersji startowej


- zaimportować w git bazową wersje aplikacji z tagiem "0.5"
  - upewnić się że obecny branch to <i_nazwisko_dev>
  - skopiować lokalnie zmiany zdalnego mastera

          git fetch origin master --tags
  - skopiować do swojego brancha commit z tagiem "0.5"

          git cherry-pick 0.5

  - utworzyć branch GIT na którym rozwiązne będzie zadanie

          git checkout -b <i_nazwisko_ex5>


#### 2. Bugfix

- zapoznać się z proponowanym rozwiązaniem repozytorium i kontrolera
- znaleźć sytuację, dla której serwer zwraca "brzydki" błąd z kodem 500 na żądanie REST, 
dodatkowo wypisując stackTrace wyjątku na konsolę
- poprawić błąd tak, aby serwis był spójny
      
#### 3. Guava
Celem zadania jest zastąpienie pewnych standardowych, powtarzalnych mini-algorytmów wywołaniami 
odpowiednich gotowych metod (najczęściej statycznych utilsów) z biblioteki guava od Google'a.

- dodać do projektu zależność do biblioteki guava 
- w klasach `SearchTodoService` oraz `TodoRestController` zastąpić wywołania metod:
     - `iterable2list()`

     odpowiednimi metodami statycznymi z guavy.
     
Inną popularną i sprawdzoną biblioteką wspierającą standardowe sytuacje jest apache-commons     

#### 4. Hibernate validator

- dodać do projektu zależność do `spring-boot-starter-validation`
- ustawić adnotację @NotNull polu `name` klasy `Todo` 
- wytestować klientem REST próbę wstawienia i zmiany obiektu z brakującym polem `name`
- ustawić adnotację @Valid parametrom typu Todo w metodach modyfikujących kontrolera REST
- wytestować ponownie działanie klientem REST

#### 5. Testy jednostkowe, wprowadzenie

Aby wprowadzić do aplikacji możliwość uruchamiania i pisania testów jednostkowych, należy
umieścić w niej zależność do biblioteki testowej. W środowisku Javy najpopularniejszą jest JUnit, 
inną popularną TestNG.

W naszym projekcie zależność springowa `spring-boot-starter-test` dostarcza kilka zależności 
do bardzo przydatnych i popularnych bibliotek wspierających testy (m.i. JUnit, AssertJ, Mockito, Hamcrest). 
Stąd nie ma potrzeby dodawać tych zależności wprost w Mavenie.

Istnieje wiele konwencji nazywania metod testowych, podstawą wszystkich jest CZYTELNOŚĆ. W tym 
projekcie ustalmy nazewnictwo odpowiadające standardowym blokom testu given-when-then 
wg. konwencji:

     nazwaTestowanejMetody_sytuacjaWejsciowa_spodziewanyWynikTestu()
     
- Zapoznać się i przeanalizować klasę `SearchTodoServiceTest`
- Zapoznać się z klasą `TodoFakeRepositoryImpl` stanowiącą tzw. "test double" - sztuczną, uproszczoną implementację
interfejsu utworzoną na potrzeby testów. W klasie tej większość metod została wygenerowana z IDE i pozostawiona w domyślnej implementacji.
Zaimplementowane zostały metody wykorzystywane w naszym teście.
- Uruchomić test każdym ze sposobów:
     - RMC na klasie testowej -> Run   
     - RMC w kodzie na metodzie testującej -> Run  
     - RMC na pakiecie `service` oraz `com.inetum.training`
     - z linii poleceń komendą
  
             mvn test
        
     - z IDE IntelliJ w mavenie RMC na "Lifecycle"->"test" i wybrać "run build"  
  
#### 6. Własne testy
- Napisać testy do pozostałych metod serwisu wyszukiwania
- Stworzyć co najmniej jeden test, który zwróci brak wyszukanych elementów - istotna jest tu reprezentacja 
 pustego wyszukiwania (null czy lista pusta?)
- Zakomitować zmiany w testach jednostkowych (zadanie 5) jako oddzielny commit   
   
#### 7. Zapisać własne zmiany   

    git branch
      git commit -m "odpowiedni komentarz"  //może być więcej commitów
      git push -u origin <i_nazwisko_ex5>