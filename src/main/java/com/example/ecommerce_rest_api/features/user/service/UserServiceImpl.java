package com.example.ecommerce_rest_api.features.user.service;

import com.example.ecommerce_rest_api.common.exception.ResourceNotFoundException;
import com.example.ecommerce_rest_api.common.response.ImgBBUploadResponse;
import com.example.ecommerce_rest_api.common.service.ImgBBService;
import com.example.ecommerce_rest_api.features.user.DTO.AddressDTO;
import com.example.ecommerce_rest_api.features.user.DTO.UserDTO;
import com.example.ecommerce_rest_api.features.user.DTO.UserUpdateRequest;
import com.example.ecommerce_rest_api.features.user.entity.Address;
import com.example.ecommerce_rest_api.features.user.entity.User;
import com.example.ecommerce_rest_api.features.user.repository.AddressRepository;
import com.example.ecommerce_rest_api.features.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ImgBBService imgBBService;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Autowired
    public UserServiceImpl(
            ModelMapper modelMapper,
            UserRepository userRepository,
            ImgBBService imgBBService,
            PasswordEncoder passwordEncoder,
            AddressRepository addressRepository
    ) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.imgBBService = imgBBService;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
    }

    @Override
    public UserDTO getUserById(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User" , "id" , userId)
        );

        return mapToDTO(user);
    }


    @Override
    public UserDTO updateUser(Long userId, UserUpdateRequest request, MultipartFile profileImage)
    {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );

        if (userRepository.existsByUsername(request.getUsername()) && !request.getUsername().equals(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())&& !request.getEmail().equals(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone_number(request.getPhone());
        user.setRole(request.getRole());
        user.setGender(request.getGender());
        user.setCreatedAt(LocalDateTime.now());

        if (profileImage != null && !profileImage.isEmpty()) {
            ImgBBUploadResponse uploadResponse = imgBBService.uploadImage(profileImage);

            user.setProfileImageUrl(uploadResponse.getData().getDisplay_url());
            user.setProfileImageDeleteUrl(uploadResponse.getData().getDelete_url());
            user.setProfileImageId(uploadResponse.getData().getId());
        }

        User updatedUser = userRepository.save(user);

        return mapToDTO(updatedUser);
    }

    @Override
    public UserDTO updateProfileImage(Long userId, MultipartFile newImage) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // upload new image
        ImgBBUploadResponse uploadResponse = imgBBService.uploadImage(newImage);

        user.setProfileImageUrl(uploadResponse.getData().getDisplay_url());
        user.setProfileImageDeleteUrl(uploadResponse.getData().getDelete_url());
        user.setProfileImageId(uploadResponse.getData().getId());

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    @Override
    public AddressDTO addAddress(Long userId, AddressDTO addressDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = mapToAddressEntity(addressDTO);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);

        return mapToAddressDTO(savedAddress);
    }

    @Override
    public List<AddressDTO> getAddressByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Address> addresses = addressRepository.findByUserId(userId);

        return addresses.stream().map(this::mapToAddressDTO).toList();
    }

    @Override
    public AddressDTO updateAddress(Long userId,Long addressId, AddressDTO addressDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userId));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address","id",addressId));

        address.setTitle(addressDTO.getTitle());
        address.setAddress_line_1(addressDTO.getAddress_line_1());
        address.setAddress_line_2(addressDTO.getAddress_line_2());
        address.setCountry(addressDTO.getCountry());
        address.setCity(addressDTO.getCity());
        address.setPostal_code(addressDTO.getPostal_code());
        address.setLandmark(addressDTO.getLandmark());
        address.setPhone_number(addressDTO.getPhone_number());

        Address savedAddress = addressRepository.save(address);

        return mapToAddressDTO(savedAddress);
    }

    @Override
    public String deleteAddress(Long userId, Long addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userId));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address","id",addressId));

        if (!address.getUser().getId().equals(user.getId())){
            return("You are not allowed to delete this address");
        }

        addressRepository.deleteById(addressId);

        return "Address Deleted Successfully";
    }

    private UserDTO mapToDTO(User user){
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setProfileImageUrl(user.getProfileImageUrl());
        return userDTO;
    }

    private Address mapToAddressEntity(AddressDTO addressDTO){
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setId(null);
        return address;
    }

    private AddressDTO mapToAddressDTO(Address address){
        return modelMapper.map(address, AddressDTO.class);
    }

}