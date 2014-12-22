package com.spixsoftware.spixlibrary.tools.contentpreparer;

public interface ContentLoader<ARGS,RESULT> {

    public RESULT loadContent(ARGS... args);

}
