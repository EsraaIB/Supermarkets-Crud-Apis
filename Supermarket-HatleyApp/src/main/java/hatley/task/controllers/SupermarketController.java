package hatley.task.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hatley.task.entities.Supermarket;
import hatley.task.services.SupermarketSerivce;

@RestController
public class SupermarketController {
	@Autowired
	private SupermarketSerivce supermarketService;

	@RequestMapping(method = RequestMethod.GET, value = "/getAllSupermarkets")
	public List<Supermarket> getAllSupermarkets() {
		List<Supermarket> allSupermarkets = new ArrayList();
		try {
			allSupermarkets = supermarketService.getAllSupermarkets();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allSupermarkets;

	}

	@RequestMapping(method = RequestMethod.POST, value = "/addNewSupermarket")
	public Supermarket addNewSupermarket(@RequestPart("supermarket") String supermarket,
			@RequestPart("image") MultipartFile file) {
		Supermarket addedObject = null;
		try {
			addedObject = supermarketService.addSupermarket(supermarket, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addedObject;

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/updateSupermarket/{id}")
	public Supermarket updateSupermarket(@RequestPart("supermarket") String supermarket,
			@RequestPart("image") MultipartFile file, @PathVariable Long id) {
		Supermarket updatedObject = null;
		try {
			updatedObject = supermarketService.updateSupermarket(supermarket, file, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatedObject;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteSupermarket/{id}")
	public void deleteSupermarket(@PathVariable Long id) {
		try {
			supermarketService.deleteSupermarket(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/activate/{id}")
	public void activateSupermarket(@PathVariable Long id) {
		try {
			supermarketService.activateList(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/deactivate/{id}")
	public void deactivateSupermarketsList(@PathVariable Long id) {
		try {
			supermarketService.deactivateList(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
