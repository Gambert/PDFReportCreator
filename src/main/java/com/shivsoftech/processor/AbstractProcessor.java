package com.shivsoftech.processor;

import com.shivsoftech.core.AppContext;

public abstract class AbstractProcessor<E> implements Processor {

    protected AppContext context;

    public AbstractProcessor() {
        setContext(AppContext.getInstance());
    }

    @Override
    public void setContext(AppContext context) {
        this.context = context;
    }
}
