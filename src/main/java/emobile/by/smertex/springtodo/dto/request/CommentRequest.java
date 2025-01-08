package emobile.by.smertex.springtodo.dto.request;

import emobile.by.smertex.springtodo.dto.filter.CommentFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(@Valid @NotNull CommentFilter commentFilter,
                             @Valid @NotNull Pageable pageable) {
}
