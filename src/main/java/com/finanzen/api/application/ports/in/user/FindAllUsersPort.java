package com.finanzen.api.application.ports.in.user;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.user.User;

public interface FindAllUsersPort {
    PageResult<User> findAll(int page, int size);
}
