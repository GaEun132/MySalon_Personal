package com.example.shopping.service;

import com.example.shopping.dto.AddressDto;
import com.example.shopping.dto.CartDto;
import com.example.shopping.entity.Address;
import com.example.shopping.entity.Cart;
import com.example.shopping.entity.User;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.AddressRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Transactional
    public AddressDto.CreateAddressResponse createAddress(Long userId, AddressDto.CreateAddressRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Address address = request.toEntity();
        address.assignUser(user);
        Address savedAddress = addressRepository.save(address);
        return AddressDto.CreateAddressResponse.fromEntity(savedAddress);

    }

    @Transactional(readOnly = true)
    public AddressDto.GetAddressListByUserResponse getAddressListByUser(Long userId) {

        List<Address> addresses = addressRepository.findByUserUserId(userId);
        return AddressDto.GetAddressListByUserResponse.fromEntity(addresses);
    }

    @Transactional
    public void deleteAddressById(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Transactional(readOnly = true)
    public AddressDto.GetDefaultAddressResponse getDefaultAddressByUser(Long userId) {

        Address address = addressRepository.findByUserUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

         return AddressDto.GetDefaultAddressResponse.fromEntity(address);
   }


}
