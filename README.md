
How to start the application
---

1. This Application is built using java (Dropwizard framwork).
2. It requires connection to my sql server.
3. Before start the server make sure there is a active my sql connection available.
4. In the config.yml the db config is there :
```dataSourceFactory:
      driverClass: com.mysql.cj.jdbc.Driver
      user: your_username
      password: your_password
      url: jdbc:mysql://127.0.0.1:3306/SpeerDatabase
      properties:
      charSet: UTF-8  # Optional: Set the character set if needed
      hibernate.dialect: org.hibernate.dialect.MySQLDialect
      hibernate.hbm2ddl.auto: update
```
5. Create a database name and add the url of the database.
6. Also, Make sure to replace the username, password according to your local setup.
7. Run `mvn clean install` to build your application
8. Start application with `java -jar target/SpeerAssignment-1.0-SNAPSHOT.jar server config.yml`
9. To check that your application is running enter url `http://localhost:8080`

API Endpoints 
--
API endpoints : 

Login endpoint :
* POST    /api/auth/signup : User signup
* GET     /api/auth/login : User login (returns a JWT token)
* GET     /api/auth/{username} : Fetched user from username

Notes related endpoints : 
* GET     /api/notes : Fetches all notes of a user
* POST    /api/notes : Create a new note
* PUT     /api/notes : Update a note
* GET     /api/notes/search : Search for notes using keywords
* DELETE  /api/notes/{noteId} : Delete a note from noteId
* GET     /api/notes/{noteId} : Get a note from noteId
* POST    /api/notes/{noteId}/{username}/share : Share note with other user

After loging, the api returns a jwt token, for hitting Notes related api's we have tho pass the jwt token that authenticates the user.

Sample Curls 
---

User signup :
```
curl --location 'http://0.0.0.0:8080/api/auth/signup' \
--header 'Content-Type: application/json' \
--data '{
    "name" : "raju",
    "username" : "raju3",
    "password" : "raju123"
}'

Response : 

{
    "success": true,
    "message": "User signed up successfully!!",
    "code": null,
    "data": {
        "name": "raju",
        "id": 4,
        "username": "raju3",
        "password": "raju123",
        "notes": []
    }
}
```

User login :
```
curl --location --request GET 'http://0.0.0.0:8080/api/auth/login?username=raju2&password=raju123' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaXB1IiwiZXhwIjoxNzA1MjU4NTQ1fQ.kh5amiJqr4oQVLkLWAMYsuBiyEL_szWAvJ_SdUIPTdx8c0FaiPf3tvqdI7IbdJUjI7hPHMjanrPTbZ-uLj7raQ' \
--data '{
"name" : "raju",
"username" : "raju2",
"password" : "raju3"
}'

Response : 

{
    "success": true,
    "message": "Logged in successfully!!",
    "data": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWp1MiIsImV4cCI6MTcwNTI4NDQzNX0.oEMo-ml5CH4BPjeoeUQVrTtJ-MgUPSNQeJdsFf4tkjgqbNV8G2ltX63P0gjagDkQWrmvY__4pwbQwWVcBFD77A"
}
```
Create note :

```
curl --location 'http://0.0.0.0:8080/api/notes' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaXB0YXlhbjJrIiwiZXhwIjoxNzA1MjgyODAyfQ.6vDXc72ySuPLEWi3HS09LwH7S_uOrxZ-jz7ozJfdK0XduVSNDDAZta79MPD2KLw78NlhzL-2I-99a9Aaa0vxuA' \
--data '{
"title" : "note3",
"content" : "solve. That'\''s what he kept trying to convince himself. She was trying to insert her opinionprove."
}'

Response :
{
    "success": true,
    "message": null,
    "data": null
}
```

Get all notes :
```
curl --location --request GET 'http://0.0.0.0:8080/api/notes' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaXB0YXlhbjJrIiwiZXhwIjoxNzA1MjgxMjg3fQ.rzxrtO3jNXFu_vKqwFnXDGyYnN1lSOgRMNU595lexLNGsuyd8eRimNv7C0S6fG8AQtYtUQeL7-aN0b_0JZlclg' \
--data '{
"title" : "note1",
"content" : "my first note"
}'

Response : 

{
    "success": true,
    "message": null,
    "code": null,
    "data": [
        {
            "note_id": 1,
            "title": "note1",
            "content": "It really doesn't matter what she thinks as it isn't her problem to solve. That's what he kept trying to convince himself. She was trying to insert her opinion where it wasn't wanted or welcome. He aquestion now became whether he would stick to his convictions and go through with his plan knowing she wouldn't approve."
        },
        {
            "note_id": 2,
            "title": "note2",
            "content": "It really doesn't matter what she thinks as it isn't her problem to solve. That's what he kept trying to convince himself. She was trying to insert her opinionprove."
        }
    ]
}
```

Get note from id :
```
curl --location --request GET 'http://0.0.0.0:8080/api/notes/3' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaXB0YXlhbjJrIiwiZXhwIjoxNzA1MjgxMjg3fQ.rzxrtO3jNXFu_vKqwFnXDGyYnN1lSOgRMNU595lexLNGsuyd8eRimNv7C0S6fG8AQtYtUQeL7-aN0b_0JZlclg' \
--data ''

Response :

{
    "success": true,
    "message": null,
    "data": {
        "note_id": 3,
        "title": "note3",
        "content": "this is note 3."
    }
}
```

Update note
```
curl --location --request PUT 'http://0.0.0.0:8080/api/notes/' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaXB0YXlhbjJrIiwiZXhwIjoxNzA1MjgyODAyfQ.6vDXc72ySuPLEWi3HS09LwH7S_uOrxZ-jz7ozJfdK0XduVSNDDAZta79MPD2KLw78NlhzL-2I-99a9Aaa0vxuA' \
--data '{
"note_id": 3,
"title" : "note3",
"content" : "this is note 3."
}'

Response : 
{
    "success": true,
    "message": null,
    "data": null
}
```
Delete note :

```
curl --location --request DELETE 'http://0.0.0.0:8080/api/notes/3' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaXB0YXlhbjJrIiwiZXhwIjoxNzA1MjczNjQzfQ.tvlHHPwf59p63S62e9VPVtefpGn4vcLzrEWoFdaP3uvbbLajv2A_QkrChecOTsq_AFlkS6sjPfVAM1lhftUWgA' \
--data ''

Response : 

{
    "success": true,
    "message": null,
    "data": null
}

```
Search Query :
```
curl --location --request GET 'http://0.0.0.0:8080/api/notes/search?query=It%20really%20doesn%27t%20matter%20what%20she%20thinks%20' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaXB0YXlhbjJrIiwiZXhwIjoxNzA1MjgxMjg3fQ.rzxrtO3jNXFu_vKqwFnXDGyYnN1lSOgRMNU595lexLNGsuyd8eRimNv7C0S6fG8AQtYtUQeL7-aN0b_0JZlclg' \


Response : 

{
    "success": true,
    "message": null,
    "data": [
        {
            "note_id": 1,
            "title": "note1",
            "content": "It really doesn't matter what she thinks as it isn't her problem to solve. That's what he kept trying to convince himself. She was trying to insert her opinion where it wasn't wanted or welcome. He aquestion now became whether he would stick to his convictions and go through with his plan knowing she wouldn't approve."
        },
        {
            "note_id": 2,
            "title": "note2",
            "content": "It really doesn't matter what she thinks as it isn't her problem to solve. That's what he kept trying to convince himself. She was trying to insert her opinionprove."
        }
    ]
}
```
Share note :

```
curl --location --request POST 'http://0.0.0.0:8080/api/notes/1/raju2/share' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaXB0YXlhbjJrIiwiZXhwIjoxNzA1MjgyODAyfQ.6vDXc72ySuPLEWi3HS09LwH7S_uOrxZ-jz7ozJfdK0XduVSNDDAZta79MPD2KLw78NlhzL-2I-99a9Aaa0vxuA' \
--data ''

Response :
{
    "success": true,
    "message": null,
    "data": null
}
```

On unauthorised requests :
```
curl --location --request DELETE 'http://0.0.0.0:8080/api/notes/3' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaXB0YXlhbjJrIiwiZXhwIjoxNzA1MjczNjQzfQ.tvlHHPwf59p63S62e9VPVtefpGn4vcLzrEWoFdaP3uvbbLajv2A_QkrChecOTsq_AFlkS6sjPfVAM1lhftUWgA' \
--data ''

Response : 

{
    "success": false,
    "message": "User not authorised!!",
    "code": null,
    "data": null
}
```

Also I have implemented one rate limiter as well that permits 1 request per second, per user.
The permits per second is configurable from the config.yml
```
permitsPerSecond : 1.0
```
