package com.example.fullstackboard.service;

import com.example.fullstackboard.domain.Member;
import com.example.fullstackboard.domain.Role;
import com.example.fullstackboard.dto.MemberRequest;
import com.example.fullstackboard.dto.MemberResponse;
import com.example.fullstackboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse register(MemberRequest request) {
        // 중복 이메일 방지
        if (memberRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Role role = request.role() == null ? Role.USER : request.role();

        // 비밀번호는 추후 BCrypt로 암호화 예정 (3단계 내용 재활용)
        Member saved = memberRepository.save(
                Member.builder()
                        .email(request.email())
                        .password(request.password())
                        .role(role)
                        .build()
        );
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public MemberResponse getById(Long id) {
        Member m = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return toResponse(m);
    }

    private MemberResponse toResponse(Member m) {
        return new MemberResponse(m.getId(), m.getEmail(), m.getRole(), m.getCreatedAt());
    }
}
