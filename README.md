# CsvBatchExample

A simple Project to explore Spring Batch CSV with MapStruct and H2 In-Memory.

Mainly I wanted to see how much work I would need to create a Spring Batch Job for reading and writing CSV.

## Cmds I did run on Intellij scratch

GET http://localhost:8080/file

###

GET http://localhost:8080/users

###

POST http://localhost:8080/users
Content-Type: application/json
[
{
"id": null,
"vorname": "Thomas",
"nachname": "Hebman",
"alter": 25 }, {
"id": null,
"vorname": "Dominik",
"nachname": "Neuer",
"alter": 48 }
]

###

GET http://localhost:8080/jobs/names

###

GET http://localhost:8080/jobs?jobName=readjob

###

POST http://localhost:8080/jobs/read/user.csv

###

POST http://localhost:8080/jobs/read/genUser.csv

###

POST http://localhost:8080/jobs/write/genUser.csv

###

POST http://localhost:8080/jobs/write/genUser2.csv