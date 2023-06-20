package NestNet.NestNetWebSite.service;

import NestNet.NestNetWebSite.domain.member.Member;
import NestNet.NestNetWebSite.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;        //의존관계 주입




}
