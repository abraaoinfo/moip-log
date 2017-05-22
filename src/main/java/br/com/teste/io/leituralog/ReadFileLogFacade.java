package br.com.teste.io.leituralog;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;



public class ReadFileLogFacade {
	
	
	private  Logger log = Logger.getLogger(ReadFileLogFacade.class);
	
	public void frequenceUrlAndCode(){
	      ReadFileLog arq =new ReadFileLog();
          LinkedHashMap<String, Map<String, Long>> out;
		
          try {
			  out = arq.redFileLog();
			  arq.getListUrl(out)
			  .forEach((v)->System.out.println( v.getKey() + "-" + v.getValue()));
			  
			  arq.getListStatusCode(out)
			  .forEach((v)->System.out.println( v.getKey() + "-" + v.getValue()));                                
		} catch (Exception e) {
			log.error(e.getMessage());
		}
        
		
		
	}

}
