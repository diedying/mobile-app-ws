package com.jcgwysryyf.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jcgwysryyf.app.ws.service.AddressService;
import com.jcgwysryyf.app.ws.service.UserService;
import com.jcgwysryyf.app.ws.shared.dto.AddressDTO;
import com.jcgwysryyf.app.ws.shared.dto.UserDto;
import com.jcgwysryyf.app.ws.ui.model.request.UserDetailsRequestModel;
import com.jcgwysryyf.app.ws.ui.model.response.AddressesRest;
import com.jcgwysryyf.app.ws.ui.model.response.OperationStatusModel;
import com.jcgwysryyf.app.ws.ui.model.response.RequestOperationStatus;
import com.jcgwysryyf.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressesService;
	
	@Autowired
	AddressService addressService;
	
	@GetMapping(path="/{id}",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception{
		UserRest returnValue = new UserRest();
		
		if(userDetails.getFirstName().isEmpty()) throw new Exception("required field is empty");
		
//		UserDto userDto = new UserDto();
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
//		BeanUtils.copyProperties(userDetails,userDto);
		
		UserDto createdUser = userService.createUser(userDto);
//		BeanUtils.copyProperties(createdUser, returnValue);
		returnValue = modelMapper.map(createdUser, UserRest.class);
		
		return returnValue;
	}
	
	@PutMapping(path="/{id}",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public UserRest updateUser(@PathVariable String id,@RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails,userDto);
		
		UserDto updateUser = userService.updateUser(id,userDto);
		BeanUtils.copyProperties(updateUser, returnValue);
		
		return returnValue;
	}
	
	@DeleteMapping(path="/{id}",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value="page",defaultValue="0") int page,
			@RequestParam(value="limit",defaultValue="50") int limit)
	{
		List<UserRest> returnValue = new ArrayList<UserRest>();
		
		List<UserDto> users = userService.getUsers(page,limit);
		for(UserDto userDto:users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		return returnValue;
		
	}
	//http://localhost:8080/mobile-app-ws/users/fghj/addresses
	@GetMapping(path="/{id}/addresses",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<AddressesRest> getAddress(@PathVariable String id) {
		List<AddressesRest> returnValue = new ArrayList<AddressesRest>();
		List<AddressDTO> addressesDto = addressesService.getAddresses(id);
		
		if(addressesDto!=null&&!addressesDto.isEmpty()) {
			ModelMapper modelMapper = new ModelMapper();
			java.lang.reflect.Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			returnValue = modelMapper.map(addressesDto,listType);
			
		}
		return returnValue;
	}

	// http://localhost:8080/mobile-app-ws/users/fghj/addresses
	@GetMapping(path = "/{id}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public AddressesRest getUserAddress(@PathVariable String addressId) {
		
		AddressDTO addressDto = addressService.getAddress(addressId);

		ModelMapper modelMapper = new ModelMapper();
		
		return modelMapper.map(addressDto, AddressesRest.class);
	}

}
