package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.crud.CRUDDesignPattern;

public interface UserService extends CRUDDesignPattern<User, UserDto> {

}
