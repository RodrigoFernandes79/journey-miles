<h1 align="center"> Journey Miles API 🛫🧳🌎 </h1>

REST API for a tourism platform, where you can search and view destinations, average travel prices, testimonials from other travelers and much more.
We created an integration with AI through CHATGPT for a feature that includes random description based on the destination. New entities and functionalities such as Flights and Flight Reservations were also created.

Project proposed by Alura in the Challenge Backend 7th Edition.

![alura_challenges](https://user-images.githubusercontent.com/83513696/235794427-a9e2c870-d132-41a4-9dd1-8122a6cf1c71.jpg)

![Badge](http://img.shields.io/static/v1?label=STATUS&message=IN%20DEVELOPMENT&color=GREEN&style=for-the-badge)
![Java](https://camo.githubusercontent.com/80db829f48ed5c5c3d48d6a3d864ff175b0e6cc6c5a12fcceaf5e14396f2bd6c/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f6c6162656c3d4a617661266d6573736167653d313726636f6c6f723d6f72616e6765267374796c653d666f722d7468652d6261646765266c6f676f3d6a617661)
![Spring_Boot](https://camo.githubusercontent.com/7abded8183b03ca2f54811488d9dbebaf3eb32c802535cee725fd073df18447a/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f6c6162656c3d537072696e67626f6f74266d6573736167653d76332e312e3126636f6c6f723d627269676874677265656e267374796c653d666f722d7468652d6261646765266c6f676f3d737072696e67626f6f74)
## ✔️Techniques and technologies
- `☕Java 17`
- `💻Intellij`
- `📚Maven`
- `♨️Spring Boot`
- `🗃️Spring Data JPA`
- `🐘PostgreSQL`
- `💡OpenAPI`
- `🔐Spring Security`
- `🪽Flyway`
- `🌶️Lombok`
- `🧋Mockito`
- `🐋Docker`

## 🚀Functionalities:
### 👤Client

- `Register`: Save Customer through a POST /clients with the information in a JSON in the body of the request.

- `Update`: Update Client through a PATCH /clients/{ID}, where ID is the Client identifier, the new Client data must be sent in the body of the request.
     * Only the Client user can update their data. 
     * The client must be authenticated.

- `Search by id`: Search Client by ID through a GET /clients/{ID}, where {ID} is the Client identifier.
     * The client must be authenticated.

- `Search all`: Search for Clients through a GET /clients.
     * Only admins must be authorized.

- `Delete`: Delete Client through DELETE /clients/{ID}, where {ID} is the Client's identifier.
    * Only the Client user can delete their data. 
    * The client must be authenticated.
      
### 👤Comment

- `Register`: Save Comment through a POST /comments with the information in a JSON in the body of the request.
      * Only the Client user can post a new comment. 
      * The client must be authenticated

- `Update`: Update Comment through a PATCH /comments/{ID}, where ID is the Comment identifier, the new Comment data must be sent in the body of the request.
     * Only the Client user can update their data. 
     * The client must be authenticated.

- `Search by id`: Search all comments by ID through a GET /comments/{ID}, where {ID} is the Client identifier.
     * The client must be authenticated.
     * Shows a paginated list containing all the customer's comments

- `Search all`: Search for Comments through a GET /comments.
     * Only admins must be authorized.
     * displays a paginated list of all comments

- `Delete`: Delete Comment through DELETE /comments/{ID}, where {ID} is the comment's identifier.
    * Only the Client user can delete their data. 
    * The client must be authenticated.
      
- `Show random comments`: Search for 3 random  comments through a GET /comments-home.
     * The client must be authenticated.
       



