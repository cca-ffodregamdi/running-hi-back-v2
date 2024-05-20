package com.runninghi.runninghibackv2.auth.apple;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApplePublicKey(
        @JsonProperty("kty") String kty,
        @JsonProperty("kid") String kid,
        @JsonProperty("use") String use,
        @JsonProperty("alg") String alg,
        @JsonProperty("n") String n,
        @JsonProperty("e") String e
) {

    public boolean isSameAlg(String alg) {
        return this.alg.equals(alg);
    }

    public boolean isSameKid(String kid) {
        return this.kid.equals(kid);
    }
}