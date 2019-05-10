package it.polito.tdp.dizionariograph.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.dizionariograph.db.WordDAO;

public class Model {
	
	private Map<String, String> idMap;
	private Graph<String, DefaultEdge> graph;
	private List<String> parole;
	Map<String, String> backVisit;
	
	private class EdgeTraversedGraphListener implements TraversalListener<String, DefaultEdge> {

		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> ev) {
			
			String sourceString = graph.getEdgeSource(ev.getEdge());
			String destinationString = graph.getEdgeTarget(ev.getEdge());
			if(!backVisit.containsKey(destinationString) && backVisit.containsKey(sourceString)) {
				backVisit.put(destinationString, sourceString);
			}
			else if(!backVisit.containsKey(sourceString) && backVisit.containsKey(destinationString)){
				backVisit.put(sourceString, destinationString);
			}
			
		}

		@Override
		public void vertexFinished(VertexTraversalEvent<String> arg0) {	
		}

		@Override
		public void vertexTraversed(VertexTraversalEvent<String> arg0) {
		}
		
	}
	
	public void createGraph(int numeroLettere) {
		
		//creo il grafo
		this.graph = new SimpleDirectedGraph<>(DefaultEdge.class);
		
		//ottengo tutte le parole
		WordDAO dao = new WordDAO();
		this.parole = dao.getAllWordsFixedLength(numeroLettere);
		
		//creo idMap
		this.idMap = new HashMap<>();
		for(String stemp:this.parole) {
			idMap.put(stemp, stemp);
		}
		
		//metto i vertici al grafo
		Graphs.addAllVertices(this.graph, this.parole);
		
		//scorro tutte le parole per vedere se ci sono delle parole che differiscono per 1 lettera
		for(String partenza:this.graph.vertexSet()) {
			for(String arrivo:this.graph.vertexSet()) {
				if(this.sonoConnesse(partenza, arrivo)) {
					this.graph.addEdge(partenza, arrivo);
				}
			}
		}
		
	}

	public List<String> displayNeighbours(String parolaInserita) {
		
		List<String> result = new ArrayList<String>();
		
		backVisit = new HashMap<>();
		GraphIterator<String, DefaultEdge> it = new BreadthFirstIterator<>(this.graph, parolaInserita);
		it.addTraversalListener(new Model.EdgeTraversedGraphListener());
		
		//backVisit.put(parolaInserita, null);
		while(it.hasNext()) {
			String figlio = it.next();
			if(this.sonoConnesse(parolaInserita, figlio)) {
				result.add(figlio);
			}
		}
		
		return result;
	
	}

	public int findMaxDegree() {
		
		int maxDegree=-1;
		for(String stemp:this.graph.vertexSet()) {
			GraphIterator<String, DefaultEdge> it = new BreadthFirstIterator<>(this.graph, stemp);
			int cnt=0;
			while(it.hasNext()) {
				String figlio = it.next();
				if(this.sonoConnesse(stemp, figlio)) {
					cnt++;
				}
			}
			if(cnt>maxDegree) {
				maxDegree=cnt;
			}
		}
		
		return maxDegree;
	}
	
	public Graph<String, DefaultEdge> getGrafo() {
		return this.graph;
	}
	
	public boolean sonoConnesse(String p1, String p2) {
		if(p1.equals(p2))
			return false;
		int k=0;
		for(int i=0; i<p1.length(); i++) {
			if(p1.charAt(i)!=p2.charAt(i)) {
				k++;
			}
		}
		if(k==1)
			return true;
		return false;
	}
	
	public void cancellaGrafo() {
		this.graph=null;
	}

	public String espandiGrafo() {
		String temp = "";
		for(String stemp : this.graph.vertexSet()) {
			temp+=(stemp+"\n");
		}
		return temp;
	}
	
}
