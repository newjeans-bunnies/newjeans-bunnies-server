package newjeans.bunnies.newjeansbunnies.domain.image.service

import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.domain.image.ImageEntity
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.ImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.error.exception.DisabledImageException
import newjeans.bunnies.newjeansbunnies.domain.image.error.exception.NotExistImageException
import newjeans.bunnies.newjeansbunnies.domain.image.repository.ImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
@Configuration
class ImageService(
    private val imageRepository: ImageRepository
) {

    fun getImageByPostId(postId: String): List<ImageEntity> {
        return imageRepository.findByPostId(postId).orElseThrow {
            throw NotExistPostIdException
        }
    }

    fun createImage(createDate: String, imageId: String, imageURL: String, postId: String) {
        imageRepository.save(
            ImageEntity(
                imageId = imageId, createDate = createDate, imageURL = imageURL, postId = postId
            )
        )
    }

    @Transactional
    fun deleteImage(imageId: String): StatusResponseDto {

        if (!checkDeleteImage(imageId)) throw DisabledImageException

        // 게시글 가져오기
        val image = imageRepository.findById(imageId).orElseThrow {
            throw NotExistImageException
        }

        // 사진 비활성화 처리
        imageRepository.save(
            ImageEntity(
                imageId = image.imageId,
                createDate = image.createDate,
                imageURL = image.imageURL,
                postId = image.postId,
                state = false
            )
        )
        return StatusResponseDto(204, "사진이 삭제되었습니다.")
    }

    // 사진 리스트로 가져오기
    fun getListImage(date: String): List<ImageResponseDto> {

        // 가져올 이미지 개수
        val pageSize = 30

        // 이미지 가져오기
        val imageListData = imageRepository.findByCreateDateBeforeOrderByCreateDateDesc(
            date, PageRequest.of(
                0, pageSize, Sort.by(
                    Sort.Direction.DESC, "createDate"
                )
            )
        ).orElseThrow {
            throw NotExistPostIdException
        }

        // 이미지 반환
        return imageListData.map {
            ImageResponseDto(
                createDate = it.createDate, uuid = it.imageId, imageURL = it.imageURL
            )
        }
    }

    fun checkDeleteImage(imageId: String): Boolean {
        return imageRepository.findById(imageId).orElseThrow {
            throw NotExistPostIdException
        }.state
    }
}