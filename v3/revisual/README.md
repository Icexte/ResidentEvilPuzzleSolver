# RE Puzzle Solver v3

Aplicativo Android em Kotlin para consultar puzzles e checklists de missões da série Resident Evil.

## Recursos
- Pesquisa por nome, jogo, local, dificuldade e tipo.
- Filtros por RE0, RE1, RE2, RE3, Code Veronica, RE4, RE5, RE6, RE7 e Village.
- Abas de PUZZLES e MISSÕES.
- Passo a passo em janela própria.
- Dificuldade e status de progresso salvo no aparelho.
- Diagramas vetoriais originais no lugar de screenshots oficiais.
- GitHub Actions para gerar APK Debug.

## Build no Codespace
```bash
cd /workspaces/ResidentEvilPuzzleSolver
gradle :app:assembleDebug
```

APK:
`app/build/outputs/apk/debug/app-debug.apk`

## Observação
As soluções do app devem ser conferidas por jogo/versão quando existirem diferenças de campanha ou dificuldade. As fontes usadas nos registros são indicadas dentro de cada guia.
