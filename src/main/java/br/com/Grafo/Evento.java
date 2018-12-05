package br.com.Grafo;

import java.util.ArrayList;
import java.util.Comparator;

public class Evento {
	private String titulo;
	private ArrayList<Pessoa> listParticipante;
	private ArrayList<TrabalhoEvento> listTrabalhoEvento;

	public Evento(String titulo) {
		super();
		this.titulo = titulo;
		this.listParticipante = new ArrayList<>();
		this.listTrabalhoEvento = new ArrayList<>();
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public ArrayList<Pessoa> getListParticipante() {
		return this.listParticipante;
	}

	public void setListParticipante(ArrayList<Pessoa> listParticipante) {
		this.listParticipante = listParticipante;
	}

	public void AddListParticipante(Pessoa listParticipante) {
		this.listParticipante.add(listParticipante);
		this.listParticipante.sort(Comparator.comparing(u -> u.getNome()));

	}

	public ArrayList<TrabalhoEvento> getListTrabalhoEvento() {
		return this.listTrabalhoEvento;
	}

	public void setListTrabalhoEvento(ArrayList<TrabalhoEvento> listTrabalhoEvento) {
		this.listTrabalhoEvento = listTrabalhoEvento;
	}

	public void AddListTrabalhoEvento(TrabalhoEvento listTrabalhoEvento) {
		this.listTrabalhoEvento.add(listTrabalhoEvento);
		this.listTrabalhoEvento.sort(Comparator.comparing(u -> u.getTitulo()));
	}
}
