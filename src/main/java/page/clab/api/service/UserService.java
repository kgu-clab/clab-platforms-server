package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.auth.util.AuthUtil;
import page.clab.api.exception.AssociatedAccountExistsException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.UserRepository;
import page.clab.api.type.dto.UpdateUserRequestDto;
import page.clab.api.type.dto.UserRequestDto;
import page.clab.api.type.dto.UserResponseDto;
import page.clab.api.type.entity.User;
import page.clab.api.type.etc.Role;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(UserRequestDto userRequestDto) throws PermissionDeniedException {
        checkUserAdminRole();
        if (userRepository.findById(userRequestDto.getId()).isPresent())
            throw new AssociatedAccountExistsException();
        User user = User.of(userRequestDto);
        userRepository.save(user);
    }

    public List<UserResponseDto> getUsers() throws PermissionDeniedException {
        checkUserAdminRole();
        List<User> users = userRepository.findAll();
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User user : users) {
            UserResponseDto userResponseDto = UserResponseDto.of(user);
            userResponseDtos.add(userResponseDto);
        }
        return userResponseDtos;
    }

    public UserResponseDto searchUser(String userId, String name) throws PermissionDeniedException {
        checkUserAdminRole();
        User user = null;
        if (userId != null)
            user = getUserByIdOrThrow(userId);
        else if (name != null)
            user = getUserByNameOrThrow(name);
        else
            throw new IllegalArgumentException("적어도 userId 또는 name 중 하나를 제공해야 합니다.");

        if (user == null)
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        return UserResponseDto.of(user);
    }

    public void updateUserInfoByUser(UpdateUserRequestDto updateUserRequestDto) {
        String userId = AuthUtil.getAuthenticationInfoUserId();
        User user = userRepository.findById(userId).get();
        user.setPassword(updateUserRequestDto.getPassword());
        user.setName(updateUserRequestDto.getName());
        user.setContact(updateUserRequestDto.getContact());
        user.setEmail(updateUserRequestDto.getEmail());
        user.setDepartment(updateUserRequestDto.getDepartment());
        user.setGrade(updateUserRequestDto.getGrade());
        user.setBirth(updateUserRequestDto.getBirth());
        user.setAddress(updateUserRequestDto.getAddress());
        user.setIsInSchool(updateUserRequestDto.getIsInSchool());
        user.setImageUrl(updateUserRequestDto.getImageUrl());
        userRepository.save(user);
    }

    public void deleteUserByAdmin(String userId) throws PermissionDeniedException {
        checkUserAdminRole();
        getUserByIdOrThrow(userId);
        userRepository.deleteById(userId);
    }

    public void deleteUserByUser() {
        String userId = AuthUtil.getAuthenticationInfoUserId();
        userRepository.deleteById(userId);
    }

    private void checkUserAdminRole() throws PermissionDeniedException {
        String userId = AuthUtil.getAuthenticationInfoUserId();
        User user = userRepository.findById(userId).get();
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new PermissionDeniedException("권한이 부족합니다.");
        }
    }

    public User getUserByIdOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
    }

    public User getUserByNameOrThrow(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
    }

}
