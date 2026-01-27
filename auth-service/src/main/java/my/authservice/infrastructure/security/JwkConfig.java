package my.authservice.infrastructure.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import com.nimbusds.jose.jwk.RSAKey;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Configuration
public class JwkConfig {

    @Bean
    public JWKSource<SecurityContext> jwkSource(
            @Value("${spring.security.jwt.key-store.private-key-location}") Resource privateKeyResource,
            @Value("${spring.security.jwt.key-store.public-key-location}") Resource publicKeyResource
    ) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        InputStream is = privateKeyResource.getInputStream();
        String privatePem = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String privateBase64 = privatePem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] privateBytes = Base64.getDecoder().decode(privateBase64);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));

        is = publicKeyResource.getInputStream();
        String publicPem = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String publicBase64 = publicPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] publicBytes = Base64.getDecoder().decode(publicBase64);
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));

        // Construir JWK
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }
}
