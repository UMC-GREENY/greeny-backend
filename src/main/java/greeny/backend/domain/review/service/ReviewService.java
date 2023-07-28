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
import greeny.backend.exception.situation.ProductNotFound;
import greeny.backend.exception.situation.ReviewNotFound;
import greeny.backend.exception.situation.StoreNotFound;
import greeny.backend.exception.situation.TypeDoesntExistsException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public void writeStoreReview(Long id, WriteReviewRequestDto writeReviewRequestDto, List<MultipartFile> multipartFiles, Member member) {
        //storeReview 저장
        Store store = storeRepository.findById(id).orElseThrow(()->new StoreNotFound());
        StoreReview storeReview =storeReviewRepository.save(writeReviewRequestDto.toStoreReviewEntity(member, store));
        if(multipartFiles!=null){
            uploadFiles(multipartFiles,storeReview);
        }
    }
    @Transactional
    public void writeProductReview(Long id, WriteReviewRequestDto writeReviewRequestDto,List<MultipartFile> multipartFiles,Member member) {
        Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFound());
        ProductReview productReview = productReviewRepository.save(writeReviewRequestDto.toProductReviewEntity(member,product));
        if(multipartFiles!=null){
            uploadFiles(multipartFiles,productReview);
        }
    }



    @Transactional
    public Page<Object> getAllSimpleReviewInfos(String type, Pageable pageable) {
        if(type.equals("s")) {
            Page<StoreReview> pages = storeReviewRepository.findAll(pageable);
            return getStoreMap(pages);
        } else if(type.equals("p")) {
            Page<ProductReview> pages = productReviewRepository.findAll(pageable);
            return getProductMap(pages);
        } else throw new TypeDoesntExistsException();
    }

    @Transactional
    public Page<Object> getSimpleReviewInfos(String type,Long reviewId,Pageable pageable) {
        if(type.equals("s")) {
            StoreReview storeReview = storeReviewRepository.findById(reviewId).orElseThrow(()->new ReviewNotFound());
            Page<StoreReview> pages = storeReviewRepository.findStoreReviewsByStore(pageable,storeReview.getStore());
            return getStoreMap(pages);
        } else if(type.equals("p")) {
            ProductReview productReview = productReviewRepository.findById(reviewId).orElseThrow(()->new ReviewNotFound());
            Page<ProductReview> pages = productReviewRepository.findProductReviewsByProduct(pageable,productReview.getProduct());
            return getProductMap(pages);
        } else throw new TypeDoesntExistsException();
    }




    @Transactional
    public GetReviewInfoResponseDto getStoreReviewInfo(Long id) {
        StoreReview storeReview = storeReviewRepository.findById(id).orElseThrow(()->new ReviewNotFound());

        List<String> urls = new ArrayList<>();
        List<StoreReviewImage> storeReviewImages = storeReview.getStoreReviewImages();
        if(storeReviewImages!=null) {
            for (StoreReviewImage image : storeReview.getStoreReviewImages()) {
                urls.add(image.getImageUrl());
            }
        }
        return buildReviewInfoResponseDto
                (storeReview.getReviewer().getEmail(),storeReview.getCreatedAt(),storeReview.getStar(),
                storeReview.getContent(),urls);
    }
    @Transactional
    public GetReviewInfoResponseDto getProductReviewInfo(Long id) {
        ProductReview productReview = productReviewRepository.findById(id).orElseThrow(()->new ReviewNotFound());

        List<String> urls = new ArrayList<>();
        List<ProductReviewImage> productReviewImages = productReview.getProductReviewImages();
        if(productReviewImages!=null) {
            for(ProductReviewImage image : productReview.getProductReviewImages()) {
                urls.add(image.getImageUrl());
            }
        }
        return buildReviewInfoResponseDto
                (productReview.getReviewer().getEmail(),productReview.getCreatedAt(),productReview.getStar(),
                productReview.getContent(),urls);
    }



    @Transactional
    public void deleteStoreReview(Long reviewId) {
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
    public void deleteProductReview(Long reviewId) {
        //image 삭제 : s3에서 삭제 -> ProductReviewImage 삭제
        List<ProductReviewImage> reviewImages=productReviewImageRepository.findByProductReviewId(reviewId);
        if(reviewImages!=null) {
            for(ProductReviewImage img:reviewImages) {s3Service.deleteFile(img.getImageUrl());}
            productReviewImageRepository.deleteAll(reviewImages);
        }
        //review 삭제 : ProductReview 삭제
        productReviewRepository.deleteById(reviewId);
    }


    public GetReviewInfoResponseDto buildReviewInfoResponseDto(String email, String createdAt, Integer star, String content, List<String> urls) {
        return GetReviewInfoResponseDto.builder()
                .writerEmail(email)
                .createdAt(createdAt)
                .star(star)
                .content(content)
                .fileUrls(urls)
                .build();
    }


    public void uploadFiles(List<MultipartFile> multipartFiles,StoreReview storeReview) {
        for(MultipartFile file:multipartFiles) {
            StoreReviewImage storeReviewImage = new StoreReviewImage().
                    getEntity(storeReview,s3Service.uploadFile(file));
            storeReview.getStoreReviewImages().add(storeReviewImage);
        }
    }
    public void uploadFiles(List<MultipartFile> multipartFiles,ProductReview productReview) {
        for(MultipartFile file:multipartFiles) {
            ProductReviewImage productReviewImage = new ProductReviewImage().
                    getEntity(productReview,s3Service.uploadFile(file));
            productReview.getProductReviewImages().add(productReviewImage);
        }
    }



    @NotNull
    private Page<Object> getProductMap(Page<ProductReview> pages) {
        return pages.map(review ->
                GetReviewListResponseDto.builder()
                        .id(review.getId())
                        .createdAt(review.getCreatedAt())
                        .writerEmail(review.getReviewer().getEmail())
                        .star(review.getStar())
                        .content(review.getContent())
                        .existsFile(!review.getProductReviewImages().isEmpty())
                        .build());
    }
    @NotNull
    private Page<Object> getStoreMap(Page<StoreReview> pages) {
        return pages.map(review ->
                GetReviewListResponseDto.builder()
                        .id(review.getId())
                        .createdAt(review.getCreatedAt())
                        .writerEmail(review.getReviewer().getEmail())
                        .star(review.getStar())
                        .content(review.getContent())
                        .existsFile(!review.getStoreReviewImages().isEmpty())
                        .build());
    }

}
