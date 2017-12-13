package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;

@ClientEndpoint
public class Controller implements Initializable {

    @FXML
    Button buttonSend;

    private Session session;
    private WebSocketContainer webSocketContainer;

    public Controller() {
        URI uri = URI.create("ws://localhost:8080/chat");
        webSocketContainer = ContainerProvider.getWebSocketContainer();

        try {
            webSocketContainer.connectToServer(this, uri);
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        System.out.println("Nawiązano połączenie z serwerem");
    }

    public void sendMessage(String message){
        try {
            session.getBasicRemote().sendBinary(ByteBuffer.wrap(message.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonSend.setOnMouseClicked(event -> sendMessage("Wiadomość z klienta"));
    }
}
