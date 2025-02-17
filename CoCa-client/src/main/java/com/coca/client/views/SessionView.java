package com.coca.client.views;

import com.coca.client.events.*;
import com.coca.client.enums.RollType;
import com.coca.client.enums.Shift;
import com.coca.client.models.BladeRunner;
import com.coca.client.models.Mainframe;
import com.coca.client.models.MessageCode;
import com.coca.client.models.Npc;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableJms
public class SessionView implements IView {
    private final BladeRunner bladeRunner;
    private final String bladeRunnerName;
    private final String queueName;
    private final JmsTemplate jmsTemplate;

    private Label healthLabel;
    private Label resolveLabel;
    private Label promotionPointsLabel;
    private Label humanityPointsLabel;
    private Label chinyenPointsLabel;
    private CheckBoxTreeItem<String> bladeRunnerList;
    private final GridPane grid;
    private final VBox leftContent;
    private final VBox chatBox;
    private final ScrollPane chatScrollPane;
    private final Button pushButton;
    private final Button acceptButton;
    private final Text shift;
    private final Scene scene;
    private final Map<String, String> labelToSkill;

    public SessionView(boolean isAdmin, BladeRunner bladeRunner, String queueName, JmsTemplate jmsTemplate) {
        this.bladeRunner = bladeRunner;
        this.queueName = queueName;
        this.jmsTemplate = jmsTemplate;
        bladeRunnerName = getBladeRunnerName();

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

        grid = createGrid();

        leftContent = new VBox(5);
        grid.add(leftContent, 0, 1);

        var middleContent = new VBox();
        middleContent.setAlignment(Pos.CENTER);
        grid.add(middleContent, 1, 1);

        shift = createShiftText();
        changeShift(Shift.MORNING);
        middleContent.getChildren().add(shift);

        chatBox = new VBox();
        chatScrollPane = createChatScrollPane();
        chatScrollPane.setContent(chatBox);
        middleContent.getChildren().add(chatScrollPane);

        pushButton = new Button("Push roll");
//        pushButton.setOnAction(e -> rollPushAction());
//        pushButton.setVisible(false);
        acceptButton = new Button("Accept");
//        acceptButton.setOnAction(e -> rollAcceptAction());
//        acceptButton.setVisible(false);
//
//        var rollReactionBox = new HBox(20);
//        rollReactionBox.setAlignment(Pos.BOTTOM_RIGHT);
//        rollReactionBox.getChildren().add(pushButton);
//        rollReactionBox.getChildren().add(acceptButton);
//        grid.add(rollReactionBox, 25, 22);
//
//        var mainframeButton = new Button("Mainframe");
//        mainframeButton.setOnAction(e -> createMainframeDialog());
//        var mainframeBox = new HBox(10);
//        mainframeBox.setAlignment(Pos.BOTTOM_RIGHT);
//        mainframeBox.getChildren().add(mainframeButton);
//        grid.add(mainframeBox, 25, 21);

        var rightContent = new VBox();
        rightContent.setVisible(false);
        var label = new Label("Right content");
        rightContent.getChildren().add(label);
        grid.add(rightContent, 2, 1);

        var expandButton = new Button();
        var imageView = createImageView("npc.png");
        expandButton.setGraphic(imageView);
        expandButton.setOnAction(e -> rightContent.setVisible(!rightContent.isVisible()));
        expandButton.setAlignment(Pos.CENTER_RIGHT);

        var toolbarBox = new HBox();
        toolbarBox.getChildren().add(expandButton);
        var toolbar = createToolbar(toolbarBox);
        grid.add(toolbar, 0, 0, 3, 1);

        if (isAdmin) {
            setupAdmin();
        }
        else {
            setupUser(bladeRunner);
        }

        var gridLinesButton = new Button("Lines");
        gridLinesButton.setOnAction(e -> grid.setGridLinesVisible(!grid.isGridLinesVisible()));
        leftContent.getChildren().add(gridLinesButton);

        scene = new Scene(grid, 800, 600);
    }

    public void addToChatBox(String message) {
        Platform.runLater(() -> {
            var textFlow = new TextFlow();
            var text = new Text(String.format("Player %s joined game", message));
            text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
            text.setTextAlignment(TextAlignment.LEFT);
            textFlow.setPadding(new Insets(5));
            textFlow.getChildren().add(text);
            chatBox.getChildren().add(textFlow);
        });
    }

    public void addToVBoxRollResult(int id, String attribute, String skill, int attributeValue, int skillValue,
                                    List<Integer> diceRolls, List<RollType> rollTypes, int successes) {
        var text = new TextFlow();
        text.setMaxWidth(400);
        text.setPadding(new Insets(5, 5, 5, 5));
        var t1 = new Text(String.format("%s rolled for %s(%s) and %s(%s) and achieved ",
                id, capitalize(attribute), map(attributeValue), convertSkillToLabel(skill), map(skillValue)));
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

        var image = (Image) null;
        if (bladeRunner != null && bladeRunner.getAvatarImage() != null) {
            var stream = new ByteArrayInputStream(bladeRunner.getAvatarImage().getImage());
            image = new Image(stream);
        }
        else {
            var resource = getClass().getClassLoader().getResourceAsStream("bladerunner.jpg");
            if (resource != null) {
                image = new Image(resource);
            }
        }

        var imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        Platform.runLater(() -> {
            chatBox.getChildren().add(text);
            chatBox.getChildren().add(imageView);
            //vBox.getChildren().addListener();
            chatScrollPane.setVvalue(1.0);
        });
    }

    public void setPushButtonVisible(boolean visible) {
        pushButton.setVisible(visible);
    }

    public void setAcceptButtonVisible(boolean visible) {
        acceptButton.setVisible(visible);
    }

    public void addToTreeView(String name) {
        Platform.runLater(() -> bladeRunnerList.getChildren().add(new TreeItem<>(name)));
    }

    public void changeShift(Shift shift) {
        this.shift.setText(String.format("Shift: %s", shift.name()));
    }

    private GridPane createGrid() {
        var grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(5));

        var leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(20);
        var middleColumn = new ColumnConstraints();
        middleColumn.setPercentWidth(60);
        var rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(20);
        grid.getColumnConstraints().addAll(leftColumn, middleColumn, rightColumn);

        return grid;
    }

    private ScrollPane createChatScrollPane() {
        var scrollPane = new ScrollPane();
        scrollPane.setPrefSize(400, 600);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }

    private Text createShiftText() {
        var shift = new Text();
        shift.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        return shift;
    }

    private ToolBar createToolbar(HBox toolbarBox) {
        var toolbar = new ToolBar();
        HBox.setHgrow(toolbarBox, Priority.ALWAYS);
        toolbarBox.setAlignment(Pos.CENTER_RIGHT);
        toolbar.getItems().add(toolbarBox);
        toolbar.setBackground(Background.EMPTY);
        return toolbar;
    }

    private ImageView createImageView(String name) {
        var resource = getClass().getClassLoader().getResourceAsStream(name);
        if (resource == null) {
            return null;
        }

        var image = new Image(resource);
        var imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(10);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }

    private void setupUser(BladeRunner bladeRunner) {
        if (bladeRunner == null) {
            System.out.println("Blade runner is null and can't set up the view");
            return;
        }

        var stats = createStatsBox();
        leftContent.getChildren().add(stats);

        createAttributeRow("Strength", bladeRunner.getStrength());
        createSkillRow("Force", bladeRunner.getForce());
        createSkillRow("Hand-to-hand combat", bladeRunner.getHandToHandCombat());
        createSkillRow("Stamina", bladeRunner.getStamina());

        createAttributeRow("Agility", bladeRunner.getAgility());
        createSkillRow("Firearms", bladeRunner.getFirearms());
        createSkillRow("Mobility", bladeRunner.getMobility());
        createSkillRow("Stealth", bladeRunner.getStealth());

        createAttributeRow("Intelligence", bladeRunner.getIntelligence());
        createSkillRow("Medical aid", bladeRunner.getMedicalAid());
        createSkillRow("Observation", bladeRunner.getObservation());
        createSkillRow("Tech", bladeRunner.getTech());

        createAttributeRow("Empathy", bladeRunner.getEmpathy());
        createSkillRow("Connections", bladeRunner.getConnections());
        createSkillRow("Insight", bladeRunner.getInsight());
        createSkillRow("Manipulation", bladeRunner.getManipulation());

//        healthLabel = createStatusRowWithLimit("Health",  bladeRunner.getHealth(), 10, 2);
//        resolveLabel = createStatusRowWithLimit("Resolve", bladeRunner.getResolve(), 11, 3);
//        promotionPointsLabel = createStatusRow("Promotion points", bladeRunner.getPromotionPoints(), 4);
//        humanityPointsLabel = createStatusRow("Humanity points", bladeRunner.getHumanityPoints(), 5);
//        chinyenPointsLabel = createStatusRow("Chinyen points", bladeRunner.getChinyenPoints(), 6);
    }

    private void setupAdmin() {
        bladeRunnerList = new CheckBoxTreeItem<>("Blade Runners");
        bladeRunnerList.setExpanded(false);
        var treeView = new TreeView<>(bladeRunnerList);
        treeView.setPrefHeight(100);
        treeView.setPrefWidth(200);
        treeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        leftContent.getChildren().add(treeView);

        var npcButton = new Button("Create NPC");
        npcButton.setOnAction(e -> createNpcDialog());
        var npcBox = new HBox(10);
        npcBox.setAlignment(Pos.BOTTOM_RIGHT);
        npcBox.getChildren().add(npcButton);
        leftContent.getChildren().add(npcBox);

        var shiftButton = new Button("Change shift");
        shiftButton.setOnAction(e -> createShiftChangeDialog());
        var box = new HBox(10);
        box.setAlignment(Pos.BOTTOM_RIGHT);
        box.getChildren().add(shiftButton);
        leftContent.getChildren().add(box);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void refresh() {
    }

    private void rollAction(String skill, Integer bonusDie, Stage stage) {
        var rollEvent = new RollEvent(bladeRunner.getId(), labelToSkill.get(skill), bonusDie);
        rollEvent.setMessageCode(MessageCode.getMessageCode(RollEvent.class));
        jmsTemplate.convertAndSend(queueName, rollEvent);
        stage.close();
    }

    private void rollPushAction() {
        var pushEvent = new PushEvent(bladeRunner.getId());
        pushEvent.setMessageCode(MessageCode.getMessageCode(PushEvent.class));
        jmsTemplate.convertAndSend(queueName, pushEvent);
    }

    private void changeShiftAction(Shift shift) {
        var changeEvent = new ShiftChangeEvent(shift);
        changeEvent.setMessageCode(MessageCode.getMessageCode(ShiftChangeEvent.class));
        jmsTemplate.convertAndSend(queueName, changeEvent);
    }

    private void createNpc(String name, boolean isHuman, int strength, int agility, int intelligence, int empathy,
                           int force, int handToHandCombat, int stamina, int firearms, int mobility, int stealth,
                           int medicalAid, int observation, int tech, int connections, int manipulation, int insight) {
        var health = (strength + agility + 2) / 4;
        var resolve = (intelligence + empathy + 2) / 4;
        var npc = new Npc(0, name, "", "", isHuman, strength, agility, intelligence, empathy, health, resolve, force,
                handToHandCombat, stamina, firearms, mobility, stealth, medicalAid, observation, tech, connections,
                manipulation, insight);
        var npcEvent = new NpcDataEvent(npc);
        npcEvent.setMessageCode(MessageCode.getMessageCode(NpcDataEvent.class));
        jmsTemplate.convertAndSend(queueName, npcEvent);
    }

    private void sendToMainframe(String title, String description) {
        var author = bladeRunner == null ? "unknown" : bladeRunnerName;
        var mainframeData = new Mainframe(0, author, title, description, null);
        var mainframeEvent = new MainframeAddDataEvent(mainframeData);
        mainframeEvent.setMessageCode(MessageCode.getMessageCode(MainframeAddDataEvent.class));
        jmsTemplate.convertAndSend(queueName, mainframeEvent);
    }

    private void rollAcceptAction() {
        var acceptEvent = new AcceptEvent(bladeRunner.getId());
        acceptEvent.setMessageCode(MessageCode.getMessageCode(AcceptEvent.class));
        jmsTemplate.convertAndSend(queueName, acceptEvent);
        setPushButtonVisible(false);
        setAcceptButtonVisible(false);
    }

    private void createSkillRow(String text, int value) {
        var mainBox = new HBox(5);
        var labelBox = new HBox();
        labelBox.setAlignment(Pos.CENTER_LEFT);
        var valueBox = new HBox();
        valueBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(valueBox, Priority.ALWAYS);

        var label = createClickableLabel(text);
        labelBox.getChildren().add(label);
        var valueLabel = createLabel(map(value), 12);
        valueBox.getChildren().add(valueLabel);
        mainBox.getChildren().addAll(labelBox, valueBox);
        leftContent.getChildren().add(mainBox);
    }

    private void createAttributeRow(String text, int value) {
        var mainBox = new HBox(5);
        var labelBox = new HBox();
        labelBox.setAlignment(Pos.CENTER_LEFT);
        var valueBox = new HBox();
        valueBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(valueBox, Priority.ALWAYS);

        var label = createLabel(text, 16);
        labelBox.getChildren().add(label);
        var valueLabel = createLabel(map(value), 16);
        valueBox.getChildren().add(valueLabel);
        mainBox.getChildren().addAll(labelBox, valueBox);
        leftContent.getChildren().add(mainBox);
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
        var label = new Label(s);
        label.setFont(Font.font("Tahoma", FontWeight.NORMAL, size));
        label.setAlignment(Pos.CENTER_LEFT);
        return label;
    }

    private HBox createStatsBox() {
        var statsBox = new HBox(5);
        var label = new Label("Stats");
        label.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        statsBox.getChildren().add(label);
        statsBox.setAlignment(Pos.CENTER);
        return statsBox;
    }

    private Label createClickableLabel(String s) {
        var label =  new Label(s);
        label.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
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

    private void createNpcDialog() {
        var npcDialog = new Stage();
        npcDialog.setTitle("Create NPC dialog");
        npcDialog.initModality(Modality.APPLICATION_MODAL);

        var nameLabel = new Label("Name");
        var nameTextField = new TextField();
        var isHumanLabel = new Label("Human");
        var isHumanCheckbox = new CheckBox();

        ObservableList<String> skillData = FXCollections.observableArrayList();
        skillData.addAll("A", "B", "C", "D");

        var strengthLabel = new Label("Strength");
        var strengthValue = new ComboBox<>(skillData);
        var agilityLabel = new Label("Agility");
        var agilityValue = new ComboBox<>(skillData);
        var intelligenceLabel = new Label("Intelligence");
        var intelligenceValue = new ComboBox<>(skillData);
        var empathyLabel = new Label("Empathy");
        var empathyValue = new ComboBox<>(skillData);

        var forceLabel = new Label("Force");
        var forceValue = new ComboBox<>(skillData);
        var handToHandCombatLabel = new Label("Hand-to-hand combat");
        var handToHandCombatValue = new ComboBox<>(skillData);
        var staminaLabel = new Label("Stamina");
        var staminaValue = new ComboBox<>(skillData);

        var firearmsLabel = new Label("Firearms");
        var firearmsValue = new ComboBox<>(skillData);
        var mobilityLabel = new Label("Mobility");
        var mobilityValue = new ComboBox<>(skillData);
        var stealthLabel = new Label("Stealth");
        var stealthValue = new ComboBox<>(skillData);

        var medicalAidLabel = new Label("Medical aid");
        var medicalAidValue = new ComboBox<>(skillData);
        var observationLabel = new Label("Observation");
        var observationValue = new ComboBox<>(skillData);
        var techLabel = new Label("Tech");
        var techValue = new ComboBox<>(skillData);

        var connectionsLabel = new Label("Connections");
        var connectionsValue = new ComboBox<>(skillData);
        var manipulationLabel = new Label("Manipulation");
        var manipulationValue = new ComboBox<>(skillData);
        var insightLabel = new Label("Insight");
        var insightValue = new ComboBox<>(skillData);

        var createButton = new Button("Create");
        var box = new HBox(10);
        box.setAlignment(Pos.BOTTOM_RIGHT);
        box.getChildren().add(createButton);
        createButton.setOnAction(e -> createNpc(nameTextField.getText(), isHumanCheckbox.isSelected(),
                map(strengthValue.getValue()), map(agilityValue.getValue()), map(intelligenceValue.getValue()), map(empathyValue.getValue()),
                map(forceValue.getValue()), map(handToHandCombatValue.getValue()), map(staminaValue.getValue()),
                map(firearmsValue.getValue()), map(mobilityValue.getValue()), map(stealthValue.getValue()),
                map(medicalAidValue.getValue()), map(observationValue.getValue()), map(techValue.getValue()),
                map(connectionsValue.getValue()), map(manipulationValue.getValue()), map(insightValue.getValue())));

        var container = new GridPane();
        container.setHgap(10);
        container.setVgap(10);
        container.setPadding(new Insets(25));
        container.setAlignment(Pos.CENTER);
        container.add(nameLabel, 0, 0);
        container.add(nameTextField, 1, 0);
        container.add(isHumanLabel, 0, 1);
        container.add(isHumanCheckbox, 1, 1);
        container.add(strengthLabel, 0, 2);
        container.add(strengthValue, 1, 2);
        container.add(agilityLabel, 0, 3);
        container.add(agilityValue, 1, 3);
        container.add(intelligenceLabel, 0, 4);
        container.add(intelligenceValue, 1, 4);
        container.add(empathyLabel, 0, 5);
        container.add(empathyValue, 1, 5);
        container.add(forceLabel, 0, 6);
        container.add(forceValue, 1, 6);
        container.add(handToHandCombatLabel, 0, 7);
        container.add(handToHandCombatValue, 1, 7);
        container.add(staminaLabel, 0, 8);
        container.add(staminaValue, 1, 8);
        container.add(firearmsLabel, 0, 9);
        container.add(firearmsValue, 1, 9);
        container.add(mobilityLabel, 0, 10);
        container.add(mobilityValue, 1, 10);
        container.add(stealthLabel, 0, 11);
        container.add(stealthValue, 1, 11);
        container.add(medicalAidLabel, 0, 12);
        container.add(medicalAidValue, 1, 12);
        container.add(observationLabel, 0, 13);
        container.add(observationValue, 1, 13);
        container.add(techLabel, 0, 14);
        container.add(techValue, 1, 14);
        container.add(connectionsLabel, 0, 15);
        container.add(connectionsValue, 1, 15);
        container.add(manipulationLabel, 0, 16);
        container.add(manipulationValue, 1, 16);
        container.add(insightLabel, 0, 17);
        container.add(insightValue, 1, 17);
        container.add(createButton, 0, 18);

        npcDialog.setScene(new Scene(container));
        npcDialog.show();
    }

    private void createMainframeDialog() {
        var mainframeDialog = new Stage();
        mainframeDialog.setTitle("Report to Mainframe");
        mainframeDialog.initModality(Modality.APPLICATION_MODAL);

        var idLabel = new Label("Title");
        var idTextField = new TextField();
        var reportLabel = new Label("Report");
        var reportTextArea = new TextArea();
        reportTextArea.setWrapText(true);
        reportTextArea.setPrefSize(200, 200);

        var sendButton = new Button("Send");
        var box = new HBox(10);
        box.setAlignment(Pos.BOTTOM_RIGHT);
        box.getChildren().add(sendButton);
        sendButton.setOnAction(e -> sendToMainframe(idTextField.getText(), reportTextArea.getText()));

        var container = new GridPane();
        container.setHgap(10);
        container.setVgap(10);
        container.setPadding(new Insets(25));
        container.setAlignment(Pos.CENTER);
        container.add(idLabel, 0, 0);
        container.add(idTextField, 0, 1);
        container.add(reportLabel, 0, 2);
        container.add(reportTextArea, 0, 3);
        container.add(box, 0, 4);

        mainframeDialog.setScene(new Scene(container));
        mainframeDialog.show();
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

    private int map(String value) {
        switch (value) {
            case "D" -> {
                return 6;
            }
            case "C" -> {
                return 8;
            }
            case "B" -> {
                return 10;
            }
            case "A" -> {
                return 12;
            }
            default -> {
                return 0;
            }
        }
    }

    private String getBladeRunnerName() {
        if (bladeRunner == null) {
            return "unknown";
        } else {
            var builder = new StringBuilder();
            if (bladeRunner.getName() != null && !bladeRunner.getName().isEmpty()) {
                builder.append(bladeRunner.getName());
            }

            if (bladeRunner.getNickname() != null && !bladeRunner.getNickname().isEmpty()) {
                builder.append(" '").append(bladeRunner.getNickname()).append("'");
            }

            if (bladeRunner.getSurname() != null && !bladeRunner.getSurname().isEmpty()) {
                builder.append(" ").append(bladeRunner.getSurname());
            }

            return builder.toString();
        }
    }
}
