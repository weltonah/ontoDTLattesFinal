package br.com.modelo;

public class TrabalhoEventoXml {
	private String tituloTrabalho;
	private OntoClass evento;

	public TrabalhoEventoXml(String tituloTrabalho, OntoClass evento) {
		super();
		this.tituloTrabalho = tituloTrabalho;
		this.evento = evento;
	}

	public String getTituloTrabalho() {
		return this.tituloTrabalho;
	}

	public void setTituloTrabalho(String tituloTrabalho) {
		this.tituloTrabalho = tituloTrabalho;
	}

	public OntoClass getEvento() {
		return this.evento;
	}

	public void setEvento(OntoClass evento) {
		this.evento = evento;
	}

	@Override
	public String toString() {
		return "TrabalhoEvento [tituloTrabalho=" + this.tituloTrabalho + ", evento=" + this.evento.toString() + "]";
	}
}
