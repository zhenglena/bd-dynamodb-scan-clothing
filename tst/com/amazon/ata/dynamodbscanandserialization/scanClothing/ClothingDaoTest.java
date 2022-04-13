package com.amazon.ata.dynamodbscanandserialization.scanClothing;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class ClothingDaoTest {

    @Mock
    private DynamoDBMapper mapper;

    @Mock
    private PaginatedScanList<Clothing> scanResult;

    @InjectMocks
    private ClothingDao clothingDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void scanByClothingType_clothingTypeIsShoes_returnsClothesList() {
        //GIVEN
        String clothingType = "shoes";

        when(mapper.scan(eq(Clothing.class), any())).thenReturn(scanResult);

        ArgumentCaptor<DynamoDBScanExpression> scanExpressionArgumentCaptor =
            ArgumentCaptor.forClass(DynamoDBScanExpression.class);

        //WHEN
        List<Clothing> scanList = clothingDao.scanByClothingType(clothingType);

        //THEN
        verify(mapper).scan(eq(Clothing.class), scanExpressionArgumentCaptor.capture());
        DynamoDBScanExpression scanExpression = scanExpressionArgumentCaptor.getValue();
        Map<String, AttributeValue> valueMap = scanExpression.getExpressionAttributeValues();

        assertNotNull(valueMap, "Expected the expression attribute value map to be set in the scan expression");
        assertEquals(valueMap.size(), 1, "Expected clothing type to be set in attribute value map");

        Map.Entry<String, AttributeValue> entry = valueMap.entrySet().iterator().next();
        assertEquals(entry.getValue().getS(), clothingType, "Expected the attribute value map to contain " +
            "the clothingType as a String");

        String filterExpressionField = entry.getKey();
        assertTrue(filterExpressionField.startsWith(":"), "Expected the filter expression " +
            "attribute name to start with a colon.");
        String expectedFilterExpressionOption1 = String.format("clothingType = %s", filterExpressionField,
            filterExpressionField);
        String expectedFilterExpressionOption2 = String.format("%s = clothingType", filterExpressionField,
            filterExpressionField);

        assertNotNull(scanExpression.getFilterExpression(), "Expected the scan expression to contain a filter" +
            " expression");
        assertTrue(Pattern.matches(expectedFilterExpressionOption1, scanExpression.getFilterExpression()) ||
            Pattern.matches(expectedFilterExpressionOption2, scanExpression.getFilterExpression()), String.format(
                "Filter expression incorrect. Expected: %s or %s ", expectedFilterExpressionOption1,
            expectedFilterExpressionOption2));

        assertEquals(scanResult, scanList, "Expected method to return the results of the scan");
    }
}
