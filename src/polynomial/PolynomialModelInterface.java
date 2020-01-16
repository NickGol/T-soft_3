package polynomial;


import com.sun.istack.internal.NotNull;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;

public interface PolynomialModelInterface {
    ObservableList<LineChart.Data> getObservableListForPoints();
    ObservableList<LineChart.Data> getObservableListForPolynomial();
    boolean checkIfXIsUnique(@NotNull Double xCoordinate);
    void deleteDataItem(int index);
    void addDataItem(@NotNull Double xCoordinate, @NotNull Double yCoordinate);
    void readDataFromFile(@NotNull String filePath);
    void generateRandomData();
    void registerObserver(PolynomialObserver observer);
    void removeObserver(PolynomialObserver observer);
}
