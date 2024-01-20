import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.application.Platform;

public class PageAccueil {
    private Stage stage;
    private static Client client;
    private VBox clientsNonAbonnes = new VBox();
    private VBox clientsAbonnements = new VBox();
    private static VBox lesMessages = new VBox();
    private ScrollPane scrollPane = new ScrollPane();

    public PageAccueil(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
        this.clientsNonAbonnes.getChildren().addAll(new Label("Non abonnés"));
        clientsAbonnements.getChildren().addAll(new Label("Abonnements"));
    }

    public void showPageAccueil(){
        BorderPane borderPane = new BorderPane();
        TextField messageInput = new TextField();
        Button sendButton = new Button("Envoyer");
        sendButton.setOnAction(e -> {
            String message = messageInput.getText();
            if (message.isEmpty() || message.equals("") || message.isBlank()) {
                return;
            }
            client.envoyerMessage(message);
            messageInput.clear();
        });
        HBox messageBox = new HBox(messageInput, sendButton);
        messageBox.setSpacing(10);
        messageBox.setPadding(new Insets(10));
        borderPane.setBottom(messageBox);

        Scene scene = new Scene(borderPane, 850, 600);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createFriendDisplay(String friendName, Client client, boolean isAmi) {
        HBox interieur = new HBox();
        Label label = new Label(friendName);
        Button button = new Button(isAmi ? "Supprimer" : "Ajouter");
        button.setOnAction(e -> {
            try {
                handleFriendAction(friendName, client, isAmi);
                updateFriendDisplay(interieur, friendName, client, !isAmi);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        interieur.getChildren().addAll(label, button);
        return interieur;
    }

    private void handleFriendAction(String friendName, Client client, boolean isAmi) throws ClassNotFoundException {
        if (isAmi) {
            client.supprimerAbonnement(friendName);
        }
        else {
            client.ajouterAbonnement(friendName);
        }
    }

    private void updateFriendDisplay(HBox container, String friendName, Client client, boolean isAmi) {
        container.getChildren().clear();
        Label label = new Label(friendName);
        Button button = new Button();
        
        if (isAmi) {
            button.setText("Supprimer");
            button.setOnAction(e -> {
                try {
                    handleFriendAction(friendName, client, true);
                    updateFriendDisplay(container, friendName, client, false);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
            clientsAbonnements.getChildren().add(container);
            clientsNonAbonnes.getChildren().remove(container);
        } else {
            button.setText("Ajouter");
            button.setOnAction(e -> {
                try {
                    handleFriendAction(friendName, client, false);
                    updateFriendDisplay(container, friendName, client, true);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
            clientsNonAbonnes.getChildren().add(container);
            clientsAbonnements.getChildren().remove(container);
        }

        container.getChildren().addAll(label, button);
    }

    public void show() throws ClassNotFoundException {
        BorderPane borderPane = new BorderPane();
        // liste non amis
        // Affichage des non-amis
        for (String string : client.getClientsNonAbonnes()) {
            HBox interieur = createFriendDisplay(string, client, false);
            clientsNonAbonnes.getChildren().add(interieur);
        }
        borderPane.setLeft(clientsNonAbonnes);

        // Affichage des amis
        for (String string : client.getClientsAbonnes()) {
            HBox interieur = createFriendDisplay(string, client, true);
            clientsAbonnements.getChildren().add(interieur);
        }
        borderPane.setRight(clientsAbonnements);

        // Panel en bas pour écrire des messages
        TextField messageInput = new TextField();
        Button sendButton = new Button("Envoyer");
        sendButton.setOnAction(e -> {
            String message = messageInput.getText();
            if (message.isEmpty() || message.equals("") || message.isBlank()) {
                return;
            }
            client.envoyerMessage(message);
            messageInput.clear();
        });
        HBox messageBox = new HBox(messageInput, sendButton);
        messageBox.setSpacing(10);
        messageBox.setPadding(new Insets(10));
        borderPane.setBottom(messageBox);

        // Panel au centre avec la zone des messages

        scrollPane.setContent(lesMessages);
        borderPane.setCenter(scrollPane);

        Scene scene = new Scene(borderPane, 850, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void MettreAJourAffichage(String message){
        String[] partsLikes = message.split("\\|\\|\\|"); // partsLikes[0] = "///like", partsLikes[1] = date, partsLikes[2] = pseudo, partsLikes[3] = contenu
        String date = partsLikes[1].trim();
        String pseudo = partsLikes[2].trim();
        String contenu = "";
        for (int i = 3; i < partsLikes.length; i++) {
            contenu = partsLikes[i].trim();
        }
        for (Node node : lesMessages.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                for (Node child : hbox.getChildren()) {
                    if (child instanceof TextArea) {
                        TextArea marea = (TextArea) child;
                        if (marea.getText().contains(date) && marea.getText().contains(pseudo)) {
                            try {
                                marea.setText("Date : " + date + "\n" + "Pseudo : " + pseudo + "\n" + contenu + "\n" + "Nombre de like : " + LikeSQL.nbLikesMessage(MessageSQL.recupererMessage(date, pseudo, contenu).getIdMessage())+"\n");
                            }
                            catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void afficheMessage(String message) {
        // Déclarez un tableau pour stocker la valeur de nbLike
        int[] nbLikeArray = new int[2];
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (message.contains("///like")) {
                    MettreAJourAffichage(message);
                    
                }
                else{
                    String date="";
                    String pseudo="";
                    String contenu="";
                    String[] parts = message.split("\\|\\|\\|");
                    
                    if (parts.length <= 3) {
                        date = parts[0].trim();
                        pseudo = parts[1].trim();
                        contenu = "";
                        for (int i = 2; i < parts.length; i++) {
                            contenu = parts[i].trim();
                        }
                    }
                    Message m = null;
                    int userLikeMessage = 0;
                    try {
                        m = MessageSQL.recupererMessage(date, pseudo, contenu);
                        nbLikeArray[0] = LikeSQL.nbLikesMessage(m.getIdMessage());
                        userLikeMessage = LikeSQL.nbLikesMessageParUtilisateur(m.getIdMessage(), client.getIdUser());
        
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    HBox hbox = new HBox();
                    TextArea marea = new TextArea();
                    marea.setText("Date : " + date + "\n" + "Pseudo : " + pseudo + "\n" + contenu + "\n" + "Nombre de like : " + nbLikeArray[0] + "\n");
                    
                    marea.setEditable(false);
                    Button button = new Button("Like");
                    final String finalContenu = contenu;
                    final Message finalM = m;
                    final String finalPseudo = pseudo;
                    final String finalDate = date;
                    button.setOnAction(e -> {
                        try {
                            client.likeMessage(finalDate,finalPseudo, finalContenu);
                            button.setDisable(true);
                            nbLikeArray[0] = LikeSQL.nbLikesMessage(finalM.getIdMessage());
                            marea.setText("Date : " + finalDate + "\n" + "Pseudo : " + finalPseudo + "\n" + finalContenu + "\n" + "Nombre de like : " + nbLikeArray[0]);
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    });
                    Button button3 = null;
                    if (client.getPseudoClient().equals(pseudo)) {
                        button3 = new Button("Supprimer");
                        button3.setOnAction(e -> {
                            try {
                                System.out.println("Supprimer");
                                client.supprimerMessage(finalDate,finalPseudo, finalContenu);
                                
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }
                        });
                    }
                    if (userLikeMessage != 0) {
                        button.setDisable(true);
                    }
                    hbox.getChildren().addAll(marea, button);
                    if (button3 != null) {
                        hbox.getChildren().add(button3);
                    }
                    lesMessages.getChildren().add(0, hbox);

                }
            }
        });
    }

    public static void supprimerMessageEtMettreAjourAffichage(String Message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String[] partsMessage = Message.split("\\|\\|\\|");
                String date = partsMessage[1].trim();
                String pseudo = partsMessage[2].trim();
                HBox hboxASupprimer = null;
                for (Node node : lesMessages.getChildren()) {
                    if (node instanceof HBox) {
                        HBox hbox = (HBox) node;
                        for (Node child : hbox.getChildren()) {
                            if (child instanceof TextArea) {
                                TextArea marea = (TextArea) child;
                                if (marea.getText().contains(date) && marea.getText().contains(pseudo)) {
                                    hboxASupprimer=hbox;
                                }
                            }
                        }
                    }
                }
                if (hboxASupprimer!=null){
                    lesMessages.getChildren().remove(hboxASupprimer);
                }
            }
        }); 
    }

    public static void afficherPopUpBannissement() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bannissement");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez été banni. La connexion sera fermée.");
            alert.showAndWait();
    
            Platform.exit();
            try {
                UtilisateurSQL.supprimerUtilisateur(client.getPseudoClient());
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
    }
}
