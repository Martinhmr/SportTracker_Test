**Sport Tracker – Android Developer Assignment**

Předkládám vypracované řešení aplikace pro evidenci sportovních výkonů. Zadání jsem pojal jako ukázku moderního Android vývoje (standard roku 2026), takže v kódu nenajdete žádné XML layouty ani zastaralé postupy, ale čistý Jetpack Compose a reaktivní architekturu.

**Architektura a Tech Stack**

Aby byl projekt udržitelný a testovatelný, zvolil jsem Clean Architecture s rozdělením na vrstvy Data, Domain a Presentation (MVVM).

**Jazyk:** Kotlin.

**UI:** Jetpack Compose + Material 3. Deklarativní UI výrazně zjednodušuje responzivitu a podporu landscape/portrait módu.

**DI:** Hilt. Pro tento rozsah by stačil manuální DI, ale Hilt demonstruje připravenost na škálování projektu.

**Data Layer:**

**Local:** Room Database (pro okamžité načtení a offline režim).

**Remote:** Firebase Firestore (pro cloud zálohu).

**Repository:** Funguje jako "Single Source of Truth". Použil jsem Flow a operátor combine, který v reálném čase spojuje data z lokální DB a cloudu.

**Testing:** Unit testy pro ViewModel (Mockk, Coroutines Test) ověřující logiku filtrování a stavů.

**Designová rozhodnutí & UX**

Zadání kladlo důraz na logiku průchodu aplikací. Zvolil jsem následující schéma:

Start = Seznam výkonů:

Uživatelé sportovních aplikací chtějí primárně vidět svůj progres. Nutit je začínat na prázdném formuláři by bylo špatné UX.

Akce "Přidat" (FAB):

Otevírá formulář na nové vrstvě navigace. Po uložení se vrací zpět a uživatel vidí okamžitou zpětnou vazbu.

Mazání:

Implementoval jsem možnost smazání záznamu s potvrzovacím dialogem (Delete flow), aby uživatel měl kontrolu nad svými daty.

**Synchronizace a Offline-First**
Aplikace je stavěná tak, aby nečekala na síť.

**Okamžité zobrazení:** Repozitář okamžitě emituje lokální data. Remote data se "dopočtou" asynchronně a připojí se do seznamu, jakmile dorazí z Firebase.

**Filtrování:** Probíhá reaktivně ve ViewModelu nad streamem dat.

**Jak projekt spustit**

Projekt obsahuje konfigurační soubor google-services.json.

Stačí otevřít v Android Studiu a spustit (Build & Run).

Firestore databáze je nastavena v Test Mode (zápis povolen).

Testy lze spustit standardně přes SportListViewModelTest.

**Poznámka k testovacím datům**

Aplikace je napojena na sdílenou testovací instanci Firebase Firestore. Všichni uživatelé s touto verzí aplikace sdílí stejnou databázi.

V produkčním prostředí by byla implementována autentizace (Firebase Auth) a separace dat na základě userId.

Prosím, neukládejte citlivá data.

**Možná rozšíření** 

V rámci zachování rozumného rozsahu tohoto demo projektu jsem se soustředil primárně na architekturu datového toku, testovatelnost a "Happy Path". Pro plné produkční nasazení bych řešení rozšířil o následující body, kterých jsem si vědom.

**Process Death & State Restoration:**

Aktuálně se spoléhám na životní cyklus ViewModelu. Pro zachování stavu (např. rozepsaného formuláře) při usmrcení aplikace systémem (na pozadí) bych implementoval SavedStateHandle.

**Robustní Error Handling:**

Chybové stavy (výpadek sítě, chyba zápisu do Firestore) by měly být propagovány z Repozitáře do ViewModelu (např. pomocí Result wrapperu) a následně zobrazeny uživateli (Snackbar/Dialog) pomocí jednorázových událostí (Channel / SharedFlow).

**Design System:**

Extrakce barev, typografie a komponent do vlastního modulu pro konzistentní branding, nad rámec základního Material 3

Díky za čas