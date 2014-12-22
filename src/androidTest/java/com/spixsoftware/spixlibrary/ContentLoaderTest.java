package com.spixsoftware.spixlibrary;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.spixsoftware.spixlibrary.tools.contentpreparer.ContentLoader;
import com.spixsoftware.spixlibrary.tools.contentpreparer.ContentPreparer;

public class ContentLoaderTest extends ApplicationTestCase<Application> {


    public ContentLoaderTest() {
        super(Application.class);
    }


    public void testGetContent() {

        ContentPreparer<String, String> contentPreparer = new ContentPreparer<>();

        String result = contentPreparer.getContent("1", new ContentLoader<String, String>() {
            @Override
            public String loadContent(String s) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "result";
            }

            @Override
            public String prepareArguments() {
                return "prepared";
            }
        });
        assertEquals("result", result);
    }

    public void testGetPreparedContent() {
        ContentPreparer<String, String> contentPreparer = new ContentPreparer<>();

        final ContentLoader<String, String> loader = new ContentLoader<String, String>() {
            @Override
            public String loadContent(String s) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "result";
            }

            @Override
            public String prepareArguments() {
                return "prepared";
            }
        };

        contentPreparer.prepareContent("1", loader);

        String result = contentPreparer.getContent("1", loader);

        assertEquals("result", result);
    }

    public void testGetPreparedContent2() {
        ContentPreparer<String, String> contentPreparer = new ContentPreparer<>();

        final ContentLoader<String, String> loader = new ContentLoader<String, String>() {
            @Override
            public String loadContent(String s) {
                return "result";
            }

            @Override
            public String prepareArguments() {
                return "prepared";
            }
        };

        contentPreparer.prepareContent("1", loader);

        String result = contentPreparer.getContent("1", loader);

        assertEquals("result", result);
    }

    public void testGetPreparedContent3() {
        ContentPreparer<String, String> contentPreparer = new ContentPreparer<>();

        final ContentLoader<String, String> loader = new ContentLoader<String, String>() {
            @Override
            public String loadContent(String s) {
                return "result";
            }

            @Override
            public String prepareArguments() {
                return "prepared";
            }
        };

        contentPreparer.prepareContent("1", loader);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result = contentPreparer.getContent("1", loader);

        assertEquals("result", result);
    }

}
