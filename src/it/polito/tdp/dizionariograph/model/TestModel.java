package it.polito.tdp.dizionariograph.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		
		model.createGraph(5);
		System.out.println(String.format("**Grafo creato**\n"));
		System.out.println(model.getGrafo());
		
		List<String> vicini = model.displayNeighbours("xds");
		System.out.println("Neighbours di casa: " + vicini + "\n");
		
		System.out.println("Cerco il vertice con grado massimo...");
		System.out.println(model.findMaxDegree());
	}

}
