Here is the Transfer application.

It simply transfers money from one account to another. It works for an Intra Bank Transfer only.

There are curl scripts available to test the functionality in the /curl top level directory

The Domain Model is quite Simple

A Bank can have many users (0-n)
A User can have many accounts (0-n)
An Account contains balance information etc
A Transaction is a record of the Transfer of Money between accounts

The Architecture is as follows.
- REST API (DTO)
- Command Layer
- Service Layer
- DAO Layer

There are 4 key RESTful Resources
- Bank:
- User
- Account
- Transfer: Transfer can be invoked synchronously or asynchronously. The asynch approach utilizes BlockingQueue which is being used as a message bus.

The IntraBankTransferCommandExecutor is the key executor that performs the transfer of funds.

As the Account objects could be debited or credited by Mutiple threads at the same time I have ensured thread safety
via a Re-entrant Lock within the Account Object.

To compile: mvn clean package
To run: java -jar target/transfer-1.0.0-SNAPSHOT.jar server application.yml
To test: please run the create-bank.sh script. There are some unit tests but I they are not so sophisticated.