package malow.gladiatus.common.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.gladiatus.common.models.responses.CharacterInfoResponse;
import malow.gladiatus.common.models.responses.LoginResponse;
import malow.gladiatus.common.models.responses.NoCharacterFoundResponse;
import malow.gladiatus.common.models.responses.SessionExpiredResponse;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(LoginRequest.class), @JsonSubTypes.Type(LoginResponse.class),
        @JsonSubTypes.Type(CharacterInfoRequest.class), @JsonSubTypes.Type(CharacterInfoResponse.class),
        @JsonSubTypes.Type(NoCharacterFoundResponse.class), @JsonSubTypes.Type(SessionExpiredResponse.class)})
public interface ModelInterface
{
    public String toNetworkString();
}
