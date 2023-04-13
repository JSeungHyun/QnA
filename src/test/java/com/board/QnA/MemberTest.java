package com.board.QnA;

import com.board.QnA.member.dto.MemberPostDto;
import com.board.QnA.member.entity.Member;
import com.google.gson.Gson;
import com.board.QnA.member.mapper.MemberMapper;
import com.board.QnA.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;
    @MockBean
    private MemberService memberService;

    @Autowired
    private MemberMapper mapper;

    @Test
    @DisplayName("멤버 등록")
    void postMemberTest() throws Exception{
        // given
        MemberPostDto postDto = new MemberPostDto("admin@gmail.com","123","관리인","010-1234-1234");
        Member member = mapper.memberPostToMember(postDto);
        member.setMemberId(1L);

        BDDMockito.given(memberService.createMember(Mockito.any(Member.class))).willReturn(member);
        String content = gson.toJson(postDto);

        // when
        ResultActions actions = mockMvc.perform(
                post("/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        //then
        actions
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", is(startsWith("/members")))
        );

    }
}
