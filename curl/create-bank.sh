# Create Bank
curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/bank -d '{"name":"Revolut"}'
curl -X GET http://localhost:8080/bank/Revolut
# Create User
curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/user -d '{"name":"Richie", "bankName":"Revolut"}'
curl -X GET http://localhost:8080/user/Richie
# Create Savings Account
curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/account -d '{"owner":"Richie", "bankName":"Revolut","sortCode":"30-30-30", "accountNumber":12345, "balance": 300, "currency":"GBP", "bankAccountName":"Savings"}'
# Create Current Account
curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/account -d '{"owner":"Richie", "bankName":"Revolut","sortCode":"40-40-40", "accountNumber":12346, "balance": 300, "currency":"GBP", "bankAccountName":"Current"}'
curl -X GET http://localhost:8080/account/12345
curl -X GET http://localhost:8080/account/12346
# Transfer Cash from one to the other, returns a UUID for the TX
curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/transfer/async -d '{"from":12345, "to":12346, "amount":200, "type":"Transfer"}'
curl -X GET http://localhost:8080/transfer/{UUID}
curl -X GET http://localhost:8080/transfer/findAll
# Transfer Cash from one to the other again, this time not enough funds are available
curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/transfer/async -d '{"from":12345, "to":12346, "amount":200, "type":"Transfer"}'
curl -X GET http://localhost:8080/transfer/{UUID}
curl -X GET http://localhost:8080/transfer/findAll
