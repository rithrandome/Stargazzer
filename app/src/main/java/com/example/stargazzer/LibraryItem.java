package com.example.stargazzer;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class LibraryItem {

    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public LibraryItem(Collection collection) {
        this.collection = collection;
    }

    public static class Collection{

        private String href;
        private List<Items> items;
        private final Metadata metadata;
        private final List<Links> links;
        private final String version;

        public Metadata getMetadata() {
            return metadata;
        }

        public String getVersion() {
            return version;
        }

        public List<Links> getLinks() {
            return links;
        }

        public String getHref() {
            return href;
        }

        public List<Items> getItems() {
            return items;
        }

        public Collection(String href, List<Items> items, Metadata metadata, String version, List<Links> links) {
            this.href = href;
            this.items = items;
            this.metadata = metadata;
            this.version = version;
            this.links = links;
        }

        public static class Items {

            private List<Data> data;
            private String href;
            private List<Links> links;

            public List<Data> getData() {
                return data;
            }

            public String getHref() {
                return href;
            }

            public List<Links> getLinks() {
                return links;
            }

            public static class Data {

                private String center;
                private Date date_created;
                private List<String> keywords;
                private String media_type;

                @SerializedName("nasa_id")
                private String nasa_id;

                @SerializedName("title")
                private String title;
                private String description;

                public String getCenter() {
                    return center;
                }

                public Date getDate_created() {
                    return date_created;
                }

                public List<String> getKeywords() {
                    return keywords;
                }

                public String getMedia_type() {
                    return media_type;
                }

                public String getNasa_id() {
                    return nasa_id;
                }

                public String getTitle() {
                    return title;
                }

                public String getDescription() {
                    return description;
                }

                public Data(String center, Date date_created, List<String> keywords, String media_type, String nasa_id, String title, String description) {
                    this.center = center;
                    this.date_created = date_created;
                    this.keywords = keywords;
                    this.media_type = media_type;
                    this.nasa_id = nasa_id;
                    this.title = title;
                    this.description = description;
                }
            }

            public static class Links{

                private String href;
                private String rel;
                private String render;

                public String getHref() {
                    return href;
                }

                public String getRel() {
                    return rel;
                }

                public String getRender() {
                    return render;
                }

                public Links(String href, String rel, String render) {
                    this.href = href;
                    this.rel = rel;
                    this.render = render;
                }
            }
        }

        public static class Links {

            private String href;
            private String rel;
            private String prompt;

            public String getHref() {
                return href;
            }

            public String getRel() {
                return rel;
            }

            public String getPrompt() {
                return prompt;
            }

            public Links(String href, String rel, String prompt) {
                this.href = href;
                this.rel = rel;
                this.prompt = prompt;
            }
        }

        public static class Metadata {

            private String total_hits;

            public String getTotal_hits() {
                return total_hits;
            }

            public Metadata(String total_hits) {
                this.total_hits = total_hits;
            }
        }
    }

}
