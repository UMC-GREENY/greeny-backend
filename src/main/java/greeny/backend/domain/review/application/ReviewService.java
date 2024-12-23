package greeny.backend.domain.review.application;

import greeny.backend.infra.aws.S3Service;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.entity.ProductRepository;
import greeny.backend.domain.review.presentation.dto.GetReviewListResponseDto;
import greeny.backend.domain.review.presentation.dto.WriteReviewRequestDto;
import greeny.backend.domain.review.presentation.dto.GetReviewInfoResponseDto;
import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.review.entity.StoreReview;
import greeny.backend.domain.review.entity.ProductReviewRepository;
import greeny.backend.domain.review.entity.StoreReviewRepository;
import greeny.backend.domain.review.entity.ProductReviewImage;
import greeny.backend.domain.review.entity.StoreReviewImage;
import greeny.backend.domain.review.entity.ProductReviewImageRepository;
import greeny.backend.domain.review.entity.StoreReviewImageRepository;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.entity.StoreRepository;
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
import static greeny.backend.domain.Target.*;

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
        Store store = storeRepository.findById(id).orElseThrow(StoreNotFound::new);
        StoreReview storeReview = storeReviewRepository.save(writeReviewRequestDto.toStoreReviewEntity(member, store));
        if (multipartFiles != null) {
            uploadFiles(multipartFiles, storeReview);
        }
    }

    @Transactional
    public void writeProductReview(Long id, WriteReviewRequestDto writeReviewRequestDto, List<MultipartFile> multipartFiles, Member member) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFound::new);
        ProductReview productReview = productReviewRepository.save(writeReviewRequestDto.toProductReviewEntity(member, product));
        if (multipartFiles != null) {
            uploadFiles(multipartFiles, productReview);
        }
    }

    @Transactional(readOnly = true)
    public Page<GetReviewListResponseDto> getMemberReviewList(String type, Pageable pageable, Member member) {
        if (type.equals(STORE.toString())) {
            return storeReviewRepository.findAllByReviewer(pageable, member)
                    .map(storeReview -> GetReviewListResponseDto.toDetailStoreDto(storeReview, type, storeReview.getStore().getId()));
        }
        else if (type.equals(PRODUCT.toString())) {
            return productReviewRepository.findAllByReviewer(pageable, member)
                    .map(productReview -> GetReviewListResponseDto.toDetailProductDto(productReview, type, productReview.getProduct().getId()));
        }
        throw new TypeDoesntExistsException();
    }

    @Transactional(readOnly = true)
    public Page<GetReviewListResponseDto> searchSimpleReviewInfos(String keyword, String type, Pageable pageable) {
        if (!StringUtils.hasText(keyword)) {
            return getAllSimpleReviewInfos(type, pageable);
        }
        if (type.equals(STORE.toString())) {
            return storeReviewRepository.findAllByContentContainingIgnoreCase(keyword, pageable)
                    .map(storeReview -> GetReviewListResponseDto.toDetailStoreDto(storeReview, type, storeReview.getStore().getId()));
        }
        else if (type.equals(PRODUCT.toString())) {
            return productReviewRepository.findAllByContentContainingIgnoreCase(keyword, pageable)
                    .map(productReview -> GetReviewListResponseDto.toDetailProductDto(productReview, type, productReview.getProduct().getId()));
        }
        throw new TypeDoesntExistsException();
    }

    private Page<GetReviewListResponseDto> getAllSimpleReviewInfos(String type, Pageable pageable) {
        if (type.equals(STORE.toString())) {
            return storeReviewRepository.findAll(pageable)
                    .map(storeReview -> GetReviewListResponseDto.toDetailStoreDto(storeReview, type, storeReview.getStore().getId()));
        }
        else if (type.equals(PRODUCT.toString())) {
            return productReviewRepository.findAll(pageable)
                    .map(productReview -> GetReviewListResponseDto.toDetailProductDto(productReview, type, productReview.getProduct().getId()));
        }
        throw new TypeDoesntExistsException();
    }

    @Transactional(readOnly = true)
    public Page<GetReviewListResponseDto> getSimpleReviewInfos(String type, Long id, Pageable pageable) {
        if (type.equals(STORE.toString())) {
            Store store = storeRepository.findById(id).orElseThrow(StoreNotFound::new);
            Page<StoreReview> pages = storeReviewRepository.findStoreReviewsByStore(pageable, store);
            return pages.map(GetReviewListResponseDto::from);
        }
        else if (type.equals(PRODUCT.toString())) {
            Product product = productRepository.findById(id).orElseThrow(ProductNotFound::new);
            Page<ProductReview> pages = productReviewRepository.findProductReviewsByProduct(pageable, product);
            return pages.map(GetReviewListResponseDto::from);
        }
        else {
            throw new TypeDoesntExistsException();
        }
    }

    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getStoreReviewInfo(Long id) {
        StoreReview storeReview = storeReviewRepository.findById(id).orElseThrow(ReviewNotFound::new);
        List<String> urls = getStoreReviewImgUrls(storeReview);
        return buildReviewInfoResponseDto(storeReview.getReviewer().getEmail(), storeReview.getCreatedAt(), storeReview.getStar(),
                storeReview.getContent(), urls, false);
    }

    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getProductReviewInfo(Long id) {
        ProductReview productReview = productReviewRepository.findById(id).orElseThrow(ReviewNotFound::new);
        List<String> urls = getProductReviewImgUrls(productReview);
        return buildReviewInfoResponseDto(productReview.getReviewer().getEmail(), productReview.getCreatedAt(), productReview.getStar(),
                productReview.getContent(), urls, false);
    }

    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getStoreReviewInfoWithAuth(Long id, Member member) {
        StoreReview storeReview = storeReviewRepository.findById(id).orElseThrow(ReviewNotFound::new);
        boolean isWriter = storeReview.getReviewer().getEmail().equals(member.getEmail());
        List<String> urls = getStoreReviewImgUrls(storeReview);
        return buildReviewInfoResponseDto
                (storeReview.getReviewer().getEmail(), storeReview.getCreatedAt(), storeReview.getStar(),
                        storeReview.getContent(), urls, isWriter);
    }

    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getProductReviewInfoWithAuth(Long id, Member member) {
        ProductReview productReview = productReviewRepository.findById(id).orElseThrow(ReviewNotFound::new);
        boolean isWriter = productReview.getReviewer().getEmail().equals(member.getEmail());
        List<String> urls = getProductReviewImgUrls(productReview);
        return buildReviewInfoResponseDto
                (productReview.getReviewer().getEmail(), productReview.getCreatedAt(), productReview.getStar(),
                        productReview.getContent(), urls, isWriter);
    }

    @Transactional
    public void deleteStoreReview(Long reviewId, Member currentMember) {
        StoreReview storeReview = storeReviewRepository.findById(reviewId).orElseThrow(ReviewNotFound::new);
        if (!storeReview.getReviewer().getEmail().equals(currentMember.getEmail())) {
            throw new MemberNotEqualsException();
        }
        List<StoreReviewImage> reviewImages = storeReviewImageRepository.findByStoreReviewId(reviewId);
        if (reviewImages != null) {
            for (StoreReviewImage img : reviewImages) {
                s3Service.deleteFile(img.getImageUrl());
            }
            storeReviewImageRepository.deleteAll(reviewImages);
        }
        storeReviewRepository.deleteById(reviewId);
    }

    @Transactional
    public void deleteProductReview(Long reviewId, Member currentMember) {
        ProductReview productReview = productReviewRepository.findById(reviewId).orElseThrow(ReviewNotFound::new);
        if (!productReview.getReviewer().getId().equals(currentMember.getId())) {
            throw new MemberNotEqualsException();
        }
        List<ProductReviewImage> reviewImages = productReviewImageRepository.findByProductReviewId(reviewId);
        if (reviewImages != null) {
            for (ProductReviewImage img : reviewImages) {
                s3Service.deleteFile(img.getImageUrl());
            }
            productReviewImageRepository.deleteAll(reviewImages);
        }
        productReviewRepository.deleteById(reviewId);
    }

    public GetReviewInfoResponseDto buildReviewInfoResponseDto(String email, String createdAt, Integer star, String content, List<String> urls, boolean isWriter) {
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
    public void uploadFiles(List<MultipartFile> multipartFiles, StoreReview storeReview) {
        for (MultipartFile file : multipartFiles) {
            StoreReviewImage storeReviewImage = StoreReviewImage.getEntity(storeReview, s3Service.uploadFile(file));
            storeReview.getStoreReviewImages().add(storeReviewImage);
        }
    }

    @Transactional
    public void uploadFiles(List<MultipartFile> multipartFiles, ProductReview productReview) {
        for (MultipartFile file : multipartFiles) {
            ProductReviewImage productReviewImage = ProductReviewImage.getEntity(productReview, s3Service.uploadFile(file));
            productReview.getProductReviewImages().add(productReviewImage);
        }
    }

    @NotNull
    private List<String> getProductReviewImgUrls(ProductReview productReview) {
        List<String> urls = new ArrayList<>();
        List<ProductReviewImage> productReviewImages = productReview.getProductReviewImages();
        if (productReviewImages != null) {
            for (ProductReviewImage image : productReview.getProductReviewImages()) {
                urls.add(image.getImageUrl());
            }
        }
        return urls;
    }

    @NotNull
    private List<String> getStoreReviewImgUrls(StoreReview storeReview) {
        List<String> urls = new ArrayList<>();
        List<StoreReviewImage> storeReviewImages = storeReview.getStoreReviewImages();
        if (storeReviewImages != null) {
            for (StoreReviewImage image : storeReview.getStoreReviewImages()) {
                urls.add(image.getImageUrl());
            }
        }
        return urls;
    }
}