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
    http://127.0.0.1:8080/lanparty_manager/api/public/v1/auth/login
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

### Schema for entity

Request:

```bash
curl \
    -v \
    -X GET \
    --header "X-Auth-Token: 243abc4628527cd8" \
    --header "Accept: application/schema+json" \
    http://127.0.0.1:8080/lanparty_manager/api/public/rest/profile/appUsers
```

Response:

```
Content-Type: application/schema+json
```

```json
{
  "title" : "App user",
  "properties" : {
    "confirmCode" : {
      "title" : "Confirm code",
      "readOnly" : false,
      "type" : "string"
    },
    "lastLoginTs" : {
      "title" : "Last login ts",
      "readOnly" : false,
      "type" : "string",
      "format" : "date-time"
    },
    "roles" : {
      "title" : "Roles",
      "readOnly" : false,
      "type" : "string",
      "format" : "uri"
    },
    "name" : {
      "title" : "Name",
      "readOnly" : false,
      "type" : "string"
    },
    "nickname" : {
      "title" : "Nickname",
      "readOnly" : false,
      "type" : "string"
    },
    "registerTs" : {
      "title" : "Register ts",
      "readOnly" : false,
      "type" : "string",
      "format" : "date-time"
    },
    "id" : {
      "title" : "Id",
      "readOnly" : true,
      "type" : "integer"
    },
    "confirmTs" : {
      "title" : "Confirm ts",
      "readOnly" : false,
      "type" : "string",
      "format" : "date-time"
    },
    "barcode" : {
      "title" : "Barcode",
      "readOnly" : false,
      "type" : "string"
    },
    "enabled" : {
      "title" : "Enabled",
      "readOnly" : false,
      "type" : "boolean"
    },
    "email" : {
      "title" : "Email",
      "readOnly" : false,
      "type" : "string"
    }
  },
  "definitions" : { },
  "type" : "object",
  "$schema" : "http://json-schema.org/draft-04/schema#"
}
```

### Custom metadata

Request:

```bash
curl \
    -v \
    http://127.0.0.1:8080/lanparty_manager/api/public/v1/config/metadata
```

Response:

```
Content-Type: application/json
```

```json
[
  {
    "name": "appUser",
    "title": "App User",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "enableds",
        "title": "Enabled",
        "type": "Basic",
        "class": "boolean"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "confirmCodes",
        "title": "Confirm Code",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": false,
        "unique": true,
        "name": "emails",
        "title": "Email",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "passwords",
        "title": "Password",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": true,
        "unique": true,
        "name": "nicknames",
        "title": "Nickname",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "registerTss",
        "title": "Register Ts",
        "type": "Basic",
        "class": "dateTime"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "confirmTss",
        "title": "Confirm Ts",
        "type": "Basic",
        "class": "dateTime"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "lastLoginTss",
        "title": "Last Login Ts",
        "type": "Basic",
        "class": "dateTime"
      },
      {
        "nullable": true,
        "unique": true,
        "name": "barcodes",
        "title": "Barcode",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "appUserTokenss",
        "title": "App User Tokens",
        "type": "OneToMany",
        "class": "appUserToken"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "roless",
        "title": "Roles",
        "type": "ManyToMany",
        "class": "role"
      }
    ]
  },
  {
    "name": "appUserToken",
    "title": "App User Token",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": true,
        "name": "tokens",
        "title": "Token",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "appUsers",
        "title": "App User",
        "type": "ManyToOne",
        "class": "appUser"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "creationDateTimes",
        "title": "Creation Date Time",
        "type": "Basic",
        "class": "dateTime"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "expirationDateTimes",
        "title": "Expiration Date Time",
        "type": "Basic",
        "class": "dateTime"
      }
    ]
  },
  {
    "name": "cfg",
    "title": "Cfg",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "players",
        "title": "Player",
        "type": "ManyToOne",
        "class": "player"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "games",
        "title": "Game",
        "type": "ManyToOne",
        "class": "game"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "cfgContents",
        "title": "Cfg Content",
        "type": "Basic",
        "class": "string"
      }
    ]
  },
  {
    "name": "game",
    "title": "Game",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": true,
        "name": "tags",
        "title": "Tag",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "platforms",
        "title": "Platform",
        "type": "ManyToOne",
        "class": "platform"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "gameGametypess",
        "title": "Game Gametypes",
        "type": "OneToMany",
        "class": "gameGametype"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "gameMapss",
        "title": "Game Maps",
        "type": "OneToMany",
        "class": "gameMap"
      }
    ]
  },
  {
    "name": "gameGametype",
    "title": "Game Gametype",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "games",
        "title": "Game",
        "type": "ManyToOne",
        "class": "game"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "gametypes",
        "title": "Gametype",
        "type": "ManyToOne",
        "class": "gametype"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "tags",
        "title": "Tag",
        "type": "Basic",
        "class": "string"
      }
    ]
  },
  {
    "name": "gameMap",
    "title": "Game Map",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "tags",
        "title": "Tag",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "games",
        "title": "Game",
        "type": "ManyToOne",
        "class": "game"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "stocks",
        "title": "Stock",
        "type": "Basic",
        "class": "boolean"
      }
    ]
  },
  {
    "name": "gametype",
    "title": "Gametype",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": true,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "gameGametypess",
        "title": "Game Gametypes",
        "type": "OneToMany",
        "class": "gameGametype"
      }
    ]
  },
  {
    "name": "match",
    "title": "Match",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "tournaments",
        "title": "Tournament",
        "type": "ManyToOne",
        "class": "tournament"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "statuss",
        "title": "Status",
        "type": "Basic",
        "class": "matchStatus"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "playOrders",
        "title": "Play Order",
        "type": "Basic",
        "class": "integer"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "servers",
        "title": "Server",
        "type": "ManyToOne",
        "class": "server"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "gameMaps",
        "title": "Game Map",
        "type": "ManyToOne",
        "class": "gameMap"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "gametypes",
        "title": "Gametype",
        "type": "ManyToOne",
        "class": "gametype"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "localTeams",
        "title": "Local Team",
        "type": "ManyToOne",
        "class": "team"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "guestTeams",
        "title": "Guest Team",
        "type": "ManyToOne",
        "class": "team"
      }
    ]
  },
  {
    "name": "pane",
    "title": "Pane",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "showcases",
        "title": "Showcase",
        "type": "ManyToOne",
        "class": "showcase"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "modes",
        "title": "Mode",
        "type": "Basic",
        "class": "paneMode"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "tournaments",
        "title": "Tournament",
        "type": "ManyToOne",
        "class": "tournament"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "matchs",
        "title": "Match",
        "type": "ManyToOne",
        "class": "match"
      }
    ]
  },
  {
    "name": "platform",
    "title": "Platform",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": true,
        "name": "tags",
        "title": "Tag",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": false,
        "unique": true,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "gamess",
        "title": "Games",
        "type": "OneToMany",
        "class": "game"
      }
    ]
  },
  {
    "name": "player",
    "title": "Player",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "nicknames",
        "title": "Nickname",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "appUsers",
        "title": "App User",
        "type": "ManyToOne",
        "class": "appUser"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "cfgss",
        "title": "Cfgs",
        "type": "OneToMany",
        "class": "cfg"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "teams",
        "title": "Team",
        "type": "ManyToOne",
        "class": "team"
      }
    ]
  },
  {
    "name": "role",
    "title": "Role",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "appUserss",
        "title": "App Users",
        "type": "ManyToMany",
        "class": "appUser"
      }
    ]
  },
  {
    "name": "seat",
    "title": "Seat",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "ipAddresss",
        "title": "Ip Address",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "tournaments",
        "title": "Tournament",
        "type": "ManyToOne",
        "class": "tournament"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "lastContacts",
        "title": "Last Contact",
        "type": "Basic",
        "class": "dateTime"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "players",
        "title": "Player",
        "type": "ManyToOne",
        "class": "player"
      }
    ]
  },
  {
    "name": "server",
    "title": "Server",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": true,
        "name": "tags",
        "title": "Tag",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "games",
        "title": "Game",
        "type": "ManyToOne",
        "class": "game"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "addresss",
        "title": "Address",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "ports",
        "title": "Port",
        "type": "Basic",
        "class": "integer"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "rconPasswords",
        "title": "Rcon Password",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "logFiles",
        "title": "Log File",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "logParsingEnableds",
        "title": "Log Parsing Enabled",
        "type": "Basic",
        "class": "boolean"
      }
    ]
  },
  {
    "name": "showcase",
    "title": "Showcase",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": true,
        "name": "tags",
        "title": "Tag",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "connecteds",
        "title": "Connected",
        "type": "Basic",
        "class": "boolean"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "lastAddresss",
        "title": "Last Address",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": true,
        "unique": false,
        "name": "lastContacts",
        "title": "Last Contact",
        "type": "Basic",
        "class": "dateTime"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "paness",
        "title": "Panes",
        "type": "OneToMany",
        "class": "pane"
      }
    ]
  },
  {
    "name": "team",
    "title": "Team",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "tournaments",
        "title": "Tournament",
        "type": "ManyToOne",
        "class": "tournament"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "playerss",
        "title": "Players",
        "type": "OneToMany",
        "class": "player"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "localMatchess",
        "title": "Local Matches",
        "type": "OneToMany",
        "class": "match"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "guestMatchess",
        "title": "Guest Matches",
        "type": "OneToMany",
        "class": "match"
      }
    ]
  },
  {
    "name": "tournament",
    "title": "Tournament",
    "fields": [
      {
        "nullable": false,
        "unique": true,
        "name": "Ids",
        "title": "Id",
        "type": "Id",
        "class": "long"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "enableds",
        "title": "Enabled",
        "type": "Basic",
        "class": "boolean"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "names",
        "title": "Name",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "games",
        "title": "Game",
        "type": "ManyToOne",
        "class": "game"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "startTss",
        "title": "Start Ts",
        "type": "Basic",
        "class": "dateTime"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "endTss",
        "title": "End Ts",
        "type": "Basic",
        "class": "dateTime"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "registrationEnableds",
        "title": "Registration Enabled",
        "type": "Basic",
        "class": "boolean"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "startRegistrationTss",
        "title": "Start Registration Ts",
        "type": "Basic",
        "class": "dateTime"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "endRegistrationTss",
        "title": "End Registration Ts",
        "type": "Basic",
        "class": "dateTime"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "statuss",
        "title": "Status",
        "type": "Basic",
        "class": "tournamentStatus"
      },
      {
        "nullable": false,
        "unique": false,
        "name": "cfgs",
        "title": "Cfg",
        "type": "Basic",
        "class": "string"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "seatss",
        "title": "Seats",
        "type": "OneToMany",
        "class": "seat"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "matchess",
        "title": "Matches",
        "type": "OneToMany",
        "class": "match"
      },
      {
        "nullable": null,
        "unique": null,
        "name": "teamss",
        "title": "Teams",
        "type": "OneToMany",
        "class": "team"
      }
    ]
  }
]
```

### Custom metadata for single entity

Request:

```bash
curl \
    -v \
    http://127.0.0.1:8080/lanparty_manager/api/public/v1/config/metadata/appUsers
```

Response:

```
Content-Type: application/json
```

```json
{ 
   "name":"appUser",
   "title":"App User",
   "fields":[ 
      { 
         "nullable":false,
         "unique":true,
         "name":"Ids",
         "title":"Id",
         "type":"Id",
         "class":"long"
      },
      { 
         "nullable":false,
         "unique":false,
         "name":"enableds",
         "title":"Enabled",
         "type":"Basic",
         "class":"boolean"
      },
      { 
         "nullable":true,
         "unique":false,
         "name":"confirmCodes",
         "title":"Confirm Code",
         "type":"Basic",
         "class":"string"
      },
      { 
         "nullable":false,
         "unique":true,
         "name":"emails",
         "title":"Email",
         "type":"Basic",
         "class":"string"
      },
      { 
         "nullable":false,
         "unique":false,
         "name":"passwords",
         "title":"Password",
         "type":"Basic",
         "class":"string"
      },
      { 
         "nullable":true,
         "unique":false,
         "name":"names",
         "title":"Name",
         "type":"Basic",
         "class":"string"
      },
      { 
         "nullable":true,
         "unique":true,
         "name":"nicknames",
         "title":"Nickname",
         "type":"Basic",
         "class":"string"
      },
      { 
         "nullable":false,
         "unique":false,
         "name":"registerTss",
         "title":"Register Ts",
         "type":"Basic",
         "class":"dateTime"
      },
      { 
         "nullable":true,
         "unique":false,
         "name":"confirmTss",
         "title":"Confirm Ts",
         "type":"Basic",
         "class":"dateTime"
      },
      { 
         "nullable":true,
         "unique":false,
         "name":"lastLoginTss",
         "title":"Last Login Ts",
         "type":"Basic",
         "class":"dateTime"
      },
      { 
         "nullable":true,
         "unique":true,
         "name":"barcodes",
         "title":"Barcode",
         "type":"Basic",
         "class":"string"
      },
      { 
         "nullable":null,
         "unique":null,
         "name":"appUserTokenss",
         "title":"App User Tokens",
         "type":"OneToMany",
         "class":"appUserToken"
      },
      { 
         "nullable":null,
         "unique":null,
         "name":"roless",
         "title":"Roles",
         "type":"ManyToMany",
         "class":"role"
      }
   ]
}
```
