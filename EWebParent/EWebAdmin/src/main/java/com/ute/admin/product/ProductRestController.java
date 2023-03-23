package com.ute.admin.product;

import java.io.IOException;
import java.util.*;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ute.admin.supplier.ISupplierService;
import com.ute.common.constants.Constants;
import com.ute.common.entity.ProductImage;
import com.ute.common.entity.Supplier;
import com.ute.common.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.ute.admin.category.ICategoryService;
import com.ute.common.entity.Category;
import com.ute.common.entity.Product;
import com.ute.common.request.ProductRequest;
import com.ute.common.response.ResponseMessage;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProductRestController {

    @Autowired
    IProductService productService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    ISupplierService supplierService;

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/product/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {

        if (productService.existsByName(request.getName().trim())) {
            return new ResponseEntity<>(new ResponseMessage("Name of product is existed"), HttpStatus.BAD_REQUEST);
        }

        Product product = new Product(request.getName().trim());
        Category category = categoryService.findById(Integer.parseInt(request.getCategoryId())).get();
        Supplier supplier = supplierService.findById(Integer.parseInt(request.getSupplierId())).get();
        product.setCreatedTime(new Date());
        product.setEnabled(true);
        product.setPrice(Float.parseFloat(request.getPrice()));
        product.setCost(Float.parseFloat(request.getCost()));
        product.setDiscountPercent(Float.parseFloat(request.getDiscount()));
        product.setCategory(category);
        product.setSupplier(supplier);
        productService.save(product);

        return new ResponseEntity<>(new ResponseMessage("Create new product successfully"), HttpStatus.CREATED);
    }

    @PutMapping("product/update-image/{id}")
    public ResponseEntity<?> updateImage(@PathVariable Integer id,
                                         @RequestParam("mainImage") MultipartFile mainImage,
                                         @RequestParam(name = "extraImage", required = false) MultipartFile[] extraImage) throws IOException {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Product not found"), HttpStatus.NOT_FOUND);
        }
        if (!mainImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImage.getOriginalFilename());

            Map uploadResult = cloudinary.uploader().upload(mainImage.getBytes(),
                    ObjectUtils.asMap("public_id", "products/" + id + "/" + HelperUtil.deleteExtensionFileImage(fileName)));
            String image = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();
            product.get().setMainImage(image);
            product.get().setPublicId(publicId);
        } else {
            if (product.get().getMainImage().isEmpty())
                product.get().setMainImage(Constants.PRODUCT_IMAGE_DEFAULT);
        }

        Set<ProductImage> extraProductImage = new HashSet<>();
        if (extraImage != null) {
            for (MultipartFile multipartFile : extraImage) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                    Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(),
                            ObjectUtils.asMap("public_id", "products/" + id + "/extra/"
                                    + HelperUtil.deleteExtensionFileImage(fileName)));

                    String extraMultipart = uploadResult.get("secure_url").toString();
                    String publicId = uploadResult.get("public_id").toString();

                    ProductImage productImage = new ProductImage();
                    productImage.setExtraImage(extraMultipart);
                    productImage.setPublicId(publicId);
                    productService.saveExtraImage(productImage);
                    extraProductImage.add(productImage);
                }
            }
        }
        product.get().setProductImages(extraProductImage);
        productService.save(product.get());
        return new ResponseEntity<>(new ResponseMessage("Updated image successfully"), HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<?> listProducts() {
        List<Product> products = productService.listAll();
        if (products.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("List of users is empty!"), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/extra")
    public ResponseEntity<?> listExtraProducts() {
        List<ProductImage> products = productService.listExtraImage();
        if (products.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("List of users is empty!"), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> changeNameProductById(@PathVariable Integer id, @RequestBody ProductRequest request) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        product.get().setName(request.getName());
        product.get().setCost(Float.parseFloat(request.getCost()));
        product.get().setPrice(Float.parseFloat(request.getPrice()));
        product.get().setDiscountPercent(Float.parseFloat(request.getDiscount()));
        Category category = categoryService.findById(Integer.parseInt(request.getCategoryId())).get();
        product.get().setCategory(category);
        productService.save(product.get());

        return new ResponseEntity<>(new ResponseMessage("Update category successfully"), HttpStatus.OK);
    }

    @PutMapping("/product/disabled/{id}")
    public ResponseEntity<?> disabledProduct(@PathVariable Integer id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        product.get().setEnabled(false);
        productService.save(product.get());

        return new ResponseEntity<>(new ResponseMessage("Disabled product successfully"), HttpStatus.OK);
    }
}
