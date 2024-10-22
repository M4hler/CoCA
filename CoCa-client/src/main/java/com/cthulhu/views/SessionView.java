package com.cthulhu.views;

import com.cthulhu.enums.RollType;
import com.cthulhu.enums.Shift;
import com.cthulhu.events.AcceptEvent;
import com.cthulhu.events.ShiftChangeEvent;
import com.cthulhu.events.PushEvent;
import com.cthulhu.events.RollEvent;
import com.cthulhu.models.BladeRunner;
import com.cthulhu.models.MessageCode;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableJms
public class SessionView implements IView {
    private final BladeRunner bladeRunner;
    private final String bladeRunnerName;
    private final String queueName;
    private final JmsTemplate jmsTemplate;

    private Text title;
    private Text shift;
    private Label healthLabel;
    private Label resolveLabel;
    private Label promotionPointsLabel;
    private Label humanityPointsLabel;
    private Label chinyenPointsLabel;
    private CheckBoxTreeItem<String> root;
    private final GridPane grid;
    private final VBox vBox;
    private final ScrollPane scrollPane;
    private final Button pushButton;
    private final Button acceptButton;
    private final Scene scene;
    private final Map<String, String> labelToSkill;

    public SessionView(boolean isAdmin, BladeRunner bladeRunner, String queueName) {
        this.bladeRunner = bladeRunner;
        this.queueName = queueName;
        if(bladeRunner == null) {
            bladeRunnerName = "";
        }
        else {
            bladeRunnerName = bladeRunner.getName();
        }

        jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:61616"));
        var converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        jmsTemplate.setMessageConverter(converter);

        labelToSkill = new HashMap<>();
        labelToSkill.put("Force", "force");
        labelToSkill.put("Hand-to-hand combat", "handToHandCombat");
        labelToSkill.put("Stamina", "stamina");
        labelToSkill.put("Firearms", "firearms");
        labelToSkill.put("Mobility", "mobility");
        labelToSkill.put("Stealth", "stealth");
        labelToSkill.put("Medical aid", "medicalAid");
        labelToSkill.put("Observation", "observation");
        labelToSkill.put("Tech", "tech");
        labelToSkill.put("Connections", "connections");
        labelToSkill.put("Manipulation", "manipulation");
        labelToSkill.put("Insight", "insight");

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        vBox = new VBox();
        vBox.setMaxWidth(390);

        shift = new Text("Shift: MORNING");
        shift.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(shift, 3, 1, 1, 1);

        scrollPane = new ScrollPane();
        scrollPane.setPrefSize(400, 600);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(vBox);

        grid.add(scrollPane, 3, 2, 20, 20);

        pushButton = new Button("Push roll");
        pushButton.setOnAction(e -> rollPushAction());
        pushButton.setVisible(false);
        acceptButton = new Button("Accept");
        acceptButton.setOnAction(e -> rollAcceptAction());
        acceptButton.setVisible(false);

        var rollReactionBox = new HBox(20);
        rollReactionBox.setAlignment(Pos.BOTTOM_RIGHT);
        rollReactionBox.getChildren().add(pushButton);
        rollReactionBox.getChildren().add(acceptButton);
        grid.add(rollReactionBox, 25, 22);

        scene = new Scene(grid, 800, 600);

        if(isAdmin) {
            setupAdmin();
        }
        else {
            setupUser(bladeRunner);
        }
    }

    public void addToVBox(String message) {
        Platform.runLater(() -> {
            var textFlow = new TextFlow();
            var text = new Text("Player " + message + " joined game");
            text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
            text.setTextAlignment(TextAlignment.LEFT);
            textFlow.setPadding(new Insets(5, 5, 5, 5));
            textFlow.getChildren().add(text);
            vBox.getChildren().add(textFlow);
        });
    }

    public void addToVBoxRollResult(String name, String attribute, String skill, int attributeValue, int skillValue,
                                    List<Integer> diceRolls, List<RollType> rollTypes, int successes) {
        var text = new TextFlow();
        text.setMaxWidth(400);
        text.setPadding(new Insets(5, 5, 5, 5));
        var t1 = new Text(String.format("%s rolled for %s(%s) and %s(%s) and achieved ",
                name, capitalize(attribute), map(attributeValue), convertSkillToLabel(skill), map(skillValue)));
        t1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));

        var t2 = new Text();
        t2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        if(successes == 0) {
            t2.setText("FAILURE");
            t2.setFill(Color.FIREBRICK);
        }
        else {
            t2.setText(String.format("SUCCESS(%d)", successes));
            t2.setFill(Color.GREEN);
        }

        var t3 = new Text(" rolling ");
        t3.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        for(int i = 0; i < diceRolls.size(); i++) {
            t3.setText(String.format("%s%d for %s", t3.getText(), diceRolls.get(i), rollTypes.get(i).name()));
            if(i < diceRolls.size() - 1) {
                t3.setText(t3.getText() + ", ");
            }
        }

        text.setTextAlignment(TextAlignment.LEFT);
        text.getChildren().addAll(t1, t2, t3);

        Platform.runLater(() -> {
            vBox.getChildren().add(text);
            //vBox.getChildren().addListener();
            scrollPane.setVvalue(1.0);
        });
    }

    public void setPushButtonVisible(boolean visible) {
        pushButton.setVisible(visible);
    }

    public void setAcceptButtonVisible(boolean visible) {
        acceptButton.setVisible(visible);
    }

    public void addToTreeView(String name) {
        Platform.runLater(() -> root.getChildren().add(new TreeItem<>(name)));
    }

    public void changeShift(Shift shift) {
        this.shift.setText("Shift: " + shift.name());
    }

    private void setupUser(BladeRunner bladeRunner) {
        if(bladeRunner == null) {
            System.out.println("Blade runner is null and can't set up the view");
            return;
        }

        title = new Text("User view");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 3, 0, 2, 1);

        var stats = createLabel("Stats", 16);
        grid.add(stats, 0, 1, 2, 1);

        createAttributeRow("Strength", bladeRunner.getStrength(), 2, 14);
        createRow("Force", bladeRunner.getForce(), 3, 12);
        createRow("Hand-to-hand combat", bladeRunner.getHandToHandCombat(), 4, 12);
        createRow("Stamina", bladeRunner.getStamina(), 5, 12);

        createAttributeRow("Agility", bladeRunner.getAgility(), 6, 14);
        createRow("Firearms", bladeRunner.getFirearms(), 7, 12);
        createRow("Mobility", bladeRunner.getMobility(), 8, 12);
        createRow("Stealth", bladeRunner.getStealth(), 9, 12);

        createAttributeRow("Intelligence", bladeRunner.getIntelligence(), 10, 14);
        createRow("Medical aid", bladeRunner.getMedicalAid(), 11, 12);
        createRow("Observation", bladeRunner.getObservation(), 12, 12);
        createRow("Tech", bladeRunner.getTech(), 13, 12);

        createAttributeRow("Empathy", bladeRunner.getEmpathy(), 14, 14);
        createRow("Connections", bladeRunner.getConnections(), 15, 12);
        createRow("Insight", bladeRunner.getInsight(), 16, 12);
        createRow("Manipulation", bladeRunner.getManipulation(), 17, 12);

        healthLabel = createStatusRowWithLimit("Health",  bladeRunner.getHealth(), 10, 2);
        resolveLabel = createStatusRowWithLimit("Resolve", bladeRunner.getResolve(), 11, 3);
        promotionPointsLabel = createStatusRow("Promotion points", bladeRunner.getPromotionPoints(), 4);
        humanityPointsLabel = createStatusRow("Humanity points", bladeRunner.getHumanityPoints(), 5);
        chinyenPointsLabel = createStatusRow("Chinyen points", bladeRunner.getChinyenPoints(), 6);
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
        grid.add(treeView, 0, 3);

        var shiftButton = new Button("Change shift");
        shiftButton.setOnAction(e -> createShiftChangeDialog());
        var box = new HBox(10);
        box.setAlignment(Pos.BOTTOM_RIGHT);
        box.getChildren().add(shiftButton);
        grid.add(box, 0, 21);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void refresh() {
    }

    private void rollAction(String skill, Integer bonusDie, Stage stage) {
        var rollEvent = new RollEvent(bladeRunnerName, labelToSkill.get(skill), bonusDie);
        rollEvent.setMessageCode(MessageCode.getMessageCode(RollEvent.class));
        jmsTemplate.convertAndSend(queueName, rollEvent);
        stage.close();
    }

    private void rollPushAction() {
        var pushEvent = new PushEvent(bladeRunnerName);
        pushEvent.setMessageCode(MessageCode.getMessageCode(PushEvent.class));
        jmsTemplate.convertAndSend(queueName, pushEvent);
    }

    private void changeShiftAction(Shift shift) {
        var changeEvent = new ShiftChangeEvent(shift);
        changeEvent.setMessageCode(MessageCode.getMessageCode(ShiftChangeEvent.class));
        jmsTemplate.convertAndSend(queueName, changeEvent);
    }

    private void rollAcceptAction() {
        var acceptEvent = new AcceptEvent(bladeRunnerName);
        acceptEvent.setMessageCode(MessageCode.getMessageCode(AcceptEvent.class));
        jmsTemplate.convertAndSend(queueName, acceptEvent);
        setPushButtonVisible(false);
        setAcceptButtonVisible(false);
    }

    private void createRow(String text, int value, int row, int size) {
        var label = createClickableLabel(text, size);
        var valueLabel = createLabel(map(value), size);
        grid.add(label, 0, row, 2, 1);
        grid.add(valueLabel, 2, row, 1, 1);
    }

    private void createAttributeRow(String text, int value, int row, int size) {
        var label = createLabel(text, size);
        var valueLabel = createLabel(map(value), size);
        grid.add(label, 0, row, 2, 1);
        grid.add(valueLabel, 2, row, 1, 1);
    }

    private Label createStatusRowWithLimit(String text, int value, int limit, int row) {
        var label = createLabel(text, 12);
        var labelValue = createLabel(String.format("%d/%d", value, limit), 12);
        grid.add(label, 25, row);
        grid.add(labelValue, 26, row);

        return labelValue;
    }

    private Label createStatusRow(String text, int value, int row) {
        var label = createLabel(text, 12);
        var labelValue = createLabel(String.valueOf(value), 12);
        grid.add(label, 25, row);
        grid.add(labelValue, 26, row);

        return labelValue;
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
        label.setOnMouseClicked(mouseEvent -> createRollDialog(s));
        return label;
    }

    private void createRollDialog(String s) {
        var rollDialog = new Stage();
        rollDialog.setTitle("Roll dialog");
        rollDialog.initModality(Modality.APPLICATION_MODAL);

        var title = new Label("Rolling for " + s);
        var bonusDieLabel = new Label("Choose bonus die");

        var comboBox = new ComboBox<Integer>();
        ObservableList<Integer> data = FXCollections.observableArrayList();
        data.addAll(-1, 0, 1);
        comboBox.setItems(data);

        var rollbutton = new Button("Roll");
        var rollBox = new HBox(10);
        rollBox.setAlignment(Pos.BOTTOM_RIGHT);
        rollBox.getChildren().add(rollbutton);
        rollbutton.setOnAction(e -> rollAction(s, comboBox.getValue(), rollDialog));

        var container = new GridPane();
        container.setHgap(10);
        container.setVgap(10);
        container.setPadding(new Insets(25));
        container.setAlignment(Pos.CENTER);
        container.add(title, 0, 0);
        container.add(bonusDieLabel, 0, 2);
        container.add(comboBox, 4, 2);
        container.add(rollBox, 0, 4);

        rollDialog.setScene(new Scene(container));
        rollDialog.show();
    }

    private void createShiftChangeDialog() {
        var shiftDialog = new Stage();
        shiftDialog.setTitle("Change shift dialog");
        shiftDialog.initModality(Modality.APPLICATION_MODAL);

        var comboBox = new ComboBox<Shift>();
        ObservableList<Shift> data = FXCollections.observableArrayList();
        List<Shift> values = Arrays.asList(Shift.values());
        data.addAll(values);
        comboBox.setItems(data);

        var changeButton = new Button("Change shift");
        var nextButton = new Button("Next shift");
        var box = new HBox(10);
        box.setAlignment(Pos.BOTTOM_RIGHT);
        box.getChildren().add(changeButton);
        box.getChildren().add(nextButton);
        changeButton.setOnAction(e -> changeShiftAction(comboBox.getValue()));
        nextButton.setOnAction(e -> changeShiftAction(Shift.valueOf(shift.getText().replaceAll("Shift:", "").trim()).next()));

        var container = new GridPane();
        container.setHgap(10);
        container.setVgap(10);
        container.setPadding(new Insets(25));
        container.setAlignment(Pos.CENTER);
        container.add(comboBox, 0, 0);
        container.add(box, 0, 1);

        shiftDialog.setScene(new Scene(container));
        shiftDialog.show();
    }

    private String convertSkillToLabel(String skill) {
        for(var entry : labelToSkill.entrySet()) {
            if(entry.getValue().equals(skill)) {
                return entry.getKey();
            }
        }

        return skill;
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
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
