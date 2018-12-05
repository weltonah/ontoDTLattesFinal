package br.com.Grafo;

import java.util.ArrayList;
import java.util.Comparator;

public class TrabalhoEvento {
	private String titulo;
	private ArrayList<Pessoa> listParticipante;
	private ArrayList<Evento> listParticipouEvento;

	public TrabalhoEvento(String titulo) {
		super();
		this.titulo = titulo;
		this.listParticipante = new ArrayList<>();
		this.listParticipouEvento = new ArrayList<>();
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

	public ArrayList<Evento> getListParticipouEvento() {
		return this.listParticipouEvento;
	}

	public void setListParticipouEvento(ArrayList<Evento> listParticipouEvento) {
		this.listParticipouEvento = listParticipouEvento;
	}

	public void AddListParticipouEvento(Evento listParticipouEvento) {
		this.listParticipouEvento.add(listParticipouEvento);
	}
}
