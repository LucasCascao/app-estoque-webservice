package br.com.alura.estoque.retrofit.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static br.com.alura.estoque.retrofit.callback.MensagemCallback.MENSAGEM_ERRO_FALHA_COMUNICACAO;
import static br.com.alura.estoque.retrofit.callback.MensagemCallback.MENSAGEM_ERRO_RESPOSTA_NAO_SUCEDIDA;

public class CallbackComRetorno<T> implements Callback<T> {

    private final ResponseCallback<T> responseCallback;

    public CallbackComRetorno(ResponseCallback<T> responseCallback) {
        this.responseCallback = responseCallback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()){
            T result = response.body();
            if(result != null){
                responseCallback.quandoSucesso(result);
            }
        } else {
            responseCallback.quandoFalha(MENSAGEM_ERRO_RESPOSTA_NAO_SUCEDIDA);
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<T> call, Throwable t) {
        responseCallback.quandoFalha(MENSAGEM_ERRO_FALHA_COMUNICACAO + t.getMessage());
    }

    public interface ResponseCallback<T> {
        void quandoSucesso(T result);
        void quandoFalha(String erro);
    }
}
