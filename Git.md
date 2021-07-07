Git - typowe sytuacje
===================

### Zapisywanie

Za każdym razem przed wysłaniem swoich zmian na serwer (`git push`) należy
sprawdzić, na którym branchu się znajdujemy (`git branch`).
 
Zalecana sekwencja komend:

        git branch
        git commit <parametry>
        git push origin <nazwa_brancha_z_nazwiskiem>

`commit` wygodnie wykonuje się z poziomu IDE (dobre IDE pokazuje ostrzeżenia i błędy przed sfinalizowaniem commita)

**Nie wolno komitować do "mastera"!!.** Branch `"master"` w tym projekcie traktujemy jako "readonly" z treścią zadań
oraz przykładowymi rozwiązaniami, które są punktem wyjścia do kolejnych zadań.

### Konflikty

W przypadku wystąpienia konfliktu należy go rozwiązać, np. jednym ze sposobów:

a) ręcznie w konsoli [github_link](https://help.github.com/articles/resolving-a-merge-conflict-using-the-command-line/), 
[atlassian_link](https://confluence.atlassian.com/bitbucket/resolve-merge-conflicts-704414003.html)

b) z IDE - np. w Intellij: [link](https://www.jetbrains.com/help/idea/2017.1/resolving-conflicts.html)
      
   - RMC na pliku(plikach) z konfliktem -> "git" -> "resolve conflicts" i wyklikać w narzędziu
   - `git commit`  - aby zatwierdzić 
   
c) cofnąć (revert) lokalnie swoje zmiany przed cherry-pick:
   - `git cherry-pick --abort`    (jeśli to cherry-pick spowodował konflikt)
   - revert np. [w IntelliJ](https://stackoverflow.com/questions/26175661/intellij-git-revert-a-commit)
   - `git commit` dla zatwierdzenia revert 
   - użyć właściwej komendy, która doprowadziła do konfliktu

### Twardy reset

W przypadku "totalnego rozjazdu", gdy aplikacja nie działa, ciężko się połapać co jest źle i chcemy 
cofnąć wszystkie zmiany do określonego punktu, przydaje się komenda:

        git reset --hard <jakis_commit>
        
gdzie `<jakis_commit>` można określać jako
        
   - HEAD, HEAD~2
   - skrót (SHA) commita
   - nazwa taga
   
Uwaga!! Twardy reset to operacja niebezpieczna i należy jej używać w ostateczności i jedynie na branchach lokalnych.
Jeżeli coś jest wypushowane, nie powinno się tego wymazywać na serwerze. W takich przypadkach należy robić revert jako kolejny commit.

Operacja przywraca wszystkie pliki do stanu ze wskazanego commita, czyli kasuje wszystkie późniejsze zmiany.

### Schowek

Często przydatny jest lokalny schowek GITa, jeśli nie chcemy zmian commitować, ale chcemy mieć możliwość do nich wrócić. 

         git stash
         git <komendy_gita>
         git stash apply
         
powyższa sekwencja komend powoduje:
      
   - zachowanie lokalnych zmian w schowku
   - dalej sekwencja dowolnych operacji git (np. cherry-pick, checkout innego brancha/commita, reset)
   - przywrócenie zmian ze schowka w bieżącym miejscu
   
### Linki
 Oficjalna książka/tutorial online:
   - [po angielsku](https://git-scm.com/book/en/v2/)
   - [po polsku](https://git-scm.com/book/pl/v1/)
   
   Na stronach Atlasian
   - [spis tutoriali](https://www.atlassian.com/git/tutorials)
   - [cheatsheet (ściągawka)](https://www.atlassian.com/git/tutorials/atlassian-git-cheatsheet)
   
   Oraz tysiące wątków na stackoverflow :)