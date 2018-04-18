package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rim.ItemStatistics;
import rim.Product;
import rim.Sale;

public class StatisticsViewController implements Initializable {
	@FXML private ListView listView;
	@FXML private ComboBox comboBox;
	@FXML private CheckBox itemCheck;
	@FXML private CheckBox monthCheck;
	@FXML private TableView<Sale> tableView;
	@FXML private TableColumn<Sale, String> dateColumn;
	@FXML private TableColumn<Sale, String> quantityColumn;
	@FXML private TableColumn<Sale, String> saleColumn;
	@FXML private TableColumn<Sale, String> idColumn;
	@FXML private Text totalSaleLabel;
	@FXML private Text quantitySoldLabel;
	
	private static Product product;
	private static String month;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Product> products = Product.getProducts();
		for(Product p : products) {
			listView.getItems().add(p.getName());
		}
		
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		// for combo box
		this.initializeMonths();
	}
	
	public void getDataPressed() {
		String pName = (String) listView.getSelectionModel().getSelectedItem();
		if (pName != null) {
			product = Product.findProduct(pName);
		}
		
		month = (String) comboBox.getSelectionModel().getSelectedItem();
		
		// fill table with data
		// PropertyValueFactory string corresponds to the members in the Sale class
		dateColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("date"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("quantity"));
		saleColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("totalSale"));
		idColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("saleId"));
		ObservableList os = getSales();
		tableView.setItems(os);
		
		
	}
	
	
	/**
	 * stores table data in ObservableList
	 * @return
	 */
	private ObservableList<Sale> getSales() {
		ItemStatistics is = new ItemStatistics(product, month);
		List<Sale> sales = is.getSales();
		
		ObservableList<Sale> os = FXCollections.observableArrayList();
		for (Sale s : sales) {
			os.add(new Sale(s.getSaleId(), s.getDate(), s.getProductId(), s.getProductName(),
					s.getTotalSale(), s.getSalesTax(), s.getQuantity()));
		}
		
		// set total sale and quantity sold
	    totalSaleLabel.setText(String.format("Total Sale: $%.2f", is.calculateTotalSale()));
	    quantitySoldLabel.setText("Quantity Sold: " + is.calculateTotalQuantity());
		return os;
	}
	
	
	public void returnToInventory(ActionEvent event) throws IOException {
		Parent inventoryViewParent = FXMLLoader.load(getClass().getResource("InventoryView.fxml"));
		Scene inventoryViewScene = new Scene(inventoryViewParent,1200, 800);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(inventoryViewScene);
		window.show();
	}
	
	/**
	 * return to login page
	 */
	public void returnToLogin(ActionEvent event) throws IOException{
		Parent loginViewParent = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
		Scene loginViewScene = new Scene(loginViewParent);
		
		// get the stage information
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(loginViewScene);
		window.show();
		
	}
	
	private void initializeMonths() {
		comboBox.getItems().add("January");
		comboBox.getItems().add("February");
		comboBox.getItems().add("March");
		comboBox.getItems().add("April");
		comboBox.getItems().add("May");
		comboBox.getItems().add("June");
		comboBox.getItems().add("July");
		comboBox.getItems().add("August");
		comboBox.getItems().add("September");
		comboBox.getItems().add("October");
		comboBox.getItems().add("November");
		comboBox.getItems().add("December");
	}

}
