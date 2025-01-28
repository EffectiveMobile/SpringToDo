package emobile.by.smertex.springtodo.mapper.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Comment;
import emobile.by.smertex.springtodo.dto.read.ReadCommentDto;
import emobile.by.smertex.springtodo.mapper.interfaces.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentToReadCommentDtoMapper implements Mapper<Comment, ReadCommentDto> {

    private final UserToReadUserDtoMapper userToReadUserDtoMapper;

    @Override
    public ReadCommentDto map(Comment from) {
        return ReadCommentDto.builder()
                .id(from.getId())
                .content(from.getContent())
                .createdAt(from.getCreatedAt())
                .createdBy(userToReadUserDtoMapper.map(from.getCreatedBy()))
                .build();
    }
}
