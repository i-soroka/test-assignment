### Endpoints
- GET /tasks/uncompleted/{username}?type=
- GET /tasks/of-the-day?username=
- GET /tasks/all?username=
- PUT /tasks/wishlist?taskId=
- GET /tasks/wishlist?username=
- PUT /tasks/complete?taskId=
- GET /tasks/completed?username=
- GET /tasks/rated?username=

### User
There's a default username 'root' you can use for testing

### Database
I've been said not to use db, but I picked h2, since it works in memory and very useful for such tasks

### Improvements
1. Rated logic. I had many questions appeared in my mind to that logic, but I didn't want to spam you with these, so
I did very straighforward and simple implementation.
2. Global exception handling. There's no exception handling, and any exception leads to http 500 response.
3. Logging. There must be logging in the service and controller classes.
4. Responses. Smarter responses from the REST API.
5. Requests to a database must leverage pagination, and in some cases set transactions.
6. Exception handling. There're too many places that are error prone. Should be refactored.
7. Refactoring. There are places that could be better from readability and architectural perspective.