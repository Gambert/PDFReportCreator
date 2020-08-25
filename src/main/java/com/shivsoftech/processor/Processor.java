package com.shivsoftech.processor;

import com.shivsoftech.core.AppContext;

public interface Processor<E> {

    public void setContext(AppContext context);

    public void doProcess();
}
