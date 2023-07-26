package greeny.backend.domain.review.service;

import greeny.backend.config.aws.S3Service;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.repository.ProductRepository;
import greeny.backend.domain.review.dto.WriteReviewRequestDto;
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
import greeny.backend.exception.situation.StoreNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


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
            for(MultipartFile file:multipartFiles) {
                StoreReviewImage storeReviewImage = new StoreReviewImage().
                        getEntity(storeReview,s3Service.uploadFile(file));
                storeReview.getStoreReviewImages().add(storeReviewImage);
            }
        }

    }

    @Transactional
    public void writeProductReview(Long id, WriteReviewRequestDto writeReviewRequestDto,List<MultipartFile> multipartFiles,Member member) {
        Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFound());
        ProductReview productReview = productReviewRepository.save(writeReviewRequestDto.toProductReviewEntity(member,product));

        if(multipartFiles!=null){
            for(MultipartFile file:multipartFiles) {
                ProductReviewImage productReviewImage = new ProductReviewImage().
                        getEntity(productReview,s3Service.uploadFile(file));
                productReview.getProductReviewImages().add(productReviewImage);
            }
        }
    }


    public void deleteStoreReview(Long reviewId) {



        //image 삭제
        List<Long> files=storeReviewImageRepository.findByStoreReview_Id(reviewId);
        for(Long imgId:files) {storeReviewImageRepository.deleteById(imgId);}
        //리뷰 삭제
        storeReviewRepository.deleteById(reviewId);
    }
    public void deleteProductReview(Long reviewId) {
        //img 삭제
        List<Long> files=productReviewImageRepository.findByProductReview_Id(reviewId);
        for(Long imgId:files) {productReviewImageRepository.deleteById(imgId);}
        //리뷰 삭제
        productReviewRepository.deleteById(reviewId);
    }

}
