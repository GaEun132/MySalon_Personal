package com.example.shopping.dto;

import com.example.shopping.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class AddressDto {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class GetAddressListByUserResponse {
        private List<GetAddressByUserResponse> addresses;

        public static GetAddressListByUserResponse fromEntity(List<Address> addresses) {
            List<GetAddressByUserResponse> addressResponses = addresses.stream()
                    .map(GetAddressByUserResponse::fromEntity)
                    .toList();
            return GetAddressListByUserResponse.builder()
                    .addresses(addressResponses)
                    .build();
        }
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class GetAddressByUserResponse {
        private Long addressId;
        private String name;
        private String city;
        private String street;
        private String zipcode;
        private boolean isDefault;

        public static GetAddressByUserResponse fromEntity(Address address) {
            return GetAddressByUserResponse.builder()
                    .addressId(address.getId())
                    .name(address.getName())
                    .city(address.getCity())
                    .street(address.getStreet())
                    .zipcode(address.getZipcode())
                    .isDefault(address.isDefault())
                    .build();
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateAddressResponse {
        private Long addressId;
        private String name;
        private String city;
        private String street;
        private String zipcode;
        private boolean isDefault;

        public static CreateAddressResponse fromEntity(Address address) {
            return CreateAddressResponse.builder()
                    .addressId(address.getId())
                    .name(address.getName())
                    .city(address.getCity())
                    .street(address.getStreet())
                    .zipcode(address.getZipcode())
                    .isDefault(address.isDefault())
                    .build();
        }
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetDefaultAddressResponse {
        private Long addressId;
        private String name;
        private String city;
        private String street;
        private String zipcode;
        private boolean isDefault;

        public static GetDefaultAddressResponse fromEntity(Address address) {
            return GetDefaultAddressResponse.builder()
                    .addressId(address.getId())
                    .name(address.getName())
                    .city(address.getCity())
                    .street(address.getStreet())
                    .zipcode(address.getZipcode())
                    .isDefault(address.isDefault())
                    .build();
        }
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateAddressRequest {
        private String name;
        private String city;
        private String street;
        private String zipcode;
        private boolean isDefault;

        public  Address toEntity() {
            return Address.builder()
                    .name(name)
                    .city(city)
                    .street(street)
                    .zipcode(zipcode)
                    .isDefault(isDefault)
                    .build();
        }
    }
}
