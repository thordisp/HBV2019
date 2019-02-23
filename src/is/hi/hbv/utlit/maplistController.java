package is.hi.hbv.utlit;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.*;
import java.util.Map.Entry;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;


//   .setScene(new Scene(root, 700, 450));

import java.util.*;
public class maplistController {

    @FXML
    private VBox root_vbox;
    @FXML
    private ListView<String> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private ToggleButton contrast_togglebutton;
    @FXML
    private ToggleButton size_togglebutton;


    private final HashMap<String, ArrayList<Comparable<?>>> hm = new HashMap<>();
    Group zoomGroup;


    @FXML
    void zoomIn(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }

    @FXML
    void stylingContrast(ActionEvent event) {
        if (contrast_togglebutton.isSelected() == true) {
            root_vbox.getStyleClass().add("contrast");
        } else {
            root_vbox.getStyleClass().remove("contrast");
        }
    }

    @FXML
    void stylingSizing(ActionEvent event) {
        if (size_togglebutton.isSelected() == true) {
            root_vbox.getStyleClass().add("touch-sizes");
        } else {
            root_vbox.getStyleClass().remove("touch-sizes");
        }
    }


    @FXML
    void initialize() {

        // nota paint to get x and y
        hm.put("Capital area/Reykjavik", new ArrayList<>(Arrays.asList(260.0, 545.0, "Code: KHAF\nElevation: 66ft")));
        hm.put("Keflavik", new ArrayList<>(Arrays.asList(200.0, 580.0, "Code: KHWD\nElevation: 52ft")));
        hm.put("North/Akureyri", new ArrayList<>(Arrays.asList(600.0, 215.0, "Code: KNUQ\nElevation: 32ft")));
        hm.put("South/Vestmanneyjar", new ArrayList<>(Arrays.asList(410.0, 690.0, "Code: KPAO\nElevation: 7ft")));
        hm.put("East/Husavik", new ArrayList<>(Arrays.asList(1007.0, 275.0, "Code: KRHV\nElevation: 135ft")));
        hm.put("West/Olafsvik", new ArrayList<>(Arrays.asList(93.0, 394.0, "Code: KSQL\nElevation: 52ft")));
        hm.put("All areas", new ArrayList<>(Arrays.asList(563.0, 364.0, "Code: KSFO\nElevation: 13ft")));

        ObservableList<String> names = FXCollections.observableArrayList();
        Set<Entry<String, ArrayList<Comparable<?>>>> set = hm.entrySet();
        Iterator<Entry<String, ArrayList<Comparable<?>>>> i = set.iterator();
        while (i.hasNext()) {
            Map.Entry<String, ArrayList<Comparable<?>>> me = i.next();
            names.add((String) me.getKey());
        }
        Collections.sort(names);

        map_listview.setItems(names);
        map_pin.setVisible(false);

        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));

        // Wrap scroll content in a Group so ScrollPane re-computes scroll bars
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);


        // Add large UI styling and make full screen if we are on device
        if (Platform.isSupported(ConditionalFeature.INPUT_TOUCH)) {
            size_togglebutton.setSelected(true);
            root_vbox.getStyleClass().add("touch-sizes");
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            root_vbox.setPrefSize(bounds.getWidth(), bounds.getHeight());
        }

    }

    @FXML
    void listClicked(MouseEvent event) {
        String item = map_listview.getSelectionModel().getSelectedItem();
        List<Comparable<?>> list = hm.get(item);

        // animation scroll to new position
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = (Double) list.get(0) / mapWidth;
        double scrollV = (Double) list.get(1) / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // move the pin and set it's info
        double pinW = map_pin.getBoundsInLocal().getWidth();
        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX((Double) list.get(0) - (pinW / 2));
        map_pin.setLayoutY((Double) list.get(1) - (pinH));
        pin_info.setText((String) list.get(2));
        map_pin.setVisible(true);
    }

    private void zoom(double scaleValue) {
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }
}
