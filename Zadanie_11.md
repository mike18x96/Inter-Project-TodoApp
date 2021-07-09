Zadanie 11 - Rozwinięce aplikacji
=========================

**Informacja:** Ostatni zestaw zadań, do tego zestawu nie będzie rozwiązania. Sposób rozwiązania - dowolny mieszczący się w wymaganiach.

#### 1. Zaimportować aplikację w wersji startowej


- zaimportować w git bazową wersje aplikacji z tagiem "0.11"
- upewnić się że obecny branch to <i_nazwisko_dev>
- skopiować lokalnie zmiany zdalnego mastera

        git fetch origin master --tags
- skopiować do swojego brancha commit z tagiem "0.11"

        git cherry-pick 0.11

- zmergować własne zmiany w klasach testowych z pobranym z serwera commitem
- zapoznać się z rozwiązaniem i przeanalizować
- utworzyć branch GIT na którym rozwiązne będzie zadanie

        git checkout -b <i_nazwisko_ex11>     
      
#### 2. Todo powiązane z User

Nowe wymaganie: każde `Todo` ma swojego właściciela. 

- Zmienić model tak, aby każdy obiekt `Todo` był połączony z Userem poprzez login użytkownika i tak zapisywany w bazie
(ale `Todo` nie może znać hasła użytkownika)
- Pobranie wszystkich `Todo` ma działać inaczej dla różnych ról
     - użytkownik może pobrać tylko te elementy, których jest właścicielem
     - admin widzi i pobiera wszystkie elementy 
     - rolę i login użytkownika należy wyciągnąć z kontekstu bezpieczeństwa, np:
     
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CurrentUser currentUser = (CurrentUser) auth.getPrincipal();
                            
- Wyszukiwarka `Todo` ma działać z tymi samymi obostrzeniami 
- Te same obostrzenia dotyczą usuwania i edycji istniejących `Todo` - zmian może dokonywać właściciel lub admin
- Przy zapisywaniu `Todo` należy automatycznie użyć loginu użytkownika z kontekstu bezpieczeństwa 
   
#### 3. Zarządzanie użytkownikami   

Dodać możliwość zarządzania użytkownikami wg wymagań:
- admin widzi wszystko i może zarządzać użytkownikami w bazie (create, delete, 
reset hasła -> odpowiedź hasłem generowanym, zmiana uprawnien)
- użytkownik ma endpoint na zmianę hasła i jednoczesnie nie moze nic innego zmienic w swoim obiekcie
- przy hasłach danego użytkownika należy stosować URL zgodny z konwencją REST (`/users/{login_usera}/password`)

Oczywiście należy zaktualizować testy, napisać testy do nowych wymagań, a także sprawdzić swoje rozwiązanie klientem REST.

#### 4. Dynamiczne zapytania

Wyszukiwarka todo w tej chwili korzysta z anotacji @Query.
Takie rozwiązanie się nie skaluje. Możliwe jest że bedzię potrzeba dodania innych warunków niż tylko sprawdzanie null.
(np. nazwa `Todo` zaczynająca się od wybranego ciągu znaków).

Aby umożliwić tworzenie dynamicznych zapytań z odpowiednio generowaną klauzulą WHERE należy użyć technologii pozwalającej 
generować i składać predykaty logiczne. W technologiach JPA oraz Hibernate stworzono w tym celu "Criteria Api".

Spring Data wspiera takie zapytania ([LINK](https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/)) poprzez: 
- interfejs `Specification` (z którego można otrzymać `Predicate` z JPA Criteria Api) 
- wsparcie dla biblioteki QueryDsl

**Wyzwanie:** używając jednego z wymienionych rozwiązań (najlepiej `Specification`) zmienić wyszukiwarkę Todo, 
aby można było wyszukiwać po dowolnym zestawie pól z klasy Todo, dopasowanie każdego pola relacją równości. 
Paging oraz wymagania z punktu 2 pozostają.

#### 5. Uzupełnienie

Elementami, które są często przydatne w implementacji usług REST (i nie tylko) i które warto znać są m.i.:
 - `ResponseEntity` - do parametryzowania odpowiedzi HTTP, np. ręcznego ustawiania HttpStatus 
 (różne statusy w odpowiedzi na jedno żądanie, co nie jest możliwe przy @ResponseStatus)
 - adnotacje zmieniające sposób mapowania na/z JSON (np. @JsonIgnore) oraz własne mappery JSON
 - własne walidatory 