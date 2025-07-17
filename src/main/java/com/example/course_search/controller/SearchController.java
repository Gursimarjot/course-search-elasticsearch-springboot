package com.example.course_search.controller;

import com.example.course_search.model.CourseDocument;
import com.example.course_search.service.CourseSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final CourseSearchService courseSearchService;

    @GetMapping("/search")
    public Map<String, Object> searchCourses(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String startDate,
            @RequestParam(defaultValue = "upcoming") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ZonedDateTime startDateTime = null;
        if (startDate != null && !startDate.isBlank()) {
            startDateTime = ZonedDateTime.parse(startDate);
        }

        SearchHits<CourseDocument> hits = courseSearchService.searchCourses(
                q, minAge, maxAge, category, type,
                minPrice, maxPrice, startDateTime,
                sort, page, size
        );

        Map<String, Object> response = new HashMap<>();
        response.put("total", hits.getTotalHits());

        List<Map<String, Object>> courseList = new ArrayList<>();
        hits.forEach(hit -> {
            CourseDocument doc = hit.getContent();
            Map<String, Object> item = new HashMap<>();
            item.put("id", doc.getId());
            item.put("title", doc.getTitle());
            item.put("category", doc.getCategory());
            item.put("price", doc.getPrice());
            item.put("nextSessionDate", doc.getNextSessionDate());
            courseList.add(item);
        });

        response.put("courses", courseList);
        return response;
    }
}
