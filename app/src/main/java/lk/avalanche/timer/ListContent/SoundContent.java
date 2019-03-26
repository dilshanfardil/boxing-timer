package lk.avalanche.timer.ListContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.avalanche.timer.db.Entity.Sound;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SoundContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<SoundItem> ITEMS = new ArrayList<SoundItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SoundItem> ITEM_MAP = new HashMap<String, SoundItem>();


    private static void addItem(SoundItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static SoundItem createItem(String str, boolean type) {
        return new SoundItem(str, str, str, type);
    }

    public static void makeListContent(List<Sound> sounds) {
        ITEM_MAP.clear();
        ITEMS.clear();
        for (Sound sound : sounds) {
            addItem(createItem(sound.getName(), sound.isType()));
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class SoundItem {
        public final String id;
        public final String content;
        public final String details;
        public final boolean type;

        public SoundItem(String id, String content, String details, boolean type) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.type = type;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
