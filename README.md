# Tetris Android — progetto corretto

App Kotlin con interfaccia Android nativa, senza Jetpack Compose e senza dipendenze esterne.
Controlli: sinistra, rotazione, destra, discesa, caduta immediata, pausa, nuova partita.
Il motore gestisce collisioni, linee complete, punteggio e fine partita.

## GitHub
Carica **il contenuto della cartella App5fe80a** nella radice del repository (non la cartella contenitore).
Il workflow `.github/workflows/android.yml` compila `assembleDebug` e carica l'APK in Actions → Artifacts.
La Release viene pubblicata solo per tag `v*`.

## Stato delle verifiche
Controlli statici effettuati; **compilazione Gradle e test su dispositivo non eseguiti** in questo ambiente.
