package br.com.Ontology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import br.com.DAO.ReadFile;
import br.com.converter.TratamentoDeDados;
import br.com.modelo.OntoClass;
import br.com.modelo.OntoPessoa;
import br.com.modelo.TriplaOwl;

public class OntologyDAO {

	private File file;
	private OWLOntologyManager manager;
	private OWLOntology ontology;
	private IRI DATALATTESIRI = IRI.create("http://www.datalattes.com/ontologies/datalattes.owl");

	// Construtor
	public OntologyDAO(String NomeArq) throws OWLOntologyCreationException {
		this.manager = OWLManager.createOWLOntologyManager();
		// pega uma copia do arquivo
		this.file = ReadFile.PegarFile(NomeArq);
		// carrega ontologia
		this.ontology = this.manager.loadOntologyFromOntologyDocument(this.file);
	}

	public void saveOntologyDAO(OWLDocumentFormat formato)
			throws OWLOntologyStorageException, FileNotFoundException, OWLOntologyCreationException {
		limparDadosDesnecessario();
		this.manager.saveOntology(this.ontology, formato, new FileOutputStream(this.file));
		diferentIndividual();
		this.manager.saveOntology(this.ontology, formato, new FileOutputStream(this.file));
	}

	public void diferentIndividual() throws OWLOntologyCreationException {
		this.manager = OWLManager.createOWLOntologyManager();
		this.ontology = this.manager.loadOntologyFromOntologyDocument(this.file);
		OWLDataFactory factory = this.manager.getOWLDataFactory();
		OWLDifferentIndividualsAxiom diffInd = factory
				.getOWLDifferentIndividualsAxiom(this.ontology.getIndividualsInSignature());
		this.ontology.add(diffInd);
	}

	public void preencherOnto(ArrayList<OntoPessoa> listapessoa)
			throws OWLOntologyStorageException, FileNotFoundException, OWLOntologyCreationException {
		for (OntoPessoa pessoa : listapessoa) {
			preencherDadosGerais(pessoa);
			preencherAreaAtuacao(pessoa);
			preencherProjetoPesquisa(pessoa);
			preencherEvento(pessoa);
			preencherFormacao(pessoa, listapessoa);
			preencherBanca(pessoa);
			preencherTrabalhoEvento(pessoa);
		}
	}

	public void preencherDadosGerais(OntoPessoa pessoa) {
		String nomeclatura = (pessoa.getIdLattes() == "" || pessoa.getIdLattes().isEmpty()
				|| pessoa.getIdLattes() == null) ? pessoa.getNomeCompleto() : pessoa.getIdLattes();
		addIndividual(nomeclatura, "Pessoa");
		addAtribNoIndivido(nomeclatura, pessoa.getIdLattes(), "IdLattes");
		addAtribNoIndivido(nomeclatura, pessoa.getNomeCompleto(), "NomeCompleto");
	}

	public void preencherProjetoPesquisa(OntoPessoa pessoa) {
		String nomeclatura = (pessoa.getIdLattes() == "" || pessoa.getIdLattes().isEmpty()
				|| pessoa.getIdLattes() == null) ? pessoa.getNomeCompleto() : pessoa.getIdLattes();
		pessoa.getListOntoProjetoPesquisa().forEach(u -> {
			addIndividual(u.getTitulo(), u.getTipo());
			addRelacaoInd(nomeclatura, u.getTitulo(), "TrabalhoEmProjetoPesquisa");
			addRelacaoInd(u.getTitulo(), nomeclatura, "ProjetoTeveParticipante");
		});
	}

	public void preencherEvento(OntoPessoa pessoa) {
		String nomeclatura = (pessoa.getIdLattes() == "" || pessoa.getIdLattes().isEmpty()
				|| pessoa.getIdLattes() == null) ? pessoa.getNomeCompleto() : pessoa.getIdLattes();
		pessoa.getListOntoEvento().forEach(u -> {
			addIndividual(u.getTitulo(), u.getTipo());
			addRelacaoInd(nomeclatura, u.getTitulo(), "participouEvento");
			addRelacaoInd(u.getTitulo(), nomeclatura, "eventoTemParticipante");
		});
	}

	public void preencherAreaAtuacao(OntoPessoa pessoa) {
		String nomeclatura = (pessoa.getIdLattes() == "" || pessoa.getIdLattes().isEmpty()
				|| pessoa.getIdLattes() == null) ? pessoa.getNomeCompleto() : pessoa.getIdLattes();
		pessoa.getListOntoAreaAtuacao().forEach(u -> {
			if (u.getGrandeArea().length() > 0) {
				addIndividual(u.getGrandeArea(), "AreaAtuacao");
				addRelacaoInd(nomeclatura, u.getGrandeArea(), "temAreaAtuacao");
				addRelacaoInd(u.getGrandeArea(), nomeclatura, "areaAtuacaoTemPesquisador");
			}
			if (u.getAreaConhecimento().length() > 0) {
				addIndividual(u.getAreaConhecimento(), "AreaConhecimento");
				addRelacaoInd(nomeclatura, u.getAreaConhecimento(), "temAreaConhecimento");
				addRelacaoInd(u.getAreaConhecimento(), nomeclatura, "areaConhecimentoTemPesquisador");
			}

			if (u.getSubAreaConhecimento().length() > 0) {
				addIndividual(u.getSubAreaConhecimento(), "SubArea");
				addRelacaoInd(nomeclatura, u.getSubAreaConhecimento(), "temSubArea");
				addRelacaoInd(u.getSubAreaConhecimento(), nomeclatura, "subAreaTemPesquisador");
			}
			if (u.getNomeEspecialidade().length() > 0) {
				addIndividual(u.getNomeEspecialidade(), "Especialidade");
				addRelacaoInd(nomeclatura, u.getNomeEspecialidade(), "temEspecialidade");
				addRelacaoInd(u.getNomeEspecialidade(), nomeclatura, "especialidadeTemPesquisador");
			}
		});
	}

	public void preencherFormacao(OntoPessoa pessoa, ArrayList<OntoPessoa> listapessoa) {
		String nomeclatura = (pessoa.getIdLattes() == "" || pessoa.getIdLattes().isEmpty()
				|| pessoa.getIdLattes() == null) ? pessoa.getNomeCompleto() : pessoa.getIdLattes();
		pessoa.getListOntoFormacao().forEach(u -> {
			String nome = "";
			if (u.isFlagFormacaoOrientacao()) {
				first: for (OntoPessoa ontoPessoa : listapessoa) {
					if (!ontoPessoa.equals(pessoa)) {

						for (OntoClass ontoClass : ontoPessoa.getListOntoFormacao()) {
							if ((!ontoClass.isFlagFormacaoOrientacao())
									&& (ontoClass.getTitulo().contentEquals(u.getTitulo()))) {
								nome = (ontoPessoa.getIdLattes() == "" || ontoPessoa.getIdLattes().isEmpty()
										|| ontoPessoa.getIdLattes() == null) ? ontoPessoa.getNomeCompleto()
												: ontoPessoa.getIdLattes();
								break first;
							}
						}
					}
				}
				addIndividual(nome, "Pessoa");
				addRelacaoInd(nome, nomeclatura, "orientou");
				addRelacaoInd(nomeclatura, nome, "foiOrientadoPor");
			}
		});
	}

	public void preencherBanca(OntoPessoa pessoa) {
		String nomeclatura = (pessoa.getIdLattes() == "" || pessoa.getIdLattes().isEmpty()
				|| pessoa.getIdLattes() == null) ? pessoa.getNomeCompleto() : pessoa.getIdLattes();
		pessoa.getListOntoBanca().forEach(u -> {
			addIndividual(u.getTitulo(), u.getTipo());
			addRelacaoInd(nomeclatura, u.getTitulo(), "participouDeBanca");
			addRelacaoInd(u.getTitulo(), nomeclatura, "bancaTemParticipante");
		});
	}

	public void preencherTrabalhoEvento(OntoPessoa pessoa) {
		String nomeclatura = (pessoa.getIdLattes() == "" || pessoa.getIdLattes().isEmpty()
				|| pessoa.getIdLattes() == null) ? pessoa.getNomeCompleto() : pessoa.getIdLattes();
		pessoa.getListOntoTrabalhoEvento().forEach(u -> {
			addIndividual(u.getTituloTrabalho(), "Producao");
			addIndividual(u.getEvento().getTitulo(), "Evento");
			addRelacaoInd(nomeclatura, u.getTituloTrabalho(), "apresentouTrabalhoEvento");
			addRelacaoInd(u.getTituloTrabalho(), nomeclatura, "eventoTeveTrabalhoDe");
			addRelacaoInd(u.getTituloTrabalho(), u.getEvento().getTitulo(), "trabalhoEmEvento");
			addRelacaoInd(u.getEvento().getTitulo(), u.getTituloTrabalho(), "eventoTeveTrabalho");
		});
	}

	public void limparDadosDesnecessario() {
		for (TriplaOwl triplaOwl : TratamentoDeDados.listaPessoaDesnecessario(this.ontology)) {
			removeIndividual(triplaOwl.getSujeito());
		}
		for (TriplaOwl triplaOwl : TratamentoDeDados.listaEventoDesnecessario(this.ontology)) {
			removeIndividual(triplaOwl.getSujeito());
		}
		for (TriplaOwl triplaOwl : TratamentoDeDados.listaBancaDesnecessario(this.ontology)) {
			removeIndividual(triplaOwl.getSujeito());
		}
	}

	public void removeIndividual(IRI objeto) {
		OWLDataFactory factory = this.manager.getOWLDataFactory();
		OWLNamedIndividual nome = factory.getOWLNamedIndividual(objeto);
		OWLEntityRemover remover = new OWLEntityRemover(this.ontology);
		remover.visit(nome);
		this.manager.applyChanges(remover.getChanges());
	}

	public void addIndividual(String Nome, String Tipo) {
		OWLDataFactory factory = this.manager.getOWLDataFactory();
		OWLIndividual nome = factory.getOWLNamedIndividual(this.DATALATTESIRI + "#", Nome);
		OWLDifferentIndividualsAxiom diffInd = factory.getOWLDifferentIndividualsAxiom(nome);
		OWLClass personClass = factory.getOWLClass(this.DATALATTESIRI + "#", Tipo);
		OWLClassAssertionAxiom da = factory.getOWLClassAssertionAxiom(personClass, nome);
		this.ontology.add(da);
		this.ontology.add(diffInd);
	}

	public void addAtribNoIndivido(String Nome, String valor, String Tipo) {
		OWLDataFactory factory = this.manager.getOWLDataFactory();
		OWLIndividual individual = factory.getOWLNamedIndividual(this.DATALATTESIRI + "#", Nome);
		OWLDataProperty dataProp = factory.getOWLDataProperty(this.DATALATTESIRI + "#", Tipo);
		OWLDataPropertyAssertionAxiom da = factory.getOWLDataPropertyAssertionAxiom(dataProp, individual, valor);
		this.ontology.add(da);
	}

	public void addRelacaoInd(String NomePrimeiro, String NomeSegundo, String Relacao) {
		OWLDataFactory factory = this.manager.getOWLDataFactory();
		OWLIndividual individual = factory.getOWLNamedIndividual(this.DATALATTESIRI + "#", NomePrimeiro);
		OWLIndividual individual2 = factory.getOWLNamedIndividual(this.DATALATTESIRI + "#", NomeSegundo);
		OWLObjectProperty obj = factory.getOWLObjectProperty(this.DATALATTESIRI + "#", Relacao);
		OWLObjectPropertyAssertionAxiom da = factory.getOWLObjectPropertyAssertionAxiom(obj, individual, individual2);
		this.ontology.add(da);
	}


}
