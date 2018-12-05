package br.com.Grafo;

import java.util.ArrayList;
import java.util.Comparator;

public class Relacao {

	private String nome;
	private ArrayList<String> listParticipante;

	public Relacao(String nome) {
		super();
		this.nome = nome;
		this.listParticipante = new ArrayList<String>();
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ArrayList<String> getListParticipante() {
		return this.listParticipante;
	}

	public void setListParticipante(ArrayList<String> listParticipante) {
		this.listParticipante = listParticipante;
	}

	public void AddListParticipante(String listParticipante) {
		this.listParticipante.add(listParticipante);
		this.listParticipante.sort(Comparator.comparing(u -> u));
	}

}
