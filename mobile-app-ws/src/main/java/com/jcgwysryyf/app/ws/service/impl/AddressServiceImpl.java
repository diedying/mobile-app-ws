package com.jcgwysryyf.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcgwysryyf.app.ws.io.entity.AddressEntity;
import com.jcgwysryyf.app.ws.io.entity.UserEntity;
import com.jcgwysryyf.app.ws.io.repositories.AddressRepository;
import com.jcgwysryyf.app.ws.io.repositories.UserRepository;
import com.jcgwysryyf.app.ws.service.AddressService;
import com.jcgwysryyf.app.ws.shared.dto.AddressDTO;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;

	@Override
	public List<AddressDTO> getAddresses(String userId) {
		List<AddressDTO> returnValue = new ArrayList<AddressDTO>();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity==null) return returnValue;
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		
		ModelMapper modelMapper = new ModelMapper();
		for(AddressEntity addressEntity:addresses) {
			returnValue.add(modelMapper.map(addressEntity,AddressDTO.class));
		}
		return returnValue;
	}

	@Override
	public AddressDTO getAddress(String addressId) {
				
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if(addressEntity==null) return null;
		return new ModelMapper().map(addressEntity, AddressDTO.class);
	}
	

}
