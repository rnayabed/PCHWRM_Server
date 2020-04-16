package PCHWRMServer;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Section;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class monitorUI extends StackPane {

    Label notConnectedPaneHeadingLabel;
    Label notConnectedPaneSubHeadingLabel;
    Label GPUModelNameLabel;
    Label CPUModelNameLabel;
    Label memorySubHeadingLabel;
    Label videoMemorySubHeadingLabel;

    Gauge CPULoadGauge;
    Gauge CPUTempGauge;
    Gauge GPULoadGauge;
    Gauge GPUTempGauge;
    Gauge memoryGauge;
    Gauge videoMemoryGauge;
    Gauge CPUFanSpeedGauge;
    Gauge GPUFanSpeedGauge;

    VBox notConnectedPane;
    VBox gaugesPane;

    public void loadNodes()
    {
        Font.loadFont(getClass().getResource("assets/Roboto.ttf").toExternalForm().replace("%20"," "), 13);
        getStylesheets().add(getClass().getResource("assets/style.css").toExternalForm());


        //notConnectedPane
        notConnectedPane = new VBox();
        notConnectedPane.getStyleClass().add("pane");
        notConnectedPane.setSpacing(10);
        notConnectedPane.setAlignment(Pos.CENTER);

        notConnectedPaneHeadingLabel = new Label("Listening for Connection");
        notConnectedPaneHeadingLabel.getStyleClass().add("h1");
        notConnectedPaneSubHeadingLabel = new Label("Server running on XXX.XXX.X.X, Port XXXX");
        notConnectedPaneSubHeadingLabel.getStyleClass().add("h4");

        notConnectedPane.getChildren().addAll(notConnectedPaneHeadingLabel,notConnectedPaneSubHeadingLabel);

        //GaugePane
        gaugesPane = new VBox();
        gaugesPane.getStyleClass().add("pane");
        gaugesPane.setSpacing(35);
        gaugesPane.setAlignment(Pos.CENTER);
        
        VBox CPUBox = new VBox();
        CPUBox.setSpacing(10);
        CPUBox.setAlignment(Pos.CENTER);
        
        Label CPUHeadingLabel = new Label("CPU");
        CPUHeadingLabel.getStyleClass().add("h2");
        
        CPUModelNameLabel = new Label("");
        CPUModelNameLabel.getStyleClass().add("h4");
        
        HBox CPULoadGaugeBox = new HBox();
        CPULoadGaugeBox.setSpacing(10);
        Region r1 = new Region();
        HBox.setHgrow(r1, Priority.ALWAYS);

        CPULoadGauge = new Gauge();
        CPULoadGauge.setMaxValue(100);
        CPULoadGauge.setSkinType(Gauge.SkinType.TILE_SPARK_LINE);
        CPULoadGauge.setBackgroundPaint(Color.BLACK);
        CPULoadGauge.setTitleColor(Color.WHITE);
        CPULoadGauge.setTitle("LOAD - %");
        CPULoadGauge.setValueColor(Color.WHITE);
        CPULoadGauge.setUnitColor(Color.WHITE);
        CPULoadGauge.setCache(true);
        CPULoadGauge.setCacheHint(CacheHint.SPEED);
        
        CPUTempGauge = new Gauge();
        CPUTempGauge.setSkinType(Gauge.SkinType.SIMPLE_SECTION);
        CPUTempGauge.setTitleColor(Color.WHITE);
        CPUTempGauge.setTitle("TEMP");
        CPUTempGauge.setValue(0);
        CPUTempGauge.setValueColor(Color.WHITE);
        CPUTempGauge.setUnitColor(Color.WHITE);
        CPUTempGauge.setSections(new Section(0,65,Color.GREEN), new Section(65.01,75,Color.ORANGE), new Section(75.01,100,Color.RED));
        CPUTempGauge.setUnit("°C");
        CPUTempGauge.setCache(true);
        CPUTempGauge.setCacheHint(CacheHint.SPEED);

        CPULoadGaugeBox.getChildren().addAll(CPULoadGauge,r1,CPUTempGauge);
        
        CPUBox.getChildren().addAll(CPUHeadingLabel,CPUModelNameLabel,CPULoadGaugeBox);



        VBox GPUBox = new VBox();
        GPUBox.setSpacing(10);
        GPUBox.setAlignment(Pos.CENTER);

        Label GPUHeadingLabel = new Label("GPU");
        GPUHeadingLabel.getStyleClass().add("h2");

        GPUModelNameLabel = new Label("");
        GPUModelNameLabel.getStyleClass().add("h4");

        HBox GPULoadGaugeBox = new HBox();
        GPULoadGaugeBox.setSpacing(10);
        Region r2 = new Region();
        HBox.setHgrow(r2, Priority.ALWAYS);

        GPULoadGauge = new Gauge();
        GPULoadGauge.setMaxValue(100);
        GPULoadGauge.setSkinType(Gauge.SkinType.TILE_SPARK_LINE);
        GPULoadGauge.setBackgroundPaint(Color.BLACK);
        GPULoadGauge.setTitleColor(Color.WHITE);
        GPULoadGauge.setTitle("LOAD - %");
        GPULoadGauge.setValueColor(Color.WHITE);
        GPULoadGauge.setUnitColor(Color.WHITE);
        GPULoadGauge.setCache(true);
        GPULoadGauge.setCacheHint(CacheHint.SPEED);

        GPUTempGauge = new Gauge();
        GPUTempGauge.setSkinType(Gauge.SkinType.SIMPLE_SECTION);
        GPUTempGauge.setTitleColor(Color.WHITE);
        GPUTempGauge.setTitle("TEMP");
        GPUTempGauge.setValue(0);
        GPUTempGauge.setValueColor(Color.WHITE);
        GPUTempGauge.setUnitColor(Color.WHITE);
        GPUTempGauge.setSections(new Section(0,65,Color.GREEN), new Section(65.01,75,Color.ORANGE), new Section(75.01,100,Color.RED));
        GPUTempGauge.setUnit("°C");
        GPUTempGauge.setCache(true);
        GPUTempGauge.setCacheHint(CacheHint.SPEED);

        GPULoadGaugeBox.getChildren().addAll(GPULoadGauge, r2, GPUTempGauge);

        GPUBox.getChildren().addAll(GPUHeadingLabel,GPUModelNameLabel,GPULoadGaugeBox);

        Region rr = new Region();
        HBox.setHgrow(rr, Priority.ALWAYS);

        HBox upperRow = new HBox(CPUBox, rr, GPUBox);
        upperRow.setSpacing(35);

        //Lower Row

        VBox memoryBox = new VBox();
        memoryBox.setAlignment(Pos.CENTER);
        memoryBox.setSpacing(10);

        Label memoryHeadingLabel = new Label("RAM");
        memoryHeadingLabel.getStyleClass().add("h2");

        memorySubHeadingLabel = new Label("");
        memorySubHeadingLabel.getStyleClass().add("h4");

        memoryGauge = new Gauge();
        memoryGauge.setSkinType(Gauge.SkinType.SIMPLE_SECTION);
        memoryGauge.setTitleColor(Color.WHITE);
        memoryGauge.setValue(0);
        memoryGauge.setValueColor(Color.WHITE);
        memoryGauge.setUnitColor(Color.WHITE);
        memoryGauge.setSections(new Section(0,65,Color.GREEN), new Section(65.01,75,Color.ORANGE), new Section(75.01,100,Color.RED));
        memoryGauge.setUnit("%");
        memoryGauge.setCache(true);
        memoryGauge.setCacheHint(CacheHint.SPEED);

        memoryBox.getChildren().addAll(memoryHeadingLabel, memorySubHeadingLabel, memoryGauge);


        VBox videoMemoryBox = new VBox();
        videoMemoryBox.setAlignment(Pos.CENTER);
        videoMemoryBox.setSpacing(10);

        Label videoMemoryHeadingLabel = new Label("VRAM");
        videoMemoryHeadingLabel.getStyleClass().add("h2");

        videoMemorySubHeadingLabel = new Label("");
        videoMemorySubHeadingLabel.getStyleClass().add("h4");

        videoMemoryGauge = new Gauge();
        videoMemoryGauge.setSkinType(Gauge.SkinType.SIMPLE_SECTION);
        videoMemoryGauge.setTitleColor(Color.WHITE);
        videoMemoryGauge.setValue(0);
        videoMemoryGauge.setValueColor(Color.WHITE);
        videoMemoryGauge.setUnitColor(Color.WHITE);
        videoMemoryGauge.setSections(new Section(0,65,Color.GREEN), new Section(65.01,75,Color.ORANGE), new Section(75.01,100,Color.RED));
        videoMemoryGauge.setUnit("%");
        videoMemoryGauge.setCache(true);
        videoMemoryGauge.setCacheHint(CacheHint.SPEED);

        videoMemoryBox.getChildren().addAll(videoMemoryHeadingLabel, videoMemorySubHeadingLabel, videoMemoryGauge);



        Region ss = new Region();

        VBox fanSpeedVBox = new VBox();
        fanSpeedVBox.setAlignment(Pos.CENTER);
        fanSpeedVBox.setSpacing(10);

        Label fanSpeedLabel = new Label("FAN SPEED");
        fanSpeedLabel.getStyleClass().add("h2");

        HBox fanSpeedGaugeBox = new HBox();
        fanSpeedGaugeBox.setSpacing(15);

        CPUFanSpeedGauge = new Gauge();
        CPUFanSpeedGauge.setSkinType(Gauge.SkinType.FLAT);
        CPUFanSpeedGauge.setTitleColor(Color.WHITE);
        CPUFanSpeedGauge.setTitle("CPU");
        CPUFanSpeedGauge.setValue(50);
        CPUFanSpeedGauge.setMaxValue(1);
        CPUFanSpeedGauge.setValueColor(Color.WHITE);
        CPUFanSpeedGauge.setUnitColor(Color.WHITE);
        CPUFanSpeedGauge.setUnit("RPM");
        CPUFanSpeedGauge.setCache(true);
        CPUFanSpeedGauge.setCacheHint(CacheHint.SPEED);

        Region r3 = new Region();
        HBox.setHgrow(r3, Priority.ALWAYS);

        GPUFanSpeedGauge = new Gauge();
        GPUFanSpeedGauge.setSkinType(Gauge.SkinType.FLAT);
        GPUFanSpeedGauge.setTitleColor(Color.WHITE);
        GPUFanSpeedGauge.setTitle("GPU");
        GPUFanSpeedGauge.setValue(500);
        GPUFanSpeedGauge.setValueColor(Color.WHITE);
        GPUFanSpeedGauge.setUnitColor(Color.WHITE);
        GPUFanSpeedGauge.setUnit("RPM");
        GPUFanSpeedGauge.setCache(true);
        GPUFanSpeedGauge.setCacheHint(CacheHint.SPEED);

        fanSpeedGaugeBox.getChildren().addAll(CPUFanSpeedGauge, r3, GPUFanSpeedGauge);

        fanSpeedVBox.getChildren().addAll(fanSpeedLabel, fanSpeedGaugeBox);

        HBox lowerRow = new HBox(memoryBox, videoMemoryBox, ss, fanSpeedVBox);
        lowerRow.setSpacing(35);

        ss.prefWidthProperty().bind(rr.widthProperty());


        gaugesPane.getChildren().addAll(upperRow, lowerRow);


        getChildren().addAll(notConnectedPane,gaugesPane);

        gaugesPane.setPadding(new Insets(5));
        notConnectedPane.toFront();

    }

    enum pane{
        notConnected, gauges
    }

    pane currentPane = pane.notConnected;

    void switchPane(pane p)
    {
        currentPane = p;
        if(currentPane == pane.gauges)
        {
            gaugesPane.toFront();
            resetAllNodes();
        }
        else if(currentPane == pane.notConnected)
            notConnectedPane.toFront();
    }

    void resetAllNodes()
    {
        CPULoadGauge.setValue(0);
        GPULoadGauge.setValue(0);
        CPUFanSpeedGauge.setValue(0);
        GPUFanSpeedGauge.setValue(0);
        CPUTempGauge.setValue(0);
        GPUTempGauge.setValue(0);
        memoryGauge.setValue(0);
        videoMemoryGauge.setValue(0);
        CPUModelNameLabel.setText("");
        GPUModelNameLabel.setText("");
        memorySubHeadingLabel.setText("");
        videoMemorySubHeadingLabel.setText("");
    }
}
