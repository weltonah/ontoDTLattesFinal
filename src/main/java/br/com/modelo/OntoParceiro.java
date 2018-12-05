package br.com.modelo;

public class OntoParceiro {
	private String nome;
	private String citacao;
	private String Id;

	public OntoParceiro(String nome, String citacao, String id) {
		this.nome = nome;
		this.citacao = (citacao == "" || citacao.isEmpty() || citacao == null) ? "" : citacao;
		this.Id = id;
	}

	public OntoParceiro(String nome, String id) {
		this.nome = nome;
		this.citacao = "";
		this.Id = id;
	}

	public OntoParceiro(String id) {
		this.Id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCitacao() {
		return this.citacao;
	}

	public void setCitacao(String citacao) {
		this.citacao = citacao;
	}

	public String getId() {
		return this.Id;
	}

	public void setId(String id) {
		this.Id = id;
	}

	@Override
	public String toString() {
		return "OntoParceiro [nome=" + this.nome + ", citacao=" + this.citacao + ", Id=" + this.Id + "]";
	}
}
