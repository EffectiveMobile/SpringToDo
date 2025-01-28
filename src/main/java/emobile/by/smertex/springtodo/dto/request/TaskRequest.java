package emobile.by.smertex.springtodo.dto.request;

import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record TaskRequest(@Valid @NotNull TaskFilter taskFilter) {
}
