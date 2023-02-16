package me.donas.boost.domain.user.application;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;
import static me.donas.boost.global.exception.CommonErrorCode.*;
import static org.springframework.http.MediaType.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.user.domain.User;
import me.donas.boost.domain.user.domain.UserPrincipal;
import me.donas.boost.domain.user.dto.ProfileUpdateRequest;
import me.donas.boost.domain.user.dto.ProfileUpdateResponse;
import me.donas.boost.domain.user.exception.UserException;
import me.donas.boost.domain.user.repository.UserRepository;
import me.donas.boost.global.utils.S3UploadUtils;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final UserRepository userRepository;
	private final S3UploadUtils s3UploadUtils;

	@Transactional
	public ProfileUpdateResponse updateProfile(UserPrincipal userPrincipal, ProfileUpdateRequest request,
		MultipartFile image) {
		User user = userRepository.findByUsername(userPrincipal.getUsername()).orElseThrow(
			() -> new UserException(NOTFOUND)
		);
		if (validateNickname(request.nickname())) {
			throw new UserException(INVALID_PARAMETER);
		}
		String imageUrl = (image != null) ? saveProfileImage(user.getUsername(), image) : user.getProfile().getImage();
		user.updateProfile(request.toEntity(imageUrl));
		return ProfileUpdateResponse.of(user.getProfile());
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

	private String saveProfileImage(String username, MultipartFile image) {
		return s3UploadUtils.upload(username, image);
	}
}
