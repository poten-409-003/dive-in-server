package com.poten.dive_in.opengraph.controller;

import com.poten.dive_in.opengraph.dto.OpenGraphDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/openGraph")
public class OpenGraphController {

    @GetMapping("/fetch")
    public ResponseEntity<OpenGraphDTO> fetchOpenGraphData(@RequestParam String url) {
        OpenGraphDTO openGraphData = new OpenGraphDTO();

        try {
            // URL에서 OpenGraph 메타 데이터를 추출
            Document doc = Jsoup.connect(url).get();

            // OpenGraph 메타 태그 추출
            boolean hasOpenGraphTags = false;
            for (Element element : doc.select("meta[property^=og:]")) {
                String property = element.attr("property");
                String content = element.attr("content");

                switch (property) {
                    case "og:title":
                        openGraphData.setTitle(content);
                        break;
                    case "og:description":
                        openGraphData.setDescription(content);
                        break;
                    case "og:image":
                        openGraphData.setImage(content);
                        break;
                    case "og:url":
                        openGraphData.setUrl(content);
                        break;
                }
                hasOpenGraphTags = true;
            }

            // OpenGraph 태그가 없는 경우
            if (!hasOpenGraphTags) {
                openGraphData.setError("No OpenGraph meta tags found for the provided URL.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(openGraphData);
            }

            return ResponseEntity.ok(openGraphData);
        } catch (IOException e) {
            openGraphData.setError("Failed to connect to the provided URL.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(openGraphData);
        } catch (Exception e) {
            openGraphData.setError("An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(openGraphData);
        }
    }
}
