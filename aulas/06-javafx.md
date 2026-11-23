# 6. Aula extra · a mesma tela com JavaFX

[Voltar ao roteiro](../README.md) · [Aula anterior](05-java-firebase.md) · [Próxima aula: checklist do trabalho](07-trabalho.md)

JavaFX é outra forma de montar uma interface desktop em Java. Os enunciados da manhã e da noite pedem Swing na entrega. A prática desta aula serve para vocês experimentarem outra biblioteca e enxergarem o que dá para reaproveitar da lógica do projeto.

## Abrir o exemplo no IntelliJ

Usem o JDK 21 e importem o mesmo `pom.xml`. O Maven baixa `javafx-controls`. No nosso projeto, o `javafx-maven-plugin` indica qual classe abrir e configura a execução do JavaFX. Se forem criar um projeto separado, copiem a dependência e o bloco do plugin, com as versões do [POM deste repositório](../pom.xml), e ajustem `mainClass` para o pacote da classe de vocês.

```bash
mvn clean javafx:run
```

No IntelliJ, também dá para abrir **View → Tool Windows → Maven**, expandir **Plugins → javafx** e executar **javafx:run**. Se rodarem pelo triângulo verde como se fosse um `main` comum e aparecer `JavaFX runtime components are missing`, executem o objetivo Maven acima. Este exemplo usa JavaFX 22 e está organizado como projeto sem `module-info.java`.

## Comparar os componentes

| Swing | JavaFX | Onde usamos |
| --- | --- | --- |
| `JFrame` | `Stage` | A janela. |
| Painel principal | `Scene` com um nó raiz | Conteúdo que entra na janela. |
| `JPanel` com `GridLayout` | `GridPane` | Teclado numérico. |
| `JPanel` com distribuição em linha/coluna | `HBox` / `VBox` | Separar visor e teclado, ou empilhar os dados. |
| `JLabel` com `ImageIcon` | `ImageView` | Exibir o retrato. |
| `addActionListener` | `setOnAction` | Executar o código quando alguém clica. |

Em [UrnaFx.java](../src/main/java/br/edu/urna/javafx/UrnaFx.java), comecem pelo `start(Stage janela)`. Ele cria o teclado, os componentes do visor, encaixa o layout em uma `Scene` e chama `janela.show()`. Para entender o lugar de cada tecla, observem que `GridPane.add` recebe **componente, coluna, linha**, nesta ordem. Os índices começam em zero.

```java
var tecla = new Button("7");
tecla.setOnAction(evento -> {
    votacao.digitar(7);
    atualizarVisor("");
});
teclado.add(tecla, 0, 2);
```

O método `atualizarVisor` começa com `foto.setImage(null)`. Se a digitação estiver completa e o número existir, ele abre o PNG de `resources` e faz `foto.setImage(new Image(recurso.toExternalForm()))`. O objeto `PainelVotacao` continua o mesmo da aula Swing. Digitem `07`, usem `CORRIGE` e depois digitem `99` para comparar.

![Exemplo JavaFX depois de completar 07](../imagens/javafx-numero-completo.png)

## Se quiserem praticar um pouco mais

Troquem a posição do visor com a do teclado no `HBox`. Depois, ajustem a grade ou experimentem um estilo CSS básico. Vejam que vocês conseguem mudar a aparência sem alterar a regra de quando o candidato aparece.

A urna JavaFX desta aula grava em memória. Se quiserem adaptar o exercício para Firestore por conta própria, carreguem a lista de candidatos antes de mostrar a tela e usem `Task` em uma thread de trabalho para salvar. Atualizem os componentes em `setOnSucceeded` e `setOnFailed`. Mantenham CONFIRMA desabilitado enquanto salva e preservem o ID da tentativa quando não houver resposta. A versão Swing da aula anterior já demonstra esse fluxo com `SwingWorker`.

| Se aparecer | Confiram |
| --- | --- |
| `package javafx... does not exist` | O POM foi importado? `javafx-controls` está resolvido? |
| `JavaFX runtime components are missing` | A execução foi feita com `mvn javafx:run`? |
| A janela abre, mas não aparece a foto | O nome do PNG e o caminho em `resources` coincidem? |
| A janela trava depois de ligar o banco | Uma chamada de rede pode ter ficado dentro do evento do botão. |

Referências [OpenJFX com Maven](https://openjfx.io/openjfx-docs/) · [Task na API JavaFX 22](https://openjfx.io/javadoc/22/javafx.graphics/javafx/concurrent/Task.html).
