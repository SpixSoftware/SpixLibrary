package com.spixsoftware.spixlibrary;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.spixsoftware.spixlibrary.tools.contentpreparer.ContentLoader;
import com.spixsoftware.spixlibrary.tools.contentpreparer.ContentPreparer;

import java.util.Random;

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

    public void testGetContentMultiple() {

        for (int i = 0; i < 1000; i++) {
            ContentPreparer<String, String> contentPreparer = new ContentPreparer<>();

            String result = contentPreparer.getContent("" + i, new ContentLoader<String, String>() {
                @Override
                public String loadContent(String s) {
                    try {
                        Thread.sleep(randInt(0, 50));
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

    public void testGetPreparedContentMultiple() {

        for (int i = 0; i < 1000; i++) {

            ContentPreparer<String, String> contentPreparer = new ContentPreparer<>();

            final ContentLoader<String, String> loader = new ContentLoader<String, String>() {
                @Override
                public String loadContent(String s) {
                    try {
                        Thread.sleep(randInt(0, 50));
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

            contentPreparer.prepareContent("" + i, loader);

            try {
                Thread.sleep(randInt(0, 50));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String result = contentPreparer.getContent("" + i, loader);

            assertEquals("result", result);
        }
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

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
