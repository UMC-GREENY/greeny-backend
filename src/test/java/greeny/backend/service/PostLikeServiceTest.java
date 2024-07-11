package greeny.backend.service;

import greeny.backend.domain.community.entity.Post;
import greeny.backend.domain.community.repository.PostLikeRepository;
import greeny.backend.domain.community.repository.PostRepository;
import greeny.backend.domain.community.service.PostLikeService;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.entity.Role;
import greeny.backend.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class PostLikeServiceTest {
    @Autowired
    PostLikeService postLikeService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostLikeRepository postLikeRepository;

    @Test
    void likeConcurrency() throws InterruptedException {
        // Given
        log.info("test start");
        Member savedWriter = memberRepository.save(createMember("asd123@naver.com"));
        Member savedLiker = memberRepository.save(createMember("fgh123@naver.com"));
        Post savedPost = postRepository.save(createPost(savedWriter));

        // When
        int numberOfThread = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThread);
        for (int i = 0; i < numberOfThread; i++) {
            executorService.submit(() -> {
                log.info("Current thread");
                postLikeService.like(savedPost.getId(), savedLiker);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // Then
        assertThat(postLikeRepository.existsByPostIdAndLikerId(savedPost.getId(), savedLiker.getId())).isTrue();
        memberRepository.deleteAll();
        postRepository.deleteAll();
        postLikeRepository.deleteAll();
        log.info("test end");
    }

    Member createMember(String email)  {
        return Member.builder()
                .email(email)
                .role(Role.ROLE_USER)
                .build();
    }

    Post createPost(Member writer) {
        return Post.builder()
                .writer(writer)
                .title("안녕")
                .content("반가워!")
                .hits(0)
                .build();
    }
}