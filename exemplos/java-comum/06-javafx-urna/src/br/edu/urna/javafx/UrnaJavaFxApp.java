/*
 * Exemplo 06 - Interface Grafica com JavaFX
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Demonstra a mesma urna simplificada usando JavaFX:
 * Stage (janela), Scene (cena), VBox/HBox/GridPane (layouts) e ImageView (foto).
 */
package br.edu.urna.javafx;

import br.edu.urna.javafx.model.Candidato;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

/**
 * Aplicacao JavaFX que demonstra a construcao do teclado da urna e exibicao de foto.
 */
public class UrnaJavaFxApp extends Application {

    private static final int QUANTIDADE_DIGITOS = 5;

    private List<Candidato> listaCandidatos;
    private String numeroDigitado;
    private boolean votoEmBranco;

    private Label rotuloNumero;
    private Label rotuloNome;
    private Label rotuloPartido;
    private Label rotuloMensagem;
    private ImageView visualizadorFoto;
    private Button botaoConfirmar;

    @Override
    public void init() {
        this.numeroDigitado = "";
        this.votoEmBranco = false;
        this.listaCandidatos = new ArrayList<Candidato>();
        this.listaCandidatos.add(new Candidato("91001", "Golfe", "PEsp", "golfe.png"));
        this.listaCandidatos.add(new Candidato("91002", "Beisebol", "PEsp", "beisebol.png"));
        this.listaCandidatos.add(new Candidato("92001", "Rock", "PMus", "rock.png"));
        this.listaCandidatos.add(new Candidato("93001", "Astronauta", "PProf", "astronauta.png"));
    }

    @Override
    public void start(Stage palcoPrincipal) {
        // --- LADO ESQUERDO: VISOR ---
        Label rotuloCargo = new Label("SEU VOTO PARA: VEREADOR");
        rotuloCargo.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        rotuloNumero = new Label("_____");
        rotuloNumero.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 30px; -fx-font-weight: bold;");

        rotuloNome = new Label("");
        rotuloNome.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        rotuloPartido = new Label("");
        rotuloPartido.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        VBox caixaDados = new VBox(8, rotuloCargo, rotuloNumero, rotuloNome, rotuloPartido);
        caixaDados.setMinWidth(240);

        visualizadorFoto = new ImageView();
        visualizadorFoto.setFitWidth(140);
        visualizadorFoto.setFitHeight(170);
        visualizadorFoto.setPreserveRatio(true);

        HBox painelVisor = new HBox(16, caixaDados, visualizadorFoto);
        painelVisor.setPadding(new Insets(16));
        painelVisor.setStyle("-fx-background-color: white; -fx-border-color: #b4bcc6; -fx-border-width: 2;");

        // --- LADO DIREITO: TECLADO ---
        GridPane gradeNumeros = new GridPane();
        gradeNumeros.setHgap(8);
        gradeNumeros.setVgap(8);
        gradeNumeros.setAlignment(Pos.CENTER);

        for (int i = 1; i <= 9; i++) {
            int coluna = (i - 1) % 3;
            int linha = (i - 1) / 3;
            gradeNumeros.add(criarBotaoNumero(i), coluna, linha);
        }
        gradeNumeros.add(criarBotaoNumero(0), 1, 3);

        Button botaoBranco = new Button("BRANCO");
        botaoBranco.setStyle("-fx-background-color: white; -fx-font-weight: bold;");
        botaoBranco.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evento) {
                if (numeroDigitado.isEmpty()) {
                    votoEmBranco = true;
                    atualizarVisor("Aperte CONFIRMA para votar em BRANCO ou CORRIGE.");
                }
            }
        });

        Button botaoCorrigir = new Button("CORRIGE");
        botaoCorrigir.setStyle("-fx-background-color: #f39c12; -fx-font-weight: bold;");
        botaoCorrigir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evento) {
                numeroDigitado = "";
                votoEmBranco = false;
                atualizarVisor("Voto corrigido.");
            }
        });

        botaoConfirmar = new Button("CONFIRMA");
        botaoConfirmar.setStyle("-fx-background-color: #27ae60; -fx-font-weight: bold;");
        botaoConfirmar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evento) {
                String resumo;
                if (votoEmBranco) {
                    resumo = "Voto em BRANCO confirmado (JavaFX)!";
                } else {
                    Candidato encontrado = buscarCandidato(numeroDigitado);
                    resumo = encontrado != null
                            ? "Voto confirmado para " + encontrado.getNome() + "!"
                            : "Voto NULO confirmado!";
                }
                numeroDigitado = "";
                votoEmBranco = false;
                atualizarVisor(resumo);
            }
        });

        HBox linhaAcoes = new HBox(8, botaoBranco, botaoCorrigir, botaoConfirmar);
        linhaAcoes.setAlignment(Pos.CENTER);

        VBox painelTeclado = new VBox(12, gradeNumeros, linhaAcoes);
        painelTeclado.setPadding(new Insets(14));
        painelTeclado.setStyle("-fx-background-color: #2d323a;");

        // --- LAYOUT RAIZ ---
        HBox corpo = new HBox(16, painelVisor, painelTeclado);
        rotuloMensagem = new Label("Digite 5 digitos (ex: 91001) ou aperte BRANCO.");
        rotuloMensagem.setStyle("-fx-font-weight: bold;");

        VBox raiz = new VBox(12, corpo, rotuloMensagem);
        raiz.setPadding(new Insets(16));
        raiz.setStyle("-fx-background-color: #ebeef2;");

        Scene cena = new Scene(raiz, 720, 340);
        palcoPrincipal.setTitle("Exemplo 06 - Urna Eletronica em JavaFX");
        palcoPrincipal.setScene(cena);
        atualizarVisor("Digite 5 digitos (ex: 91001) ou aperte BRANCO.");
        palcoPrincipal.show();
    }

    private Button criarBotaoNumero(final int valor) {
        Button botao = new Button(String.valueOf(valor));
        botao.setPrefSize(60, 42);
        botao.setStyle("-fx-background-color: #1e2228; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        botao.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evento) {
                if (!votoEmBranco && numeroDigitado.length() < QUANTIDADE_DIGITOS) {
                    numeroDigitado = numeroDigitado + valor;
                    atualizarVisor("");
                }
            }
        });
        return botao;
    }

    private Candidato buscarCandidato(String numero) {
        for (int i = 0; i < listaCandidatos.size(); i++) {
            if (listaCandidatos.get(i).getNumero().equals(numero)) {
                return listaCandidatos.get(i);
            }
        }
        return null;
    }

    private void atualizarVisor(String aviso) {
        rotuloNome.setText("");
        rotuloPartido.setText("");
        visualizadorFoto.setImage(null);

        StringBuilder mascara = new StringBuilder(numeroDigitado);
        while (mascara.length() < QUANTIDADE_DIGITOS) {
            mascara.append("_");
        }
        rotuloNumero.setText(votoEmBranco ? "BRANCO" : mascara.toString());

        if (votoEmBranco) {
            rotuloNome.setText("VOTO EM BRANCO");
            botaoConfirmar.setDisable(false);
        } else if (numeroDigitado.length() == QUANTIDADE_DIGITOS) {
            Candidato candidato = buscarCandidato(numeroDigitado);
            if (candidato != null) {
                rotuloNome.setText("Nome: " + candidato.getNome());
                rotuloPartido.setText("Partido: " + candidato.getPartido());
                URL url = getClass().getResource(candidato.getCaminhoFoto());
                if (url != null) {
                    visualizadorFoto.setImage(new Image(url.toExternalForm()));
                }
                aviso = "Aperte CONFIRMA ou CORRIGE.";
            } else {
                rotuloNome.setText("NUMERO ERRADO");
                rotuloPartido.setText("VOTO NULO");
                aviso = "Numero inexistente. Confirmar registrara voto NULO.";
            }
            botaoConfirmar.setDisable(false);
        } else {
            botaoConfirmar.setDisable(true);
        }

        rotuloMensagem.setText(aviso);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
