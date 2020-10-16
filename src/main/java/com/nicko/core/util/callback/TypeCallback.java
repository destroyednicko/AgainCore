package com.nicko.core.util.callback;

import java.io.Serializable;

public interface TypeCallback<T> extends Serializable {

    /**
     * Callback pra uma 'task' rodando um 'dataSet'
     *
     * @param data dado necessário pra rodar a 'task'
     */
    void callback(T data);

}
