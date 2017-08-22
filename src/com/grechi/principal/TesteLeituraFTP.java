package com.grechi.principal;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.grechi.utils.ConexaoFTP;

public class TesteLeituraFTP {
	
	static String enderecoFTP = "ftp.exemplo.com";
	static String diretorio = "";
	static String username = "";
	static String password = "";
	static String diretorioDestino = "C:\\Users\\Public\\Downloads";
	
	static JFileChooser fc = new JFileChooser();

	public static void main(String[] args) {
		ConexaoFTP conexaoFTP = new ConexaoFTP(enderecoFTP, diretorio, username, password);
		 
		List<String> listaArquivos = conexaoFTP.listarArquivos();
		
		File file = pegarArquivo(); 
        conexaoFTP.uploadArquivo(file.getAbsolutePath());
        
		conexaoFTP.baixarArquivo(listaArquivos.get(0), diretorioDestino);
		 
		conexaoFTP.deletarArquivo(listaArquivos.get(0));
	}

	private static File pegarArquivo() {
		File file = null;
		int returnVal = fc.showOpenDialog(new JPanel());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }
        
		return file;
	}
			
			
		
	}


