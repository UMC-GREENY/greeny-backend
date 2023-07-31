package greeny.backend.domain.review.service;

import greeny.backend.config.aws.S3Service;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.repository.ProductRepository;
import greeny.backend.domain.review.dto.GetReviewListResponseDto;
import greeny.backend.domain.review.dto.WriteReviewRequestDto;
import greeny.backend.domain.review.dto.GetReviewInfoResponseDto;
import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.review.entity.ReviewComparator;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public Page<Object> getMemberReviewList(Pageable pageable, Member member) {

        List<StoreReview> storeList = storeReviewRepository.findStoreReviewsByReviewer(pageable,member).getContent();
        List<ProductReview> productList = productReviewRepository.findProductReviewsByReviewer(pageable,member).getContent();

        List<Object> combinedList = new ArrayList<>();
        combinedList.addAll(storeList);
        combinedList.addAll(productList);
        //Collections.sort(combinedList, new ReviewComparator());
        // or createdAt 변환해서 정렬?

        Page<Object> resultPages =  PageableExecutionUtils.getPage(combinedList, pageable,()->{
            long totalCount = Math.max(storeList.size(), productList.size());
            return totalCount;
        });
        return resultPages.map(this::processObject);

    }


    @Transactional(readOnly=true)
    public Page<Object> getAllSimpleReviewInfos(String type, Pageable pageable) {
        if(type.equals("s")) {
            Page<StoreReview> pages = storeReviewRepository.findAll(pageable);
            return pages.map(review -> toStoreReviewDTO(review));
        } else if(type.equals("p")) {
            Page<ProductReview> pages = productReviewRepository.findAll(pageable);
            return pages.map(review -> toProductReviewDTO(review));
        } else throw new TypeDoesntExistsException();
    }

    @Transactional(readOnly = true)
    public Page<Object> getSimpleReviewInfos(String type,Long id,Pageable pageable) {
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

    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getStoreReviewInfo(Long id) {  // TODO 다른 사용자의 리뷰 삭제를 방지하기 위해 현재 사용자와 리뷰 작성자 비교
        StoreReview storeReview = storeReviewRepository.findById(id).orElseThrow(ReviewNotFound::new);

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
    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getProductReviewInfo(Long id) {
        ProductReview productReview = productReviewRepository.findById(id).orElseThrow(ReviewNotFound::new);

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
        storeReviewRepository.findById(reviewId).orElseThrow(ReviewNotFound::new);
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
        productReviewRepository.findById(reviewId).orElseThrow(ReviewNotFound::new);
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


    private Object processObject(Object object) {
        // Store or Product : 타입에 따라 처리
        if (object instanceof StoreReview) {
            return toStoreReviewDTO((StoreReview) object);
        } else if (object instanceof ProductReview) {
            return toProductReviewDTO((ProductReview) object);
        } return null;
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


}
