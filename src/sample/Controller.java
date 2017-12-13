package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

    @FXML
    TextArea textArea;

    @FXML
    TextField textMessage;

    private Session session;
    private WebSocketContainer webSocketContainer;

    public Controller() {
        webSocketContainer = ContainerProvider.getWebSocketContainer();
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

    public String getTextFromTextField(){
        String text = textMessage.getText();
        textMessage.clear();
        return text;
    }

    @OnMessage
    public void onMessage(Session session, ByteBuffer byteBuffer){
        textArea.appendText(new String(byteBuffer.array()) + "\n");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.setEditable(false);
        buttonSend.setOnMouseClicked(event -> sendMessage(getTextFromTextField()));

        URI uri = URI.create("ws://localhost:8080/chat");
        try {
            webSocketContainer.connectToServer(this, uri);
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
