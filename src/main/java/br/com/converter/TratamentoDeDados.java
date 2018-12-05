package br.com.converter;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLOntology;

import br.com.modelo.OntoClass;
import br.com.modelo.OntoParceiro;
import br.com.modelo.OntoPessoa;
import br.com.modelo.TrabalhoEventoXml;
import br.com.modelo.TriplaOwl;
import info.debatty.java.stringsimilarity.NGram;

public class TratamentoDeDados {

	// Normalização de dados
	public String corrigirString(String text) {
		// Retira acentuacao e carcteres especiais
		text = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		// retira -
		text = text.replaceAll("-", " ");
		// troca espeços por underlines
		text = text.replaceAll("\\s+", "_");
		// retira numeros
		text = text.replaceAll("[^\\w^\\;]", "");
		// coloca tudo em minusculo
		text = text.toLowerCase();
		return text;
	}

	// Processo de expansão de dados onde cada item vira uma arvore
	public void ExpansaoMembros(ArrayList<OntoPessoa> listapessoa) {
		ArrayList<OntoPessoa> listaAux = new ArrayList<>();
		for (OntoPessoa ontoPessoa : listapessoa) {
			ontoPessoa.setFlagLattes(true);
			for (OntoClass ontoClass : ontoPessoa.getListOntoBanca()) {
				for (OntoParceiro ontoParceiro : ontoClass.getListAutores()) {
					OntoPessoa ont = new OntoPessoa(ontoParceiro.getNome(), ontoParceiro.getId(), "",
							ontoParceiro.getCitacao());
					OntoClass bancaAux = new OntoClass(ontoClass.getTitulo(), ontoClass.getTipo(), ontoClass.getAno());
					ont.AddListOntoBanca(bancaAux);
					listaAux.add(ont);
				}
				ontoClass.setListAutores(new ArrayList<>());
			}
			for (OntoClass ontoClass : ontoPessoa.getListOntoFormacao()) {
				for (OntoParceiro ontoParceiro : ontoClass.getListAutores()) {
					OntoPessoa ont = new OntoPessoa(ontoParceiro.getNome(), ontoParceiro.getId(), "",
							ontoParceiro.getCitacao());
					OntoClass formacaoAux = new OntoClass(ontoClass.getTitulo(), ontoClass.getTipo(),
							ontoClass.getAno());
					ontoClass.setFlagFormacaoOrientacao(true);
					ont.AddListOntoFormacao(formacaoAux);
					listaAux.add(ont);
				}
				ontoClass.setListAutores(new ArrayList<>());
			}
			for (OntoClass ontoClass : ontoPessoa.getListOntoProjetoPesquisa()) {
				for (OntoParceiro ontoParceiro : ontoClass.getListAutores()) {
					OntoPessoa ont = new OntoPessoa(ontoParceiro.getNome(), ontoParceiro.getId(), "",
							ontoParceiro.getCitacao());
					OntoClass pesquisa = new OntoClass(ontoClass.getTitulo(), ontoClass.getTipo(), ontoClass.getAno());
					ont.AddListOntoProjetoPesquisa(pesquisa);
					listaAux.add(ont);
				}
				ontoClass.setListAutores(new ArrayList<>());
			}
		}
		listapessoa.addAll(listaAux);
	}

	public static void EventoeTrabalho(OntoPessoa pessoa) {
		ArrayList<OntoClass> aux = new ArrayList<>();
		pessoa.getListOntoEvento().forEach(ev -> {
			pessoa.getListOntoTrabalhoEvento().forEach(tr -> {
				if (ev.getTitulo().contentEquals(tr.getEvento().getTitulo()))
					aux.add(ev);
			});
		});
		aux.forEach(ev -> pessoa.getListOntoEvento().remove(ev));
	}

	// Retorna lista de eventos que só possuem um individuos vinculados a ele
	public static ArrayList<TriplaOwl> listaEventoDesnecessario(OWLOntology ontology) {
		ArrayList<TriplaOwl> listDelete = new ArrayList<>();
		ontology.individualsInSignature().filter(u -> u.isOWLNamedIndividual())
				.filter(u -> ontology.classAssertionAxioms(u).findFirst().get().signature().findFirst().get().getIRI()
						.getFragment().contains("Evento"))
				.forEach(w -> {
					if (ontology.objectPropertyAssertionAxioms(w).count() == 1) {
						TriplaOwl triplaOwl = new TriplaOwl(w.getIRI());
						listDelete.add(triplaOwl);
					}
				});
		return listDelete;
	}

	// Retorna lista de bancas que só possuem um individuos vinculados a ele
	public static ArrayList<TriplaOwl> listaBancaDesnecessario(OWLOntology ontology) {
		ArrayList<TriplaOwl> listDelete = new ArrayList<>();
		ontology.individualsInSignature().filter(u -> u.isOWLNamedIndividual())
				.filter(u -> ontology.classAssertionAxioms(u).findFirst().get().signature().findFirst().get().getIRI()
						.getFragment().contains("Banca"))
				.forEach(w -> {
					if (ontology.objectPropertyAssertionAxioms(w).count() == 1) {
						TriplaOwl triplaOwl = new TriplaOwl(w.getIRI());
						listDelete.add(triplaOwl);
					}
				});
		return listDelete;
	}

	// Retorna lista de individuos que possuem menos de 1 item cadastrado
	public static ArrayList<TriplaOwl> listaPessoaDesnecessario(OWLOntology ontology) {
		ArrayList<TriplaOwl> listDelete = new ArrayList<>();
		ontology.individualsInSignature().filter(u -> u.isOWLNamedIndividual())
				.filter(u -> ontology.classAssertionAxioms(u).findFirst().get().signature().findFirst().get().getIRI()
						.getFragment().contains("Pessoa"))
				.forEach(w -> {
					if (ontology.objectPropertyAssertionAxioms(w).count() == 1) {
						TriplaOwl triplaOwl = new TriplaOwl(w.getIRI());
						listDelete.add(triplaOwl);
					}
				});
		return listDelete;
	}

	// processo de junção de membros
	public void JuncaoMembros(ArrayList<OntoPessoa> listaPessoa) {
		int antes = listaPessoa.size();
		UnificarNomeComIdLattes(listaPessoa);
		System.out.println("N° de individuos que foram unificados (Nome,IdLattes):" + (antes - listaPessoa.size()));
		antes = listaPessoa.size();
		UnificarNomeComNome(listaPessoa);
		System.out.println("N° de individuos que foram unificados (Nome,Nome):" + (antes - listaPessoa.size()));
		antes = listaPessoa.size();
		UnificarListCitacaoComNome(listaPessoa);
		System.out.println("N° de individuos que foram unificados (Citacao,Nome):" + (antes - listaPessoa.size()));
		antes = listaPessoa.size();
		UnificarNomeContidoEmOutro(listaPessoa);
		System.out.println("N° de individuos que foram unificados (Substring,Nome):" + (antes - listaPessoa.size()));
		antes = listaPessoa.size();
		UnificarCitacaoPorCitacao(listaPessoa);
		System.out.println("N° de individuos que foram unificados (Citacao,Citacao):" + (antes - listaPessoa.size()));
		antes = listaPessoa.size();
		UnificarNomeComNomeAlgoritmoNGram(listaPessoa);
		System.out
				.println("N° de individuos que foram unificados com Ngram (Nome,Nome):" + (antes - listaPessoa.size()));
	}

	public void UnificarNomeComIdLattes(ArrayList<OntoPessoa> listaPessoa) {
		listaPessoa.sort(Comparator.comparing(u -> u.getIdLattes()));
		for (int i = 0; i < listaPessoa.size(); i++) {
			String idlattes = listaPessoa.get(i).getIdLattes();
			if (!idlattes.contentEquals("")) {
				for (int j = i + 1; j < listaPessoa.size(); j++) {
					if (i != j) {
						if (idlattes.contentEquals(listaPessoa.get(j).getIdLattes())) {
							listaPessoa.get(i).Copiar(listaPessoa.get(j));
							listaPessoa.get(i).cont();
							listaPessoa.remove(j);
							j--;
						}
					}
				}
			}
		}
	}

	public void UnificarNomeComNome(ArrayList<OntoPessoa> listaPessoa) {
		listaPessoa.sort(Comparator.comparing(u -> u.getNomeCompleto()));
		for (int i = 0; i < listaPessoa.size(); i++) {
			String nome = listaPessoa.get(i).getNomeCompleto();
			for (int j = i + 1; j < listaPessoa.size(); j++) {
				if (i != j) {
					if (nome.contentEquals(listaPessoa.get(j).getNomeCompleto())) {
						listaPessoa.get(i).Copiar(listaPessoa.get(j));
						listaPessoa.get(i).cont();
						listaPessoa.remove(j);
						j--;
					}
				}
			}
		}
	}

	public void UnificarNomeComNomeAlgoritmoNGram(ArrayList<OntoPessoa> listaPessoa) {
		listaPessoa.sort(Comparator.comparing(u -> u.getNomeCompleto()));
		NGram ngram = new NGram(4);
		for (int i = 0; i < listaPessoa.size(); i++) {
			String nome = listaPessoa.get(i).getNomeCompleto();
			for (int j = i + 1; j < listaPessoa.size(); j++) {
				if (i != j) {
					OntoPessoa segundoNome = listaPessoa.get(j);
					if (i == listaPessoa.size())
						break;
					if (nome.charAt(0) == listaPessoa.get(j).getNomeCompleto().charAt(0)) {
						if (ngram.distance(nome, segundoNome.getNomeCompleto()) < 0.25) {
							listaPessoa.get(i).Copiar(segundoNome);
							listaPessoa.get(i).cont();
							listaPessoa.remove(j);
							j--;
						}
					}
				}
			}
		}
	}

	public void UnificarCitacaoPorCitacao(ArrayList<OntoPessoa> listaPessoa) {
		listaPessoa.sort(Comparator.comparing(u -> u.getNomeCompleto()));
		for (int i = 0; i < listaPessoa.size(); i++) {
			ArrayList<String> listcitacaoPivo = listaPessoa.get(i).getCitacaoList();
			for (int j = i + 1; j < listaPessoa.size(); j++) {
				if (!(i == j)) {
					for (int k = 0; k < listaPessoa.get(j).getCitacaoList().size(); k++) {
						String unidadelist = listaPessoa.get(j).getCitacaoList().get(k);
						if (listcitacaoPivo.contains(unidadelist) && (!unidadelist.contentEquals(""))) {
							listaPessoa.get(i).Copiar(listaPessoa.get(j));
							listaPessoa.get(i).cont();
							listaPessoa.remove(j);
							break;
						}
					}
				}
			}
		}
	}

	public void UnificarListCitacaoComNome(ArrayList<OntoPessoa> listaPessoa) {
		listaPessoa.sort(Comparator.comparing(u -> u.getNomeCompleto()));
		for (int i = 0; i < listaPessoa.size(); i++) {
			ArrayList<String> listcitacaoPivo = listaPessoa.get(i).getCitacaoList();
			if (!(listcitacaoPivo.get(0).contentEquals("") && listcitacaoPivo.size() == 1)) {
				for (int j = i + 1; j < listaPessoa.size(); j++) {
					if (i != j) {
						for (int p = 0; p < listcitacaoPivo.size(); p++) {
							if (i == listaPessoa.size())
								break;
							if (listcitacaoPivo.get(p).contentEquals(listaPessoa.get(j).getNomeCompleto())) {
								listaPessoa.get(i).Copiar(listaPessoa.get(j));
								listaPessoa.get(i).cont();
								listaPessoa.remove(j);
								j--;
								break;
							}
						}
					}
				}
			}
		}
	}

	public void UnificarNomeContidoEmOutro(ArrayList<OntoPessoa> listaPessoa) {
		listaPessoa.sort(Comparator.comparing(u -> ((OntoPessoa) u).getNomeCompleto()).reversed());
		for (int i = 0; i < listaPessoa.size(); i++) {
			String nome = listaPessoa.get(i).getNomeCompleto();
			for (int j = i + 1; j < listaPessoa.size(); j++) {
				if (!(i == j)) {
					if (nome.contains(listaPessoa.get(j).getNomeCompleto())) {
						listaPessoa.get(i).Copiar(listaPessoa.get(j));
						listaPessoa.get(i).cont();
						listaPessoa.remove(j);
						j--;
					}
				}
			}
		}
	}

	//
	public void tratarEventos(ArrayList<OntoPessoa> listaPessoa) {
		listaPessoa.sort(Comparator.comparing(u -> ((OntoPessoa) u).getListOntoTrabalhoEvento().size()).reversed());
		int totalcont = 0;
		int cont = 99;
		while (cont != 0) {
			cont = 0;
			for (int i = 0; i < listaPessoa.size(); i++) {
				OntoPessoa ontoPessoa = listaPessoa.get(i);
				if (ontoPessoa.getListOntoTrabalhoEvento().size() == 0)
					break;
				for (int j = i + 1; j < listaPessoa.size(); j++) {
					OntoPessoa ontoPessoa2 = listaPessoa.get(j);
					if (ontoPessoa2.getListOntoTrabalhoEvento().size() == 0)
						break;
					for (int k = 0; k < ontoPessoa.getListOntoEvento().size(); k++) {
						OntoClass evento = ontoPessoa.getListOntoEvento().get(k);

						if ((ontoPessoa2.getListOntoEvento().size()
								+ ontoPessoa2.getListOntoTrabalhoEvento().size()) != 0) {
							for (int t = 0; t < ontoPessoa2.getListOntoEvento().size(); t++) {
								OntoClass evento2 = ontoPessoa2.getListOntoEvento().get(t);
								if (testeEvento(evento.getTitulo(), evento2.getTitulo())) {
									cont++;
									if (evento.getTitulo().length() < evento2.getTitulo().length())
										evento2.setTitulo(evento.getTitulo());
									else {
										if (evento.getTitulo().length() == evento2.getTitulo().length()) {
											if (evento.getTitulo().hashCode() < evento2.getTitulo().hashCode())
												evento2.setTitulo(evento.getTitulo());
											else
												evento.setTitulo(evento2.getTitulo());
										} else
											evento.setTitulo(evento2.getTitulo());
									}
								}
							}

							for (int t = 0; t < ontoPessoa2.getListOntoTrabalhoEvento().size(); t++) {
								TrabalhoEventoXml evento2 = ontoPessoa2.getListOntoTrabalhoEvento().get(t);
								if (testeEvento(evento.getTitulo(), evento2.getEvento().getTitulo())) {
									cont++;
									if (evento.getTitulo().length() < evento2.getEvento().getTitulo().length())
										evento2.getEvento().setTitulo(evento.getTitulo());
									else {
										if (evento.getTitulo().length() == evento2.getEvento().getTitulo().length()) {
											if (evento.getTitulo().hashCode() < evento2.getEvento().getTitulo()
													.hashCode())
												evento2.getEvento().setTitulo(evento.getTitulo());
											else
												evento.setTitulo(evento2.getEvento().getTitulo());
										} else
											evento.setTitulo(evento2.getEvento().getTitulo());
									}
								}
							}
						}
					}
					for (int k = 0; k < ontoPessoa.getListOntoTrabalhoEvento().size(); k++) {
						TrabalhoEventoXml evento = ontoPessoa.getListOntoTrabalhoEvento().get(k);

						if ((ontoPessoa2.getListOntoEvento().size()
								+ ontoPessoa2.getListOntoTrabalhoEvento().size()) != 0) {
							for (int t = 0; t < ontoPessoa2.getListOntoEvento().size(); t++) {
								OntoClass evento2 = ontoPessoa2.getListOntoEvento().get(t);
								if (testeEvento(evento.getEvento().getTitulo(), evento2.getTitulo())) {
									cont++;
									if (evento.getEvento().getTitulo().length() < evento2.getTitulo().length())
										evento2.setTitulo(evento.getEvento().getTitulo());
									else {
										if (evento.getEvento().getTitulo().length() == evento2.getTitulo().length()) {
											if (evento.getEvento().getTitulo().hashCode() < evento2.getTitulo()
													.hashCode())
												evento2.setTitulo(evento.getEvento().getTitulo());
											else
												evento.getEvento().setTitulo(evento2.getTitulo());
										} else
											evento.getEvento().setTitulo(evento2.getTitulo());
									}
								}
							}
							for (int t = 0; t < ontoPessoa2.getListOntoTrabalhoEvento().size(); t++) {
								TrabalhoEventoXml evento2 = ontoPessoa2.getListOntoTrabalhoEvento().get(t);
								if (testeEvento(evento.getEvento().getTitulo(), evento2.getEvento().getTitulo())) {
									cont++;
									if (evento.getEvento().getTitulo().length() < evento2.getEvento().getTitulo()
											.length())
										evento2.getEvento().setTitulo(evento.getEvento().getTitulo());
									else {
										if (evento.getEvento().getTitulo().length() == evento2.getEvento().getTitulo()
												.length()) {
											if (evento.getEvento().getTitulo().hashCode() < evento2.getEvento()
													.getTitulo().hashCode())
												evento2.getEvento().setTitulo(evento.getEvento().getTitulo());
											else
												evento.getEvento().setTitulo(evento2.getEvento().getTitulo());
										} else
											evento.getEvento().setTitulo(evento2.getEvento().getTitulo());
									}
								}
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < listaPessoa.size(); i++) {
			totalcont = totalcont + listaPessoa.get(i).getListOntoEvento().size();
		}
		System.out.println("Eventos depois da unificação : " + totalcont);
	}

	public void tratarBancaInterna(ArrayList<OntoPessoa> listaPessoa) {
		NGram ngram = new NGram(4);
		int totalcont = 0;
		for (int i = 0; i < listaPessoa.size(); i++) {
			OntoPessoa ontoPessoa = listaPessoa.get(i);
			for (int k = 0; k < ontoPessoa.getListOntoBanca().size(); k++) {
				OntoClass banca = ontoPessoa.getListOntoBanca().get(k);
				for (int t = k + 1; t < ontoPessoa.getListOntoBanca().size(); t++) {
					OntoClass banca2 = ontoPessoa.getListOntoBanca().get(t);
					if (banca.getAno() + 1 == banca2.getAno() || banca.getAno() - 1 == banca2.getAno()
							|| banca.getAno() + 2 == banca2.getAno() || banca.getAno() - 2 == banca2.getAno()
							|| banca.getAno() == banca2.getAno()) {

						if (banca.getTitulo().length() * 1.4 <= banca2.getTitulo().length()
								|| banca.getTitulo().length() >= banca2.getTitulo().length() * 1.4) {
							continue;
						}
						if (banca.getTitulo() == banca2.getTitulo()) {
							banca.setTitulo(banca2.getTitulo());
							ontoPessoa.getListOntoBanca().remove(t);
							t--;
						}
						double aux = ngram.distance(banca.getTitulo(), banca2.getTitulo());
						if (aux > 0 && aux < 0.25) {
							if (banca.getTitulo().length() < banca2.getTitulo().length()) {
								banca2.setTitulo(banca.getTitulo());
								ontoPessoa.getListOntoBanca().remove(k);
								k = -1;
								break;
							} else {
								if (banca.getTitulo().length() == banca2.getTitulo().length()) {
									if (banca.getTitulo().hashCode() < banca2.getTitulo().hashCode()) {
										banca2.setTitulo(banca.getTitulo());
										ontoPessoa.getListOntoBanca().remove(k);
										k = -1;
										break;
									} else {
										banca.setTitulo(banca2.getTitulo());
										ontoPessoa.getListOntoBanca().remove(t);
										t--;
									}
								} else {
									banca.setTitulo(banca2.getTitulo());
									ontoPessoa.getListOntoBanca().remove(t);
									t--;
								}
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < listaPessoa.size(); i++) {
			totalcont = totalcont + listaPessoa.get(i).getListOntoBanca().size();
		}
		System.out.println("Banca antes da unificação : " + totalcont);
	}

	// Compara bancas uma a uma para unificação
	public void tratarBancaExterna(ArrayList<OntoPessoa> listaPessoa, int contaux) {
		NGram ngram = new NGram(4);
		int cont = 99;
		while (cont != 0) {
			cont = 0;
			for (int i = 0; i < listaPessoa.size(); i++) {
				OntoPessoa ontoPessoa = listaPessoa.get(i);
				for (int j = i + 1; j < listaPessoa.size(); j++) {
					OntoPessoa ontoPessoa2 = listaPessoa.get(j);
					for (int k = 0; k < ontoPessoa.getListOntoBanca().size(); k++) {
						OntoClass banca = ontoPessoa.getListOntoBanca().get(k);
						for (int t = 0; t < ontoPessoa2.getListOntoBanca().size(); t++) {
							OntoClass banca2 = ontoPessoa2.getListOntoBanca().get(t);
							if (banca.getAno() + 1 == banca2.getAno() || banca.getAno() - 1 == banca2.getAno()
									|| banca.getAno() + 2 == banca2.getAno() || banca.getAno() - 2 == banca2.getAno()
									|| banca.getAno() == banca2.getAno()) {
								double aux = ngram.distance(banca.getTitulo(), banca2.getTitulo());
								if (aux > 0 && aux < 0.20) {
									cont++;
									contaux++;
									if (banca.getTitulo().length() < banca2.getTitulo().length())
										banca2.setTitulo(banca.getTitulo());
									else {
										if (banca.getTitulo().length() == banca2.getTitulo().length()) {
											if (banca.getTitulo().hashCode() < banca2.getTitulo().hashCode())
												banca2.setTitulo(banca.getTitulo());
											else
												banca.setTitulo(banca2.getTitulo());
										} else
											banca.setTitulo(banca2.getTitulo());
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void eliminarIndividuosDesnecessarios(ArrayList<OntoPessoa> listaPessoa) {

		for (int i = 0; i < listaPessoa.size(); i++) {
			OntoPessoa ontoPessoa = listaPessoa.get(i);
			int valortotal = ontoPessoa.getListOntoBanca().size() + ontoPessoa.getListOntoEvento().size()
					+ ontoPessoa.getListOntoProjetoPesquisa().size() + ontoPessoa.getListOntoAreaAtuacao().size()
					+ ontoPessoa.getListOntoFormacao().size() + ontoPessoa.getListOntoOrientacao().size()
					+ ontoPessoa.getListOntoTrabalhoEvento().size();
			if (valortotal < 2) {
				listaPessoa.remove(i);
				i--;
			}
		}
	}

	public Boolean testeEvento(String nome1, String nome2) {
		NGram ngram = new NGram(4);
		if (nome1.length() * 1.4 <= nome2.length() || nome1.length() >= nome2.length() * 1.4) {
			return false;
		} else {
			if (nome1 == nome2) {
				return false;
			} else {
				double aux = ngram.distance(nome1, nome2);
				if (aux > 0 && aux < 0.25) {
					return true;
				}
			}
		}
		return false;
	}

	public Map<String, String> CriarMap(ArrayList<OntoPessoa> listaPessoa) {
		Map<String, String> MapNome = new HashMap<String, String>();
		for (int i = 0; i < listaPessoa.size(); i++) {
			OntoPessoa ontoPessoa = listaPessoa.get(i);
			if (ontoPessoa.getIdLattes().length() > 1) {
				MapNome.put(ontoPessoa.getIdLattes(), ontoPessoa.getNomeCompleto());
			}
		}
		return MapNome;
	}

	public void tratarEventoExterna(ArrayList<OntoPessoa> listaPessoa, int cont) {
		for (int i = 0; i < listaPessoa.size(); i++) {
			OntoPessoa ontoPessoa = listaPessoa.get(i);
			for (int k = 0; k < ontoPessoa.getListOntoEvento().size(); k++) {
				OntoClass evento = ontoPessoa.getListOntoEvento().get(k);
				for (int t = k + 1; t < ontoPessoa.getListOntoEvento().size(); t++) {
					OntoClass evento2 = ontoPessoa.getListOntoEvento().get(t);
					if (testeEvento(evento.getTitulo(), evento2.getTitulo())) {
						cont++;
						if (evento.getTitulo().length() < evento2.getTitulo().length()) {
							// evento2.setTitulo(evento.getTitulo());
							ontoPessoa.getListOntoEvento().remove(t);
							t--;
						} else {
							if (evento.getTitulo().length() == evento2.getTitulo().length()) {
								if (evento.getTitulo().hashCode() < evento2.getTitulo().hashCode()) {
									// evento2.setTitulo(evento.getTitulo());
									ontoPessoa.getListOntoEvento().remove(t);
									t--;
								} else {
									evento.setTitulo(evento2.getTitulo());
									ontoPessoa.getListOntoEvento().remove(t);
									t--;
								}
							} else {
								evento.setTitulo(evento2.getTitulo());
								ontoPessoa.getListOntoEvento().remove(t);
								t--;
							}
						}
					}
				}
			}

			for (int k = 0; k < ontoPessoa.getListOntoTrabalhoEvento().size(); k++) {
				TrabalhoEventoXml evento = ontoPessoa.getListOntoTrabalhoEvento().get(k);
				for (int t = k + 1; t < ontoPessoa.getListOntoTrabalhoEvento().size(); t++) {
					TrabalhoEventoXml evento2 = ontoPessoa.getListOntoTrabalhoEvento().get(t);
					if (testeEvento(evento.getEvento().getTitulo(), evento2.getEvento().getTitulo())) {
						cont++;
						if (evento.getEvento().getTitulo().length() < evento2.getEvento().getTitulo().length())
							evento2.getEvento().setTitulo(evento.getEvento().getTitulo());
						else {
							if (evento.getEvento().getTitulo().length() == evento2.getEvento().getTitulo().length()) {
								if (evento.getEvento().getTitulo().hashCode() < evento2.getEvento().getTitulo()
										.hashCode())
									evento2.getEvento().setTitulo(evento.getEvento().getTitulo());
								else
									evento.getEvento().setTitulo(evento2.getEvento().getTitulo());
							} else
								evento.getEvento().setTitulo(evento2.getEvento().getTitulo());
						}
					}
				}
			}
			for (int k = 0; k < ontoPessoa.getListOntoEvento().size(); k++) {
				OntoClass evento = ontoPessoa.getListOntoEvento().get(k);
				for (int t = k; t < ontoPessoa.getListOntoTrabalhoEvento().size(); t++) {
					TrabalhoEventoXml evento2 = ontoPessoa.getListOntoTrabalhoEvento().get(t);
					if (testeEvento(evento.getTitulo(), evento2.getEvento().getTitulo())) {
						cont++;
						if (evento.getTitulo().length() < evento2.getEvento().getTitulo().length())
							evento2.getEvento().setTitulo(evento.getTitulo());
						else {
							if (evento.getTitulo().length() == evento2.getEvento().getTitulo().length()) {
								if (evento.getTitulo().hashCode() < evento2.getEvento().getTitulo().hashCode())
									evento2.getEvento().setTitulo(evento.getTitulo());
								else
									evento.setTitulo(evento2.getEvento().getTitulo());
							} else
								evento.setTitulo(evento2.getEvento().getTitulo());
						}
					}
				}
			}
		}
	}

}
