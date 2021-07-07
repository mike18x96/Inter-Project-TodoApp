Zadanie 4 - JPA i Spring data
=========================

Celem zadania jest zastąpienie "statycznego symulatora bazy" prawdziwą bazą danych (trzymaną w pamięci), 
wykorzystanie mappera ORM oraz skorzystanie ze SpringData w celu łatwego dostępu do bazy z poziomu kodu

#### 1. Zaimportować aplikację w wersji startowej

- zaimportować w git bazową wersje aplikacji z tagiem "0.4"
  - upewnić się że obecny branch to <i_nazwisko_dev>
  - skopiować lokalnie zmiany zdalnego mastera

          git fetch origin master --tags
  - skopiować do swojego brancha commit z tagiem "0.4"

          git cherry-pick 0.4

  - utworzyć branch GIT na którym rozwiązne będzie zadanie

          git checkout -b <i_nazwisko_ex4>
      
#### 2. JPA, Spring data CRUD

- dodać do projektu zależność do `spring-boot-starter-data-jpa` 
- korzystając z adnotacji JPA przekształcić klasę `Todo` w encję
     - której będzie odpowiadała w bazie tabela `TODOS`
     - `id` będzie kluczem głównym, który będzie automatycznie inkrementowany      
- zdefiniować interfejs `TodoJpaRepository`, który rozszerzy odpowiednio `CrudRepository` ze SpringData
- wstrzyknąć przez konstruktor zdefiniowane repozytorium do `TodoRestController` - użyć @Autowired
- zmienić implementację metod kontrolera, aby teraz wykorzystywały wstrzyknięte repozytorium 
(nie zmieniać nagłówków metod, API pozostaje takie same)
- zmienić typ wyjątku wyrzucany w metodzie `get()` na standardowy z `javax.persistance` o tej samej nazwie. 
Usunąć zbędną klasę własnego wyjątku.
- przetestować działanie aplikacji klientem REST

#### 3. Bazy w pamięci

- sprawdzić w logach aplikacji jaka baza danych jest używana (wskazówka: hibernate dialect)
- dodać do projektu zależność do bazy h2
- sprawdzić dialect hibernate
- dodać zależność do hsqldb
- sprawdzić dialect hibernate
- zakomentować/usunąć zależność do h2
- sprawdzić dialect hibernate

Zanotować wnioski z tego zadania.

#### 4. SpringData, metody generowane z nazwy

Zmienić implementację wyszukiwania w klasie `SearchTodoRestController`, aby korzystała z bazy danych.

- dodać do interfejsu `TodoJpaRepository` metody automatycznie generowane z nazwy, 
które będą realizowały odpowiednie wyszukiwanie/wyszukiwania
- przekształcić kontroler wyszukiwania, aby działał poprawnie 
- przetestować działanie usługi klientem REST
- przekształcić metody wyszukujące generowane z nazwy w jedną metodę wyszukującą korzystacjąc z anotacji `@Query`
- przetestować działanie usługi klientem REST

Sposób rozwiązania zadania nie jest narzucony z góry. Można zdefiniować dowolnie wiele dodatkowych klas i metod.

#### 5. Zapisać własne zmiany 
      git branch
      git commit -m "odpowiedni komentarz"  //może być więcej commitów
      git push -u origin <i_nazwisko_ex4>
      

