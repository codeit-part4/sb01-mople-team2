package com.sprint.mople.domain.review.service;

import com.sprint.mople.domain.review.dto.ReviewResponse;
import com.sprint.mople.domain.review.entity.Review;
import com.sprint.mople.domain.review.repository.ReviewRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
  private final ReviewRepository reviewRepository;

   public ReviewResponse createReview(Review review) {
     return ReviewResponse.from(reviewRepository.save(review));
   }

   public List<Review> getAllReviews() {
     return reviewRepository.findAll();
   }

   public Optional<Review> getReviewById(UUID id) {
     return reviewRepository.findById(id);
   }

   public void deleteReview(UUID id) {
     reviewRepository.deleteById(id);
   }

}
