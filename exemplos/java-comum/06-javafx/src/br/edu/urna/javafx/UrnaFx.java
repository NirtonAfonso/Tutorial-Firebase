package br.edu.urna.javafx;

import br.edu.urna.dados.RepositorioEmMemoria;
import br.edu.urna.dominio.PainelVotacao;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// A mesma regra de digitação, com outros componentes de tela.
public final class UrnaFx extends Application {
    private final RepositorioEmMemoria repositorio = new RepositorioEmMemoria();
    private final PainelVotacao votacao = new PainelVotacao(2, repositorio.listarCandidatos());
    private final Label numero = new Label();
    private final Label nome = new Label();
    private final Label partido = new Label();
    private final Label mensagem = new Label();
    private final ImageView foto = new ImageView();
    private final Button confirma = new Button("CONFIRMA");

    @Override
    public void start(Stage janela) {
        var teclado = new GridPane();
        teclado.setHgap(5);
        teclado.setVgap(5);
        for (int valor = 1; valor <= 9; valor++) {
            teclado.add(criarTecla(valor), (valor - 1) % 3, (valor - 1) / 3);
        }
        teclado.add(criarTecla(0), 1, 3);

        var branco = new Button("BRANCO");
        branco.setOnAction(evento -> {
            votacao.votarEmBranco();
            atualizarVisor("");
        });
        branco.setStyle("-fx-background-color: white;");
        var corrige = new Button("CORRIGE");
        corrige.setStyle("-fx-background-color: #ffb165;");
        confirma.setStyle("-fx-background-color: #8bdaa0;");
        corrige.setOnAction(evento -> {
            votacao.corrigir();
            atualizarVisor("");
        });
        confirma.setOnAction(evento -> {
            // A versão JavaFX deste laboratório grava somente em memória.
            repositorio.salvar(votacao.prepararConfirmacao());
            votacao.votoRegistrado();
            atualizarVisor("Voto registrado na memória.");
        });

        numero.setStyle("-fx-font-size: 30; -fx-font-family: monospace;");
        foto.setFitWidth(112);
        foto.setFitHeight(112);
        foto.setPreserveRatio(true);
        var visor = new VBox(8, new Label("Cargo de treino | Prefeito"), numero, nome, partido);
        visor.setMinWidth(235);
        nome.setWrapText(true);
        var ladoDireito = new VBox(8, teclado, new HBox(5, branco, corrige, confirma));
        var painel = new HBox(12, visor, foto, ladoDireito);
        painel.setAlignment(Pos.TOP_LEFT);
        var conteudo = new VBox(16, painel, mensagem);
        conteudo.setPadding(new Insets(16));
        janela.setTitle("Urna de treino | JavaFX");
        janela.setScene(new Scene(conteudo, 650, 285));
        atualizarVisor("");
        janela.show();
    }

    private Button criarTecla(int valor) {
        var tecla = new Button(Integer.toString(valor));
        tecla.setPrefWidth(48);
        tecla.setOnAction(evento -> {
            votacao.digitar(valor);
            atualizarVisor("");
        });
        return tecla;
    }

    private void atualizarVisor(String aviso) {
        numero.setText((votacao.numero() + "__").substring(0, 2));
        nome.setText("");
        partido.setText("");
        foto.setImage(null);
        var selecionado = votacao.candidato();
        if (selecionado.isPresent()) {
            var candidato = selecionado.get();
            nome.setText(candidato.nome());
            partido.setText("Partido " + candidato.partido());
            var recurso = getClass().getResource(candidato.caminhoDaFoto());
            if (recurso == null) {
                mensagem.setText("A foto não foi encontrada no projeto.");
            } else {
                foto.setImage(new Image(recurso.toExternalForm()));
                mensagem.setText(aviso);
            }
        } else {
            if (votacao.branco()) {
                nome.setText("Voto em branco");
            } else if (votacao.completo()) {
                nome.setText("Número inexistente. Confirme para votar nulo.");
            }
            mensagem.setText(aviso);
        }
        confirma.setDisable(!votacao.podeConfirmar());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
