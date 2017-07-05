package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
    /**
     * Convert file content to string
     * @param url
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String fileToString(URL url) throws URISyntaxException, IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));
        String file = new String(encoded, Charset.defaultCharset());
        return file;
    }
}
