# 1A. Começando com um projeto Java comum

[Voltar ao roteiro](../README.md) · [Próxima aula: Swing](02-swing.md)

Se vocês já têm um projeto Java aberto no IntelliJ ou no NetBeans, podem aproveitar o que fizeram. Nesta trilha, o JDK compila as classes. Swing já vem no JDK. Para acessar o Firestore, vamos acrescentar um JAR para ler e montar JSON. Quem fizer a aula complementar de JavaFX vai baixar também o SDK do JavaFX.

## 1. Confiram o JDK

No terminal, rodem os dois comandos.

```text
java -version
javac -version
```

Os dois devem apontar para o **JDK 21**. O `javac` é o compilador. Se apenas `java` funcionar, confiram a instalação e o `PATH`. No IntelliJ, confiram também **File → Project Structure → Project → SDK**. No NetBeans, vejam **Tools → Java Platforms** e a plataforma escolhida nas propriedades do projeto.

## 2. Abram uma janela pequena primeiro

Na raiz do repositório, executem o arquivo abaixo. O Java 21 consegue executar um arquivo-fonte diretamente.

```text
java exemplos/primeiro-botao/TeclaSwing.java
```

A janela abre com `_`. Ao clicar em **7**, o visor mostra `7`. Se estiverem fazendo pela IDE, também podem criar a classe [TeclaSwing.java](../exemplos/primeiro-botao/TeclaSwing.java) em um projeto comum e rodar o `main`.

**No IntelliJ, para criar um projeto do zero.** Vão em **File → New → Project → Java**, escolham **IntelliJ** em **Build system** e selecionem o JDK 21. Criem em uma pasta separada do repositório de exemplo. Assim o IntelliJ abre o projeto como Java comum.

**No NetBeans, para criar um projeto do zero.** Vão em **File → New Project → Java with Ant → Java Application**. Escolham o JDK 21. Se forem desenhar a janela, adicionem um **JFrame Form** no pacote da tela e usem a aba **Design**. O NetBeans guarda o desenho no `.form`. Mantenham esse arquivo junto da classe `.java`.

## 3. Executem a urna de treino sem banco

No terminal, a partir da pasta que tem o `README.md`:

**Windows · PowerShell**

```powershell
javac --release 21 -encoding UTF-8 -d out -sourcepath src/main/java src/main/java/br/edu/urna/swing/UrnaSwing.java
java -cp "out;src/main/resources" br.edu.urna.swing.UrnaSwing
```

**macOS ou Linux**

```bash
javac --release 21 -encoding UTF-8 -d out -sourcepath src/main/java src/main/java/br/edu/urna/swing/UrnaSwing.java
java -cp "out:src/main/resources" br.edu.urna.swing.UrnaSwing
```

O primeiro comando compila a entrada `UrnaSwing.java` e as classes que ela usa, procurando os fontes em `src/main/java`. A opção `-d out` define o destino dos arquivos `.class`. Já `-cp` indica onde o Java procura classes e recursos ao executar. O separador é `;` no Windows e `:` no macOS e no Linux. As aspas mantêm o classpath inteiro no mesmo argumento.

Digitem `0` e reparem no visor `0_`, ainda sem foto. Depois digitem `7`. Devem aparecer **Júlia Horizonte**, o partido LAB e o retrato. Apertem **CORRIGE** e confiram que o visor limpa. Experimentem também **BRANCO** e o número `99`.

![O visor Swing depois de completar 07](../imagens/swing-numero-completo.png)

## 4. Levem os exemplos para o projeto do grupo

Se forem rodar os exemplos na IDE ou aproveitar código na urna do grupo, usem esta ordem. Se já tiverem uma classe com a mesma responsabilidade, comparem com a do exemplo e adaptem os métodos aos poucos.

| Do repositório | O que copiar para o projeto Java comum |
| --- | --- |
| [dominio](../src/main/java/br/edu/urna/dominio) | `Candidato`, `Voto` e `PainelVotacao`. Mantenham em `src/br/edu/urna/dominio` ou na pasta equivalente de fontes. |
| [dados](../src/main/java/br/edu/urna/dados) | Comecem com `RepositorioUrna`, `RepositorioEmMemoria` e `PersistenciaException`. |
| [swing](../src/main/java/br/edu/urna/swing) | `UrnaSwing` e `PainelSwing`. Rodem o `main` de `UrnaSwing`. |
| [fotos](../src/main/resources/fotos) | Copiem a pasta `fotos` inteira. Ela precisa ficar na raiz dos recursos que entram no classpath. |
| [rest](../java-comum/src/br/edu/urna/rest) | Acrescentem este pacote quando chegarem ao Firestore pela API REST. |

A pasta de fontes começa antes do pacote. Por exemplo, o arquivo `src/br/edu/urna/swing/UrnaSwing.java` declara `package br.edu.urna.swing;`. Se o grupo usar outro pacote, ajustem também os `import` das classes que se comunicam.

Para os PNGs:

- **IntelliJ.** Criem uma pasta `recursos` ao lado de `src` e ponham as fotos em `recursos/fotos`. Cliquem com o botão direito em `recursos` e escolham **Mark Directory as → Resources Root**. Se tiverem aberto um projeto já existente, também podem conferir isso em **Project Structure → Modules → Sources**.
- **NetBeans com Ant.** Ponham a pasta `fotos` na raiz de fontes. No projeto típico, fica em `src/fotos`, ao lado de `src/br`. Confiram se a imagem aparece em **Source Packages** e se foi copiada para `build/classes/fotos` depois do build.

Em ambos, o código usa `getResource("/fotos/julia.png")`. O `/` inicial parte da raiz do classpath. Se mudar o nome, a extensão ou as maiúsculas de um arquivo, ajustem também o campo `foto` do cadastro.

### Como acrescentar um JAR quando precisarem

1. Baixem o JAR da fonte indicada na aula. Na prática do Firestore, será `gson-2.13.1.jar`. Criem uma pasta `lib` no projeto do grupo e guardem o arquivo ali. O arquivo `-sources.jar` traz os fontes para consulta. Para executar, usem o JAR sem esse sufixo.
2. No **IntelliJ**, abram **File → Project Structure → Modules → Dependencies → + → JARs or Directories**. Selecionem o JAR de `lib`, confirmem e mantenham o escopo **Compile**. Apenas copiar o arquivo para a pasta do projeto não o coloca no classpath da IDE.
3. No **NetBeans com Ant**, usem **Project Properties → Libraries** ou o nó **Libraries → Add JAR/Folder**. Adicionem o JAR ao **Classpath** de compilação. Se a opção de projeto mostrar caminhos separados para execução, confirmem que a biblioteca entra no classpath de execução também.
4. Rodem a classe novamente. Se `import com.google.gson...` continua vermelho, confiram o módulo em que a dependência foi adicionada. Se compila mas aparece `NoClassDefFoundError` ao rodar, confiram o classpath da configuração de execução.

## 5. Sigam para o próximo ponto em que precisam de ajuda

| Se vocês querem… | Sigam para… |
| --- | --- |
| Entender o teclado, a foto e os botões | [Swing](02-swing.md) |
| Entender como o programa reage a falhas | [Exceções](03-excecoes.md) |
| Criar o banco e ligar a urna Java comum | [Criar o Firestore](04-firestore.md) → [Firestore REST em Java comum](05-firestore-sem-maven.md) |
| Experimentar JavaFX com o SDK baixado à parte | [JavaFX, com Maven e sem Maven](06-javafx.md) |
| Decidir se vale migrar um projeto iniciado | [Maven e migração](01-maven.md) |

**Para praticar.** Façam a primeira execução em memória na máquina de outra pessoa do grupo. Se o código compilar mas faltar a foto, comparem os caminhos no classpath antes de mexer no botão ou no cadastro.

Referências [Criar um projeto Java no IntelliJ](https://www.jetbrains.com/help/idea/creating-and-running-your-first-java-application.html) · [Bibliotecas no IntelliJ](https://www.jetbrains.com/help/idea/library.html) · [Java no NetBeans](https://netbeans.apache.org/tutorial/main/kb/docs/java/quickstart/).
