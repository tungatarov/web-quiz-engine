# Web Quiz Engine

This project is a multi-user web service for creating, getting and solving quizzes using REST API.
Quizzes are stored in the service's memory, without an external storage.

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
4. Open http://localhost:8999/

Usage
------------
A simple JSON API that always returns the same quiz to be solved. The API supported operations: creating, getting all quizzes, getting quiz by id and solving it by passing an answer. Each operation is described in more detail below.

***Create a new quiz***
----
  To create a new quiz, needs to send a JSON as the request's body via POST to /api/quizzes.

* **URL**

  /api/quizzes

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
  To get a quiz by id, needs to send the GET request to /api/quizzes/{id}

* **URL**

  /api/quizzes/{id}

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
    
    
***Get all quizzes***
----
  To get all existing quizzes in the service, needs to send the GET request to /api/quizzes.

* **URL**

  /api/quizzes

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
  [
    {
      "id": 1,
      "title": "The Java Logo",
      "text": "What is depicted on the Java logo?",
      "options": ["Robot","Tea leaf","Cup of coffee","Bug"]
    },
    {
      "id": 2,
      "title": "The Ultimate Question",
      "text": "What is the answer to the Ultimate Question of Life, the Universe and Everything?",
      "options": ["Everything goes right","42","2+2=4","11011100"]
    }
  ]
  ```
    If there are no quizzes, the service returns an empty JSON array: [].
 
            
***Solve a quiz***
----
  To solve the quiz, needs to send a POST request to /api/quizzes/{id}/solve and pass the answer parameter in the content.

* **URL**

  /api/quizzes/{id}/solve

* **Method:**

  `POST`
  
* **Data Params**

   `{ "answer": [0,2] }`
   
   It is also possible to send an empty array [] since some quizzes may not have correct options.

* **Success Response:**

  If the passed answer is correct:

  * **Content:** `{"success":true,"feedback":"Congratulations, you're right!"}`
 
* **Error Responses:**

  If the answer is incorrect:

  * **Content:** `{"success":false,"feedback":"Wrong answer! Please, try again."}`
    
  If the specified quiz does not exist:
  
  * **Code:** 404 NOT FOUND <br />
    **Content:** `{"success":false,"feedback":"Wrong answer! Please, try again."}`
