package br.com.teste.io.leituralog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadFileLog {

    private static final String SEARCH_URL= "request_to=\"";
    private static final String SEARCH_CODE = "response_status=\"";
    private static int LIMIT =3;


	public  LinkedHashMap<String, Map<String, Long>> redFileLog() throws IOException {
		Path p = Paths.get(System.getProperty("user.dir"), "log.txt");

        LinkedHashMap<String, Map<String, Long>> out =
                Files.lines(p, StandardCharsets.ISO_8859_1)
                        .filter(linha -> linha.contains("request_to") && linha.contains("response_status"))
                        .collect(
                                Collectors.groupingBy(s->getStatusCode(s), LinkedHashMap::new,
                                        Collectors.groupingBy(s->getUrl(s), Collectors.counting()))
                        );
		return out;
	}


	public  Stream<Entry<String, Long>>  getListUrl( final LinkedHashMap<String, Map<String, Long>> out) {
		
             Stream<Entry<String, Long>> output = out.values()
		    .stream()
		    .flatMap(m -> m.entrySet().stream())
		    .collect(Collectors.groupingBy(Map.Entry::getKey,
		            Collectors.summingLong(Map.Entry::getValue)))
		    .entrySet().stream()
		    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
		    .limit(LIMIT);
		   return output;

	}


	public  Stream<Entry<String, Long>>  getListStatusCode(final LinkedHashMap<String, Map<String, Long>> out) {
		Stream<Entry<String, Long>> output  = out.entrySet()
		    .stream()
		    .collect(Collectors.toMap(Map.Entry::getKey,
		            e -> e.getValue().values().stream()
		            .mapToLong(Number::longValue).sum()))
		            .entrySet().stream()
		            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
		            .limit(LIMIT);
		return output;
	}
    

    private String getUrl(final String line) {
    	final int initialInd = line.indexOf(SEARCH_URL) + SEARCH_URL.length();
        final int endInd = line.indexOf("\"", initialInd);
		return line.substring(initialInd, endInd);
    }

    private   String getStatusCode(final String linha) {
    	final int initialInd = linha.indexOf(SEARCH_CODE) + SEARCH_CODE.length();
    	final int endInd = linha.indexOf("\"", initialInd);
        return linha.substring(initialInd, endInd);
    }
}