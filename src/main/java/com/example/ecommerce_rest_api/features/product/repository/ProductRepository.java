package com.example.ecommerce_rest_api.features.product.repository;

import com.example.ecommerce_rest_api.features.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findBySlug(String slug);

    boolean existsBySlug(String slug);

    @Query("select p from Product p where p.isActive = true and p.deletedAt is null ")
    Page<Product> findAllActive(Pageable pageable);

    @Query("select p from Product p where p.isFeatured=true and p.isActive=true and p.deletedAt is null ")
    Page<Product> findAllFeatured(Pageable pageable);

    @Query("select p from Product p where p.deletedAt is null and "+
            "lower(p.name) like lower(concat('%', :keyword , '%')) or "+
            "lower(p.description) like lower(concat('%', :keyword , '%')) " )
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select p from Product p join p.subCategories sc where sc.id in :categoryIds and p.deletedAt is null")
    Page<Product> findBySubCategoriesIds(@Param("categoryIds")List<Long> categoryIds, Pageable pageable);

    @Query("select p from Product p where p.brand = :brand and p.deletedAt is null")
    Page<Product> findByBrand(@Param("brand") String brand, Pageable pageable);

    @Query("select distinct p.brand from Product p where p.brand is not null and p.deletedAt is null order by p.brand")
    List<String> findAllBrands();
}
