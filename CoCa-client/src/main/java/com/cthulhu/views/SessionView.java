package com.cthulhu.views;

import com.cthulhu.models.BladeRunner;
import jakarta.jms.MessageListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionView implements IView {
    private Label strength;
    private Label force;
    private Label handToHandCombat;
    private Label stamina;
    private Label agility;
    private Label firearms;
    private Label mobility;
    private Label stealth;
    private Label intelligence;
    private Label medicalAid;
    private Label observation;
    private Label tech;
    private Label empathy;
    private Label connections;
    private Label insight;
    private Label manipulation;

    private Text title;
    private final GridPane grid;
    private final Scene scene;

    public SessionView(boolean isAdmin, BladeRunner bladeRunner) {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        VBox vBox = new VBox();
        for(int i = 0; i < 5; i++) {
            Text text = new Text("Message: " + i);
            text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
            text.setTextAlignment(TextAlignment.LEFT);
            vBox.getChildren().add(text);
        }

        ScrollPane scrollPane = new ScrollPane();
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

    private void setupUser(BladeRunner bladeRunner) {
        title = new Text("User view");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 3, 0, 2, 1);

        Label stats = createLabel("Stats", 16);
        grid.add(stats, 0, 1, 2, 1);

        Label strengthLabel = createLabel("Strength", 14);
        grid.add(strengthLabel, 0, 2, 2, 1);
        strength = createLabel(map(bladeRunner.getStrength()), 14);
        grid.add(strength, 2, 2, 1, 1);
        Label forceLabel = createLabel("Force", 12);
        grid.add(forceLabel, 0, 3, 2, 1);
        force = createLabel(map(bladeRunner.getForce()), 12);
        grid.add(force, 2, 3, 1, 1);
        Label handToHandCombatLabel = createLabel("Hand-to-hand combat", 12);
        grid.add(handToHandCombatLabel, 0, 4, 2, 1);
        handToHandCombat = createLabel(map(bladeRunner.getHandToHandCombat()), 12);
        grid.add(handToHandCombat, 2, 4, 1, 1);
        Label staminaLabel = createLabel("Stamina", 12);
        grid.add(staminaLabel, 0, 5, 2, 1);
        stamina = createLabel(map(bladeRunner.getStamina()), 12);
        grid.add(stamina, 2, 5, 1, 1);

        Label agilityLabel = createLabel("Agility", 14);
        grid.add(agilityLabel, 0, 6, 2, 1);
        agility = createLabel(map(bladeRunner.getAgility()), 14);
        grid.add(agility, 2, 6, 1, 1);
        Label firearmsLabel = createLabel("Firearms", 12);
        grid.add(firearmsLabel, 0, 7, 2, 1);
        firearms = createLabel(map(bladeRunner.getFirearms()), 12);
        grid.add(firearms, 2, 7, 1, 1);
        Label mobilityLabel = createLabel("Mobility", 12);
        grid.add(mobilityLabel, 0, 8, 2, 1);
        mobility = createLabel(map(bladeRunner.getMobility()), 12);
        grid.add(mobility, 2, 8, 1, 1);
        Label stealthLabel = createLabel("Stealth", 12);
        grid.add(stealthLabel, 0, 9, 2, 1);
        stealth = createLabel(map(bladeRunner.getStealth()), 12);
        grid.add(stealth, 2, 9, 1, 1);

        Label intelligenceLabel = createLabel("Intelligence", 14);
        grid.add(intelligenceLabel, 0, 10, 2, 1);
        intelligence = createLabel(map(bladeRunner.getIntelligence()), 14);
        grid.add(intelligence, 2, 10, 1, 1);
        Label medicalAidLabel = createLabel("Medical aid", 12);
        grid.add(medicalAidLabel, 0, 11, 2, 1);
        medicalAid = createLabel(map(bladeRunner.getMedicalAid()), 12);
        grid.add(medicalAid, 2, 11, 2, 1);
        Label observationLabel = createLabel("Observation", 12);
        grid.add(observationLabel, 0, 12, 2, 1);
        observation = createLabel(map(bladeRunner.getObservation()), 12);
        grid.add(observation, 2, 12, 2, 1);
        Label techLabel = createLabel("Tech", 12);
        grid.add(techLabel, 0, 13, 2, 1);
        tech = createLabel(map(bladeRunner.getTech()), 12);
        grid.add(tech, 2, 13, 1, 1);

        Label empathyLabel = createLabel("Empathy", 14);
        grid.add(empathyLabel, 0, 14, 2, 1);
        empathy = createLabel(map(bladeRunner.getEmpathy()), 14);
        grid.add(empathy, 2, 14, 1, 1);
        Label connectionsLabel = createLabel("Connections", 12);
        grid.add(connectionsLabel, 0, 15, 2, 1);
        connections = createLabel(map(bladeRunner.getConnections()), 12);
        grid.add(connections, 2, 15, 1, 1);
        Label insightLabel = createLabel("Insight", 12);
        grid.add(insightLabel, 0, 16, 2, 1);
        insight = createLabel(map(bladeRunner.getInsight()), 12);
        grid.add(insight, 2, 16, 1, 1);
        Label manipulationLabel = createLabel("Manipulation", 12);
        grid.add(manipulationLabel, 0, 17, 2, 1);
        manipulation = createLabel(map(bladeRunner.getManipulation()), 12);
        grid.add(manipulation, 2, 17, 1, 1);
    }

    private void setupAdmin() {
        title = new Text("Admin view");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 3, 0, 2, 1);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void refresh() {
    }

    private Label createLabel(String s, int size) {
        Label label =  new Label(s);
        label.setFont(Font.font("Tahoma", FontWeight.NORMAL, size));
        label.setAlignment(Pos.CENTER_LEFT);
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
