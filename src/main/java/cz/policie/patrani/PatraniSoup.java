package cz.policie.patrani;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Inicializovaný {@link Jsoup} pro hledání na stránkách http://aplikace.policie.cz
 */
class PatraniSoup {

    // Timeout po který budeme četat na odpověd
    private static final int DEFAULT_TIMEOUT_MILLIS = 60_000; // 60s

    /**
     * Inicializuje {@link Jsoup} pro hledání na stránkách http://aplikace.policie.cz
     *
     * @param url adresa stránky s formulářem
     * @return inicializovaný {@link Jsoup}
     * @throws IOException chyba parsování
     */
    static Connection connect(String url) throws IOException {

        Connection.Response response = Jsoup
                .connect(url)
                .timeout(DEFAULT_TIMEOUT_MILLIS)
                .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0")
                .method(Connection.Method.GET)
                .execute();

        Document doc = response.parse();
        String viewstate = doc.select("input[name=__VIEWSTATE]").attr("value");
        String viewstategenerator = doc.select("input[name=__VIEWSTATEGENERATOR]").attr("value");
        String eventvalidation = doc.select("input[name=__EVENTVALIDATION]").attr("value");

        return Jsoup
                .connect(url)
                .timeout(DEFAULT_TIMEOUT_MILLIS)
                .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0")
                .data("__EVENTTARGET", "")
                .data("__EVENTARGUMENT", "")
                .data("__VIEWSTATE", viewstate)
                .data("__VIEWSTATEGENERATOR", viewstategenerator)
                .data("__EVENTVALIDATION", eventvalidation)
                ;
    }

}
