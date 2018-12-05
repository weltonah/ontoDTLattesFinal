package br.com.Grafo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class Grafo {

	private ArrayList<Pessoa> listParticipante;
	private ArrayList<Banca> listParticipouBanca;
	private ArrayList<Evento> listParticipouEvento;
	private ArrayList<AreaAtuacao> listParticipouAreaAtuacao;
	private ArrayList<AreaConhecimento> listParticipouAreaConhecimento;
	private ArrayList<SubArea> listParticipouSubArea;
	private ArrayList<Especialidade> listParticipouEspecialidade;
	private ArrayList<ProjetoPesquisa> listParticipouProjetoPesquisa;
	private ArrayList<TrabalhoEvento> listParticipouTrabalhoEvento;
	private ArrayList<Resultado> listResultado;

	public Grafo() {
		this.listParticipante = new ArrayList<>();
		this.listParticipouBanca = new ArrayList<>();
		this.listParticipouEvento = new ArrayList<>();
		this.listParticipouAreaAtuacao = new ArrayList<>();
		this.listParticipouAreaConhecimento = new ArrayList<>();
		this.listParticipouSubArea = new ArrayList<>();
		this.listParticipouEspecialidade = new ArrayList<>();
		this.listParticipouProjetoPesquisa = new ArrayList<>();
		this.listParticipouTrabalhoEvento = new ArrayList<>();
		this.listResultado = new ArrayList<>();
	}

	public void PreencherResultado(ArrayList<String[]> listaResultado, int tipo) {
		for (String[] strings : listaResultado) {
			if (Integer.parseInt(strings[2]) > 0) {
				Pessoa pessoa1 = null;
				for (Pessoa pessoa : this.listParticipante) {
					if (pessoa.getNome().contentEquals(strings[0])) {
						pessoa1 = pessoa;
						break;
					}
				}
				Pessoa pessoa2 = null;
				for (Pessoa pessoa : this.listParticipante) {
					if (pessoa.getNome().contentEquals(strings[1])) {
						pessoa2 = pessoa;
						break;
					}
				}
				if (pessoa1 != null && pessoa2 == null) {
					Pessoa pessoaAux2 = new Pessoa(strings[1]);
					Resultado resultado = new Resultado();
					resultado.AddListParticipante(pessoa1);
					resultado.AddListParticipante(pessoaAux2);
					pessoa1.AddListResultado(resultado);
					pessoaAux2.AddListResultado(resultado);
					resultado.Addcriterio(strings[2], tipo);
					AddParticipante(pessoaAux2);
					AddListResultado(resultado);
				} else {
					if (pessoa1 == null && pessoa2 != null) {
						Pessoa pessoaAux1 = new Pessoa(strings[0]);
						Resultado resultado = new Resultado();
						resultado.AddListParticipante(pessoa2);
						resultado.AddListParticipante(pessoaAux1);
						pessoa2.AddListResultado(resultado);
						pessoaAux1.AddListResultado(resultado);
						resultado.Addcriterio(strings[2], tipo);
						AddParticipante(pessoaAux1);
						AddListResultado(resultado);
					} else {
						if (pessoa1 != null && pessoa2 != null) {
							buscarresultado(pessoa1, pessoa2, strings[2], tipo);
						} else {
							Pessoa pessoaAux1 = new Pessoa(strings[0]);
							Pessoa pessoaAux2 = new Pessoa(strings[1]);
							Resultado resultado = new Resultado();
							resultado.AddListParticipante(pessoaAux1);
							resultado.AddListParticipante(pessoaAux2);
							pessoaAux1.AddListResultado(resultado);
							pessoaAux2.AddListResultado(resultado);
							resultado.Addcriterio(strings[2], tipo);
							AddParticipante(pessoaAux1);
							AddParticipante(pessoaAux2);
							AddListResultado(resultado);
						}
					}
				}
			}
		}
	}

	public void buscarresultado(Pessoa pessoa1, Pessoa pessoa2, String valor, int tipo) {
		boolean flag = true;
		for (Resultado resultado : pessoa1.getListResultado()) {
			if (resultado.getListParticipante().contains(pessoa2)) {
				resultado.Addcriterio(valor, tipo);
				flag = false;
				break;
			}
		}
		if (flag) {
			Resultado resultado = new Resultado();
			resultado.AddListParticipante(pessoa1);
			resultado.AddListParticipante(pessoa2);
			pessoa1.AddListResultado(resultado);
			pessoa2.AddListResultado(resultado);
			resultado.Addcriterio(valor, tipo);
			AddListResultado(resultado);
		}
	}

	public ArrayList<String[]> InferirBanca() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for (Banca banca : this.listParticipouBanca) {
			for (int i = 0; i < banca.getListParticipante().size(); i++) {
				for (int j = 0; j < banca.getListParticipante().size(); j++) {
					if (i != j) {
						String[] resultado = new String[2];
						resultado[0] = banca.getListParticipante().get(i).getNome();
						resultado[1] = banca.getListParticipante().get(j).getNome();
						list.add(resultado);
					}
				}
			}
		}
		list.sort(Comparator.comparing(u -> u[0]));
		return list;
	}

	public ArrayList<String[]> InferirEvento() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for (Evento evento : this.listParticipouEvento) {
			for (int i = 0; i < evento.getListParticipante().size(); i++) {
				for (int j = 0; j < evento.getListParticipante().size(); j++) {
					if (i != j) {
						String[] resultado = new String[2];
						resultado[0] = evento.getListParticipante().get(i).getNome();
						resultado[1] = evento.getListParticipante().get(j).getNome();
						list.add(resultado);
					}
				}
			}
		}
		list.sort(Comparator.comparing(u -> u[0]));
		return list;
	}

	public ArrayList<String[]> InferirAreaAtuacao() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for (AreaAtuacao areaatucao : this.listParticipouAreaAtuacao) {
			for (int i = 0; i < areaatucao.getListParticipante().size(); i++) {
				for (int j = 0; j < areaatucao.getListParticipante().size(); j++) {
					if (i != j) {
						String[] resultado = new String[2];
						resultado[0] = areaatucao.getListParticipante().get(i).getNome();
						resultado[1] = areaatucao.getListParticipante().get(j).getNome();
						list.add(resultado);
					}
				}
			}
		}
		list.sort(Comparator.comparing(u -> u[0]));
		return list;
	}

	public ArrayList<String[]> InferirOrientacao() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getListAlunosOrientados().size() > 0) {
				for (int i = 0; i < pessoa.getListAlunosOrientados().size(); i++) {
					for (int j = 0; j < pessoa.getListAlunosOrientados().size(); j++) {
						if (i != j) {
							String[] resultado = new String[2];
							resultado[0] = pessoa.getListAlunosOrientados().get(i).getNome();
							resultado[1] = pessoa.getListAlunosOrientados().get(j).getNome();
							list.add(resultado);
						}
					}
				}
			}
		}
		list.sort(Comparator.comparing(u -> u[0]));
		return list;
	}

	public ArrayList<String[]> InferirProjetoPesquisa() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getListParticipouProjetoPesquisa().size() > 0) {
				for (ProjetoPesquisa pesq1 : pessoa.getListParticipouProjetoPesquisa()) {
					if (!pesq1.equals(pessoa)) {
						for (Pessoa pessoa2 : pesq1.getListParticipante()) {
							if (!pessoa2.equals(pesq1)) {
								for (ProjetoPesquisa pesq2 : pessoa.getListParticipouProjetoPesquisa()) {
									for (Pessoa pessoa3 : pesq2.getListParticipante()) {
										if (!pessoa.equals(pessoa3) && !pessoa2.equals(pessoa3)) {
											String[] resultado = new String[2];
											resultado[0] = pessoa.getNome();
											resultado[1] = pessoa3.getNome();
											list.add(resultado);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		list.sort(Comparator.comparing(u -> u[0]));
		return list;
	}

	public ArrayList<String[]> InferirProjetoEmEvento() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getListParticipouTrabalhoEvento().size() > 0) {
				for (TrabalhoEvento trabalhoEvento : pessoa.getListParticipouTrabalhoEvento()) {
					for (Evento evento : trabalhoEvento.getListParticipouEvento()) {
						for (Pessoa pessoa2 : evento.getListParticipante()) {
							if (!pessoa.equals(pessoa2)) {
								String[] resultado = new String[2];
								resultado[0] = pessoa.getNome();
								resultado[1] = pessoa2.getNome();
								list.add(resultado);
							}
						}
					}
				}
			}
		}
		list.sort(Comparator.comparing(u -> u[0]));
		return list;
	}

	public ArrayList<String[]> InferirAreaConhecimento() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for (AreaConhecimento areaConhecimento : this.listParticipouAreaConhecimento) {
			for (int i = 0; i < areaConhecimento.getListParticipante().size(); i++) {
				for (int j = 0; j < areaConhecimento.getListParticipante().size(); j++) {
					if (i != j) {
						String[] resultado = new String[2];
						resultado[0] = areaConhecimento.getListParticipante().get(i).getNome();
						resultado[1] = areaConhecimento.getListParticipante().get(j).getNome();
						list.add(resultado);
					}
				}
			}
		}
		list.sort(Comparator.comparing(u -> u[0]));
		return list;
	}

	public ArrayList<String[]> InferirSubArea() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for (SubArea subArea : this.listParticipouSubArea) {
			for (int i = 0; i < subArea.getListParticipante().size(); i++) {
				for (int j = 0; j < subArea.getListParticipante().size(); j++) {
					if (i != j) {
						String[] resultado = new String[2];
						resultado[0] = subArea.getListParticipante().get(i).getNome();
						resultado[1] = subArea.getListParticipante().get(j).getNome();
						list.add(resultado);
					}
				}
			}
		}
		list.sort(Comparator.comparing(u -> u[0]));
		return list;
	}

	public ArrayList<String[]> InferirEspecialidade() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		for (Especialidade especialidade : this.listParticipouEspecialidade) {
			for (int i = 0; i < especialidade.getListParticipante().size(); i++) {
				for (int j = 0; j < especialidade.getListParticipante().size(); j++) {
					if (i != j) {
						String[] resultado = new String[2];
						resultado[0] = especialidade.getListParticipante().get(i).getNome();
						resultado[1] = especialidade.getListParticipante().get(j).getNome();
						list.add(resultado);
					}
				}
			}
		}
		list.sort(Comparator.comparing(u -> u[0]));
		return list;
	}

	// conta o numero de relações
	public ArrayList<String[]> contRelacoes(ArrayList<String[]> lista) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		ArrayList<Relacao> listrelacao = new ArrayList<Relacao>();

		for (int i = 0; i < lista.size(); i++) {
			String nome = lista.get(i)[0];
			Relacao relacao = new Relacao(nome);
			int j = 0;
			for (j = i; (j < lista.size()) && (lista.get(j)[0].contentEquals(nome)); j++) {
				relacao.AddListParticipante(lista.get(j)[1]);
			}
			listrelacao.add(relacao);
			i = j - 1;
		}
		listrelacao.forEach(u -> {
			for (int i = 0; i < u.getListParticipante().size(); i++) {
				String nome = u.getListParticipante().get(i);
				int j = 0;
				int cont = 0;
				String[] res = new String[3];
				for (j = i; (j < u.getListParticipante().size())
						&& (u.getListParticipante().get(j).contentEquals(nome)); j++) {
					cont++;
				}
				i = j - 1;
				res[0] = u.getNome();
				res[1] = u.getListParticipante().get(i);
				res[2] = "" + (cont);
				result.add(res);
			}
		});
		return result;
	}

	public void AddBanca(String[] info) {
		Pessoa resultPe = null;
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getNome().contentEquals(info[0])) {
				resultPe = pessoa;
				break;
			}
		}
		Banca resultba = null;
		for (Banca banca : this.listParticipouBanca) {
			if (banca.getTitulo().contentEquals(info[1])) {
				resultba = banca;
				break;
			}
		}
		if (resultPe != null && resultba == null) {
			Banca banca = new Banca(info[1]);
			banca.AddListParticipante(resultPe);
			resultPe.AddListParticipouBanca(banca);
			AddParticipouBanca(banca);
		} else {
			if (resultPe == null && resultba != null) {
				Pessoa pe = new Pessoa(info[0]);
				pe.AddListParticipouBanca(resultba);
				resultba.AddListParticipante(pe);
				AddParticipante(pe);
			} else {
				if (resultPe != null && resultba != null) {
					resultPe.AddListParticipouBanca(resultba);
					resultba.AddListParticipante(resultPe);
				} else {
					Pessoa pe = new Pessoa(info[0]);
					Banca banca = new Banca(info[1]);
					banca.AddListParticipante(pe);
					pe.AddListParticipouBanca(banca);
					AddParticipante(pe);
					AddParticipouBanca(banca);
				}
			}
		}
	}

	public void AddEvento(String[] info) {
		Pessoa resultPe = null;
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getNome().contentEquals(info[0])) {
				resultPe = pessoa;
				break;
			}
		}
		Evento resultEv = null;
		for (Evento evento : this.listParticipouEvento) {
			if (evento.getTitulo().contentEquals(info[1])) {
				resultEv = evento;
				break;
			}
		}
		if (resultPe != null && resultEv == null) {
			Evento evento = new Evento(info[1]);
			evento.AddListParticipante(resultPe);
			resultPe.AddListParticipouEvento(evento);
			AddListParticipouEvento(evento);
		} else {
			if (resultPe == null && resultEv != null) {
				Pessoa pe = new Pessoa(info[0]);
				pe.AddListParticipouEvento(resultEv);
				resultEv.AddListParticipante(pe);
				AddParticipante(pe);
			} else {
				if (resultPe != null && resultEv != null) {
					resultPe.AddListParticipouEvento(resultEv);
					resultEv.AddListParticipante(resultPe);
				} else {
					Pessoa pe = new Pessoa(info[0]);
					Evento evento = new Evento(info[1]);
					evento.AddListParticipante(pe);
					pe.AddListParticipouEvento(evento);
					AddParticipante(pe);
					AddListParticipouEvento(evento);
				}
			}
		}
	}

	public void AddAreaAtuacao(String[] info) {
		Pessoa resultPe = null;

		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getNome().contentEquals(info[0])) {
				resultPe = pessoa;
				break;
			}
		}
		AreaAtuacao resultAA = null;
		for (AreaAtuacao areatucao : this.listParticipouAreaAtuacao) {
			if (areatucao.getTitulo().contentEquals(info[1])) {
				resultAA = areatucao;
				break;
			}
		}
		if (resultPe != null && resultAA == null) {
			AreaAtuacao areatucao = new AreaAtuacao(info[1]);
			areatucao.AddListParticipante(resultPe);
			resultPe.AddListParticipouAreaAtuacao(areatucao);
			AddListParticipouAreaAtuacao(areatucao);
		} else {
			if (resultPe == null && resultAA != null) {
				Pessoa pe = new Pessoa(info[0]);
				pe.AddListParticipouAreaAtuacao(resultAA);
				resultAA.AddListParticipante(pe);
				AddParticipante(pe);
			} else {
				if (resultPe != null && resultAA != null) {
					resultPe.AddListParticipouAreaAtuacao(resultAA);
					resultAA.AddListParticipante(resultPe);
				} else {
					Pessoa pe = new Pessoa(info[0]);
					AreaAtuacao areatucao = new AreaAtuacao(info[1]);
					areatucao.AddListParticipante(pe);
					pe.AddListParticipouAreaAtuacao(areatucao);
					AddParticipante(pe);
					AddListParticipouAreaAtuacao(areatucao);
				}
			}
		}
	}

	public void AddAreaConhecimento(String[] info) {
		Pessoa resultPe = null;
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getNome().contentEquals(info[0])) {
				resultPe = pessoa;
				break;
			}
		}
		AreaConhecimento resultAC = null;
		for (AreaConhecimento areaconhecimento : this.listParticipouAreaConhecimento) {
			if (areaconhecimento.getTitulo().contentEquals(info[1])) {
				resultAC = areaconhecimento;
				break;
			}
		}
		if (resultPe != null && resultAC == null) {
			AreaConhecimento areaconhecimento = new AreaConhecimento(info[1]);
			areaconhecimento.AddListParticipante(resultPe);
			resultPe.AddListParticipouAreaConhecimento(areaconhecimento);
			AddListParticipouAreaConhecimento(areaconhecimento);
		} else {
			if (resultPe == null && resultAC != null) {
				Pessoa pe = new Pessoa(info[0]);
				pe.AddListParticipouAreaConhecimento(resultAC);
				resultAC.AddListParticipante(pe);
				AddParticipante(pe);
			} else {
				if (resultPe != null && resultAC != null) {
					resultPe.AddListParticipouAreaConhecimento(resultAC);
					resultAC.AddListParticipante(resultPe);
				} else {
					Pessoa pe = new Pessoa(info[0]);
					AreaConhecimento areaconhecimento = new AreaConhecimento(info[1]);
					areaconhecimento.AddListParticipante(pe);
					pe.AddListParticipouAreaConhecimento(areaconhecimento);
					AddParticipante(pe);
					AddListParticipouAreaConhecimento(areaconhecimento);
				}
			}
		}
	}

	public void AddSubArea(String[] info) {
		Pessoa resultPe = null;
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getNome().contentEquals(info[0])) {
				resultPe = pessoa;
				break;
			}
		}
		SubArea resultSA = null;
		for (SubArea subarea : this.listParticipouSubArea) {
			if (subarea.getTitulo().contentEquals(info[1])) {
				resultSA = subarea;
				break;
			}
		}
		if (resultPe != null && resultSA == null) {
			SubArea subarea = new SubArea(info[1]);
			subarea.AddListParticipante(resultPe);
			resultPe.AddListParticipouSubArea(subarea);
			AddListParticipouSubArea(subarea);
		} else {
			if (resultPe == null && resultSA != null) {
				Pessoa pe = new Pessoa(info[0]);
				pe.AddListParticipouSubArea(resultSA);
				resultSA.AddListParticipante(pe);
				AddParticipante(pe);
			} else {
				if (resultPe != null && resultSA != null) {
					resultPe.AddListParticipouSubArea(resultSA);
					resultSA.AddListParticipante(resultPe);
				} else {
					Pessoa pe = new Pessoa(info[0]);
					SubArea subarea = new SubArea(info[1]);
					subarea.AddListParticipante(pe);
					pe.AddListParticipouSubArea(subarea);
					AddParticipante(pe);
					AddListParticipouSubArea(subarea);
				}
			}
		}
	}

	public void AddEspecialidade(String[] info) {
		Pessoa resultPe = null;
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getNome().contentEquals(info[0])) {
				resultPe = pessoa;
				break;
			}
		}
		Especialidade resultEP = null;
		for (Especialidade especialidade : this.listParticipouEspecialidade) {
			if (especialidade.getTitulo().contentEquals(info[1])) {
				resultEP = especialidade;
				break;
			}
		}
		if (resultPe != null && resultEP == null) {
			Especialidade especialidade = new Especialidade(info[1]);
			especialidade.AddListParticipante(resultPe);
			resultPe.AddListParticipouEspecialidade(especialidade);
			AddListParticipouEspecialidade(especialidade);
		} else {
			if (resultPe == null && resultEP != null) {
				Pessoa pe = new Pessoa(info[0]);
				pe.AddListParticipouEspecialidade(resultEP);
				resultEP.AddListParticipante(pe);
				AddParticipante(pe);
			} else {
				if (resultPe != null && resultEP != null) {
					resultPe.AddListParticipouEspecialidade(resultEP);
					resultEP.AddListParticipante(resultPe);
				} else {
					Pessoa pe = new Pessoa(info[0]);
					Especialidade eveespecialidadento = new Especialidade(info[1]);
					eveespecialidadento.AddListParticipante(pe);
					pe.AddListParticipouEspecialidade(eveespecialidadento);
					AddParticipante(pe);
					AddListParticipouEspecialidade(eveespecialidadento);
				}
			}
		}
	}

	public void AddOrientacao(String[] info) {
		Pessoa resultOrientador = null;
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getNome().contentEquals(info[0])) {
				resultOrientador = pessoa;
				break;
			}
		}

		Pessoa resultAluno = null;
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getNome().contentEquals(info[1])) {
				resultAluno = pessoa;
				break;
			}
		}

		if (resultOrientador != null && resultAluno == null) {
			Pessoa aluno = new Pessoa(info[1]);
			resultOrientador.AddListAlunosOrientados(aluno);
			aluno.AddListOrientadores(resultOrientador);
			AddParticipante(aluno);
		} else {
			if (resultOrientador == null && resultAluno != null) {
				Pessoa orientador = new Pessoa(info[0]);
				resultAluno.AddListOrientadores(orientador);
				orientador.AddListAlunosOrientados(resultAluno);
				AddParticipante(orientador);
			} else {
				if (resultOrientador != null && resultAluno != null) {
					resultAluno.AddListOrientadores(resultOrientador);
					resultOrientador.AddListAlunosOrientados(resultAluno);
				} else {
					Pessoa orientador = new Pessoa(info[0]);
					Pessoa aluno = new Pessoa(info[1]);
					orientador.AddListAlunosOrientados(aluno);
					aluno.AddListOrientadores(orientador);
					AddParticipante(orientador);
					AddParticipante(aluno);
				}
			}
		}
	}

	public void AddProjetoPesquisa(String[] info) {
		Pessoa resultPe = null;
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getNome().contentEquals(info[0])) {
				resultPe = pessoa;
				break;
			}
		}
		ProjetoPesquisa resultEP = null;
		for (ProjetoPesquisa projetoPesquisa : this.listParticipouProjetoPesquisa) {
			if (projetoPesquisa.getTitulo().contentEquals(info[1])) {
				resultEP = projetoPesquisa;
				break;
			}
		}
		if (resultPe != null && resultEP == null) {
			ProjetoPesquisa projetoPesquisa = new ProjetoPesquisa(info[1]);
			projetoPesquisa.AddListParticipante(resultPe);
			resultPe.AddListParticipouProjetoPesquisa(projetoPesquisa);
			AddListParticipouProjetoPesquisa(projetoPesquisa);
		} else {
			if (resultPe == null && resultEP != null) {
				Pessoa pe = new Pessoa(info[0]);
				pe.AddListParticipouProjetoPesquisa(resultEP);
				resultEP.AddListParticipante(pe);
				AddParticipante(pe);
			} else {
				if (resultPe != null && resultEP != null) {
					resultPe.AddListParticipouProjetoPesquisa(resultEP);
					resultEP.AddListParticipante(resultPe);
				} else {
					Pessoa pe = new Pessoa(info[0]);
					ProjetoPesquisa projetoPesquisa = new ProjetoPesquisa(info[1]);
					projetoPesquisa.AddListParticipante(pe);
					pe.AddListParticipouProjetoPesquisa(projetoPesquisa);
					AddParticipante(pe);
					AddListParticipouProjetoPesquisa(projetoPesquisa);
				}
			}
		}
	}

	public void AddProjetoEventoePessoa(String[] info) {
		Pessoa resultPe = null;
		for (Pessoa pessoa : this.listParticipante) {
			if (pessoa.getNome().contentEquals(info[0])) {
				resultPe = pessoa;
				break;
			}
		}
		TrabalhoEvento resultTE = null;
		for (TrabalhoEvento trabalhoEventoXml : this.listParticipouTrabalhoEvento) {
			if (trabalhoEventoXml.getTitulo().contentEquals(info[1])) {
				resultTE = trabalhoEventoXml;
				break;
			}
		}
		if (resultPe != null && resultTE == null) {
			TrabalhoEvento trabalhoEvento = new TrabalhoEvento(info[1]);
			trabalhoEvento.AddListParticipante(resultPe);
			resultPe.AddListParticipouTrabalhoEvento(trabalhoEvento);
			AddListParticipouTrabalhoEvento(trabalhoEvento);
		} else {
			if (resultPe == null && resultTE != null) {
				Pessoa pe = new Pessoa(info[0]);
				pe.AddListParticipouTrabalhoEvento(resultTE);
				resultTE.AddListParticipante(pe);
				AddParticipante(pe);
			} else {
				if (resultPe != null && resultTE != null) {
					resultPe.AddListParticipouTrabalhoEvento(resultTE);
					resultTE.AddListParticipante(resultPe);
				} else {
					Pessoa pe = new Pessoa(info[0]);
					TrabalhoEvento trabalhoEventoXml = new TrabalhoEvento(info[1]);
					trabalhoEventoXml.AddListParticipante(pe);
					pe.AddListParticipouTrabalhoEvento(trabalhoEventoXml);
					AddParticipante(pe);
					AddListParticipouTrabalhoEvento(trabalhoEventoXml);
				}
			}
		}
	}

	public void AddProjetoEventoParaEvento(String[] info) {
		Evento resultEv = null;
		for (Evento evento : this.listParticipouEvento) {
			if (evento.getTitulo().contentEquals(info[0])) {
				resultEv = evento;
				break;
			}
		}
		TrabalhoEvento resultTE = null;
		for (TrabalhoEvento trabalhoEventoXml : this.listParticipouTrabalhoEvento) {
			if (trabalhoEventoXml.getTitulo().contentEquals(info[1])) {
				resultTE = trabalhoEventoXml;
				break;
			}
		}
		if (resultEv != null && resultTE == null) {
			TrabalhoEvento trabalhoEvento = new TrabalhoEvento(info[1]);
			trabalhoEvento.AddListParticipouEvento(resultEv);
			resultEv.AddListTrabalhoEvento(trabalhoEvento);
			AddListParticipouTrabalhoEvento(trabalhoEvento);
		} else {
			if (resultEv == null && resultTE != null) {
				Evento evento = new Evento(info[0]);
				evento.AddListTrabalhoEvento(resultTE);
				resultTE.AddListParticipouEvento(evento);
				AddListParticipouEvento(evento);
			} else {
				if (resultEv != null && resultTE != null) {
					resultEv.AddListTrabalhoEvento(resultTE);
					resultTE.AddListParticipouEvento(resultEv);
				} else {
					Evento evento = new Evento(info[0]);
					TrabalhoEvento trabalhoEventoXml = new TrabalhoEvento(info[1]);
					trabalhoEventoXml.AddListParticipouEvento(evento);
					evento.AddListTrabalhoEvento(trabalhoEventoXml);
					AddListParticipouEvento(evento);
					AddListParticipouTrabalhoEvento(trabalhoEventoXml);
				}
			}
		}
	}

	// imprimi resultado final
	public void imprimirResultado(Map<String, String> mapNome) {
		this.listResultado.sort(Comparator.comparing(u -> u.getTotal()));
		this.listResultado.forEach(u -> {
			String nome1 = u.getListParticipante().get(0).getNome();
			String nome2 = u.getListParticipante().get(1).getNome();
			
			if (u.getListParticipante().get(0).getNome().matches("-?\\d+(\\.\\d+)?")) {
				nome1 = mapNome.get(u.getListParticipante().get(0).getNome());
			}
			if (u.getListParticipante().get(1).getNome().matches("-?\\d+(\\.\\d+)?")) {
				nome2 = mapNome.get(u.getListParticipante().get(1).getNome());
			}
			System.out.println("[\"" + nome1 + "\" , \"" + nome2 + "\" , " + u.getTotal() + "], ");
		});
	}

	public void AddParticipante(Pessoa listParticipante) {
		this.listParticipante.add(listParticipante);
		this.listParticipante.sort(Comparator.comparing(u -> u.getNome()));
	}

	public void AddParticipouBanca(Banca listParticipouBanca) {
		this.listParticipouBanca.add(listParticipouBanca);
	}

	public ArrayList<Pessoa> getListParticipante() {
		return this.listParticipante;
	}

	public void setListParticipante(ArrayList<Pessoa> listParticipante) {
		this.listParticipante = listParticipante;
	}

	public ArrayList<Banca> getListParticipouBanca() {
		return this.listParticipouBanca;
	}

	public void setListParticipouBanca(ArrayList<Banca> listParticipouBanca) {
		this.listParticipouBanca = listParticipouBanca;
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

	public ArrayList<AreaAtuacao> getListParticipouAreaAtuacao() {
		return this.listParticipouAreaAtuacao;
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
