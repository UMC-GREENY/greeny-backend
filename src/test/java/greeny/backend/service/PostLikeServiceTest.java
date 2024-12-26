package greeny.backend.service;

import greeny.backend.domain.community.entity.Post;
import greeny.backend.domain.community.entity.PostLikeRepository;
import greeny.backend.domain.community.entity.PostRepository;
import greeny.backend.domain.community.application.PostLikeService;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.entity.Role;
import greeny.backend.domain.member.entity.MemberRepository;
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
        Member savedWriter = memberRepository.save(createMember("asd123@naver.com"));
        Member savedLiker = memberRepository.save(createMember("fgh123@naver.com"));
        Post savedPost = postRepository.save(createPost(savedWriter));

        // When
        int numberOfThread = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThread);
        for (int i = 0; i < numberOfThread; i++) {
            executorService.submit(() -> {
                try {
                    postLikeService.like(savedPost.getId(), savedLiker);  // 실패 시 ConstraintViolationException 발생
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // Then
        assertThat(postLikeRepository.existsByPostIdAndLikerId(savedPost.getId(), savedLiker.getId())).isTrue();
        memberRepository.deleteAll();
        postRepository.deleteAll();
        postLikeRepository.deleteAll();
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
                .hasPostFile(false)
                .build();
    }
}