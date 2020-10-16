package com.nicko.core.util.callback;

import java.io.Serializable;

public interface Callback extends Serializable {

    /**
     * Callback pra uma 'task' rodando um 'dataSet'
     */
    void callback();

}
