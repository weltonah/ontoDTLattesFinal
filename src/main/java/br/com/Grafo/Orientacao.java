package br.com.Grafo;

import java.util.ArrayList;
import java.util.Comparator;

public class Orientacao {
	private Pessoa orientador;
	private ArrayList<Pessoa> listParticipante;

	public Orientacao(Pessoa orientador) {
		super();
		this.orientador = orientador;
		this.listParticipante = new ArrayList<>();
	}

	public Pessoa getOrientador() {
		return this.orientador;
	}

	public void setOrientador(Pessoa orientador) {
		this.orientador = orientador;
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
