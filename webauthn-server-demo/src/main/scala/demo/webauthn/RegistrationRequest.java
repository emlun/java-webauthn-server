package demo.webauthn;

import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class RegistrationRequest {

    String username;
    String credentialNickname;
    String requestId;
    PublicKeyCredentialCreationOptions publicKeyCredentialCreationOptions;

}
