package emobile.by.smertex.springtodo.dto.request;

import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record TaskRequest(@Valid @NotNull TaskFilter taskFilter,
                          @Valid @NotNull Pageable pageable) {
}
