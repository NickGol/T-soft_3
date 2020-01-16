package polynomial;

import com.sun.istack.internal.NotNull;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class PolynomialView extends Application implements PolynomialObserver {

    public static void main(String[] args) {
        launch(args);
    }

    public static final String Column1MapKey = "X";
    public static final String Column2MapKey = "Y";
    private ObservableList<LineChart.Data> listForPoints;// = FXCollections.observableArrayList();
    private ObservableList<LineChart.Data> listForPolynomial;// = FXCollections.observableArrayList();
    PolynomialModelInterface model;
    //PolynomialControllerInterface controller;
    PolynomialController controller;

    Scene scene;

    BorderPane border;
        VBox vbox;
            Text title;
            TableView table;
                TableColumn parameterXColumn;
                TableColumn parameterYColumn;
            HBox hBoxTextFields;
                TextField paramXField;
                TextField paramYField;
            HBox hBoxButtons;
                Button buttonAdd;
                Button buttonDelete;
            Button buttonGenerateNewData;
            Text polynomial;
            TextField polynomialFormula;
        LineChart<Number,Number> lineChart;
            NumberAxis xAxis;
            NumberAxis yAxis;
            LineChart.Series series;
            LineChart.Series series1;

            Text textFieldError;


    @Override
    public void start(Stage primaryStage) {

        model = new PolynomialModel();

        listForPoints = model.getObservableListForPoints();
        listForPolynomial = model.getObservableListForPolynomial();
        model.generateRandomData();
        model.registerObserver(this);

        scene = createScene();
        addEventListeners();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Задание №3. Определение коэффициентов полинома");
        primaryStage.setWidth(800);
        //primaryStage.setHeight(500);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(535);
        primaryStage.setMaximized(true);
        primaryStage.getScene().getStylesheets().add("polynomial/Styles.css");

        primaryStage.show();
        //System.out.println(primaryStage.getHeight());
        //makeButtonAddDisabled();
        makeButtonDeleteDisabled();
        controller = new PolynomialController(model, this);
    }

    private Scene createScene() {
        // Use a border pane as the root for scene
        border = new BorderPane();
        border.setLeft(addVBoxToLeftPanel());
        border.setCenter(addLineChartToCenter());
        return new Scene(border);
    }

    /**
     * Creates a VBox with a list of links for the left region
     */
    private VBox addVBoxToLeftPanel() {

        vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setAlignment(Pos.TOP_CENTER);
        //vbox.setPrefWidth(250);

        title = new Text("Точки полинома");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        textFieldError = new Text("Неправильные данные");
        textFieldError.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        polynomial = new Text("Полином:");
        polynomial.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        polynomialFormula = new TextField("y = 5x^2 + 7x² + 3");
        polynomialFormula.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        polynomialFormula.setEditable(false);
        polynomialFormula.setAlignment(Pos.CENTER);

        parameterXColumn = new TableColumn<>("Парвметр X");
        parameterYColumn = new TableColumn<>("Параметр Y");
        parameterXColumn.setCellValueFactory(new PropertyValueFactory<LineChart.Data,Double>("XValue"));
        parameterYColumn.setCellValueFactory(new PropertyValueFactory<LineChart.Data,String>("YValue"));

        //table = new TableView<>(generateDataInMap());
        table = new TableView<>(listForPoints);
        table.setPrefSize(200, 250);
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(parameterXColumn, parameterYColumn);
        table.setPlaceholder(new Text("No columns available"));
        table.setItems(listForPoints);

        buttonGenerateNewData = new Button("Создать новый набор");
        buttonGenerateNewData.setPrefHeight(20);

        vbox.getChildren().addAll(title, table, addHBoxTextFields(), addHBoxButtons(), buttonGenerateNewData, polynomial, polynomialFormula);
        return vbox;
    }

    private HBox addHBoxTextFields() {

        hBoxTextFields = new HBox();
        hBoxTextFields.setPadding(new Insets(10, 12, 0, 12));
        hBoxTextFields.setSpacing(10);   // Gap between nodes
        //hbox.setStyle("-fx-background-color: #336699;");
        paramXField = new TextField();
        paramXField.setPrefSize(100, 20);
        paramXField.setPromptText("Параметр X");
        paramXField.setText("0");

        paramYField = new TextField();
        paramYField.setPrefSize(100, 20);
        paramYField.setPromptText("Параметр Y");
        paramYField.setText("0");

        hBoxTextFields.getChildren().addAll(paramXField, paramYField);

        return hBoxTextFields;
    }

    private HBox addHBoxButtons() {

        hBoxButtons = new HBox();
        hBoxButtons.setPadding(new Insets(0, 12, 5, 12));
        hBoxButtons.setSpacing(10);   // Gap between nodes
        //hboxButtons.setStyle("-fx-background-color: #336699;");

        buttonAdd = new Button("Добавить");
        buttonAdd.setPrefSize(100, 20);

        buttonDelete = new Button("Удалить");
        buttonDelete.setPrefSize(100, 20);

        hBoxButtons.getChildren().addAll(buttonAdd, buttonDelete);

        return hBoxButtons;
    }

    private LineChart addLineChartToCenter() {
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel("Ось X");
        yAxis.setLabel("Ось Y");
        //creating the chart
        lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("Вид полинома");
        //defining a series
        series = new LineChart.Series(listForPoints);
        series.setName("Координаты");
        series1 = new LineChart.Series(listForPolynomial);
        series1.setName("Полином");
        lineChart.getData().addAll(series1, series);

        return lineChart;
    }

    void addEventListeners() {

        listForPoints.addListener( (ListChangeListener.Change<? extends LineChart.Data> e) -> {
            //TODO: make event handler (Maybe in another place)
            if( listForPoints.size() == 0 ) {
                makeButtonDeleteDisabled();
            }
        });

        paramXField.setOnKeyReleased((value) -> {
            //controller.checkXCoordinate(paramXField.getText());
            //TODO: make event handler
        });

        paramYField.setOnKeyReleased((value) -> {
            controller.checkYCoordinate(paramYField.getText());
            //TODO: make event handler
        });

        buttonAdd.setOnMouseReleased( (value) -> {
            //controller.checkIfXIsUnique(paramXField.getText());
            controller.addNewPoint(paramXField.getText(), paramYField.getText());
            //TODO: make event handler
        });

        buttonDelete.setOnMouseReleased( (value) -> {
            //TODO: make event handler (Maybe in another place)
            //table.getItems().remove(table.getSelectionModel().getSelectedIndex());
            controller.removePoint(table.getSelectionModel().getSelectedIndex());
        });

        buttonGenerateNewData.setOnMouseReleased( (value) -> {
            //TODO: make event handler (Maybe in another place)
            controller.generateNewPointsSet();
        });

        table.setOnMouseClicked( (value) -> {
            if( value.getClickCount()>0 && table.getSelectionModel().getSelectedIndex()>=0) {
                makeButtonDeleteEnabled();
            }
        });
    }

    public void makeButtonAddEnabled() {
        buttonAdd.setDisable(false);
    }

    public void makeButtonAddDisabled() {
        buttonAdd.setDisable(true);
    }

    public void makeButtonDeleteEnabled() {
        buttonDelete.setDisable(false);
    }

    public void makeButtonDeleteDisabled() {
        buttonDelete.setDisable(true);
    }


    public void showCoordinatesErrorText(@NotNull String text) {
        if( !vbox.getChildren().contains(textFieldError) ) {
            int i = vbox.getChildren().indexOf(hBoxTextFields);
            vbox.getChildren().add(i+1, textFieldError);
        }
        textFieldError.setText(text);
    }

    public void hideCoordinatesErrorText() {
        if( vbox.getChildren().contains(textFieldError) ) {
            vbox.getChildren().remove(textFieldError);
        }
    }

    @Override
    public void updatePolynomialText(String polynomial) {
        polynomialFormula.setText(polynomial);
    }
}
