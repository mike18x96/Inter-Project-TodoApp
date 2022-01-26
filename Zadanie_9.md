Zadanie 9 - Spring Security, kontynuacja
=========================


#### 1. Zaimportować aplikację w wersji startowej

- zaimportować w git bazową wersje aplikacji z tagiem "0.9"
- upewnić się że obecny branch to <i_nazwisko_dev>
- skopiować lokalnie zmiany zdalnego mastera

        git fetch origin master --tags
- skopiować do swojego brancha commit z tagiem "0.9"

        git cherry-pick 0.9

- zmergować własne zmiany w klasach testowych z pobranym z serwera commitem
- utworzyć branch GIT na którym rozwiązne będzie zadanie

        git checkout -b <i_nazwisko_ex9>        
      
#### 2. UserDetails

- poczytać w dokumentacji co reprezentuje i do czego służy interfejs `UserDetails`
- odkomentować w klasie `CurrentUser` zakomentowane elementy
- uzupełnić klasę tak, aby poprawnie implementowała `UserDetails`
     - metody boolowskie niech zwracają `true` (nie będziemy w takich szczegółach reprezentować kont użytkowników)
     - pozostałe zgodnie z nazewnictwem
     
W kolejnym kroku domenowa klasa `User` zostanie połączona z klasą reprezentującą użytkownika w kontekście security     
     
- zaimplementować klasę `UserDetailsServiceImpl` 
    - klasa powinna korzystać z `UserRepository`
    - metoda `loadUserByUsername()` powinna wyszukać w repozytorium użytkownika po jego loginie, a następnie 
    na podstawie wyszukanego obiektu stworzyć i zwrócić obiekt typu `CurrentUser`
          - rola użytkownika powinna zaczynać się przedrostkiem `ROLE_`
    - w przypadku braku w bazie użytkownika o szukanym loginie należy rzucić `UsernameNotFoundException`

#### 3. Konfiguracja Security - użytkownicy w bazie danych

- zakomentować adnotacje w klasie `InMemorySecurityConfig` (@Configuration @EnableWebSecurity)
- odkomentować klasę `SecurityConfig` (`PasswordEncoder` pozostawić zakomentowany) 
- skopiować metodę `configure(HttpSecurity)` do klasy `SecurityConfig`
- wyjaśnienie: metoda `configure(AuthenticationManagerBuilder auth)` konfiguruje AuthenticationManager, 
aby używał naszego `UserDetailsServiceImpl` do pobierania użytkowników po loginie
- uruchomić i przetestować aplikację klientem REST (role, zasoby, endpointy)

Czy wszystko działa jak powinno? (każda rola ma dostęp do tych zasobów co poprzednio)

- jeśli coś nie działa jak poprzednio, poprawić `configure(HttpSecurity http)`

#### 4. Szyfrowanie haseł

- odkomentować w klasie `SecurityConfig` metodę `passwordEncoder()` - co uzyskujemy w ten sposób?
- zaszyfrować hasła przed zapisaniem w bazie 
     - w klasie `UserInitializer` dodać do konstruktora parametr typu `PasswordEncoder`
     - w tym konstruktorze użyć metody `.encode()` obiektu szyfrującego przed zapisem hasła do bazy
- uruchomić aplikację i znaleźć w logach jak teraz zapisywani są użytkownicy (hasło)
- usunąć z klasy `User` ograniczenie na długość hasła
- w klasie `SecurityConfig` 
     - dodać pole @Autowired typu `PasswordEncoder`
     - dodać ten encoder do tworzonego `UserDetailsService` w metodzie `configure(AuthenticationManagerBuilder)`
- uruchomić aplikację i przetestować klientem REST (role, zasoby, endpointy)

#### 5. Konfiguracja preAuthorized na usłudze

W tej chwili całe security skonfigurowane jest centralnie w klasie konfiguracyjnej. Część tej konfiguracji
można przenieść do klas serwisów/kontrolerów określając danej usłudze jej obostrzenia. 

- zmienić w `SecurityConfig` konfigurację dla requestów HTTP: po linii `http.authorizeRequests()`
     - zakomentować dalsze linie
     - zezwolić każdemu na dostęp do endpointów `/actuator/info`, `/actuator/health`
     - dla pozostałych endpointów wymagamy pełnego logowania dodając:
     
             .anyRequest().fullyAuthenticated();
             
- uruchomić i przestować aplikację klientem REST - endpoint `/users` jest dostępny nie tylko dla adminów
- dodać możliwość autoryzowania adnotacjami @Pre/PostAuthorized - klasie `SecurityConfig` dodajemy adnotację:
           
           @EnableGlobalMethodSecurity(prePostEnabled = true)
           
- adnotacja @PreAuthorize na usłudze
     - dodać metodzie pobierającej użytkowników adnotację:

             @PreAuthorize("hasRole('ADMIN')")
             
     - czy adnotację można zastosować na całej klasie kontrolera?
     
Bezpieczeństwo zasobów można konfigurować wyrażeniami na wiele sposobów: 
[LINK do dokumentacji](https://docs.spring.io/spring-security/site/docs/current/reference/html/el-access.html)  
   
**Uwaga:** w rozwiązaniu zastosowano @Autowired na polach oraz na konstruktorach. 
Poczytać które z rozwiązań jest zalecane i dlaczego. Zastanowić się dlaczego użyliśmy w projekcie obu rozwiązań.

#### 6. Zapisać własne zmiany

      git branch
      git commit -m "odpowiedni komentarz"  //może być więcej commitów
      git push -u origin <i_nazwisko_ex9>
