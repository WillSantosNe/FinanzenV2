package com.finanzen.api.application.dto.common;

import java.util.List;

/**
 * Generic Data Transfer Object for paginated results.
 * <p>
 * This record acts as a framework-agnostic wrapper for paginated data.
 * By using this class instead of Spring Data's {@code Page} interface,
 * the core application and domain layers remain completely decoupled from
 * the underlying persistence infrastructure.
 * </p>
 *
 * @param <T> the type of the data elements contained in the page (e.g., Domain Entities or DTOs).
 * @param data the list of items for the current page.
 * @param currentPage the zero-based index of the current page.
 * @param totalItems the total number of elements available across all pages.
 * @param totalPages the total number of pages available.
 */
public record PageResult<T>(
        List<T> data,
        int currentPage,
        long totalItems,
        int totalPages
) {
}