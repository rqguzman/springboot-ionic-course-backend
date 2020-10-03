package com.rafaelguzman.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rafaelguzman.cursomc.services.exceptions.FileException;

@Service
public class ImageService {

	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {

		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());

		if (!"png".equals(ext) && !"jpg".equals(ext)) {
			throw new FileException("Somente imagens PNG e JPG são permitidas.");
		}

		try {

			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());

			if ("png".equals(ext)) {
				img = pngToJpg(img);
			}

			return img;

		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo.");
		}
	}

	/* Converte uma imagem de .png para .jpg */
	
	public BufferedImage pngToJpg(BufferedImage img) {

		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);

		return jpgImage;
	}

	/* O método que faz o upload para o Amazon S3 recebe um InputStream 
	 * Daí precisamos fornecer esse InputStream. 
	 */
	
	public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			
			return new ByteArrayInputStream(os.toByteArray());
			
		} catch (IOException e) {
			
			throw new FileException("Erro ao ler arquivo");
		}
	}
}
