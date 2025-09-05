package panntod.core.library.library_system.dto.commons;

import java.util.List;

public record PageResponse<T>(List<T> content, long totalElements, int totalPages, int page, int size) {}
