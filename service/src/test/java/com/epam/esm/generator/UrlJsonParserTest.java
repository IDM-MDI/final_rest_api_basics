package com.epam.esm.generator;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;


@PrepareForTest(UrlJsonParser.class)
class UrlJsonParserTest {
    private static final String URL = "testURL";
    private final URLConnection conn = mock(URLConnection.class);
    private final InputStream stubInputStream = IOUtils.toInputStream("{some: TestDataForMyInputStream}", "UTF-8");
    @SneakyThrows
    @Test
    void readJsonFromUrl() {
        try(MockedConstruction<URL> mockedURL = mockConstruction(URL.class, (url, context) -> {
            when(url.openConnection()).thenReturn(conn);
        })) {
            JsonObject expected = new JsonObject();
            expected.addProperty("some","TestDataForMyInputStream");
            when(conn.getContent())
                    .thenReturn(stubInputStream);
            doNothing()
                    .when(conn)
                    .connect();
            JsonObject actual = UrlJsonParser.readJsonFromUrl(URL);
            Assertions.assertEquals(expected,actual);
        }
    }

    @Test
    void readStringFromJson() {
        String expected = "TestDataForMyInputStream";
        JsonObject json = new JsonObject();
        json.addProperty("test",expected);

        String actual = UrlJsonParser.readStringFromJson(json, "test");
        Assertions.assertEquals(expected,actual);
    }
}