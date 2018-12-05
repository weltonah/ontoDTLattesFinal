package br.com;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

import br.com.DAO.BuscarXmlToPessoa;
import br.com.Grafo.Grafo;
import br.com.Grafo.GrafoController;
import br.com.Ontology.OntologyDAO;
import br.com.converter.ConverterFile;
import br.com.converter.TratamentoDeDados;
import br.com.modelo.OntoPessoa;

public class Main {

	
	public static void main(String[] args) throws Exception {
		// Nome do arquivo OWL prenchido localizado na raiz do projeto
		String nomeFile = "Completo.owl";
		// Inicializa ontologia
		OntologyDAO ontoDao = new OntologyDAO(nomeFile);
		TratamentoDeDados tratamentoDeDados = new TratamentoDeDados();
		ArrayList<String> Namexml;
		ontoDao = new OntologyDAO(nomeFile);
		tratamentoDeDados = new TratamentoDeDados();

		// Pega lista de arquivos
		Namexml = ListaDeArquivos(25);
		ArrayList<OntoPessoa> listaPessoa = new ArrayList<>();
		System.out.println("usuarios incluidos");
		// Carrega todos os arquivos
		for (String string : Namexml) {
			// Carrega os arquivos de entradas presentes no
			// src/main/resouser/static/testFile/
			File owlfile = new ClassPathResource("static/Basedados/" + string).getFile();
			// converte o arquivo para um Document
			Document xmlfile = ConverterFile.ConverterFileToDocument(owlfile);
			// retira informações do XML e coloca em memoria
			BuscarXmlToPessoa preencherXMLtoOnto = new BuscarXmlToPessoa(xmlfile);
			OntoPessoa pessoa = new OntoPessoa(tratamentoDeDados.corrigirString(preencherXMLtoOnto.NomeCompleto()),
					tratamentoDeDados.corrigirString(preencherXMLtoOnto.IDLattes()),
					tratamentoDeDados.corrigirString(preencherXMLtoOnto.UltimaAtualizacao()),
					tratamentoDeDados.corrigirString(preencherXMLtoOnto.NomeCitacao()));
			System.out.println(pessoa.getNomeCompleto());
			preencherXMLtoOnto.buscarXML(pessoa);
			listaPessoa.add(pessoa);
		}
		System.out.println("**************5%******************");
		int cont = 0;
		// Tratar as bancas internas e externas para unificação de nomes
		tratamentoDeDados.tratarBancaExterna(listaPessoa, cont);
		System.out.println("numero de banca que foram combinadas " + cont);
		System.out.println("**************20%*****************");
		cont = 0;
		// Tratar as evento internas e externas para unificação de nomes
		tratamentoDeDados.tratarEventoExterna(listaPessoa, cont);
		System.out.println("numero de eventos que foram combinadas " + cont);
		System.out.println("**************30%*****************");
		System.out.println("tamanho pessoas antes da expansao " + listaPessoa.size());
		tratamentoDeDados.ExpansaoMembros(listaPessoa);
		cont = listaPessoa.size();
		System.out.println("tamanho pessoas depois da expansao " + listaPessoa.size());
		System.out.println("**************35%*****************");

		int aux;
		do {
			aux = listaPessoa.size();
			tratamentoDeDados.JuncaoMembros(listaPessoa);
			if (aux != listaPessoa.size())
				break;
		} while (aux != listaPessoa.size());
		System.out.println("antes da unificação " + cont);
		System.out.println("depois da unificação " + listaPessoa.size());
		cont = listaPessoa.size();
		System.out.println("**************40%*****************");
		System.out.println("antes " + listaPessoa.size());
		tratamentoDeDados.eliminarIndividuosDesnecessarios(listaPessoa);
		System.out.println("Individuos descartados: " + (cont - listaPessoa.size()));
		System.out.println("**************45%*****************");

		int totalEvento = 0;
		for (int i = 0; i < listaPessoa.size(); i++)
			totalEvento = totalEvento + listaPessoa.get(i).getListOntoEvento().size();
		int totalcontBanca = 0;
		for (int i = 0; i < listaPessoa.size(); i++) 
			totalcontBanca = totalcontBanca + listaPessoa.get(i).getListOntoBanca().size();
		// Tratar evento
		System.out.println("Eventos antes da unificação : " + totalEvento);
		tratamentoDeDados.tratarEventos(listaPessoa);
		System.out.println("**************65%*****************");
		System.out.println("Banca antes da unificação : " + totalEvento);
		// Tratar banca
		tratamentoDeDados.tratarBancaInterna(listaPessoa);
		System.out.println("**************85%*****************");
		// Preencher ontologia
		ontoDao.preencherOnto(listaPessoa);
		System.out.println("**************90%*****************");
		Map<String, String> MapNome = tratamentoDeDados.CriarMap(listaPessoa);
		listaPessoa = new ArrayList<>();
		// Salvar ontologia
		ontoDao.saveOntologyDAO(new FunctionalSyntaxDocumentFormat());
		System.out.println("**************95%*****************");
		// Criar grafo a partir da OWL
		GrafoController graf = new GrafoController(nomeFile);
		// Inferir relações
		graf.inferir();
		System.out.println("**************100%*****************");
		System.out.println("**************Resultado*****************");
		Grafo result = graf.BuscarResultado();
		// Imprimir resultados
		result.imprimirResultado(MapNome);
		System.out.println("Fim");
	}


	// Lista de todos os arquivos que são utilizados no processamento
	public static ArrayList<String> ListaDeArquivos(int tam) {
		// Tam max 44
		ArrayList<String> Namexml = new ArrayList<>();
		ArrayList<String> result = new ArrayList<>();
		Namexml.add("Alessandreiacurriculo.xml");
		Namexml.add("Alexcurriculo.xml");
		Namexml.add("AndreLuizcurriculo.xml");
		Namexml.add("BernardoMartinscurriculo.xml");
		Namexml.add("Carloscurriculo.xml");
		Namexml.add("Cirocurriculo.xml");
		Namexml.add("Edelbertocurriculo.xml");
		Namexml.add("EdmarOliveiracurriculo.xml");
		Namexml.add("EduardoBarrelecurriculo.xml");
		Namexml.add("EduardoPaganicurriculo.xml");
		Namexml.add("FabricioMartinscurriculo.xml");
		Namexml.add("Fernandacurriculo.xml");
		Namexml.add("Gleiphcurriculo.xml");
		Namexml.add("Hedercurriculo.xml");
		Namexml.add("Heliocurriculo.xml");
		Namexml.add("IgorKnopcurriculo.xml");
		Namexml.add("Itamarcurriculo.xml");
		Namexml.add("IuryHigorcurriculo.xml");
		Namexml.add("Jairocurriculo.xml");
		Namexml.add("JoseMariacurriculo.xml");
		Namexml.add("LeonardoVieiracurriculo.xml");
		Namexml.add("Liamaracurriculo.xml");
		Namexml.add("Lorenzacurriculo.xml");
		Namexml.add("LucianaBrugiolocurriculo.xml");
		Namexml.add("LucianaCamposcurriculo.xml");
		Namexml.add("LucianoJerezcurriculo.xml");
		Namexml.add("LuizFelipecurriculo.xml");
		Namexml.add("MarceloBernardescurriculo.xml");
		Namexml.add("MarceloCaniatocurriculo.xml");
		Namexml.add("MarceloLoboscocurriculo.xml");
		Namexml.add("MarceloMorenocurriculo.xml");
		Namexml.add("MarcoAntoniocurriculo.xml");
		Namexml.add("MarcosPassinicurriculo.xml");
		Namexml.add("MarioAntoniocurriculo.xml");
		Namexml.add("PriscilaCaprilescurriculo.xml");
		Namexml.add("RafaelAlvescurriculo.xml");
		Namexml.add("RaulFonsecacurriculo.xml");
		Namexml.add("ReginaBragacurriculo.xml");
		Namexml.add("RodrigoLuiscurriculo.xml");
		Namexml.add("RodrigoWebercurriculo.xml");
		Namexml.add("SauloMoraescurriculo.xml");
		Namexml.add("Steniocurriculo.xml");
		Namexml.add("Vâniacurriculo.xml");
		Namexml.add("VictorStroelecurriculo.xml");
		Namexml.add("Wagnercurriculo.xml");

		for (int i = 0; i < tam; i++)
			result.add(Namexml.get(i));
		return result;
	}

}
