package pkgApp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javafx.beans.value.*;

import pkgApp.RetirementApp;
import pkgCore.Retirement;

public class RetirementController implements Initializable {

	private RetirementApp mainApp = null;
	@FXML
	private TextField txtSaveEachMonth;
	@FXML
	private TextField txtYearsToWork;
	@FXML
	private TextField txtAnnualReturnWorking;
	@FXML
	private TextField txtWhatYouNeedToSave;
	@FXML
	private TextField txtYearsRetired;
	@FXML
	private TextField txtAnnualReturnRetired;
	@FXML
	private TextField txtRequiredIncome;
	@FXML
	private TextField txtMonthlySSI;

	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();

	public RetirementApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(RetirementApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Adding an entry in the hashmap for each TextField control I want to validate
		// with a regular expression
		// "\\d*?" - means any decimal number
		// "\\d*(\\.\\d*)?" means any decimal, then optionally a period (.), then
		// decmial
		hmTextFieldRegEx.put(txtYearsToWork, "\\d*?");
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "\\d*(\\.\\d*)?");

		// Check out these pages (how to validate controls):
		// https://stackoverflow.com/questions/30935279/javafx-input-validation-textfield
		// https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value?rq=1
		// https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0
		// There are some examples on how to validate / check format

		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();

			txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
									Boolean newPropertyValue) {
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if (!newPropertyValue) {
						if (!txtField.getText().matches(strRegEx)) {
							txtField.setText("");
							txtField.requestFocus();
						}
					}
				}
			});
		}

		intWorkValidation(txtYearsToWork);
		percentValidation(txtAnnualReturnWorking);
		intRetiredValidation(txtYearsRetired);
		percentValidation(txtAnnualReturnRetired);
		intReqInValidation(txtRequiredIncome);
		intSSIValidation(txtMonthlySSI);

	}



	@FXML
	public void btnClear(ActionEvent event) {
		System.out.println("Clear pressed");

		// disable read-only controls
		txtSaveEachMonth.setDisable(true);
		txtWhatYouNeedToSave.clear();

		// Clear, enable txtYearsToWork
		txtYearsToWork.clear();
		txtYearsToWork.setDisable(false);

		txtAnnualReturnWorking.clear();
		txtAnnualReturnWorking.setDisable(false);

		txtWhatYouNeedToSave.clear();
		txtWhatYouNeedToSave.setDisable(true);

		txtYearsRetired.clear();
		txtYearsRetired.setDisable(false);

		txtAnnualReturnRetired.clear();
		txtAnnualReturnRetired.setDisable(false);

		txtRequiredIncome.clear();
		txtRequiredIncome.setDisable(false);

		txtMonthlySSI.clear();
		txtMonthlySSI.setDisable(false);

	}

	@FXML
	public void btnCalculate() {

		System.out.println("calculating");
		int YearsToWork = Integer.parseInt(txtYearsToWork.getText());
		double AnnualReturnWorking = Double.parseDouble(txtAnnualReturnWorking.getText());
		int YearsRetired = Integer.parseInt(txtYearsRetired.getText());
		double AnnualReturnRetired= Double.parseDouble(txtAnnualReturnRetired.getText());
		int RequiredIncome= Integer.parseInt(txtRequiredIncome.getText());
		int MonthlySSI= Integer.parseInt(txtMonthlySSI.getText());

		txtSaveEachMonth.setDisable(false);
		txtWhatYouNeedToSave.setDisable(false);

		Retirement ret = new Retirement(YearsToWork, AnnualReturnWorking, YearsRetired, AnnualReturnRetired, RequiredIncome, MonthlySSI);

		txtSaveEachMonth.setText(Double.toString(ret.MonthlySavings()));
		txtWhatYouNeedToSave.setText(Double.toString(ret.TotalAmountToSave()));
		// as input
	}
	
	private void percentValidation(TextField field){
		field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if(!newValue){
				if(!field.getText().matches("[0]\\.[0][1-9]|[0]\\.[1][0]")){
					field.setText("");
					field.requestFocus();
				}
			}
		});
	}

	public void intWorkValidation(TextField field){
		field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if(!newValue){
				if(!field.getText().matches("[0-9]|[0-3][0-9]|4[0]")){
					field.setText("");
					field.requestFocus();
				}
			}
		});
	}

	public void intReqInValidation(TextField field){
		field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if(!newValue){
				if(!field.getText().matches("[2][6-9][4-9][2-9]|[3-9][0-9][0-9][0-9]|10[0][0][0]")){
					field.setText("");
					field.requestFocus();
				}
			}
		});
	}

	public void intSSIValidation(TextField field){
		field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if(!newValue){
				if(!field.getText().matches("[0-9]|[0-9][0-9]|[0-9][0-9][0-9]|[0-2][0-6][0-4][0-2]")){
					field.setText("");
					field.requestFocus();
				}
			}
		});
	}

	public void intRetiredValidation(TextField field){
		field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if(!newValue){
				if(!field.getText().matches("[0-9]|[0-1][0-9]|2[0]")){
					field.setText("");
					field.requestFocus();
				}
			}
		});
	}

}
