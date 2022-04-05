package com.codegym.controller;

import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Value("C:/Users/nguye/OneDrive/Desktop/image/")
    private String uploadPath;

    @GetMapping
    public ResponseEntity<Page<Product>> findAllProduct(@RequestParam(name = "q") Optional<String> q, @PageableDefault(value = 5) Pageable pageable) {
        Page<Product> products;
        if (!q.isPresent()) {
            products = this.productService.findAll(pageable);
        } else {
            products = this.productService.findByName(q.get(), pageable);
        }
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id) {
        Optional<Product> productOptional = this.productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @ModelAttribute ProductForm productForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        MultipartFile image = productForm.getImage();
        String fileName = image.getOriginalFilename();
        long currentTimeMillis = System.currentTimeMillis();
        fileName = currentTimeMillis + fileName;
        try {
            FileCopyUtils.copy(image.getBytes(), new File(uploadPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(productForm.getName(), productForm.getPrice(), productForm.getQuantity(),
                productForm.getDescription(), fileName, productForm.getCategory());
        return new ResponseEntity<>(this.productService.save(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> editProduct( @PathVariable Long id,@Valid @ModelAttribute ProductForm productForm,BindingResult bindingResult) {
        Optional<Product> productOptional = this.productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (bindingResult.hasFieldErrors()){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        MultipartFile multipartFile = productForm.getImage();
        String image;
        if (multipartFile.getSize() == 0) {
            image = productOptional.get().getImage();
        } else {
            String fileName = productForm.getImage().getOriginalFilename();
            long currentTime = System.currentTimeMillis();
            fileName = currentTime + fileName;
            image = fileName;
            try {
                FileCopyUtils.copy(productForm.getImage().getBytes(), new File(uploadPath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Product product = new Product(productOptional.get().getId(), productForm.getName(), productForm.getPrice(), productForm.getQuantity(),
                productForm.getDescription(), image, productForm.getCategory());
        return new ResponseEntity<>(this.productService.save(product), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        Optional<Product> productOptional = this.productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.productService.remove(id);
        return new ResponseEntity<>(productOptional.get(), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Page<Product>> findByCategory(@PathVariable Long id, @PageableDefault(value = 5) Pageable pageable) {
        Page<Product> products = this.productService.findByCategory(id, pageable);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
