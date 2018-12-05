package br.com.modelo;

import org.semanticweb.owlapi.model.IRI;

public class TriplaOwl {
	IRI sujeito;
	IRI predicado;
	IRI objeto;

	public TriplaOwl(IRI sujeito) {
		super();
		this.sujeito = sujeito;
	}

	public TriplaOwl(IRI sujeito, IRI predicado, IRI objeto) {
		super();
		this.sujeito = sujeito;
		this.predicado = predicado;
		this.objeto = objeto;
	}

	public IRI getSujeito() {
		return this.sujeito;
	}

	public void setSujeito(IRI sujeito) {
		this.sujeito = sujeito;
	}

	public IRI getPredicado() {
		return this.predicado;
	}

	public void setPredicado(IRI predicado) {
		this.predicado = predicado;
	}

	public IRI getObjeto() {
		return this.objeto;
	}

	public void setObjeto(IRI objeto) {
		this.objeto = objeto;
	}
}
