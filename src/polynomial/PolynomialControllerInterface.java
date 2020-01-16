package polynomial;

public interface PolynomialControllerInterface {
    void addNewPoint(String xCoordinate, String yCoordinate);
    boolean checkIfXIsUnique(String xCoordinate);
    void checkXCoordinate(String text);
    void checkYCoordinate(String text);
    void generateNewPointsSet();
    void removePoint(int index);
}
