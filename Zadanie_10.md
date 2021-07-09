Zadanie 10 - Logowanie, testy security, paging
=========================


#### 1. Zaimportować aplikację w wersji startowej

- zaimportować w git bazową wersje aplikacji z tagiem "0.10"
- upewnić się że obecny branch to <i_nazwisko_dev>
- skopiować lokalnie zmiany zdalnego mastera

        git fetch origin master --tags
- skopiować do swojego brancha commit z tagiem "0.10"

        git cherry-pick 0.10

- zmergować własne zmiany w klasach testowych z pobranym z serwera commitem
- utworzyć branch GIT na którym rozwiązne będzie zadanie

        git checkout -b <i_nazwisko_ex10>        

#### 2. Logownie
W jednym z poprzednich zadań w klasie `UserInitializer` dodana została adnotacja lomboka @Slf4j. 
Dzięki temu w metodach mamy dostęp do obiektu loggera o nazwie `log` i nie musimy pisać standardowej linii:
        
        public static final Logger log = LoggerFactory.getLogger(NazwaKlasy.class);
     
od razu możemy korzystać z tego obiektu jak w przykładzie.
     
a) Polecenia:
     
- znaleźć informacje jakie są standardowe poziomy logowania 
     - ciekawostka: jak nazywa się poziom logowania napoważniejszych błędów w Androidzie?
- sprawdzić jaki jest poziom logowania aplikacji, a jaki testów automatycznych podnoszących kontekst 
     (klasa `StandaloneUserRestControllerTest`)
- zmienić poziom logowania web aplikacji oraz hibernate na DEBUG: w pliku `application.properties` dodać wpis
   
        logging.level.org.springframework.web=DEBUG
        logging.level.org.hibernate=DEBUG

    następnie uruchomić aplikację i przejrzeć logi
    
- zmienić poziom logowania w obu przypadkach na INFO
- ustawić w pliku `.properties` możliwość podglądu zapytań SQL, które się generują z JPA. 
- uruchomić i przetestować aplikację klientem REST. Zwrócić uwagę na generowane zapytania
- logi zapytań można sformatować w sposób "przyjazny" wpisem
       
        spring.jpa.properties.hibernate.format_sql=true
       
- ponownie uruchomić i przetestować aplikację
- zdefiniować plik, do którego trafią logi, przetestować aplikację i przejrzeć plik
- zakomentować wpis konfigurujący logowanie do pliku

b) Konfiguracja na potrzeby testów
       
- aby ustawienia logowania zadziałały w testach kontekstu wystarczy klasie testu dodać adnotację @SpringBootTest - to m.i. zaczytuje
       parametry z pliku `application.properties`
- zmienić ustawienia poziomu logowania w testach. W tym celu
     - stworzyć plik `application.properties` w katalogu `resources` wewnątrz `test`
     - dodać w tym pliku wpisy, np. zmieniające poziom logowania na INFO oraz WARN
- wyłączyć banner z napisem SPRING z logów w testach     
- każdorazowo uruchomić testy i przejrzeć logi 
    
Przy uruchamianiu testów pozostają jeszcze logi springa z konfiguracji testu. To również można wyłączyć konfigurując konkretny logger:
[link1](https://www.mkyong.com/logging/logback-disable-logging-in-unit-test/), [link2](https://www.mkyong.com/spring-boot/spring-boot-test-how-to-stop-debug-logs/)

#### 3. Testy security - integracyjne

- dodać do projektu zależność do `spring-security-test` (wybrać odpowiedni scope)
- odkomentować i zapoznać się z klasą `UserRestControllerSecurityTest`
- uruchomić te testy
- uzupełnić treść pustego testu zgodnie z nagłówkiem
- zmienić poziom logowania dla Spring Security w testach na DEBUG
- ponownie uruchomić testy security

Poziom logowania kontekstu security został zmieniony dla wszystkich testów integracyjnych, 
dla testów w klasie `StandaloneUserRestControllerTest` nie jest istotny ("brudzi" logi).

**Pytanie:** jak można ustawić te poziomy logowania różnie dla różnych testów?

Gdy wszystkie testy przechodzą i nie ma problemów, warto przestawić poziom logowania tak, aby testy nie brudziły 
m.i. logów przy budowaniu projektu z poziomu mavena.

#### 4. Paging

W tej chwili aplikacja zwraca z kilku endpointów listy elementów. Listy te mogą być potencjalnie bardzo duże, natomiast 
często interfejs użytkownika potrzebuje jedynie kawałka takiej listy (5, 10, 20 elementów). 
Aby to uzyskać należy zastosować page-owanie. 

 - zmienić metodę `TodoRestController.getAll()`, aby obsługiwała page-owanie:
      - zmienić `TodoJpaRepository` aby rozszerzało interfejs `JpaRepository` (który rozszerza istotny tu `PagingAndSortingRepository`)
      - dodać metodzie parametr typu `Pageable` oraz zmienić typ zwracany na `Page<Todo>`
      - użyć w implementacji odpowiedniej metody wyszukującej `TodoJpaRepository`
 - zakomentować @Test dla testów, które nie przechodzą
 - zakomentować `TodoFakeRepositoryImpl` - klasa miałaby za dużo niepotrzebnej implementacji
 - uruchomić aplikację i przetestować klientem REST 
      - aby zastosować page-owanie wystarczy dodać w parametrach metody GET `page` i/lub `size`, np:
      
             http://localhost:8080/todos?page=1&size=2
             
         `page` - numer strony
         `size` - rozmiar strony (ilość elementów na stronie)
         
      - sprawdzić jaki jest domyślny rozmiar strony oraz jaki nr ma pierwsza strona
      
 - odkomentować zakomentowane testy i naprawić je
      - aby w testach standalone umożliwić automatyczne tworzenie obiektów `Pageable` należy rozszerzyć mockMvc, np:
      
             MockMvcBuilders.standaloneSetup(new TodoRestController(fakeRepository))
                       .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                       .build();
                       
      - oczekiwaną ścieżkę JSON należy wyszukać w odpowiedzi żądania
      - aby Mockito zrozumiało, którą metodę `findAll()` zastępujemy, należy użyć `any(Pageable.class)` 
      - najprostszą implementacją interfejsu `Page` jest `PageImpl`
      - nie należy dodawać testów sprawdzających działanie biblioteki (np. czy jeśli żądanie prosi o 2gą stronę i jest
      w repo odpowiednio wiele elementów, to zwrócone zostaną oczekiwane elementy). 
      Można sprawdzić czy repozytorium jest odpytywane z właściwymi parametrami, co czyni test "białoskrzynkowym"
      
 **Pytanie:** Czy można zorganizować klasy tak, aby FakeRepository na potrzeby testów wymagało minimalnej implementacji 
 (tylko tych metod, które są wykorzystywane w naszych kontrolerach)?      

#### 5. Paging w pozostałych kontrolerach
      
 - zamienić wszystkie metody kontrolerów zwracające listę elementów w podobny sposób (użytkownicy, wyszukiwarka)
 - przy wyszukiwarce znaleźć sposób, w jaki w Spring Data używa się `Pageable` i `Page` w metodach generowanych z nazwy
 - do zamiany `Page<User>` na `Page<UserDto>` zdefiniować własny springowy `Converter<Page, PageDto>` i użyć odpowiedniej metody 
 wykorzystującej dostarczony konwerter 
 - zaktualizować wszystkie testy i sprawdzić czy przechodzą 
 - uruchomić aplikację i przetestować klientem REST
 
 **Informacja:** dodatkowym parametrem `Pageable` jest `sort` - 
 wskazujący sposób sortowania wyników (odpowiada `order by` w SQL). 
 
#### 6. Zapisać własne zmiany

      git branch
      git commit -m "odpowiedni komentarz"  //może być więcej commitów
      git push -u origin <i_nazwisko_ex10>
