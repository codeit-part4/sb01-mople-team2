package com.sprint.mople.domain.review.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContentRatingSummaryDto {

  private UUID id;
  private String title;
  private double averageRating;
  private long reviewCount;
}
