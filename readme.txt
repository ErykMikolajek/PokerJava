Gra Poker zaimplementowana w bardzo podstawowy sposób.
Rozgrywka przebiega na zasadzie: losowanie 5 kart -> możliwa wymiana od 0 do 5 kart -> określenie kto wygrał (miał mocniejszy układ) -> rozpoczęcie kolejnej tury 

Program uruchamia się poprzez uruchomienie pliku Server.class, wpisaniu liczby graczy, a następnie
uruchomieniu takiej liczby instancji klasy Client.class jaką wpisaliśmy w pliku serwera.

Możliwe komunikaty serwera:
-"Ile graczy będzie grało" - odpowiedź: liczba od 2 do 10
-"Jesteś graczem numer: " numer gracza - brak konieczności odpowiedzi
-"Gra rozpoczęta z " liczba_gracz " graczami." - brak konieczności odpowiedzi
-"Twoje karty:" wypisanie 5 kart które posiada gracz - brak konieczności odpowiedzi
-"Najlepszy układ twoich kart:" wypisanie obliczonego najwyższego obecnie posiadanego układu kart - brak konieczności odpowiedzi
-"Wybierz, co chcesz zrobic: 1. Wymienic karty, 2. Spasowac" - odpowiedź: 1 lub 2
-"Podaj bez separatorow numery kart które chcesz wymienić: " - podajemy numery kart które chcemy wymienić, bez separatorów, np: 235, gdy chcemy wymienić karty nr 2, nr 3 i nr 5.
