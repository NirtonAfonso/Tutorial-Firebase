# 3. Tratando erros sem travar a urna

[Voltar ao roteiro](../README.md) · [Aula anterior](02-swing.md) · [Próxima aula: Firestore](04-firestore.md)

Imaginem que alguém digitou `99`. Se esse número não estiver cadastrado, a urna sabe exatamente o que fazer: mostra o aviso e só registra o voto como nulo se a pessoa confirmar. Agora imaginem que o Firestore parou de responder depois que a pessoa apertou CONFIRMA. Aí a urna precisa avisar que não recebeu a confirmação do banco. São situações diferentes e pedem respostas diferentes.

## O que o Java oferece

| Recurso | Como aparece na prática |
| --- | --- |
| `try` e `catch` | Tentem abrir um arquivo. Se ele não puder ser lido, tratem a `IOException`. |
| `throw` | Lancem uma exceção quando o método receber algo que ele não consegue aceitar. |
| `throws` | Avisem na assinatura que a falha pode chegar a quem chamou o método. |
| `finally` | Executem uma ação de limpeza mesmo quando houve falha. Por exemplo, restaurem um botão depois de uma operação. |
| `try (var recurso = ...)` | Fechem automaticamente arquivos ou outros recursos `AutoCloseable`, inclusive quando o bloco falha. |

`IOException` e a nossa `PersistenciaException` são verificadas pelo compilador. Quem chama um método que as declara precisa capturar ou declarar a exceção também. `IllegalArgumentException` é um exemplo de exceção não verificada. No roteiro usamos quando recebemos um número ou campo de candidato fora do formato esperado.

Experimentem este código numa classe com `main`, antes de mexer no banco.

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExemploLeitura {
    public static void main(String[] args) {
        try (var arquivo = Files.newBufferedReader(Path.of("candidatos.txt"))) {
            System.out.println(arquivo.readLine());
        } catch (IOException erro) {
            System.err.println("Não consegui abrir candidatos.txt na pasta do projeto.");
            System.err.println("Detalhe para depurar: " + erro.getClass().getSimpleName());
        }
    }
}
```

Rodem uma vez sem o arquivo. Depois criem `candidatos.txt` na pasta de execução e escrevam `07 - Júlia Horizonte`. Comparem os dois resultados. O erro é tratado no lugar em que temos contexto suficiente para orientar a pessoa.

## Como decidir o que mostrar

| Situação | Tratamento na urna |
| --- | --- |
| Faltam dígitos | Deixem CONFIRMA desabilitado. |
| O número completo não existe | Mostrem o aviso e, se confirmado, salvem como nulo. |
| O número existe e o PNG está ausente | Mostrem os dados, coloquem “Sem foto” e corrijam o arquivo antes da entrega. |
| A senha administrativa está errada | Mostrem uma mensagem e preservem a etapa atual da eleição. |
| O banco caiu ou demorou demais | Mantenham o voto a confirmar. Registrem o erro e deixem tentar o mesmo envio. |
| O `.env` está ausente | A urna conectada avisa na abertura. Confiram o diretório de execução antes de alterar o código. |

### Onde capturar os erros do Firestore

O tratamento na tela é o mesmo para quem está em Java comum e para quem está usando Maven. Na [conexão REST](05-firestore-sem-maven.md), o proxy trata a resposta HTTP, o tempo de espera e a interrupção da requisição. Na [conexão pelo Admin SDK](05-java-firebase.md), o proxy trata o resultado da operação enviada pelo SDK. Os dois entregam à tela uma `PersistenciaException` quando a gravação falha.

A [classe FirestoreProxy](../src/main/java/br/edu/urna/dados/FirestoreProxy.java) espera no máximo dez segundos pela resposta de cada chamada. Ela traduz a falha do SDK para [PersistenciaException](../src/main/java/br/edu/urna/dados/PersistenciaException.java), guardando a causa. Se a operação for interrompida, o código restaura o sinal com `Thread.currentThread().interrupt()` antes de propagar a falha.

No [PainelSwing](../src/main/java/br/edu/urna/swing/PainelSwing.java), o `SwingWorker` faz a gravação em `doInBackground()` e devolve o controle para a tela em `done()`. Se o grupo chamar o banco diretamente dentro de `actionPerformed`, a janela pode ficar sem responder enquanto espera. Em `done()`, `get()` sinaliza se o trabalho terminou com sucesso ou lançou uma exceção. O visor só avança depois do sucesso.

Se a internet cair depois de o banco ter recebido a gravação, o aplicativo pode não ter recebido a resposta. Por isso a tentativa seguinte preserva o **mesmo ID e o mesmo conteúdo**. O exemplo bloqueia a edição enquanto existe dúvida sobre esse envio. Esse cuidado protege a repetição dentro da sessão aberta. Para retomar uma confirmação após fechar o aplicativo, o grupo teria que guardar o ID da operação de forma durável e reconciliar o estado.

Evitem `catch (Exception e) {}`. Quando acontece alguma coisa, fica difícil descobrir se o programa salvou o voto. Exibam uma mensagem que faça sentido para quem está na urna e guardem a causa técnica no log. Antes de compartilhar um log ou print para pedir ajuda, retirem credenciais e informações locais sensíveis.

**Para praticar.** Na urna em memória, troquem temporariamente o nome de um PNG cadastrado. Na aula de Firebase, façam uma cópia local do `.env` fora do projeto e testem com um caminho de arquivo que não existe. Observem em qual ponto cada problema aparece e restaurem o caminho correto depois.

Referências [Exceções no Java](https://dev.java/learn/exceptions/) · [SwingWorker](https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html).
