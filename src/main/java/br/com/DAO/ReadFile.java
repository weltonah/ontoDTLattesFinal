package br.com.DAO;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

public class ReadFile {
	// Pega o OWL raiz na pasta /static/OWL/datalattes.owl faz uma copia para a raiz
	// do projeto
	public static File PegarFile(String NomeArq) {
		try {
			File file = new ClassPathResource("/static/OWL/datalattes.owl").getFile();
			File ont = new File(System.getProperty("user.dir") + "/" + NomeArq);
			FileUtils.copyFile(file, ont);
			return ont;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
