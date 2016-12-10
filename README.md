# transient-go-server
The Transient-Go REST API

## List of REST Endpoints

* Create a new user:
  * `POST /v1/user?id=<id>&pass=<password>&name=<name> HTTP/1.1`
* Retrieve information after Basic Access Authentication:
  * `GET /v1/user/<id> HTTP/1.1`
* Add new transient after Basic Access Authentication:
  * `PUT /v1/user/<id>/transient/<transient_ivorn> HTTP/1.1`
* Retrieve Leaderboard:
  *  `GET /v1/leaderboard HTTP/1.1`
