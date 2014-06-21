package malow.gladiatus.common.models;

import com.fasterxml.jackson.databind.ObjectMapper;

import malow.gladiatus.common.models.requests.BasicAbilitiesRequest;
import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.gladiatus.common.models.requests.RegisterRequest;
import malow.gladiatus.common.models.responses.BasicAbilitiesResponse;
import malow.gladiatus.common.models.responses.CharacterInfoResponse;
import malow.gladiatus.common.models.responses.LoginResponse;
import malow.gladiatus.common.models.responses.NoCharacterFoundResponse;
import malow.gladiatus.common.models.responses.SessionExpiredResponse;

public class ConvertStringToModel
{
    public static ModelInterface toModel(String networkString)
    {
        ObjectMapper mapper = new ObjectMapper();

        try { return mapper.readValue(networkString, LoginRequest.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, LoginResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, CharacterInfoRequest.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, CharacterInfoResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, NoCharacterFoundResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, SessionExpiredResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, RegisterRequest.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, BasicAbilitiesRequest.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, BasicAbilitiesResponse.class); } catch (Exception e) {}

        return null;
    }
}
