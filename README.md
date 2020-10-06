
**BankAccountToy API**
----
  ### Getting Started
    A Restful BankAccountToy API developed by using ;
    
    - Java 11
    - Spring Boot 2.3.3.RELEASE
    - Spring REST
    - PostgreSql
    - Lombok

  
I had an existing customer with money in the following accounts:

-Checking account
-Saving account
-Private loan account 

   
  ### Endpoints

#### 1. Account creation
 Creates an account and assign it with an IBAN an endpoint should be provided.

###### Request
```http
POST /api/accounts HTTP/1.1
Host: localhost:8080
Content-Type: application/json

**Data Params**
{
  "balance": 100,
  "customerNumber": 1,
  "type": "CHECKING"
}

AccountType [CHECKING , SAVINGS, PRIVATE_LOAN]
```


###### Response
201 - in case of success
```http
HTTP/1.1 201 Created
Content-Type: application/json


{
        "balance": 100,
        "customerNumber": 1,
        "iban": 29d7940a-6e84-4902-b2af-15ee6eb661c7,
        "id": 1,
        "type": "CHECKING",
        "withdrawable": true
}

```

406 - If balance amount is given under zero
```http
HTTP/1.1 406 NOT_ACCEPTABLE 
Content-Type: application/json
```



#### 2. Show current balance
 Gets the balance of a specified bank account.
###### Request
```http
GET /api/accounts/{iban}/balance HTTP/1.1
Host: localhost:8080
Content-Type: application/json

**URL Params**
iban : 29d7940a-6e84-4902-b2af-15ee6eb661c7
```


###### Response
200 - in case of success
```http
HTTP/1.1 200 OK
Content-Type: application/json
500

```

404 - If specified account is not found.
```http
HTTP/1.1 404 NOT_FOUND 
Content-Type: application/json
```

#### 3. Filter accounts
 Gets accounts by account type.
###### Request
```http
GET /api/accounts/list HTTP/1.1
Host: localhost:8080
Content-Type: application/json

**Data Params**
List<AccountType> 
[AccountType.CHECKING,AccountType.SAVINGS, AccountType.PRIVATE_LOAN]
```


###### Response
200 - in case of success
```http
HTTP/1.1 200 OK
Content-Type: application/json
[]

```

#### 5. Show a transacton history
 Shows the transaction history of specified account.
###### Request
```http
GET /api/accounts/{iban}/history HTTP/1.1
Host: localhost:8080
Content-Type: application/json

**URL Params**
iban : 29d7940a-6e84-4902-b2af-15ee6eb661c7
```


###### Response
200 - in case of success
```http
HTTP/1.1 200 OK
Content-Type: application/json
[]

```
404 - If specified account is not found.
```http
HTTP/1.1 404 NOT_FOUND 
Content-Type: application/json
```

#### 5. Deposit money
 Adds some amount to a specified bank account.
###### Request
```http
POST /api/accounts/deposit HTTP/1.1
Host: localhost:8080
Content-Type: application/json

**Request Params**
Deposit Account Iban : 29d7940a-6e84-4902-b2af-15ee6eb661c7
Deposit Amount : 1000
```


###### Response
200 - in case of success
```http
HTTP/1.1 200 OK
Content-Type: application/json

```
404 - If specified account is not found.
```http
HTTP/1.1 404 NOT_FOUND 
Content-Type: application/json
```
405 - If deposit amount is given under zero.
```http
HTTP/1.1 405 METHOD_NOT_ALLOWED 
Content-Type: application/json
```


#### 6. Transfer money
 Transfers some money across two bank accounts.
###### Request
```http
POST /api/accounts/transfer HTTP/1.1
Host: localhost:8080
Content-Type: application/json

**Request Params**
Withdraw Account Iban : 5c590a16-cbff-4abb-90e2-0988b344edeb
Deposit Account Iban : 29d7940a-6e84-4902-b2af-15ee6eb661c7
Deposit Amount : 1000
```


###### Response
200 - in case of success
```http
HTTP/1.1 200 OK
Content-Type: application/json

```
404 - If specified accounts are not found.
```http
HTTP/1.1 404 NOT_FOUND 
Content-Type: application/json
```
405 - If amount is given under zero.
```http
HTTP/1.1 405 METHOD_NOT_ALLOWED 
Content-Type: application/json
```

406 - If withdraw account is private loan account.
```http
HTTP/1.1 406 NOT_ACCEPTABLE
Content-Type: application/json
```

### Testing

In order to run tests of this application, run the following maven command.

```
mvn clean verify
```


### How to Build and Run
- If you already have a PostgreSQL database instance on your local, you can skip this step.
	 - Create a new PostgreSQL database instance using docker.
		
		``` docker run -d --name postgres-local -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres```

  - Once the postgres server is up & running, you can create the database and schema.

		``` psql -h localhost -U postgres -d postgres -c "CREATE DATABASE bankaccounttoy;"```
	
	
		``` psql -h localhost -U postgres -d bankaccounttoy -c "CREATE SCHEMA test;"```
	
	
- ``` mvn clean verify ``` build and run all unit tests
-  ``` mvn spring-boot:run  ```  run the application

### NOTES
 - Integration tests are not implemented.