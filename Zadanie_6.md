Zadanie 6 - testy, testy, testy
=========================


#### 1. Zaimportować aplikację w wersji startowej

- zaimportować w git bazową wersje aplikacji z tagiem "0.6"
  - upewnić się że obecny branch to <i_nazwisko_dev>
  - skopiować lokalnie zmiany zdalnego mastera

          git fetch origin master --tags
  - skopiować do swojego brancha commit z tagiem "0.6"

          git cherry-pick 0.6

  - utworzyć branch GIT na którym rozwiązne będzie zadanie

          git checkout -b <i_nazwisko_ex6>

     
#### 2. Rozpoznanie

- przejrzeć rozwiązania zadań wewnątrz pakietu `main` 
- wyczyścić odpowiednie klasy z zakomentowanego kodu zastąpionego wywołaniami guavy
- zapoznać się ze wstępną wersją klasy `StandaloneTodoRestControllerTest`     

#### 3. Testowanie kontrolera - Standalone

Standalone setup tworzony w metodzie `setUp()` klasy testowej, jest najprostszym sposobem utworzenia testowej 
instancji kontrolera. Nie wymaga definiowania i konfigurowania testowej wersji kontekstu Springa, 
ponieważ nie podnosi pełnego kontestku aplikacji. 

W efekcie nie podnosi się pełna aplikacja w wersji testowej, a jedynie jej fragment ze zdefiniowanym jednym kontrolerem.
Dzięki temu takie testy są bliższe testom jednostkowym. [link](https://stackoverflow.com/questions/32223490/are-springs-mockmvc-used-for-unit-testing-or-integration-testing)

**Polecenia:**

- uruchomić testy, przejrzeć logi konsoli
- odkomentować linię `.andDo( ... )`, następnie powtórzyć poprzednie polecenie
- napisać pozostałe testy do wszystkich metod REST klasy `TodoRestController` - dla niektórych metod potrzebne będzie 
po kilka testów (np. PUT może skutkować stworzeniem nowego rekordu w bazie lub update'em już istniejącego)
- refaktor
     - zastosować statyczne importy dla metod matcherów i builderów, aby kod testów był krótszy
     - `applicationtJson` może być reużyte w wielu testach, a nawet w wielu klasach testowych - umożliwić 
     to tworząc odpowiednią stałą 
     
Informacja: w klasie `TodoAppApplicationTests` pozostawiony jest domyślny, wygenerowany przez SpringBoot test, 
który podnosi pełen kontekst Springa.

#### 4. Mockito

Na potrzeby naszych testów stworzyliśmy klasę pomocniczą z uproszczoną implementacją `TodoFakeRepositoryImpl`. 
Testy powinny sprawdzać pojedynczy element (najczęściej jedną metodę klasy), a więc zależności do innych niebanalnych
klas zastępujemy w ten sposób zależnościami do implementacji uproszczonych, których działanie możemy kontrolować w testach
i które na pewno zadziałają w sposób oczekiwany. Działanie tych zależnych klas powinno być przetestowane w testach dedykowanych tym klasom.

Innym sposobem dostarczania do testów obiektów zastępczych (różne rodzaje jak: _fake, stub, mock_ ) jest użycie
jednej z bibliotek do mockowania. W naszym projekcie skupimy się na _Mockito_ - jednej z najpopularniejszych i najczęściej
stosowanych.
          
W klasie `SearchTodoServiceTest` zastosowano kilka elementów biblioteki:

- konfigurację przy pomocy adnotacji @Mock, @InjectMocks oraz inicjowanie przez @ExtendWith(MockitoExtension.class)
- w testowanej metodzie konstrukcja  `Mockito.when().thenReturn()` definiuje zachowanie metody `findBySearchParams()`
  w tym konkretnym teście
- konstrukcja `Mockito.eq("nazwa")` wskazuje dopasowanie parametrów wywołania metody - w tym wypadku `findBySearchParams()`
  zwróci listę z pojedynczym elementem, jeśli parametrem jej wywołania będzie dokładnie `"nazwa"`. Inne ciekawe 
  dopasowana to np: `Mockito.any()`, `Mockito.anyXYZ()` (XYZ zastępujemy typem)
- linia z konstrukcją `Mockito.verify()` nie jest obowiązkowa w tym teście. Wprowadza ona dodatkową asercję:
  sprawdzenie czy na repozytorium metoda `findBySearchParams()` została zawołana dokładnie raz z dowolnymi parametrami. 
  Bardziej precyzyjne byłoby tutaj `eq("nazwa")`
- zakomentowana linia sprawdza brak innych interakcji z repozytorium. 
  Nie należy jej nadużywać, może prowadzić do powstania _kruchych testów_ - inna zmiana w testowanej klasie, spowoduje
  wysypanie się wielu testów. Optymalnie jeden test powinien sprawdzać jedną hipotezę (na hipotezę może składać się kilka asercji)!   
    
**Polecenia:**    

- przerobić pozostałe testy w klasie `SearchTodoServiceTest`, tak aby wykorzystywały Mockito
- w klasie `StandaloneTodoRestControllerTest`
     - dodać adnotację @Mock dla repozytorium, dodać anotację @ExtendWith(MockitoExtension.class) na klasę, zmienić typ repozytorium, 
        i usunąć niepotrzebny wpis z `setUp()`
     - przerobić testy tak aby korzystały teraz z Mockito
- zastanowić się czy lepiej używać Mockito, czy własnych "test doubles"        

#### 5. Zapisać własne zmiany

    git branch
      git commit -m "odpowiedni komentarz"  //może być więcej commitów
      git push -u origin <i_nazwisko_ex6>
   