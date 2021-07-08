Zadanie 8 - Spring Security, podstawy
=========================


#### 1. Zaimportować aplikację w wersji startowej

- zaimportować w git bazową wersje aplikacji z tagiem "0.8"
- upewnić się że obecny branch to <i_nazwisko_dev>
- skopiować lokalnie zmiany zdalnego mastera

        git fetch origin master --tags
- skopiować do swojego brancha commit z tagiem "0.8"

        git cherry-pick 0.8

- zmergować własne zmiany w klasach testowych z pobranym z serwera commitem             
- utworzyć branch GIT na którym rozwiązne będzie zadanie

        git checkout -b <i_nazwisko_ex8>

#### 2. Rozpoznanie

- zapoznać się z przykładowym rozwiązaniem poprzedniego zadania, zwrócić uwagę na
     - klasę `UserDto`
     - klasę `UserInitializer`, w której konstruktor zapisuje do bazy 2 przykładowych użytkowników
     - test, w którym upewniamy się, że hasło nie jest zwracane w usłudze REST    
- uruchomić aplikację i odszukać w logach zapisywanie użytkowników           
- poeksperymentować w `UserInitializer`, zmieniając wstawianych użytkowników tak, 
aby nie były spełnione warunki waliddacyjne, za każdym razem uruchamiając aplikację
- przywrócić `UserInitializer` do początkowej wersji
- przetestować endpoint `/users` klientem REST
- dodać do aplikacji zależność do `spring-boot-starter-security`
- uruchomić aplikację i przetestować endpointy `/users` oraz `/todos`

#### 3. Wstęp do security

Celem zadania jest sprawdzenie krok po kroku elementarnych zasad działania konfiguracji security. 
Wszystkie testy należy wykonywać klientem REST.

- odkomentować klasę `BasicSecurityConfig`, ale metodę `configure()` pozostawić zakomentowaną
- uruchomić aplikację i spróbować przetestować dowolny endpoint 
- odkomentować nagłówek metody `configure()`, ale treść metody pozostawić zakomentowaną
- uruchomić aplikację i przetestować endpointy, sprawdzić metody GET, POST, PUT na endpoincie `/todos`
- odkomentować treść metody `configure()`
- uruchomić aplikację i przetestować jak poprzednio

W tym momencie została skonfigurowana aplikacja, która zezwala anonimowemu użytkownikowi na dostęp do wszystkiego.

#### 4. In memory security

Celem zadania jest zdefiniowanie poziomów dostępu do różnych zasobów różnym użytkownikom. Zasoby definiowane są przez URL-e.

- zakomentować adnotacje na klasie `BasicSecurityConfig`
- odkomentować kod klasy `InMemorySecurityConfig`
- uruchomić aplikację i przetestować endpointy klientem REST
     - najpierw tak jak do tej pory
     - w celu autoryzacyji żądania HTTP należy każdorazowo dodać nagłówek autoryzacji, z typem "Basic", 
login i hasło wyciągnąć z metody `InMemorySecurityConfig.configureGlobal()`
     - sprawdzić którzy użytkownicy mogą użyć endpointów:
          - `/users` 
          - `/todos` (także POST) 
          - `/todos/1` (także PUT, DELETE)
- poprawić bezpieczeństwo zasobów tak, aby:
    - dostawianie, usuwanie i wszystkie żądania `/todos/{id}` również wymagały uwierzytelnienia rolą USER
    - administrator miał możliwość korzystania z endpointów zaczynających się od `/todos` 
    (podpowiedź: jeden użytkownik może mieć kilka ról)
- zabezpieczyć zasób wyszukiwarki obiektów Todo rolą 'USER'    
          
**Pytanie:** jaki jest związek między użytkownikami, których zapisujemy w bazie, 
a użytkownikami, których używamy do autoryzacji zasobów? 
   
#### 5. Zapisać własne zmiany   

      git branch
      git commit -m "odpowiedni komentarz"  //może być więcej commitów
      git push -u origin <i_nazwisko_ex8>
