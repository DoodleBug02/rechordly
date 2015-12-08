package me.chrisvle.rechordly.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

//    static {
//        // Add some sample items.
////        for (int i = 1; i <= COUNT; i++) {
////            addItem(createDummyItem(i));
////        }
//        addItem(new DummyItem("Favorite Song", "03:32", makeDetails(1)));
//        addItem(new DummyItem("New Lyrics", "02:14", makeDetails(2)));
//        addItem(new DummyItem("Practice", "13:46", makeDetails(3)));
//        addItem(new DummyItem("Beatles Cover", "06:25", makeDetails(4)));
//        addItem(new DummyItem("Favorite Song 2", "03:33", makeDetails(5)));
//        addItem(new DummyItem("Song 3", "01:27", makeDetails(6)));
////        addItem(new DummyItem("ThisIsATestToSeeHowMuchTextBlah", "1234567890", makeDetails(6)));
//    }

    public static void addItem(DummyItem item) {
        for (int i = 0; i < ITEMS.size(); i++) {
            if (ITEMS.get(i).id.equals(item.id)) {
                ITEMS.remove(i);
                break;
            }
        }
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void delete(String name) {
        for (int i = 0; i < ITEMS.size(); i++) {
            if (ITEMS.get(i).id.equals(name)) {
                ITEMS.remove(i);
                ITEM_MAP.remove(name);
                break;
            }
        }
    }

//    private static DummyItem createDummyItem(int position) {
//        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
//    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
