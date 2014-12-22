package com.spixsoftware.spixlibrary.tools.contentpreparer;

public interface ContentLoader<ARG, RESULT> {

    public RESULT loadContent(ARG arg);

    public ARG prepareArguments();

}
