package com.yubico.webauthn.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yubico.util.json.JsonStringSerializable;
import com.yubico.util.json.JsonStringSerializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;


/**
 * Relying Parties may use this to specify their preference regarding
 * attestation conveyance during credential generation.
 */
@JsonSerialize(using = JsonStringSerializer.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AttestationConveyancePreference implements JsonStringSerializable {
    /**
     * Indicates that the Relying Party is not interested in authenticator
     * attestation.
     *
     * For example, in order to potentially avoid having to obtain
     * user consent to relay identifying information to the Relying Party, or to
     * save a roundtrip to an Attestation CA.
     *
     * This is the default value.
     */
    NONE("none"),

    /**
     * Indicates that the Relying Party prefers an attestation conveyance yielding
     * verifiable attestation statements, but allows the client to decide how to
     * obtain such attestation statements. The client MAY replace the
     * authenticator-generated attestation statements with attestation statements
     * generated by an Anonymization CA, in order to protect the user’s privacy,
     * or to assist Relying Parties with attestation verification in a
     * heterogeneous ecosystem.
     *
     * Note: There is no guarantee that the Relying Party will obtain a verifiable
     * attestation statement in this case. For example, in the case that the
     * authenticator employs self attestation.
     */
    INDIRECT("indirect"),

    /**
     * Indicates that the Relying Party wants to receive the attestation statement
     * as generated by the authenticator.
     */
    DIRECT("direct");

    @NonNull
    private final String id;

    public static AttestationConveyancePreference DEFAULT = NONE;

    @Override
    public String toJsonString() {
        return id;
    }

}
