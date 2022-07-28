package com.madhan.ecommerce.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.madhan.ecommerce.dto.ProductDto;
import com.madhan.ecommerce.dto.ProductResponse;
import com.madhan.ecommerce.entity.Product;
import com.madhan.ecommerce.exception.ProductException;
import com.madhan.ecommerce.exception.ProductNotFoundException;
import com.madhan.ecommerce.repository.IInventoryRepository;
import com.madhan.ecommerce.util.AppUtil;


@Service
public class InventoryService {

    @Autowired
    IInventoryRepository inventoryRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @SuppressWarnings("unchecked")
	public ProductResponse getAllProducts(String page, String limit, String categories, String size, String sortBy,
                                          String orderBy) {

        Query query = new Query();
        Criteria criteria = new Criteria();
        boolean isReqParamAvail = false;
        List<ProductDto> products = null;
        int pageNo = Integer.parseInt(page);
        int pageSize = Integer.parseInt(limit);

        pageNo = pageNo <= 0 ? 0 : pageNo - 1;
        pageSize = pageSize <= 0 ? 12 : pageSize;
        List<AggregationOperation> operation = new ArrayList<>();
        Sort sort = orderBy.equalsIgnoreCase("desc") ? Sort.by(Sort.Direction.DESC, sortBy)
                : Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (categories != null) {
            List<String> category = Stream.of(categories.split(",")).collect(Collectors.toList());
            query.addCriteria(Criteria.where("categories").in(category));
            criteria = criteria.and("categories").in(category);
            isReqParamAvail = true;
        }

        if (size != null) {
            List<String> sizeList = Stream.of(size.split(",")).collect(Collectors.toList());
            query.addCriteria(Criteria.where("size").in(sizeList));
            criteria = criteria.and("size").in(sizeList);
            isReqParamAvail = true;
        }

        query.with(pageable);
        operation.add(Aggregation.match(criteria));
        operation.add(Aggregation.group().count().as("total"));

        Aggregation aggregation = newAggregation(operation);

        Map<String, Integer> result = mongoTemplate.aggregate(aggregation, Product.class, HashMap.class)
                .getUniqueMappedResult();
        if (isReqParamAvail) {
            Field field = query.fields();
            field.include("_id").include("title").include("images").include("price").include("instock");
            products = mongoTemplate.find(query, Product.class).stream().map(p-> new ProductDto(p.getId(),p.getTitle(),p.getImages(),p.getPrice(),p.getInstock())).collect(Collectors.toList());
        } else {
            Page<ProductDto> product = inventoryRepository.findAllBy(pageable);
            products = product.getContent();
        }

        return new ProductResponse(products, (pageNo + 1), pageSize,
                result != null ? result.get("total") : 0);
    }

    public Product saveproduct(Product request) {
        try {
            request.setId(UUID.randomUUID().toString());
            request.setCreatedAt(AppUtil.getDateTime());
            return inventoryRepository.save(request);
        } catch (Exception e) {
            throw new ProductException("Error Occurred During Product Save", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public Product updateProduct(Product request) {

        try {
            Product product = findProduct(request.getId());
            request.setCreatedAt(product.getCreatedAt());
            request.setUpdatedAt(AppUtil.getDateTime());
            return inventoryRepository.save(request);
        } catch (Exception e) {
            throw new ProductException("Error Occurred During Product Update", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void deleteProduct(String pid) {
        findProduct(pid);
        inventoryRepository.deleteById(pid);
    }

    public Product findProduct(String pid) {

        return inventoryRepository.findById(pid)
                .orElseThrow(() -> new ProductNotFoundException("Product Details does not exist from DB for ::" + pid));

    }

}
