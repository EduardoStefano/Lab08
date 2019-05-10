package it.polito.tdp.dizionariograph;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.dizionariograph.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DizionarioGraphController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtNumeroLettere;

    @FXML
    private TextField txtParolaDaCercare;

    @FXML
    private Button btnGeneraGrafo;

    @FXML
    private Button btnTrovaVicini;

    @FXML
    private Button btnTrovaGradoMax;

    @FXML
    private TextArea txtResult;

    @FXML
    private Button btnReset;

    @FXML
    void doErase(ActionEvent event) {
    	txtResult.clear();
    	txtParolaDaCercare.clear();
    	txtNumeroLettere.clear();
    	this.model.cancellaGrafo();
    }

    @FXML
    void doGeneraGrafo(ActionEvent event) {
    	txtResult.clear();
    	int numeroLettere = -1;
    	
    	try {
    		numeroLettere = Integer.parseInt(txtNumeroLettere.getText()); 
    		this.model.createGraph(numeroLettere);
    		txtResult.appendText("Creato grafo con "+this.model.getGrafo().vertexSet().size()+" vertici e "+this.model.getGrafo().edgeSet().size()+" archi\n");
    		txtResult.appendText(model.espandiGrafo());
    	}
    	catch (NumberFormatException e){
    		txtResult.appendText("Inserire un numero e non un carattere");
    	}
    	

    }

    @FXML
    void doTrovaGradoMax(ActionEvent event) {
    	txtResult.clear();
    	int gradoMassimo=-1;
    	try {
    		gradoMassimo = this.model.findMaxDegree();
    		txtResult.appendText(gradoMassimo+"");
    	}
    	catch(RuntimeException e) {
    		txtResult.appendText("Grafo non generato, inserire un numero di lettere e premere Genera grafo");
    	}
    	
    }

    @FXML
    void doTrovaVicini(ActionEvent event) {
    	txtResult.clear();
    	String parolaDaCercare = txtParolaDaCercare.getText();
    	try {
    		List<String> risultato = new LinkedList<>();
    		risultato = this.model.displayNeighbours(parolaDaCercare);
    		for(String stemp:risultato) {
    			txtResult.appendText(stemp+"\n");
    		}
    	}
    	catch(IllegalArgumentException e) {
    		txtResult.appendText("Inserire una parola che sia presente nel dizionario");
    	}
    	catch(RuntimeException e) {
    		txtResult.appendText("Non è stato generato nessun grafo");
    	}
    }

    @FXML
    void initialize() {
        assert txtNumeroLettere != null : "fx:id=\"txtNumeroLettere\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert txtParolaDaCercare != null : "fx:id=\"txtParolaDaCercare\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert btnGeneraGrafo != null : "fx:id=\"btnGeneraGrafo\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert btnTrovaVicini != null : "fx:id=\"btnTrovaVicini\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert btnTrovaGradoMax != null : "fx:id=\"btnTrovaGradoMax\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model=model;
    }
}