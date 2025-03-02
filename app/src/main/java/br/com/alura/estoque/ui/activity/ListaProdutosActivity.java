package br.com.alura.estoque.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.alura.estoque.R;
import br.com.alura.estoque.model.Produto;
import br.com.alura.estoque.repository.ProdutoRepository;
import br.com.alura.estoque.ui.dialog.EditaProdutoDialog;
import br.com.alura.estoque.ui.dialog.SalvaProdutoDialog;
import br.com.alura.estoque.ui.recyclerview.adapter.ListaProdutosAdapter;

public class ListaProdutosActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "Lista de produtos";
    public static final String MENSAGEM_ERRO_CARREGAR_PRODUTOS = "Não foi possivel carregar os produtos novos";
    public static final String MENSAGEM_ERRO_REMOVER_PRODUTOS = "Não foi possivel remover o produto";
    public static final String MENSAGEM_ERRO_SALVAR = "Não possível salvar o produto";
    public static final String MENSAGEM_ERRO_EDICAO = "Não possível editar o produto";
    private ListaProdutosAdapter adapter;
    private ProdutoRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        setTitle(TITULO_APPBAR);

        configuraListaProdutos();
        configuraFabSalvaProduto();

        repository = new ProdutoRepository(this);

        buscaProdutos();
    }

    private void buscaProdutos() {
        repository.buscaProdutos(new ProdutoRepository.DadosCarregadosCallback<List<Produto>>() {
            @Override
            public void quandoSucesso(List<Produto> produtos) {
                adapter.atualiza(produtos);
            }

            @Override
            public void quandoFalha(String erro) {
                mostraErro(MENSAGEM_ERRO_CARREGAR_PRODUTOS);
            }
        });
    }

    private void mostraErro(String mensagem) {
        Toast.makeText(ListaProdutosActivity.this,
                mensagem,
                Toast.LENGTH_SHORT).show();
    }

    private void configuraListaProdutos() {
        RecyclerView listaProdutos = findViewById(R.id.activity_lista_produtos_lista);
        adapter = new ListaProdutosAdapter(this, this::abreFormularioEditaProduto);
        listaProdutos.setAdapter(adapter);
        adapter.setOnItemClickRemoveContextMenuListener(this::remove);
    }

    private void remove(int posicao, Produto produtoEscolhido) {
        repository.remove(produtoEscolhido, new ProdutoRepository.DadosCarregadosCallback<Void>() {
            @Override
            public void quandoSucesso(Void resultado) {
                adapter.remove(posicao);
            }

            @Override
            public void quandoFalha(String erro) {
                mostraErro(MENSAGEM_ERRO_REMOVER_PRODUTOS);
            }
        });
    }

    private void configuraFabSalvaProduto() {
        FloatingActionButton fabAdicionaProduto = findViewById(R.id.activity_lista_produtos_fab_adiciona_produto);
        fabAdicionaProduto.setOnClickListener(v -> abreFormularioSalvaProduto());
    }

    private void abreFormularioSalvaProduto() {
        new SalvaProdutoDialog(this, this::salva).mostra();
    }

    private void salva(Produto produtoCriado) {
        repository.salva(produtoCriado, new ProdutoRepository.DadosCarregadosCallback<Produto>() {
            @Override
            public void quandoSucesso(Produto produto) {
                adapter.adiciona(produto);
            }

            @Override
            public void quandoFalha(String erro) {
                mostraErro(MENSAGEM_ERRO_SALVAR);
            }
        });
    }

    private void abreFormularioEditaProduto(int posicao, Produto produto) {
        new EditaProdutoDialog(this, produto,
                produtoCriado -> edita(posicao, produtoCriado))
                .mostra();
    }

    private void edita(int posicao, Produto produtoCriado) {
        repository.edita(produtoCriado, new ProdutoRepository.DadosCarregadosCallback<Produto>() {
            @Override
            public void quandoSucesso(Produto produtoEditado) {
                adapter.edita(posicao, produtoEditado);
            }

            @Override
            public void quandoFalha(String erro) {
                mostraErro(MENSAGEM_ERRO_EDICAO);
            }
        });
    }
}
