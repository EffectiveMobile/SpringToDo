package emobile.by.smertex.springtodo.dto.read;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
@Schema(description = "Сущность пагинации, которая содержит в себе ее элементы")
public class PageResponse <T>{

    List<T> content;

    Metadata<T> metadata;

    public static <T> PageResponse<T> of(Page<T> page){
        return new PageResponse<T>(
                page.getContent(),
                new Metadata<>(page.getNumber(), page.getSize(), page.getTotalElements())
        );
    }

    @Schema(description = "Метаданные пагинации: стараница, размер страницы, общее число элементов")
    @Value
    private static class Metadata <T>{
        int page;

        int size;

        long totalElements;
    }
}
