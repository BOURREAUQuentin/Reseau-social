import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ServeurFX extends Application {

    private ServeurModele serveurModele;
    private TextArea serverLog;
    private TextArea finalServerLog;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.serveurModele = new ServeurModele(this);

        // Mise en place de l'interface graphique du serveur
        BorderPane root = new BorderPane();
        root.setPrefSize(400, 300);

        this.serverLog = new TextArea();
        this.serverLog.setEditable(false);
        this.serverLog.setWrapText(true);

        root.setCenter(this.serverLog);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Serveur Messagerie");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(this.serveurModele.closeServer());
        primaryStage.show();

        // Démarrer le serveur en arrière-plan
        new Thread(this.serveurModele.startServer()).start();
    }
}
