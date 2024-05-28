package newjeans.bunnies.newjeansbunnies.domain.image.service

import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.domain.image.Constant.MAX_POST_IMAGE
import newjeans.bunnies.newjeansbunnies.domain.image.ImageEntity
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.CreateImageRequestDto
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.ImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.error.exception.CapacityExceededImageException
import newjeans.bunnies.newjeansbunnies.domain.image.error.exception.DisabledImageException
import newjeans.bunnies.newjeansbunnies.domain.image.error.exception.NotExistImageException
import newjeans.bunnies.newjeansbunnies.domain.image.repository.ImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidRoleException
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Configuration
class ImageService(
    private val imageRepository: ImageRepository
) {

    // 게시글에 있는 사진 불러오기
    fun getImageByPostId(postId: String): List<ImageEntity> {
        return imageRepository.findByPostId(postId).orElseThrow {
            throw NotExistPostIdException
        }
    }

    // 사진 생성
    fun createImage(
        createImageRequestDto: List<CreateImageRequestDto>,
        userId: String,
        postId: String,
        authorizedUser: String?
    ): StatusResponseDto {

        if (userId != authorizedUser) throw InvalidRoleException

        // 게시글 안에 사진이 20장 보다 많다면 예외 처리
        if (createImageRequestDto.size > MAX_POST_IMAGE) {
            throw CapacityExceededImageException
        }

        createImageRequestDto.map {
            imageRepository.save(
                ImageEntity(
                    id = it.imageId,
                    postId = postId,
                    createDate = it.createDate,
                    imageKey = "image/${it.imageId}",
                    userId = userId
                )
            )
        }

        return StatusResponseDto(201, "사진이 생성됨")
    }

    // 이미지 활성화
    fun enabledImage(imageId: String, authorizedUser: String?) {

        // 사진 불러오기
        val image = imageRepository.findByIdOrNull(imageId) ?: throw NotExistImageException

        // 사진 활성화
        image.state = true

        // 활성화된 사진 저장
        imageRepository.save(image)
    }

    // 사진 비활성화
    fun disabledImage(imageId: String, authorizedUser: String?): StatusResponseDto {

        // 게시글 가져오기
        val image = imageRepository.findByIdOrNull(imageId) ?: throw NotExistPostIdException

        // 이미지 만든 사람과 이미지 삭제하려는 사람 이름이 다르면 예외 처리
        if (image.userId != authorizedUser) throw InvalidRoleException

        // 사진 비활성화
        image.state = false

        // 비활성화 된 사진 저장
        imageRepository.save(image)

        return StatusResponseDto(204, "사진이 비활성화되었습니다.")
    }

    @Transactional
    fun deleteImage(imageId: String, authorizedUser: String?): StatusResponseDto {

        // 이미지 가져오기
        val image = imageRepository.findByIdOrNull(imageId) ?: throw NotExistPostIdException

        // 이미지 만든 사람과 이미지 삭제하려는 사람 이름이 다르면 예외 처리
        if (image.userId != authorizedUser) throw InvalidRoleException

        // 게시글 활성화 확인
        if (image.state) throw DisabledImageException

        // 사진 삭제 처리
        imageRepository.delete(image)

        return StatusResponseDto(204, "사진이 삭제되었습니다.")
    }

    // 사진 리스트로 가져오기
    fun getListImage(pageSize: Int, page: Int): Slice<ImageResponseDto> {

        // 이미지 가져오기
        val pageRequest = PageRequest.of(
            page, pageSize, Sort.by(
                Sort.Direction.DESC, "createDate"
            )
        )

        val imageListData = imageRepository.findSliceBy(pageRequest).orElseThrow {
            throw NotExistImageException
        }

        // 이미지 반환
        return imageListData.map {
            ImageResponseDto(
                createDate = it.createDate, imageUrl = it.imageKey, userId = it.userId
            )
        }
    }

    fun getUserListImage(pageSize: Int, page: Int, userId: String): Slice<ImageResponseDto> {
        // 이미지 가져오기
        val pageRequest = PageRequest.of(
            page, pageSize, Sort.by(
                Sort.Direction.DESC, "createDate"
            )
        )

        val imageListData = imageRepository.findSliceBy(pageRequest, userId).orElseThrow {
            throw NotExistImageException
        }

        // 이미지 반환
        return imageListData.map {
            ImageResponseDto(
                createDate = it.createDate, imageUrl = it.imageKey, userId = it.userId
            )
        }
    }

    // 사진 아이디로 사진 정보 가져오기
    fun getImage(imageId: String): ImageEntity {
        return imageRepository.findByIdOrNull(imageId) ?: throw NotExistPostIdException
    }

}