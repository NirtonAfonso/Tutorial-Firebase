# 6. Aula extra · a mesma tela com JavaFX

[Voltar ao roteiro](https://github.com/NirtonAfonso/Tutorial-Firebase/wiki) · [Firestore em Java comum](https://github.com/NirtonAfonso/Tutorial-Firebase/wiki/Firestore-sem-Maven) · [Firestore com Maven](https://github.com/NirtonAfonso/Tutorial-Firebase/wiki/Firestore-com-Maven) · [Próxima aula: checklist do trabalho](https://github.com/NirtonAfonso/Tutorial-Firebase/wiki/Roteiro-da-entrega)

**Arquivos desta prática** · [Exemplo Java comum](https://github.com/NirtonAfonso/Tutorial-Firebase/tree/main/exemplos/java-comum/06-javafx) · [Exemplo Maven](https://github.com/NirtonAfonso/Tutorial-Firebase/tree/main/exemplos/maven/06-javafx).

## O que é uma cena?

JavaFX é um conjunto de bibliotecas para construir interfaces. Quando a aplicação inicia, o runtime chama `start(Stage janela)`. O **Stage** representa a janela. A **Scene** reúne o conteúdo que aparece nela. Esse conteúdo forma uma árvore de **nós**: alguns contêm outros nós, como o `GridPane`, e outros representam um controle ou uma imagem. Um botão pode estar dentro da grade do teclado, que por sua vez está dentro do painel principal.

![Hierarquia de nós com classes concretas do JavaFX](https://docs.oracle.com/javafx/2/scenegraph/img/figure2.png)

Acompanhem os ramos da figura. Percorram agora `UrnaFx.java` e encontrem um controle, o painel que o contém e a linha que entrega a cena à janela. A figura apresenta a árvore de uma interface em uma edição anterior da documentação. Para configurar a versão usada no exercício, sigam as instruções de SDK ou Maven desta aula. [Figura da Oracle](https://docs.oracle.com/javafx/2/scenegraph/jfxpub-scenegraph.htm) · [Guia OpenJFX](https://openjfx.io/openjfx-docs/).

```mermaid
flowchart TD
    S["Stage"] --> C["Scene"]
    C --> V["VBox principal"]
    V --> H["HBox do conteúdo"]
    V --> M["Mensagem"]
    H --> P["Visor e retrato"]
    H --> T["Painel com teclado e ações"]
```

Em `setOnAction`, o botão recebe a reação ao clique. Na nossa tela ela atualiza o objeto `PainelVotacao` e repassa o estado para os componentes. `setImage(null)` retira o retrato anterior. Façam o teste `07 → CORRIGE → 9`: se a imagem de Júlia continuar no primeiro dígito da nova escolha, o visor está mostrando um estado antigo. Ao ajustar isso, vocês mexem na renderização da tela. A regra que decide se os dígitos estão completos continua no modelo.

JavaFX é outra forma de montar uma interface desktop em Java. Os enunciados da manhã e da noite pedem Swing na entrega. A prática desta aula serve para vocês experimentarem outra biblioteca e enxergarem o que dá para reaproveitar da lógica do projeto.

## Quem está com Maven

Usem o JDK 21 e abram o `pom.xml` de `exemplos/maven/06-javafx`. O Maven baixa `javafx-controls`. O `javafx-maven-plugin` indica qual classe abrir e prepara a execução. Se forem criar um projeto separado, copiem a dependência e o bloco do plugin com as versões do [POM deste repositório](https://github.com/NirtonAfonso/Tutorial-Firebase/blob/main/exemplos/maven/06-javafx/pom.xml). Ajustem `mainClass` para a classe de vocês.

```bash
mvn clean javafx:run
```

No IntelliJ, abram **View → Tool Windows → Maven** e procurem **Plugins → javafx → javafx:run**. Se a execução pelo triângulo verde disser `JavaFX runtime components are missing`, usem o objetivo Maven acima.

## Quem está com Java comum

### Baixem o SDK e confiram as pastas

Na [página oficial de download](https://gluonhq.com/products/javafx/), escolham o **JavaFX SDK 22**, a mesma versão do exemplo Maven. Selecionem o sistema e a arquitetura corretos: Windows x64, macOS Intel ou Apple Silicon, ou Linux de acordo com a máquina. Descompactem em uma pasta local. Confiram que o caminho termina em `lib` e que lá estão os JARs do JavaFX. Baixem o pacote **SDK**.

### Testem no terminal

Abram o terminal na pasta `exemplos/java-comum/06-javafx`. Ajustem a primeira linha para a pasta `lib` de vocês.

**Windows · PowerShell**

```powershell
$fx = "C:/ferramentas/javafx-sdk-22/lib"
javac --release 21 -encoding UTF-8 --module-path $fx --add-modules javafx.controls -d out -sourcepath src src/br/edu/urna/javafx/UrnaFx.java
java --module-path $fx --add-modules javafx.controls -cp "out;recursos" br.edu.urna.javafx.UrnaFx
```

**macOS ou Linux**

```bash
fx="/caminho/para/javafx-sdk-22/lib"
javac --release 21 -encoding UTF-8 --module-path "$fx" --add-modules javafx.controls -d out -sourcepath src src/br/edu/urna/javafx/UrnaFx.java
java --module-path "$fx" --add-modules javafx.controls -cp "out:recursos" br.edu.urna.javafx.UrnaFx
```

O `--module-path` indica onde o Java encontra os módulos do JavaFX. `--add-modules javafx.controls` habilita os controles e suas dependências. O `-cp` reúne as classes da urna e os recursos. O projeto de treino não tem `module-info.java` nem usa FXML.

### Rodem o mesmo exemplo pela IDE

1. Abram a pasta do exemplo Java comum. Marquem `src` como fontes e `recursos` como recursos. A classe principal e os pacotes usados pela tela estão incluídos.
2. No **IntelliJ**, abram **File → Project Structure → Libraries → + → Java** e selecionem `lib` do SDK do JavaFX. Vinculem a biblioteca ao módulo da aplicação e confirmem que aparece nas dependências.
3. Criem uma configuração do tipo **Application** em **Run → Edit Configurations**. Indiquem `br.edu.urna.javafx.UrnaFx` como **Main class**. Em **Modify options → Add VM options**, usem a linha abaixo com o caminho correto.
4. No **NetBeans com Ant**, adicionem os JARs da pasta `lib` em **Project Properties → Libraries** para a compilação. Em **Project Properties → Run**, selecionem a classe principal e preencham **VM Options** com a mesma linha. O layout das opções pode variar conforme a versão da IDE.

```text
--module-path "C:/ferramentas/javafx-sdk-22/lib" --add-modules javafx.controls
```

As opções acima são da JVM. O campo **Program arguments** é repassado ao `main` e não serve para configurar o JavaFX. Se a IDE continuar usando outra configuração ao rodar um arquivo isolado, rodem a configuração do projeto que acabaram de criar.

**Resultado esperado.** A janela abre com o cargo e o teclado. Primeiro apertem `0`. Depois apertem `7` e confiram nome, partido e imagem. Usem **CORRIGE** para reiniciar a digitação.

## Comparar os componentes

| Swing | JavaFX | Onde usamos |
| --- | --- | --- |
| `JFrame` | `Stage` | A janela. |
| Painel principal | `Scene` com um nó raiz | Conteúdo que entra na janela. |
| `JPanel` com `GridLayout` | `GridPane` | Teclado numérico. |
| `JPanel` com distribuição em linha/coluna | `HBox` / `VBox` | Separar visor e teclado, ou empilhar os dados. |
| `JLabel` com `ImageIcon` | `ImageView` | Exibir o retrato. |
| `addActionListener` | `setOnAction` | Executar o código quando alguém clica. |

Em [UrnaFx.java](https://github.com/NirtonAfonso/Tutorial-Firebase/blob/main/exemplos/java-comum/06-javafx/src/br/edu/urna/javafx/UrnaFx.java), comecem pelo `start(Stage janela)`. Ele cria o teclado, os componentes do visor, encaixa o layout em uma `Scene` e chama `janela.show()`. Para entender o lugar de cada tecla, observem que `GridPane.add` recebe **componente, coluna, linha**, nesta ordem. Os índices começam em zero.

```java
var tecla = new Button("7");
tecla.setOnAction(evento -> {
    votacao.digitar(7);
    atualizarVisor("");
});
teclado.add(tecla, 0, 2);
```

O método `atualizarVisor` começa com `foto.setImage(null)`. Se a digitação estiver completa e o número existir, ele abre o PNG de `resources` e faz `foto.setImage(new Image(recurso.toExternalForm()))`. O objeto `PainelVotacao` continua o mesmo da aula Swing. Digitem `07`, usem `CORRIGE` e depois digitem `99` para comparar.

![Exemplo JavaFX depois de completar 07](https://github.com/NirtonAfonso/Tutorial-Firebase/blob/main/imagens/javafx-numero-completo.png?raw=true)

## Se quiserem praticar um pouco mais

Troquem a posição do visor com a do teclado no `HBox`. Depois, ajustem a grade ou experimentem um estilo CSS básico. Vejam que vocês conseguem mudar a aparência sem alterar a regra de quando o candidato aparece.

A urna JavaFX desta aula grava em memória. Se quiserem adaptar o exercício para Firestore por conta própria, carreguem a lista de candidatos antes de mostrar a tela e usem `Task` em uma thread de trabalho para salvar. Atualizem os componentes em `setOnSucceeded` e `setOnFailed`. Mantenham CONFIRMA desabilitado enquanto salva e preservem o ID da tentativa quando não houver resposta. A versão Swing da aula anterior já demonstra esse fluxo com `SwingWorker`.

| Se aparecer | Confiram |
| --- | --- |
| `package javafx... does not exist` | Com Maven, confiram o POM. No projeto comum, confiram os JARs do SDK na biblioteca de compilação. |
| `JavaFX runtime components are missing` | Com Maven, rodem `mvn javafx:run`. No projeto comum, confiram as opções da JVM e a pasta `lib` do SDK. |
| A janela abre, mas não aparece a foto | O nome do PNG e o caminho em `resources` coincidem? |
| A janela trava depois de ligar o banco | Uma chamada de rede pode ter ficado dentro do evento do botão. |

Referências [OpenJFX com Maven e SDK](https://openjfx.io/openjfx-docs/) · [Download oficial](https://gluonhq.com/products/javafx/) · [Task na API JavaFX 22](https://openjfx.io/javadoc/22/javafx.graphics/javafx/concurrent/Task.html).
