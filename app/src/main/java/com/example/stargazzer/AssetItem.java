package com.example.stargazzer;

import java.util.List;

public class AssetItem {

    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public AssetItem(Collection collection) {
        this.collection = collection;
    }

    public static class Collection{

        private String href;

        private List<Items> items;

        private String version;

        public String getHref() {
            return href;
        }

        public List<Items> getItems() {
            return items;
        }

        public String getVersion() {
            return version;
        }

        public static class Items {
            private final String href;

            public Items(String href) {
                this.href = href;
            }

            public String getHref() {
                return href;
            }
        }
    }


}
