package br.com.Grafo;

import java.io.File;
import java.util.ArrayList;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class GrafoController {

	private OWLOntologyManager manager;
	private OWLOntology ontology;
	private Grafo grafo;
	private Grafo grafoResultado;

	public GrafoController(String nomeArq) throws OWLOntologyCreationException {
		File file = new File(System.getProperty("user.dir") + "/" + nomeArq);
		this.manager = OWLManager.createOWLOntologyManager();
		this.ontology = this.manager.loadOntologyFromOntologyDocument(file);
		this.grafoResultado = new Grafo();

	}

	public Grafo BuscarResultado() {
		return this.grafoResultado;
	}

	public void inferir() {
		preencherBanca();
		preencherEvento();
		preencherAreaAtuacao();
		preencherOrientacao();
		preencherProjetoPesquisa();
		preencherProjetoEmEvento();
	}

	public void preencherBanca() {
		this.grafo = new Grafo();
		buscarDadosOWL("Banca", "bancaTemParticipante", 0);
		ArrayList<String[]> aux = this.grafo.InferirBanca();
		aux = this.grafo.contRelacoes(aux);
		this.grafoResultado.PreencherResultado(aux, 0);
	}

	public void preencherEvento() {
		this.grafo = new Grafo();
		buscarDadosOWL("Evento", "eventoTemParticipante", 1);
		ArrayList<String[]> aux = this.grafo.InferirEvento();
		aux = this.grafo.contRelacoes(aux);
		this.grafoResultado.PreencherResultado(aux, 1);
	}

	public void preencherAreaAtuacao() {
		this.grafo = new Grafo();
		buscarDadosOWL("AreaAtuacao", "areaAtuacaoTemPesquisador", 2);
		buscarDadosOWL("AreaConhecimento", "areaConhecimentoTemPesquisador", 3);
		buscarDadosOWL("SubArea", "subAreaTemPesquisador", 4);
		buscarDadosOWL("Especialidade", "especialidadeTemPesquisador", 5);
		ArrayList<String[]> aux = this.grafo.InferirAreaAtuacao();
		aux = this.grafo.contRelacoes(aux);
		this.grafoResultado.PreencherResultado(aux, 2);
		ArrayList<String[]> aux2 = this.grafo.InferirAreaConhecimento();
		aux2 = this.grafo.contRelacoes(aux2);
		this.grafoResultado.PreencherResultado(aux2, 3);
		ArrayList<String[]> aux3 = this.grafo.InferirSubArea();
		aux3 = this.grafo.contRelacoes(aux3);
		this.grafoResultado.PreencherResultado(aux3, 4);
		ArrayList<String[]> aux4 = this.grafo.InferirEspecialidade();
		aux4 = this.grafo.contRelacoes(aux4);
		this.grafoResultado.PreencherResultado(aux4, 5);
	}

	public void preencherOrientacao() {
		this.grafo = new Grafo();
		buscarDadosOWL("Pessoa", "foiOrientadoPor", 6);
		ArrayList<String[]> aux = this.grafo.InferirOrientacao();
		aux = this.grafo.contRelacoes(aux);
		this.grafoResultado.PreencherResultado(aux, 6);
	}

	public void preencherProjetoPesquisa() {
		this.grafo = new Grafo();
		buscarDadosOWL("ProjetoPesquisa", "ProjetoTeveParticipante", 7);
		ArrayList<String[]> aux = this.grafo.InferirProjetoPesquisa();
		aux = this.grafo.contRelacoes(aux);
		this.grafoResultado.PreencherResultado(aux, 7);
	}

	public void preencherProjetoEmEvento() {
		this.grafo = new Grafo();
		buscarDadosOWL("Evento", "eventoTemParticipante", 1);
		buscarDadosOWL("Producao", "eventoTeveTrabalhoDe", 8);
		buscarDadosOWL("Producao", "trabalhoEmEvento", 9);
		ArrayList<String[]> aux = this.grafo.InferirProjetoEmEvento();
		aux = this.grafo.contRelacoes(aux);
		this.grafoResultado.PreencherResultado(aux, 8);
	}

	public void buscarDadosOWL(String Classe, String Relacao, int opcao) {
		ArrayList<String[]> aux = new ArrayList<>();
		this.ontology.individualsInSignature().filter(u -> u.isOWLNamedIndividual())
				.filter(u -> this.ontology.classAssertionAxioms(u).findFirst().get().signature().findFirst().get()
						.getIRI().getFragment().contains(Classe))
				.filter(u -> this.ontology.objectPropertyAssertionAxioms(u)
						.anyMatch(i -> i.signature().findFirst().get().getIRI().getFragment().contains(Relacao)))
				.forEach(p -> {
					this.ontology.objectPropertyAssertionAxioms(p)
							.filter(w -> w.signature().findFirst().get().getIRI().getFragment().contains(Relacao))
							.forEach(y -> {
								String[] individuos = new String[2];
								individuos[1] = p.getIRI().getIRIString()
										.substring(p.getIRI().getIRIString().indexOf("#") + 1);
								y.signature().skip(1).forEach(w -> {
									if (!p.getIRI().equals(w.getIRI())) {
										individuos[0] = w.getIRI().getIRIString()
												.substring(w.getIRI().getIRIString().indexOf("#") + 1);
									}
								});
								aux.add(individuos);
							});
				});
		aux.forEach(u -> {
			if (opcao == 0)
				this.grafo.AddBanca(u);
			if (opcao == 1)
				this.grafo.AddEvento(u);
			if (opcao == 2)
				this.grafo.AddAreaAtuacao(u);
			if (opcao == 3)
				this.grafo.AddAreaConhecimento(u);
			if (opcao == 4)
				this.grafo.AddSubArea(u);
			if (opcao == 5)
				this.grafo.AddEspecialidade(u);
			if (opcao == 6)
				this.grafo.AddOrientacao(u);
			if (opcao == 7)
				this.grafo.AddProjetoPesquisa(u);
			if (opcao == 8)
				this.grafo.AddProjetoEventoePessoa(u);
			if (opcao == 9)
				this.grafo.AddProjetoEventoParaEvento(u);

		});

	}
}
