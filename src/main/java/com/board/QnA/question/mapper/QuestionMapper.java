package com.board.QnA.question.mapper;

import com.board.QnA.question.dto.QuestionPatchDto;
import com.board.QnA.question.dto.QuestionPostDto;
import com.board.QnA.question.dto.QuestionResponseDto;
import com.board.QnA.question.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    @Mapping(source = "memberId", target = "member.memberId")
    Question questionPostToQuestion(QuestionPostDto questionPostDto);
    @Mapping(source = "memberId", target = "member.memberId")
    Question questionPatchDtoToQuestion(QuestionPatchDto questionPatchDto);
    @Mapping(source = "member.name",target = "name")
    QuestionResponseDto questionToResponseDto(Question question);
    List<QuestionResponseDto> questionsToQuestionDtos(List<Question> questions);

}
