package newjeans.bunnies.newjeansbunnies.domain.image.service

import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.domain.image.ImageEntity
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.ImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.error.exception.ActivatedImageException
import newjeans.bunnies.newjeansbunnies.domain.image.error.exception.DisabledImageException
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

    // 게시글에 있는 사진 불러오기
    fun getImageByPostId(postId: String): List<ImageEntity> {
        return imageRepository.findByPostId(postId).orElseThrow {
            throw NotExistPostIdException
        }
    }

    // 사진 생성
    fun createImage(imageId: String, postId: String) {
        imageRepository.save(
            ImageEntity(
                imageId = imageId, postId = postId
            )
        )
    }

    // 이미지 활성화
    fun activationImage(imageId: String, imageURL: String, createDate: String) {
        val image = getImage(imageId)
        imageRepository.save(
            ImageEntity(
                imageId = imageId, imageURL = imageURL, createDate = createDate, postId = image.postId, state = true
            )
        )
    }


    // 사진 비활성화
    @Transactional
    fun disabledImage(imageId: String): StatusResponseDto {

        // 활성화되어 있는 사진 인지 확인
        if (!checkStateImage(imageId)) throw DisabledImageException

        // 게시글 가져오기
        val image = getImage(imageId)

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

        return StatusResponseDto(200, "사진이 비활성화되었습니다.")
    }

    @Transactional
    fun deleteImage(imageId: String): StatusResponseDto {

        // 비활성화되어 있는 사진 인지 확인
        if (checkStateImage(imageId)) throw ActivatedImageException

        // 사진정보 가져오기
        val image = getImage(imageId)

        // 사진 삭제 처리
        imageRepository.delete(
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

        // TODO("활성화 되어 있는 이미지만 가져오기")

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
                createDate = it.createDate?:"", uuid = it.imageId, imageURL = it.imageURL ?: ""
            )
        }
    }

    // 사진 상태 확인
    fun checkStateImage(imageId: String): Boolean {
        return getImage(imageId).state
    }

    // 사진 아이디로 사진 정보 가져오기
    fun getImage(imageId: String): ImageEntity {
        return imageRepository.findById(imageId).orElseThrow {
            throw NotExistPostIdException
        }
    }

}