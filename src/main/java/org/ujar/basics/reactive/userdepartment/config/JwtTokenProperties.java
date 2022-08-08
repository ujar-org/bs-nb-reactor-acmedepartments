package org.ujar.basics.reactive.userdepartment.config;

import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConstructorBinding
@Validated
@ConfigurationProperties("ujar.jwt.token")
public record JwtTokenProperties(@NotNull String secret, @NotNull Long expiration) {

}
