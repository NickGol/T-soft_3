package polynomial;

public class PolynomialController implements PolynomialControllerInterface {

    PolynomialModelInterface model;
    PolynomialView view;
    boolean xCoordinate, yCoordinate;

    public PolynomialController(PolynomialModelInterface model, PolynomialView view) {
        this.model = model;
        this.view = view;
        xCoordinate = true;
        yCoordinate = true;
    }


    @Override
    public void addNewPoint(String xCoordinate, String yCoordinate) {
        if( checkIfXIsUnique(xCoordinate) ) {
            model.addDataItem(Double.valueOf(xCoordinate), Double.valueOf(yCoordinate));
        }
    }

    @Override
    public boolean checkIfXIsUnique(String xCoordinate) {
        if( model.checkIfXIsUnique(Double.valueOf(xCoordinate)) ) {
            view.hideCoordinatesErrorText();
            return true;
        } else {
            view.showCoordinatesErrorText("Координата \"X\" уже задана.");
            return false;
        }
    }

    @Override
    public void checkXCoordinate(String text) {
        try {
            Double.valueOf(text);
            view.makeButtonAddEnabled();
            xCoordinate = true;
            changeAddButtonState();
        } catch (NumberFormatException e) {
            view.makeButtonAddDisabled();
            xCoordinate = false;
            changeAddButtonState();
        }
    }

    @Override
    public void checkYCoordinate(String text) {
        try {
            Double.valueOf(text);
            view.makeButtonAddEnabled();
            yCoordinate = true;
            changeAddButtonState();
        } catch (NumberFormatException e) {
            view.makeButtonAddDisabled();
            yCoordinate = false;
            changeAddButtonState();
        }
    }

    @Override
    public void generateNewPointsSet() {
        model.generateRandomData();
    }

    @Override
    public void removePoint(int index) {
        if( index>=0 )
            model.deleteDataItem(index);
        else {
            view.makeButtonDeleteDisabled();
        }
    }

    private void changeAddButtonState() {
        if( xCoordinate && yCoordinate ) {
            view.makeButtonAddEnabled();
            view.hideCoordinatesErrorText();
        } else {
            view.makeButtonAddDisabled();
            view.showCoordinatesErrorText("Неправильный формат данных.");
        }
    }
}
