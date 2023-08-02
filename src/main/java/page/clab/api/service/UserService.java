package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.auth.util.AuthUtil;
import page.clab.api.exception.AssociatedAccountExistsException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.UserRepository;
import page.clab.api.type.dto.UserRequestDto;
import page.clab.api.type.entity.User;
import page.clab.api.type.etc.OAuthProvider;
import page.clab.api.type.etc.Role;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(UserRequestDto userRequestDto) throws PermissionDeniedException {
        checkUserAdminRole();
        if (userRepository.findById(userRequestDto.getId()).isPresent())
            throw new AssociatedAccountExistsException();
        User user = toUser(userRequestDto);
        userRepository.save(user);
    }

    public List<User> getUsers() throws PermissionDeniedException {
        checkUserAdminRole();
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getUser(String userId) throws PermissionDeniedException {
        checkUserAdminRole();
        User user = getUserByIdOrThrow(userId);
        return user;
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
//        User user = AuthUtil.getAuthenticationInfo();
        User user = userRepository.findById("201912156").get(); // 임시 테스트용 | 로그인 구현 후 삭제할 것
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new PermissionDeniedException("권한이 부족합니다.");
        }
    }

    public User getUserByIdOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
    }

    private User toUser(UserRequestDto userRequestDto) {
        User user = User.builder()
                .id(userRequestDto.getId())
                .password(userRequestDto.getPassword())
                .name(userRequestDto.getName())
                .contact(userRequestDto.getContact())
                .email(userRequestDto.getEmail())
                .department(userRequestDto.getDepartment())
                .grade(userRequestDto.getGrade())
                .birth(userRequestDto.getBirth())
                .address(userRequestDto.getAddress())
                .isInSchool(userRequestDto.getIsInSchool())
                .role(Role.USER)
                .provider(OAuthProvider.LOCAL)
                .build();
        return user;
    }

    public String generatePassword(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("생년월일이 올바르지 않습니다.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        return birthDate.format(formatter);
    }

}
