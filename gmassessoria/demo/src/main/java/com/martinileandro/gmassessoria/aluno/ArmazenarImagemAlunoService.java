package com.martinileandro.gmassessoria.aluno;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ArmazenarImagemAlunoService {

    @Value("${app.upload.dir}")
    private String diretorioUpload;

    public String salvarImagem(MultipartFile file){
        try{
            Path diretorioPath = Paths.get(diretorioUpload);
            if(!Files.exists(diretorioPath)){
                Files.createDirectories(diretorioPath);
            }

            String nomeFile = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path filePath = diretorioPath.resolve(nomeFile);
            Files.copy(file.getInputStream(), filePath);
            return "/imagens/" + nomeFile;
        } catch (IOException e){
            throw new RuntimeException("Erro ao processar o arquivo de imagem no servidor", e);
        }
    }

    public void deletarImagemAntiga(String rotaImagemAntiga) {
        try {
            String nomeArquivo = rotaImagemAntiga.replace("/imagens/", "");
            Path caminhoCompleto = Paths.get(diretorioUpload).resolve(nomeArquivo);

            Files.deleteIfExists(caminhoCompleto);
        } catch (IOException e) {
            System.err.println("Aviso: Falha ao deletar imagem antiga: " + rotaImagemAntiga);
        }
    }
}
