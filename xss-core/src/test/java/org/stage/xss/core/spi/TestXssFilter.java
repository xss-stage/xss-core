package org.stage.xss.core.spi;

import org.springframework.stereotype.Component;

@Component
public class TestXssFilter implements XssFilter{

    private static final String FILTER_NAME = "string";

    @Override
    public String getFilterName(){
        return FILTER_NAME;
    }

    @Override
    public <P> P doFilter(Object dirty, Class<P> cast){
        return cast.cast("hello world");
    }

}
