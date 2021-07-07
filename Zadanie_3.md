Zadanie 3 - Lombok, uzupełnienie REST
=========================


#### 1. Zaimportować aplikację w wersji startowej

- zaimportować w git bazową wersje aplikacji z tagiem "0.3"
  - upewnić się że obecny branch to <i_nazwisko_dev>
  - skopiować lokalnie zmiany zdalnego mastera

          git fetch origin master --tags
  - skopiować do swojego brancha commit z tagiem "0.3"

          git cherry-pick 0.3

  - utworzyć branch GIT na którym rozwiązne będzie zadanie

          git checkout -b <i_nazwisko_ex3>
      
#### 2. Bugfix
- zapoznać się z proponowanym rozwiązaniem repozytorium i kontrolera
- znaleźć scenariusz powodujący błąd w logice działania aplikacji, spowodować błąd używając klienta REST
- poprawić ten błąd             
      
#### 3. Biblioteka Lombok
Celem zadania jest odchudzenie kodu z powtarzalnych metod i zastąpienie ich krótszymi adnotacjami.

- dodać do pliku mavena `pom.xml` zależność do biblioteki lombok
- zastąpić w klasie `Todo` gettery, settery i konstruktory odpowiednimi adnotacjami lomboka
- dodać wsparcie dla metod `equals()` oraz `hashCode()`

W dalszej części projektu, o ile to możliwe, należy używać lomboka

#### 4. REST - obsługa wyjątków, 
Rozwinąć klasę `TodoRestController`

- dla wyszukiwania jednego elementu - jeśli nie ma elementu w repozytorium, wyrzucić `EntityNotFoundException` 
(nowy rodzaj wyjątku dziedziczący z `RuntimeException`) 
- sprawdzić zachowanie klienta REST dla powyższej sytuacji
- używając adnotacji 

         @ExceptionHandler(EntityNotFoundException.class)        
   napisać metodę obsługującą wyjątki tego typu, która odeśle tekstową informację o problemie
  
#### 5. REST - HttpStatus
W tej chwili aplikacja odpowiada na żądania REST kodami: 200, 404. 

- Należy to rozwinąć używając adnotacji @ResponseStatus i enuma `HttpStatus`. Ustawić
   - dla metody http POST - status CREATED
   - dla metody http DELETE - status NO_CONTENT
   - dla błędu `EntityNotFoundException` - status NOT_FOUND
- Sprawdzić odsyłane statusy wszystkich metod klientem REST

#### 6. Usługa wyszukiwania - REST + Java 8
Napisać usługę umożliwiającą wyszukiwanie obiektów Todo po nazwie i/lub priorytecie 
(jeśli istnieją oba parametry wyszukania należy wyszukiwać po obu, jeśli jeden - wyszukać po tym parametrze, 
jeśli żaden - zwrócić wszystkie elementy)

- Napisać metodę, która będzie realizować to wyszukiwanie z repozytorium (sugestia: użyć strumienia oraz filtrowania Javy 8)
- Napisać w nowej klasie usługę REST pod adresem
      
        http://localhost:8080/search/todos       
   która otrzyma parametry wyszukiwania w URLu, zgodnie ze standardem metody GET
  
#### 7. Zapisać własne zmiany
      git branch
      git commit -m "odpowiedni komentarz"  //może być więcej commitów
      git push -u origin <i_nazwisko_ex3>
      