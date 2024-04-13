package newjeans.bunnies.newjeansbunnies.domain.image.service

import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.ImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.repository.ImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
@Configuration
class ImageService(
    private val imageRepository: ImageRepository
) {
    fun createImage() {

    }

    @Transactional
    fun deleteImage(postId: String) {
        imageRepository.deleteByUuid(postId)
    }

    // 사진 리스트로 가져오기
    fun getListImage(date: String): List<ImageResponseDto> {
        val pageSize = 30
        val imageListData = imageRepository.findByCreateDateBeforeOrderByCreateDateDesc(
            date, PageRequest.of(
                0, pageSize, Sort.by(
                    Sort.Direction.DESC, "createDate"
                )
            )
        ).orElseThrow {
            throw NotExistPostIdException
        }

        // 형변환
        return imageListData.map {
            ImageResponseDto(
                createDate = it.createDate,
                uuid = it.uuid,
                imageURL = it.imageURL
            )
        }
    }
}