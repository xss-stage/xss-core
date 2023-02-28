package org.stage.xss.core;

public interface XssFilter{

    String getFilterName();

    <P> P doFilter(Object dirty, Class<P> cast);

}
