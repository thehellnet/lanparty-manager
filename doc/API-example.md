# API Examples

## AppUser

### Login

Request:

```bash
curl \
    -v \
    -X POST \
    --header "Content-Type: application/json" \
    --data '{"email": "admin", "password": "admin"}' \
    http://127.0.0.1:8080/lanparty_manager/api/public/v1/appUser/login
```

Response:

```json
{
  "id" : 52,
  "token" : "18f40f23de87643c",
  "expiration" : 1579521012669
}
```

## REST

### Generic

Request:

```bash
curl \
    -v \
    -X GET \
    --header "X-Auth-Token: 18f40f23de87643c" \
    http://127.0.0.1:8080/lanparty_manager/api/public/rest
```

Response:

```
Content-Type: application/hal+json
```

```json
{
  "_links" : {
    "roles" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/roles{?page,size,sort}",
      "templated" : true
    },
    "gameMaps" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/gameMaps{?page,size,sort}",
      "templated" : true
    },
    "showcases" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/showcases{?page,size,sort}",
      "templated" : true
    },
    "tournaments" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/tournaments{?page,size,sort}",
      "templated" : true
    },
    "cfgs" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/cfgs{?page,size,sort}",
      "templated" : true
    },
    "teams" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/teams{?page,size,sort}",
      "templated" : true
    },
    "gametypes" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/gametypes{?page,size,sort}",
      "templated" : true
    },
    "appUserTokens" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/appUserTokens{?page,size,sort}",
      "templated" : true
    },
    "appUsers" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/appUsers{?page,size,sort}",
      "templated" : true
    },
    "players" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/players{?page,size,sort}",
      "templated" : true
    },
    "platforms" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/platforms{?page,size,sort}",
      "templated" : true
    },
    "games" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/games{?page,size,sort}",
      "templated" : true
    },
    "servers" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/servers{?page,size,sort}",
      "templated" : true
    },
    "matches" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/matches{?page,size,sort}",
      "templated" : true
    },
    "gameGametypes" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/gameGametypes{?page,size,sort}",
      "templated" : true
    },
    "seats" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/seats{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/profile"
    }
  }
}
```

### Profile for entity

Request:

```bash
curl \
    -v \
    -X GET \
    --header "X-Auth-Token: 18f40f23de87643c" \
    http://127.0.0.1:8080/lanparty_manager/api/public/rest/profile/appUsers
```

Response:

```
Content-Type: application/alps+json
```

```json
{
  "alps" : {
    "version" : "1.0",
    "descriptor" : [ {
      "id" : "appUser-representation",
      "href" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/profile/appUsers",
      "descriptor" : [ {
        "name" : "enabled",
        "type" : "SEMANTIC",
        "doc" : {
          "format" : "TEXT",
          "value" : "User enabled or not"
        }
      }, {
        "name" : "email",
        "type" : "SEMANTIC"
      }, {
        "name" : "name",
        "type" : "SEMANTIC"
      }, {
        "name" : "nickname",
        "type" : "SEMANTIC"
      }, {
        "name" : "lastLogin",
        "type" : "SEMANTIC"
      }, {
        "name" : "barcode",
        "type" : "SEMANTIC"
      }, {
        "name" : "roles",
        "type" : "SAFE",
        "rt" : "http://127.0.0.1:8080/lanparty_manager/api/public/rest/profile/roles#role-representation"
      } ]
    }, {
      "id" : "get-appUsers",
      "name" : "appUsers",
      "type" : "SAFE",
      "descriptor" : [ {
        "name" : "page",
        "type" : "SEMANTIC",
        "doc" : {
          "format" : "TEXT",
          "value" : "The page to return."
        }
      }, {
        "name" : "size",
        "type" : "SEMANTIC",
        "doc" : {
          "format" : "TEXT",
          "value" : "The size of the page to return."
        }
      }, {
        "name" : "sort",
        "type" : "SEMANTIC",
        "doc" : {
          "format" : "TEXT",
          "value" : "The sorting criteria to use to calculate the content of the page."
        }
      } ],
      "rt" : "#appUser-representation"
    }, {
      "id" : "create-appUsers",
      "name" : "appUsers",
      "type" : "UNSAFE",
      "descriptor" : [ ],
      "rt" : "#appUser-representation"
    }, {
      "id" : "update-appUser",
      "name" : "appUser",
      "type" : "IDEMPOTENT",
      "descriptor" : [ ],
      "rt" : "#appUser-representation"
    }, {
      "id" : "patch-appUser",
      "name" : "appUser",
      "type" : "UNSAFE",
      "descriptor" : [ ],
      "rt" : "#appUser-representation"
    }, {
      "id" : "get-appUser",
      "name" : "appUser",
      "type" : "SAFE",
      "descriptor" : [ ],
      "rt" : "#appUser-representation"
    }, {
      "id" : "delete-appUser",
      "name" : "appUser",
      "type" : "IDEMPOTENT",
      "descriptor" : [ ],
      "rt" : "#appUser-representation"
    }, {
      "name" : "findByBarcode",
      "type" : "SAFE",
      "descriptor" : [ ]
    }, {
      "name" : "findByEmail",
      "type" : "SAFE",
      "descriptor" : [ ]
    } ]
  }
}
```
