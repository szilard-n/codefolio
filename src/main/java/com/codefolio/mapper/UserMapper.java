package com.codefolio.mapper;

import com.codefolio.dto.user.UserDto;
import com.codefolio.dto.user.UserPreviewDto;
import com.codefolio.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserDto map(User user);

    List<UserDto> map(List<User> users);

    UserPreviewDto mapToPreview(User user);
}
