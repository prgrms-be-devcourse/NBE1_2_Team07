package com.develetter.develetter.userfilter.service;

import com.develetter.develetter.userfilter.entity.UserFilter;

public interface UserFilterService {
    UserFilter getUserFilterByUserId(Long userId);
}
