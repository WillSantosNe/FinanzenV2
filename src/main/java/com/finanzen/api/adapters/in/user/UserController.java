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

    @GetMapping()
    public ResponseEntity<PageResult<UserGetDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult<User> pageResult = findAllUsersPort.findAll(page, size);

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
