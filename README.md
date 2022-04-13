### Scanning the ClothingStore table

You're creating an online store to sell some of your old clothes and creating a table, `ClothingStore`, to help keep track
of the items for sale. 

First, use CloudFormation to populate the table.

1. Create the tables we'll be using for this activity by running the following `aws` CLI commands:
   ```none
   aws cloudformation create-stack --region us-west-2 --stack-name dynamodbscan-clothingstoretable --template-body file://cloudformation/DynamoDB_ClothingStore.yml --capabilities CAPABILITY_IAM
   ```
1. Make sure the `aws` command runs without error.
1. Log into your AWS account and verify that the table exists on DynamoDB and has sample data.
   
The `ClothingStore` table looks like the following:

* `id`: partition key — unique id for each item
* `clothingType`: the type of clothing (jeans, shoes, shirts, etc.)
* `color`: the color of the item
* `cost`: the cost of the item
* `currentStatus`: whether or not the item is for sale (either 'for sale' or 'sold')

You want to be able to search the table and retrieve all the items of a certain clothing type. Implement the method 
`scanByClothingType` in `ClothingDao` that scans the table and returns all the clothing items of a certain type.

The `Clothing` class is already written and annotated for you. 

The unit tests in `ClothingDaoTest` are set-up with a mock database so that you can test your methods offline. The 
`main()` method is set-up in `ClothingApp` so that you can connect to the real `ClothingStore` table and test your
methods that way.

HINTS:
* [How do I retrieve results based on clothing type when clothingType isn't a key?](scanClothing/hints/hint-01.md)
