# Urna eletrônica em Java · roteiro de POO

Pessoal, organizei o material para vocês conseguirem estudar por conta própria e continuar de onde pararam no projeto. Comecem com a urna em memória, façam a foto aparecer no visor e só depois liguem a tela ao banco. Testar uma coisa de cada vez ajuda a localizar os erros.

O roteiro vale tanto para a **extensão da manhã (Urna Cidadã)** quanto para a **AV3 da noite**. A interface pedida nos dois trabalhos é **Swing**. JavaFX tem uma prática complementar. As datas e instruções de cada turma estão na [página da entrega](aulas/07-trabalho.md).

**[Abrir a Wiki do tutorial](https://github.com/NirtonAfonso/Tutorial-Firebase/wiki)**

## Escolham o caminho que combina com o projeto de vocês

| | Projeto Java comum | Projeto Maven |
| --- | --- | --- |
| Para começar | [Preparar o projeto no IntelliJ ou NetBeans](aulas/00-java-comum.md) | [Abrir, criar ou migrar o projeto](aulas/01-maven.md) |
| Tela Swing | A mesma urna com JDK 21. | A mesma urna com JDK 21 e `pom.xml`. |
| Firestore | [API REST com um JAR do Gson](aulas/05-firestore-sem-maven.md). A conta de teste entra pelo Firebase Authentication e as regras do Firestore controlam o acesso. | [Admin SDK declarado no POM](aulas/05-java-firebase.md). O exemplo usa uma conta de serviço no projeto de laboratório. |
| Configuração de conexão | [Modelo de `.env` para Java comum](java-comum/.env.example) | [Modelo de `.env` para Maven](.env.example) |
| JavaFX | SDK baixado à parte. | Dependência baixada pelo Maven. |

Quem já montou a urna em um projeto comum pode continuar nele. Quem quiser migrar encontra o passo a passo na aula de Maven.

## Aulas para os dois caminhos

1. [Swing, teclado numérico e foto](aulas/02-swing.md). Testem o visor, os botões e a passagem do primeiro para o último dígito.
2. [Exceções e tratamento de erros](aulas/03-excecoes.md). Vejam como a tela reage a entradas inválidas, arquivos ausentes e falhas de rede.
3. [Criar o banco e cadastrar candidatos](aulas/04-firestore.md). A partir daí, sigam para o exemplo de Firestore da trilha escolhida.
4. [JavaFX, com Maven e sem Maven](aulas/06-javafx.md). Experimentem a prática complementar com a mesma lógica de votação.
5. [Adaptar para o trabalho e conferir a entrega](aulas/07-trabalho.md). Organizem os dois cargos, a zerésima, a abertura, o encerramento e os relatórios.

## Primeira execução

Baixem o repositório pelo **Code → Download ZIP**, descompactem e entrem na pasta que contém este README. Vocês também podem clonar com Git. Para abrir o material, usem a conta GitHub que recebeu acesso.

**Java comum**, primeira janela usando apenas o JDK 21:

```text
java exemplos/primeiro-botao/TeclaSwing.java
```

A tecla `7` muda o visor de `_` para `7`. Depois, [rodem a urna de treino sem Maven](aulas/00-java-comum.md#3-executem-a-urna-de-treino-sem-banco).

**Maven**, urna de treino em memória:

```bash
mvn clean compile exec:java
```

No IntelliJ também dá para abrir o [POM](pom.xml) e executar o `main` de [UrnaSwing](src/main/java/br/edu/urna/swing/UrnaSwing.java). O IntelliJ inclui um Maven que pode ser usado pela própria IDE.

Na urna de treino, apertem `0` e depois `7`. O visor mostra **Júlia Horizonte**, o partido LAB e a foto no segundo clique. Façam também um teste com **CORRIGE**, **BRANCO** e o número `99`.

![Exemplo Swing depois de completar 07](imagens/swing-numero-completo.png)

Cada exemplo trabalha um pedaço do projeto. A última aula indica como juntar os dois cargos, administrar a eleição e montar a apuração.

**Nirton Afonso** · Programação Orientada a Objetos
