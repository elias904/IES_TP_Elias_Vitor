package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import Fabrica.Fabrica;
import entidade.EmprestimoEntidade;
import entidade.Entidade;
import entidade.LivroEntidade;
import visao.Visao;

public class LivroPersistencia extends Persistencia{
	static java.io.File diretorio = null;
	static java.io.File arquivo;
	private static FileWriter texto = null;
	private static PrintWriter escrito = null;
	Fabrica fabrica = Fabrica.getFactory("Livro");
	private static List<Entidade> livros = new ArrayList<Entidade>();

	//Declaração da classe como singleton
	private static LivroPersistencia uniqueInstance;
	
	private LivroPersistencia() {
	}
	public static synchronized LivroPersistencia getInstance() {
		if (uniqueInstance == null)
			diretorio = new java.io.File("/home/eliasmnds/Desktop/UFSJ 2022.2/EngSoftware");
			arquivo = new java.io.File(diretorio, "livros.txt");
			uniqueInstance = new LivroPersistencia();

		return uniqueInstance;
	}
	//-------------------------------------------
	@Override
	public void inserir(Entidade livro) {
		// TODO Auto-generated method stub
		livro.setId(livros.size() + 1);
		livros.add(livro);
	}
	
	public void escreverArquivo() throws IOException {
		// TODO Auto-generated method stub
		for(Entidade l: livros) 
		{
			texto = new FileWriter(arquivo, true);
			escrito = new PrintWriter(texto);
			escrito.println("Livro:");
			escrito.println("Id: ~" + l.getId());
			escrito.println("Nome do Livro: &" + l.getNomeLivro());
			escrito.flush();
			escrito.close();
		}
	}
	@Override
	public void alterar(Entidade livroAntigo, Entidade livroNovo) {
		// TODO Auto-generated method stub
		for(Entidade l: livros) 
		{
			if(l.getNomeLivro().equals(livroAntigo.getNomeLivro())){
				LivroEntidade novoLivro = (LivroEntidade) l;
				novoLivro.setNomeLivro(livroNovo.getNomeLivro());
				return;
			}
		}
		
	}
	@Override
	public void excluir(Entidade livro) {
		// TODO Auto-generated method stub
		for(Entidade l: livros) 
		{
			if(l.getNomeLivro().equals(livro.getNomeLivro())) {
				livros.remove(l);
				break;
			}
		}
	}
	@Override
	public void buscaPorId(int Id) {
		// TODO Auto-generated method stub
		for(Entidade l: livros) 
		{
			if(l.getId().equals(Id)) {
				Visao visao;
				Fabrica fabrica;
				fabrica = Fabrica.getFactory("Livro");
				visao = fabrica.createVisao();
				visao.viewBusca(l);
			}
		}
	}
	@Override
	public void buscaPorString(String Nome) {
		// TODO Auto-generated method stub
		for(Entidade l: livros) 
		{
			if(l.getNomeLivro().equals(Nome)) {
				Visao visao;
				Fabrica fabrica;
				fabrica = Fabrica.getFactory("Livro");
				visao = fabrica.createVisao();
				visao.viewBusca(l);
			}
		}
	}
	@Override
	public void visualizar() {
		// TODO Auto-generated method stub
		Visao visao;
		Fabrica fabrica;
		fabrica = Fabrica.getFactory("Livro");
		visao = fabrica.createVisao();
		visao.view(livros);
		
	}
	@Override
	public void carregarArquivo() throws IOException {
		// TODO Auto-generated method stub
		if(arquivo.exists()) {
			FileReader fr = new FileReader(arquivo);
			BufferedReader br = new BufferedReader(fr);
			
			
			List<String> ids = new ArrayList<String>();
			List<String> nomeLivros = new ArrayList<String>();
			
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
				}
			Integer tamanho = nomeLivros.size();
			
			
			for(int i = 0; i < tamanho; i++) {
				
				LivroEntidade livro = (LivroEntidade) fabrica.createEntidade();
				livro.setId(Integer.parseInt(ids.get(i)));
				livro.setNomeLivro(nomeLivros.get(i));
				livros.add(livro);
			}

			br.close();
			fr.close();
			arquivo.delete();
	}
} }
