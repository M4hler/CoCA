package com.cthulhu.views;

import com.cthulhu.controllers.MainController;
import com.cthulhu.events.RollEvent;
import com.cthulhu.models.BladeRunner;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Getter
@Setter
@Configuration
@EnableJms
public class SessionView implements IView {
    private Text title;
    private CheckBoxTreeItem<String> root;
    private final GridPane grid;
    private final VBox vBox;
    private final Scene scene;

    private final JmsTemplate jmsTemplate;

    public SessionView(boolean isAdmin, BladeRunner bladeRunner) {
        jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:61616"));
        var converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        jmsTemplate.setMessageConverter(converter);

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        vBox = new VBox();

        var scrollPane = new ScrollPane();
        scrollPane.setPrefSize(400, 600);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(vBox);

        grid.add(scrollPane, 3, 1, 20, 20);

        scene = new Scene(grid, 800, 600);

        if(isAdmin) {
            setupAdmin();
        }
        else {
            setupUser(bladeRunner);
        }
    }

    public void addToVBox(String message) {
        System.out.println("Adding to vbox");
        Platform.runLater(() -> {
            var text = new Text("Player " + message + " joined game");
            text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
            text.setTextAlignment(TextAlignment.LEFT);
            vBox.getChildren().add(text);
        });
    }

    public void addToTreeView(String name) {
        System.out.println("Adding to treeView");
        Platform.runLater(() -> root.getChildren().add(new TreeItem<>(name)));
    }

    private void setupUser(BladeRunner bladeRunner) {
        title = new Text("User view");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 3, 0, 2, 1);

        var stats = createLabel("Stats", 16);
        grid.add(stats, 0, 1, 2, 1);

        createRow("Strength", bladeRunner.getStrength(), 2, 14);
        createRow("Force", bladeRunner.getForce(), 3, 12);
        createRow("Hand-to-hand combat", bladeRunner.getHandToHandCombat(), 4, 12);
        createRow("Stamina", bladeRunner.getStamina(), 5, 12);

        createRow("Agility", bladeRunner.getAgility(), 6, 14);
        createRow("Firearms", bladeRunner.getFirearms(), 7, 12);
        createRow("Mobility", bladeRunner.getMobility(), 8, 12);
        createRow("Stealth", bladeRunner.getStealth(), 9, 12);

        createRow("Intelligence", bladeRunner.getIntelligence(), 10, 14);
        createRow("Medical aid", bladeRunner.getMedicalAid(), 11, 12);
        createRow("Observation", bladeRunner.getObservation(), 12, 12);
        createRow("Tech", bladeRunner.getTech(), 13, 12);

        createRow("Empathy", bladeRunner.getEmpathy(), 14, 14);
        createRow("Connections", bladeRunner.getConnections(), 15, 12);
        createRow("Insight", bladeRunner.getInsight(), 16, 12);
        createRow("Manipulation", bladeRunner.getManipulation(), 17, 12);
    }

    private void setupAdmin() {
        title = new Text("Admin view");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 3, 0, 2, 1);

        root = new CheckBoxTreeItem<>("Blade Runners");
        root.setExpanded(false);
        var treeView = new TreeView<>(root);
        treeView.setPrefHeight(100);
        treeView.setPrefWidth(200);
        treeView.setCellFactory(CheckBoxTreeCell.forTreeView());

        var rollbutton = new Button("Roll");
        var rollBox = new HBox(10);
        rollBox.setAlignment(Pos.BOTTOM_RIGHT);
        rollBox.getChildren().add(rollbutton);
        rollbutton.setOnAction(e -> rollAction());

        grid.add(treeView, 0, 1);
        grid.add(rollBox, 0, 5);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void refresh() {
    }

    private void rollAction() {
        System.out.println("Roll action");
        System.out.println("Sending event to: " + MainController.getQueue());
        var rollEvent = new RollEvent(8, null, "", 0);
        jmsTemplate.convertAndSend(MainController.getQueue(), rollEvent);
    }

    private void createRow(String text, int value, int row, int size) {
        var label = createClickableLabel(text, size);
        var valueLabel = createLabel(map(value), size);
        grid.add(label, 0, row, 2, 1);
        grid.add(valueLabel, 2, row, 1, 1);
    }

    private Label createLabel(String s, int size) {
        var label =  new Label(s);
        label.setFont(Font.font("Tahoma", FontWeight.NORMAL, size));
        label.setAlignment(Pos.CENTER_LEFT);
        return label;
    }

    private Label createClickableLabel(String s, int size) {
        var label =  new Label(s);
        label.setFont(Font.font("Tahoma", FontWeight.NORMAL, size));
        label.setAlignment(Pos.CENTER_LEFT);
        label.setOnMouseEntered(mouseEvent
                -> label.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY))));
        label.setOnMouseExited(mouseEvent -> label.setBackground(Background.EMPTY));
        //label.setOnMouseClicked();
        return label;
    }

    private String map(int value) {
        switch (value) {
            case 6 -> {
                return "D";
            }
            case 8 -> {
                return "C";
            }
            case 10 -> {
                return "B";
            }
            case 12 -> {
                return "A";
            }
            default -> {
                return "?";
            }
        }
    }
}
