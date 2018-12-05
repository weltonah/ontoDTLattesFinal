package br.com.Grafo;

import java.util.ArrayList;
import java.util.Comparator;

public class Resultado {
	private final int valorBanca = 10;
	private final int valorEvento = 10;
	private final int valorAreaAtuacao = 30;
	private final int valorAreaConhecimento = 60;
	private final int valorSubArea = 80;
	private final int valorEspecialidade = 100;
	private final int valorOrientacao = 10;
	private final int valorProjetoPesquisa = 20;
	private final int valorProjetoEvento = 30;
	private int contBanca;
	private int contEvento;
	private int contAreaAtuacao;
	private int contAreaConhecimento;
	private int contSubArea;
	private int contEspecialidade;
	private int contOrientacao;
	private int contProjetoPesquisa;
	private int contProjetoEvento;
	private ArrayList<Pessoa> listParticipante;

	public Resultado() {
		super();
		this.listParticipante = new ArrayList<>();
	}

	public int getTotal() {
		int result = 0;
		result = result + (this.contBanca * this.valorBanca);
		result = result + (this.contEvento * this.valorEvento);
		result = result + (this.contAreaAtuacao * this.valorAreaAtuacao);
		result = result + (this.contAreaConhecimento * this.valorAreaConhecimento);
		result = result + (this.contSubArea * this.valorSubArea);
		result = result + (this.contEspecialidade * this.valorEspecialidade);
		result = result + (this.contOrientacao * this.valorOrientacao);
		result = result + (this.contProjetoPesquisa * this.valorProjetoPesquisa);
		result = result + (this.contProjetoEvento * this.valorProjetoEvento);
		return result;
	}

	public void Addcriterio(String valor, int tipo) {
		switch (tipo) {
		case 0:
			this.setContBanca(Integer.parseInt(valor));
			break;
		case 1:
			this.setContEvento(Integer.parseInt(valor));
			break;
		case 2:
			this.setContAreaAtuacao(Integer.parseInt(valor));
			break;
		case 3:
			this.setContAreaConhecimento(Integer.parseInt(valor));
			break;
		case 4:
			this.setContSubArea(Integer.parseInt(valor));
			break;
		case 5:
			this.setContEspecialidade(Integer.parseInt(valor));
			break;
		case 6:
			this.setContOrientacao(Integer.parseInt(valor));
			break;
		case 7:
			this.setContProjetoPesquisa(Integer.parseInt(valor));
			break;
		case 8:
			this.setContProjetoEvento(Integer.parseInt(valor));
			break;
		default:
			break;
		}

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

	public int getContBanca() {
		return this.contBanca;
	}

	public void setContBanca(int contBanca) {
		this.contBanca = contBanca;
	}

	public int getContEvento() {
		return this.contEvento;
	}

	public void setContEvento(int contEvento) {
		this.contEvento = contEvento;
	}

	public int getContAreaAtuacao() {
		return this.contAreaAtuacao;
	}

	public void setContAreaAtuacao(int contAreaAtuacao) {
		this.contAreaAtuacao = contAreaAtuacao;
	}

	public int getContAreaConhecimento() {
		return this.contAreaConhecimento;
	}

	public void setContAreaConhecimento(int contAreaConhecimento) {
		this.contAreaConhecimento = contAreaConhecimento;
	}

	public int getContSubArea() {
		return this.contSubArea;
	}

	public void setContSubArea(int contSubArea) {
		this.contSubArea = contSubArea;
	}

	public int getContEspecialidade() {
		return this.contEspecialidade;
	}

	public void setContEspecialidade(int contEspecialidade) {
		this.contEspecialidade = contEspecialidade;
	}

	public int getContOrientacao() {
		return this.contOrientacao;
	}

	public void setContOrientacao(int contOrientacao) {
		this.contOrientacao = contOrientacao;
	}

	public int getContProjetoPesquisa() {
		return this.contProjetoPesquisa;
	}

	public void setContProjetoPesquisa(int contProjetoPesquisa) {
		this.contProjetoPesquisa = contProjetoPesquisa;
	}

	public int getContProjetoEvento() {
		return this.contProjetoEvento;
	}

	public void setContProjetoEvento(int contProjetoEvento) {
		this.contProjetoEvento = contProjetoEvento;
	}

	@Override
	public String toString() {
		return "Resultado [contBanca=" + this.contBanca + ", contEvento=" + this.contEvento + ", contAreaAtuacao="
				+ this.contAreaAtuacao + ", contAreaConhecimento=" + this.contAreaConhecimento + ", contSubArea="
				+ this.contSubArea + ", contEspecialidade=" + this.contEspecialidade + ", contOrientacao="
				+ this.contOrientacao + ", contProjetoPesquisa=" + this.contProjetoPesquisa + ", contProjetoEvento="
				+ this.contProjetoEvento + ", listParticipante=" + this.listParticipante.get(0).getNome() + " ,"
				+ this.listParticipante.get(1).getNome() + "]";
	}
}
