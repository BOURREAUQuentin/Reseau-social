import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurModele extends Application{ 

    private ServeurFX vueServeur;
    private PrintWriter writer;

    public ServeurModele(ServeurFX vueServeur){
        this.vueServeur = vueServeur;
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);

            //Platform.runLater(() -> finalServerLog.appendText("Le serveur est à l'écoute sur le port 12345\n"));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Platform.runLater(() -> finalServerLog.appendText("Client connecté au serveur !\n"));

                Writer writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;
                while ((message = reader.readLine()) != null) {
                    final String finalMessage = message;  // Copie finale
                    Platform.runLater(() -> finalServerLog.appendText("Client: " + finalMessage + "\n"));
                    writer.println(message);
                }

                Platform.runLater(() -> finalServerLog.appendText("Client déconnecté.\n"));
                writer.close();
                reader.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServer() {
        System.exit(0);
    }
}