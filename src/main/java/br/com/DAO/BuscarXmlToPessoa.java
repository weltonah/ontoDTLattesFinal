package br.com.DAO;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.converter.TratamentoDeDados;
import br.com.modelo.AreaAtuacaoXML;
import br.com.modelo.OntoClass;
import br.com.modelo.OntoParceiro;
import br.com.modelo.OntoPessoa;
import br.com.modelo.TrabalhoEventoXml;

public class BuscarXmlToPessoa {
	XPath xpath;
	public Document xmlfile;
	public TratamentoDeDados tratamentoDeDados;

	public BuscarXmlToPessoa(Document xmlfile) {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		this.xpath = xPathfactory.newXPath();
		this.xmlfile = xmlfile;
		this.tratamentoDeDados = new TratamentoDeDados();
	}

	public OntoPessoa buscarXML(OntoPessoa pessoa) throws XPathExpressionException {
		pessoa.setListOntoAreaAtuacao(listOntoAreaAtuacao());
		pessoa.setListOntoTrabalhoEvento(listOntoTrabalhoEvento());
		pessoa.setListOntoEvento(listOntoEvento());
		pessoa.setListOntoOrgEvento(listOrganizacaoEvento());
		pessoa.setListOntoFormacao(listOntoFormacao());
		pessoa.setListOntoProjetoPesquisa(listOntoProjetoPesquisa());
		pessoa.setListOntoBanca(listOntoBanca());
		TratamentoDeDados.EventoeTrabalho(pessoa);
		return pessoa;
	}

	public String UltimaAtualizacao() throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile("string(/CURRICULO-VITAE[1]/@DATA-ATUALIZACAO)");
		StringBuilder sb = new StringBuilder(expr.evaluate(this.xmlfile));
		sb.insert(8, " ");
		sb.insert(4, "/");
		sb.insert(2, "/");
		return sb.toString();
	}

	public String IDLattes() throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile("string(/CURRICULO-VITAE[1]/@NUMERO-IDENTIFICADOR)");
		return expr.evaluate(this.xmlfile);
	}

	public String NomeCompleto() throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile("string(/*/DADOS-GERAIS[1]/@NOME-COMPLETO)");
		return expr.evaluate(this.xmlfile);
	}

	public String NomeCitacao() throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile("string(/*/DADOS-GERAIS[1]/@NOME-EM-CITACOES-BIBLIOGRAFICAS)");
		return expr.evaluate(this.xmlfile);
	}

	public String ResumoCV() throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile("string(/*/DADOS-GERAIS/RESUMO-CV[1]/@TEXTO-RESUMO-CV-RH)");
		return expr.evaluate(this.xmlfile);
	}

	public boolean DedicaoExclusiva() throws XPathExpressionException {
		XPathExpression expr = this.xpath
				.compile("//ATUACAO-PROFISSIONAL/VINCULOS[@FLAG-DEDICACAO-EXCLUSIVA='SIM' and  @ANO-FIM='']");
		NodeList dedicacao = (NodeList) expr.evaluate(this.xmlfile, XPathConstants.NODESET);
		if (dedicacao.getLength() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<TrabalhoEventoXml> listOntoTrabalhoEvento() throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile("//TRABALHO-EM-EVENTOS");
		NodeList livros = (NodeList) expr.evaluate(this.xmlfile, XPathConstants.NODESET);
		ArrayList<TrabalhoEventoXml> listResult = new ArrayList<>();
		for (int i = 0; i < livros.getLength(); i++) {
			Node TipoNode = livros.item(i);
			String tituloTrabalho = TipoNode.getChildNodes().item(0).getAttributes().getNamedItem("TITULO-DO-TRABALHO")
					.getTextContent();
			String tituloEvento = TipoNode.getChildNodes().item(1).getAttributes().getNamedItem("NOME-DO-EVENTO")
					.getTextContent();
			int ano = Integer.valueOf(
					TipoNode.getChildNodes().item(0).getAttributes().getNamedItem("ANO-DO-TRABALHO").getTextContent());
			OntoClass evento = new OntoClass(this.tratamentoDeDados.corrigirString(tituloEvento), "TrabalhoEmEvento", ano);
			TrabalhoEventoXml item = new TrabalhoEventoXml(this.tratamentoDeDados.corrigirString(tituloTrabalho), evento);
			listResult.add(item);
		}
		return listResult;
	}

	public ArrayList<OntoClass> listOntoBanca() throws XPathExpressionException {
		ArrayList<OntoClass> result = new ArrayList<>();
		result.addAll(BuscaBanca("//PARTICIPACAO-EM-BANCA-DE-GRADUACAO", "Banca"));
		result.addAll(BuscaBanca("//PARTICIPACAO-EM-BANCA-DE-MESTRADO", "Banca"));
		result.addAll(BuscaBanca("//PARTICIPACAO-EM-BANCA-DE-DOUTORADO", "Banca"));
		result.addAll(BuscaBanca("//PARTICIPACAO-EM-BANCA-DE-EXAME-QUALIFICACAO", "Banca"));
		result.addAll(BuscaBanca("//PARTICIPACAO-EM-BANCA-DE-APERFEICOAMENTO-ESPECIALIZACAO", "Banca"));
		return result;
	}

	public ArrayList<OntoClass> BuscaBanca(String raiz, String tipo) throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile(raiz);
		NodeList livros = (NodeList) expr.evaluate(this.xmlfile, XPathConstants.NODESET);
		ArrayList<OntoClass> ListArtigoCompleto = new ArrayList<>();
		for (int i = 0; i < livros.getLength(); i++) {
			Node TipoNode = livros.item(i);
			String titulo = TipoNode.getChildNodes().item(0).getAttributes().getNamedItem("TITULO").getTextContent();
			int ano = Integer
					.parseInt(TipoNode.getChildNodes().item(0).getAttributes().getNamedItem("ANO").getTextContent());
			NodeList listAutores = TipoNode.getChildNodes();
			ArrayList<OntoParceiro> listParticipantes = new ArrayList<>();
			for (int j = 0; j < listAutores.getLength(); j++) {
				Node autoresNode = listAutores.item(j);
				if (autoresNode.getNodeName().contentEquals("PARTICIPANTE-BANCA")) {
					String nome = autoresNode.getAttributes().getNamedItem("NOME-COMPLETO-DO-PARTICIPANTE-DA-BANCA")
							.getTextContent();
					String citacao = autoresNode.getAttributes()
							.getNamedItem("NOME-PARA-CITACAO-DO-PARTICIPANTE-DA-BANCA").getTextContent();
					String id = autoresNode.getAttributes().getNamedItem("NRO-ID-CNPQ").getTextContent();
					OntoParceiro ontopar = new OntoParceiro(this.tratamentoDeDados.corrigirString(nome),
							this.tratamentoDeDados.corrigirString(citacao), this.tratamentoDeDados.corrigirString(id));
					listParticipantes.add(ontopar);
				}
			}
			OntoClass itemBanca = new OntoClass(this.tratamentoDeDados.corrigirString(titulo), tipo, listParticipantes);
			itemBanca.setAno(ano);
			ListArtigoCompleto.add(itemBanca);
		}
		return ListArtigoCompleto;
	}

	public ArrayList<OntoClass> listOntoFormacao() throws XPathExpressionException {
		ArrayList<OntoClass> result = new ArrayList<>();
		result.addAll(buscaFormacao("//GRADUACAO", "Graduacao", 1, "TITULO-DO-TRABALHO-DE-CONCLUSAO-DE-CURSO",
				"NOME-DO-ORIENTADOR", "NUMERO-ID-ORIENTADOR"));
		result.addAll(buscaFormacao("//APERFEICOAMENTO", "Aperfeicoamento", 1, "TITULO-DA-MONOGRAFIA",
				"NOME-DO-ORIENTADOR", null));
		result.addAll(buscaFormacao("//ESPECIALIZACAO", "Especializacao", 1, "TITULO-DA-MONOGRAFIA",
				"NOME-DO-ORIENTADOR", null));
		result.addAll(buscaFormacao("//MESTRADO", "MestradoAcademico", 1, "TITULO-DA-DISSERTACAO-TESE",
				"NOME-COMPLETO-DO-ORIENTADOR", "NUMERO-ID-ORIENTADOR"));
		result.addAll(buscaFormacao("//DOUTORADO", "Doutorado", 1, "TITULO-DA-DISSERTACAO-TESE",
				"NOME-COMPLETO-DO-ORIENTADOR", "NUMERO-ID-ORIENTADOR"));
		result.addAll(buscaFormacao("//MESTRADO-PROFISSIONALIZANTE", "MestradoProfissional", 1,
				"TITULO-DA-DISSERTACAO-TESE", "NOME-COMPLETO-DO-ORIENTADOR", "NUMERO-ID-ORIENTADOR"));
		return result;
	}

	private ArrayList<OntoClass> buscaFormacao(String raiz, String tipo, int NumTitulo, String Nometitulo,
			String NomeOrientador, String IdOrientador) throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile(raiz);
		NodeList livros = (NodeList) expr.evaluate(this.xmlfile, XPathConstants.NODESET);
		ArrayList<OntoClass> listResult = new ArrayList<>();
		for (int i = 0; i < livros.getLength(); i++) {
			Node TipoNode = livros.item(i);
			if (TipoNode.getAttributes().getNamedItem("STATUS-DO-CURSO").getTextContent().contentEquals("CONCLUIDO")) {
				String titulo = TipoNode.getAttributes().getNamedItem(Nometitulo).getTextContent();
				String nomeOrientador = TipoNode.getAttributes().getNamedItem(NomeOrientador).getTextContent();
				String idOrientador = (IdOrientador == null) ? ""
						: TipoNode.getAttributes().getNamedItem(IdOrientador).getTextContent();
				ArrayList<OntoParceiro> listAutores = new ArrayList<>();
				if (nomeOrientador.isEmpty() || nomeOrientador.contentEquals("") || nomeOrientador == null) {

				} else {
					OntoParceiro ontoOrientador = new OntoParceiro(this.tratamentoDeDados.corrigirString(nomeOrientador),
							this.tratamentoDeDados.corrigirString(idOrientador));
					listAutores.add(ontoOrientador);
					OntoClass eve = new OntoClass(this.tratamentoDeDados.corrigirString(titulo), tipo, listAutores);
					listResult.add(eve);
				}

			}
		}
		return listResult;
	}

	public ArrayList<OntoClass> listOrganizacaoEvento() throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile("//ORGANIZACAO-DE-EVENTO");
		NodeList livros = (NodeList) expr.evaluate(this.xmlfile, XPathConstants.NODESET);
		ArrayList<OntoClass> listResult = new ArrayList<>();
		for (int i = 0; i < livros.getLength(); i++) {
			Node TipoNode = livros.item(i);
			String titulo = TipoNode.getChildNodes().item(0).getAttributes().getNamedItem("TITULO").getTextContent();
			int ano = Integer
					.valueOf(TipoNode.getChildNodes().item(0).getAttributes().getNamedItem("ANO").getTextContent());

			OntoClass eve = new OntoClass(this.tratamentoDeDados.corrigirString(titulo), "Evento", ano);
			listResult.add(eve);
		}
		return listResult;
	}

	public ArrayList<OntoClass> listOntoEvento() throws XPathExpressionException {
		ArrayList<OntoClass> result = new ArrayList<>();
		result.addAll(buscaEvento("//PARTICIPACAO-EM-CONGRESSO", "Evento", 1, "NOME-DO-EVENTO"));
		result.addAll(buscaEvento("//PARTICIPACAO-EM-FEIRA", "Evento", 1, "NOME-DO-EVENTO"));
		result.addAll(buscaEvento("//PARTICIPACAO-EM-SEMINARIO", "Evento", 1, "NOME-DO-EVENTO"));
		result.addAll(buscaEvento("//PARTICIPACAO-EM-SIMPOSIO", "Evento", 1, "NOME-DO-EVENTO"));
		result.addAll(buscaEvento("//PARTICIPACAO-EM-ENCONTRO", "Evento", 1, "NOME-DO-EVENTO"));
		result.addAll(buscaEvento("//PARTICIPACAO-EM-EXPOSICAO", "Evento", 1, "NOME-DO-EVENTO"));
		return result;
	}

	private ArrayList<OntoClass> buscaEvento(String raiz, String tipo, int NumTitulo, String Nometitulo)
			throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile(raiz);
		NodeList livros = (NodeList) expr.evaluate(this.xmlfile, XPathConstants.NODESET);
		ArrayList<OntoClass> listResult = new ArrayList<>();
		for (int i = 0; i < livros.getLength(); i++) {
			Node TipoNode = livros.item(i);
			String titulo = TipoNode.getChildNodes().item(NumTitulo).getAttributes().getNamedItem(Nometitulo)
					.getTextContent();
			int ano = Integer
					.valueOf(TipoNode.getChildNodes().item(0).getAttributes().getNamedItem("ANO").getTextContent());

			OntoClass eve = new OntoClass(this.tratamentoDeDados.corrigirString(titulo), tipo, ano);
			listResult.add(eve);
		}
		return listResult;
	}

	public ArrayList<AreaAtuacaoXML> listOntoAreaAtuacao() throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile("//AREA-DE-ATUACAO");
		NodeList listaxml = (NodeList) expr.evaluate(this.xmlfile, XPathConstants.NODESET);
		ArrayList<AreaAtuacaoXML> listResult = new ArrayList<>();
		for (int i = 0; i < listaxml.getLength(); i++) {
			Node TipoNode = listaxml.item(i);
			String grandeArea = TipoNode.getAttributes().getNamedItem("NOME-GRANDE-AREA-DO-CONHECIMENTO")
					.getTextContent();
			String areaConhecimento = TipoNode.getAttributes().getNamedItem("NOME-DA-AREA-DO-CONHECIMENTO")
					.getTextContent();
			String subAreaConhecimento = TipoNode.getAttributes().getNamedItem("NOME-DA-SUB-AREA-DO-CONHECIMENTO")
					.getTextContent();
			String nomeEspecialidade = TipoNode.getAttributes().getNamedItem("NOME-DA-ESPECIALIDADE").getTextContent();
			listResult.add(new AreaAtuacaoXML(this.tratamentoDeDados.corrigirString(grandeArea),
					this.tratamentoDeDados.corrigirString(areaConhecimento),
					this.tratamentoDeDados.corrigirString(subAreaConhecimento),
					this.tratamentoDeDados.corrigirString(nomeEspecialidade)));
		}
		return listResult;
	}

	public ArrayList<OntoClass> listOntoProjetoPesquisa() throws XPathExpressionException {
		XPathExpression expr = this.xpath.compile("//PARTICIPACAO-EM-PROJETO");
		NodeList projetos = (NodeList) expr.evaluate(this.xmlfile, XPathConstants.NODESET);
		ArrayList<OntoClass> listResult = new ArrayList<>();
		for (int i = 0; i < projetos.getLength(); i++) {
			Node TipoNode = projetos.item(i);
			if (TipoNode.getChildNodes().getLength() > 1) {
				String titulo = TipoNode.getChildNodes().item(0).getAttributes().getNamedItem("NOME-DO-PROJETO")
						.getTextContent();
				if (titulo.isEmpty() || titulo == null) {

				} else {
					NodeList auxlist = TipoNode.getChildNodes().item(0).getChildNodes();
					ArrayList<OntoParceiro> listAutores = new ArrayList<>();
					for (int j = 0; j < auxlist.getLength(); j++) {
						Node aux = auxlist.item(j);
						if (aux.getNodeName().contentEquals("EQUIPE-DO-PROJETO")) {
							NodeList NodelistAutoresProjeto = aux.getChildNodes();
							for (int t = 0; t < NodelistAutoresProjeto.getLength(); t++) {
								Node Autores = NodelistAutoresProjeto.item(t);
								if (Autores != null) {
									String id = (Autores.getAttributes().getNamedItem("NRO-ID-CNPQ") == null) ? ""
											: Autores.getAttributes().getNamedItem("NRO-ID-CNPQ").getTextContent();
									String citacao = Autores.getAttributes().getNamedItem("NOME-PARA-CITACAO")
											.getTextContent().replaceAll(" ", "_");
									String nome = Autores.getAttributes().getNamedItem("NOME-COMPLETO").getTextContent()
											.replaceAll(" ", "_");
									listAutores.add(new OntoParceiro(this.tratamentoDeDados.corrigirString(nome),
											this.tratamentoDeDados.corrigirString(citacao), this.tratamentoDeDados.corrigirString(id)));
								}
							}
						}
					}
					OntoClass eve = new OntoClass(this.tratamentoDeDados.corrigirString(titulo), "ProjetoPesquisa",
							listAutores);
					listResult.add(eve);
				}

			}
		}
		return listResult;
	}

}
