package me.donas.boost.global.utils;

import static me.donas.boost.global.exception.CommonErrorCode.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import me.donas.boost.global.exception.FileException;

@Component
public class S3UploadUtils {
	private final String bucket;
	private final S3Operations s3Operations;

	public S3UploadUtils(@Value("${spring.cloud.aws.s3.bucket}") String bucket, S3Operations s3Operations) {
		this.bucket = bucket;
		this.s3Operations = s3Operations;
	}

	public String upload(String username, MultipartFile file) {
		if (isNotImage(file)) {
			throw new FileException(INVALID_FILE_TYPE);
		}
		String fileExtension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
		String fileName = String.format("%s/profile.%s", username, fileExtension);
		try (InputStream is = file.getInputStream()) {
			S3Resource resource = s3Operations.upload(bucket, fileName, is,
				ObjectMetadata.builder().contentType(file.getContentType()).build());

			return resource.getURL().toString();
		} catch (IOException e) {
			throw new FileException(FILE_UPLOAD_ERROR);
		}
	}

	public void delete(String s3Url) {
		if (Objects.requireNonNull(s3Url).isBlank()) {
			return;
		}
		s3Operations.deleteObject(s3Url);
	}

	private String getExtension(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	private boolean isNotImage(MultipartFile multipartFile) {
		String contentType = multipartFile.getContentType();
		if (contentType == null) {
			return false;
		}
		return !(contentType.equals(IMAGE_JPEG_VALUE) || contentType.equals(IMAGE_GIF_VALUE) ||
			contentType.equals(IMAGE_PNG_VALUE));
	}
}
