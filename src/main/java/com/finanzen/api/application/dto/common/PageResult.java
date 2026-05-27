package com.finanzen.api.application.dto.common;

import java.util.List;

public record PageResult<T>(
        List<T> data,
        int currentPage,
        long totalItems,
        int totalPages
) {
}