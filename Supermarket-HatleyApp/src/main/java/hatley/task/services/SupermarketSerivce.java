package hatley.task.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import hatley.task.entities.Supermarket;
import hatley.task.repositories.SupermarketRepository;

@Service
public class SupermarketSerivce {

	@Autowired
	private SupermarketRepository supermarketRepo;
	private String imagePath = "C:\\HatleyApp";
	private String deletedImagesPath = "C:\\HatleyAppDeletedImages";

	public List<Supermarket> getAllSupermarkets() throws Exception {
		List<Supermarket> supermarkets = new ArrayList<>();

		supermarketRepo.findAll().forEach(supermarkets::add);
		return supermarkets;

	}

	public Supermarket addSupermarket(String supermarket, MultipartFile file) throws Exception {

		// map string to object
		ObjectMapper objectMapper = new ObjectMapper();
		Supermarket supermarketObj = objectMapper.readValue(supermarket, Supermarket.class);
		// save Image
		saveImage(supermarketObj, file);

		supermarketObj.setIsActivated(true);
		return supermarketRepo.save(supermarketObj);

	}

	public String saveImage(Supermarket supermarketObj, MultipartFile file) throws Exception {

		if (file != null) {
			// check if imagepath file is exists .. if not create it
			File directory = new File(imagePath);
			if (!directory.exists()) {
				directory.mkdir();
			}

			InputStream initialStream = file.getInputStream();
			byte[] buffer = new byte[initialStream.available()];
			initialStream.read(buffer);
			String imageName = supermarketObj.getEnglishName() + new Date().getTime();
			File targetFile = new File(imagePath + File.separator + imageName + ".jpeg");

			OutputStream outStream = new FileOutputStream(targetFile);
			outStream.write(buffer);
			supermarketObj.setImagePath(imagePath + File.separator + imageName + ".jpeg");
			return (imagePath + File.separator + imageName + ".jpeg");
		}
		return null;
	}

	public void deleteImage(String picPath) throws Exception {

		File deletedPicturesFolder = new File(deletedImagesPath);

		if (!deletedPicturesFolder.exists()) {
			deletedPicturesFolder.mkdirs();

		}
		String[] names = picPath.split("\\\\");
		Files.move(Paths.get(picPath), Paths.get(deletedImagesPath + File.separator + names[names.length - 1]));
	}

	public Supermarket updateSupermarket(String supermarketJson, MultipartFile file, Long id) throws Exception {
		Optional<Supermarket> oldObject = supermarketRepo.findById(id);
		// map string to object
		ObjectMapper objectMapper = new ObjectMapper();
		Supermarket supermarket = objectMapper.readValue(supermarketJson, Supermarket.class);
		
		if (oldObject.isPresent()) {
			if (supermarket.getName() != null && !supermarket.getName().isEmpty()) {
				oldObject.get().setName(supermarket.getName());
			}
			if (supermarket.getEnglishName() != null && !supermarket.getEnglishName().isEmpty()) {
				oldObject.get().setEnglishName(supermarket.getEnglishName());
			}
			if (supermarket.getAddress() != null && !supermarket.getAddress().isEmpty()) {
				oldObject.get().setAddress(supermarket.getAddress());
			}
			if(!file.isEmpty())
			{
				deleteImage(oldObject.get().getImagePath());
				String result=saveImage(oldObject.get(), file);
				oldObject.get().setImagePath(result);
			}
			return supermarketRepo.save(oldObject.get());

		}
		return supermarket;
	}

	public void deleteSupermarket(Long id) throws Exception {
		Optional<Supermarket> oldObject = supermarketRepo.findById(id);
		if (oldObject.isPresent()) {
			// put image in deleted images folder
			deleteImage(oldObject.get().getImagePath());
			// delete record from db
			supermarketRepo.delete(oldObject.get());

		}

	}

	public void activateList(Long id) throws Exception {

		supermarketRepo.changeSupermarketActiveStatus(true, id);

	}

	public void deactivateList(Long id) throws Exception {
		supermarketRepo.changeSupermarketActiveStatus(false, id);

	}

}
