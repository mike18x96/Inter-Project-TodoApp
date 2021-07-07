Zadanie 2 - Serwis REST, statyczne repozytorium
=========================

#### 1. Zaimportować aplikację w wersji startowej

- Zaimportować w git bazową wersje aplikacji z tagiem "0.2" 
    - upewnić się że obecny branch to <i_nazwisko_dev>
    - skopiować lokalnie zmiany zdalnego mastera
            
            git fetch origin master --tags
    - skopiować do swojego brancha commit z tagiem "0.2"        
         
            git cherry-pick 0.2
    
    - utworzyć branch GIT na którym rozwiązne będzie zadanie

            git checkout -b <i_nazwisko_ex2>
    
#### 2. Serializacja klasy domenowej

W klasie `Todo`

- wygenerować z IDE settery i gettery
- zaimplementować interfejs `Serializable`
- wygenerować pole statyczne `serialVersionUID` (zgodnie z dokumentacją Serializable) 
     
#### 3. Statyczne repozytorium
     
Stworzyć klasę `TodoRepository` w pakiecie `persistance`. Klasa powinna zawierać   
 
- statyczne pole - mapę (id->Todo), która będzie bezpieczna wątkowo. 
Mapa symuluje tutaj tabelę bazy danych z kluczem na polu "id"
- metody statyczne, które pozwolą wykonywać na mapie operacje CRUD. 
Pamiętać o unikalności identyfikatorów!

#### 4. Serwis REST

Wystawić pod adresem

      http://localhost:8080/todos
      
serwis REST operujący na `TodoRepository` ze standardowym zestawem operacji:

  - pobranie wszystkich elementów Todo metodą http GET pod adresem `/todos` - zwraca listę obiektów Todo
  - dodanie nowego obiektu Todo do bazy metodą http POST pod adresem `/todos` - zwraca utworzone id obiektu
  - pobranie/edycja/usuwanie konkretnego elementu odpowiednimi metodami http pod adresem `/todos/{id_elementu`}

Elementy niech będą zwracane/wysyłane w domyślnym formacie JSON.
  
W rozwiązaniu wykorzystać adnotacje Springa: @RestController, @RequestMapping, @RequestBody, @PathVariable

Nie implementować obsługi wyjątków oraz statusów HTTP, na razie używamy domyślnych.

#### 5. Testowanie serwisu REST

Działanie metody GET można wytestować w przeglądarce wpisując odpowiedni adres URL zasobu

  - napełnić TodoRepository kilkoma przykładowymi wartościami, aby uruchomiona aplikacja zawierała po starcie te wartości
  - sprawdzić w przeglądarce działanie pobierania Todo pod adresami `/todos` oraz `/todos/id_elementu`
  
W celu przetestowania innych metod http najlepiej zainstalować klienta REST. 

  - W przypadku braku znajomości/preferencji takich klientów proponujemy dodatki do przeglądarek:
  
      - dla Chrome'a - Postman
      - dla Firefoxa - RestClient    
    
  - Przetestować wszystkie wystawione metody REST swojego serwisu zainstalowanym klientem.
  
      - sprawdzić status, headers oraz body zwracanych odpowiedzi http
      - aby serwis rozumiał POST/PUT z zawartością JSON należy dodać nagłówek (header) żądaniu http
      `"Content-Type":"application/json"`
      
#### 6. Zapisać własne zmiany
      
      git branch
      git commit -m "odpowiedni komentarz"  //może być więcej commitów
      git push -u origin <i_nazwisko_ex2>
      
  
  
  