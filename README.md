# Web Quiz Engine

This project is a multi-user web service for creating, getting and solving quizzes using REST API, an embedded database, security, and other technologies. Concentrated on the server side ("engine") with no user interface at all. The stages of the project are described in terms of a client-server model, where the client can be a browser, a curl tool, a REST client (like a postman), or something else.

Requirements
------------
* Java 1.8 or higher
* Git

Installation
------------
1. Clone this repository `git clone git@github.com:tungatarov/web-quiz-engine.git`
3. Open the project's root directory
2. Build the application\
   for *nix `gradlew build`\
   for windows `gradlew.bat build`
3. Run the program `java -jar build/libs/*.jar`
4. Url `http://localhost:8999/`

Usage
------------
 The API supported operations: creating, getting all quizzes, getting a quiz by id and solving it by passing an answer. Each operation is described in more detail below.

 To perform any quiz operations the user must be registered and then authorized via HTTP Basic Auth by sending their email address and password for each request.

***Register a user***
----
  To register a new user, needs to send a JSON with `email` and `password` via `POST` request to `/api/register`.

* **URL**

  `/api/register`

* **Method:**

  `POST`
  
* **Data Params**

  ```
  {
    "email": "test@gmail.com",
    "password": "secret"
  }
  ```

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** None
    
* **Error Response:**

  * **Code:** 400 BAD REQUEST <br />
    **Content:** None
    
***Create a new quiz***
----
  To create a new quiz, needs to send a JSON as the request's body via `POST` to `/api/quizzes`.

* **URL**

  `/api/quizzes`

* **Method:**

  `POST`
  
* **Data Params**

   `answer` optional, since all options can be wrong.
  
   ```
  {
    "title": "Coffee drinks",
    "text": "Select only coffee drinks.",
    "options": ["Americano","Tea","Cappuccino","Sprite"],
    "answer": [0,2]
  }
  ```

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
    
  ```
  {
    "id": 1,
    "title": "Coffee drinks",
    "text": "Select only coffee drinks.",
    "options": ["Americano","Tea","Cappuccino","Sprite"],
  }
  ```

***Get a quiz by id***
----
  To get a quiz by id, needs to send the `GET` request to `/api/quizzes/{id}`.

* **URL**

  `/api/quizzes/{id}`

* **Method:**

  `GET`
  
*  **URL Params**

 **Required:**

 `id=[integer]`

* **Data Params**

None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
    
  ```
  {
    "title": "The Java Logo",
    "text": "What is depicted on the Java logo?",
    "options": ["Robot","Tea leaf","Cup of coffee","Bug"]
  }
  ```
 
* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** `{ error : "NOT FOUND" }`
    
    
***Get all quizzes with paging***
----
  To get all existing quizzes in the service, needs to send the `GET` request to `/api/quizzes`. The API returns 10 quizzes and supports the navigation through pages by passing the `page` parameter `/api/quizzes?page=1`.

* **URL**

  `/api/quizzes`

* **Method:**

  `GET`
  
*  **URL Params**

 **Required:**

None

 **Optional:**

  `page=[Integer]`

* **Data Params**

None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
    
  ```
  {
    "totalPages":1,
    "totalElements":3,
    "last":true,
    "first":true,
    "sort":{ },
    "number":0,
    "numberOfElements":3,
    "size":10,
    "empty":false,
    "pageable": { },
    "content":[
      {"id":102,"title":"Test 1","text":"Text 1","options":["a","b","c"]},
      {"id":103,"title":"Test 2","text":"Text 2","options":["a", "b", "c", "d"]},
      {"id":202,"title":"The Java Logo","text":"What is depicted on the Java logo?",
       "options":["Robot","Tea leaf","Cup of coffee","Bug"]}
    ]
  }
  ```
    If there are no quizzes, the service returns an empty JSON array: [].
    

***Get all completions of quizzes with paging***
----
  To get all completions of quizzes for a specified user in the service, needs to send the `GET` request to `/api/quizzes/completed` together with the user auth data. All the completions were sorted from the most recent to the oldest.

* **URL**

  `/api/quizzes/completed`

* **Method:**

  `GET`
  
*  **URL Params**

 **Required:**

None

* **Data Params**

None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
    
  ```
  {
    "totalPages":1,
    "totalElements":5,
    "last":true,
    "first":true,
    "empty":false,
    "content":[
      {"id":103,"completedAt":"2019-10-29T21:13:53.779542"},
      {"id":102,"completedAt":"2019-10-29T21:13:52.324993"},
      {"id":101,"completedAt":"2019-10-29T18:59:58.387267"},
      {"id":101,"completedAt":"2019-10-29T18:59:55.303268"},
      {"id":202,"completedAt":"2019-10-29T18:59:54.033801"}
    ]
  }
  ```
    If there are no quizzes, the service returns an empty JSON array: [].    
 
            
***Solve a quiz***
----
  To solve the quiz, needs to send a `POST` request to `/api/quizzes/{id}/solve` and pass the `answer` parameter in the content.

* **URL**

  `/api/quizzes/{id}/solve`

* **Method:**

  `POST`
  
*  **URL Params**

 **Required:**

 `id=[integer]`
  
* **Data Params**

   `{ "answer": [0,2] }`
   
   It is also possible to send an empty array [] since some quizzes may not have correct options.

* **Success Response:**

  If the passed answer is correct:

  * **Code:** 200 <br />
  * **Content:** `{"success":true,"feedback":"Congratulations, you're right!"}`
 
* **Error Response:**

  If the answer is incorrect:

  * **Code:** 200 <br />
  * **Content:** `{"success":false,"feedback":"Wrong answer! Please, try again."}`
    
  OR
    
  If the specified quiz does not exist:
  
  * **Code:** 404 NOT FOUND <br />
    **Content:** `{"success":false,"feedback":"Wrong answer! Please, try again."}`
    
    
***Delete a quiz***
----
  To delete the quiz, needs to send the `DELETE` request to `/api/quizzes/{id}`.

* **URL**

  `/api/quizzes/{id}`

* **Method:**

  `DELETE`

*  **URL Params**

 **Required:**

 `id=[integer]`
  
* **Data Params**

  None

* **Success Response:**

  If the operation was successful:

  * **Code:** 204 NO CONTENT <br />
    **Content:** None
    
* **Error Response:**

  If the specified quiz does not exist:
  
  * **Code:** 404 NOT FOUND <br />
    **Content:** None
    
  OR
  
  If the specified user is not the author of this quiz:

  * **Code:** 403 FORBIDDEN <br />
      **Content:** None
