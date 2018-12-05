package br.com.Grafo;

import java.util.ArrayList;

public class Pessoa {
	private String nome;
	private ArrayList<Banca> listParticipouBanca;
	private ArrayList<Evento> listParticipouEvento;
	private ArrayList<AreaAtuacao> listParticipouAreaAtuacao;
	private ArrayList<AreaConhecimento> listParticipouAreaConhecimento;
	private ArrayList<SubArea> listParticipouSubArea;
	private ArrayList<Especialidade> listParticipouEspecialidade;
	private ArrayList<Pessoa> listOrientadores;
	private ArrayList<Pessoa> listAlunosOrientados;
	private ArrayList<ProjetoPesquisa> listParticipouProjetoPesquisa;
	private ArrayList<TrabalhoEvento> listParticipouTrabalhoEvento;
	private ArrayList<Resultado> listResultado;

	public Pessoa(String nome) {
		super();
		this.nome = nome;
		this.listParticipouBanca = new ArrayList<>();
		this.listParticipouEvento = new ArrayList<>();
		this.listParticipouAreaAtuacao = new ArrayList<>();
		this.listParticipouAreaConhecimento = new ArrayList<>();
		this.listParticipouSubArea = new ArrayList<>();
		this.listParticipouEspecialidade = new ArrayList<>();
		this.listOrientadores = new ArrayList<>();
		this.listAlunosOrientados = new ArrayList<>();
		this.listParticipouProjetoPesquisa = new ArrayList<>();
		this.listParticipouTrabalhoEvento = new ArrayList<>();
		this.listResultado = new ArrayList<>();
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ArrayList<Banca> getListParticipouBanca() {
		return this.listParticipouBanca;
	}

	public void setListParticipouBanca(ArrayList<Banca> listParticipouBanca) {
		this.listParticipouBanca = listParticipouBanca;
	}

	public void AddListParticipouBanca(Banca listParticipouBanca) {
		this.listParticipouBanca.add(listParticipouBanca);
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

	public void setListParticipouAreaAtuacao(ArrayList<AreaAtuacao> listParticipouAreaAtuacao) {
		this.listParticipouAreaAtuacao = listParticipouAreaAtuacao;
	}

	public void AddListParticipouAreaAtuacao(AreaAtuacao listParticipouAreaAtuacao) {
		this.listParticipouAreaAtuacao.add(listParticipouAreaAtuacao);
	}

	public ArrayList<AreaConhecimento> getListParticipouAreaConhecimento() {
		return this.listParticipouAreaConhecimento;
	}

	public void setListParticipouAreaConhecimento(ArrayList<AreaConhecimento> listParticipouAreaConhecimento) {
		this.listParticipouAreaConhecimento = listParticipouAreaConhecimento;
	}

	public void AddListParticipouAreaConhecimento(AreaConhecimento listParticipouAreaConhecimento) {
		this.listParticipouAreaConhecimento.add(listParticipouAreaConhecimento);
	}

	public ArrayList<SubArea> getListParticipouSubArea() {
		return this.listParticipouSubArea;
	}

	public void setListParticipouSubArea(ArrayList<SubArea> listParticipouSubArea) {
		this.listParticipouSubArea = listParticipouSubArea;
	}

	public void AddListParticipouSubArea(SubArea listParticipouSubArea) {
		this.listParticipouSubArea.add(listParticipouSubArea);
	}

	public ArrayList<Especialidade> getListParticipouEspecialidade() {
		return this.listParticipouEspecialidade;
	}

	public void setListParticipouEspecialidade(ArrayList<Especialidade> listParticipouEspecialidade) {
		this.listParticipouEspecialidade = listParticipouEspecialidade;
	}

	public void AddListParticipouEspecialidade(Especialidade listParticipouEspecialidade) {
		this.listParticipouEspecialidade.add(listParticipouEspecialidade);
	}

	public ArrayList<Pessoa> getListOrientadores() {
		return this.listOrientadores;
	}

	public void setListOrientadores(ArrayList<Pessoa> listOrientadores) {
		this.listOrientadores = listOrientadores;
	}

	public void AddListOrientadores(Pessoa listOrientadores) {
		this.listOrientadores.add(listOrientadores);
	}

	public ArrayList<Pessoa> getListAlunosOrientados() {
		return this.listAlunosOrientados;
	}

	public void setListAlunosOrientados(ArrayList<Pessoa> listAlunosOrientados) {
		this.listAlunosOrientados = listAlunosOrientados;
	}

	public void AddListAlunosOrientados(Pessoa listAlunosOrientados) {
		this.listAlunosOrientados.add(listAlunosOrientados);
	}

	public ArrayList<AreaAtuacao> getListParticipouAreaAtuacao() {
		return this.listParticipouAreaAtuacao;
	}

	public ArrayList<ProjetoPesquisa> getListParticipouProjetoPesquisa() {
		return this.listParticipouProjetoPesquisa;
	}

	public void setListParticipouProjetoPesquisa(ArrayList<ProjetoPesquisa> listParticipouProjetoPesquisa) {
		this.listParticipouProjetoPesquisa = listParticipouProjetoPesquisa;
	}

	public void AddListParticipouProjetoPesquisa(ProjetoPesquisa listParticipouProjetoPesquisa) {
		this.listParticipouProjetoPesquisa.add(listParticipouProjetoPesquisa);
	}

	public ArrayList<TrabalhoEvento> getListParticipouTrabalhoEvento() {
		return this.listParticipouTrabalhoEvento;
	}

	public void setListParticipouTrabalhoEvento(ArrayList<TrabalhoEvento> listParticipouTrabalhoEvento) {
		this.listParticipouTrabalhoEvento = listParticipouTrabalhoEvento;
	}

	public void AddListParticipouTrabalhoEvento(TrabalhoEvento listParticipouTrabalhoEvento) {
		this.listParticipouTrabalhoEvento.add(listParticipouTrabalhoEvento);
	}

	public ArrayList<Resultado> getListResultado() {
		return this.listResultado;
	}

	public void setListResultado(ArrayList<Resultado> listResultado) {
		this.listResultado = listResultado;
	}

	public void AddListResultado(Resultado listResultado) {
		this.listResultado.add(listResultado);
	}

}
