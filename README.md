# 🃏 Truco Gaúcho em Java

Implementação do **Truco Gaúcho** em Java puro, com testes JUnit 5 cobrindo as regras do jogo.

---

## 📁 Estrutura do Projeto

```
truco-gaucho/
├── pom.xml
└── src/
    ├── main/java/br/com/truco/
    │   ├── Main.java                        ← Simulação automática no console
    │   ├── model/
    │   │   ├── Carta.java                   ← Carta com hierarquia gaúcha
    │   │   ├── Naipe.java                   ← Enum de naipes
    │   │   ├── Baralho.java                 ← Deck de 40 cartas
    │   │   ├── Jogador.java                 ← Jogador com mão de cartas
    │   │   ├── Equipe.java                  ← Time A / Time B
    │   │   ├── EstadoTruco.java             ← Escalada: Truco→Seis→Nove→Doze
    │   │   ├── Mao.java                     ← Uma mão (até 3 rodadas)
    │   │   ├── Partida.java                 ← Placar até 12 pontos
    │   │   └── ResultadoRodada.java         ← Resultado de cada vaza
    │   ├── engine/
    │   │   ├── RodadaEngine.java            ← Compara cartas e resolve vaza
    │   │   └── TrucoEngine.java             ← Orquestra mão completa
    │   └── service/
    │       ├── DistribuidorService.java     ← Distribui cartas
    │       └── PlacarService.java           ← Controla o placar
    └── test/java/br/com/truco/
        ├── model/
        │   ├── CartaTest.java               ← 18 testes de hierarquia
        │   ├── BaralhoTest.java             ← 9 testes do deck
        │   ├── JogadorTest.java             ← 10 testes do jogador
        │   ├── MaoTest.java                 ← 12 testes de lógica da mão
        │   ├── PartidaTest.java             ← 8 testes do placar
        │   └── EstadoTrucoTest.java         ← 8 testes de escalada
        ├── engine/
        │   ├── RodadaEngineTest.java        ← 10 testes de resolução de vaza
        │   └── TrucoEngineTest.java         ← 10 testes de integração
        └── service/
            ├── DistribuidorServiceTest.java ← 5 testes de distribuição
            └── PlacarServiceTest.java       ← 6 testes de placar
```

---

## ⚙️ Pré-requisitos

| Ferramenta | Versão mínima | Download |
|------------|--------------|---------|
| Java JDK   | 17+          | https://adoptium.net |
| Maven      | 3.8+         | https://maven.apache.org/download.cgi |

### Verificar instalação

```bash
java -version    # deve mostrar 17 ou superior
mvn  -version    # deve mostrar 3.8 ou superior
```

---

## 🚀 Como executar

### 1. Clonar / extrair o projeto

```bash
# Se baixou o ZIP:
unzip truco-gaucho.zip
cd truco-gaucho

# Se clonou do Git:
git clone <https://github.com/robissu/Truco-Gauderio>
cd truco-gaucho
```

### 2. Compilar o projeto

```bash
mvn compile
```

### 3. Rodar a simulação no console

```bash
chcp 65001
mvn exec:java "-Dexec.mainClass=br.com.truco.Main"
```

Ou gerar o JAR e executar:

```bash
mvn package -DskipTests
java -jar target/truco-gaucho-1.0.0.jar
```

**Exemplo de saída:**
```
╔══════════════════════════════════╗
║     TRUCO GAÚCHO - Simulação     ║
╚══════════════════════════════════╝

━━━ MÃO 1 ━━━
  Rodada 1: Vitória de Gaúcho [4 de Paus [força:14] vs 3 de Copas [força:10]]
  Rodada 2: Vitória de Gaúcho [7 de Copas [força:13] vs 2 de Ouros [força:9]]
  Resultado: Time A venceu a mão (1 ponto(s))
  Placar: Time A 1 x 0 Time B
...
🏆 Vencedor: Time A
   Placar final: Time A 12 x 7 Time B
```

---

## 🧪 Como executar os testes

### Rodar todos os testes

```bash
mvn test
```

### Rodar uma classe de teste específica

```bash
mvn test -Dtest=CartaTest
mvn test -Dtest=RodadaEngineTest
mvn test -Dtest=MaoTest
```

### Rodar um método de teste específico

```bash
mvn test -Dtest="CartaTest#zapDeveSerMaisForte"
```

### Rodar testes por pacote

```bash
mvn test -Dtest="br.com.truco.model.*"
mvn test -Dtest="br.com.truco.engine.*"
```

### Gerar relatório de cobertura (Surefire)

```bash
mvn test
# Relatório em: target/surefire-reports/
```

---

## 📋 Resumo dos Testes

| Classe de Teste | Testes | O que cobre |
|----------------|--------|-------------|
| `CartaTest` | 18 | Hierarquia das 4 manilhas, cartas normais, equals/hashCode |
| `BaralhoTest` | 9 | 40 cartas sem duplicatas, distribuição, reinício |
| `JogadorTest` | 10 | Receber cartas, jogar por índice, melhor/pior carta, proteções |
| `MaoTest` | 12 | Vitória 2-0, empate+vitória, 1x1 na 3ª, truco, encerramento |
| `PartidaTest` | 8 | Acumulação de pontos, encerramento em 12, proteções |
| `EstadoTrucoTest` | 8 | Escalada Truco→Seis→Nove→Doze, valores, isMaximo |
| `RodadaEngineTest` | 10 | Confrontos carta a carta, empates, todas as manilhas |
| `TrucoEngineTest` | 10 | Mão completa, rodadas manuais, processamento de truco |
| `DistribuidorServiceTest` | 5 | 3 cartas por jogador, sem repetição, novaRodada |
| `PlacarServiceTest` | 6 | Pontuação, string de placar, encerramento em 12 |
| **Total** | **~96** | |

---

## 🃏 Regras Implementadas

### Hierarquia de cartas (Truco Gaúcho)

```
Manilhas fixas (mais fortes):
  1º - 4 de Paus     (Zap)       → força 14
  2º - 7 de Copas    (Copas)     → força 13
  3º - Ás de Espadas (Espadilha) → força 12
  4º - 7 de Ouros    (Sete)      → força 11

Demais cartas (do mais forte ao mais fraco):
  3 → 2 → A → K → J → Q → 7 → 6 → 5 → 4
```

### Lógica da mão

- Vence quem ganhar **2 rodadas** de 3
- Empate na 1ª + vitória na 2ª → vence quem ganhou a 2ª
- Vitória na 1ª + empate na 2ª → vence quem ganhou a 1ª
- Empate em todas → vence o time com a "mão" (baralha)

### Escalada de truco

```
Sem truco (1pt) → Truco (2pts) → Seis (6pts) → Nove (9pts) → Doze (12pts)
```

### Fim de partida

- Quem chegar a **12 pontos** primeiro vence

---

## 🔧 IDEs recomendadas

**IntelliJ IDEA** (recomendado):
1. `File → Open` → selecionar a pasta `truco-gaucho`
2. IntelliJ detecta o `pom.xml` automaticamente
3. Rodar testes: clique direito na pasta `test` → `Run All Tests`

**VS Code**:
1. Instalar extensão **Extension Pack for Java**
2. `File → Open Folder` → selecionar `truco-gaucho`
3. Painel de testes aparece automaticamente na sidebar

**Eclipse**:
1. `File → Import → Maven → Existing Maven Projects`
2. Selecionar a pasta do projeto

---

## 🏗️ Arquitetura

```
┌─────────────────────────────────────────┐
│                  Main                    │  ← ponto de entrada
└──────────────────┬──────────────────────┘
                   │
        ┌──────────▼──────────┐
        │    TrucoEngine      │  ← orquestra a mão
        └──┬──────────────┬───┘
           │              │
  ┌────────▼────┐  ┌──────▼──────────┐
  │ RodadaEngine│  │DistribuidorSvc  │
  └────────┬────┘  └─────────────────┘
           │
  ┌────────▼───────────────────────┐
  │  Model: Carta, Jogador, Mao,   │
  │  Baralho, Partida, EstadoTruco │
  └────────────────────────────────┘
```
