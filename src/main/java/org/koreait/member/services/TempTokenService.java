package org.koreait.member.services;

import lombok.RequiredArgsConstructor;
import org.koreait.member.constants.TokenAction;
import org.koreait.member.entities.Member;
import org.koreait.member.entities.TempToken;
import org.koreait.member.exceptions.MemberNotFoundException;
import org.koreait.member.repositories.MemberRepository;
import org.koreait.member.repositories.TempTokenRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Lazy
@Service
@RequiredArgsConstructor
public class TempTokenService {

    private final MemberRepository memberRepository;
    private final TempTokenRepository tempTokenRepository;

    /**
     * 임시 접근 토큰 발급
     *
     * @return
     */
    public TempToken issue(String email, TokenAction action) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        TempToken token = TempToken.builder()
                .token(UUID.randomUUID().toString())
                .member(member)
                .action(action)
                .expireTime(LocalDateTime.now().plusMinutes(3L))
                .build();

        tempTokenRepository.saveAndFlush(token);

        return token;
    }
}
