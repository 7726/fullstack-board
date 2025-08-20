package com.example.fullstack_board.controller;

import com.example.fullstack_board.dto.MemberRequest;
import com.example.fullstack_board.dto.MemberResponse;
import com.example.fullstack_board.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse register(@Valid @RequestBody MemberRequest request) {
        return memberService.register(request);
    }

    @Operation(summary = "회원 단건 조회")
    @GetMapping("/{id}")
    public MemberResponse getById(@PathVariable Long id) {
        return memberService.getById(id);
    }
}
