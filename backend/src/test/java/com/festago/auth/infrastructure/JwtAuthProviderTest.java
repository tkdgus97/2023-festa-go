package com.festago.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.domain.Member;
import com.festago.support.MemberFixture;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtAuthProviderTest {

    private static final String SECRET_KEY = "1231231231231231223131231231231231231212312312";
    JwtAuthProvider jwtAuthProvider = new JwtAuthProvider(SECRET_KEY, 360);

    @Test
    void 토큰_생성_성공() {
        // given
        Member member = MemberFixture.member()
            .id(1L)
            .build();
        JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY.getBytes())
            .build();

        // when
        String token = jwtAuthProvider.provide(member);

        // then
        assertThat(parser.isSigned(token))
            .isTrue();
    }
}