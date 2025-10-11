package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.DTOS.ProductRequestDTO;
import com.merx_commerce.demo.entities.DTOS.ProductResponseDTO;
import com.merx_commerce.demo.entities.Product;
import com.merx_commerce.demo.exceptions.AlreadyExistsException;
import com.merx_commerce.demo.exceptions.NotFoundException;
import com.merx_commerce.demo.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    //user e admin
    public List<ProductResponseDTO> findAllProducts() {
        return productRepository.findAll().stream().map(ProductResponseDTO::new).toList();
    }

    //user e admin
    public ProductResponseDTO findById(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist Product with this ID"));
        return new ProductResponseDTO(product);
    }

    //admin
    public ProductResponseDTO createProduct(ProductRequestDTO product){
        if(productRepository.existsByName(product.name())) throw new AlreadyExistsException("already exists a product with this Name");
        Product newProduct = Product.builder().name(product.name()).description(product.description()).price(product.price()).quantityInStock(product.quantityInStock()).images(product.images()).category(product.category()).build();
        Product savedProduct = productRepository.save(newProduct);
        return new ProductResponseDTO(savedProduct);
    }

    //admin e user(quando comprar item, irá mudar a quantidade no estoque)
    public ProductResponseDTO updateProduct(ProductRequestDTO product, Long id){
        Product updatedProduct = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Not exist Product with this ID"));
        if(productRepository.existsByName(product.name()) && !Objects.equals(updatedProduct.getName(), product.name())) throw new AlreadyExistsException("already exists a product with this name");
        updatedProduct.setName(product.name());
        updatedProduct.setDescription(product.description());
        updatedProduct.setPrice(product.price());
        updatedProduct.setQuantityInStock(product.quantityInStock());
        updatedProduct.setImages(product.images());
        updatedProduct.setCategory(product.category());
        Product savedProduct = productRepository.save(updatedProduct);
        return new ProductResponseDTO(savedProduct);
    }

    //admin
    public void deleteProduct(Long id){
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
        }else {
            throw new NotFoundException("Not exist Product with this ID");
        }
    }
}
