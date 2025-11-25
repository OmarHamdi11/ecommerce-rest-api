package com.example.ecommerce_rest_api.features.user.repository;

import com.example.ecommerce_rest_api.features.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address> findByUserId(Long userId);

}
