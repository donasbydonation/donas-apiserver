package me.donas.boost.domain.schedule.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.schedule.application.CreatorInfoService;
import me.donas.boost.domain.schedule.dto.CreatorInfoRequest;
import me.donas.boost.domain.schedule.dto.CreatorInfoResponse;
import me.donas.boost.domain.schedule.dto.CreatorInfoSimpleResponse;
import me.donas.boost.domain.schedule.dto.CreatorInfoUpdateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CreatorInfoController {

	private final CreatorInfoService creatorInfoService;

	@PostMapping("/creator-infos")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> createCreatorInfo(
		@Valid @RequestPart(value = "creatorInfo") CreatorInfoRequest request,
		@RequestPart(value = "profile", required = false) MultipartFile file
	) {
		Long id = creatorInfoService.createCreatorInfo(request, file);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/creator-infos/{id}")
			.buildAndExpand(id).toUri();

		return ResponseEntity.created(location).build();
	}

	@PutMapping("/creator-infos")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Long> updateCreatorInfo(
		@Valid @RequestPart(value = "creatorInfo") CreatorInfoUpdateRequest request,
		@RequestPart(value = "profile", required = false) MultipartFile file
	) {
		Long id = creatorInfoService.updateCreatorInfo(request, file);
		return ResponseEntity.ok(id);
	}

	@GetMapping("/creator-infos")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<CreatorInfoResponse>> readCreatorInfosFromAdmin(
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "0") int page
	) {
		return ResponseEntity.ok(creatorInfoService.readCreatorInfosFromAdmin(size, page));
	}

	@GetMapping("/creator-infos/{id}")
	public ResponseEntity<CreatorInfoResponse> readCreatorInfo(@PathVariable Long id) {
		return ResponseEntity.ok(creatorInfoService.readCreatorInfo(id));
	}

	@GetMapping("/creator-infos/all")
	public ResponseEntity<List<CreatorInfoSimpleResponse>> readCreatorsNameWithId() {
		return ResponseEntity.ok(creatorInfoService.readCreatorsNameWithId());
	}

	@DeleteMapping("/creator-infos/{id}")
	public ResponseEntity<Long> deleteCreatorInfo(@PathVariable Long id) {
		return ResponseEntity.ok(creatorInfoService.deleteCreatorInfo(id));
	}
}
