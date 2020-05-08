package com.company.examples;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

public class ExampleTest {

    @BeforeClass
    public static void setup() {
    }

    String run(String url) throws IOException {
        /*
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
        */
        return null;
    }

    @Test
    public void test() {
        try {
            String response = run("https://raw.github.com/square/okhttp/master/README.md");
            System.out.println(response);
        } catch (IOException ioe) {
            fail();
        }
    }
}
