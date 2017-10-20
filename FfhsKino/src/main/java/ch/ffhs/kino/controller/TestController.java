package ch.ffhs.kino.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class TestController  implements Initializable, ControlledScreen {

	ScreensController myController;
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		// TODO Auto-generated method stub
		myController = screenPage;
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
