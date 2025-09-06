package panntod.core.library.library_system.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import panntod.core.library.library_system.dto.commons.ApiResponse;
import panntod.core.library.library_system.dto.commons.LoginResponse;
import panntod.core.library.library_system.dto.commons.PageResponse;
import panntod.core.library.library_system.dto.commons.TokenResponse;
import panntod.core.library.library_system.dto.users.*;
import panntod.core.library.library_system.enums.UserRole;
import panntod.core.library.library_system.services.UserService;
import panntod.core.library.library_system.utils.SortUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * REST Controller untuk resource Product.
 * <p>
 * Endpoint:
 * - POST   /api/login         -> Login
 * - POST   /api/users         -> create produk baru
 * - GET    /api/users         -> list produk dengan pagination + sorting
 * - GET    /api/users/{id}    -> detail produk
 * - PUT    /api/users/{id}    -> update penuh
 * - PATCH  /api/users/{id}    -> update parsial
 * - DELETE /api/users/{id}    -> hapus produk
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> create(@Valid @RequestBody UserCreateDto dto) {
        var createUserDto = userService.create(dto);
        return ResponseEntity.status(201).body(
                new ApiResponse<>("success","SUCCESS_CREATE_USER", "User created successfully", Optional.of(createUserDto))
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserDto>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) List<String> sort,
            @RequestParam(required = false) String fullname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) UserRole role
            ) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, SortUtil.parseSortParam(sort));
        UserSearchRequest req = new UserSearchRequest(fullname, email, address, isActive, role);
        return ResponseEntity.ok(userService.search(req, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> get(@PathVariable UUID id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(
                new ApiResponse<>("success","SUCCESS_GET_USER_BY_ID", "User found", Optional.of(user))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserCreateDto dto
    ) {
        var updated = userService.update(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>("success","SUCCESS_UPDATE_USER", "User updated successfully", Optional.of(updated))
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> patch(
            @PathVariable UUID id,
            @RequestBody UserPatchDto dto
    ) {
        var patched = userService.patch(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>("success","SUCCESS_UPDATE_PARTIAL_USER", "User partially updated successfully", Optional.of(patched))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>("success","SUCCESS_DELETE_USER", "User deleted successfully", Optional.empty())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody UserLoginDto dto) {
        var loginResponse = userService.login(dto);
        return ResponseEntity.ok(
                new ApiResponse<>("success","SUCCESS_LOGIN", "Login successful", Optional.of(loginResponse))
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("error","FAILED_REFRESH_TOKEN_NOT_FOUND", "refreshToken is required", Optional.empty())
            );
        }

        try {
            String newAccessToken = userService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(
                    new ApiResponse<>("success","SUCCESS_GENERATE_ACCESS_TOKEN", "Access token refreshed successfully", Optional.of(
                            new TokenResponse(newAccessToken, refreshToken)
                    ))
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(401).body(
                    new ApiResponse<>("error","FAILED_GENERATE_ACCESS_TOKEN", ex.getMessage(), Optional.empty())
            );
        }
    }
}
