package com.example.ecommerce_rest_api.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgBBUploadResponse {

    private boolean success;
    private int status;
    private ImgBBData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImgBBData {
        private String id;
        private String title;
        private String url_viewer;
        private String url;
        private String display_url;
        private String delete_url;
        private Long size;
        private String time;
        private String expiration;
        private Integer width;
        private Integer height;
        private ImgBBImage image;
        private ImgBBThumb thumb;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ImgBBImage {
            private String filename;
            private String name;
            private String mime;
            private String extension;
            private String url;
            private Integer width;
            private Integer height;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ImgBBThumb {
            private String filename;
            private String name;
            private String mime;
            private String extension;
            private String url;
            private Integer width;
            private Integer height;
        }
    }
}