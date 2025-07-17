package com.example.course_search.service;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.Suggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSearchService {

    private final RestHighLevelClient client;

    public List<String> suggest(String prefix) {
        CompletionSuggestionBuilder suggestionBuilder = new CompletionSuggestionBuilder("suggest")
                .prefix(prefix)
                .size(10);

        SuggestBuilder suggestBuilder = new SuggestBuilder().addSuggestion("course-suggest", suggestionBuilder);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().suggest(suggestBuilder);
        SearchRequest searchRequest = new SearchRequest("courses").source(sourceBuilder);

        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            Suggest suggest = response.getSuggest();
            CompletionSuggestion completionSuggestion = suggest.getSuggestion("course-suggest");

            List<String> results = new ArrayList<>();
            for (CompletionSuggestion.Entry entry : completionSuggestion.getEntries()) {
                for (CompletionSuggestion.Entry.Option option : entry.getOptions()) {
                    results.add(option.getText().string());
                }
            }
            return results;
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<String> fuzzySearch(String keyword) {
        MatchQueryBuilder matchQuery = QueryBuilders
                .matchQuery("title", keyword)
                .fuzziness(Fuzziness.AUTO);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(matchQuery);
        SearchRequest searchRequest = new SearchRequest("courses").source(sourceBuilder);

        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            List<String> titles = new ArrayList<>();

            response.getHits().forEach(hit -> {
                String title = (String) hit.getSourceAsMap().get("title");
                if (title != null) {
                    titles.add(title);
                }
            });

            return titles;
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
