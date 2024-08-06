package com.amazon.ata.dynamodbscanandserialization.scanClothing;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides access to the ClothingStore table.
 */
public class ClothingDao {
    private final DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Clothing objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public ClothingDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Use the scan() method to retrieve all the items from the ClothingStore table that have a given clothing type.
     * @param clothingType the given clothing type
     * @return the list of clothing retrieved from the database
     */
    public List<Clothing> scanByClothingType(final String clothingType) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":type", new AttributeValue().withS(clothingType));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("clothingType = :type")
                .withExpressionAttributeValues(valueMap);
        return mapper.scan(Clothing.class, scanExpression);
    }
}
