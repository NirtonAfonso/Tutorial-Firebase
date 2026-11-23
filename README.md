# Urna eletrônica em Java · roteiro de POO

Pessoal, como não teremos mais encontros para trabalhar esses assuntos, organizei este roteiro para vocês conseguirem seguir por conta própria. Abram o projeto, rodem um exemplo por vez e façam as pequenas mudanças propostas. Se der erro, confiram primeiro o resultado esperado da etapa em que vocês estão. Assim fica mais fácil saber se o problema está no Java, na tela ou na conexão com o Firebase.

Este material serve tanto para o **projeto de extensão da manhã (Urna Cidadã)** quanto para a **AV3 da noite**. A parte técnica é a mesma. Nos dois enunciados, a interface da entrega é em **Java Swing**. Quem quiser conhecer JavaFX tem uma aula extra com a mesma lógica de votação. A tabela com as orientações de cada turma está na [página do trabalho](aulas/07-trabalho.md).

## Comecem por aqui

| Aula | O que vocês vão conseguir fazer |
| --- | --- |
| [1. Maven e migração do projeto](aulas/01-maven.md) | Abrir o exemplo no IntelliJ, criar um Maven do zero ou trazer o código já feito. |
| [2. Uma urna básica com Swing](aulas/02-swing.md) | Montar visor e teclado, usar o GUI Builder do NetBeans se quiserem e mostrar a foto após o último dígito. |
| [3. Exceções e tratamento de erros](aulas/03-excecoes.md) | Separar erro de digitação, foto ausente e falha de conexão. |
| [4. Criando o Cloud Firestore](aulas/04-firestore.md) | Criar um projeto de laboratório, cadastrar candidatos fictícios e configurar o `.env`. |
| [5. Java, Firebase e a classe Proxy](aulas/05-java-firebase.md) | Ler documentos, salvar um voto de teste e ligar a tela Swing ao banco. |
| [6. Experimentando JavaFX](aulas/06-javafx.md) | Montar a tela com `Stage`, `Scene` e `GridPane` e reaproveitar a regra de digitação. |
| [7. Adaptando para o trabalho](aulas/07-trabalho.md) | Organizar os dois cargos, a abertura, a zerésima, a apuração e a entrega da sua turma. |

## Para rodar a primeira vez

Com **JDK 21** e o Maven, abram a pasta que tem o [pom.xml](pom.xml). Se estiverem no IntelliJ, podem usar o Maven que já vem na IDE. Na [primeira aula](aulas/01-maven.md) mostro como rodar pelo menu e pelo terminal.

```bash
git clone https://github.com/NirtonAfonso/Tutorial-Firebase.git
cd Tutorial-Firebase
mvn clean compile
mvn exec:java
```

Também dá para baixar em **Code → Download ZIP**, descompactar e abrir o `pom.xml`. Enquanto o repositório estiver privado, usem a conta GitHub que recebeu acesso.

Na janela Swing, digitem `0`, esperem um instante e depois apertem `7`. O visor mostra **Júlia Horizonte** e a foto só no segundo clique. Experimentem `CORRIGE`, `BRANCO` e o número `99`. Essa primeira tela grava em memória. O exemplo conectado ao Firestore está na aula 5.

![Exemplo Swing após digitar 07](imagens/swing-numero-completo.png)

Cada exemplo trabalha um pedaço do projeto. Os grupos vão implementar a passagem entre os dois cargos, a administração e os relatórios usando as orientações da última aula.

**Nirton Afonso** · Programação Orientada a Objetos
