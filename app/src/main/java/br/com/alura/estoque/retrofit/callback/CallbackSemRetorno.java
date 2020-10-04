package br.com.alura.estoque.retrofit.callback;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static br.com.alura.estoque.retrofit.callback.MensagemCallback.MENSAGEM_ERRO_FALHA_COMUNICACAO;

public class CallbackSemRetorno implements Callback<Void> {

    private final ResponseCallback callback;

    public CallbackSemRetorno(ResponseCallback callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<Void> call, Response<Void> response) {
        if(response.isSuccessful()){
            callback.quandoSucesso();
        } else {
            callback.quandoFalha(MENSAGEM_ERRO_FALHA_COMUNICACAO);
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<Void> call, Throwable t) {
        callback.quandoFalha(MENSAGEM_ERRO_FALHA_COMUNICACAO + t.getMessage());
    }

    public interface ResponseCallback {
        void quandoSucesso();
        void quandoFalha(String erro);
    }
}
