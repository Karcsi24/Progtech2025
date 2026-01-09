# ❌⭕ Amőba Játék

Ez egy Java nyelven írt, konzolos felületű Amőba játék, amely Maven alapokon nyugszik. A projekt célja a klasszikus "ötöt egy sorba" játék megvalósítása picit átgondolva, objektumorientált elvek (OOP) alkalmazásával, adatbázis-integrációval és naplózással.

## 🚀 Funkciók

* **Klasszikus játékmenet:** Egy játékos játszhat a (AI) számítógép ellen.
* **Állapotmentés:** A játék képes elmenteni és betölteni a játékállást vagy eredményeket (H2 beépített adatbázis használatával).
* **Naplózás:** Részletes hibakeresési és működési naplók készítése `.log` fájlba és konzolra (Logback segítségével).
* **Input kezelés:** Validált koordináta-megadás.

## 🛠️ Technológiák

A projekt a következő technológiákat és könyvtárakat használja:

* **Nyelv:** Java (JDK 11+)
* **Build eszköz:** Maven
* **Adatbázis:** H2 Database (Beágyazott/Fájl alapú)
* **Naplózás:** SLF4J / Logback
* **Tesztelés:** JUnit 5, Mockito

## 📂 Projekt Struktúra

```text
Amoba/
├── src/
│   ├── main/java/game/
│   │   ├── data/       # Adatbázis kezelés (DataHandler)
│   │   ├── logic/      # Játék logika (GameEngine)
│   │   ├── model/      # Adatmodellek (Board, Symbol, Coordinate)
│   │   └── ui/         # Felhasználói felület (ConsoleInterface)
│   └── test/           # Unit tesztek
├── target/             # Fordított fájlok (automatikusan generált)
├── amoba_db.mv.db      # H2 adatbázis fájl
├── amoba_log.txt       # Log fájl
└── pom.xml             # Maven konfiguráció
