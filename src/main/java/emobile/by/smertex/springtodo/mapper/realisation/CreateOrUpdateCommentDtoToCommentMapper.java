package emobile.by.smertex.springtodo.mapper.realisation;

import emobile.by.smertex.springtodo.database.entity.realisation.Comment;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateCommentDto;
import emobile.by.smertex.springtodo.mapper.interfaces.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrUpdateCommentDtoToCommentMapper implements Mapper<CreateOrUpdateCommentDto, Comment> {

    @Override
    public Comment map(CreateOrUpdateCommentDto from) {
        return copy(from, new Comment());
    }

    @Override
    public Comment map(CreateOrUpdateCommentDto from, Comment to) {
        return copy(from, to);
    }

    private Comment copy(CreateOrUpdateCommentDto from, Comment to){
        to.setContent(from.content());
        return to;
    }
}
