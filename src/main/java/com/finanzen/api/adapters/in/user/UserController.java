package com.finanzen.api.adapters.in.user;

import com.finanzen.api.adapters.in.user.dto.UserGetDto;
import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.in.user.FindAllUsersPort;
import com.finanzen.api.application.ports.in.user.FindUserByIdPort;
import com.finanzen.api.domain.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller acting as the inbound adapter for user management.
 * <p>
 * This adapter manages administrative user lookups. It is strictly secured
 * with method-level authorization, ensuring that only users with the 'ADMIN'
 * role can access these endpoints.
 * </p>
 */
@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final FindAllUsersPort findAllUsersPort;
    private final FindUserByIdPort findUserByIdPort;

    public UserController(FindAllUsersPort findAllUsersPort, FindUserByIdPort findUserByIdPort) {
        this.findAllUsersPort = findAllUsersPort;
        this.findUserByIdPort = findUserByIdPort;
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the user ID.
     * @return 200 OK with the user profile DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> findById(@PathVariable Long id) {
        User user = findUserByIdPort.findById(id);

        UserGetDto userGetDto = new UserGetDto(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
        return ResponseEntity.ok(userGetDto);
    }

    /**
     * Retrieves a paginated list of all system users.
     *
     * @param page page index.
     * @param size page size.
     * @return 200 OK with a paginated list of user DTOs.
     */
    @GetMapping()
    public ResponseEntity<PageResult<UserGetDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult<User> pageResult = findAllUsersPort.findAll(page, size);

        // Mapeamento de Domain para DTO de saída
        List<UserGetDto> dtos = pageResult.data().stream()
                .map(user -> new UserGetDto(
                        user.getId(),
                        user.getEmail(),
                        user.getRole()
                )).toList();

        PageResult<UserGetDto> dtoPageResult = new PageResult<>(
                dtos,
                pageResult.currentPage(),
                pageResult.totalItems(),
                pageResult.totalPages()
        );

        return ResponseEntity.ok(dtoPageResult);
    }
}