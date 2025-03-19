Odkaz na video: https://youtu.be/J24VXycysmU

Block Puzzle je konzolová hra, v ktorej hráč vypĺňa hraciu plochu 10×10 rôznymi tvarmi (blokmi). 
V každom ťahu môže hráč vybrať jeden z troch dostupných blokov, otočiť ho a umiestniť na plochu. 
Cieľom je zapĺňať celé riadky alebo stĺpce, získavať body a uvoľňovať miesto.
Existuje aj mechanika ťahov, ktorá funguje takto:
 na začiatku hry máte k dispozícii 7 ťahov a vždy, keď zložíte stĺpec alebo riadok
 , máte k dispozícii ďalšie 2 ťahy.

Hlavné vlastnosti:
	•	Hracia plocha 10×10.
	•	Tri aktívne bloky naraz, ktoré je možné otáčať doľava/doprava.
	•	Po vyplnení riadku sa pripočítajú body.
	•	Ovládanie cez konzolové menu: príkazy rotate, select, exit a podobne.
	•	Ukladanie výsledkov (Score), komentárov (Comment) a hodnotení (Rating) do databázy PostgreSQL.
	•	Podpora JDBC služieb na zápis/čítanie tabuliek score, "comment", rating.
	•	Testy (JUnit 5) overujú logiku služieb a základné scenáre.

Ako hrať:
	1.	Po spustení ConsoleUI sa program spýta na meno hráča (pre záznam výsledkov).
	2.	Dostupné príkazy:
	•	rotate i left/right – otočenie bloku i (1..3).
	•	select i r c – umiestnenie bloku i na riadok r, stĺpec c.
	•	scores – zobrazenie najlepších výsledkov.
	•	comment <text> – pridanie komentára k aktuálnej hre.
	•	comments – zobrazenie najnovších komentárov.
	•	rate <1..5> – ohodnotenie hry.
	•	rating – zobrazenie priemerného hodnotenia.
	•	exit – ukončenie hry.
	3.	Po skončení hry sa výsledné body uložia do tabuľky score.

Systémové požiadavky:
	•	Java 17+.
	•	PostgreSQL (databáza gamestudio s tabuľkami score, "comment", rating).
	•	Pripojený ovládač org.postgresql:postgresql.