package br.com.Grafo;

import java.util.ArrayList;
import java.util.Comparator;

public class AreaAtuacao {
	private String titulo;
	private ArrayList<Pessoa> listParticipante;

	public AreaAtuacao(String titulo) {
		super();
		this.titulo = titulo;
		this.listParticipante = new ArrayList<>();
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
}
