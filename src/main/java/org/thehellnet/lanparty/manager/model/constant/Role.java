package org.thehellnet.lanparty.manager.model.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

    ACTION_LOGIN("Can login for get tokens"),
    ACTION_APPUSER_CHANGE_PASSWORD("Can change password"),

    APPUSER_CREATE("Can create AppUsers"),
    APPUSER_READ("Can read AppUsers"),
    APPUSER_UPDATE("Can update AppUsers"),
    APPUSER_DELETE("Can delete AppUsers"),

    APPUSERTOKEN_CREATE("Can create AppUsersToken"),
    APPUSERTOKEN_READ("Can read AppUsersToken"),
    APPUSERTOKEN_UPDATE("Can update AppUsersToken"),
    APPUSERTOKEN_DELETE("Can delete AppUsersToken"),

    CFG_CREATE("Can create Cfg"),
    CFG_READ("Can read Cfg"),
    CFG_UPDATE("Can update Cfg"),
    CFG_DELETE("Can delete Cfg"),

    GAME_CREATE("Can create Game"),
    GAME_READ("Can read Game"),
    GAME_UPDATE("Can update Game"),
    GAME_DELETE("Can delete Game"),

    GAMEGAMETYPE_CREATE("Can create GameGametype"),
    GAMEGAMETYPE_READ("Can read GameGametype"),
    GAMEGAMETYPE_UPDATE("Can update GameGametype"),
    GAMEGAMETYPE_DELETE("Can delete GameGametype"),

    GAMEMAP_CREATE("Can create GameMap"),
    GAMEMAP_READ("Can read GameMap"),
    GAMEMAP_UPDATE("Can update GameMap"),
    GAMEMAP_DELETE("Can delete GameMap"),

    GAMETYPE_CREATE("Can create Gametype"),
    GAMETYPE_READ("Can read Gametype"),
    GAMETYPE_UPDATE("Can update Gametype"),
    GAMETYPE_DELETE("Can delete Gametype"),

    MATCH_CREATE("Can create Match"),
    MATCH_READ("Can read Match"),
    MATCH_UPDATE("Can update Match"),
    MATCH_DELETE("Can delete Match"),

    PLAYER_CREATE("Can create Player"),
    PLAYER_READ("Can read Player"),
    PLAYER_UPDATE("Can update Player"),
    PLAYER_DELETE("Can delete Player"),

    SEAT_CREATE("Can create Seat"),
    SEAT_READ("Can read Seat"),
    SEAT_UPDATE("Can update Seat"),
    SEAT_DELETE("Can delete Seat"),

    SERVER_CREATE("Can create Server"),
    SERVER_READ("Can read Server"),
    SERVER_UPDATE("Can update Server"),
    SERVER_DELETE("Can delete Server"),

    TEAM_CREATE("Can create Team"),
    TEAM_READ("Can read Team"),
    TEAM_UPDATE("Can update Team"),
    TEAM_DELETE("Can delete Team"),

    TOURNAMENT_CREATE("Can create Tournament"),
    TOURNAMENT_READ("Can read Tournament"),
    TOURNAMENT_UPDATE("Can update Tournament"),
    TOURNAMENT_DELETE("Can delete Tournament");

    private String description;
    private Role[] impliedRoles;

    Role(String description, Role... impliedRoles) {
        this.description = description;
        this.impliedRoles = impliedRoles;
    }

    @JsonValue
    public String getName() {
        return name();
    }

    public Role[] getImpliedRoles() {
        return impliedRoles;
    }

    @Override
    public String toString() {
        return description;
    }
}
