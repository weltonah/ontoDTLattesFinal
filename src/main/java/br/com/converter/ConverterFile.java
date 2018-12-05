package br.com.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ConverterFile {
	
	// Converte o XML ou o ZIP para o forma Document que pode ser explorado com o
	// Xpath
	public static Document ConverterFileToDocument(File file) {
		Document xmlfile = null;
		String nome = file.getName();
        InputStream stream;
        ZipFile zf = null;
		// Se arquivo é em .zip
        if(nome.contains(".zip")){
            try {
				zf = new ZipFile(file);
				ZipEntry ze = zf.entries().nextElement();
				stream = zf.getInputStream(ze);
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				xmlfile = docBuilder.parse(stream);
				ze.clone();
				zf.close();
				stream.close();
			} catch (IOException | ParserConfigurationException | SAXException e) {
				e.printStackTrace();
			}

        }
		// se o arquivo é em .xml
        else{
			try {
				stream = new FileInputStream(file);
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				xmlfile = docBuilder.parse(stream);
				stream.close();
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}

        }
        return xmlfile;
	}
		
	
	
}
