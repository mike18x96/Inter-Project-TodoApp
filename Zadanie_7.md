Zadanie 7 - poprawa czytelności testów, użytkownicy
=========================


#### 1. Zaimportować aplikację w wersji startowej

- zaimportować w git bazową wersje aplikacji z tagiem "0.7"
  - upewnić się że obecny branch to <i_nazwisko_dev>
  - skopiować lokalnie zmiany zdalnego mastera

          git fetch origin master --tags
  - skopiować do swojego brancha commit z tagiem "0.7"

          git cherry-pick 0.7

  - zmergować własne zmiany w klasach testowych z pobranym z serwera commitem
  - utworzyć branch GIT na którym rozwiązne będzie zadanie

          git checkout -b <i_nazwisko_ex7>
      
#### 2. Refactor 

##### a) Builder, Jackson oraz klasa utils
Zapoznać się z następującymi elementami:

- adnotacja Lomboka @Builder() w klasie `Todo`
- klasą `TestJsonUtils`, której metoda `convertObjectToJson()` konwertuje dowolny obiekt do formatu JSON, 
pomijając pola puste. W implementacji została użyta popularna biblioteka do takich konwersji - _Jackson_ 
- zmianami w teście `StandaloneTodoRestControllerTest.post_elementWithoutName_returns400()`

##### b) AssertJ
W JUnit standardowe asercje bywają mało wygodne i mało czytelne (pytanie: czy najpierw jest wartość oczekiwana testu czy testowana?). 

Biblioteka AssertJ poprawia czytelność asercji i pozwala je pisać w sposób "fluent" z dodawaniem kolejnych sprawdzanych 
elementów "po kropce" - jako wywołania metod o czytelnych nagłówkach.

- W klasie `SearchTodoServiceTest` zaprezentowano przykładowe użycia asercji z AssertJ
- Warto wiedzieć, że przeważnie asercje zaczynamy od `assertThat()`, następnie IDE podpowiada kolejne możliwości po wpisaniu ".";
dostępne opcje zależą od typów badanych elementów

##### c) Polecenia
Zrefaktorować własne testy tam gdzie ma to sens

- używać AssertJ
- użycie builderów nie jest konieczne
- jak najbardziej można wprowadzać statyczne importy w miarę poznawania bibliotek
- zapisać rozwiązanie tego zadania w oddzielnym commicie

#### 3. Użytkownicy
W projekcie pojawiła się klasa `User`, która będzie reprezentować użytkowników systemu. Podobnie do klasy `Todo`

- przekształcić klasę `User` w klasę utrwalaną 
     - skorzystać z adnotacji @Enumerated w odpowiednim miejscu
     - login nie może być pusty
     - hasło musi zawierać co najmniej 5 znaków
     - rola nie może być pusta
- stworzyć repozytorium Spring Data dla klasy `User` (identyfikatorem encji jest login)
- stworzyć kontroler z jedną metodą, która pozwoli wyświetlić wszystkich użytkowników systemu: login i rola, ale bez hasła
- napisać test(y) kontrolera

#### 4. Zapisać własne zmiany

    git branch
      git commit -m "odpowiedni komentarz"  //może być więcej commitów
      git push -u origin <i_nazwisko_ex7>

   