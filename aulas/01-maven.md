# 1B. Colocando o projeto no Maven

[Voltar ao roteiro](../README.md) · [Projeto Java comum](00-java-comum.md) · [Próxima aula: Swing](02-swing.md)

Se o Java já rodava no seu computador e passou a dar erro quando você mandou o projeto para um colega, vocês provavelmente esbarraram no problema que o Maven ajuda a resolver. Lembram dos arquivos `.jar` adicionados manualmente na IDE? Com o Maven, o grupo declara as bibliotecas e suas versões no `pom.xml`. A IDE ou o Maven baixa as bibliotecas necessárias, inclusive as dependências que elas usam. Assim o grupo compila com as mesmas versões.

A migração é uma opção para o grupo. Se vocês já têm as telas em um projeto comum e querem seguir nele, façam a [outra trilha](00-java-comum.md). Para a prática com Maven, o Firestore usa o Admin SDK e o JavaFX entra pelo POM.

## Confiram o Java antes de mexer no projeto

```bash
java -version
javac -version
mvn -version
```

Neste roteiro, usem **JDK 21**. É o JDK que traz o `javac`. Em `mvn -version`, confiram também a linha `Java version`. Caso `mvn` esteja indisponível no terminal, vocês podem usar o Maven integrado ao IntelliJ. Se preferirem usar o terminal, sigam a [instalação oficial do Maven](https://maven.apache.org/install.html), incluam a pasta `bin` do Maven no `PATH` e abram outro terminal. Se o Maven apontar para um Java antigo, ajustem o `JAVA_HOME` para a pasta do JDK 21.

## Abrir o código que deixei no repositório

1. Baixem ou clonem o repositório e descompactem, se necessário.
2. No IntelliJ, usem **File → Open** e apontem para a pasta que contém `pom.xml`. Aceitem abrir como projeto Maven se aparecer essa escolha.
3. Em **File → Project Structure → Project**, escolham o JDK 21. Se o Java do Maven for outro, confiram **Settings → Build, Execution, Deployment → Build Tools → Maven → Runner → JRE**.
4. Abram **View → Tool Windows → Maven** e usem **Reload All Maven Projects** se a importação ainda estiver pendente. Na primeira vez, o download pode levar alguns minutos.
5. Na janela Maven, abram **Lifecycle** e executem **compile**. Em seguida, abram [UrnaSwing.java](../src/main/java/br/edu/urna/swing/UrnaSwing.java) e usem o triângulo verde ao lado do `main`. No terminal, os equivalentes são `mvn clean compile` e `mvn exec:java`.

![Janela Maven do IntelliJ com os objetivos de compilação](https://resources.jetbrains.com/help/img/idea/2026.3/maven_structure.png)

Imagem da [documentação do IntelliJ IDEA](https://www.jetbrains.com/help/idea/convert-a-regular-project-into-a-maven-project.html). Reparem em **Lifecycle** na janela **Maven**.

No NetBeans, usem **File → Open Project** na pasta do `pom.xml`. Deixem a IDE carregar as dependências. Para executar a tela, selecionem `UrnaSwing.java` e usem **Run File**, ou configurem essa classe como a classe principal do projeto.

## Criar um projeto Maven do zero

**IntelliJ**, que usamos em aula. Entrem em **File → New → Project**, escolham **Java**, marquem **Maven** em **Build system** e selecionem o JDK 21. Usem um nome como `urna-grupo`. Se a IDE mostrar **Advanced Settings**, preencham `GroupId` com `br.edu.urna` e `ArtifactId` com `urna-grupo`.

**NetBeans**. Em **File → New Project**, escolham **Java with Maven → Java Application**. Preencham nome, `Group Id` e pasta. Se surgir uma classe de exemplo, rodem o `main` com um `System.out.println("Projeto funcionando")` antes de adicionar o Firebase.

No `pom.xml`, `groupId` identifica o grupo, `artifactId` identifica o projeto e `version` identifica a versão. O trecho abaixo fixa a versão do Java e a codificação do código. Copiem para dentro de `<project>`, aproveitando `<properties>` se já existir.

```xml
<properties>
    <maven.compiler.release>21</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

As bibliotecas entram em `<dependencies>`. Por exemplo, o Admin SDK usa as coordenadas `com.google.firebase:firebase-admin`. Consultem o [pom.xml deste roteiro](../pom.xml) para ver o bloco completo com a versão. Swing faz parte do JDK. Para usar Swing em um projeto Maven, nenhum JAR de Swing precisa entrar no POM. JavaFX tem dependências próprias, apresentadas na aula 6.

## Migrar o projeto Java que já está pronto

Minha sugestão para quem já fez várias telas é criar um Maven novo ao lado do projeto antigo e copiar aos poucos. Se já estiverem usando Git, façam um commit do ponto em que tudo rodava e trabalhem em uma branch de migração. Os dois caminhos dão um ponto de volta.

1. No projeto antigo, rodem o programa e anotem a classe que tem o `main`, os JARs adicionados à IDE e os caminhos das imagens.
2. Criem o Maven como explicado acima. Se preferirem migrar na mesma pasta, criem `pom.xml` na raiz e selecionem **Load Maven Project** no IntelliJ.
3. Copiem primeiro as classes de domínio e depois as telas, mantendo o pacote. Uma classe com `package br.edu.urna.tela;` vai em `src/main/java/br/edu/urna/tela/`. Evitem deixar duas cópias de uma mesma classe dentro das pastas de código.
4. Movam arquivos de recurso, como PNG e ícones, para `src/main/resources`. Imagem em `src/main/resources/fotos/julia.png` pode ser aberta com `MinhaTela.class.getResource("/fotos/julia.png")`. Ajustem trechos como `new ImageIcon("C:/...")`.
5. Quem já usa o GUI Builder do NetBeans precisa copiar o `.java` **e** o `.form` juntos para a pasta do pacote. O `.form` guarda o desenho do formulário. Se houver `org.netbeans.lib.awtextra.AbsoluteLayout` ou `org.jdesktop.layout` nos imports, identifiquem e declarem a biblioteca do projeto antigo ou troquem o layout de forma controlada. Os exemplos deste repositório usam layouts do próprio JDK.
6. Procurem o `groupId`, `artifactId` e a versão de cada JAR antigo no [Maven Central](https://central.sonatype.com/). Declarem essas bibliotecas no POM e removam as referências manuais da IDE depois de conferir que o Maven as resolveu. Para um JAR interno sem publicação, combinem uma forma de distribuição com o grupo antes de remover a cópia antiga.
7. Rodem `mvn clean compile`. Executem o mesmo `main` e repitam um fluxo que já funcionava, incluindo foto e botões. Só tragam mais dependências ou façam a integração com o Firebase quando essa etapa passar. Façam um commit da migração antes da próxima alteração.

| No projeto Maven | Coloque aqui |
| --- | --- |
| `src/main/java` | Classes Java, em suas pastas de pacote. |
| `src/main/resources` | Fotos, ícones, FXML e outros recursos usados em tempo de execução. |
| `src/test/java` | Testes Java, se o grupo já tiver. |
| `pom.xml` | JDK, dependências e plugins. |
| `target` | Arquivos gerados. O Maven pode recriar a pasta. |
| `.env` | Configuração local na raiz. A chave JSON fica fora do repositório. |

![Estrutura Maven de exemplo no IntelliJ](https://resources.jetbrains.com/help/img/idea/2026.3/maven_support_generated_architecture.png)

Imagem da [documentação do IntelliJ sobre migração](https://www.jetbrains.com/help/idea/convert-a-regular-project-into-a-maven-project.html).

## Comandos que vocês vão usar

| Comando | Para que serve |
| --- | --- |
| `mvn clean compile` | Limpa `target` e compila as classes e os recursos principais. |
| `mvn test` | Compila e roda os testes que existirem no projeto. |
| `mvn package` | Passa pelas etapas anteriores e cria o JAR em `target`. |
| `mvn dependency:tree` | Mostra quais dependências entraram e de onde vieram. |
| `mvn exec:java` | Executa a classe principal configurada no POM deste roteiro. |

Um JAR criado por `mvn package` pode precisar das bibliotecas em separado para executar. Para as aulas, usem o `main` pela IDE ou o comando indicado em cada página.

| Erro | Primeira coisa a conferir |
| --- | --- |
| `release version 21 not supported` | O Maven ou a IDE pode estar usando outro JDK. |
| `The goal you specified requires a project` | O terminal precisa estar na pasta com `pom.xml`. |
| `package ... does not exist` | Recarreguem o Maven. Confiram as dependências e seus imports. |
| Rodava antes e parou de carregar a foto | Confiram `target/classes/fotos` após compilar, a extensão e as maiúsculas do nome. |
| `Duplicate class` ou erro em tempo de execução depois de migrar | Procurem cópias de classes e JARs manuais antigos. |

**Antes de seguir.** Peçam a outro integrante para abrir o projeto e executar o `main` usando o mesmo `pom.xml`. Se funcionar nas duas máquinas, já dá para seguir para a tela.

Referências [Maven e o layout padrão](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html) · [Criando Maven no IntelliJ](https://www.jetbrains.com/help/idea/maven-support.html) · [Migrando no IntelliJ](https://www.jetbrains.com/help/idea/convert-a-regular-project-into-a-maven-project.html).
