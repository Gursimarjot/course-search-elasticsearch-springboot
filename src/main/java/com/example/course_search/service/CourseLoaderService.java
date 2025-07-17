package com.example.course_search.service;

import com.example.course_search.model.CourseDocument;
import com.example.course_search.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseLoaderService {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void loadSampleCourses() {
        try {
            InputStream is = new ClassPathResource("sample-courses.json").getInputStream();
            List<CourseDocument> courses = objectMapper.readValue(is, new TypeReference<>() {});

            for (CourseDocument course : courses) {
                course.setSuggest(new Completion(Collections.singletonList(course.getTitle())));
            }

            courseRepository.saveAll(courses);
            System.out.println("✅ Indexed " + courses.size() + " courses into Elasticsearch.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
