package com.grechi.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class ConexaoFTP {
	
	private String enderecoFTP;
	private String diretorio;
	private String username;
	private String password;
	
	public ConexaoFTP(String enderecoFTP, String diretorio, String username, String password) {
		super();
		this.enderecoFTP = enderecoFTP;
		this.diretorio = diretorio;
		this.username = username;
		this.password = password;
	}


	public List<String> listarArquivos(){

		FTPClient client = new FTPClient();
		List<String> nomesArquivos = new ArrayList<>();

        try {
            client.connect(enderecoFTP);
            client.login(username, password);
            //Avança uma pasta no servidor FTP
            client.cwd(diretorio);

            if (client.isConnected()) {
                //Obtem a lista de nomes dos arquivos no diretório
                String[] names = client.listNames();
                for (String name : names) {
                    System.out.println("Name = " + name);
                }

                FTPFile[] ftpFiles = client.listFiles();
                for (FTPFile ftpFile : ftpFiles) {
                    //Checa se a FTPFile é um arquivo regular
                    if (ftpFile.getType() == FTPFile.FILE_TYPE) {
                    	nomesArquivos.add(ftpFile.getName());
                    }
                }
            }
            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return nomesArquivos;
	}
	
	
	public void baixarArquivo(String nomeArquivo, String diretorioDestino){

        FTPClient client = new FTPClient();
        try (OutputStream os = new BufferedOutputStream(
        		new FileOutputStream(diretorioDestino + "/" + nomeArquivo))) {
            client.connect(enderecoFTP);
            client.login(username, password);
            client.cwd(diretorio);
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);

            // Realiza download do arquivo
            client.retrieveFile(nomeArquivo, os);
         
            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	public void uploadArquivo(String caminhoArquivo){
	   	FTPClient client = new FTPClient();
	   	File arquivo = new File(caminhoArquivo);
        
        try (InputStream is = new FileInputStream(arquivo)) {
        	client.connect(enderecoFTP);
            client.login(username, password);
            client.cwd(diretorio);

            // Realiza upload do arquivo
            client.storeFile(arquivo.getName(), is);
            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	
	public void deletarArquivo(String nomeArquivo){
		FTPClient client = new FTPClient();

        try {
            client.connect(enderecoFTP);
            client.login(username, password);
            client.cwd(diretorio);

            boolean deleted = client.deleteFile(nomeArquivo);
            if (deleted) {
                System.out.printf("Arquivo %s foi deletado...", nomeArquivo);
            } else {
                System.out.println("Nenhum arquivo foi deletado...");
            }

            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
}
