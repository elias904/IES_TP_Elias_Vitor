package persistencia;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import Fabrica.Fabrica;
import entidade.EmprestimoEntidade;
import entidade.Entidade;
import visao.Visao;

public class EmprestimoPersistencia extends Persistencia{
	static java.io.File diretorio = null;
	static java.io.File arquivo;
	private static FileWriter texto = null;
	private static PrintWriter escrito = null;
	Fabrica fabrica = Fabrica.getFactory("Emprestimo");
	
	//Lista de Emprestimo
	private static List<Entidade> emprestimos = new ArrayList<Entidade>();
	//Declaração da classe como singleton
	private static EmprestimoPersistencia uniqueInstance;
	
	private EmprestimoPersistencia() {
	}
	public static synchronized EmprestimoPersistencia getInstance() {
		if (uniqueInstance == null)
			uniqueInstance = new EmprestimoPersistencia();
		diretorio = new java.io.File("/home/eliasmnds/Desktop/UFSJ 2022.2/EngSoftware");
		arquivo = new java.io.File(diretorio, "emprestimos.txt");
		return uniqueInstance;
	}
	//------------------------------------
	//Criar metodo para salvar o array no arquivo
	//Criar metodo para ler o arquivo e salvar no arrays
	@Override
	public void inserir(Entidade emprestimo) throws IOException {
		// TODO Auto-generated method stub
		emprestimo.setId(emprestimos.size() + 1);
		Boolean verifica = emprestimos.add(emprestimo);
		if(verifica == true) {
			System.out.println("Cadastrado com Sucesso!");
		}
		else {
			System.out.println("Sem sucesso no Cadastro!");
		}
	}

	public void escreverArquivo() throws IOException {
		// TODO Auto-generated method stub
		for(Entidade e: emprestimos) 
		{
			texto = new FileWriter(arquivo, true);
			escrito = new PrintWriter(texto);
			escrito.println("Emprestimo:");
			escrito.println("Id: ~" + e.getId());
			escrito.println("Nome do Livro: &" + e.getNomeLivro());
			escrito.println("Matricula Aluno: #" + e.getMatriculaAluno());
			escrito.flush();
			escrito.close();
		}
	}
	//O objetivo é poder verficiar a existência do emprestimo para poder renová-lo
	@Override
	public void alterar(Entidade emprestimoAntigo, Entidade emprestimoNovo) {
		// TODO Auto-generated method stub
		for(Entidade e: emprestimos) 
		{
			if(e.getMatriculaAluno().equals(emprestimoAntigo.getMatriculaAluno())  && e.getNomeLivro().equals(emprestimoAntigo.getNomeLivro())){
				EmprestimoEntidade novoEmprestimo = (EmprestimoEntidade) e;
				novoEmprestimo.setMatriculaAluno(emprestimoNovo.getMatriculaAluno());
				novoEmprestimo.setNomeLivro(emprestimoNovo.getNomeLivro());
				return;
			}
		}
		
	}
	@Override
	public void excluir(Entidade emprestimo) {
		// TODO Auto-generated method stub
		for(Entidade e: emprestimos) 
		{
			if(e.getMatriculaAluno().equals(emprestimo.getMatriculaAluno())  && e.getNomeLivro().equals(emprestimo.getNomeLivro())) {
				emprestimos.remove(e);
				break;
			}
		}
		
	}
	@Override
	public void buscaPorId(int Id) {
		// TODO Auto-generated method stub
		for(Entidade e: emprestimos) 
		{
			if(e.getId().equals(Id)) {
				Visao visao;
				visao = fabrica.createVisao();
				visao.viewBusca(e);
				break;
			}
		}
	}
	@Override
	public void buscaPorString(String Nome) {
		// TODO Auto-generated method stub
		for(Entidade e: emprestimos) 
		{
			if(e.getNomeLivro().equals(Nome)) {
				Visao visao;
				visao = fabrica.createVisao();
				visao.viewBusca(e);
				break;
			}
		}
	}
	@Override
	public void visualizar() {
		// TODO Auto-generated method stub
		Visao visao;
		visao = fabrica.createVisao();
		visao.view(emprestimos);
	}
	@Override
	public void carregarArquivo() throws IOException {
		// TODO Auto-generated method stub
		if(arquivo.exists()) {
			FileReader fr = new FileReader(arquivo);
			BufferedReader br = new BufferedReader(fr);
			
			
			List<String> ids = new ArrayList<String>();
			List<String> nomeLivros = new ArrayList<String>();
			List<String> matriculaAlunos = new ArrayList<String>();
			
				while(br.ready()) 
				{
					String linha = br.readLine();
					
					//id
					if(linha.contains("~")) 
					{
						String array[] = linha.split("~");
						ids.add(array[1]);
					}
					//Nome do Livro
					if(linha.contains("&")) 
					{
						String array[] = linha.split("&");
						nomeLivros.add(array[1]);
					}
					//Matricula do Aluno
					if(linha.contains("#")) 
					{
						String array[] = linha.split("#");
						matriculaAlunos.add(array[1]);
					}
				}
			Integer tamanho = nomeLivros.size();
			
			
			for(int i = 0; i < tamanho; i++) {
				
				EmprestimoEntidade emprestimo = (EmprestimoEntidade) fabrica.createEntidade();
				
				emprestimo.setId(Integer.parseInt(ids.get(i)));
				emprestimo.setNomeLivro(nomeLivros.get(i));
				emprestimo.setMatriculaAluno(matriculaAlunos.get(i));
				emprestimos.add(emprestimo);
			}

			br.close();
			fr.close();
			arquivo.delete();
			
	}
  }
}
