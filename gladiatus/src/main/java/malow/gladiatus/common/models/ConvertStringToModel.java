package malow.gladiatus.common.models;

import com.fasterxml.jackson.databind.ObjectMapper;

import malow.gladiatus.common.models.requests.BasicAbilitiesRequest;
import malow.gladiatus.common.models.requests.CharacterCreateRequest;
import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.gladiatus.common.models.requests.RegisterRequest;
import malow.gladiatus.common.models.responses.BasicAbilitiesResponse;
import malow.gladiatus.common.models.responses.CharacterCreationFailedResponse;
import malow.gladiatus.common.models.responses.CharacterCreationSuccessfulResponse;
import malow.gladiatus.common.models.responses.CharacterInfoResponse;
import malow.gladiatus.common.models.responses.LoginFailedResponse;
import malow.gladiatus.common.models.responses.LoginSuccessfulResponse;
import malow.gladiatus.common.models.responses.NoCharacterFoundResponse;
import malow.gladiatus.common.models.responses.RegisterFailedResponse;
import malow.gladiatus.common.models.responses.SessionExpiredResponse;
import malow.gladiatus.common.models.responses.SomethingWentHorriblyWrongResponse;
import malow.gladiatus.common.models.responses.UnexpectedRequestResponse;

public class ConvertStringToModel
{
    public static ModelInterface toModel(String networkString)
    {
        ObjectMapper mapper = new ObjectMapper();

        try { return mapper.readValue(networkString, BasicAbilitiesRequest.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, CharacterCreateRequest.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, CharacterInfoRequest.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, LoginRequest.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, RegisterRequest.class); } catch (Exception e) {}

        try { return mapper.readValue(networkString, BasicAbilitiesResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, CharacterCreationFailedResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, CharacterCreationSuccessfulResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, CharacterInfoResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, LoginFailedResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, LoginSuccessfulResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, NoCharacterFoundResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, RegisterFailedResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, SessionExpiredResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, SomethingWentHorriblyWrongResponse.class); } catch (Exception e) {}
        try { return mapper.readValue(networkString, UnexpectedRequestResponse.class); } catch (Exception e) {}

        throw new RuntimeException("Malow, you've forgot to add a new request/response to the converter...");

        //return null;
    }
}
