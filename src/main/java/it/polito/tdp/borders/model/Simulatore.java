package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	// Modello --> stato del sistema
	private Graph<Country, DefaultEdge> grafo;
	
	// tipi di evento --> coda prioritaria 
	private PriorityQueue<Evento> queue; 
	
	// parametri della simulazione
	private int N_MIGRANTI = 1000; 
	private Country partenza; 
	
	// valori in output
	private int T = -1; 
	private Map<Country, Integer> stanziali; 
	
	public void init(Country country, Graph<Country, DefaultEdge> grafo) {
		
		this.partenza = country;
		this.grafo = grafo; 
		
		//imposto stato imniziale 
		this.T = 1; 
		this.stanziali = new HashMap<Country, Integer>();
		
		for (Country c : this.grafo.vertexSet()) {
			stanziali.put(c, 0);
		}
		//creo coda
		this.queue = new PriorityQueue<Evento>();
		//inserisco primo evento 
		this.queue.add(new Evento(T,partenza,N_MIGRANTI));
	}
	public void run() {
		// finchè la coda non si svuola, prendo un  evento e lo eseguo
		
		Evento e;
		while((e=this.queue.poll()) != null) {
			//simulo evento
			
			this.T = e.getT();
			int nPersone = e.getN();
			Country stato = e.getCountry();
			
			//ottengo i vicini 
			List<Country> vicini = Graphs.neighborListOf(this.grafo, stato);
			
			int migrantiPerStato = (nPersone/2)/vicini.size();
			
			if(migrantiPerStato > 0) {
				// le persone si possono muovere 
				for(Country confinante : vicini) {
					queue.add(new Evento(e.getT() + 1, confinante, migrantiPerStato));
				}
			}
			int stanziali = nPersone - migrantiPerStato*vicini.size();
			this.stanziali.put(stato, this.stanziali.get(stato) + stanziali);
		}
	}
	
	public Map<Country,Integer> getStanziali(){
		return this.stanziali;
	}

	public Integer getT() {
		return this.T;
	}
	
	
	
	
	
	
}
