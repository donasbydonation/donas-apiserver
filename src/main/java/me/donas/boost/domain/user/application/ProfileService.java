package me.donas.boost.domain.user.application;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;
import static me.donas.boost.global.exception.CommonErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.user.domain.User;
import me.donas.boost.domain.user.domain.UserPrincipal;
import me.donas.boost.domain.user.dto.ProfileUpdateRequest;
import me.donas.boost.domain.user.dto.ProfileResponse;
import me.donas.boost.domain.user.exception.UserException;
import me.donas.boost.domain.user.repository.UserRepository;
import me.donas.boost.global.utils.S3UploadUtils;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final UserRepository userRepository;
	private final S3UploadUtils s3UploadUtils;

	@Transactional(readOnly = true)
	public ProfileResponse getProfile(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException(NOTFOUND));
		return ProfileResponse.of(user.getProfile());
	}

	@Transactional
	public ProfileResponse updateProfile(UserPrincipal userPrincipal, ProfileUpdateRequest request,
		MultipartFile file) {
		User user = userRepository.findByUsername(userPrincipal.getUsername()).orElseThrow(
			() -> new UserException(NOTFOUND)
		);
		if (!validateNickname(request.nickname())) {
			throw new UserException(INVALID_PARAMETER);
		}
		String imageUrl = (file != null) ? saveProfileImage(user.getUsername(), file) : user.getProfile().getImage();
		user.updateProfile(request.toEntity(imageUrl));
		return ProfileResponse.of(user.getProfile());
	}

	private boolean validateNickname(String nickname) {
		if (nickname.isBlank()) {
			return false;
		}
		if (isDuplicateNickname(nickname)) {
			throw new UserException(DUPLICATE_NICKNAME);
		}
		return true;
	}

	private boolean isDuplicateNickname(String nickname) {
		return userRepository.existsByProfileNickname(nickname);
	}

	private String saveProfileImage(String username, MultipartFile file) {
		return s3UploadUtils.upload(username, file);
	}
}
