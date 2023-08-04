package greeny.backend.domain.review.service;

import greeny.backend.config.aws.S3Service;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.repository.ProductRepository;
import greeny.backend.domain.review.dto.GetReviewListResponseDto;
import greeny.backend.domain.review.dto.WriteReviewRequestDto;
import greeny.backend.domain.review.dto.GetReviewInfoResponseDto;
import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.review.entity.StoreReview;
import greeny.backend.domain.review.repository.ProductReviewRepository;
import greeny.backend.domain.review.repository.StoreReviewRepository;
import greeny.backend.domain.reviewimage.entity.ProductReviewImage;
import greeny.backend.domain.reviewimage.entity.StoreReviewImage;
import greeny.backend.domain.reviewimage.repository.ProductReviewImageRepository;
import greeny.backend.domain.reviewimage.repository.StoreReviewImageRepository;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.repository.StoreRepository;
import greeny.backend.exception.situation.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

import static greeny.backend.domain.review.dto.GetReviewListResponseDto.toProductReviewDTO;
import static greeny.backend.domain.review.dto.GetReviewListResponseDto.toStoreReviewDTO;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final StoreReviewRepository storeReviewRepository;
    private final ProductReviewImageRepository productReviewImageRepository;
    private final StoreReviewImageRepository storeReviewImageRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final S3Service s3Service;

    /* 리뷰 작성하기 */
    @Transactional
    public void writeStoreReview(Long id, WriteReviewRequestDto writeReviewRequestDto, List<MultipartFile> multipartFiles, Member member) {
        //storeReview 저장
        Store store = storeRepository.findById(id).orElseThrow(StoreNotFound::new);
        StoreReview storeReview =storeReviewRepository.save(writeReviewRequestDto.toStoreReviewEntity(member, store));
        if(multipartFiles!=null){
            uploadFiles(multipartFiles,storeReview);
        }
    }
    @Transactional
    public void writeProductReview(Long id, WriteReviewRequestDto writeReviewRequestDto,List<MultipartFile> multipartFiles,Member member) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFound::new);
        ProductReview productReview = productReviewRepository.save(writeReviewRequestDto.toProductReviewEntity(member,product));
        if(multipartFiles!=null){
            uploadFiles(multipartFiles,productReview);
        }
    }


    /* 내가 쓴 리뷰 불러오기 */
    @Transactional(readOnly = true)
    public Page<GetReviewListResponseDto> getMemberReviewList(String type, Pageable pageable, Member member) {
        if(type.equals("s")) {
            Page<StoreReview> pages = storeReviewRepository.findAllByReviewer(pageable,member);
            return pages.map(review -> toStoreReviewDTO(review));
        } else if(type.equals("p")) {
            Page<ProductReview> pages = productReviewRepository.findAllByReviewer(pageable,member);
            return pages.map(review -> toProductReviewDTO(review));
        } else throw new TypeDoesntExistsException();
    }


    /* 검색 OR getAllSimpleReviewInfos */
    @Transactional(readOnly=true)
    public Page<GetReviewListResponseDto> searchSimpleReviewInfos(String keyword, String type, Pageable pageable) {
        if(!StringUtils.hasText(keyword)) { return getAllSimpleReviewInfos(type, pageable);    }
        if(type.equals("s")) {
            Page<StoreReview> pages =storeReviewRepository.findAllByContentContainingIgnoreCase(keyword,pageable);
            return pages.map(review -> toStoreReviewDTO(review));
        } else if(type.equals("p")) {
            Page<ProductReview> pages =productReviewRepository.findAllByContentContainingIgnoreCase(keyword,pageable);
            return pages.map(review -> toProductReviewDTO(review));
        } else throw new TypeDoesntExistsException();
    }

    /* 스토어 OR 제품 전체 review list 불러오기 */
    @Transactional(readOnly = true)
    private Page<GetReviewListResponseDto> getAllSimpleReviewInfos(String type, Pageable pageable) {
        if(type.equals("s")) {
            Page<StoreReview> pages = storeReviewRepository.findAll(pageable);
            return pages.map(review -> toStoreReviewDTO(review));
        } else if(type.equals("p")) {
            Page<ProductReview> pages = productReviewRepository.findAll(pageable);
            return pages.map(review -> toProductReviewDTO(review));
        } else throw new TypeDoesntExistsException();
    }

    /* 스토어&제품 ID로 review list 불러오기 */
    @Transactional(readOnly = true)
    public Page<GetReviewListResponseDto> getSimpleReviewInfos(String type,Long id,Pageable pageable) {
        if(type.equals("s")) {
            Store store = storeRepository.findById(id).orElseThrow(StoreNotFound::new);
            Page<StoreReview> pages = storeReviewRepository.findStoreReviewsByStore(pageable, store);
            return pages.map(review -> toStoreReviewDTO(review));
        } else if(type.equals("p")) {
            Product product = productRepository.findById(id).orElseThrow(ProductNotFound::new);
            Page<ProductReview> pages = productReviewRepository.findProductReviewsByProduct(pageable, product);
            return pages.map(review -> toProductReviewDTO(review));
        } else throw new TypeDoesntExistsException();
    }


    /* 상세리뷰 불러오기 : 인증X */
    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getStoreReviewInfo(Long id) {
        StoreReview storeReview = storeReviewRepository.findById(id).orElseThrow(ReviewNotFound::new);
        //이메일찾기 - 현재멤버 비교 -> dto에 일치여부 포함
        List<String> urls = getStoreReviewImgUrls(storeReview);
        return buildReviewInfoResponseDto(storeReview.getReviewer().getEmail(),storeReview.getCreatedAt(),storeReview.getStar(),
                storeReview.getContent(),urls,false);
    }
    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getProductReviewInfo(Long id) {
        ProductReview productReview = productReviewRepository.findById(id).orElseThrow(ReviewNotFound::new);

        List<String> urls = getProductReviewImgUrls(productReview);
        return buildReviewInfoResponseDto(productReview.getReviewer().getEmail(),productReview.getCreatedAt(),productReview.getStar(),
                productReview.getContent(),urls,false);
    }

    /* 상세리뷰 불러오기 : 인증O */
    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getStoreReviewInfoWithAuth(Long id,Member member) {
        StoreReview storeReview = storeReviewRepository.findById(id).orElseThrow(ReviewNotFound::new);
        //이메일찾기 - 현재멤버 비교 -> dto에 일치여부 포함
        boolean isWriter = storeReview.getReviewer().getEmail().equals(member.getEmail());
        List<String> urls = getStoreReviewImgUrls(storeReview);
        return buildReviewInfoResponseDto
                (storeReview.getReviewer().getEmail(),storeReview.getCreatedAt(),storeReview.getStar(),
                        storeReview.getContent(),urls,isWriter);
    }
    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getProductReviewInfoWithAuth(Long id,Member member) {
        ProductReview productReview = productReviewRepository.findById(id).orElseThrow(ReviewNotFound::new);
        boolean isWriter = productReview.getReviewer().getEmail().equals(member.getEmail());

        List<String> urls = getProductReviewImgUrls(productReview);
        return buildReviewInfoResponseDto
                (productReview.getReviewer().getEmail(),productReview.getCreatedAt(),productReview.getStar(),
                        productReview.getContent(),urls,isWriter);
    }



    /* 리뷰 삭제 */
    @Transactional
    public void deleteStoreReview(Long reviewId,Member currentMember) {  // TODO 다른 사용자의 리뷰 삭제를 방지하기 위해 현재 사용자와 리뷰 작성자 비교 : 완료
        // 리뷰 존재 & 작성자 본인여부 검증
        StoreReview storeReview = storeReviewRepository.findById(reviewId).orElseThrow(ReviewNotFound::new);
        if(!storeReview.getReviewer().getEmail().equals(currentMember.getEmail())) {throw new MemberNotEqualsException();}

        //image 삭제 : s3에서 삭제 -> StoreReviewImage 삭제
        List<StoreReviewImage> reviewImages=storeReviewImageRepository.findByStoreReviewId(reviewId);
        if(reviewImages!=null) {
            for(StoreReviewImage img:reviewImages) {s3Service.deleteFile(img.getImageUrl());}
            storeReviewImageRepository.deleteAll(reviewImages);
        }
        //review 삭제 : StoreReview 삭제
        storeReviewRepository.deleteById(reviewId);
    }
    @Transactional
    public void deleteProductReview(Long reviewId,Member currentMember) {
        // 리뷰 존재 & 작성자 본인여부 검증
        ProductReview productReview = productReviewRepository.findById(reviewId).orElseThrow(ReviewNotFound::new);
        if(!productReview.getReviewer().getId().equals(currentMember.getId())) {throw new MemberNotEqualsException();}

        //image 삭제 : s3에서 삭제 -> ProductReviewImage 삭제
        List<ProductReviewImage> reviewImages=productReviewImageRepository.findByProductReviewId(reviewId);
        if(reviewImages!=null) {
            for(ProductReviewImage img:reviewImages) {s3Service.deleteFile(img.getImageUrl());}
            productReviewImageRepository.deleteAll(reviewImages);
        }
        //review 삭제 : ProductReview 삭제
        productReviewRepository.deleteById(reviewId);
    }


    public GetReviewInfoResponseDto buildReviewInfoResponseDto(String email, String createdAt, Integer star, String content, List<String> urls,boolean isWriter) {
        return GetReviewInfoResponseDto.builder()
                .writerEmail(email)
                .createdAt(createdAt)
                .star(star)
                .content(content)
                .fileUrls(urls)
                .isWriter(isWriter)
                .build();
    }


    @Transactional
    public void uploadFiles(List<MultipartFile> multipartFiles,StoreReview storeReview) {
        for(MultipartFile file:multipartFiles) {
            StoreReviewImage storeReviewImage = new StoreReviewImage().
                    getEntity(storeReview,s3Service.uploadFile(file));
            storeReview.getStoreReviewImages().add(storeReviewImage);
        }
    }
    @Transactional
    public void uploadFiles(List<MultipartFile> multipartFiles,ProductReview productReview) {
        for(MultipartFile file:multipartFiles) {
            ProductReviewImage productReviewImage = new ProductReviewImage().
                    getEntity(productReview,s3Service.uploadFile(file));
            productReview.getProductReviewImages().add(productReviewImage);
        }
    }


    @NotNull
    private List<String> getProductReviewImgUrls(ProductReview productReview) {
        List<String> urls = new ArrayList<>();
        List<ProductReviewImage> productReviewImages = productReview.getProductReviewImages();
        if(productReviewImages!=null) {
            for(ProductReviewImage image : productReview.getProductReviewImages()) {
                urls.add(image.getImageUrl());
            }
        }
        return urls;
    }
    @NotNull
    private List<String> getStoreReviewImgUrls(StoreReview storeReview) {
        List<String> urls = new ArrayList<>();
        List<StoreReviewImage> storeReviewImages = storeReview.getStoreReviewImages();
        if(storeReviewImages!=null) {
            for (StoreReviewImage image : storeReview.getStoreReviewImages()) {
                urls.add(image.getImageUrl());
            }
        }
        return urls;
    }


}
