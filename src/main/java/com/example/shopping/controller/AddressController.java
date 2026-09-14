package com.example.shopping.controller;

import com.example.shopping.dto.AddressDto;
import com.example.shopping.dto.CartDto;
import com.example.shopping.dto.CategoryDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressDto.CreateAddressResponse> createAddress(@CurrentUser Long userId, @RequestBody AddressDto.CreateAddressRequest request) {
        AddressDto.CreateAddressResponse response = addressService.createAddress(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/is-default")
    public ResponseEntity<AddressDto.GetDefaultAddressResponse> getDefaultAddressByUser(@CurrentUser Long userId) {
        AddressDto.GetDefaultAddressResponse response = addressService.getDefaultAddressByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<AddressDto.GetAddressListByUserResponse> getAddressListByUser(@CurrentUser Long userId) {

        AddressDto.GetAddressListByUserResponse response = addressService.getAddressListByUser(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddressById(@PathVariable Long addressId){
        addressService.deleteAddressById(addressId);
        return ResponseEntity.ok().build();
    }

}
