import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard extends Application {

	double mean = 0;
	int number;
	double deviation = 10;
	ArrayList<Double> numberList;
	double sum;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		VBox root = new VBox();

		Button go = new Button("Go");
		go.setPrefWidth(300);
		
		
		HBox meanHBox = new HBox();
		Label meanLabel = new Label("Enter Mean: ");
		TextField meanInput = new TextField();
		meanInput.setPrefWidth(232);
		meanInput.setText(Double.toString(mean));
		
		meanHBox.getChildren().addAll(meanLabel, meanInput);

		HBox deviationHBox = new HBox();
		Label deviationLabel = new Label("Enter Deviation: ");
		TextField deviationInput = new TextField();
		deviationInput.setPrefWidth(211);
		deviationInput.setText(Double.toString(deviation));
		
		deviationHBox.getChildren().addAll(deviationLabel, deviationInput);

		HBox numberHBox = new HBox();
		Label numberLabel = new Label("Enter N: ");
		TextField numberInput = new TextField();
		numberInput.setPrefWidth(252);

		numberHBox.getChildren().addAll(numberLabel, numberInput);

		go.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if (meanInput.getText() != null)
					mean = Double.parseDouble(meanInput.getText());

				if (deviationInput.getText() != null)
					deviation = Double.parseDouble(deviationInput.getText());

				if (numberInput.getText() != null)
					number = Integer.parseInt(numberInput.getText());

				generateList();
				Collections.sort(numberList);

				Stage newStage = new Stage();

				VBox root = new VBox();

				TextField tf = new TextField();
				tf.setText(String.format("Mean: %f Max: %f Min: %f SD: %f Median: %f  ", calculateMean(), getMax(),
						getMin(), calculateStandardDeviation(), calculateMedian()));

				ListView<Double> list = new ListView<Double>();
				ObservableList<Double> items = FXCollections.observableArrayList(numberList);
				list.setItems(items);

				root.getChildren().addAll(tf, list);

				Scene newScene = new Scene(root, 500, 300);
				newStage.setScene(newScene);
				newStage.show();

			}
		});

		root.getChildren().addAll(go, meanHBox, deviationHBox, numberHBox);

		/*
		 * Set primary stage properties and make it visible
		 */
		primaryStage.setTitle("Lab 2");
		primaryStage.setScene(new Scene(root, 300, 110));
		primaryStage.show();

	}

	private void generateList() {
		NormalDistribution nDist = new NormalDistribution(mean, deviation);
		numberList = new ArrayList<Double>(number);

		for (int i = 0; i < number; i++)
			numberList.add(nDist.sample());
	}

	private double calculateMedian() {
		double median = numberList.get(numberList.size() / 2); // This is inefficient but works for now
		return median;
	}

	private double calculateStandardDeviation() {
		double mean = calculateMean();
		List<Double> list = numberList;
		double diffrenceOfSquare;
		double sd;

		differenceOfSquares(list, mean);
		diffrenceOfSquare = sum;

		sd = Math.sqrt(diffrenceOfSquare / list.size());

		return sd;

	}

	private void differenceOfSquares(List<Double> list, double mean) {
		sum = 0;
		list.forEach(item -> sum += (item - mean) * (item - mean));

	}

	private double calculateMean() {
		double sum = 0;
		for (int i = 0; i < numberList.size(); i++)
			sum += numberList.get(i);

		return (sum / numberList.size());
	}

	private double getMax() {
		return numberList.get(numberList.size() - 1);
	}

	private double getMin() {
		return numberList.get(0);
	}

}